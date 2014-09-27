package com.stratio.meta2.server.actors

import akka.actor.{Actor, ActorLogging, Props}
import com.stratio.meta2.core.engine.Engine

object NormalizerActor {
  def props(engine: Engine): Props = Props(new NormalizerActor(engine))
}

class NormalizerActor(engine: Engine) extends Actor with ActorLogging {

  log.info("Lifting normalizer actor")

  def receive = {
    case _ =>
      println("Normalizer Actor")
    //sender ! "OK"
    //memberActorRef.tell(objetoConWorkflow, context.sender)
  }
}
