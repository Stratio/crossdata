/*
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

import akka.actor.SupervisorStrategy.Restart
import akka.actor._
import akka.cluster.Cluster
import akka.contrib.pattern.DistributedPubSubExtension
import akka.contrib.pattern.DistributedPubSubMediator.{Publish, Subscribe, SubscribeAck}
import com.stratio.crossdata.common.result.{ErrorSQLResult, SuccessfulSQLResult}
import com.stratio.crossdata.common.security.Session
import com.stratio.crossdata.common.util.akka.KeepAlive.KeepAliveMaster
import com.stratio.crossdata.common.util.akka.KeepAlive.KeepAliveMaster.{DoCheck, HeartbeatLost}
import com.stratio.crossdata.common.{CommandEnvelope, SQLCommand, _}
import com.stratio.crossdata.server.actors.JobActor.Commands.{CancelJob, StartJob}
import com.stratio.crossdata.server.actors.JobActor.Events.{JobCompleted, JobFailed}
import com.stratio.crossdata.server.config.{ServerActorConfig, ServerConfig}
import org.apache.log4j.Logger
import org.apache.spark.sql.crossdata.XDSessionProvider
import org.apache.spark.sql.types.StructType

import scala.concurrent.duration._
import scala.util.{Failure, Success}


object ServerActor {
  val ManagementTopic: String = "jobsManagement"

  def props(cluster: Cluster, sessionProvider: XDSessionProvider, config: ServerActorConfig): Props =
    Props(new ServerActor(cluster, sessionProvider, config))

  protected case class JobId(requester: ActorRef, sessionId: UUID, queryId: UUID)

  protected case class ManagementEnvelope(command: ControlCommand, source: ActorRef)

  private object ManagementMessages {

    case class DelegateCommand(scommand: CommandEnvelope, broadcaster: ActorRef)

    case class FinishJob(jobActor: ActorRef)

  }

  case class State(jobsById: Map[JobId, ActorRef], clientMonitor: ActorRef)

}

// TODO it should only accept messages from known sessions
class ServerActor(cluster: Cluster, sessionProvider: XDSessionProvider, serverActorConfig: ServerActorConfig)
  extends Actor with ServerConfig {

  import ServerActor.ManagementMessages._
  import ServerActor._

  lazy val logger = Logger.getLogger(classOf[ServerActor])

  lazy val mediator = DistributedPubSubExtension(context.system).mediator

  override def preStart(): Unit = {
    super.preStart()

    // Subscribe to the management distributed topic
    mediator ! Subscribe(ManagementTopic, self)

  }

  // Actor behaviours

  override def receive: Actor.Receive = initial(Set(ManagementTopic))

  private def initial(pendingTopics: Set[String]): Receive = {
    case SubscribeAck(Subscribe(ManagementTopic, None, self)) =>
      val newPendingTopics = pendingTopics - ManagementTopic
      checkSubscriptions(newPendingTopics)
  }

  private def checkSubscriptions(pendingTopics: Set[String]): Unit =
    if (pendingTopics.isEmpty) {
      val clientMonitor = context.actorOf(KeepAliveMaster.props(self))
      context.become(ready(State(Map.empty, clientMonitor)))
    } else
      context.become(initial(pendingTopics))

  /**
    * If a `cmd` is passed to this method is because it has already been checked that this server can run it.
    *
    * @param cmd
    * @param st
    */
  private def executeAccepted(cmd: CommandEnvelope)(st: State): Unit = cmd match {
    case CommandEnvelope(sqlCommand@SQLCommand(query, queryId, withColnames, timeout), session@Session(id, requester)) =>
      logger.debug(s"Query received $queryId: $query. Actor ${self.path.toStringWithoutAddress}")
      logger.debug(s"Session identifier $session")
      val sessionOpt = sessionProvider.session(id) match {
        case Success(xdSession) =>
          val jobActor = context.actorOf(JobActor.props(xdSession, sqlCommand, sender(), timeout))
          jobActor ! StartJob
          context.become(
            ready(st.copy(jobsById = st.jobsById + (JobId(requester, id, sqlCommand.queryId) -> jobActor)))
          )

        case Failure(error) =>
          logger.warn(s"Received message with an unknown sessionId $id", error)
          sender ! ErrorSQLResult(s"Unable to recover the session ${session.id}. Cause: ${error.getMessage}")
      }


    case CommandEnvelope(addAppCommand@AddAppCommand(path, alias, clss, _), session@Session(id, requester)) =>
      if ( sessionProvider.session(id).map(_.addApp(path, clss, alias)).getOrElse(None).isDefined)// TODO improve addJar sessionManagement
        sender ! SQLReply(addAppCommand.requestId, SuccessfulSQLResult(Array.empty, new StructType()))
      else
        sender ! SQLReply(addAppCommand.requestId, ErrorSQLResult("App can't be stored in the catalog"))

    case CommandEnvelope(cc@CancelQueryExecution(queryId), session@Session(id, requester)) =>
      st.jobsById.get(JobId(requester, id, queryId)).get ! CancelJob
  }


  // Receive functions:

  // Broadcast messages treatment
  def broadcastRequestsRec(st: State): Receive = {
    case DelegateCommand(_, broadcaster) if broadcaster == self => //Discards from this server broadcast delegated-commands

    case DelegateCommand(cmd, broadcaster) if broadcaster != self =>
      cmd match {
        // Inner pattern matching for future delegated command validations
        case sc@CommandEnvelope(CancelQueryExecution(queryId), Session(sid, requester)) =>
          st.jobsById.get(JobId(requester, sid, queryId)) foreach (_ => executeAccepted(sc)(st))
        /* If it doesn't validate it won't be re-broadcast since the source server already distributed it to all
            servers through the topic. */
      }
  }

  // TODO do not accept unknown sessions
  // Commands reception: Checks whether the command can be run at this Server passing it to the execution method if so
  def commandMessagesRec(st: State): Receive = {

    case sc@CommandEnvelope(_: SQLCommand, _) =>
      executeAccepted(sc)(st)

    case sc@CommandEnvelope(_: AddJARCommand, _) =>
      executeAccepted(sc)(st)

    case sc@CommandEnvelope(_: AddAppCommand, _) =>
      executeAccepted(sc)(st)

    case sc@CommandEnvelope(cc: ControlCommand, session@Session(id, requester)) =>
      st.jobsById.get(JobId(requester, id, cc.requestId)) map { _ =>
        executeAccepted(sc)(st) // Command validated to be executed by this server.
      } getOrElse {
        // If it can't run here it should be executed somewhere else
        mediator ! Publish(ManagementTopic, DelegateCommand(sc, self))
      }

    case sc@CommandEnvelope(_: ClusterStateCommand, session) =>
      sender ! ClusterStateReply(sc.cmd.requestId, cluster.state)

    case sc@CommandEnvelope(_: OpenSessionCommand, session) =>
      sessionProvider.newSession(session.id)
      sender ! ClusterStateReply(sc.cmd.requestId, cluster.state)

      st.clientMonitor ! DoCheck(session.id, 1 minute) //TODO: Time before alarm has to be configurable

    case sc@CommandEnvelope(_: CloseSessionCommand, session) =>
      // TODO validate/ actorRef instead of sessionId
      sessionProvider.closeSession(session.id)
      /* Note that the client monitoring isn't explicitly stopped. It'll after the first miss
          is detected, right after the driver has ended its session. */

  }

  // Manages events from the `JobActor` which runs the task
  def eventsRec(st: State): Receive = {

    case JobFailed(e) =>
      logger.error(e.getMessage, e)
      sentenceToDeath(sender())

    case JobCompleted =>
      sentenceToDeath(sender())

    case FinishJob(who) =>
      context.become(ready(st.copy(jobsById = st.jobsById.filterNot(_._2 == who))))
      context.children.find(_ == who).foreach(gracefullyKill)
  }

  // Manages clients' heartbeats losses, closing their sessions and stopping all jobs related to them.
  def clientMonitoringEvents(st: State): Receive = {
    case HeartbeatLost(sessionId: UUID) =>
      val newjobsmap = st.jobsById filter {
        case (JobId(_, jobSessionId, _), job) if jobSessionId == sessionId =>
          gracefullyKill(job) // WARNING! Side-effect
          sessionProvider.closeSession(sessionId)
          false
        case _ => true
      }
      context.become(ready(st.copy(jobsById = newjobsmap)))
  }

  // Function composition to build the finally applied receive-function
  private def ready(st: State): Receive =
    broadcastRequestsRec(st) orElse
      commandMessagesRec(st) orElse
      eventsRec(st) orElse
      clientMonitoringEvents(st) orElse { case any =>
      logger.warn(s"Something is going wrong! Unknown message: $any")
    }

  private def sentenceToDeath(victim: ActorRef): Unit = serverActorConfig.completedJobTTL match {
    case finite: FiniteDuration =>
      context.system.scheduler.scheduleOnce(finite, self, FinishJob(victim))(context.dispatcher)
    case _ => // Reprieve by infinite limit
  }

  def gracefullyKill(victim: ActorRef): Unit = {
    victim ! CancelJob
    victim ! PoisonPill
  }

  //TODO: Use number of tries and timeout configuration parameters
  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(serverActorConfig.retryNoAttempts, serverActorConfig.retryCountWindow) {
    case _ => Restart //Crashed job gets restarted (or not, depending on `retryNoAttempts` and `retryCountWindow`)
  }

}

