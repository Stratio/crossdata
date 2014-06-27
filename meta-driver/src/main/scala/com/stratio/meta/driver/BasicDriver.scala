/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.driver

import akka.actor.{ ActorSelection, ActorSystem}
import com.stratio.meta.driver.config.{DriverSectionConfig, ServerSectionConfig, BasicDriverConfig, DriverConfig}
import akka.contrib.pattern.ClusterClient
import com.stratio.meta.driver.actor.ProxyActor
import com.stratio.meta.common.result._
import com.stratio.meta.common.ask.APICommand
import org.apache.log4j.Logger
import  scala.concurrent.duration._
import java.util.UUID
import akka.pattern.ask
import com.stratio.meta.driver.result.SyncResultHandler
import com.stratio.meta.common.exceptions._
import com.stratio.meta.common.ask.Connect
import com.stratio.meta.common.ask.Command
import com.stratio.meta.common.ask.Query
import com.stratio.meta.communication.Disconnect
import com.stratio.meta.driver.utils.RetryPolitics

object BasicDriver extends DriverConfig {
  /**
   * Class logger.
   */
  override lazy val logger = Logger.getLogger(getClass)

  def getBasicDriverConfigFromFile ={
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

  lazy val logger= BasicDriver.logger

  lazy val queries: java.util.Map[String, IResultHandler] = new java.util.HashMap[String, IResultHandler]

  lazy val system = ActorSystem("MetaDriverSystem",BasicDriver.config)
  //For Futures
  implicit val context = system.dispatcher
  lazy val initialContacts: Set[ActorSelection] = contactPoints.map(contact=> system.actorSelection(contact)).toSet
  lazy val clusterClientActor = system.actorOf(ClusterClient.props(initialContacts),"remote-client")
  lazy val proxyActor = system.actorOf(ProxyActor.props(clusterClientActor,basicDriverConfig.serverSection.clusterActor, this), "proxy-actor")

  lazy val retryPolitics: RetryPolitics = {
    new RetryPolitics(basicDriverConfig.driverSection.retryTimes, basicDriverConfig.driverSection.retryDuration.millis)
  }
  lazy val contactPoints: List[String]= {
    basicDriverConfig.serverSection.clusterHosts.toList.map(host=>"akka.tcp://" + basicDriverConfig.serverSection.clusterName + "@" + host + "/user/receptionist")
  }

  var userId: String = null

  def this() {
    this(BasicDriver.getBasicDriverConfigFromFile)
  }

  /**
   * Release connection to MetaServer.
   * @param user Login to the user (Audit only)
   * @return ConnectResult
   */
  @throws(classOf[ConnectionException])
  def connect(user:String): Result = {
    logger.info("Establishing connection with user: " + user + " to " + contactPoints)
    val result = retryPolitics.askRetry(proxyActor,new Connect(user),5 second)
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
   * @param user The user login.
   * @param targetCatalog The target catalog.
   * @param query The query.
   * @param callback The callback object.
   * @deprecated  As of release 0.0.5, replaced by asyncExecuteQuery(targetCatalog, query, callback)}
   */
  @deprecated(message = "You should use asyncExecuteQuery(targetCatalog, query, callback)", since = "0.0.5")
  @throws(classOf[ConnectionException])
  def asyncExecuteQuery(user:String, targetCatalog: String, query: String, callback: IResultHandler) : String = {
    logger.warn("You use a deprecated method. User parameter (" + user + ") will be ignored")
    asyncExecuteQuery(targetCatalog, query, callback)
  }

  /**
   * Execute a query in the Meta server asynchronously.
   * @param targetCatalog The target catalog.
   * @param query The query.
   * @param callback The callback object.
   */
  @throws(classOf[ConnectionException])
  def asyncExecuteQuery(targetCatalog: String, query: String, callback: IResultHandler) : String = {
    if(userId==null){
      throw new ConnectionException("You must connect to cluster")
    }
    val queryId = UUID.randomUUID()
    queries.put(queryId.toString, callback)
    sendQuery(new Query(queryId.toString, targetCatalog, query, userId))
    queryId.toString
  }

  /**
   * Launch query in Meta Server
   * @param user Login the user (Audit only)
   * @param targetKs Target keyspace
   * @param query Launched query
   * @return QueryResult
   * @deprecated  As of release 0.0.5, replaced by asyncExecuteQuery(targetCatalog, query, callback)}
   */
  @throws(classOf[ConnectionException])
  @throws(classOf[ParsingException])
  @throws(classOf[ValidationException])
  @throws(classOf[ExecutionException])
  @throws(classOf[UnsupportedException])
  @deprecated(message = "You should use executeQuery(targetKs, query)", since = "0.0.5")
  def executeQuery(user: String, targetKs: String, query: String): Result = {
    logger.warn("You use a deprecated method. User parameter (" + user + ") will be ignored")
    executeQuery(targetKs, query)
  }
  /**
   * Launch query in Meta Server
   * @param targetKs Target keyspace
   * @param query Launched query
   * @return QueryResult
   */
  @throws(classOf[ConnectionException])
  @throws(classOf[ParsingException])
  @throws(classOf[ValidationException])
  @throws(classOf[ExecutionException])
  @throws(classOf[UnsupportedException])
  def executeQuery(targetKs: String, query: String): Result = {
    if(userId==null){
      throw new ConnectionException("You must connect to cluster")
    }
    val queryId = UUID.randomUUID()
    val callback = new SyncResultHandler
    queries.put(queryId.toString, callback)
    sendQuery(new Query(queryId.toString, targetKs, query, userId))
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
    val params: java.util.List[String] = new java.util.ArrayList[String]
    params.add(catalogName)
    val result = retryPolitics.askRetry(proxyActor, new Command(APICommand.LIST_TABLES, params))
    result.asInstanceOf[MetadataResult]
  }

  /**
   * List the existing tables in a database catalog.
   * @return A MetadataResult with a map of columns.
   */
  def listFields(catalogName: String, tableName: String): MetadataResult = {
    val params: java.util.List[String] = new java.util.ArrayList[String]
    params.add(catalogName)
    params.add(tableName)
    val result = retryPolitics.askRetry(proxyActor, new Command(APICommand.LIST_COLUMNS, params))
    result.asInstanceOf[MetadataResult]
  }

  def sendQuery(message: AnyRef){
    proxyActor.ask(message)(5 second)
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
  def removeResultHandler(queryId: String) : Boolean = {
    queries.remove(queryId) != null
  }

  /**
   * Shutdown actor system
   */
  def close() {
    system.shutdown()
  }

}