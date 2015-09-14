/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.stratio.crossdata.driver

import java.util

import akka.actor.{ActorSelection, ActorSystem}
import akka.contrib.pattern.ClusterClient
import com.stratio.crossdata.driver.actor.ProxyActor
import com.stratio.crossdata.driver.config.DriverConfig
import com.stratio.crossdata.driver.utils.RetryPolitics
import com.typesafe.config.{ConfigValueFactory}
import org.apache.log4j.Logger

import collection.JavaConversions._

object Driver extends DriverConfig {
  override lazy val logger = Logger.getLogger(getClass)
}

class Driver(val seedNodes: java.util.List[String] = new util.ArrayList[String]()) {

  private lazy val logger = Driver.logger

  private val finalConfig = seedNodes match {
    case c if !c.isEmpty => Driver.config.withValue(
      "akka.cluster.seed-nodes",
      ConfigValueFactory.fromAnyRef(seedNodes))
    case _ => Driver.config
  }

  private val system = ActorSystem("CrossdataSystem", finalConfig)

  if(logger.isDebugEnabled){
    system.logConfiguration()
  }

  private val contactPoints = finalConfig.getStringList("akka.cluster.seed-nodes")
  private val initialContacts: Set[ActorSelection] = contactPoints.map(contact => system.actorSelection(contact)).toSet
  val clusterClientActor = system.actorOf(ClusterClient.props(initialContacts), "remote-client")

  val proxyActor = system.actorOf(ProxyActor.props(clusterClientActor, this), "proxy-actor")

  val retryPolitics: RetryPolitics = {
    new RetryPolitics
  }

  def send(s: String): String = {
    val result = retryPolitics.askRetry(proxyActor, "Ping")
    logger.info("Result: " + result)
    result
  }

}
