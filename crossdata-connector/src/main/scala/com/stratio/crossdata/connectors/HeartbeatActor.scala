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

package com.stratio.crossdata.connectors

import java.util.concurrent.{TimeUnit, Executors}

import akka.actor.Actor
import com.stratio.crossdata.communication.HeartbeatSig


trait HeartbeatActor extends Actor {

  private val scheduler = Executors.newSingleThreadScheduledExecutor()

  private val callback = new Runnable {
    def run = {
      self ! new HeartbeatSig()
    }
  }

  scheduler.scheduleAtFixedRate(callback, 0, 500, TimeUnit.MILLISECONDS)

  def receive: Receive = {
    case heartbeat: HeartbeatSig =>  handleHeartbeat(heartbeat)
  }

  def handleHeartbeat(heartbeat: HeartbeatSig) = {
    println("HeartbeatActor receives a heartbeat message")
  }


}
