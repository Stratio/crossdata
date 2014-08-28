package com.stratio.meta.server.actors

import akka.actor.{ Props, ActorRef, ActorLogging, Actor, ActorSelection }
import akka.cluster.ClusterEvent.MemberUp
import akka.actor.RootActorPath
import org.apache.log4j.Logger

object ConnectorActor {
  def props(): Props = new Props()
}

class ConnectorActor extends Actor with TimeTracker {
  /**
   * Class logger.
   */
  val log = Logger.getLogger(classOf[ValidatorActor])

  override lazy val timerName: String = this.getClass.getName

  var connectorsMap: Map[String, ActorSelection] = Map()

  override def receive: Receive = {
    //case _ =>
    // sender ! "OK"
    //memberActorRef.tell(objetoConWorkflow, context.sender)

    case MemberUp(member) =>
      log.info("**********************************Member is Up: {}", member.toString, member.getRoles.toString())
      val memberActorRef = context.actorSelection(RootActorPath(member.address) / "user" / "clusterListener")
      connectorsMap += (member.toString -> memberActorRef)
      memberActorRef ! "hola pichi, estás metaregistrado"
  }

}