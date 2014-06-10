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

import akka.actor.{Props, Actor, ActorLogging}
import com.stratio.meta.core.utils.MetaQuery
import com.stratio.meta.core.executor.Executor
import org.apache.log4j.Logger
import com.stratio.meta.common.result.QueryResult

object ExecutorActor{
  def props(executor:Executor): Props = Props(new ExecutorActor(executor))
}

class ExecutorActor(executor:Executor) extends Actor with TimeTracker{
  val log =Logger.getLogger(classOf[ExecutorActor])
  override val timerName: String = this.getClass.getName

  override def receive: Receive ={
    case query:MetaQuery if !query.hasError=> {
      log.debug("Init Executor Task")
      val timer=initTimer()
      val result = executor.executeQuery(query).getResult
      sender ! result
      finishTimer(timer)
      log.debug("Finish Executor Task")
    }
    case query:MetaQuery if query.hasError=>{
      sender ! query.getResult
    }
    case _ => {
      sender ! QueryResult.createFailQueryResult("Not recognized object")
    }
  }
}
