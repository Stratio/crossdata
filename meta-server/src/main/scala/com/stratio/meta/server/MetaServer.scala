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

package com.stratio.meta.server

import org.apache.commons.daemon.{DaemonContext, Daemon}
import org.apache.log4j.Logger
import com.stratio.meta2.core.engine.Engine
import akka.actor.ActorSystem
import akka.contrib.pattern.ClusterReceptionistExtension
import com.stratio.meta.server.config.ServerConfig
import com.stratio.meta2.server.actors.ServerActor

class MetaServer extends Daemon with ServerConfig{
  override lazy val logger = Logger.getLogger(classOf[MetaServer])

  lazy val engine = new Engine(engineConfig)
  // Create an Akka system
  lazy val system = ActorSystem(clusterName,config)

  override def destroy(): Unit = {

  }

  override def stop(): Unit = {
    system.shutdown()
    engine.shutdown()
    logger.info("Meta Server stop")
  }

  override def start(): Unit = {

  }

  override def init(p1: DaemonContext): Unit = {
    logger.info("Init Meta Server --- v0.0.5")
    val serverActor = system.actorOf(ServerActor.props(engine), actorName)
    ClusterReceptionistExtension(system).registerService(serverActor)
  }
}
