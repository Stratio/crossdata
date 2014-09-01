package com.stratio.meta.server.actors

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSelection, Props, ReceiveTimeout, RootActorPath }
import akka.cluster.ClusterEvent._
import akka.actor.RootActorPath
import akka.cluster.Cluster
import akka.actor.ReceiveTimeout
import com.stratio.meta2.core.query.InProgressQuery
import com.stratio.meta.communication._

object ConnectorActor {
  def props(): Props = Props(new ConnectorActor)
}

class ConnectorActor extends Actor with ActorLogging {

  log.info("levantado el connector actor")

  var connectorsMap: Map[String, ActorRef] = Map()

  def receive = {

    case MemberUp(member) =>
      println("Member is Up: " + member.toString + member.getRoles.toString())
      val memberActorRef = context.actorSelection(RootActorPath(member.address) / "user" / "clusterListener")
      //      connectorsMap += (member.toString -> memberActorRef)
      memberActorRef ! new Request("name")

    case state: CurrentClusterState =>
      log.info("Current members: {}", state.members.mkString(", "))

    case UnreachableMember(member) =>
      log.info("Member detected as unreachable: {}", member)

    case MemberRemoved(member, previousStatus) =>
      log.info("Member is Removed: {} after {}",
        member.address, previousStatus)

    case _: MemberEvent =>
      log.info("Receiving anything else")

    case _: ClusterDomainEvent =>
      println("ClusterDomainEvent")

    case ReceiveTimeout =>
      println("ReceiveTimeout")

    case toConnector: MetadataStruct =>
      connectorsMap(toConnector.connectorName) ! toConnector

    case toConnector: StorageQueryStruct =>
      connectorsMap(toConnector.connectorName) ! toConnector

    case toConnector: WorkflowStruct =>
      connectorsMap(toConnector.connectorName) ! toConnector

    case response: Response =>
      connectorsMap += (response.msg -> sender)

    case other =>
      println("connector actor receive event")
    //      sender ! "OK"
    //memberActorRef.tell(objetoConWorkflow, context.sender)
  }

}