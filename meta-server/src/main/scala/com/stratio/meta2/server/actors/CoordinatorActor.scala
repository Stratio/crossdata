package com.stratio.meta2.server.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.stratio.meta2.core.coordinator.Coordinator
import com.stratio.meta.common.executionplan._
import com.stratio.meta.common.result._
import com.stratio.meta.communication.ConnectToConnector
import com.stratio.meta.communication.DisconnectFromConnector
import com.stratio.meta2.core.metadata.MetadataManager
import com.stratio.meta2.core.query.{SelectPlannedQuery, StoragePlannedQuery, MetadataPlannedQuery, PlannedQuery}
import com.stratio.meta2.common.data.{FirstLevelName, CatalogName}

object CoordinatorActor {
  def props(connectorMgr: ActorRef, coordinator: Coordinator): Props = Props(new CoordinatorActor(connectorMgr, coordinator))
}

class CoordinatorActor(connectorMgr: ActorRef, coordinator: Coordinator) extends Actor with ActorLogging {
  log.info("Lifting coordinator actor")

  /**
   * Queries in progress.
   */
  //TODO Move this to infinispan
  val inProgress: scala.collection.mutable.Map[String, ExecutionWorkflow] = scala.collection.mutable.Map()
  val inProgressSender: scala.collection.mutable.Map[String, ActorRef] = scala.collection.mutable.Map()

  /**
   * Queries that trigger a persist operation once the result is returned.
   */
  //TODO Move this to infinispan
  val persistOnSuccess: scala.collection.mutable.Map[String, MetadataWorkflow] = scala.collection.mutable.Map()

  //TODO Move this to infinispan
  val pendingQueries: scala.collection.mutable.Map[FirstLevelName, String] = scala.collection.mutable.Map()

  def receive = {

    case plannedQuery: PlannedQuery => {
      val workflow = plannedQuery.getExecutionWorkflow()

      workflow match {
        case workflow: MetadataWorkflow => {
          val requestSender = sender
          val queryId = plannedQuery.asInstanceOf[MetadataPlannedQuery].getQueryId;
          inProgress.put(queryId, workflow)
          inProgressSender.put(queryId, requestSender)
          if(workflow.getActorRef != null){
            persistOnSuccess.put(queryId, workflow)
            workflow.getActorRef.asInstanceOf[ActorRef] ! workflow.getMetadataOperation(queryId)
          } else {
            pendingQueries.put(workflow.getCatalogMetadata.getName, queryId)
          }
        }

        case workflow: StorageWorkflow => {
          val requestSender = sender
          val queryId = plannedQuery
            .asInstanceOf[StoragePlannedQuery].getQueryId;
          inProgress.put(queryId, workflow)
          inProgressSender.put(queryId, requestSender)
          workflow.getActorRef.asInstanceOf[ActorRef] ! workflow.getStorageOperation(queryId)
        }

        case workflow: ManagementWorkflow => {
          val requestSender = sender
          val queryId = plannedQuery
            .asInstanceOf[MetadataPlannedQuery].getQueryId
          requestSender ! coordinator.executeManagementOperation(workflow.getManagementOperation(queryId))
        }

        case workflow: QueryWorkflow => {
          val requestSender = sender
          val queryId = plannedQuery.asInstanceOf[SelectPlannedQuery].getQueryId
          inProgress.put(queryId, workflow)
          inProgressSender.put(queryId, requestSender)
          if(ResultType.RESULTS.equals(workflow.getResultType)){
            workflow.getActorRef.asInstanceOf[ActorRef] ! workflow.getWorkflow
          }else if(ResultType.TRIGGER_EXECUTION.equals(workflow.getResultType)){
            //TODO Trigger next step execution.
            throw new UnsupportedOperationException("Trigger execution not supported")
          }
        }
        case _ =>{
          println("non recognized workflow")
        }
      }
    }

    case result: Result => {
      val queryId = result.getQueryId
      println("receiving result from "+sender+"; queryId="+queryId)
      if(persistOnSuccess.contains(queryId)){
        //TODO Trigger coordinator persist operation.
        persistOnSuccess.remove(queryId)
      }
      inProgress.remove(queryId)
      val initialSender = inProgressSender(queryId)
      inProgressSender.remove(queryId)
      initialSender ! result
    }

    case _: ConnectToConnector =>
      println("connecting to connector ")

    case _: DisconnectFromConnector =>
      println("disconnecting from connector")

    case _ => {
      sender ! Result.createUnsupportedOperationErrorResult("Not recognized object")
    }

  }

}
