/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.common.util.akka.keepalive

import akka.actor.{Actor, ActorRef, Cancellable}
import LiveMan.HeartBeat

import scala.concurrent.duration._

object LiveMan {
  case class HeartBeat[ID](sourceId: ID)
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
