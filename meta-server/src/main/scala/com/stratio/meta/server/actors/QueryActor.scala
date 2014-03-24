package com.stratio.meta.server.actors

import akka.actor.{ActorLogging, Actor}
import com.stratio.meta.communication.Query
import com.stratio.meta.common.result.MetaResult


class QueryActor extends Actor with ActorLogging{


  override def receive: Receive = {
    case Query(query:String) => {
      log.info("Init Parser Task")
      val timer=initTimer()
      validator forward parser.parseStatement(query)
      finishTimer(timer)
      log.info("Finish Parser Task")
    }
    case _ => {
      sender ! MetaResult.createMetaResultError("Not recognized object")
    }
  }
}
