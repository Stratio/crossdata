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

import akka.actor.{ActorSystem, Address}
import akka.cluster.ClusterEvent.CurrentClusterState
import akka.cluster.MemberStatus
import com.stratio.crossdata.common._
import com.stratio.crossdata.common.result._
import com.stratio.crossdata.common.security.Session
import com.stratio.crossdata.driver.config.DriverConf
import com.stratio.crossdata.driver.metadata.FieldMetadata
import com.stratio.crossdata.driver.session.Authentication
import org.apache.spark.sql.Row
import org.apache.spark.sql.crossdata.metadata.DataTypesUtils
import org.apache.spark.sql.types.{ArrayType, DataType, StructType}
import org.slf4j.Logger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.postfixOps
import scala.util.Try

/*
 * ======================================== NOTE ========================================
 * Take into account that every time the interface of this class is modified or expanded,
 * the JavaDriver.scala has to be updated according to those changes.
 * =======================================================================================
 */

// TODO It should be moved to a new package (version 2.0)
object Driver {

  /**
    * Tuple (tableName, Optional(databaseName))
    */
  type TableIdentifier = (String, Option[String])

  private[driver] def generateDefaultAuth = Authentication("crossdata", Some("stratio"))

  private[driver] lazy val defaultDriverConf = new DriverConf

  private[driver] val InitializationTimeout: Duration = 10 seconds

  /**
    * TODO 2.0 improve implementation (avoiding explicit shutdown as a consequence)
    * currently, it is the how the actor system is shared amongs drivers (tcp and http)
    */
  private[driver] lazy val system = ActorSystem("CrossdataServerCluster", defaultDriverConf.finalSettings)

  /**
    * Stops the underlying actor system.
    * WARNING! It should be called once all active sessions have been closed. After the shutdown, new session cannot be created.
    */
  def shutdown(): Unit = {
    if (!system.isTerminated) system.shutdown()
  }

  Runtime.getRuntime.addShutdownHook(new Thread(new Runnable {
    def run() {
      shutdown()
    }
  }))


  // Factory for TCP drivers
  def newSession(): Driver = newSession(defaultDriverConf)

  def newSession(driverConf: DriverConf): Driver =
    newSession(driverConf, Driver.generateDefaultAuth)

  def newSession(user: String, password: String): Driver =
    newSession(user, password, defaultDriverConf)

  def newSession(user: String, password: String, driverConf: DriverConf): Driver =
    newSession(driverConf, Authentication(user, Option(password)))

  def newSession(seedNodes: java.util.List[String]): Driver =
    newSession(seedNodes, defaultDriverConf)

  def newSession(seedNodes: java.util.List[String], driverConf: DriverConf): Driver =
    newSession(driverConf.setClusterContactPoint(seedNodes))

    system.whenTerminated onFailure {
      case _ => system.terminate
    }
  private[crossdata] def newSession(driverConf: DriverConf, authentication: Authentication): Driver = {
    val driver = new ClusterClientDriver(driverConf, authentication)
    val isConnected = driver.openSession().getOrElse {
      throw new RuntimeException(s"Cannot establish connection to XDServer: timed out after $InitializationTimeout")
    }
    if (!isConnected) {
      throw new RuntimeException(s"The server has rejected the open session request")
    }
    driver
  }

  // TODO Factory for HTTP driver (as the tcp factory above)
  def newHTTPSession(): Driver = newHTTPSession(defaultDriverConf, Driver.generateDefaultAuth)

  private[crossdata] def newHTTPSession(driverConf: DriverConf, authentication: Authentication): Driver = ???

}


abstract class Driver(protected[crossdata] val driverConf: DriverConf, protected[crossdata] val auth: Authentication) {

  import Driver._

  protected def logger: Logger

  protected def driverSession: Session


  // TODO 2.0 remove openSession
  protected[driver] def openSession(): Try[Boolean]

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
    *
    * @param query The SQL Command.
    * @return A SQLResponse with the id and the result set.
    */
  def sql(query: String): SQLResponse

  /**
    * Add Jar to the XD Context
    *
    * @param path The path of the JAR
    * @return A SQLResponse with the id and the result set.
    */
  def addJar(path: String, toClassPath: Option[Boolean] = None): SQLResponse

  def addAppCommand(path: String, clss: String, alias: Option[String] = None): SQLResponse

  def importTables(dataSourceProvider: String, options: Map[String, String]): SQLResponse =
    sql(
      s"""|IMPORT TABLES
          |USING $dataSourceProvider
          |${mkOptionsStatement(options)}
       """.stripMargin
    )


  // TODO schema -> StructType insteadOf String
  // schema -> e.g "( name STRING, age INT )"
  def createTable(name: String, dataSourceProvider: String, schema: Option[String], options: Map[String, String], isTemporary: Boolean = false): SQLResponse =
  sql(
    s"""|CREATE ${if (isTemporary) "TEMPORARY" else ""} TABLE $name
        |USING $dataSourceProvider
        |${schema.getOrElse("")}
        |${mkOptionsStatement(options)}
       """.stripMargin
  )

  def dropTable(name: String, isTemporary: Boolean = false): SQLResponse = {

    if (isTemporary) throw new UnsupportedOperationException("Drop temporary table is not supported yet")

    sql(
      s"""|DROP ${if (isTemporary) "TEMPORARY" else ""}
          |TABLE $name
       """.stripMargin
    )
  }

  def dropAllTables(): SQLResponse = {
    sql(
      s"""|DROP ALL TABLES""".stripMargin
    )
  }

  private def mkOptionsStatement(options: Map[String, String]): String = {
    val opt = options.map { case (k, v) => s"$k '$v'" } mkString ","
    options.headOption.fold("")(_ => s" OPTIONS ( $opt ) ")
  }

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
    * @param database  Database of the table.
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
          if (!driverConf.getFlattenTables) {
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
    * Gets the server/cluster state
    *
    * @since 1.3
    * @return Current snapshot state of the cluster
    */
  def clusterState(): Future[CurrentClusterState]

  /**
    * Gets the addresses of servers up and running
    *
    * @since 1.3
    * @return A sequence with members of the cluster ready to serve requests
    */
  def serversUp(): Future[Seq[Address]] = {
    import collection.JavaConverters._
    clusterState().map { cState =>
      cState.getMembers.asScala.collect {
        case member if member.status == MemberStatus.Up => member.address
      }.toSeq
    }
  }

  /**
    * Indicates if the cluster is alive or not
    *
    * @since 1.3
    * @return whether at least one member of the cluster is alive or not
    */
  def isClusterAlive(atMost: Duration = 3 seconds): Boolean =
  Try(Await.result(serversUp(), atMost)).map(_.nonEmpty).getOrElse(false)


  /**
    * Execute an ordered shutdown
    */
  def closeSession(): Unit

  protected def securitizeCommand(command: Command): CommandEnvelope =
    CommandEnvelope(command, driverSession, auth.user)


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
      logger.error(message, cause)
      throw new RuntimeException(message, cause)
    case ErrorSQLResult(message, _) =>
      logger.error(message)
      throw new RuntimeException(message)
    // TODO manage exceptions
  }

}