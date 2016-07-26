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

import akka.actor.{Actor, ActorRef, Cancellable}
import LiveMan.HeartBeat

import scala.concurrent.duration._

object LiveMan {
  private[keepalive] case class HeartBeat[ID](sourceId: ID)
}

/**
  * Stackable modifications trait to be mixed at those actors which to be monitored.
  */
trait LiveMan[ID] extends Actor {

  // Attributes to be implemented by the actors mixing this trait

  val master: ActorRef // Actor reference to the master (monitor) actor.
  val period: FiniteDuration // Duration of the time interval between two heartbeat messages.
  val keepAliveId: ID // Actor id in the KeepAlive schema.

  val initialDelay: FiniteDuration = 0 seconds

  // Internal implementation

  private var ticks: Option[Cancellable] = None
  protected lazy val tick: HeartBeat[ID] = HeartBeat(keepAliveId)

  protected def sendTick: Unit = {
    master ! tick
  }

  abstract override def preStart(): Unit = {
    super.preStart()
    ticks = Some {
      import context.dispatcher
      context.system.scheduler.schedule(initialDelay, period)(sendTick)
    }
  }

  abstract override def postStop(): Unit = {
    ticks.foreach(_.cancel)
    super.postStop()
  }

}
