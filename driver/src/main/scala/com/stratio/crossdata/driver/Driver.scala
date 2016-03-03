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
package com.stratio.crossdata.driver

import java.util
import java.util.concurrent.atomic.AtomicReference

import akka.actor.{ActorRef, ActorSystem}
import akka.contrib.pattern.ClusterClient
import akka.pattern.ask
import akka.util.Timeout
import com.stratio.crossdata.common.result.{ErrorSQLResult, SQLResponse, SQLResult, SuccessfulSQLResult}
import com.stratio.crossdata.common.{CancelCommand, SQLAnswer, SQLCommand, SecureCommand, ServerAnswer}
import com.stratio.crossdata.driver.actor.ProxyActor
import com.stratio.crossdata.driver.config.DriverConfig
import com.stratio.crossdata.driver.config.DriverConfig.{DriverConfigHosts, DriverRetryDuration, DriverRetryTimes}
import com.stratio.crossdata.driver.metadata.FieldMetadata
import com.stratio.crossdata.driver.session.{Authentication, SessionManager}
import com.typesafe.config.{ConfigValue, ConfigValueFactory}
import org.apache.log4j.Logger
import org.apache.spark.sql.Row
import org.apache.spark.sql.crossdata.metadata.DataTypesUtils
import org.apache.spark.sql.types.{ArrayType, DataType, StructType}

import scala.collection.JavaConversions._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.reflect.ClassTag

/*
 * ======================================== NOTE ========================================
 * Take into account that every time the interface of this class is modified or expanded,
 * the JavaDriver.scala has to be updated according to those changes.
 * =======================================================================================
 */
