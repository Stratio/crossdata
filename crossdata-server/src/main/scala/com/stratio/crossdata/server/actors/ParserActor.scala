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

package com.stratio.crossdata.server.actors

import akka.actor.{Actor, ActorRef, Props}
import com.stratio.crossdata.common.ask.Query
import com.stratio.crossdata.common.data.CatalogName
import com.stratio.crossdata.common.exceptions.ParsingException
import com.stratio.crossdata.common.result.Result
import com.stratio.crossdata.core.parser.Parser
import com.stratio.crossdata.core.query.BaseQuery
import org.apache.log4j.Logger
import com.stratio.crossdata.common.metrics.TimeTracker
import com.codahale.metrics.Timer

object ParserActor {
  def props(validator: ActorRef, parser: Parser): Props = Props(new ParserActor(validator, parser))
}

class ParserActor(validator: ActorRef, parser: Parser) extends Actor with TimeTracker {
  override lazy val timerName = this.getClass.getName
  val log = Logger.getLogger(classOf[ParserActor])

  def receive : Receive = {
    case Query(queryId, catalog, statement, user) => {
      log.info("\nInit Parser Task", queryId, catalog, statement, user)
      val timer = initTimer()
      val baseQuery = new BaseQuery(queryId, statement, new CatalogName(catalog))
      try {
        val stmt = parser.parse(baseQuery)
        log.debug("Query parsed: " + stmt.getStatement)
        validator forward stmt
        //sender ! ACK(queryId, QueryStatus.PARSED)
      }catch {
        case pe: ParsingException => {
          log.error("Parsing error: " + pe.getMessage + " sender: " + sender.toString())
          val error = Result.createParsingErrorResult(pe.getMessage)
          error.setQueryId(queryId)
          sender ! error
        }
      }finally{
        finishTimer(timer)
      }

      log.info("Finish Parser Task")
    }
    case _ => {
      log.error("Unknown message")
      sender ! Result.createUnsupportedOperationErrorResult("Not recognized object")
    }
  }

}
