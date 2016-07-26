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

package com.stratio.crossdata.common.util.akka.keepalive

import akka.actor.{Actor, ActorRef, Props}
import LiveMan.HeartBeat
import KeepAliveMaster.{DoCheck, HeartbeatLost}

import scala.concurrent.duration.FiniteDuration

object KeepAliveMaster {

  /**
    * Upon this message reception, the [[KeepAliveMaster]] actor'll start checking whether heartbeats from any actor providing
    * the same id are being received at each period.
    *
    * @param id expected to be verified at each period.
    * @param period for which heartbeats, from a specific id, are expected to be received at least once before
    *               raising miss alarms for that id.
    * @param continueMonitoring `true` if the target actor should still be monitored after misses. Otherwise
    *                          it'll be forgotten by the master actor.
    */
  case class DoCheck[ID](id: ID,
                         period: FiniteDuration,
                         continueMonitoring: Boolean = false)

  /**
    * Message sent to the monitor client when a heartbeat has been lost.
    *
    * @param id Identifier of the faulty monitored actor.
    */
  case class HeartbeatLost[T](id: T)

  def props[ID](client: ActorRef): Props =
    Props(new KeepAliveMaster[ID](client))

}

/**
  * Master actor, that is, an actor able to monitor heartbeats.
  */
class KeepAliveMaster[ID](client: ActorRef) extends Actor {

  def receive: Receive = receive(Set.empty)

  def receive(pending: Set[ID]): Receive = {

    case HeartBeat(id: ID @unchecked) =>
      context.become(receive(pending - id))

    case m @ DoCheck(id: ID @unchecked, period, continue) =>
      import context.dispatcher

      val missing = pending contains id

      if (missing) client ! HeartbeatLost(id)

      if (!missing || continue) {
        context.system.scheduler.scheduleOnce(period, self, m)
        context.become(receive(pending + id))
      }

  }

}
