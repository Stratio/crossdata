/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.crossdata.server

import akka.actor.ActorSystem
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import akka.contrib.pattern.ClusterReceptionistExtension

import akka.util.Timeout
import com.stratio.crossdata.server.actors. ServerActor
import com.stratio.crossdata.server.config.ServerConfig

import org.apache.commons.daemon.{Daemon, DaemonContext}
import org.apache.log4j.Logger
import scala.concurrent.duration._


class CrossdataServer extends Daemon with ServerConfig {
  override lazy val logger = Logger.getLogger(classOf[CrossdataServer])


  // Create an Akka system
  lazy val system = ActorSystem(clusterName, config)

  //LoadWatcherManager.MANAGER.clear()
  val cluster = Cluster(system)



  override def destroy(): Unit = {

  }

  override def stop(): Unit = {
    system.shutdown()
    logger.info("Crossdata Server stop")
  }

  override def start(): Unit = {

  }

  override def init(p1: DaemonContext): Unit = {
    logger.info("Init Crossdata Server --- v1.0.0")
    val serverActor = system.actorOf(ServerActor.props(cluster), actorName)
    ClusterReceptionistExtension(system).registerService(serverActor)

    implicit val timeout = Timeout(5.seconds)


  }
}
