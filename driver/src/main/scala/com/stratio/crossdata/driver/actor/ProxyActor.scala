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
package com.stratio.crossdata.driver.actor


import java.util.UUID

import akka.actor.{Actor, ActorRef, Props}
import akka.cluster.client.ClusterClient
import akka.pattern.pipe
import com.stratio.crossdata.common._
import com.stratio.crossdata.common.result.{ErrorSQLResult, SuccessfulSQLResult}
import com.stratio.crossdata.common.security.Session
import com.stratio.crossdata.driver.Driver
import com.stratio.crossdata.driver.actor.ProxyActor.PromisesByIds
import com.stratio.crossdata.driver.util.HttpClient
import org.apache.log4j.Logger
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{StringType, StructField, StructType}

import scala.concurrent.{Future, Promise}
import scala.util.matching.Regex

object ProxyActor {
  val DefaultName = "proxy-actor"

  def props(clusterClientActor: ActorRef, driver: Driver): Props =
    Props(new ProxyActor(clusterClientActor, driver))

  case class PromisesByIds(promises: Map[UUID, Promise[ServerReply]])

}

class ProxyActor(clusterClientActor: ActorRef, driver: Driver) extends Actor {

  lazy val logger = Logger.getLogger(classOf[ProxyActor])

  private val catalogOpExp: Regex = """^\s*CREATE\s+TEMPORARY.+$""".r

  private val httpClient = HttpClient(driver.driverConf, context.system)

  override def receive: Receive = initial

  private val initial: Receive = {
    case any =>
      logger.debug("Initial state. Changing to start state.")
      context.become(start(PromisesByIds(Map.empty)))
      self ! any
  }


  // Previous step to process the message where promise is stored.
  def storePromise(promisesByIds: PromisesByIds): Receive = {
    case (message: CommandEnvelope, promise: Promise[ServerReply @unchecked]) =>
      logger.debug("Sending message to the Crossdata cluster")
      context.become(start(promisesByIds.copy(promisesByIds.promises + (message.cmd.requestId -> promise))))
      self ! message
  }

  // Process messages from the Crossdata Driver.
  def sendToServer(promisesByIds: PromisesByIds): Receive = {

    case secureSQLCommand @ CommandEnvelope(sqlCommand: SQLCommand, _, _) =>
      logger.info(s"Sending query: ${sqlCommand.sql} with requestID=${sqlCommand.requestId} & queryID=${sqlCommand.queryId}")
      clusterClientActor ! ClusterClient.Send(ServerClusterClientParameters.ServerPath, secureSQLCommand, localAffinity = false)

    case secureSQLCommand @ CommandEnvelope(addJARCommand @ AddJARCommand(path, _, _, _), session, _) =>
      import context.dispatcher
      val shipmentResponse: Future[SQLReply] = sendJarToServers(addJARCommand, path, session)
      shipmentResponse pipeTo sender

    case secureSQLCommand @ CommandEnvelope(clusterStateCommand: ClusterStateCommand, _, _) =>
      logger.debug(s"Send cluster state with requestID=${clusterStateCommand.requestId}")
      clusterClientActor ! ClusterClient.Send(ServerClusterClientParameters.ServerPath, secureSQLCommand, localAffinity = false)

    case secureSQLCommand @ CommandEnvelope(aCmd @ AddAppCommand(path, alias, clss, _), _, _) =>
      clusterClientActor ! ClusterClient.Send(ServerClusterClientParameters.ServerPath,secureSQLCommand, localAffinity=false)

    case secureSQLCommand @ CommandEnvelope(_: OpenSessionCommand | _: CloseSessionCommand, _, _) =>
      clusterClientActor ! ClusterClient.Send(ServerClusterClientParameters.ServerPath, secureSQLCommand, localAffinity = true)

    case sqlCommand: SQLCommand =>
      logger.warn(s"Command message not securitized: ${sqlCommand.sql}. Message won't be sent to the Crossdata cluster")
  }


  def sendJarToServers(command: Command, path: String, session:Session): Future[SQLReply] = {
    import scala.concurrent.ExecutionContext.Implicits.global
    httpClient.sendJarToHTTPServer(path, session) map { response =>
      SQLReply(
        command.requestId,
        SuccessfulSQLResult(Array(Row(response)), StructType(StructField("filepath", StringType) :: Nil))
      )
    } recover {
      case failureCause =>
        val msg = s"Error trying to send JAR through HTTP: ${failureCause.getMessage}"
        logger.error(msg)
        SQLReply(
          command.requestId,
          ErrorSQLResult(msg)
        )
    }
  }

  // Message received from a Crossdata Server.
  def receiveFromServer(promisesByIds: PromisesByIds): Receive = {
    case reply: ServerReply =>
      logger.info(s"Sever reply received from Crossdata Server: $sender with ID=${reply.requestId}")
      promisesByIds.promises.get(reply.requestId) match {
        case Some(p) =>
          context.become(start(promisesByIds.copy(promisesByIds.promises - reply.requestId)))
          reply match {
            case reply @ SQLReply(_, result) =>
              logger.info(s"Successful SQL execution: $result")
              p.success(reply)
            // TODO review query cancelation
            case reply @ QueryCancelledReply(id) =>
              logger.info(s"Query $id cancelled")
              p.success(reply)
            case reply @ ClusterStateReply(_, clusterState) =>
              logger.debug(s"Cluster snapshot received $clusterState")
              p.success(reply)
            case reply @ OpenSessionReply(_, isOpen) =>
              logger.debug(s"Open session reply received: open=$isOpen")
              p.success(reply)
            case _ =>
              p.failure(new RuntimeException(s"Unknown message: $reply"))
          }
        case None => logger.warn(s"Unexpected response: $reply")
      }
  }

  def start(promisesByIds: PromisesByIds): Receive = {
    storePromise(promisesByIds) orElse
    sendToServer(promisesByIds) orElse
    receiveFromServer(promisesByIds) orElse {
      case any =>
        logger.warn(s"Unknown message: $any. Message won't be sent to the Crossdata cluster")
    }
  }
}

