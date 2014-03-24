package com.stratio.meta.server.actors

import akka.actor.{Actor, ActorLogging}
import com.stratio.meta.core.utils.MetaQuery
import com.stratio.meta.core.executor.Executor


class ExecutorActor(executor:Executor) extends Actor with TimeTracker with ActorLogging{
  override val timerName: String = this.getClass.getName

  override def receive: Receive ={
    case query:MetaQuery if !query.hasError=> {
      log.info("Init Planner Task")
      val timer=initTimer()
      sender ! executor.executeQuery(query)
      finishTimer(timer)
      log.info("Finish Planner Task")
    }
    case query:MetaQuery if query.hasError=>{
      sender ! query.getResult
    }
  }
}
