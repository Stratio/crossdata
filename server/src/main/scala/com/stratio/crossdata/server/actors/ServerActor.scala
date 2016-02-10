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
import com.stratio.crossdata.common.SQLCommand
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

    case sqlCommand @ SQLCommand(query, _, withColnames) =>
      logger.info(s"Query received ${sqlCommand.queryId}: ${sqlCommand.query}. Actor ${self.path.toStringWithoutAddress}")
      try {
        val df = xdContext.sql(query)
        val (rows, schema) = if(withColnames) {
          val r = df.asInstanceOf[XDDataFrame].flattenedCollect()
          val longestRow = r.headOption.map(_ => r.maxBy(_.schema.length))
          (r, longestRow.map(_.schema).getOrElse(df.schema))
        } else (df.collect(), df.schema)
        sender ! SuccessfulQueryResult(sqlCommand.queryId, rows, schema)
      } catch {
        case e: Exception => logAndReply(sqlCommand.queryId, e)
        case soe: StackOverflowError => logAndReply(sqlCommand.queryId, soe)
        case oome: OutOfMemoryError =>
          System.gc()
          logAndReply(sqlCommand.queryId, oome)
      }

    case any =>
      logger.error(s"Something is going wrong!. Unknown message: $any")

  }

  private def logAndReply(queryId: UUID, trowable: Throwable): Unit = {
    logger.error(trowable.getMessage)
    sender ! ErrorResult(queryId, trowable.getMessage, Some(trowable))
  }


}
