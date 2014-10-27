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

package com.stratio.crossdata.server.actors

import akka.actor.{Actor, ActorRef, Props}
import com.stratio.crossdata.common.exceptions.PlanningException
import com.stratio.crossdata.common.result.Result
import com.stratio.crossdata.core.planner.Planner
import com.stratio.crossdata.core.query.{MetadataValidatedQuery, SelectValidatedQuery, StorageValidatedQuery}
import org.apache.log4j.Logger

object PlannerActor {
  def props(executor: ActorRef, planner: Planner): Props = Props(new PlannerActor(executor, planner))
}

class PlannerActor(coordinator: ActorRef, planner: Planner) extends Actor with TimeTracker {
  override lazy val timerName = this.getClass.getName
  val log = Logger.getLogger(classOf[PlannerActor])

  def receive = {
    //case query: ValidatedQuery => {
    case query: MetadataValidatedQuery => {
      log.info("\n\nGetting MetadataValidatedQuery; sending ack to " + sender + "\n\n")
      val timer = initTimer()
      try {
        val planned = planner.planQuery(query)
        finishTimer(timer)
        coordinator forward planned
      } catch {
        case pe:PlanningException => {
          log.error("Planning error: " + pe.getMessage + " from sender: " + sender.toString())
          val errorResult = Result.createErrorResult(pe)
          errorResult.setQueryId(query.getQueryId)
          sender ! errorResult
        }
        case e:Exception =>{
          log.error("Planning error: " + e.getMessage + " from sender: " + sender.toString())
        }
      }
      /*val ack = ACK(query.getQueryId, QueryStatus.PLANNED)
      sender ! ack*/
    }
    case query: SelectValidatedQuery => {
      log.info("\n\nGetting SelectValidatedQuery; sending ack to " + sender + "\n\n")
      try {
        val planned = planner.planQuery(query)
        log.info("\n\nplanner actor Sending " + planned.getClass + " to " + coordinator+"\n\n")
        coordinator forward planned
      } catch {
        case pe:PlanningException => {
          val errorResult = Result.createErrorResult(pe)
          log.error("Planning error: " + pe.getMessage + " from sender: " + sender.toString())
          errorResult.setQueryId(query.getQueryId)
          sender ! errorResult
        }
      }
      /*val ack = ACK(query.getQueryId, QueryStatus.PLANNED)
      sender ! ack*/
    }

    case query: StorageValidatedQuery => {
      log.info("\n\nGetting StorageValidatedQuery; sending ack to " + sender + "\n\n")
      val timer = initTimer()
      try {
        val planned = planner.planQuery(query)
        finishTimer(timer)
        coordinator forward planned
      } catch {
        case pe:PlanningException => {
          val errorResult = Result.createErrorResult(pe)
          log.error("Planning error: " + pe.getMessage + " from sender: " + sender.toString())
          errorResult.setQueryId(query.getQueryId)
          sender ! errorResult
        }
      }
      /*val ack = ACK(query.getQueryId, QueryStatus.PLANNED)
      sender ! ack*/
    }

    case _ => {
      sender ! Result.createUnsupportedOperationErrorResult("Not recognized object")
    }
  }

}
