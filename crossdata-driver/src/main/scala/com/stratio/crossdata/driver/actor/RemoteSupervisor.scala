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

package com.stratio.crossdata.driver.actor

import akka.actor._
import akka.contrib.pattern.ClusterClient
import akka.actor.OneForOneStrategy
import akka.actor.SupervisorStrategy.{Resume, Stop}
import scala.concurrent.duration._

object RemoteSupervisor {
  def props(initialContacts: Set[ActorSelection]): Props = Props(new RemoteSupervisor(initialContacts))
}

class RemoteSupervisor(initialContacts: Set[ActorSelection]) extends Actor {
  val clusterClient = context.actorOf(ClusterClient.props(initialContacts), "remote-client")

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
      case _: StashOverflowException => Stop
      case _: Any => Resume
    }

  override def receive: Actor.Receive = {
    case message: Any => clusterClient forward message
  }

}
