package com.stratio.meta.server.actors

import akka.actor.{ Props, ActorRef, ActorLogging, Actor, ActorSelection }
import akka.cluster.ClusterEvent._
import akka.actor.RootActorPath
import akka.cluster.Cluster
import akka.actor.ReceiveTimeout

object ConnectorActor2 {
  def props(): Props = Props(new ConnectorActor2())
}

class ConnectorActor2 extends Actor with ActorLogging {

  log.info("levantado el connector actor")

  var connectorsMap: Map[String, ActorSelection] = Map()

  override def preStart(): Unit = {
    //#subscribe
    //cluster.subscribe(self, initialStateMode = InitialStateAsEvents, classOf[MemberEvent], classOf[UnreachableMember])
  }
  override def postStop(): Unit =
    Cluster(context.system).unsubscribe(self)

  def receive = {
    case other =>
      println("connector actor receive event")
    //      sender ! "OK"
    //memberActorRef.tell(objetoConWorkflow, context.sender)
  }

}