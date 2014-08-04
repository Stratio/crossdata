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
 */package com.stratio.meta.server.actors

import akka.actor.{Props, ActorLogging, Actor, ActorRef}
import com.stratio.meta.core.planner.Planner
import com.stratio.meta.core.utils.MetaQuery
import org.apache.log4j.Logger
import com.stratio.meta.communication.ACK
import com.stratio.meta.common.result.{Result, QueryResult, QueryStatus}

object PlannerActor{
  def props(executor:ActorRef, planner:Planner): Props =Props(new PlannerActor(executor,planner))
}

class PlannerActor(executor:ActorRef, planner:Planner) extends Actor with TimeTracker {
  val log =Logger.getLogger(classOf[PlannerActor])
  override lazy val timerName= this.getClass.getName
  def receive = {
    case query:MetaQuery if !query.hasError=> {
      log.debug("Init Planner Task")
      val timer=initTimer()

      val ack = ACK(query.getQueryId, QueryStatus.PLANNED)
      //println("Sending ack: " + ack)
      sender ! ack
      //println("Execute the plan");
      executor forward planner.planQuery(query)
      finishTimer(timer)
      log.debug("Finish Planner Task")
    }
    case query:MetaQuery if query.hasError=>{
      sender ! query.getResult
    }
    case _ => {
      sender ! Result.createUnsupportedOperationErrorResult("Not recognized object")
    }
  }

}