class Driver private(properties: java.util.Map[String, ConfigValue] = Map.empty[String, ConfigValue],
             auth: Authentication = Driver.generateDefaultAuth,
             flattenTables: Boolean = false) {

  import Driver._

  /**
   * Tuple (tableName, Optional(databaseName))
   */
  type TableIdentifier = (String, Option[String])

  private lazy val driverSession = SessionManager.createSession(auth, proxyActor)

  private lazy val logger = Driver.logger

  private val clientConfig =
    properties.foldLeft(Driver.config) { case (previousConfig, keyValue@(path, configValue)) =>
    previousConfig.withValue(path, configValue)
  }

  private val system = ActorSystem("CrossdataServerCluster", clientConfig)

  private val proxyActor = {

    if (logger.isDebugEnabled) {
      system.logConfiguration()
    }

    val contactPoints = {
      val hosts = clientConfig.getStringList("config.cluster.hosts").toList
      val clusterName = Driver.config.getString("config.cluster.name")
      hosts map (host => s"akka.tcp://$clusterName@$host$ActorsPath")
    }
    val initialContacts = contactPoints.map(system.actorSelection).toSet
    logger.debug("Initial contacts: " + initialContacts)

    val clusterClientActor = system.actorOf(ClusterClient.props(initialContacts), ProxyActor.RemoteClientName)
    logger.debug("Cluster client actor created with name: " + ProxyActor.RemoteClientName)

    system.actorOf(ProxyActor.props(clusterClientActor, this), ProxyActor.DefaultName)
  }


  /**
   * Executes a SQL sentence.
   * In order to work in an asynchronous way:
   * > driver.sql("SELECT * FROM t").sqlResult onComplete { callback }
   * In order to work in an synchronous way:
   * > val sqlResult: SQLResult = driver.sql("SELECT * FROM t").waitForResult(5 seconds)
   * Also, you can use implicits
   * > import SQLResponse._
   * > val sqlResult: SQLResult = driver.sql("SELECT * FROM t")
   * > val rows: Array[Row] = driver.sql("SELECT * FROM t").resultSet
   * @param query The SQL Command.
   * @return A SQLResponse with the id and the result set.
   */
  def sql(query: String): SQLResponse = {
    val sqlCommand = new SQLCommand(query, retrieveColNames = flattenTables)
    val secureCommand = SecureCommand(sqlCommand, driverSession)
    new SQLResponse(sqlCommand.requestId, askCommand[SQLAnswer](proxyActor, secureCommand).map(_.sqlResult)) {
      // TODO cancel sync => 5 secs
      override def cancelCommand() = askCommand(proxyActor, CancelCommand(sqlCommand.requestId))
    }
  }

  // TODO remove infinite duration
  // TODO remove askPattern
  private def askCommand[T <: ServerAnswer : ClassTag](remoteActor: ActorRef, message: Any, timeout: FiniteDuration = 1 day): Future[T] = {
    remoteActor.ask(message)(timeout).mapTo[T]
  }


  /* TODO create command in XD Parser => syncQuery(SQLCommand("SHOW DATABASES"))
  def listDatabases(): Seq[String] = ??? */


  /**
   * Gets a list of tables from a database or all if the database is None
   *
   * @param databaseName The database name
   * @return A sequence of tables an its database
   */
  def listTables(databaseName: Option[String] = None): Seq[TableIdentifier] = {
    def processTableName(qualifiedName: String): (String, Option[String]) = {
      qualifiedName.split('.') match {
        case table if table.length == 1 => (table(0), None)
        case table if table.length == 2 => (table(1), Some(table(0)))
      }
    }
    import SQLResponse._
    val sqlResult: SQLResult = sql(s"SHOW TABLES ${databaseName.fold("")("IN " + _)}")
    sqlResult match {
      case SuccessfulSQLResult(result, _) =>
        result.map(row => processTableName(row.getString(0)))
      case other => handleCommandError(other)
    }
  }

  /**
   * Gets the metadata from a specific table.
   *
   * @param database Database of the table.
   * @param tableName The name of the table.
   * @return A sequence with the metadata of the fields of the table.
   */
  def describeTable(database: Option[String], tableName: String): Seq[FieldMetadata] = {

    def extractNameDataType: Row => (String, String) = row => (row.getString(0), row.getString(1))

    import SQLResponse._
    val sqlResult: SQLResult = sql(s"DESCRIBE ${database.map(_ + ".").getOrElse("")}$tableName")

    sqlResult match {
      case SuccessfulSQLResult(result, _) =>
        result.map(extractNameDataType) flatMap { case (name, dataType) =>
          if (!flattenTables) {
            FieldMetadata(name, DataTypesUtils.toDataType(dataType)) :: Nil
          } else {
            getFlattenedFields(name, DataTypesUtils.toDataType(dataType))
          }
        } toSeq

      case other =>
        handleCommandError(other)
    }
  }

  def show(query: String) = sql(query).waitForResult().prettyResult.foreach(println)

  /**
   * Execute an ordered shutdown
   */
  def stop() = Driver.clearActiveContext()

  @deprecated("Close will be removed from public API. Use stop instead")
  def close() = stop()


  private def securitizeCommand(command: SQLCommand): SecureCommand =
    new SecureCommand(command, driverSession)


  private def getFlattenedFields(fieldName: String, dataType: DataType): Seq[FieldMetadata] = dataType match {
    case structType: StructType =>
      structType.flatMap(field => getFlattenedFields(s"$fieldName.${field.name}", field.dataType))
    case ArrayType(etype, _) =>
      getFlattenedFields(fieldName, etype)
    case _ =>
      FieldMetadata(fieldName, dataType) :: Nil
  }

  private def handleCommandError(result: SQLResult) = result match {
    case ErrorSQLResult(message, Some(cause)) =>
      logger.error(message,cause)
      throw new RuntimeException(message, cause)
    case ErrorSQLResult(message, _) =>
      logger.error(message)
      throw new RuntimeException(message)
    // TODO manage exceptions
  }

}

object Driver extends DriverConfig {

  // TODO slf4j
  override lazy val logger = Logger.getLogger(getClass)

  private val DRIVER_CONSTRUCTOR_LOCK = new Object()

  private val activeDriver: AtomicReference[Driver] =
    new AtomicReference[Driver](null)

  val ActorsPath = "/user/receptionist"

  private[driver] def setActiveDriver(driver: Driver) = {
    DRIVER_CONSTRUCTOR_LOCK.synchronized {
      activeDriver.set(driver)
    }
  }

  def clearActiveContext() = {
    DRIVER_CONSTRUCTOR_LOCK.synchronized {
      val system = activeDriver.get().system
      if(!system.isTerminated) system.shutdown()
      activeDriver.set(null)
    }
  }

  def getOrCreate(properties: java.util.Map[String, ConfigValue], flattenTables: Boolean): Driver = {
    DRIVER_CONSTRUCTOR_LOCK.synchronized {
      if(activeDriver.get() == null) {
        setActiveDriver(new Driver(properties, Driver.generateDefaultAuth, flattenTables))
      }
      activeDriver.get()
    }
  }

