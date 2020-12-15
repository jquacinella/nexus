package ch.epfl.bluebrain.nexus.delta.plugins.storage.storages.operations.s3

import akka.actor.ActorSystem
import akka.http.scaladsl.model.Uri
import akka.stream.alpakka.s3.{S3Attributes, S3Exception}
import akka.stream.alpakka.s3.scaladsl.S3
import akka.stream.scaladsl.Sink
import ch.epfl.bluebrain.nexus.delta.plugins.storage.storages.AkkaSource
import ch.epfl.bluebrain.nexus.delta.plugins.storage.storages.model.EncryptionState.Decrypted
import ch.epfl.bluebrain.nexus.delta.plugins.storage.storages.model.StorageValue.S3StorageValue
import ch.epfl.bluebrain.nexus.delta.plugins.storage.storages.operations.FetchFile
import ch.epfl.bluebrain.nexus.delta.plugins.storage.storages.operations.StorageFileRejection.FetchFileRejection
import ch.epfl.bluebrain.nexus.delta.plugins.storage.storages.operations.StorageFileRejection.FetchFileRejection._
import ch.epfl.bluebrain.nexus.delta.rdf.IriOrBNode.Iri
import monix.bio.Cause.Error
import monix.bio.IO

import java.net.URLDecoder
import java.nio.charset.StandardCharsets.UTF_8

final class S3StorageFetchFile(id: Iri, value: S3StorageValue[Decrypted])(implicit as: ActorSystem) extends FetchFile {

  private val s3Attributes = S3Attributes.settings(value.toAlpakkaSettings)

  override def apply(path: Uri.Path): IO[FetchFileRejection, AkkaSource] =
    IO.fromFuture(
      S3.download(value.bucket, URLDecoder.decode(path.toString, UTF_8.toString))
        .withAttributes(s3Attributes)
        .runWith(Sink.head)
    ).redeemCauseWith(
      {
        case Error(err: S3Exception) => IO.raiseError(UnexpectedFetchError(id, path.toString, err.toString()))
        case err                     => IO.raiseError(UnexpectedFetchError(id, path.toString, err.toThrowable.getMessage))
      },
      {
        case Some((source, _)) => IO.pure(source: AkkaSource)
        case None              => IO.raiseError(FileNotFound(id, path.toString()))
      }
    )
}
