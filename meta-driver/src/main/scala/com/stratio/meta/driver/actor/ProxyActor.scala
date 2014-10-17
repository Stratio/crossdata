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

package com.stratio.meta.driver.actor

import akka.actor.{Actor, ActorRef, Props}
import akka.contrib.pattern.ClusterClient
import akka.util.Timeout
import com.stratio.meta.common.ask.{Command, Connect, Query}
import com.stratio.meta.communication.{ACK, Disconnect}
import com.stratio.meta.driver.BasicDriver
import com.stratio.meta2.common.result.{ErrorResult, Result}
import org.apache.log4j.Logger

import scala.concurrent.duration._

/**
 * Companion object.
 */
object ProxyActor {
  /**
   * Initial path for actor's indentify.
   */
  val INIT_PATH = "/user/"

  /**
   * Config prop in ProxyActor.
   * @param clusterClientActor ActorRef to ClusterClientActor pattern.
   * @param remoteActor Remote actor's name.
   * @return Actor's props.
   */
  def props(clusterClientActor: ActorRef, remoteActor: String, driver: BasicDriver): Props = Props(new ProxyActor(clusterClientActor,
    remoteActor, driver))

  /**
   * Create path with actor's name.
   * @param remoteActor Remote actor's name.
   * @return Complete path.
   */
  def remotePath(remoteActor: String) = INIT_PATH + remoteActor
}

/**
 * Actor to connect with receptionist actor in the remote cluster.
 * @param clusterClientActor ActorRef to ClusterClientActor pattern.
 * @param remoteActor Remote actor's name.
 */
class ProxyActor(clusterClientActor: ActorRef, remoteActor: String, driver: BasicDriver) extends Actor {

  /**
   * Class logger.
   */
  lazy val logger = Logger.getLogger(getClass)

  implicit val timeout = Timeout(5 seconds)

  logger.info("Up!")

  override def receive: Actor.Receive = {



    /* The driver sends the connect message. */
    case c: Connect => {
      clusterClientActor forward ClusterClient.Send(ProxyActor.remotePath(remoteActor), c, localAffinity = true)
    }

    case c: Disconnect => {
      logger.debug("Send connect " + c)
      clusterClientActor forward ClusterClient.Send(ProxyActor.remotePath(remoteActor), c, localAffinity = true)
    }

    /* API Command */
    case cmd: Command => {
      logger.debug("Send command: " + cmd)
      clusterClientActor forward ClusterClient.Send(ProxyActor.remotePath(remoteActor), cmd, localAffinity = true)
    }

    /* ACK received */
    case ack: ACK => {
      val handler = driver.getResultHandler(ack.queryId)
      if (handler != null) {
        handler.processAck(ack.queryId, ack.status)
      } else {
        logger.warn("ACK not expected received: " + ack)
      }
    }

    /* Send a query to the remote meta-server infrastructure. */
    case message: Query => {
      clusterClientActor ! ClusterClient.Send(ProxyActor.remotePath(remoteActor), message, localAffinity = true)
    }
    case result: Result => {
      val handler = driver.getResultHandler(result.getQueryId)
      if (handler != null) {
        if (!result.isInstanceOf[ErrorResult]) {
          handler.processResult(result)
        } else {
          handler.processError(result)
        }
      } else {
        logger.warn("Result not expected received for QID: " + result.getQueryId)
      }

    }
    case unknown: Any => {
      logger.error("Unknown message: " + unknown)
      sender ! "Message type not supported"
    }

    case _ => {
      logger.error("Unknown message");
    }
  }
}


