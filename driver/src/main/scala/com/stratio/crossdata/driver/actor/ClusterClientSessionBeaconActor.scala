package com.stratio.crossdata.driver.actor

import java.util.UUID

import akka.actor.{Actor, ActorRef, Props}
import akka.cluster.client.ClusterClient.SendToAll
import com.stratio.crossdata.common.util.akka.keepalive.LiveMan

import scala.concurrent.duration.FiniteDuration

object ClusterClientSessionBeaconActor {

  def props(
             sessionId: UUID,
             period: FiniteDuration,
             clusterClientActor: ActorRef,
             clusterPath: String): Props =
    Props(new ClusterClientSessionBeaconActor(sessionId, period, clusterClientActor, clusterPath))

}

/**
  * This actor is used by the driver provide the cluster with proof of life for the current session.
  * Check [[LiveMan]] for more details.
  */
class ClusterClientSessionBeaconActor private(
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
