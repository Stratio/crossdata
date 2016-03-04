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


import com.stratio.crossdata.common.result.SQLResult
import com.stratio.crossdata.driver.config.DriverConf
import com.stratio.crossdata.driver.metadata.{FieldMetadata, JavaTableName}
import com.stratio.crossdata.driver.session.Authentication
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._
import scala.concurrent.duration.Duration


class JavaDriver private (driverConf: DriverConf,
                          auth: Authentication) {

  def this(driverConf: DriverConf) =
    this(driverConf, Driver.generateDefaultAuth)

  def this() = this(new DriverConf)

  def this(user: String, password: String, driverConf: DriverConf) =
    this(driverConf, Authentication(user, password))

  def this(user: String, password: String) =
    this(user, password, new DriverConf)

  def this(seedNodes: java.util.List[String], driverConf: DriverConf) =
    this(driverConf.setClusterContactPoint(seedNodes))

  def this(seedNodes: java.util.List[String]) =
    this(seedNodes, new DriverConf)


  private lazy val logger = LoggerFactory.getLogger(classOf[JavaDriver])

  private val scalaDriver = Driver.getOrCreate(driverConf, auth)

  /**
   * Sync execution with defaults: timeout 10 sec, nr-retries 2
   */
  def sql(sqlText: String): SQLResult =
    scalaDriver.sql(sqlText).waitForResult()

  def sql(sqlText: String, timeoutDuration: Duration): SQLResult =
    scalaDriver.sql(sqlText).waitForResult(timeoutDuration)

  def importTables(dataSourceProvider: String, options: java.util.Map[String, String]): SQLResult =
    scalaDriver.importTables(dataSourceProvider, options.toMap)

  def createTable(name: String, dataSourceProvider: String, schema: Option[String], options: java.util.Map[String, String], isTemporary : Boolean): SQLResult =
    scalaDriver.createTable(name, dataSourceProvider, schema, options.toMap, isTemporary).waitForResult()

  def dropTable(name: String, isTemporary : Boolean = false): SQLResult =
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

  def stop(): Unit = {
    scalaDriver.stop()
  }

  @deprecated("Close will be removed from public API. Use stop instead")
  def close() = stop()

}

