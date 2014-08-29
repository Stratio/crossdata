package com.stratio.meta.server.actors

import akka.actor.{ Props, ActorRef, ActorLogging, Actor, ActorSelection }
import akka.cluster.ClusterEvent._
import akka.actor.RootActorPath
import akka.cluster.Cluster
import akka.actor.ReceiveTimeout

object ConnectorActor {
  def props(): Props = Props(new ConnectorActor())
}

class ConnectorActor extends Actor with ActorLogging {

  log.info("levantado el connector actor")

  var connectorsMap: Map[String, ActorSelection] = Map()

  override def preStart(): Unit = {
    //#subscribe
    Cluster(context.system).subscribe(self, classOf[MemberEvent])
    //cluster.subscribe(self, initialStateMode = InitialStateAsEvents, classOf[MemberEvent], classOf[UnreachableMember])
  }
  override def postStop(): Unit =
    Cluster(context.system).unsubscribe(self)

  def receive = {
    
    case MemberUp(member) =>
    println("Member is Up: " + member.toString + member.getRoles.toString())
    val memberActorRef = context.actorSelection(RootActorPath(member.address) / "user" / "clusterListener")
    connectorsMap += (member.toString -> memberActorRef)
    memberActorRef ! "hola pichi, estás metaregistrado"

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

    case other =>
      println("connector actor receive event")
    //      sender ! "OK"
    //memberActorRef.tell(objetoConWorkflow, context.sender)
  }

}