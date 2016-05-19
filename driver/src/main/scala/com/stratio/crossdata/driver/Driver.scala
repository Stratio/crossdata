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

import java.util.concurrent.atomic.AtomicReference

import akka.actor.{ActorSystem, Address}
import akka.cluster.ClusterEvent.CurrentClusterState
import akka.cluster.MemberStatus
import akka.contrib.pattern.ClusterClient
import com.stratio.crossdata.common._
import com.stratio.crossdata.common.result._
import com.stratio.crossdata.driver.actor.ProxyActor
import com.stratio.crossdata.driver.config.DriverConf
import com.stratio.crossdata.driver.metadata.FieldMetadata
import com.stratio.crossdata.driver.session.{Authentication, SessionManager}
import org.apache.spark.sql.Row
import org.apache.spark.sql.crossdata.metadata.DataTypesUtils
import org.apache.spark.sql.types.{ArrayType, DataType, StructType}
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future, Promise}
import scala.language.postfixOps
import scala.util.Try

/*
 * ======================================== NOTE ========================================
 * Take into account that every time the interface of this class is modified or expanded,
 * the JavaDriver.scala has to be updated according to those changes.
 * =======================================================================================
 */


object Driver {

  private val DRIVER_CONSTRUCTOR_LOCK = new Object()

  private val activeDriver: AtomicReference[Driver] =
    new AtomicReference[Driver](null)


  private[driver] def setActiveDriver(driver: Driver) = {
    DRIVER_CONSTRUCTOR_LOCK.synchronized {
      activeDriver.set(driver)
    }
  }

  def clearActiveContext = {
    DRIVER_CONSTRUCTOR_LOCK.synchronized {
      val system = activeDriver.get().system
      if (!system.isTerminated) system.shutdown()
      activeDriver.set(null)
    }
  }


  def getOrCreate(): Driver = getOrCreate(new DriverConf)

  def getOrCreate(driverConf: DriverConf): Driver =
    getOrCreate(driverConf, Driver.generateDefaultAuth)

  def getOrCreate(user: String, password: String): Driver =
    getOrCreate(user, password, new DriverConf)

  def getOrCreate(user: String, password: String, driverConf: DriverConf): Driver =
    getOrCreate(driverConf, Authentication(user, password))

  def getOrCreate(seedNodes: java.util.List[String]): Driver =
    getOrCreate(seedNodes, new DriverConf)

  def getOrCreate(seedNodes: java.util.List[String], driverConf: DriverConf): Driver =
    getOrCreate(driverConf.setClusterContactPoint(seedNodes))

  /**
    * Used by JavaDriver
    */
  private[crossdata] def getOrCreate(driverConf: DriverConf, authentication: Authentication): Driver =
    DRIVER_CONSTRUCTOR_LOCK.synchronized {
      if (activeDriver.get() == null) {
        setActiveDriver(new Driver(driverConf, authentication))
      }
      activeDriver.get()
    }

  private[driver] def generateDefaultAuth = new Authentication("crossdata", "stratio")

}

class Driver private(private[crossdata] val driverConf: DriverConf,
                     auth: Authentication = Driver.generateDefaultAuth) {


  /**
   * Tuple (tableName, Optional(databaseName))
   */
  type TableIdentifier = (String, Option[String])

  private lazy val driverSession = SessionManager.createSession(auth, proxyActor)

  private lazy val logger = LoggerFactory.getLogger(classOf[Driver])

  private val system = ActorSystem("CrossdataServerCluster", driverConf.finalSettings)

  private val proxyActor = {

    if (logger.isDebugEnabled) {
      system.logConfiguration()
    }

    val contactPoints = driverConf.getClusterContactPoint

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
    *
    * @param query The SQL Command.
   * @return A SQLResponse with the id and the result set.
   */
  def sql(query: String): SQLResponse = {

    //TODO remove this part when servers broadcast bus was realized
    //Preparse query to know if it is an special command sent from the shell or other driver user that is not a query
    val Pattern="""(\s*add)(\s+jar\s+)(.*)""".r
    query match {
      case Pattern(add,jar,path) => addJar(path.trim)
      case _ =>
        val sqlCommand = new SQLCommand(query, retrieveColNames = driverConf.getFlattenTables)
        val futureReply = askCommand(securitizeCommand(sqlCommand)).map{
          case SQLReply(_, sqlResult) => sqlResult
          case other => throw new RuntimeException(s"SQLReply expected. Received: $other")
        }
        new SQLResponse(sqlCommand.requestId, futureReply) {
          // TODO cancel sync => 5 secs
          override def cancelCommand(): Unit =
            askCommand(securitizeCommand(CancelQueryExecution(sqlCommand.queryId)))
        }
    }
  }


  /**
    * Add Jar to the XD Context
    *
    * @param path The path of the JAR
    * @return A SQLResponse with the id and the result set.
    */
  def addJar(path: String): SQLResponse = {
    val addJarCommand = AddJARCommand(path)
    val futureReply = askCommand(securitizeCommand(addJarCommand)).map{
      case SQLReply(_, sqlResult) => sqlResult
      case other => throw new RuntimeException(s"SQLReply expected. Received: $other")
    }
    new SQLResponse(addJarCommand.requestId, futureReply)
  }


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


  private def askCommand(commandEnvelope: CommandEnvelope): Future[ServerReply] = {
    val promise = Promise[ServerReply]()
    proxyActor ! (commandEnvelope, promise)
    promise.future
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
  def clusterState(): Future[CurrentClusterState] = {
    val promise = Promise[ServerReply]()
    proxyActor ! (securitizeCommand(ClusterStateCommand()), promise)
    promise.future.mapTo[ClusterStateReply].map(_.clusterState)
  }

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
  def stop() = Driver.clearActiveContext

  @deprecated("Close will be removed from public API. Use stop instead")
  def close() = stop()


  private def securitizeCommand(command: Command): CommandEnvelope =
    new CommandEnvelope(command, driverSession)


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
