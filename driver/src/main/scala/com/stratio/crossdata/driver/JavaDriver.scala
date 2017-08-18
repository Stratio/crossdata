/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.driver

import akka.actor.Address
import com.stratio.crossdata.common.result.{SQLResult, StreamedSQLResult}
import com.stratio.crossdata.driver.config.DriverConf
import com.stratio.crossdata.driver.metadata.{FieldMetadata, JavaTableName}
import com.stratio.crossdata.driver.session.Authentication
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object JavaDriver {
  def httpDriverFactory: DriverFactory = Driver.http
  def clusterClientDriverFactory: DriverFactory = Driver
}

class JavaDriver private(driverConf: DriverConf,
                         auth: Authentication,
                         driverFactory: DriverFactory) {

  def this(driverConf: DriverConf, driverFactory: DriverFactory) =
    this(driverConf, driverFactory.generateDefaultAuth, driverFactory)

  def this(driverConf: DriverConf) =
    this(driverConf, Driver)

  def this(driverFactory: DriverFactory) = this(new DriverConf, driverFactory)

  def this() = this(Driver)

  def this(user: String, password: String, driverConf: DriverConf, driverFactory: DriverFactory) =
    this(driverConf, Authentication(user, Option(password)), driverFactory)

  def this(user: String, password: String, driverConf: DriverConf) =
    this(user, password, driverConf, Driver)

  def this(user: String, driverConf: DriverConf, driverFactory: DriverFactory) =
    this(driverConf, Authentication(user), driverFactory)

  def this(user: String, driverConf: DriverConf) =
    this(user, driverConf, Driver)

  def this(user: String, password: String, driverFactory: DriverFactory) =
    this(user, password, new DriverConf, driverFactory)

  def this(user: String, password: String) =
    this(user, password, Driver)

  // TODO Create a JavaDriver factory
  /**
    * Cluster client tcp constructor
    */
  def this(tcpSeedNodes: java.util.List[String], driverConf: DriverConf) =
    this(driverConf.setClusterContactPoint(tcpSeedNodes), JavaDriver.clusterClientDriverFactory)

  def this(tcpSeedNodes: java.util.List[String]) =
    this(tcpSeedNodes, new DriverConf)

  /**
    * Http constructors
    */
  def this(httpHost: String, httpPort: Int, driverConf: DriverConf) =
  this(driverConf.setHttpHostAndPort(httpHost, httpPort), JavaDriver.httpDriverFactory)

  def this(httpHost: String, httpPort: Int) =
    this(httpHost, httpPort, new DriverConf)




  private lazy val logger = LoggerFactory.getLogger(classOf[JavaDriver])

  private val scalaDriver = driverFactory.newSession(driverConf, auth)


  /**
   * Sync execution with defaults: timeout 10 sec, nr-retries 2
   */
  def sql(sqlText: String): SQLResult =
    scalaDriver.sql(sqlText).waitForResult()

  def sql(sqlText: String, timeoutDuration: Duration): SQLResult =
    scalaDriver.sql(sqlText).waitForResult(timeoutDuration)

  def sqlStreamSource(query: String): StreamedSQLResult =
    Await.result(scalaDriver.sqlStreamedResult(query), Duration.Inf)

  def importTables(dataSourceProvider: String, options: java.util.Map[String, String]): SQLResult =
    scalaDriver.importTables(dataSourceProvider, options.toMap)

  def createTable(name: String, dataSourceProvider: String, schema: Option[String], options: java.util.Map[String, String], isTemporary: Boolean): SQLResult =
    scalaDriver.createTable(name, dataSourceProvider, schema, options.toMap, isTemporary).waitForResult()

  def dropTable(name: String, isTemporary: Boolean = false): SQLResult =
    scalaDriver.dropTable(name, isTemporary)


  def listTables(): java.util.List[JavaTableName] =
    scalaDriver.listTables(None).map { case (table, database) => new JavaTableName(table, database.getOrElse("")) }


  def listTables(database: String): java.util.List[JavaTableName] =
    scalaDriver.listTables(Some(database)).map { case (table, database) => new JavaTableName(table, database.getOrElse("")) }


  def describeTable(database: String, tableName: String): java.util.List[FieldMetadata] =
    scalaDriver.describeTable(Some(database), tableName)


  def describeTable(tableName: String): java.util.List[FieldMetadata] =
    scalaDriver.describeTable(None, tableName)


  def show(sqlText: String): Unit =
    scalaDriver.show(sqlText)

  /**
   * Indicates if the cluster is alive or not
   *
   * @since 1.3
   * @return whether at least one member of the cluster is alive or not
   */
  def isClusterAlive(): Boolean =
    scalaDriver.isClusterAlive()

  /**
    * Returns a list of the nodes forming the Crossdata cluster
    *
    * @since 1.7
    * @return list of addresses of servers running
    */
  def serversUp(): java.util.Set[Address]  = Await.result(scalaDriver.serversUp(), 10 seconds).asJava

  /**
    * Returns a list of the nodes forming the session provider
    *
    * @since 1.7
    * @return list of host:port of nodes providing the Crossdata sessions
    */
  def membersUp(): java.util.Set[String] = Await.result(scalaDriver.sessionProviderState(), 10 seconds).asJava

  def closeSession(): Unit =
    scalaDriver.closeSession()

  def addJar(path:String): Unit =
    scalaDriver.addJar(path)


}

