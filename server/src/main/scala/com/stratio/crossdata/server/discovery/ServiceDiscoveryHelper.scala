package com.stratio.crossdata.server.discovery

import com.typesafe.config.Config
import org.apache.curator.framework.CuratorFramework
import org.apache.curator.framework.recipes.leader.LeaderLatch

import scala.concurrent.Promise
import scala.util.Try

object ServiceDiscoveryConfigHelper {
  val ServiceDiscoveryPrefix = "service-discovery"
  val ServiceDiscoveryUrl = "url"
  val ServiceDiscoveryDefaultUrl = "127.0.0.1:2181"
  val RetrySleep = 1000
  val Retries = 3
  val SubscriptionPrefix = "cluster-subscription"
  val SubscriptionPath = s"$SubscriptionPath.path"
  val DefaultSubscriptionPath = "/crossdata/discovery/subscription/leader"
  val SubscriptionTimeoutPath = s"$SubscriptionPath.timeout"
  val DefaultSubscriptionTimeout = 10
  val ClusterLeaderPrefix = "cluster-leader"
  val ClusterLeaderPath = s"$SubscriptionPath.path"
  val DefaultClusterLeaderPath = "/crossdata/discovery/cluster/leader"
  val SeedsPath = "seeds"
  val DefaultSeedsPath = "/crossdata/discovery/seeds"
  val DefaultSeedNodes = "akka.tcp://CrossdataServerCluster@127.0.0.1:13420"
  val ClusterDelayPath = s"$ClusterLeaderPath.scheduler.delay"
  val DefaultClusterDelay = 10
}

case class ServiceDiscoveryConfigHelper(sdConfig: Config) {

  def get[T](path: String, default: T): T = {
    Try(sdConfig.getString(path)).getOrElse(default).asInstanceOf[T]
  }

}

case class ServiceDiscoveryHelper(
    curatorClient: CuratorFramework,
    finalConfig: Config,
    leadershipPromise: Promise[Unit],
    clusterLeader: LeaderLatch,
    sdch: ServiceDiscoveryConfigHelper){
}
