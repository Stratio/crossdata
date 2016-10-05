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

import akka.actor.Address
import com.stratio.crossdata.common.result.SQLResult
import com.stratio.crossdata.driver.config.DriverConf
import com.stratio.crossdata.driver.metadata.{FieldMetadata, JavaTableName}
import com.stratio.crossdata.driver.session.Authentication
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import scala.concurrent.Await
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

  def this(seedNodes: java.util.List[String], driverConf: DriverConf, driverFactory: DriverFactory) =
    this(driverConf.setClusterContactPoint(seedNodes), driverFactory)

  def this(seedNodes: java.util.List[String], driverConf: DriverConf) =
    this(seedNodes, driverConf, Driver)

  def this(seedNodes: java.util.List[String], driverFactory: DriverFactory) =
    this(seedNodes, new DriverConf, driverFactory)

  def this(seedNodes: java.util.List[String]) =
    this(seedNodes, Driver)

  private lazy val logger = LoggerFactory.getLogger(classOf[JavaDriver])

  private val scalaDriver = driverFactory.newSession(driverConf, auth)


  /**
   * Sync execution with defaults: timeout 10 sec, nr-retries 2
   */
  def sql(sqlText: String): SQLResult =
    scalaDriver.sql(sqlText).waitForResult()

  def sql(sqlText: String, timeoutDuration: Duration): SQLResult =
    scalaDriver.sql(sqlText).waitForResult(timeoutDuration)


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

