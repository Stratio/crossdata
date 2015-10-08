/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.spark.sql

import org.apache.spark.sql.types.StructType

package object crossdata {
  case class CrossdataTable(tableName: String, db: Option[String] = None,  userSpecifiedSchema: Option[StructType], provider: String, partitionColumn: Array[String], crossdataVersion: String, opts: Map[String, String] = Map.empty[String, String])

  val DRIVER = "crossdata.catalog.mysql.driver"
  val IP="crossdata.catalog.mysql.ip"
  val PORT="crossdata.catalog.mysql.port"
  val DB="crossdata.catalog.mysql.db.name"
  val TABLE="crossdata.catalog.mysql.db.persistTable"
  val USER="crossdata.catalog.mysql.db.user"
  val PASS="crossdata.catalog.mysql.db.pass"

  val StringSeparator: String = "."
  val CROSSDATA_VERSION = "crossdata.version"

}
