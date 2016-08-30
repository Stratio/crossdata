/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stratio.crossdata.driver.actor

import java.util.UUID

import akka.actor.{Actor, ActorRef, Props}
import akka.cluster.client.ClusterClient.SendToAll
import com.stratio.crossdata.common.util.akka.keepalive.LiveMan

import scala.concurrent.duration.FiniteDuration

object SessionBeaconActor {

  def props(
             sessionId: UUID,
             period: FiniteDuration,
             clusterClientActor: ActorRef,
             clusterPath: String): Props =
    Props(new SessionBeaconActor(sessionId, period, clusterClientActor, clusterPath))

}

/**
  * This actor is used by the driver provide the cluster with proof of life for the current session.
  * Check [[LiveMan]] for more details.
  */
class SessionBeaconActor private (
                     override val keepAliveId: UUID,
                     override val period: FiniteDuration,
                     clusterClientActor: ActorRef,
                     clusterPath: String) extends Actor with LiveMan[UUID] {

  override def receive: Receive = PartialFunction.empty
  override val master: ActorRef = clusterClientActor

  override protected def sendTick: Unit = {
    clusterClientActor ! SendToAll(clusterPath, tick)
  }

}
