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

package com.stratio.crossdata.server.mocks

import akka.actor.{Actor, Props}
import com.stratio.crossdata.common.result.{Result, QueryStatus}
import com.stratio.crossdata.communication.ACK
import com.stratio.crossdata.core.query.{IValidatedQuery, MetadataValidatedQuery, SelectValidatedQuery}
import org.apache.log4j.Logger
import com.stratio.crossdata.server.actors.TimeTracker

object MockPlannerActor {
  def props(): Props = Props(new MockPlannerActor())
}

class MockPlannerActor() extends Actor with TimeTracker {
  override lazy val timerName = this.getClass.getName
  val log = Logger.getLogger(classOf[MockPlannerActor])

  def receive : Receive = {
    case query: MetadataValidatedQuery => {
      val ack = ACK(query.getQueryId, QueryStatus.PLANNED)
      sender ! ack
    }
    case query: SelectValidatedQuery => {
      val ack = ACK(query.getQueryId, QueryStatus.PLANNED)
      sender ! ack
    }
    case query: IValidatedQuery => {
      val ack = ACK(query.getQueryId, QueryStatus.PLANNED)
      sender ! ack
    }
    case _ => {
      sender ! Result.createUnsupportedOperationErrorResult("Not recognized object")
    }
  }

}
