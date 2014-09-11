package com.stratio.meta2.server.actors

import akka.actor.{ Actor, ActorLogging, ActorRef, Props, ReceiveTimeout, RootActorPath }
import akka.cluster.ClusterEvent._
import com.stratio.meta.communication._
import com.stratio.meta2.core.query
import com.stratio.meta2.core.query.SelectPlannedQuery

object CoordinatorActor {
  def props(): Props = Props()
}

class CoordinatorActor extends Actor with ActorLogging {

  log.info("Lifting coordinator actor")

  var coordinatorsMap: Map[String, ActorRef] = Map()

  def receive = {

    case query: SelectPlannedQuery => {
      log.info("Connector Actor received InProgressQuery")
    }

    case _ =>
      println("coordinator actor receives event")
    //      sender ! "OK"
    //memberActorRef.tell(objetoConWorkflow, context.sender)
  }

}