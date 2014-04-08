/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.server

import org.apache.commons.daemon.{DaemonContext, Daemon}
import com.stratio.meta.server.actors.ServerActor
import org.apache.log4j.Logger
import com.stratio.meta.core.engine.Engine
import akka.actor.ActorSystem
import akka.contrib.pattern.ClusterReceptionistExtension
import com.stratio.meta.server.config.ServerConfig

class MetaServer extends Daemon with ServerConfig{
  override lazy val logger = Logger.getLogger("ServerApplication")

  lazy val engine = new Engine(engineConfig)
  // Create an Akka system
  lazy val system = ActorSystem(clusterName,config)

  override def destroy(): Unit = {

  }

  override def stop(): Unit = {
    system.shutdown()
  }

  override def start(): Unit = {

  }

  override def init(p1: DaemonContext): Unit = {
    logger.info("Init Meta Server --- v0.0.4")
    val serverActor = system.actorOf(ServerActor.props(engine), actorName)
    ClusterReceptionistExtension(system).registerService(serverActor)
  }
}
