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
import java.util.UUID

import com.stratio.crossdata.common.ask.{APICommand, Command, Query}
import com.stratio.crossdata.common.data._
import com.stratio.crossdata.common.exceptions._
import com.stratio.crossdata.common.exceptions.validation.{ExistNameException, NotExistNameException}
import com.stratio.crossdata.common.manifest.CrossdataManifest
import com.stratio.crossdata.common.result._
import com.stratio.crossdata.common.utils.Constants
import com.stratio.crossdata.driver.BasicDriver._
import com.stratio.crossdata.driver.result.{PaginationSyncDriverResultHandler, SyncDriverResultHandler}
import com.stratio.crossdata.driver.utils.{ManifestUtils, QueryData}
import org.apache.log4j.Logger

import scala.concurrent.duration._
import scala.collection.JavaConversions._
import scala.util.parsing.json

object DriverConnection{
  lazy val logger = Logger.getLogger(getClass)
}
class DriverConnection(val sessionId: String, userId: String,  basicDriver: BasicDriver) {

  lazy val queries: java.util.Map[String, QueryData] = new java.util.HashMap[String, QueryData]
  lazy val queriesWebUI: java.util.Map[String, QueryData] = new java.util.HashMap[String, QueryData]
  lazy val connectorOfQueries: java.util.Map[String, String] = new java.util.HashMap[String, String]

  var currentCatalog: String = ""

  /**
   * Execute a query in the Crossdata server asynchronously.
   * @param query The query.
   * @param callback The callback object.
   */
  @throws(classOf[ConnectionException])
  def asyncExecuteQuery(query: com.stratio.crossdata.driver.querybuilder.Query,
                        callback: IDriverResultHandler): Result = {
    asyncExecuteQuery(query.toString, callback)
  }

  /**
   * Execute a query in the Crossdata server asynchronously.
   * @param query The query.
   * @param callback The callback object.
   */
  def asyncExecuteQuery(query: String, callback: IDriverResultHandler): Result = {
    val queryId = UUID.randomUUID()
    queries.put(queryId.toString, new QueryData(callback, queryId.toString, query, sessionId,System.currentTimeMillis(),0,
      "IN PROGRESS"))
    queriesWebUI.put(queryId.toString, new QueryData(callback, queryId.toString,query, sessionId,
      System.currentTimeMillis(),0,"IN PROGRESS"))
    sendQuery(new Query(queryId.toString, currentCatalog, query, userId, sessionId))
    InProgressResult.createInProgressResult(queryId.toString)
  }

