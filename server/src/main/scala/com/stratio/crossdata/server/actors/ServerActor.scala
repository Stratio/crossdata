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

import akka.actor.SupervisorStrategy.{Stop, Restart}
import akka.actor._
import akka.cluster.Cluster
import com.stratio.crossdata.common.SQLCommand
import com.stratio.crossdata.common.result.{ErrorResult, SuccessfulQueryResult}
import com.stratio.crossdata.server.actors.JobActor.Events.{JobCompleted, JobFailed}
import com.stratio.crossdata.server.config.ServerConfig
import org.apache.log4j.Logger
import org.apache.spark.sql.crossdata.{XDDataFrame, XDContext}

object ServerActor {



  def props(cluster: Cluster, xdContext: XDContext): Props = Props(new ServerActor(cluster, xdContext))

  object Messages {


  }

}

class ServerActor(cluster: Cluster, xdContext: XDContext) extends Actor with ServerConfig {

  override lazy val logger = Logger.getLogger(classOf[ServerActor])

  def receive: Receive = {
    case sqlCommand @ SQLCommand(query, _, withColnames) =>
      logger.debug(s"Query received ${sqlCommand.queryId}: ${sqlCommand.query}. Actor ${self.path.toStringWithoutAddress}")
      context.actorOf(JobActor.props(xdContext, sqlCommand, sender()))
    case JobFailed(e) =>
      logger.error(e.getMessage)
      context.stop(sender())
    case JobCompleted =>
      context.stop(sender()) //TODO: This could be changed so done works could be inquired about their state
    case any =>
      logger.error(s"Something is going wrong! Unknown message: $any")
  }

  //TODO: Use number of tries and timeout configuration parameters
  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
    case _: ActorKilledException => Stop  //In the future it might be interesting to add the
    case _ => Restart //Crashed job gets restarted
  }
}
