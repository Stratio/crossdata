/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.crossdata.server.actors

import java.util.UUID

import akka.actor.{Actor, Props}
import akka.cluster.Cluster
import com.stratio.crossdata.common.{SecureSQLCommand, SQLCommand}
import com.stratio.crossdata.common.result.{ErrorResult, SuccessfulQueryResult}
import com.stratio.crossdata.server.config.ServerConfig
import org.apache.log4j.Logger
import org.apache.spark.sql.crossdata.{XDDataFrame, XDContext}


object ServerActor {
  def props(cluster: Cluster, xdContext: XDContext): Props = Props(new ServerActor(cluster, xdContext))
}

class ServerActor(cluster: Cluster, xdContext: XDContext) extends Actor with ServerConfig {

  override lazy val logger = Logger.getLogger(classOf[ServerActor])

  def receive: Receive = {

    case SecureSQLCommand(SQLCommand(query, queryId, withColnames), session) =>
        try {
          // TODO: session must be forwarded to the sql call of the xdContext
          logger.info(s"Query received ${queryId}: ${query}. Actor ${self.path.toStringWithoutAddress}")
          val df = xdContext.sql(query)
          val rows = if(withColnames) df.asInstanceOf[XDDataFrame].flattenedCollect() //TODO: Replace this cast by an implicit conversion
          else df.collect()
          sender ! SuccessfulQueryResult(queryId, rows, df.schema)
        } catch {
          case e: Exception => logAndReply(queryId, e)
          case soe: StackOverflowError => logAndReply(queryId, soe)
          case oome: OutOfMemoryError =>
            System.gc()
            logAndReply(queryId, oome)
        }

    case any =>
      logger.error(s"Something is going wrong!. Unknown message: $any")

  }

  private def logAndReply(queryId: UUID, trowable: Throwable): Unit = {
    logger.error(s" $queryId : ${trowable.getMessage}")
    sender ! ErrorResult(queryId, trowable.getMessage, Some(trowable))
  }


}
