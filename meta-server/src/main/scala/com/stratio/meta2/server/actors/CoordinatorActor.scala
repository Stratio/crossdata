package com.stratio.meta2.server.actors

import akka.actor.{ Actor, ActorLogging, ActorRef, Props }
import com.stratio.meta2.core.coordinator.Coordinator
import com.stratio.meta2.core.query.{ StoragePlannedQuery, MetadataPlannedQuery, PlannedQuery, SelectPlannedQuery }
import com.stratio.meta2.core.query.MetadataInProgressQuery
import com.stratio.meta2.core.query.InProgressQuery
import com.stratio.meta.communication.ACK
import com.stratio.meta.common.result.QueryStatus

object CoordinatorActor {
  def props(connector: ActorRef, coordinator: Coordinator): Props = Props(new CoordinatorActor(connector, coordinator))
}

class CoordinatorActor(connector: ActorRef, coordinator: Coordinator) extends Actor with ActorLogging {

  log.info("Lifting coordinator actor")

  var coordinators: scala.collection.mutable.Map[String, ActorRef] = scala.collection.mutable.Map()
  var queriesToPersist: scala.collection.mutable.Map[String, PlannedQuery] = scala.collection.mutable.Map()

  def receive = {

    case query: SelectPlannedQuery => {
      log.info("CoordinatorActor Received SelectPlannedQuery")
      println() // doesn't print log.info if this statement is not written
      //connector forward query
      connector ! coordinator.coordinate(query)
    }
    /*
     * Puts the query into a map if it's required to persist metadata 
     */
    case query: MetadataPlannedQuery => {
      log.info("CoordinatorActor Received MetadataPlannedQuery")
      println() // doesn't print log.info if this statement is not written
      //connector forward query
      val inProgress: InProgressQuery = coordinator.coordinate(query)
      if (inProgress != null) {
        queriesToPersist.put(inProgress.getQueryId(), inProgress)
        connector ! coordinator.coordinate(query)
      }
    }
    case query: StoragePlannedQuery => {
      log.info("CoordinatorActor Received StoragePlannedQuery")
      println() // doesn't print log.info if this statement is not written
      //connector forward query
      connector ! coordinator.coordinate(query)
    }
    /*
     * When Connector answers Coordinator with ACK message with EXECUTED status, coordinator persists the metadata through MDManager
     * and removes the query from the map
     */
    case connectorAck: ACK => {
      if (connectorAck.status == QueryStatus.EXECUTED) {
        log.info(connectorAck.queryId + " executed")
        coordinator.persist(queriesToPersist(connectorAck.queryId))
        queriesToPersist.remove(connectorAck.queryId)
      }
    }
    case _ =>
      sender ! "KO"
      log.info("coordinator actor receives something it doesn't understand ")
    //memberActorRef.tell(objetoConWorkflow, context.sender)
  }

}