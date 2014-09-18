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

import akka.actor.{Actor, Props}
import com.stratio.meta.common.ask.Command
import com.stratio.meta.common.result.Result
import com.stratio.meta2.core.api.APIManager
import org.apache.log4j.Logger

object APIActor{
  def props(metadata: APIManager): Props = Props(new APIActor(metadata))
}

class APIActor(metadata: APIManager) extends Actor with TimeTracker {
  val log =Logger.getLogger(classOf[APIActor])
  override lazy val timerName= this.getClass.getName

  def receive = {
    case cmd:Command => {
      log.info("command received "+cmd.toString)
      //val timer = initTimer()
      //sender ! metadata.processRequest(cmd)
      //finishTimer(timer)
    }
    case _ => {
      log.info("command _ received ")
      sender ! Result.createUnsupportedOperationErrorResult("Unsupported command")
    }
  }
}
