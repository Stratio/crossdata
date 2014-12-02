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
import com.stratio.crossdata.common.ask.APICommand
import com.stratio.crossdata.common.exceptions.{ManifestException, UnsupportedException, ExecutionException,
ValidationException, ParsingException, ConnectionException}
import com.stratio.crossdata.common.result.{CommandResult, MetadataResult, DisconnectResult, ConnectResult,
ErrorResult, Result, IDriverResultHandler}
import com.stratio.crossdata.driver.actor.ProxyActor
import com.stratio.crossdata.driver.config.{BasicDriverConfig, DriverConfig, DriverSectionConfig, ServerSectionConfig}
import com.stratio.crossdata.driver.result.SyncDriverResultHandler
import com.stratio.crossdata.driver.utils.{ManifestUtils, RetryPolitics}
import org.apache.log4j.Logger
import com.stratio.crossdata.common.manifest.CrossdataManifest

import scala.concurrent.duration._
import com.stratio.crossdata.common.data._
import com.stratio.crossdata.common.ask.Connect
import com.stratio.crossdata.communication.Disconnect
import com.stratio.crossdata.common.ask.Command
import com.stratio.crossdata.common.ask.Query
import com.stratio.crossdata.common.data.{DataStoreName, ConnectorName}

object BasicDriver extends DriverConfig {
  /**
   * Class logger.
   */
  override lazy val logger = Logger.getLogger(getClass)

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
  lazy val logger = BasicDriver.logger
  lazy val queries: java.util.Map[String, IDriverResultHandler] = new java.util.HashMap[String, IDriverResultHandler]
  lazy val system = ActorSystem("CrossdataDriverSystem", BasicDriver.config)
  lazy val initialContacts: Set[ActorSelection] = contactPoints.map(contact => system.actorSelection(contact)).toSet
  lazy val clusterClientActor = system.actorOf(ClusterClient.props(initialContacts), "remote-client")
  lazy val proxyActor = system.actorOf(ProxyActor.props(clusterClientActor, basicDriverConfig.serverSection.clusterActor, this), "proxy-actor")

  lazy val retryPolitics: RetryPolitics = {
    new RetryPolitics(basicDriverConfig.driverSection.retryTimes, basicDriverConfig.driverSection.retryDuration.millis)
  }
  lazy val contactPoints: List[String] = {
    basicDriverConfig.serverSection.clusterHosts.toList.map(host => "akka.tcp://"
      + basicDriverConfig.serverSection.clusterName + "@" + host + "/user/receptionist")
  }
  //For Futures
  implicit val context = system.dispatcher
  var userId: String = ""
  var userName: String = ""
  var currentCatalog: String = ""

  val list:Option[java.util.List[AnyRef]]=None

  def this() {
    this(BasicDriver.getBasicDriverConfigFromFile)
  }

  def this(servers:Array[String]) {
    this(BasicDriver.getBasicDriverConfigFromFile(servers))
  }

  /**
   * Release connection to CrossdataServer.
   * @param user Login to the user (Audit only).
   * @return ConnectResult.
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
        userId = ""
      }
    }
  }

  /**
   * Execute a query in the Crossdata server asynchronously.
   * @param query The query.
   * @param callback The callback object.
   */
  @throws(classOf[ConnectionException])
  def asyncExecuteQuery(query: String, callback: IDriverResultHandler): String = {
    if (userId.isEmpty) {
      throw new ConnectionException("You must connect to cluster")
    }
    val queryId = UUID.randomUUID()
    queries.put(queryId.toString, callback)
    sendQuery(new Query(queryId.toString, currentCatalog, query, userId))
    queryId.toString
  }

  def sendQuery(message: AnyRef) {
    if (userId.isEmpty) {
      throw new ConnectionException("You must connect to cluster")
    }
    proxyActor.ask(message)(5 second)
  }

