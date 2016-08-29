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
package com.stratio.crossdata.server

import java.io.File

import akka.actor.{ActorSystem, Props}
import akka.cluster.Cluster
import akka.cluster.client.ClusterClientReceptionist
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.routing.{DefaultResizer, RoundRobinPool}
import akka.stream.ActorMaterializer
import com.stratio.crossdata.common.util.akka.keepalive.KeepAliveMaster
import com.stratio.crossdata.server.actors.{ResourceManagerActor, ServerActor}
import com.stratio.crossdata.server.config.ServerConfig
import org.apache.commons.daemon.{Daemon, DaemonContext}
import org.apache.log4j.Logger
import org.apache.spark.sql.crossdata
import org.apache.spark.sql.crossdata.session.{BasicSessionProvider, HazelcastSessionProvider, XDSessionProvider}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.JavaConversions._
import scala.concurrent.Future


class CrossdataServer extends ServerConfig {

  override lazy val logger = Logger.getLogger(classOf[CrossdataServer])

  var system: Option[ActorSystem] = None
  var sessionProviderOpt: Option[XDSessionProvider] = None
  var bindingFuture: Option[Future[ServerBinding]] = None

  def start(): Unit = {

    val sparkParams = config.entrySet()
      .map(e => (e.getKey, e.getValue.unwrapped().toString))
      .toMap
      .filterKeys(_.startsWith("config.spark"))
      .map(e => (e._1.replace("config.", ""), e._2))

    val metricsPath = Option(sparkParams.get("spark.metrics.conf"))

    val filteredSparkParams = metricsPath.fold(sparkParams)(m => checkMetricsFile(sparkParams, m.get))

    val sparkContext = new SparkContext(new SparkConf().setAll(filteredSparkParams))

    sessionProviderOpt = Some {
      if (isHazelcastEnabled)
        new HazelcastSessionProvider(sparkContext, config)
      else
        new BasicSessionProvider(sparkContext, config)
    }

    val sessionProvider = sessionProviderOpt.getOrElse(throw new RuntimeException("Crossdata Server cannot be started because there is no session provider"))


    system = Some(ActorSystem(clusterName, config))


    system.fold(throw new RuntimeException("Actor system cannot be started")) { actorSystem =>
      val resizer = DefaultResizer(lowerBound = minServerActorInstances, upperBound = maxServerActorInstances)
      val serverActor = actorSystem.actorOf(
        RoundRobinPool(minServerActorInstances, Some(resizer)).props(
          Props(
            classOf[ServerActor],
            Cluster(actorSystem),
            sessionProvider)),
        actorName)

      val clientMonitor = actorSystem.actorOf(KeepAliveMaster.props(serverActor), "client-monitor")
      ClusterClientReceptionist(actorSystem).registerService(clientMonitor)

      val resourceManagerActor = actorSystem.actorOf(ResourceManagerActor.props(Cluster(actorSystem), sessionProvider))
      ClusterClientReceptionist(actorSystem).registerService(serverActor)
      ClusterClientReceptionist(actorSystem).registerService(resourceManagerActor)

      //TODO
      /*implicit val httpSystem = actorSystem
      implicit val materializer = ActorMaterializer()
      val httpServerActor = new CrossdataHttpServer(config, serverActor, actorSystem)
      val host = config.getString(ServerConfig.Host)
      val port = config.getInt(ServerConfig.HttpServerPort)
      bindingFuture = Option(Http().bindAndHandle(httpServerActor.route, host, port))*/
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

  /**
    * Just for test purposes
    */
  def stop(): Unit = {
    sessionProviderOpt.foreach(_.close())

    sessionProviderOpt.foreach(_.sc.stop())

    system.foreach { actSystem =>
      implicit val exContext = actSystem.dispatcher
      bindingFuture.foreach { bFuture =>
        bFuture.flatMap(_.unbind()).onComplete(_ => actSystem.shutdown())
      }
    }

    logger.info("Crossdata Server stopped")
  }

}
