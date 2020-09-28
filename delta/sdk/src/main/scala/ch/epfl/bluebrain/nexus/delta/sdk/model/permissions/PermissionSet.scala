package ch.epfl.bluebrain.nexus.delta.sdk.model.permissions

import cats.implicits._
import ch.epfl.bluebrain.nexus.delta.rdf.IriOrBNode.BNode
import ch.epfl.bluebrain.nexus.delta.rdf.jsonld.context.RawJsonLdContext
import ch.epfl.bluebrain.nexus.delta.rdf.jsonld.{JsonLd, JsonLdEncoder}
import ch.epfl.bluebrain.nexus.delta.rdf.{RdfError, Vocabulary}
import io.circe.generic.semiauto._
import io.circe.syntax._
import io.circe.{Decoder, Encoder}
import monix.bio.{IO, UIO}

/**
  * A wrapper for a collection of permissions
  */
final case class PermissionSet(permissions: Set[Permission]) extends AnyVal

object PermissionSet {

  implicit final val permissionSetEncoder: Encoder.AsObject[PermissionSet] = deriveEncoder
  implicit final val permissionSetDecoder: Decoder[PermissionSet]          = deriveDecoder

  implicit final val permissionSetJsonLdEncoder: JsonLdEncoder[PermissionSet] =
    new JsonLdEncoder[PermissionSet] {

      override def apply(value: PermissionSet): IO[RdfError, JsonLd] =
        JsonLd.compactedUnsafe(value.asJsonObject, defaultContext, BNode.random).pure[UIO]

      override val defaultContext: RawJsonLdContext = RawJsonLdContext(Vocabulary.contexts.permissions.asJson)
    }

}