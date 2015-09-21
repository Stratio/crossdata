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

package org.apache.spark.sql.crossdata

import java.sql.{Connection, DriverManager}
import java.util

import com.mongodb.casbah.commons.MongoDBObject
import org.apache.spark.Logging
import org.apache.spark.sql.catalyst.analysis.{OverrideCatalog, SimpleCatalog}
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.catalyst.{CatalystConf, SimpleCatalystConf}
import org.apache.spark.sql.sources.CreateTableUsing
import org.json4s.NoTypeHints
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization._
import com.mongodb.casbah._

/**
 * Default implementation of the [[org.apache.spark.sql.crossdata.XDCatalog]] with persistence using
 * [[http://www.mapdb.org/ MapDB web site]].
 * @param conf An implementation of the [[CatalystConf]].
 * @param args Possible extra arguments.
 */
class DefaultCatalogMongo(conf: CatalystConf = new SimpleCatalystConf(true),
                     val args: java.util.List[String] = new util.ArrayList[String]())
  extends SimpleCatalog(conf) with OverrideCatalog with XDCatalog with Logging {

  implicit val formats = Serialization.formats(NoTypeHints)

  val uri = MongoClientURI("mongodb://localhost:27017/")
  val mongoClient =  MongoClient(uri)
  val db = mongoClient("crossdata")

  private val StringSeparator: String = "."


  /**
   * @inheritdoc
   */
  override def open(args: Any*): Unit = {
    logInfo("XDCatalog: open")


  }

  /**
   * @inheritdoc
   */
  override def unregisterAllTables(): Unit = {
    super.unregisterAllTables()
    logInfo("XDCatalog: unregisterAllTables")
    val coll = db("crossdata")
    coll.drop()
  }

  /**
   * @inheritdoc
   */
  override def unregisterTable(tableIdentifier: Seq[String]): Unit = {
    super.unregisterTable(tableIdentifier)
    logInfo("XDCatalog: unregisterTable")
    val tableName: String = tableIdentifier.mkString(StringSeparator)
    val coll = db("crossdata")
    val tableDocument = coll.find(tableName)
    coll.remove(tableDocument)

  }

  /**
   * @inheritdoc
   */
  override def registerTable(tableIdentifier: Seq[String], plan: LogicalPlan): Unit = {
    super.registerTable(tableIdentifier, plan)
    logInfo("XDCatalog: registerTable")

    plan match {
      case createTable: CreateTableUsing => {
        val tableSchema = write(createTable.schema)
        val tableName = createTable.tableName
        val tableOptions = write(createTable.options)
        val coll = db("crossdata")
        coll.insert("tableName"->tableName,"columns"->tableSchema,"options"->tableOptions)
      }
      case _ => logError("Register Table error: Only Create Table Using command allowed")
    }

  }

  /**
   * @inheritdoc
   */
  override def refreshTable(databaseName: String, tableName: String): Unit = {
    super.refreshTable(databaseName, tableName)
    logInfo("XDCatalog: refreshTable")
  }

  /**
   * @inheritdoc
   */
  override def close(): Unit = {
    logInfo("XDCatalog: close")
    mongoClient.close
  }
}
