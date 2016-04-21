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

package com.stratio.crossdata.driver.actor

import java.util.UUID

import akka.actor.{Actor, ActorRef, Props}
import akka.contrib.pattern.ClusterClient
import akka.remote.transport.ThrottlerTransportAdapter.Direction.Receive
import com.stratio.crossdata.common._
import com.stratio.crossdata.driver.Driver
import com.stratio.crossdata.driver.actor.ProxyActor.PromisesByIds
import org.apache.log4j.Logger

import scala.concurrent.Promise
import scala.util.matching.Regex

object ProxyActor {
  val ServerPath = "/user/crossdata-server"
  val DefaultName = "proxy-actor"
  val RemoteClientName = "remote-client"

  def props(clusterClientActor: ActorRef, driver: Driver): Props =
    Props(new ProxyActor(clusterClientActor, driver))

  case class PromisesByIds(promises: Map[UUID, Promise[ServerReply]])

}

class ProxyActor(clusterClientActor: ActorRef, driver: Driver) extends Actor {

  lazy val logger = Logger.getLogger(classOf[ProxyActor])

  private val catalogOpExp: Regex = """^\s*CREATE\s+TEMPORARY.+$""".r

  override def receive: Receive = initial

  private val initial: Receive = {
    case any =>
      logger.debug("Initial state. Changing to start state.")
      context.become(start(PromisesByIds(Map.empty)))
      self ! any
  }


  // Previous step to process the message where promise is stored.
  def storePromise(promisesByIds: PromisesByIds): Receive = {
    case (message: CommandEnvelope, promise: Promise[ServerReply]) =>
      logger.debug("Sending message to the Crossdata cluster")
      context.become(start(promisesByIds.copy(promisesByIds.promises + (message.cmd.requestId -> promise))))
      self ! message
  }

  // Process messages from the Crossdata Driver.
  def sendToServer(promisesByIds: PromisesByIds): Receive = {
    case secureSQLCommand @ CommandEnvelope(sqlCommand: SQLCommand, _) =>
      logger.info(s"Sending query: ${sqlCommand.sql} with requestID=${sqlCommand.requestId} & queryID=${sqlCommand.queryId}")
      clusterClientActor ! ClusterClient.Send(ProxyActor.ServerPath, secureSQLCommand, localAffinity = false)

    /* TODO: This is a dirty trick to keep temporary tables synchronized at each XDContext
        it should be fixed as soon as Spark version is updated to 1.6 since it'll enable.
        WARNING: This disables creation cancellation commands and exposes the system behaviour to client-side code.
     */
    case secureSQLCommand @ CommandEnvelope(sqlCommand @ SQLCommand(sql @ catalogOpExp(), _, _, _), _) =>
      logger.info(s"Sending temporary catalog entry creation query to all servers: $sql")
      clusterClientActor ! ClusterClient.SendToAll(ProxyActor.ServerPath, secureSQLCommand)

    case secureSQLCommand @ CommandEnvelope(sqlCommand: AddJARCommand, _) =>
      logger.info(s"Send Add Jar command to all servers")
      clusterClientActor ! ClusterClient.SendToAll(ProxyActor.ServerPath, secureSQLCommand)

    case sqlCommand: SQLCommand =>
      logger.warn(s"Command message not securitized: ${sqlCommand.sql}. Message won't be sent to the Crossdata cluster")
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
              logger.info(s"Successful SQL execution: ${result}")
              p.success(reply)
            case reply @ QueryCancelledReply(id) =>
              logger.info(s"Query $id cancelled")
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

