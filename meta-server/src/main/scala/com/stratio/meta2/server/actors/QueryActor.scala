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

package com.stratio.meta2.server.actors

import akka.actor.{Actor, ActorRef, Props}
import com.stratio.meta.common.ask.Query
import com.stratio.meta.common.result.Result
import com.stratio.meta.core.engine.Engine
import org.apache.log4j.Logger

object QueryActor{
  def props(engine: Engine,connectorActorRef:ActorRef): Props = Props(new QueryActor(engine,connectorActorRef))

}

class QueryActor(engine: Engine,connectorActorRef:ActorRef) extends Actor{
  val log =Logger.getLogger(classOf[QueryActor])
  //val executorActorRef = context.actorOf(ExecutorActor.props(connectorActorRef,engine.getExecutor),"ExecutorActor")
  //val plannerActorRef = context.actorOf(PlannerActor.props(executorActorRef,engine.getPlanner),"PlanerActor")
  //val validatorActorRef = context.actorOf(ValidatorActor.props(plannerActorRef,engine.getValidator),"ValidatorActor")
  //val parserActorRef = context.actorOf(ParserActor.props(validatorActorRef,engine.getParser),"ParserActor")

  //var querySender : ActorRef = null;

  override def receive: Receive = {
    case Query(queryId, catalog, statement, user) => {
      log.info("User "+ user + " catalog: "+ catalog + " stmt: " + statement + " id: " + queryId)
      //querySender = sender
      //parserActorRef forward Query(queryId, catalog, statement, user)
      //parserActorRef ! Query(queryId, catalog, statement, user)
      log.info("Finish Query")
    }
    //case r: Result => {
    //  querySender ! r
    //}
    case _ => {
      println("Unknown message!")
      sender ! Result.createUnsupportedOperationErrorResult("Message not recognized")
  }
  }
}
