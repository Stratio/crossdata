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

import java.util.UUID

import akka.actor.{ActorSelection, ActorSystem}
import akka.contrib.pattern.ClusterClient
import akka.pattern.ask
import com.stratio.crossdata.common.ask.{APICommand, Command, Query, Connect}
import com.stratio.crossdata.common.exceptions._
import com.stratio.crossdata.common.result._
import com.stratio.crossdata.communication.Disconnect
import com.stratio.crossdata.driver.actor.ProxyActor
import com.stratio.crossdata.driver.config.{BasicDriverConfig, DriverConfig, DriverSectionConfig, ServerSectionConfig}
import com.stratio.crossdata.driver.result.SyncResultHandler
import com.stratio.crossdata.driver.utils.RetryPolitics
import org.apache.log4j.Logger
import com.stratio.crossdata.common.api.Manifest

import scala.concurrent.duration._

object BasicDriver extends DriverConfig {
  /**
   * Class logger.
   */
  override lazy val logger = Logger.getLogger(getClass)

  def getBasicDriverConfigFromFile = {
    logger.debug("RetryTimes    --> " + retryTimes)
    logger.debug("RetryDuration --> " + retryDuration.duration.toMillis.toString)
    logger.debug("ClusterName   --> " + clusterName)
    logger.debug("ClusterName   --> " + clusterActor)
    logger.debug("ClusterHosts  --> " + clusterHosts.map(_.toString).toArray.toString)
    new BasicDriverConfig(new DriverSectionConfig(retryTimes, retryDuration.duration.toMillis),
      new ServerSectionConfig(clusterName, clusterActor, clusterHosts.map(_.toString).toArray))
  }
}

class BasicDriver(basicDriverConfig: BasicDriverConfig) {

  /**
   * Default user to connect to the com.stratio.crossdata server.
   */
  private final val DEFAULT_USER: String = "META_USER"
  lazy val logger = BasicDriver.logger
  lazy val queries: java.util.Map[String, IResultHandler] = new java.util.HashMap[String, IResultHandler]
  lazy val system = ActorSystem("MetaDriverSystem", BasicDriver.config)
  lazy val initialContacts: Set[ActorSelection] = contactPoints.map(contact => system.actorSelection(contact)).toSet
  lazy val clusterClientActor = system.actorOf(ClusterClient.props(initialContacts), "remote-client")
  lazy val proxyActor = system.actorOf(ProxyActor.props(clusterClientActor, basicDriverConfig.serverSection.clusterActor, this), "proxy-actor")

  lazy val retryPolitics: RetryPolitics = {
    new RetryPolitics(basicDriverConfig.driverSection.retryTimes, basicDriverConfig.driverSection.retryDuration.millis)
  }
  lazy val contactPoints: List[String] = {
    basicDriverConfig.serverSection.clusterHosts.toList.map(host => "akka.tcp://" + basicDriverConfig.serverSection.clusterName + "@" + host + "/user/receptionist")
  }
  //For Futures
  implicit val context = system.dispatcher
  var userId: String = ""
  var userName: String = ""
  var currentCatalog: String = ""

  def this() {
    this(BasicDriver.getBasicDriverConfigFromFile)
  }

  /**
   * Release connection to MetaServer.
   * @param user Login to the user (Audit only)
   * @return ConnectResult
   */
  @throws(classOf[ConnectionException])
  def connect(user: String): Result = {
    logger.info("Establishing connection with user: " + user + " to " + contactPoints)
    val result = retryPolitics.askRetry(proxyActor, new Connect(user), 5 second)
    result match {
      case errorResult: ErrorResult => {
        throw new ConnectionException(errorResult.getErrorMessage)
      }
      case connectResult: ConnectResult => {
        userId = connectResult.getSessionId
        result
      }
    }
  }

  /**
   * Finnish connection to MetaServer.
   */
  @throws(classOf[ConnectionException])
  def disconnect(): Unit = {
    logger.info("Disconnecting user: " + userId + " to " + contactPoints)
    val result = retryPolitics.askRetry(proxyActor, new Disconnect(userId), 5 second, retry = 1)
    result match {
      case errorResult: ErrorResult => {
        throw new ConnectionException(errorResult.getErrorMessage)
      }
      case connectResult: DisconnectResult => {
        userId = null
      }
    }
  }

