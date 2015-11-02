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

import akka.util.Timeout
import com.stratio.crossdata.common.metadata.FieldMetadata
import com.stratio.crossdata.common.{SQLCommand, SQLResult}
import com.stratio.crossdata.driver.config.DriverConfig._
import com.typesafe.config.{ConfigValue, ConfigValueFactory}
import org.apache.log4j.Logger

import scala.collection.JavaConversions._

object JavaDriver {
  /**
   * database can be empty ("")
   */
  case class TableName(tableName: String, database: String)
}

class JavaDriver(properties: java.util.Map[String, ConfigValue]) {

  def this(serverHosts: java.util.List[String]) =
    this(Map(DriverConfigHosts -> ConfigValueFactory.fromAnyRef(serverHosts)))

  def this() = this(Map.empty[String, ConfigValue])

  import JavaDriver._
  
  private lazy val logger = Logger.getLogger(getClass)

  private val scalaDriver = new Driver(properties)

  /**
   * Sync execution with defaults: timeout 10 sec, nr-retries 2
   */
  def syncQuery(sqlCommand: SQLCommand): SQLResult = {
    scalaDriver.syncQuery(sqlCommand)
  }

  def syncQuery(sqlCommand: SQLCommand, timeout: Timeout, retries: Int): SQLResult = {
    scalaDriver.syncQuery(sqlCommand, timeout, retries)
  }

  def listDatabases(): java.util.List[String] = {
    scalaDriver.listDatabases()
  }

  def listTables(): java.util.List[TableName] = {
    scalaDriver.listTables(None).map{ case (table, database) => TableName(table, database.getOrElse(""))}
  }

  def listTables(database: String): java.util.List[TableName] = {
    scalaDriver.listTables(Some(database)).map{ case (table, database) => TableName(table, database.getOrElse(""))}
  }

  def describeTable(database: String, tableName: String): java.util.List[FieldMetadata] = {
    scalaDriver.describeTable(Some(database), tableName)
  }

  def describeTable(tableName: String): java.util.List[FieldMetadata] = {
    scalaDriver.describeTable(None, tableName)
  }

}

