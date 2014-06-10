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

import akka.actor.{ActorRef, Props, ActorLogging, Actor}
import com.stratio.meta.common.result.{QueryResult, Result}
import com.stratio.meta.core.engine.Engine
import com.stratio.meta.common.ask.Query
import org.apache.log4j.Logger

object QueryActor{
  def props(engine: Engine): Props = Props(new QueryActor(engine))
}

class QueryActor(engine: Engine) extends Actor{
  val log =Logger.getLogger(classOf[QueryActor])
  val executorActorRef = context.actorOf(ExecutorActor.props(engine.getExecutor),"ExecutorActor")
  val plannerActorRef = context.actorOf(PlannerActor.props(executorActorRef,engine.getPlanner),"PlanerActor")
  val validatorActorRef = context.actorOf(ValidatorActor.props(plannerActorRef,engine.getValidator),"ValidatorActor")
  val parserActorRef = context.actorOf(ParserActor.props(validatorActorRef,engine.getParser),"ParserActor")

  //var querySender : ActorRef = null;

  override def receive: Receive = {
    case Query(queryId, keyspace, statement, user) => {
      log.debug("User "+ user + " ks: "+ keyspace + " stmt: " + statement + " id: " + queryId)
      //querySender = sender
      parserActorRef forward Query(queryId, keyspace, statement, user)
      //parserActorRef ! Query(queryId, keyspace, statement, user)
      log.debug("Finish Query")
    }
    //case r: Result => {
    //  querySender ! r
    //}
    case _ => {
      println("Unknown message!")
      sender ! QueryResult.createFailQueryResult("Message not recognized")
  }
  }
}