  /**
   * Launch query in Crossdata Server
   * @param query Launched query
   * @return QueryResult
   */
  @throws(classOf[ConnectionException])
  @throws(classOf[ParsingException])
  @throws(classOf[ValidationException])
  @throws(classOf[ExecutionException])
  @throws(classOf[UnsupportedException])
  def executeQuery(query: String): Result = {
    if (userId.isEmpty) {
      throw new ConnectionException("You must connect to cluster")
    }
    val queryId = UUID.randomUUID()
    val callback = new SyncDriverResultHandler
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
    if (userId.isEmpty) {
      throw new ConnectionException("You must connect to cluster")
    }
    val queryId = UUID.randomUUID().toString
    val result = retryPolitics.askRetry(proxyActor, new Command(queryId, APICommand.LIST_CATALOGS, null), 30 second, retry = 2)
    if(result.isInstanceOf[MetadataResult]){
      result.asInstanceOf[MetadataResult]
    } else {
      MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_UNKNOWN)
    }
  }

  /**
   * List the existing tables in a database catalog.
   * @return A MetadataResult with a list of tables, or the object with hasError set
   *         containing the error message.
   */
  def listTables(catalogName: String): MetadataResult = {
    if (userId.isEmpty) {
      throw new ConnectionException("You must connect to cluster")
    }
    val params: java.util.List[AnyRef] = new java.util.ArrayList[AnyRef]
    params.add(catalogName)
    val queryId = UUID.randomUUID().toString
    val result = retryPolitics.askRetry(proxyActor, new Command(queryId, APICommand.LIST_TABLES, params), 30 second, retry = 2)
    if(result.isInstanceOf[MetadataResult]){
      result.asInstanceOf[MetadataResult]
    } else {
      MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_UNKNOWN)
    }
  }

  /**
   * List the existing tables in a database catalog.
   * @return A MetadataResult with a map of columns.
   */
  def listFields(catalogName: String, tableName: String): MetadataResult = {
    if (userId.isEmpty) {
      throw new ConnectionException("You must connect to cluster")
    }
    val params: java.util.List[AnyRef] = new java.util.ArrayList[AnyRef]
    params.add(catalogName)
    params.add(tableName)
    val queryId = UUID.randomUUID().toString
    val result = retryPolitics.askRetry(proxyActor, new Command(queryId, APICommand.LIST_COLUMNS, params), 30 second)
    if(result.isInstanceOf[MetadataResult]){
      result.asInstanceOf[MetadataResult]
    } else {
      MetadataResult.createSuccessMetadataResult(MetadataResult.OPERATION_UNKNOWN)
    }
  }

  /**
   * Send manifest to the server.
   * @param manifest The manifest to be sent.
   * @return A CommandResult with a string.
   */
  @throws(classOf[ManifestException])
  def addManifest(manifest: CrossdataManifest): Result = {
    if (userId.isEmpty) {
      throw new ConnectionException("You must connect to cluster")
    }
    val params: java.util.List[AnyRef] = new java.util.ArrayList[AnyRef]
    params.add(manifest)
    val queryId = UUID.randomUUID().toString
    val result = retryPolitics.askRetry(proxyActor, new Command(queryId, APICommand.ADD_MANIFEST, params), 30 second)
    if(result.isInstanceOf[CommandResult]){
      result.asInstanceOf[CommandResult]
    } else {
      result.asInstanceOf[ErrorResult]
    }
  }

  @throws(classOf[ManifestException])
  def addManifest(manifestType: Int, path: String): Result = {
    if (userId.isEmpty) {
      throw new ConnectionException("You must connect to cluster")
    }
    // Create CrossdataManifest object from XML file
    var manifest: CrossdataManifest = null

    try {
      manifest = ManifestUtils.parseFromXmlToManifest(
        manifestType, path.replace(";", "").replace("\"", "").replace("'", ""))
    }
    catch {
      case e: Any => {
        logger.error("CrossdataManifest couldn't be parsed", e)
        throw new ManifestException(e)
      }
    }
    addManifest(manifest)
  }

  /**
   * Drop manifest in the server.
   * @param manifestType The type of the manifest.
   * @param manifestName The name of the manifest.
   * @return A CommandResult with a string.
   */
  @throws(classOf[ManifestException])
  def dropManifest(manifestType: Int, manifestName: String): Result = {
    if (userId.isEmpty) {
      throw new ConnectionException("You must connect to cluster")
    }
    val params: java.util.List[AnyRef] = new java.util.ArrayList[AnyRef]
    params.add(manifestType.toString);
    params.add(manifestName)
    val queryId = UUID.randomUUID().toString
    val result = retryPolitics.askRetry(proxyActor, new Command(queryId, APICommand.DROP_MANIFEST, params), 30 second)
    result.asInstanceOf[Result]
  }

  /**
   * Reset metadata in server.
   * @return A CommandResult with a string.
   */
  def resetServerdata(): Result = {
    if (userId.isEmpty) {
      throw new ConnectionException("You must connect to cluster")
    }
    val queryId = UUID.randomUUID().toString
    val result = retryPolitics.askRetry(proxyActor, new Command(queryId, APICommand.RESET_SERVERDATA, null), 30 second)
    if(result.isInstanceOf[CommandResult]){
      result.asInstanceOf[CommandResult]
    } else {
      result.asInstanceOf[ErrorResult]
    }
  }

  /**
   * Clean metadata related to catalogs in server.
   * @return A CommandResult with a string.
   */
  def cleanMetadata(): Result = {
    if (userId.isEmpty) {
      throw new ConnectionException("You must connect to cluster")
    }
    val queryId = UUID.randomUUID().toString
    val result = retryPolitics.askRetry(proxyActor, new Command(queryId, APICommand.CLEAN_METADATA, null), 30 second)
    if(result.isInstanceOf[CommandResult]){
      result.asInstanceOf[CommandResult]
    } else {
      result.asInstanceOf[ErrorResult]
    }
  }

  /**
   * Describe the connectors available.
   * @return A CommandResult with the list.
   */
  def describeConnectors():CommandResult = {
    if (userId.isEmpty) {
      throw new ConnectionException("You must connect to cluster")
    }
    val queryId = UUID.randomUUID().toString
    val result = retryPolitics.askRetry(proxyActor, new Command(queryId, APICommand.DESCRIBE_CONNECTORS, null), 30 second)
    if(result.isInstanceOf[CommandResult]){
      result.asInstanceOf[CommandResult]
    } else {
      val errorResult = result.asInstanceOf[ErrorResult]
      CommandResult.createCommandResult(errorResult.getErrorMessage)
    }
  }

  /**
   * Describe a connector.
   * @return A CommandResult with the description of the connector.
   */
  def describeConnector(connectorName: ConnectorName): Result = {
    if (userId.isEmpty) {
      throw new ConnectionException("You must connect to cluster")
    }
    val queryId = UUID.randomUUID().toString
    val params: java.util.List[AnyRef] = new java.util.ArrayList[AnyRef]
    params.add(connectorName)
    val result = retryPolitics.askRetry(proxyActor, new Command(queryId, APICommand.DESCRIBE_CONNECTOR, params), 30 second)
    if(result.isInstanceOf[CommandResult]){
      result.asInstanceOf[CommandResult]
    } else {
      result.asInstanceOf[ErrorResult]
    }
  }

  def describeCatalog(catalogName: CatalogName): Result = {
    if (userId.isEmpty) {
      throw new ConnectionException("You must connect to cluster")
    }
    val queryId = UUID.randomUUID().toString
    val params: java.util.List[AnyRef] = new java.util.ArrayList[AnyRef]
    params.add(catalogName)
    val result = retryPolitics.askRetry(proxyActor, new Command(queryId, APICommand.DESCRIBE_CATALOG, params), 30 second)
    if(result.isInstanceOf[CommandResult]){
      result.asInstanceOf[CommandResult]
    } else {
      result.asInstanceOf[ErrorResult]
    }
  }

  /**
   * Describe a datastore.
   * @return A CommandResult with the description of the datastore.
   */
  def describeDatastore(datastoreName: DataStoreName): Result = {
    if (userId.isEmpty) {
      throw new ConnectionException("You must connect to cluster")
    }
    val queryId = UUID.randomUUID().toString
    val params: java.util.List[AnyRef] = new java.util.ArrayList[AnyRef]
    params.add(datastoreName)
    val result = retryPolitics.askRetry(proxyActor, new Command(queryId, APICommand.DESCRIBE_DATASTORE, params), 30 second)
    if(result.isInstanceOf[CommandResult]){
      result.asInstanceOf[CommandResult]
    } else {
      result.asInstanceOf[ErrorResult]
    }
  }

  def describeTables(catalogName: CatalogName): Result = {
    if (userId.isEmpty) {
      throw new ConnectionException("You must connect to cluster")
    }
    val queryId = UUID.randomUUID().toString
    val params: java.util.List[AnyRef] = new java.util.ArrayList[AnyRef]
    params.add(catalogName)
    val result = retryPolitics.askRetry(proxyActor, new Command(queryId, APICommand.DESCRIBE_TABLES, params), 30 second)
    if(result.isInstanceOf[CommandResult]){
      result.asInstanceOf[CommandResult]
    } else {
      result.asInstanceOf[ErrorResult]
    }
  }

  def describeTable(tableName: TableName): Result = {
    if (userId.isEmpty) {
      throw new ConnectionException("You must connect to cluster")
    }
    val queryId = UUID.randomUUID().toString
    val params: java.util.List[AnyRef] = new java.util.ArrayList[AnyRef]
    params.add(tableName)
    val result = retryPolitics.askRetry(proxyActor, new Command(queryId, APICommand.DESCRIBE_TABLE, params), 30 second)
    if(result.isInstanceOf[CommandResult]){
      result.asInstanceOf[CommandResult]
    } else {
      result.asInstanceOf[ErrorResult]
    }
  }

  def describeCluster(clusterName: ClusterName): Result = {
    if (userId.isEmpty) {
      throw new ConnectionException("You must connect to cluster")
    }
    val queryId = UUID.randomUUID().toString
    val params: java.util.List[AnyRef] = new java.util.ArrayList[AnyRef]
    params.add(clusterName)
    val result = retryPolitics.askRetry(proxyActor, new Command(queryId, APICommand.DESCRIBE_CLUSTER, params), 30 second)
    if(result.isInstanceOf[CommandResult]){
      result.asInstanceOf[CommandResult]
    } else {
      result.asInstanceOf[ErrorResult]
    }
  }

  def describeDatastores(): Result = {
    if (userId.isEmpty) {
      throw new ConnectionException("You must connect to cluster")
    }
    val queryId = UUID.randomUUID().toString
    val result = retryPolitics.askRetry(proxyActor, new Command(queryId, APICommand.DESCRIBE_DATASTORES, null), 30 second)
    if(result.isInstanceOf[CommandResult]){
      result.asInstanceOf[CommandResult]
    } else {
      result.asInstanceOf[ErrorResult]
    }
  }

  def describeClusters(): Result = {
    if (userId.isEmpty) {
      throw new ConnectionException("You must connect to cluster")
    }
    val queryId = UUID.randomUUID().toString
    val result = retryPolitics.askRetry(proxyActor, new Command(queryId, APICommand.DESCRIBE_CLUSTERS, null), 30 second)
    if(result.isInstanceOf[CommandResult]){
      result.asInstanceOf[CommandResult]
    } else {
      result.asInstanceOf[ErrorResult]
    }
  }

  /**
   * Describe the system.
   * @return A CommandResult with the description of the system.
   */
  def describeSystem():CommandResult = {
    if (userId.isEmpty) {
      throw new ConnectionException("You must connect to cluster")
    }
    val queryId = UUID.randomUUID().toString
    val result = retryPolitics.askRetry(proxyActor, new Command(queryId, APICommand.DESCRIBE_SYSTEM, null), 30 second)
    if(result.isInstanceOf[CommandResult]){
      result.asInstanceOf[CommandResult]
    } else {
      val errorResult = result.asInstanceOf[ErrorResult]
      CommandResult.createCommandResult(errorResult.getErrorMessage)
    }
  }

  /**
   * Return the explained execution workflow for a given query.
   * @param query The user query.
   * @return A Result.
   */
  def explainPlan(query: String): Result = {
    if (userId.isEmpty) {
      throw new ConnectionException("You must connect to cluster")
    }
    val params: java.util.List[AnyRef] = new java.util.ArrayList[AnyRef]
    params.add(query)
    params.add(currentCatalog)
    val queryId = UUID.randomUUID().toString
    val result = retryPolitics.askRetry(proxyActor, new Command(queryId, APICommand.EXPLAIN_PLAN, params), 30 second)
    result
  }

  /**
   * Get the IDriverResultHandler associated with a query identifier.
   * @param queryId Query identifier.
   * @return The result handler.
   */
  def getResultHandler(queryId: String): IDriverResultHandler = {
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
   * Shutdown actor system.
   */
  def close() {
    system.shutdown()
  }

  /**
   * This method get the UserName.
   * @return the value of userName.
   * */
  def getUserName: String = userName

  def setUserName(userName: String) {
    this.userName = userName
    if (userName.isEmpty) {
      this.userName = DEFAULT_USER
    }
  }

  /**
   * This method get the Current catalog.
   * @return  is the Catalog we are using.
   * */
  def getCurrentCatalog: String = {
    currentCatalog
  }

  def setCurrentCatalog(catalog: String) {
    this.currentCatalog = catalog
  }
}
