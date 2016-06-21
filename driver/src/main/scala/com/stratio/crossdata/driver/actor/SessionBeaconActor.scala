package com.stratio.crossdata.driver.actor

import java.util.UUID

import akka.actor.{Actor, ActorRef, Props}
import akka.contrib.pattern.ClusterClient.SendToAll
import com.stratio.crossdata.common.util.akka.KeepAlive.LiveMan

import scala.concurrent.duration.FiniteDuration

object SessionBeaconActor {

  def props(
             sessionId: UUID,
             period: FiniteDuration,
             clusterClientActor: ActorRef,
             clusterPath: String): Props =
    Props(new SessionBeaconActor(sessionId, period, clusterClientActor, clusterPath))

}

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
