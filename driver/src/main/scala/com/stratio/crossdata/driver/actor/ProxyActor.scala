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

import akka.actor.{Actor, ActorRef, Props}
import akka.contrib.pattern.ClusterClient
import com.stratio.crossdata.common.{SQLCommand, SecureCommand}
import com.stratio.crossdata.driver.Driver
import org.apache.log4j.Logger

object ProxyActor {
  val ServerPath = "/user/crossdata-server"
  val DefaultName = "proxy-actor"
  val RemoteClientName = "remote-client"

  def props(clusterClientActor: ActorRef, driver: Driver): Props =
    Props(new ProxyActor(clusterClientActor, driver))

}

class ProxyActor(clusterClientActor: ActorRef, driver: Driver) extends Actor {

  lazy val logger = Logger.getLogger(classOf[ProxyActor])


  override def receive: Receive = {
    case secureSQLCommand @ SecureCommand(cmd, _) =>
      clusterClientActor forward ClusterClient.Send(ProxyActor.ServerPath, secureSQLCommand, localAffinity = false)

      cmd match {
        case sqlCommand: SQLCommand => logger.info(s"Sending query: ${sqlCommand.sql}")
        case any => logger.info(s"Sending query: $any")
      }

    case SQLCommand =>
      logger.warn("Command message not securitized. Message won't be sent to the Crossdata cluster")

    case any =>
      logger.warn(s"Unknown message: $any. Message won't be sent to the Crossdata cluster")
  }
}