  @throws(classOf[ConnectionException])
  private def sendQuery(message: AnyRef) {
    basicDriver.proxyActor ! message
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
  def executeQuery(query: com.stratio.crossdata.driver.querybuilder.Query): Result = {
    executeQuery(query.toString)
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
    val queryId = UUID.randomUUID().toString
    val callback = new SyncDriverResultHandler
    queries.put(queryId, new QueryData(callback, queryId.toString,query, sessionId,System.currentTimeMillis(),0,"IN PROGRESS"))
    queriesWebUI.put(queryId, new QueryData(callback, queryId.toString,query, sessionId,System.currentTimeMillis(),0,"IN PROGRESS"))
    sendQuery(new Query(queryId, currentCatalog, query, userId, sessionId))
    val r = callback.waitForResult()
    queriesWebUI.get(queryId).setStatus("DONE")
    queriesWebUI.get(queryId).setEndTime(System.currentTimeMillis())
    logger.info("Query " + queryId + " finished. " + queries.get(queryId).getExecutionInfo())
    queries.remove(queryId)
    r
  }


  /**
   * Launch query in Crossdata Server
   * @param query Launched query
   * @param buildUpRows Whether a query launched against a connector using pagination collects the whole result or solely returns the first one
   * @return QueryResult
   */
  @throws(classOf[ConnectionException])
  @throws(classOf[ParsingException])
  @throws(classOf[ValidationException])
  @throws(classOf[ExecutionException])
  @throws(classOf[UnsupportedException])
  def executeQuery(query: String, buildUpRows: Boolean): Result = {
    val queryId = UUID.randomUUID().toString
    val callback = if (buildUpRows)
      new PaginationSyncDriverResultHandler
    else
      new SyncDriverResultHandler

    queries.put(queryId, new QueryData(callback, queryId.toString,query, sessionId,System.currentTimeMillis(),0,"IN PROGRESS"))
    queriesWebUI.put(queryId, new QueryData(callback, queryId.toString,query, sessionId,System.currentTimeMillis(),0,"IN PROGRESS"))
    sendQuery(new Query(queryId, currentCatalog, query, userId, sessionId))
    val r = callback.waitForResult()
    queriesWebUI.get(queryId).setStatus("DONE")
    queriesWebUI.get(queryId).setEndTime(System.currentTimeMillis())
    logger.info("Query " + queryId + " finished. " + queries.get(queryId).getExecutionInfo())
    queries.remove(queryId)
    r
  }



  def executeRawQuery(command: String): Result = {
    executeAsyncRawQuery(command, null)
  }

  def executeAsyncRawQuery(command: String, callback: IDriverResultHandler): Result = {
    var result:Result = null.asInstanceOf[Result]
    val input:String = command.replaceAll("\\s+", " ").trim
    if(input.toLowerCase.startsWith("use ")){
      result = updateCatalog(input)
    } else {
      result = executeApiCall(input)
      if(result.isInstanceOf[EmptyResult]){
        if(callback != null){
          result = asyncExecuteQuery(input, callback)
        } else {
          result = executeQuery(input)
        }
      }
    }
    result
  }



  def createCatalog(catalogName: String, ifNotExist: Boolean = false, options: Map[String, Any] = Map.empty[String, Any]): Result = {
    val strBuilder = StringBuilder.newBuilder
    strBuilder append s"CREATE CATALOG"
    if (ifNotExist) strBuilder append " IF NOT EXISTS"
    strBuilder append s" $catalogName"
    if (options.nonEmpty) strBuilder append " WITH " append json.JSONObject(options)
    executeQuery(strBuilder append ";" mkString)
  }

  //JAVA API
  def createCatalog(catalogName: String, ifNotExist: Boolean, options: util.Map[String, Any]): Result = {
    createCatalog(catalogName, ifNotExist, options.toMap)
  }

  def createTable( catalogName: String, tableName: String,  clusterName: String, columnTypeMap: Map[String, String], partitionKey: List[String], clusteringKeys: List[String] = List.empty[String], ifNotExist: Boolean = false, isExternal: Boolean = false, options: Map[String, Any] = Map.empty[String, Any]): Result = {
    val strBuilder = StringBuilder.newBuilder
     strBuilder append { if (isExternal) "REGISTER" else "CREATE"}
    strBuilder append " TABLE"
    if (ifNotExist) strBuilder append " IF NOT EXISTS"
    strBuilder append s" $catalogName.$tableName ON CLUSTER $clusterName "
    require(columnTypeMap.nonEmpty, "The table must have a schema")
    strBuilder append(columnTypeMap mkString("(" ,", ", ",") replaceAll("->", ""))
    require(columnTypeMap.nonEmpty, "The table must have a schema")
    strBuilder append " PRIMARY KEY ("
    strBuilder.append(partitionKey mkString(" (",", ", ")") replaceAll("->", ""))
    if (clusteringKeys.nonEmpty) strBuilder.append( clusteringKeys mkString(" , ", ", ", ""))//add csv clustering keys
    strBuilder append " )" //CLOSE KEYS DEFINITION
    strBuilder append " )" //CLOSE VALUES
    if (options.nonEmpty) strBuilder append " WITH " append json.JSONObject(options)
    executeQuery(strBuilder append ";" mkString)
  }

  //JAVA API
  def createTable( catalogName: String, tableName: String,  clusterName: String, columnTypeMap: util.Map[String, String], partitionKey: util.List[String], clusterKey: util.List[String], ifNotExist: Boolean , isExternal: Boolean , options: util.Map[String, Any]): Result = {
    createTable(catalogName,tableName,clusterName, columnTypeMap.toMap, partitionKey.toList, clusterKey.toList, ifNotExist, isExternal, options.toMap)
  }

  def insert( catalogName: String, tableName: String,  columnValue: Map[String, Any], ifNotExist: Boolean = false, options: Map[String, Any] = Map.empty[String, Any]): Result = {
    val strBuilder = StringBuilder.newBuilder
    strBuilder append s"INSERT INTO $catalogName.$tableName"
    require(columnValue.nonEmpty)
    strBuilder append( columnValue.keys mkString(" (", ", ", ")"))
    strBuilder append " VALUES "
    strBuilder append(mkStringValues(columnValue.values))
    if (ifNotExist) strBuilder append " IF NOT EXISTS"
    if (options.nonEmpty) strBuilder append " WITH " append json.JSONObject(options)
    executeQuery(strBuilder append ";" mkString)
  }

  //JAVA API
  def insert( catalogName: String, tableName: String,  columnValue: util.Map[String, Object], ifNotExist: Boolean, options: util.Map[String, Any]): Result = {
    insert(catalogName,tableName,columnValue.toMap, ifNotExist, options.toMap)
  }


  def attachCluster(clusterName: String, datastore: String, ifNotExists: Boolean = false, options: Map[String, Any] = Map.empty[String, Any]): Result = {
    val strBuilder = StringBuilder.newBuilder
    strBuilder append s"ATTACH CLUSTER"
    if (ifNotExists) strBuilder append " IF NOT EXISTS"
    strBuilder append s" $clusterName ON DATASTORE $datastore"
    if (options.nonEmpty) strBuilder append " WITH OPTIONS " append json.JSONObject(options)
    executeQuery(strBuilder append ";" mkString)
  }

  //JAVA API
  def attachCluster(clusterName: String, datastore: String, ifNotExists: Boolean, options: util.Map[String, Any]): Result = {
    attachCluster(clusterName, datastore, ifNotExists, options.toMap)
  }

  def attachConnector(connectorName: String, clusterName: String, options: Map[String, Any] = Map.empty[String, Any], pagination: Int = Constants.DEFAULT_PAGINATION, priority: Int = Constants.DEFAULT_PRIORITY): Result = {
    val strBuilder = StringBuilder.newBuilder
    strBuilder append s"ATTACH CONNECTOR $connectorName TO $clusterName"
    if (options.nonEmpty) strBuilder append " WITH OPTIONS " append json.JSONObject(options)
    strBuilder append s" AND PRIORITY = $priority AND PAGINATION = $pagination"
    executeQuery(strBuilder append ";" mkString)
  }

  //JAVA API
  def attachConnector(connectorName: String, clusterName: String, options: util.Map[String, Any], pagination: Int, priority: Int): Result = {
    attachConnector(connectorName, clusterName, options.toMap, pagination, priority)
  }

  def dropTable( catalogName: String, tableName: String, ifExists: Boolean = false, isExternal: Boolean = false): Result = {
    val strBuilder = StringBuilder.newBuilder
    strBuilder append { if (isExternal) "UNREGISTER" else "DROP"} append " TABLE"
    if (ifExists) strBuilder append " IF EXISTS"
    strBuilder append s" $catalogName.$tableName"
    executeQuery(strBuilder append ";" mkString)
  }

  def dropCatalog( catalogName: String, ifExists: Boolean = false): Result = {
    val strBuilder = StringBuilder.newBuilder
    strBuilder append "DROP CATALOG"
    if (ifExists) strBuilder append " IF EXISTS"
    strBuilder append s" $catalogName"
    executeQuery(strBuilder append ";" mkString)
  }

  private def mkStringValues(values: Iterable[Any]): String = {
    val stBuilder = StringBuilder.newBuilder
    stBuilder append "( "
    values.head match {
      case str: String => stBuilder append s"'$str'"
      case other => stBuilder append s"${other.toString}"
    }
    values.tail.foldLeft(stBuilder) {
      case (builder, value) => value match {
        case str: String => builder append s", '$str'"
        case _ => builder append s", ${value.toString}"
      }
    }
    stBuilder append ")"
    stBuilder mkString
  }


  @throws(classOf[NotExistNameException])
  def updateCatalog(toExecute: String): Result = {
    //val newCatalog: String = toExecute.toLowerCase.replace("use ", "").replace(";", "").trim
    val newCatalog: String = toExecute.substring(4).replace(";", "").trim
    var currentCatalog: String = getCurrentCatalog
    if (newCatalog.isEmpty) {
      setCurrentCatalog(newCatalog)
      currentCatalog = newCatalog
    } else {
      val catalogs: java.util.List[String] = (listCatalogs().asInstanceOf[MetadataResult]).getCatalogList
      if (catalogs.contains(newCatalog)) {
        setCurrentCatalog(newCatalog)
        currentCatalog = newCatalog
      } else {
        throw new NotExistNameException(new CatalogName(newCatalog));
      }
    }
    CommandResult.createCommandResult(new CatalogName(currentCatalog));
  }

  def executeApiCall(command: String): Result = {
    /**
     * Constant to define the prefix for explain plan operations.
     */
    val EXPLAIN_PLAN_TOKEN: String = "explain plan for"
    val STOP_PROCESS_TOKEN = "stop process"
    var result: Result = null
    result = EmptyResult.createEmptyResult()
    if (command.toLowerCase.startsWith("describe")) {
      result = executeDescribe(command)
    } else if (command.toLowerCase.startsWith("add connector") || command.toLowerCase.startsWith("add datastore")) {
      result = sendManifest(command)
    } else if (command.toLowerCase.startsWith("reset serverdata")) {
      result = resetServerdata()
    } else if (command.toLowerCase.startsWith("clean metadata")) {
      result = cleanMetadata()
    }else if (command.toLowerCase.startsWith("list queries")) {
      result = listQueries()
    } else if (command.toLowerCase.startsWith("drop datastore")) {
      result = dropManifest(
        CrossdataManifest.TYPE_DATASTORE, command.substring(15).replace(";", "").trim)
    } else if (command.toLowerCase.startsWith("drop connector")) {
      result = dropManifest(
        CrossdataManifest.TYPE_CONNECTOR, command.substring(15).replace(";", "").trim)
    } else if (command.toLowerCase.startsWith(EXPLAIN_PLAN_TOKEN)) {
      result = explainPlan(command)
    } else if (command.toLowerCase.startsWith(STOP_PROCESS_TOKEN)){
      result = stopProcess(command.substring(STOP_PROCESS_TOKEN.length).replace(";", "").trim);
    }
    result
  }


  def stopProcess(processQueryId: String): Result = {
    val params: java.util.List[AnyRef] = new java.util.ArrayList[AnyRef]
    params.add(processQueryId)
    val queryId = UUID.randomUUID().toString
    basicDriver.retryPolitics.askRetry(basicDriver.proxyActor, new Command(queryId, APICommand.STOP_PROCESS, params,sessionId), 5 second)

  }


  def discoverMetadata(clusterName: String): Result = {
    executeQuery("DISCOVER METADATA ON CLUSTER " + clusterName + ";")
  }

  def importCatalogs(clusterName: String): Result = {
    executeQuery("IMPORT CATALOGS FROM CLUSTER " + clusterName + ";")
  }

  def importCatalog(clusterName: String, catalogName: String): Result = {
    executeQuery("IMPORT CATALOG " + catalogName + " FROM CLUSTER " + clusterName + ";")
  }

  def importTable(clusterName: String, tableName: String): Result = {
    executeQuery("IMPORT TABLE " + tableName + " FROM CLUSTER " + clusterName + ";")
  }

  /**
   * Send a manifest through the driver.
   *
   * @param sentence The sentence introduced by the user.
   * @return The operation result.
   */
  @throws(classOf[ManifestException])
  def sendManifest(sentence: String): Result = {
    val tokens: Array[String] = sentence.split(" ")
    if (tokens.length < 3) {
      throw new ManifestException("ERROR: Invalid ADD syntax")
    }
    var typeManifest: Int = 0
    if (tokens(1).equalsIgnoreCase("datastore")) {
      typeManifest = CrossdataManifest.TYPE_DATASTORE
    } else if (tokens(1).equalsIgnoreCase("connector")) {
      typeManifest = CrossdataManifest.TYPE_CONNECTOR
    } else {
      throw new ManifestException("ERROR: Unknown type: " + tokens(1))
    }
    addManifest(typeManifest, tokens(2))
  }

  def executeDescribe(command: String): Result = {
    var result: Result = null
    if (command.toLowerCase.startsWith("describe system")) {
      result = describeSystem()
    } else if (command.toLowerCase.startsWith("describe datastores")) {
      result = describeDatastores()
    } else if (command.toLowerCase.startsWith("describe datastore ")) {
      //val datastore = command.toLowerCase.replace("describe datastore ", "").replace(";", "").trim
      val datastore = command.substring(19).replace(";", "").trim
      result = describeDatastore(new DataStoreName(datastore))
    } else if (command.toLowerCase.startsWith("describe clusters")) {
      result = describeClusters()
    } else if (command.toLowerCase.startsWith("describe cluster ")) {
      //val cluster = command.toLowerCase.replace("describe cluster ", "").replace(";", "").trim
      val cluster = command.substring(17).replace(";", "").trim
      result = describeCluster(new ClusterName(cluster))
    } else if (command.toLowerCase.startsWith("describe connectors")) {
      result = describeConnectors()
    } else if (command.toLowerCase.startsWith("describe connector ")) {
      //val connector = command.toLowerCase.replace("describe connector ", "").replace(";", "").trim
      val connector = command.substring(19).replace(";", "").trim
      result = describeConnector(new ConnectorName(connector))
    } else if (command.toLowerCase.startsWith("describe catalogs")) {
      result = listCatalogs()
    } else if (command.toLowerCase.startsWith("describe catalog")) {
      //val catalog = command.toLowerCase.replace("describe catalog ", "").replace(";", "").trim
      val catalog = command.substring(17).replace(";", "").trim
      var catalogName: CatalogName = new CatalogName(catalog)
      if(catalog.isEmpty){
        catalogName = new CatalogName(getCurrentCatalog)
      }
      result = describeCatalog(catalogName)
    } else if (command.toLowerCase.startsWith("describe tables")) {
      if (command.toLowerCase.startsWith("describe tables from ")) {
        //val catalog = command.toLowerCase.replace("describe tables from ", "").replace(";", "").trim
        val catalog = command.substring(21).replace(";", "").trim
        result = describeTables(new CatalogName(catalog))
      } else if (!getCurrentCatalog.isEmpty) {
        result = describeTables(new CatalogName(getCurrentCatalog))
      } else {
        result = Result.createErrorResult(new Exception("Catalog not specified"))
      }
    } else if (command.toLowerCase.startsWith("describe table ")) {
      if (command.toLowerCase.replace(";", "").trim.equalsIgnoreCase("describe table")) {
        result = Result.createErrorResult(new Exception("Table name is missing"))
      } else if (command.toLowerCase.startsWith("describe table ") &&
        command.toLowerCase.replace("describe table ", "").replace(";", "").trim.contains(".")) {
        //val table = command.toLowerCase.replace("describe table ", "").replace(";", "").trim
        val table = command.substring(15).replace(";", "").trim
        val tokens: Array[String] = table.split("\\.")
        result = describeTable(new TableName(tokens(0), tokens(1)))
      } else if (!getCurrentCatalog.isEmpty) {
        //val table = command.toLowerCase.replace("describe table ", "").replace(";", "").trim
        val table = command.substring(15).replace(";", "").trim
        result = describeTable(new TableName(getCurrentCatalog, table))
      } else {
        result = Result.createErrorResult(new Exception("Catalog not specified"))
      }
    } else {
      result = Result.createErrorResult(new Exception("Unknown command"))
    }
    result
  }

  /**
   * List the existing catalogs in the underlying database.
   * @return A MetadataResult with a list of catalogs, or the object with hasError set
   *         containing the error message.
   */
  def listCatalogs(): Result = {
    val queryId = UUID.randomUUID().toString
    val result = basicDriver.retryPolitics.askRetry(basicDriver.proxyActor, new Command(queryId, APICommand.LIST_CATALOGS, null,sessionId),
      30 seconds,
      retry = 2)
    if(result.isInstanceOf[MetadataResult]){
      result.asInstanceOf[MetadataResult]
    } else {
      result.asInstanceOf[ErrorResult]
    }
  }

  /**
   * List the existing tables in a database catalog.
   * @return A MetadataResult with a list of tables, or the object with hasError set
   *         containing the error message.
   */
  def listTables(catalogName: String): MetadataResult = {
    val params: java.util.List[AnyRef] = new java.util.ArrayList[AnyRef]
    params.add(catalogName)
    val queryId = UUID.randomUUID().toString
    val result = basicDriver.retryPolitics.askRetry(basicDriver.proxyActor, new Command(queryId, APICommand.LIST_TABLES, params,sessionId),
      30 second, retry = 2)
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
    val params: java.util.List[AnyRef] = new java.util.ArrayList[AnyRef]
    params.add(catalogName)
    params.add(tableName)
    val queryId = UUID.randomUUID().toString
    val result = basicDriver.retryPolitics.askRetry(basicDriver.proxyActor, new Command(queryId, APICommand.LIST_COLUMNS, params, sessionId),
      30 second)
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
    val params: java.util.List[AnyRef] = new java.util.ArrayList[AnyRef]
    params.add(manifest)
    val queryId = UUID.randomUUID().toString
    val result = basicDriver.retryPolitics.askRetry(basicDriver.proxyActor, new Command(queryId, APICommand.ADD_MANIFEST, params, sessionId),
      30 second)
    if(result.isInstanceOf[CommandResult]){
      result.asInstanceOf[CommandResult]
    } else {
      result.asInstanceOf[ErrorResult]
    }
  }

  @throws(classOf[ManifestException])
  @throws(classOf[ExistNameException])
  @throws(classOf[ApiException])
  def addManifest(manifestType: Int, path: String): Result = {

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
  @throws(classOf[NotExistNameException])
  @throws(classOf[ApiException])
  def dropManifest(manifestType: Int, manifestName: String): Result = {
    val params: java.util.List[AnyRef] = new java.util.ArrayList[AnyRef]
    params.add(manifestType.toString);
    params.add(manifestName)
    val queryId = UUID.randomUUID().toString
    val result = basicDriver.retryPolitics.askRetry(basicDriver.proxyActor, new Command(queryId, APICommand.DROP_MANIFEST, params, sessionId),
      30 second)
    result.asInstanceOf[Result]
  }

  /**
   * Reset metadata in server.
   * @return A CommandResult with a string.
   */
  def resetServerdata(): Result = {
    val queryId = UUID.randomUUID().toString
    val result = basicDriver.retryPolitics.askRetry(basicDriver.proxyActor, new Command(queryId, APICommand.RESET_SERVERDATA, null, sessionId),
      30 second)
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
    val queryId = UUID.randomUUID().toString
    val result = basicDriver.retryPolitics.askRetry(basicDriver.proxyActor, new Command(queryId, APICommand.CLEAN_METADATA, null, sessionId),
      30 second)
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
  def describeConnectors():Result = {
    val queryId = UUID.randomUUID().toString
    val result = basicDriver.retryPolitics.askRetry(basicDriver.proxyActor, new Command(queryId, APICommand.DESCRIBE_CONNECTORS, null,sessionId),
      30 second)
    if(result.isInstanceOf[CommandResult]){
      result.asInstanceOf[CommandResult]
    } else {
      result.asInstanceOf[ErrorResult]
    }
  }

  /**
   * Describe a connector.
   * @return A CommandResult with the description of the connector.
   */
  def describeConnector(connectorName: ConnectorName): Result = {
    val queryId = UUID.randomUUID().toString
    val params: java.util.List[AnyRef] = new java.util.ArrayList[AnyRef]
    params.add(connectorName)
    val result = basicDriver.retryPolitics.askRetry(basicDriver.proxyActor, new Command(queryId, APICommand.DESCRIBE_CONNECTOR, params, sessionId),
      30 second)
    if(result.isInstanceOf[CommandResult]){
      result.asInstanceOf[CommandResult]
    } else {
      result.asInstanceOf[ErrorResult]
    }
  }

  def describeCatalog(catalogName: CatalogName): Result = {
    val queryId = UUID.randomUUID().toString
    val params: java.util.List[AnyRef] = new java.util.ArrayList[AnyRef]
    params.add(catalogName)
    val result = basicDriver.retryPolitics.askRetry(basicDriver.proxyActor, new Command(queryId, APICommand.DESCRIBE_CATALOG, params,sessionId),
      30 second)
    if(result.isInstanceOf[MetadataResult]){
      result.asInstanceOf[MetadataResult]
    } else {
      result.asInstanceOf[ErrorResult]
    }
  }

  /**
   * Describe a datastore.
   * @return A CommandResult with the description of the datastore.
   */
  def describeDatastore(datastoreName: DataStoreName): Result = {
    val queryId = UUID.randomUUID().toString
    val params: java.util.List[AnyRef] = new java.util.ArrayList[AnyRef]
    params.add(datastoreName)
    val result = basicDriver.retryPolitics.askRetry(basicDriver.proxyActor, new Command(queryId, APICommand.DESCRIBE_DATASTORE, params,sessionId),
      30 second)
    if(result.isInstanceOf[CommandResult]){
      result.asInstanceOf[CommandResult]
    } else {
      result.asInstanceOf[ErrorResult]
    }
  }

  def describeTables(catalogName: CatalogName): Result = {
    val queryId = UUID.randomUUID().toString
    val params: java.util.List[AnyRef] = new java.util.ArrayList[AnyRef]
    params.add(catalogName)
    val result = basicDriver.retryPolitics.askRetry(basicDriver.proxyActor, new Command(queryId, APICommand.DESCRIBE_TABLES, params, sessionId),
      30 second)
    if(result.isInstanceOf[CommandResult]){
      result.asInstanceOf[CommandResult]
    } else {
      result.asInstanceOf[ErrorResult]
    }
  }

  def describeTable(tableName: TableName): Result = {
    val queryId = UUID.randomUUID().toString
    val params: java.util.List[AnyRef] = new java.util.ArrayList[AnyRef]
    params.add(tableName)
    val result = basicDriver.retryPolitics.askRetry(basicDriver.proxyActor, new Command(queryId, APICommand.DESCRIBE_TABLE, params, sessionId),
      30 second)
    if(result.isInstanceOf[MetadataResult]){
      result.asInstanceOf[MetadataResult]
    } else {
      result.asInstanceOf[ErrorResult]
    }
  }

  def describeCluster(clusterName: ClusterName): Result = {
    val queryId = UUID.randomUUID().toString
    val params: java.util.List[AnyRef] = new java.util.ArrayList[AnyRef]
    params.add(clusterName)
    val result = basicDriver.retryPolitics.askRetry(basicDriver.proxyActor, new Command(queryId, APICommand.DESCRIBE_CLUSTER, params, sessionId),
      30 second)
    if(result.isInstanceOf[CommandResult]){
      result.asInstanceOf[CommandResult]
    } else {
      result.asInstanceOf[ErrorResult]
    }
  }

  def describeDatastores(): Result = {
    val queryId = UUID.randomUUID().toString
    val result = basicDriver.retryPolitics.askRetry(basicDriver.proxyActor, new Command(queryId, APICommand.DESCRIBE_DATASTORES, null, sessionId),
      30 second)
    if(result.isInstanceOf[CommandResult]){
      result.asInstanceOf[CommandResult]
    } else {
      result.asInstanceOf[ErrorResult]
    }
  }

  def describeClusters(): Result = {
    val queryId = UUID.randomUUID().toString
    val result = basicDriver.retryPolitics.askRetry(basicDriver.proxyActor, new Command(queryId, APICommand.DESCRIBE_CLUSTERS, null, sessionId),
      30 second)
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
  def describeSystem():Result = {
    val queryId = UUID.randomUUID().toString
    val result = basicDriver.retryPolitics.askRetry(basicDriver.proxyActor, new Command(queryId, APICommand.DESCRIBE_SYSTEM, null,sessionId), 30 second)
    if(result.isInstanceOf[CommandResult]){
      result.asInstanceOf[CommandResult]
    } else {
      result.asInstanceOf[ErrorResult]
    }
  }

  /**
   * List of queries of the driver.
   * @return A CommandResult with the query list.
   */
  def listQueries():Result = {

    val sb : StringBuilder = new StringBuilder()
    queriesWebUI.foreach(kv => sb.append(System.getProperty("line.separator"))
      .append(kv._2.getCompleteExecutionInfo())
      .append("Sent to Connector: ")
      .append(if (connectorOfQueries.get(kv._1)==null) "none" else connectorOfQueries.get(kv._1))
      .append(System.getProperty("line.separator"))
    )

    val result = CommandResult.createCommandResult(sb.toString)
    result
  }

  /**
   * Return the explained execution workflow for a given query.
   * @param query The user query.
   * @return A Result.
   */
  def explainPlan(query: String): Result = {
    val params: java.util.List[AnyRef] = new java.util.ArrayList[AnyRef]
    params.add(query)
    params.add(currentCatalog)
    val queryId = UUID.randomUUID().toString
    val result = basicDriver.retryPolitics.askRetry(basicDriver.proxyActor, new Command(queryId, APICommand.EXPLAIN_PLAN, params, sessionId),
      30 second)
    result
  }

  /**
   * Get the IDriverResultHandler associated with a query identifier.
   * @param queryId Query identifier.
   * @return The result handler.
   */
  def getResultHandler(queryId: String): IDriverResultHandler = {
    val queryData = queries.get(queryId)
    if(queryData != null){
      queryData.resultHandler
    } else {
      logger.warn("Query " + queryId + " not found.")
      return null
    }
  }

  /**
   * Remove a result handler from the internal map of callbacks.
   * @param queryId The query identifier associated with the callback.
   * @return Whether the callback has been removed.
   */
  def removeResultHandler(queryId: String): Boolean = {
    Option(queries.get(queryId)).fold{
      logger.warn("Trying to remove the query " + queryId + " but it doesn't exist")
      false
    }{ queryData =>
      logger.info("Query " + queryId + " finished. " + queryData.getExecutionInfo())
      queries.remove(queryId) != null
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

  def getQueryIDWebUI : java.util.Map[String, QueryData] = queriesWebUI
  def getQueryConnectors:java.util.Map[String, String] = connectorOfQueries
}
