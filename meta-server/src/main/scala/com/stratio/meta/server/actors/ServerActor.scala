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

package com.stratio.meta.server.actors

import akka.actor.{Props, ActorLogging, Actor}
import com.stratio.meta.common.result._
import com.stratio.meta.core.engine.Engine
import com.stratio.meta.common.ask.{Command, Connect, Query}
import scala.util.Random
import org.apache.log4j.Logger
import java.util.UUID
import com.stratio.meta.communication.Disconnect
import com.stratio.meta.common.ask.Connect
import com.stratio.meta.common.ask.Command
import com.stratio.meta.communication.Disconnect
import com.stratio.meta.common.ask.Query

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
      sender ! ConnectResult.createConnectResult(UUID.randomUUID().toString)
    }
    case Disconnect(user)=> {
      log.info("Goodbye " + user +".")
      //println("Welcome " + user +"!")
      sender ! DisconnectResult.createDisconnectResult(user)
    }
    case cmd: Command => {
      log.info("API Command call " + cmd.commandType)
      cmdActorRef forward cmd
    }
    case _ => {
      println("Unknown!!!!");
      sender ! Result.createUnsupportedOperationErrorResult("Not recognized object")
    }
  }
}
