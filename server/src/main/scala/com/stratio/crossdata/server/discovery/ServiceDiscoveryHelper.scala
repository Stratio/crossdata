/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

  val ClusterPrefix = Option(config.getString("catalog.prefix"))

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
