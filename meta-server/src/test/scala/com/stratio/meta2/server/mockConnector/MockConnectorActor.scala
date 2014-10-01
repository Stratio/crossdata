/**
 * Created by carlos on 1/10/14.
 */


package com.stratio.connectors

import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import com.stratio.meta.common.executionplan.MetadataWorkflow
import com.stratio.meta.common.result.{ErrorResult, ErrorType, MetadataResult}
import com.stratio.meta.communication.{replyConnectorName, Execute, getConnectorName}
;


object State extends Enumeration {
  type state = Value
  val Started, Stopping, Stopped = Value
}

object MockConnectorActor {
  def props(): Props = Props(new MockConnectorActor())
}


class MockConnectorActor() extends Actor with ActorLogging {


  // subscribe to cluster changes, re-subscribe when restart

  override def preStart(): Unit = {
    //#subscribe
    Cluster(context.system).subscribe(self, classOf[MemberEvent])
    //cluster.subscribe(self, initialStateMode = InitialStateAsEvents, classOf[MemberEvent], classOf[UnreachableMember])
  }

  override def receive = {

    case s: String=>
       println("mock receiving string="+s)
    case _: com.stratio.meta.communication.Start =>
       println("start")
     case connectRequest: com.stratio.meta.communication.Connect =>
       println("connect")
     case _: com.stratio.meta.communication.Shutdown =>
       println("shutdown")
     case MemberUp(member) =>
       println("memberup")
     case state: CurrentClusterState =>
       println("currentclusterstate")
     case UnreachableMember(member) =>
       println("UnreachableMember")
     case MemberRemoved(member, previousStatus) =>
       println("MemverRemoved")
     case _: MemberEvent =>
       println("MemberEvent")

    case ex:Execute=>{
      println("execute")
      val result=MetadataResult.createSuccessMetadataResult()
      result.setQueryId(ex.queryId)
      sender ! result
    }

    //case metadataOp: MetadataOperation => {

    case metadataOp: MetadataWorkflow=> {
      println("metadataworkflow")
      val result=MetadataResult.createSuccessMetadataResult()
      result.setQueryId("myqueryId")
      sender ! result
    }

    case msg: getConnectorName => {
      println("getconnectorname")
       sender ! replyConnectorName("myConnector")
     }

    case _=> {
      println("non recogniced message")
      val err=new ErrorResult(ErrorType.EXECUTION,null)
      sender ! err
    }

  }
}