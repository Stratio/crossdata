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
package com.stratio.crossdata.driver

import akka.actor.ActorRef
import akka.cluster.ClusterEvent.CurrentClusterState
import com.stratio.crossdata.common.result._
import com.stratio.crossdata.common.security.Session
import com.stratio.crossdata.driver.config.DriverConf
import com.stratio.crossdata.driver.session.Authentication
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.Future
import scala.language.postfixOps
import scala.util.Try


class HttpDriver private[driver](driverConf: DriverConf,
                                 auth: Authentication) extends Driver(driverConf, auth) {

  lazy val logger: Logger = LoggerFactory.getLogger(classOf[HttpDriver])

  lazy val driverSession: Session = ???

  private var sessionBeacon: Option[ActorRef] = ???

  // TODO does it make sense here?
  override protected[driver] def openSession(): Try[Boolean] = ???

  override def sql(query: String): SQLResponse = ???

  override def addJar(path: String, toClassPath: Option[Boolean] = None): SQLResponse = ???

  override def addAppCommand(path: String, clss: String, alias: Option[String]): SQLResponse = ???

  override def clusterState(): Future[CurrentClusterState] = ???

  override def closeSession(): Unit = ???

  /*{
    proxyActor ! securitizeCommand(CloseSessionCommand())
    sessionBeacon.foreach(system.stop)
  }*/

}
