/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta.server.actors

import akka.actor.{Props, ActorRef, ActorLogging, Actor}
import com.stratio.meta2.core.parser.Parser
import com.stratio.meta.common.result.{QueryResult, Result}
import com.stratio.meta.common.ask.Query
import org.apache.log4j.Logger

object ParserActor{
  def props(validator:ActorRef, parser:Parser): Props= Props(new ParserActor(validator,parser))
}

class ParserActor(validator:ActorRef, parser:Parser) extends Actor with TimeTracker {
  val log =Logger.getLogger(classOf[ParserActor])
  override lazy val timerName= this.getClass.getName

  def receive = {
    case Query(queryId, catalog, statement, user) => {
      log.debug("Init Parser Task")
      val timer=initTimer()
      val stmt = parser.parseStatement(queryId, catalog, statement)
      stmt.setQueryId(queryId)
      if(!stmt.hasError){
        stmt.setSessionCatalog(catalog)
      }
      validator forward stmt
      finishTimer(timer)
      log.debug("Finish Parser Task")
    }
    case _ => {
      sender ! Result.createUnsupportedOperationErrorResult("Not recognized object")
    }
  }

}
