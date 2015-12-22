/**
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
package com.stratio.crossdata.server

import akka.actor.{ActorSystem, Props}
import akka.cluster.Cluster
import akka.contrib.pattern.ClusterReceptionistExtension
import akka.routing.RoundRobinPool
import com.stratio.crossdata.server.actors.ServerActor
import com.stratio.crossdata.server.config.ServerConfig
import org.apache.commons.daemon.{Daemon, DaemonContext}
import org.apache.log4j.Logger
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.{SparkConf, SparkContext}
import scala.collection.JavaConversions._


class CrossdataServer extends Daemon with ServerConfig {

  override lazy val logger = Logger.getLogger(classOf[CrossdataServer])

  var system: Option[ActorSystem] = None
  var xdContext: Option[XDContext] = None

  override def init(p1: DaemonContext): Unit = ()

  override def start(): Unit = {

    val sparkParams = config.entrySet()
      .map(e => (e.getKey, e.getValue.unwrapped().toString))
      .toMap
      .filterKeys(_.startsWith("config.spark"))
      .map(e => (e._1.replace("config.", ""), e._2))

    xdContext = {
      val sparkContext = new SparkContext(new SparkConf().setAll(sparkParams))
      Some(new XDContext(sparkContext))
    }

    require(xdContext.isDefined, "Crossdata context must be started")

    system = Some(ActorSystem(clusterName, config))

    system.fold(throw new RuntimeException("Actor system cannot be started")) { actorSystem =>
      // TODO resizer
      val serverActor = actorSystem.actorOf(
        RoundRobinPool(serverActorInstances).props(
          Props(classOf[ServerActor],
                Cluster(actorSystem),
                xdContext.getOrElse(throw new RuntimeException("Crossdata context cannot be started")))),
                actorName)
      ClusterReceptionistExtension(actorSystem).registerService(serverActor)
    }
    logger.info("Crossdata Server started --- v1.0.0")
  }

  override def stop(): Unit = {
    xdContext.foreach(_.sc.stop())
    system.foreach(_.shutdown())
    logger.info("Crossdata Server stopped")
  }

  override def destroy(): Unit = ()

}
