/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.server.actors

import akka.actor.{Props, ActorRef, ActorLogging, Actor}
import com.stratio.meta.core.utils.MetaQuery
import com.stratio.meta.core.validator.Validator
import org.apache.log4j.Logger
import com.stratio.meta.common.result.{Result, CommandResult}

object ValidatorActor{
  def props(planner:ActorRef, validator:Validator): Props= Props(new ValidatorActor(planner,validator))
}

class ValidatorActor(planner:ActorRef, validator:Validator) extends Actor with TimeTracker{
  val log= Logger.getLogger(classOf[ValidatorActor])
  override val timerName= this.getClass.getName

  override def receive: Receive = {
    case query:MetaQuery if !query.hasError=> {
      log.debug("Init Validator Task")
      val timer=initTimer()

      planner forward validator.validateQuery(query)
      finishTimer(timer)
      log.debug("Finish Validator Task")
    }
    case query:MetaQuery if query.hasError=>{
      sender ! query.getResult
    }
    case _ => {
      sender ! Result.createUnsupportedOperationErrorResult("Message not recognized")
    }
  }
}
