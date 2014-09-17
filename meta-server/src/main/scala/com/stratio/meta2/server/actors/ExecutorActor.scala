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
import com.stratio.meta.common.actor.ActorResultListener
import com.stratio.meta.common.result.Result
import com.stratio.meta2.core.executor.Executor
import org.apache.log4j.Logger

object ExecutorActor{
  def props(connectorActor:ActorRef,executor:Executor): Props = Props(new ExecutorActor(connectorActor,executor))
}

class ExecutorActor(connectorActor:ActorRef,executor:Executor) extends Actor with TimeTracker with ActorResultListener{

  /**
   * Map that associates a query identifier with the sender that sent that query.
   */
  var senderMap : java.util.Map[String, ActorRef] = new java.util.HashMap[String, ActorRef]()

  val log =Logger.getLogger(classOf[ExecutorActor])
  override lazy val timerName: String = this.getClass.getName

  override def receive: Receive = {
    //TODO case toConnectorActor vs case toCommand
    /*
    case query:MetaQuery if query.getPlan.involvesStreaming() =>
      //Sender is mutable
      val querySender = sender
      senderMap.put(query.getQueryId, querySender)
      val result = executor.executeQuery(query, this).getResult
      processResults(result)
    case query:MetaQuery if !query.hasError=> {
      val querySender = sender
      log.info("Init Executor Task")
      val timer=initTimer()
      val result = executor.executeQuery(query, this).getResult
      querySender ! result
      finishTimer(timer)
      log.info("Finish Executor Task")
    }
    case query:MetaQuery if query.hasError=>{
      sender ! query.getResult
    }
    * 
    */
    case _ => {
      sender ! Result.createUnsupportedOperationErrorResult("Not recognized object")
    }
  }

  override def processResults(result: Result): Unit = {
    //val r = result.asInstanceOf[QueryResult]
    //System.out.println("####################################################################################3############################################## "
    //                   + "Sending partial results: " + !r.isLastResultSet + ", QID: " + result.getQueryId
    //                   + " page: " + r.getResultPage + " results: " + r.getResultSet.size());
    //System.out.println("####################################################################################3############################################## "
    //                   + "Sending partial results for QID: " + result.getQueryId);
    senderMap.get(result.getQueryId) ! result
  }
}
