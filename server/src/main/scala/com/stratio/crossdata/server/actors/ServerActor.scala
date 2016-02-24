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

import akka.contrib.pattern.DistributedPubSubExtension
import akka.actor.SupervisorStrategy.Restart
import akka.actor._
import akka.cluster.Cluster
import akka.contrib.pattern.DistributedPubSubMediator.{Publish, SubscribeAck, Subscribe}
import com.stratio.crossdata.common.{CancelQueryExecution, SQLCommand, ControlCommand}
import com.stratio.crossdata.server.actors.JobActor.Commands.CancelJob
import com.stratio.crossdata.server.actors.JobActor.Events.{JobCompleted, JobFailed}
import com.stratio.crossdata.server.actors.ServerActor.ManagementMessages.DelegateCommand
import com.stratio.crossdata.server.config.ServerConfig
import org.apache.log4j.Logger
import org.apache.spark.sql.crossdata.XDContext


object ServerActor {

  val managementTopic: String = "jobsManagement"

  def props(cluster: Cluster, xdContext: XDContext): Props = Props(new ServerActor(cluster, xdContext))

  protected case class JobId(requester: ActorRef, queryId: UUID)

  protected case class ManagementEnvelope(command: ControlCommand, source: ActorRef)

  object ManagementMessages {
    case class DelegateCommand(command: ControlCommand, requester: ActorRef)
  }

}

class ServerActor(cluster: Cluster, xdContext: XDContext) extends Actor with ServerConfig {

  import ServerActor._

  override lazy val logger = Logger.getLogger(classOf[ServerActor])

  lazy val mediator = DistributedPubSubExtension(context.system).mediator

  override def preStart(): Unit = {
    super.preStart()
    // Subscribe to the management distributed topic
    mediator ! Subscribe(managementTopic, self)
  }

  // Actor behaviours

  override def receive: Actor.Receive = initial

  private val initial: Receive = {
    case SubscribeAck(Subscribe(managementTopic, None, self)) =>
      context.become(ready(Map.empty))
  }

  private val topicReceive: Receive = {
    case DelegateCommand(cmd, requester) if(sender != self) => self.tell(cmd, requester)
  }

  private def ready(jobsById: Map[JobId, ActorRef]): Receive = topicReceive orElse {

    // Commands

    case sqlCommand @ SQLCommand(query, _, withColnames, timeout) =>
      logger.debug(s"Query received ${sqlCommand.queryId}: ${sqlCommand.query}. Actor ${self.path.toStringWithoutAddress}")
      val jobActor = context.actorOf(JobActor.props(xdContext, sqlCommand, sender(), timeout))
      context.become(ready(jobsById + (JobId(sender(), sqlCommand.queryId) -> jobActor)))

    case cc @ CancelQueryExecution(queryId) =>
      jobsById.get(JobId(sender, queryId)) map (_ ! CancelJob) getOrElse {
        mediator ! Publish(managementTopic, DelegateCommand(cc, sender))
      }

    // Events

    case JobFailed(e) =>
      logger.error(e.getMessage)
    case JobCompleted =>
      context.stop(sender()) //TODO: This could be changed so done works could be inquired about their state
    case any =>
      logger.error(s"Something is going wrong! Unknown message: $any")
  }

  //TODO: Use number of tries and timeout configuration parameters
  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(retryNoAttempts, retryCountWindow) {
    //case _: ActorKilledException => Stop  //In the future it might be interesting to add the
    case _ => Restart //Crashed job gets restarted (or not, depending on `retryNoAttempts` and `retryCountWindow`)
  }

}
