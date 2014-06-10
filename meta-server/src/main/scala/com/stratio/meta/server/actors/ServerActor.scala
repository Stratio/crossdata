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

package com.stratio.meta.server.actors

import akka.actor.{Props, ActorLogging, Actor}
import com.stratio.meta.common.result.{CommandResult, ConnectResult, QueryResult, Result}
import com.stratio.meta.core.engine.Engine
import com.stratio.meta.common.ask.{Command, Connect, Query}
import scala.util.Random
import org.apache.log4j.Logger

object ServerActor{
  def props(engine: Engine): Props = Props(new ServerActor(engine))
}

class ServerActor(engine:Engine) extends Actor {
  val log =Logger.getLogger(classOf[ServerActor])
  val queryActorRef= context.actorOf(QueryActor.props(engine),"QueryActor")
  val cmdActorRef= context.actorOf(APIActor.props(engine.getAPIManager),"APIActor")
  def receive = {
    case query:Query =>
      //println("query: " + query)
      queryActorRef forward query
    case Connect(user)=> {
      log.info("Welcome " + user +"!")
      //println("Welcome " + user +"!")
      sender ! ConnectResult.createSuccessConnectResult(Random.nextLong())
    }
    case cmd: Command => {
      log.info("API Command call " + cmd.commandType)
      cmdActorRef forward cmd
    }
    case _ => {
      println("Unknown!!!!");
      sender ! CommandResult.createFailCommandResult("Not recognized object")
    }
  }
}
