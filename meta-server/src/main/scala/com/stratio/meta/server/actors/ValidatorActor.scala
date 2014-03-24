package com.stratio.meta.server.actors

import akka.actor.{ActorRef, ActorLogging, Actor}
import com.stratio.meta.core.utils.MetaQuery
import com.stratio.meta.core.validator.Validator

class ValidatorActor(planner:ActorRef, validator:Validator) extends Actor with TimeTracker with ActorLogging{
  override val timerName= this.getClass.getName

  override def receive: Receive = {
    case query:MetaQuery if !query.hasError=> {
      log.info("Init Validator Task")
      val timer=initTimer()

      planner forward validator.validateQuery(query)
      finishTimer(timer)
      log.info("Finish Validator Task")
    }
    case query:MetaQuery if query.hasError=>{
      sender ! query.getResult
    }
  }
}
