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

package com.stratio.crossdata.connectors


import akka.actor._
import com.stratio.crossdata.common.connector.IConnector
import com.stratio.crossdata.connectors.ConnectorRestartActor.RestartConnector
import org.apache.log4j.Logger

import scala.concurrent.duration.DurationInt


object ConnectorRestartActor {
  def props( connectorApp: ConnectorApp, connector: IConnector):
  Props = Props(new ConnectorRestartActor(connectorApp, connector))


  case class RestartConnector()
}

/**
 * Restart the actor only one time => It could store the metadata to restart the connector succesfully if needed
 */
class ConnectorRestartActor(connectorApp: ConnectorApp, connector: IConnector) extends Actor with ActorLogging {

  lazy val logger = Logger.getLogger(classOf[ConnectorActor])

  logger.info("Lifting connectorRestart actor")

  var cancelableRestart: Option[Cancellable] = None

  val timeout = 10 seconds

  def restart: Unit = {
    connectorApp.stop()
    connectorApp.startup(connector)
  }

  override def postStop = {
    super.postStop
    log.info("ConnectorRestart actor postStop")
  }

  override def receive: Receive = initialState

  def initialState: Receive = {

    case RestartConnector => {
      logger.info("Restart the connector app received")
      context.become(expectingKill)
      cancelableRestart = Some(context.system.scheduler.scheduleOnce(timeout){
        restart
        self ! PoisonPill
      }(context.dispatcher))
    }

  }


  def expectingKill: Receive = {
    case RestartConnector => {
      log.info("Duplicated restart connector received")
    }
    case connectorTerminated => {
      logger.info("Clean restart")
      cancelableRestart.foreach( _.cancel)
      restart
      self ! PoisonPill
  }
}

}
