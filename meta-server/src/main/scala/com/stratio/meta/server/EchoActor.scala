package com.stratio.meta.server

import akka.actor.{Props, ActorLogging, Actor}
import com.stratio.meta.communication.{Reply, Query}


class EchoActor extends Actor with ActorLogging{

  override def receive: Actor.Receive = {
    case Query(result) =>{
      log.info("==========>>>> " + result)
      sender ! Reply(result)
    }
  }
}
