package com.stratio.meta.server.actors

import akka.actor.{Props, ActorLogging, Actor}
import com.stratio.meta.common.result.MetaResult
import com.stratio.meta.core.engine.Engine
import com.stratio.meta.common.ask.Query

object ServerActor{
  def props(engine: Engine): Props = Props(new ServerActor(engine))
}

class ServerActor(engine:Engine) extends Actor with ActorLogging {
  val queryActorRef= context.actorOf(QueryActor.props(engine),"QueryActor")
  def receive = {
    case query:Query => queryActorRef forward query
    case _ => {
      sender ! MetaResult.createMetaResultError("Not recognized object")
    }
  }
}
