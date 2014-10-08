/**
 * Created by carlos on 1/10/14.
 */


package com.stratio.meta2.server.mocks

import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._


object State extends Enumeration {
  type state = Value
  val Started, Stopping, Stopped = Value
}

object MockConnectorManagerActor {
  def props(): Props = Props(new MockConnectorManagerActor())
}


class MockConnectorManagerActor() extends Actor with ActorLogging {


  // subscribe to cluster changes, re-subscribe when restart

  override def preStart(): Unit = {
    //#subscribe
    Cluster(context.system).subscribe(self, classOf[MemberEvent])
    //cluster.subscribe(self, initialStateMode = InitialStateAsEvents, classOf[MemberEvent], classOf[UnreachableMember])
  }

  override def receive = {
    case _ =>{
      //sender ! Result.createUnsupportedOperationErrorResult("Not recognized object")
      sender ! "ko"
    }
  }
}