package com.stratio.meta.server.actors

import akka.actor.{Props, ActorRef, ActorLogging, Actor}
import com.stratio.meta.core.parser.Parser
import com.stratio.meta.common.result.MetaResult

object ParserActor{
  def props(validator:ActorRef, parser:Parser): Props= Props(new ParserActor(validator,parser))
}

class ParserActor(validator:ActorRef, parser:Parser) extends Actor with TimeTracker with ActorLogging{
  override val timerName= this.getClass.getName

  def receive = {
    case query:String => {
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
