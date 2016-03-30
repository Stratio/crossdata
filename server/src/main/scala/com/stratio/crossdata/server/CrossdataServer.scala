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

import java.io.File

import akka.actor.ActorSystem
import akka.actor.Props
import akka.cluster.Cluster
import akka.contrib.pattern.ClusterReceptionistExtension
import akka.routing.{DefaultResizer, RoundRobinPool}
import com.stratio.crossdata.server.actors.ServerActor
import com.stratio.crossdata.server.config.{ServerActorConfig, ServerConfig}
import org.apache.commons.daemon.Daemon
import org.apache.commons.daemon.DaemonContext
import org.apache.log4j.Logger
import org.apache.spark.sql.crossdata
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

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

    val metricsPath = Option(sparkParams.get("spark.metrics.conf"))

    val filteredSparkParams = metricsPath.fold(sparkParams)(m => checkMetricsFile(sparkParams, m.get))

    xdContext = {
      val sparkContext = new SparkContext(new SparkConf().setAll(filteredSparkParams))
      Some(new XDContext(sparkContext))
    }

    require(xdContext.isDefined, "Crossdata context must be started")

    //Check if the catalog is "on line"
    val ctx=xdContext.getOrElse(throw new RuntimeException("Crossdata context cannot be started"))
    require(ctx.checkCatalogConnection,"Crossdata Server cannot be started because there isn't a connection with the Catalog")

    system = Some(ActorSystem(clusterName, config))

    val serverActorConfig = ServerActorConfig(completedJobTTL, retryNoAttempts, retryCountWindow)

    system.fold(throw new RuntimeException("Actor system cannot be started")) { actorSystem =>
      val resizer = DefaultResizer(lowerBound = minServerActorInstances, upperBound = maxServerActorInstances)
      val serverActor = actorSystem.actorOf(
        RoundRobinPool(minServerActorInstances, Some(resizer)).props(
          Props(classOf[ServerActor],
            Cluster(actorSystem),
            xdContext.getOrElse(throw new RuntimeException("Crossdata context cannot be started")),
            serverActorConfig)),
        actorName)
      ClusterReceptionistExtension(actorSystem).registerService(serverActor)
    }
    logger.info(s"Crossdata Server started --- v${crossdata.CrossdataVersion}")
  }

  def checkMetricsFile(params: Map[String, String], metricsPath: String): Map[String, String] = {
    val metricsFile = new File(metricsPath)
    if(!metricsFile.exists){
      logger.warn(s"Metrics configuration file not found: ${metricsFile.getPath}")
      params - "spark.metrics.conf"
    } else {
      params
    }
  }

  override def stop(): Unit = {
    xdContext.foreach(_.sc.stop())
    system.foreach(_.shutdown())
    logger.info("Crossdata Server stopped")
  }

  override def destroy(): Unit = ()

}
