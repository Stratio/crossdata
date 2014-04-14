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

import akka.actor.{Props, ActorLogging, Actor, ActorRef}
import com.stratio.meta.core.planner.Planner
import com.stratio.meta.core.utils.MetaQuery
import org.apache.log4j.Logger

object PlannerActor{
  def props(executor:ActorRef, planner:Planner): Props =Props(new PlannerActor(executor,planner))
}

class PlannerActor(executor:ActorRef, planner:Planner) extends Actor with TimeTracker {
  val log =Logger.getLogger(classOf[PlannerActor])
  override val timerName= this.getClass.getName
  def receive = {
    case query:MetaQuery if !query.hasError=> {
      log.info("Init Planner Task")
      val timer=initTimer()

      executor forward planner.planQuery(query)
      finishTimer(timer)
      log.info("Finish Planner Task")
    }
    case query:MetaQuery if query.hasError=>{
      sender ! query.getResult
    }
  }

}
