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

package com.stratio.crossdata.driver

import java.util

import akka.actor.{ActorSelection, ActorSystem}
import akka.contrib.pattern.ClusterClient
import com.stratio.crossdata.common.ask.Connect
import com.stratio.crossdata.common.exceptions._
import com.stratio.crossdata.common.result._
import com.stratio.crossdata.communication.Disconnect
import com.stratio.crossdata.driver.actor.ProxyActor
import com.stratio.crossdata.driver.config.{BasicDriverConfig, DriverConfig, DriverSectionConfig, ServerSectionConfig}
import com.stratio.crossdata.driver.utils.RetryPolitics
import com.stratio.crossdata.driver.result.{PaginationSyncDriverResultHandler, SyncDriverResultHandler}
import org.apache.log4j.Logger

import scala.concurrent.duration._


object BasicDriver extends DriverConfig {
  /**
   * Class logger.
   */
  override lazy val logger = Logger.getLogger(getClass)
  val balancing: Boolean= config.getBoolean("config.balancing")
  lazy val auth: Boolean= config.getBoolean("config.authentication")
  lazy val serverPathName:String = config.getString("config.serverPathName")
  lazy val crossdataServerClusterName:String= config.getString("config.crossdataServerClusterName")
  lazy val localAffinity:Boolean=config.getBoolean("config.localAffinity")

  def getBasicDriverConfigFromFile:BasicDriverConfig = {
    logger.debug("RetryTimes    --> " + retryTimes)
    logger.debug("RetryDuration --> " + retryDuration.duration.toMillis.toString)
    logger.debug("ClusterName   --> " + clusterName)
    logger.debug("ClusterName   --> " + clusterActor)
    logger.debug("ClusterHosts  --> " + clusterHosts.map(_.toString).toArray.toString)
    new BasicDriverConfig(new DriverSectionConfig(retryTimes, retryDuration.duration.toMillis),
      new ServerSectionConfig(clusterName, clusterActor, clusterHosts.map(_.toString).toArray))
  }

  def getBasicDriverConfigFromFile(servers:Array[String]):BasicDriverConfig = {
    logger.debug("RetryTimes    --> " + retryTimes)
    logger.debug("RetryDuration --> " + retryDuration.duration.toMillis.toString)
    logger.debug("ClusterName   --> " + clusterName)
    logger.debug("ClusterName   --> " + clusterActor)
    logger.debug("ClusterHosts  --> " + clusterHosts.map(_.toString).toArray.toString)
    new BasicDriverConfig(new DriverSectionConfig(retryTimes, retryDuration.duration.toMillis),
      new ServerSectionConfig(clusterName, clusterActor, servers.map(_.toString).toArray))
  }
}

class BasicDriver(basicDriverConfig: BasicDriverConfig) {

  /**
   * Default user to connect to the com.stratio.crossdata server.
   */
  private final val DEFAULT_USER: String = "CROSSDATA_USER"
  private final val DEFAULT_PASS: String = "CROSSDATA_PASS"

  val balancing: Boolean=BasicDriver.balancing;
  val serverPathName:String = BasicDriver.serverPathName
  val crossdataServerClusterName=BasicDriver.crossdataServerClusterName
  val cpuLoadPingTimeInMillis:Long = 5000
  val localAffinity:Boolean=BasicDriver.localAffinity

  private lazy val logger = BasicDriver.logger

  private val system = ActorSystem("CrossdataDriverSystem", BasicDriver.config)
  private val initialContacts: Set[ActorSelection] = contactPoints.map(contact => system.actorSelection(contact)).toSet

  val clusterClientActor = system.actorOf(ClusterClient.props(initialContacts), "remote-client")
  val proxyActor = system.actorOf(ProxyActor.props(clusterClientActor, basicDriverConfig.serverSection.clusterActor, this), "proxy-actor")

  /**
   * Session-Connection map. Currently, a single connection per driver is allowed.
   */
  private lazy val driverConnections: util.Map[String, DriverConnection] = new util.HashMap()

  val retryPolitics: RetryPolitics = {
    new RetryPolitics(basicDriverConfig.driverSection.retryTimes, basicDriverConfig.driverSection.retryDuration.millis)
  }

  private lazy val contactPoints: List[String] = {
    basicDriverConfig.serverSection.clusterHosts.toList.map(host => "akka.tcp://"
      + basicDriverConfig.serverSection.clusterName + "@" + host + "/user/receptionist")
  }

  //For Futures
  implicit val context = system.dispatcher

  var userId: String = ""
  var userName: String = ""
  var password: String = ""


  def this() {
    this(BasicDriver.getBasicDriverConfigFromFile)
  }

  def this(servers:Array[String]) {
    this(BasicDriver.getBasicDriverConfigFromFile(servers))
  }

  /**
   * Check if user authentication is enabled.
   * @return A Boolean whatever the result is.
   */
  def isAuthEnable():Boolean= BasicDriver.auth

  /**
   * Check if the user and pass are allowed to access to Crossdata Server.
   * @param user The user.
   * @param password The pass.
   * @return A Boolean whatever the result is.
   */
  private def checkUser(user: String, password: String):Boolean = true

  /**
   * Release connection to CrossdataServer.
   * @param user Login to the user (Audit only).
   * @return ConnectResult.
   */
  @throws(classOf[ConnectionException])
  def connect(user: String, pass: String): DriverConnection = {

    logger.info("Establishing connection with user: " + user + " to " + contactPoints)
    if (!checkUser(user,pass)){
      logger.info("Connection error")
      throw new ConnectionException("Authentication Error. Check your user or password!")
    }
    val result = retryPolitics.askRetry(proxyActor, new Connect(user, pass), 5 second)
    result match {
      case errorResult: ErrorResult => {
        logger.info("Connection error")
        throw new ConnectionException(errorResult.getErrorMessage)
      }
      case connectResult: ConnectResult => {
        logger.info("Connection established")
        userId = connectResult.getSessionId
        val driverConnection = new DriverConnection(connectResult.getSessionId, userId, this)
        driverConnections.put(connectResult.getSessionId, driverConnection)
        driverConnection

      }
    }
  }

  /**
   * Finnish connection to CrossdataServer.
   */
  @throws(classOf[ConnectionException])
  def disconnect(): Unit = {
    logger.info("Disconnecting user: " + userId + " to " + contactPoints)
    val result = retryPolitics.askRetry(proxyActor, new Disconnect(userId), 5 second, retry = 2)
    result match {
      case errorResult: ErrorResult => {
        throw new ConnectionException(errorResult.getErrorMessage)
      }
      case connectResult: DisconnectResult => {
        //TODO disconnect session
        userId = ""
        driverConnections.clear()
      }
    }
  }


  /**
   * Shutdown actor system.
   */
  def close() {
    system.shutdown()
  }

  //TODO review API (private and public methods). User and pass should be stored in DriverConnection
  /**
   * This method get the UserName.
   * @return the value of userName.
   * */
  def getUserName: String = userName

  def getPassword: String = password

  def setUserName(userName: String) {
    this.userName = userName
    if (userName.isEmpty) {
      this.userName = DEFAULT_USER
    }
  }

  def setPassword(password: String) {
    this.password = password
    if (password.isEmpty) {
      this.password = DEFAULT_PASS
    }
  }

  def getDriverConnections = driverConnections

}