  /**
   * Execute a query in the Meta server asynchronously.
   * @param query The query.
   * @param callback The callback object.
   */
  @throws(classOf[ConnectionException])
  def asyncExecuteQuery(query: String, callback: IResultHandler): String = {
    if (userId == null) {
      throw new ConnectionException("You must connect to cluster")
    }
    val queryId = UUID.randomUUID()
    queries.put(queryId.toString, callback)
    sendQuery(new Query(queryId.toString, currentCatalog, query, userId))
    queryId.toString
  }

  def sendQuery(message: AnyRef) {
    proxyActor.ask(message)(5 second)
  }

  /**
   * Launch query in Meta Server
   * @param query Launched query
   * @return QueryResult
   */
  @throws(classOf[ConnectionException])
  @throws(classOf[ParsingException])
  @throws(classOf[ValidationException])
  @throws(classOf[ExecutionException])
  @throws(classOf[UnsupportedException])
  def executeQuery(query: String): Result = {
    if (userId == null) {
      throw new ConnectionException("You must connect to cluster")
    }
    val queryId = UUID.randomUUID()
    val callback = new SyncResultHandler
    queries.put(queryId.toString, callback)
    sendQuery(new Query(queryId.toString, currentCatalog, query, userId))
    val r = callback.waitForResult()
    queries.remove(queryId.toString)
    r
  }

  /**
   * List the existing catalogs in the underlying database.
   * @return A MetadataResult with a list of catalogs, or the object with hasError set
   *         containing the error message.
   */
  def listCatalogs(): MetadataResult = {
    val result = retryPolitics.askRetry(proxyActor, new Command(APICommand.LIST_CATALOGS, null))
    result.asInstanceOf[MetadataResult]
  }

  /**
   * List the existing tables in a database catalog.
   * @return A MetadataResult with a list of tables, or the object with hasError set
   *         containing the error message.
   */
  def listTables(catalogName: String): MetadataResult = {
    val params: java.util.List[AnyRef] = new java.util.ArrayList[AnyRef]
    params.add(catalogName)
    val result = retryPolitics.askRetry(proxyActor, new Command(APICommand.LIST_TABLES, params))
    result.asInstanceOf[MetadataResult]
  }

  /**
   * List the existing tables in a database catalog.
   * @return A MetadataResult with a map of columns.
   */
  def listFields(catalogName: String, tableName: String): MetadataResult = {
    val params: java.util.List[AnyRef] = new java.util.ArrayList[AnyRef]
    params.add(catalogName)
    params.add(tableName)
    val result = retryPolitics.askRetry(proxyActor, new Command(APICommand.LIST_COLUMNS, params))
    result.asInstanceOf[MetadataResult]
  }

  /**
   * Send manifest to the server
   * @param manifest The manifest to be sent
   * @return A CommandResult with a string
   */
  @throws(classOf[ManifestException])
  def addManifest(manifest: Manifest): CommandResult = {
    val params: java.util.List[AnyRef] = new java.util.ArrayList[AnyRef]
    params.add(manifest)
    val result = retryPolitics.askRetry(proxyActor, new Command(APICommand.ADD_MANIFEST, params))
    result.asInstanceOf[CommandResult]
  }

  /**
   * Reset metadata in server
   * @return A CommandResult with a string
   */
  def resetMetadata(): CommandResult = {
    val result = retryPolitics.askRetry(proxyActor, new Command(APICommand.RESET_METADATA, null))
    result.asInstanceOf[CommandResult]
  }

  def listConnectors():CommandResult = {
    val result = retryPolitics.askRetry(proxyActor, new Command(APICommand.LIST_CONNECTORS, null))
    result.asInstanceOf[CommandResult]
  }

  /**
   * Get the IResultHandler associated with a query identifier.
   * @param queryId Query identifier.
   * @return The result handler.
   */
  def getResultHandler(queryId: String): IResultHandler = {
    queries.get(queryId)
  }

  /**
   * Remove a result handler from the internal map of callbacks.
   * @param queryId The query identifier associated with the callback.
   * @return Whether the callback has been removed.
   */
  def removeResultHandler(queryId: String): Boolean = {
    queries.remove(queryId) != null
  }

  /**
   * Shutdown actor system
   */
  def close() {
    system.shutdown()
  }

  def getUserName: String = {
    return userName
  }

  def setUserName(userName: String) {
    this.userName = userName
    if (userName == null) {
      this.userName = DEFAULT_USER
    }
  }

  def getCurrentCatalog: String = {
    return currentCatalog
  }

  def setCurrentCatalog(catalog: String) {
    this.currentCatalog = catalog
  }
}
