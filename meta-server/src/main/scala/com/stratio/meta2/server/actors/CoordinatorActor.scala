package com.stratio.meta2.server.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.stratio.meta2.core.coordinator.Coordinator
import com.stratio.meta.common.executionplan._
import com.stratio.meta.common.result._
import com.stratio.meta.communication.ConnectToConnector
import com.stratio.meta.communication.DisconnectFromConnector

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

  def receive = {

    case workflow: MetadataWorkflow => {
      val requestSender = sender
      inProgress.put(workflow.getQueryId, workflow)
      inProgressSender.put(workflow.getQueryId, requestSender)
      persistOnSuccess.put(workflow.getQueryId, workflow)
      workflow.getActorRef.asInstanceOf[ActorRef] ! workflow.getMetadataOperation
    }

    case workflow: StorageWorkflow => {
      val requestSender = sender
      inProgress.put(workflow.getQueryId, workflow)
      inProgressSender.put(workflow.getQueryId, requestSender)
      workflow.getActorRef.asInstanceOf[ActorRef] ! workflow.getStorageOperation
    }

    case workflow: ManagementWorkflow => {
      val requestSender = sender
      requestSender ! coordinator.executeManagementOperation(workflow.getManagementOperation)
    }

    case workflow: QueryWorkflow => {
      val requestSender = sender
      inProgress.put(workflow.getQueryId, workflow)
      inProgressSender.put(workflow.getQueryId, requestSender)
      if(ResultType.RESULTS.equals(workflow.getResultType)){
        workflow.getActorRef.asInstanceOf[ActorRef] ! workflow.getWorkflow
      }else if(ResultType.TRIGGER_EXECUTION.equals(workflow.getResultType)){
        //TODO Trigger next step execution.
        throw new UnsupportedOperationException("Trigger execution not supported")
      }
    }

    case result: Result => {
      val queryId = result.getQueryId
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
