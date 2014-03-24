package com.stratio.meta.server.actors

import akka.actor.{ActorRef, ActorLogging, Actor}
import com.stratio.meta.core.parser.Parser
import com.stratio.meta.communication.Query
import com.stratio.meta.common.result.MetaResult

class ParserActor(validator:ActorRef, parser:Parser) extends Actor with TimeTracker with ActorLogging{
  override val timerName= this.getClass.getName

  def receive = {
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
