package ch.epfl.bluebrain.nexus.delta.plugins.storage

import akka.util.Timeout
import ch.epfl.bluebrain.nexus.delta.kernel.{IndexingConfig, RetryStrategyConfig}
import ch.epfl.bluebrain.nexus.delta.sdk.cache.KeyValueStoreConfig
import ch.epfl.bluebrain.nexus.delta.sdk.model.search.PaginationConfig
import ch.epfl.bluebrain.nexus.sourcing.processor.{EventSourceProcessorConfig, StopStrategyConfig}
import ch.epfl.bluebrain.nexus.sourcing.{config, SnapshotStrategyConfig}
import ch.epfl.bluebrain.nexus.sourcing.config.AggregateConfig
import org.scalatest.OptionValues

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

//TODO: ported from service module, we might want to avoid this duplication
trait ConfigFixtures extends OptionValues {

  implicit def ec: ExecutionContext

  def neverStop     = StopStrategyConfig(None, None)
  def neverSnapShot = SnapshotStrategyConfig(None, None, None).value

  def aggregate: AggregateConfig =
    config.AggregateConfig(stopStrategy = neverStop, snapshotStrategy = neverSnapShot, processor = processor)

  def processor: EventSourceProcessorConfig = EventSourceProcessorConfig(
    askTimeout = Timeout(5.seconds),
    evaluationMaxDuration = 3.second,
    evaluationExecutionContext = ec,
    stashSize = 100
  )

  def keyValueStore: KeyValueStoreConfig =
    KeyValueStoreConfig(
      askTimeout = 5.seconds,
      consistencyTimeout = 2.seconds,
      RetryStrategyConfig.AlwaysGiveUp
    )

  def indexing: IndexingConfig = IndexingConfig(1, RetryStrategyConfig.ConstantStrategyConfig(1.second, 10))

  def pagination: PaginationConfig =
    PaginationConfig(
      defaultSize = 30,
      sizeLimit = 100,
      fromLimit = 10000
    )
}
