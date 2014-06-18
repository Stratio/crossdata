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

import akka.actor.{ActorRef, Props, Actor, ActorLogging}
import com.stratio.meta.core.utils.{Tree, MetaPath, MetaQuery}
import com.stratio.meta.core.executor.Executor
import org.apache.log4j.Logger
import com.stratio.meta.common.result.{Result, QueryResult}
import java.util
import scala.util
import com.stratio.meta.common.actor.ActorResultListener

object ExecutorActor{
  def props(executor:Executor): Props = Props(new ExecutorActor(executor))
}

class ExecutorActor(executor:Executor) extends Actor with TimeTracker with ActorResultListener{

  /**
   * Map that associates a query identifier with the sender that sent that query.
   */
  var senderMap : java.util.Map[String, ActorRef] = new java.util.HashMap[String, ActorRef]()


  val log =Logger.getLogger(classOf[ExecutorActor])
  override val timerName: String = this.getClass.getName

  override def receive: Receive = {

    case query:MetaQuery if query.getPlan.involvesStreaming() =>
      senderMap.put(query.getQueryId, sender)
      val result = executor.executeQuery(query, this).getResult
      processResults(result)
      senderMap.remove(query.getQueryId)
    case query:MetaQuery if !query.hasError=> {
      log.debug("Init Executor Task")
      val timer=initTimer()
      val result = executor.executeQuery(query, this).getResult
      sender ! result
      finishTimer(timer)
      log.debug("Finish Executor Task")
    }
    case query:MetaQuery if query.hasError=>{
      sender ! query.getResult
    }
    case _ => {
      sender ! Result.createUnsupportedOperationErrorResult("Not recognized object")
    }
  }

  override def processResults(result: Result): Unit = {
    val r = result.asInstanceOf[QueryResult]
    //System.out.println("####################################################################################3############################################## "
    //                   + "Sending partial results: " + !r.isLastResultSet + ", QID: " + result.getQueryId
    //                   + " page: " + r.getResultPage + " results: " + r.getResultSet.size());
    senderMap.get(result.getQueryId) ! result
  }
}
