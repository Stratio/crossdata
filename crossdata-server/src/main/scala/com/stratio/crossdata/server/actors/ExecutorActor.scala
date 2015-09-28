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

import akka.actor.ActorLogging
import akka.actor.{Actor, Props}
import akka.cluster.Cluster
import com.stratio.crossdata.common.{SQLCommand, SQLResult}
import com.stratio.crossdata.common.result.{ErrorResult, SuccessfulQueryResult}
import org.apache.log4j.Logger
import org.apache.spark.sql.crossdata.XDContext

object ExecutorActor {
  def props(cluster: Cluster, xDContext: XDContext): Props = Props(new ExecutorActor(cluster, xDContext))
  case class ExecuteQuery(sqlCommand: SQLCommand)
}

class ExecutorActor(cluster: Cluster, xdContext: XDContext) extends Actor with ActorLogging {

  import com.stratio.crossdata.server.actors.ExecutorActor._
  lazy val logger = Logger.getLogger(classOf[ExecutorActor])

  def receive: Receive = {

    case ExecuteQuery( sqlCommand @ SQLCommand(query, _)) =>
      try {
        val df = xdContext.sql(query)
        val rows = df.collect()
        sender ! SuccessfulQueryResult(sqlCommand.queryId, rows)
      } catch {
        case e: Throwable => {
          logger.error(e.getMessage)
          sender ! ErrorResult(sqlCommand.queryId, e.getMessage, Some(e))
        }
      }

    case any =>
      logger.error(s"Something is going wrong!. Unknown message: ${any.toString}")

  }

}