  def getOrCreate(properties: java.util.Map[String, ConfigValue], auth: Authentication, flattenTables: Boolean): Driver = {
    DRIVER_CONSTRUCTOR_LOCK.synchronized {
      if(activeDriver.get() == null) {
        setActiveDriver(new Driver(properties, auth, flattenTables))
      }
      activeDriver.get()
    }
  }

  def getOrCreate(seedNodes: java.util.List[String], flattenTables: Boolean): Driver = {
    DRIVER_CONSTRUCTOR_LOCK.synchronized {
      if(activeDriver.get() == null) {
        setActiveDriver(
          getOrCreate(
            Map(DriverConfigHosts -> ConfigValueFactory.fromAnyRef(seedNodes)),
            flattenTables))
      }
      activeDriver.get()
    }
  }

  def getOrCreate(seedNodes: java.util.List[String], auth: Authentication, flattenTables: Boolean): Driver = {
    DRIVER_CONSTRUCTOR_LOCK.synchronized {
      if(activeDriver.get() == null) {
        setActiveDriver(
          getOrCreate(
            Map(DriverConfigHosts -> ConfigValueFactory.fromAnyRef(seedNodes)),
            auth,
            flattenTables))
      }
      activeDriver.get()
    }
  }

  def getOrCreate(flattenTables: Boolean): Driver = {
    DRIVER_CONSTRUCTOR_LOCK.synchronized {
      if(activeDriver.get() == null) {
        setActiveDriver(
          getOrCreate(
            new util.HashMap[String, ConfigValue](),
            flattenTables))
      }
      activeDriver.get()
    }
  }

  def getOrCreate(auth: Authentication, flattenTables: Boolean): Driver = {
    DRIVER_CONSTRUCTOR_LOCK.synchronized {
      if(activeDriver.get() == null) {
        setActiveDriver(
          getOrCreate(
            new util.HashMap[String, ConfigValue](),
            auth,
            flattenTables))
      }
      activeDriver.get()
    }
  }

  def getOrCreate(seedNodes: java.util.List[String]): Driver = {
    DRIVER_CONSTRUCTOR_LOCK.synchronized {
      if(activeDriver.get() == null) {
        setActiveDriver(
          getOrCreate(
            Map(DriverConfigHosts -> ConfigValueFactory.fromAnyRef(seedNodes)),
            false))
      }
      activeDriver.get()
    }
  }

  def getOrCreate(seedNodes: java.util.List[String], auth: Authentication): Driver = {
    DRIVER_CONSTRUCTOR_LOCK.synchronized {
      if(activeDriver.get() == null) {
        setActiveDriver(
          getOrCreate(
            Map(DriverConfigHosts -> ConfigValueFactory.fromAnyRef(seedNodes)),
            auth,
            false))
      }
      activeDriver.get()
    }
  }

  def getOrCreate(properties: java.util.Map[String, ConfigValue]): Driver = {
    DRIVER_CONSTRUCTOR_LOCK.synchronized {
      if(activeDriver.get() == null) {
        setActiveDriver(
          getOrCreate(
            properties,
            false))
      }
      activeDriver.get()
    }
  }

  def getOrCreate(properties: java.util.Map[String, ConfigValue], auth: Authentication): Driver = {
    DRIVER_CONSTRUCTOR_LOCK.synchronized {
      if(activeDriver.get() == null) {
        setActiveDriver(
          getOrCreate(
            properties,
            auth,
            false))
      }
      activeDriver.get()
    }
  }

  def getOrCreate(auth: Authentication): Driver = {
    DRIVER_CONSTRUCTOR_LOCK.synchronized {
      if(activeDriver.get() == null) {
        setActiveDriver(
          getOrCreate(
            new util.HashMap[String, ConfigValue](),
            auth,
            false))
      }
      activeDriver.get()
    }
  }

  def getOrCreate(): Driver = {
    DRIVER_CONSTRUCTOR_LOCK.synchronized {
      if(activeDriver.get() == null) {
        setActiveDriver(
          getOrCreate(
            new util.HashMap[String, ConfigValue](),
            false))
      }
      activeDriver.get()
    }
  }

  lazy val defaultTimeout = Timeout(config .getDuration(DriverRetryDuration, MILLISECONDS), MILLISECONDS)
  lazy val defaultRetries = config.getInt(DriverRetryTimes)

  def generateDefaultAuth = new Authentication("crossdata", "stratio")

}