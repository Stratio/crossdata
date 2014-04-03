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
import com.stratio.meta.core.parser.Parser
import com.stratio.meta.common.result.{QueryResult, Result}

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
      sender ! QueryResult.CreateFailQueryResult("Not recognized object")
    }
  }

}
