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
import com.stratio.meta.common.ask.Query
import org.apache.log4j.Logger

object ParserActor{
  def props(validator:ActorRef, parser:Parser): Props= Props(new ParserActor(validator,parser))
}

class ParserActor(validator:ActorRef, parser:Parser) extends Actor with TimeTracker {
  val log =Logger.getLogger(classOf[ParserActor])
  override val timerName= this.getClass.getName

  def receive = {
    case Query(queryId, keyspace, statement, user) => {
      log.debug("Init Parser Task")
      val timer=initTimer()
      val stmt = parser.parseStatement(statement)
      stmt.setQueryId(queryId)
      if(!stmt.hasError){
        stmt.setSessionKeyspace(keyspace)
      }
      validator forward stmt
      finishTimer(timer)
      log.debug("Finish Parser Task")
    }
    case _ => {
      sender ! QueryResult.createFailQueryResult("Not recognized object")
    }
  }

}
