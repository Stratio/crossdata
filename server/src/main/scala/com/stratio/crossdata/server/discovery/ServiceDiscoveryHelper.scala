package com.stratio.crossdata.server.discovery

import com.hazelcast.config.{Config => HzConfig}
import com.typesafe.config.Config
import org.apache.curator.framework.CuratorFramework
import org.apache.log4j.Logger
import org.apache.spark.sql.crossdata.config.CoreConfig
import scala.concurrent.Future
import scala.util.Try

object ServiceDiscoveryConfigHelper extends CoreConfig {

  override val logger: Logger = Logger.getLogger(this.getClass)

  val ClusterPrefix = Try(config.getString("catalog.prefix")).toOption

  ClusterPrefix.foreach{ p =>
    logger.info(s"Cluster prefix for service discovery: $p")
  }

  val ServiceDiscoveryPrefix = "service-discovery"
  val ServiceDiscoveryUrl = "url"
  val ServiceDiscoveryDefaultUrl = "127.0.0.1:2181"
  val RetrySleep = 1000
  val Retries = 3
  val SubscriptionPrefix = "cluster-subscription"
  val SubscriptionPath = s"$SubscriptionPrefix.path"
  val DefaultSubscriptionPath = s"/stratio/crossdata${ClusterPrefix.fold("")(p => s"/$p")}/discovery/subscription/leader"
  val SubscriptionTimeoutPath = s"$SubscriptionPrefix.timeout"
  val DefaultSubscriptionTimeout = 30
  val ClusterLeaderPrefix = "cluster-leader"
  val ClusterLeaderPath = s"$ClusterLeaderPrefix.path"
  val DefaultClusterLeaderPath = s"/stratio/crossdata${ClusterPrefix.fold("")(p => s"/$p")}/discovery/cluster/leader"
  val SeedsPath = "seeds"
  val DefaultSeedsPath = s"/stratio/crossdata${ClusterPrefix.fold("")(p => s"/$p")}/discovery/seeds"
  val DefaultSeedNodes = "akka.tcp://CrossdataServerCluster@127.0.0.1:13420"
  val ClusterDelayPath = s"$ClusterLeaderPrefix.scheduler.delay"
  val DefaultClusterDelay = 300

  val ProviderPrefix = "provider"
  val ProviderPath = s"$ProviderPrefix.path"
  val DefaultProviderPath = s"/stratio/crossdata${ClusterPrefix.fold("")(p => s"/$p")}/discovery/provider/members"

}

case class ServiceDiscoveryConfigHelper(sdConfig: Config) {

  def getOrElse(path: String, default: => String) = {
    Try(sdConfig.getString(path)).getOrElse(default)
  }

}

case class ServiceDiscoveryHelper(
                                   curatorClient: CuratorFramework,
                                   finalConfig: Config,
                                   hzConfig: HzConfig,
                                   leadershipFuture: Future[Unit],
                                   sdch: ServiceDiscoveryConfigHelper)
