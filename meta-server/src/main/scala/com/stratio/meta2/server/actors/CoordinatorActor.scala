package com.stratio.meta2.server.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.stratio.meta2.core.coordinator.Coordinator
import com.stratio.meta2.core.query.SelectPlannedQuery

object CoordinatorActor {
  def props(connector: ActorRef, coordinator: Coordinator): Props = Props(new CoordinatorActor(connector, coordinator))
}

class CoordinatorActor(connector: ActorRef, coordinator: Coordinator) extends Actor with ActorLogging {

  log.info("Lifting coordinator actor")

  var coordinatorsMap: Map[String, ActorRef] = Map()

  def receive = {

    case query: SelectPlannedQuery => {
      log.info("CoordinatorActor Received SelectPlannedQuery")
      println() // doesn't print log.info if this statement is not written
      connector forward query
    }
    case query: PlannedQuery => {
      log.info("CoordinatorActor Received PlannedQuery")
      println() // doesn't print log.info if this statement is not written

      connector forward coordinator.coordinate(query)
    }
      /*
    case query: PlannedQuery => {
      log.info("CoordinatorActor received PlannedQuery")
      println("CoordinatorActor received PlannedQuery")
      connector forward query
    }
    */

    case _ =>
      sender ! "KO"
      log.info("coordinator actor receives something it doesn't understand ")
    //memberActorRef.tell(objetoConWorkflow, context.sender)
  }

}