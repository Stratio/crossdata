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

import org.apache.spark.Logging
import org.apache.spark.sql.catalyst.plans.logical.{LogicalPlan, Subquery}
import org.apache.spark.sql.catalyst.{CatalystConf, SimpleCatalystConf}

import org.apache.spark.sql.sources._
import org.apache.spark.sql.types.StructType
import com.typesafe.config.Config


import scala.util.parsing.json.{JSON, JSONObject}

object MySQLCatalog {

  val DRIVER = "crossdata.catalog.mysql.driver"
  val IP="crossdata.catalog.mysql.ip"
  val PORT="crossdata.catalog.mysql.port"
  val DB="crossdata.catalog.mysql.db"
  val TABLE="crossdata.catalog.mysql.db.persistTable"
  val USER="crossdata.catalog.mysql.db.user"
  val PASS="crossdata.catalog.mysql.db.pass"

  val StringSeparator: String = "."
  val CROSSDATA_VERSION = "crossdata.version"

  case class CrossdataTable(tableName: String, database: Option[String] = None,  userSpecifiedSchema: Option[StructType], provider: String, crossdataVersion: String, opts: Map[String, String] = Map.empty[String, String])

}

/**
 * Default implementation of the [[org.apache.spark.sql.crossdata.XDCatalog]] with persistence using
 * MySQL.
 * @param conf An implementation of the [[CatalystConf]].
 */
class MySQLCatalog(override val conf: CatalystConf = new SimpleCatalystConf(true), xDContext: XDContext)
  extends XDCatalog(conf) with Logging  {
  import MySQLCatalog._

  def config: Config = ???

  lazy val ip= config.getString(IP)
  lazy val port= config.getString(PORT)
  lazy val driver= config.getString(DRIVER)
  lazy val db= config.getString(DB)
  lazy val table= config.getString(TABLE)
  lazy val user= config.getString(USER)
  lazy val pass= config.getString(PASS)
  lazy val crossdataVersion= config.getString(CROSSDATA_VERSION)

  lazy val url=s"jdbc:mysql://$ip:$port"

  lazy val connection: Connection = {
      Class.forName(driver)
      DriverManager.getConnection(url, user, pass)
  }

  //CREATE PERSISTENT METADATA TABLE
  connection.createStatement().executeUpdate(s"CREATE DATABASE IF NOT EXISTS $db")
  connection.createStatement().executeUpdate(s"""CREATE TABLE IF NOT EXISTS $db.$table (database VARCHAR(50),
                                              | tableName VARCHAR(50),
                                              | sch TEXT,
                                              | provider TEXT,
                                              | options TEXT,
                                              | crossdataVersion TEXT,
                                              | PRIMARY KEY (database,tablename))""".stripMargin)

  /**
   * Persist in XD Catalog
   */
  override def persistTable(tableIdentifier: Seq[String], crossdataTable: CrossdataTable):
  Unit = {

    //super.registerTable(tableName, plan)
    logInfo("XDCatalog: Persist Table")

    val tableSchema = crossdataTable.userSpecifiedSchema.get.json
    val tableOptions = JSONObject(crossdataTable.opts).toString()
    val statement = connection.createStatement
    statement.executeQuery(
      s"""INSERT INTO $db.$table (database, tableName, sch, provider, options) VALUES(
         | ${crossdataTable.database},
         | ${crossdataTable.tableName},
         | ${tableSchema},
         | ${crossdataTable.provider},
         | $tableOptions
         | $crossdataVersion
       """.stripMargin)

   //Try to register the table.
   lookupRelation(tableIdentifier)
  }


  /**
   * Drop all tables of catalog
   */
  override def dropAllTables(): Unit = {
    logInfo("XDCatalog: Drop all tables from catalog")
    val statement = connection.createStatement
    statement.executeUpdate(s"""DROP TABLE $db.$table""")
    super.unregisterAllTables()
  }

  /**
   * Drop table from XD catalog
   */
  override def dropTable(tableIdentifier: Seq[String]): Unit = {
    logInfo("XDCatalog: Delete Table from catalog")
    val tableName: String = tableIdentifier(1)
    val statement = connection.createStatement
    statement.executeUpdate(s"""DELETE FROM $db.$table WHERE tableName='$tableName'""")
    super.unregisterTable(tableIdentifier)
  }


  override def tableExists(tableIdentifier: Seq[String]): Boolean = {
    val existsInCache = super.tableExists(tableIdentifier)
    if (existsInCache){
      true
    } else{
      lookUpTable(tableIdentifier) match {
        case Some(crossdataTable) =>
          val logicalPlan: LogicalPlan = createLogicalRelation(crossdataTable)
          val tableWithQualifiers = Subquery(tableIdentifier.last, logicalPlan)
          super.registerTable(tableIdentifier,logicalPlan)
          true
        case None =>
          false
      }
    }
  }

  /**
   * Search if exists a relation registered previously of table identifier
   * @param tableIdentifier
   * @param alias
   * @return
   */
  override def lookupRelation(tableIdentifier: Seq[String], alias: Option[String]): LogicalPlan = {

    lookupRelationCache(tableIdentifier, alias).getOrElse{
      lookUpTable(tableIdentifier) match {
        case Some(crossdataTable) =>
          //TODO provider => new instance => createRelation( table, options....); registerTempTable (tableIdentifier, crossdataTable)

          val logicalPlan: LogicalPlan = createLogicalRelation(crossdataTable)
          super.registerTable(tableIdentifier,logicalPlan)
          val tableWithQualifiers = Subquery(tableIdentifier.last, logicalPlan)
          // If an alias was specified by the lookup, wrap the plan in a subquery so that attributes are
          // properly qualified with this alias.
          alias.map(a => Subquery(a, tableWithQualifiers)).getOrElse(tableWithQualifiers)

        case None =>
          val tableFullName = tableIdentifier(0)
          sys.error(s"Table Not Found: $tableFullName")
      }

    }
  }

  /**
   * Search in catalog if table identifier exists
   * @param tableIdentifier
   * @return
   */
  private def lookUpTable(tableIdentifier: Seq[String]): Option[CrossdataTable] = {
    val database=tableIdentifier(0)
    val tablename=tableIdentifier(1)
    val statement = connection.createStatement
    val resultSet=statement.executeQuery(
      s"""SELECT * FROM $db.$table WHERE database='$database' AND tableName='$tablename')""".stripMargin)

    if (!resultSet.isBeforeFirst() ) {
      None
    }else{
      resultSet.next()
      val database = resultSet.getString("database")
      val table = resultSet.getString("tableName")
      val schemaJSON = resultSet.getString("sch")
      val provider= resultSet.getString("provider")
      val optsJSON = resultSet.getString("options")

      Some(CrossdataTable(table,Some(database),getUserSpecifiedSchema(schemaJSON),provider,crossdataVersion,
        getOptions(optsJSON)))
    }
  }

  private def createLogicalRelation(crossdataTable: CrossdataTable):LogicalRelation = {
    val resolved = ResolvedDataSource.lookupDataSource(crossdataTable.provider).newInstance()
    crossdataTable.userSpecifiedSchema match {
      case schema Some(StructType) =>  LogicalRelation(resolved.asInstanceOf[SchemaRelationProvider].createRelation
        (xDContext,crossdataTable.opts,schema))
      case None => LogicalRelation(resolved.asInstanceOf[RelationProvider].createRelation(xDContext,crossdataTable.opts))
    }
  }

  private def getUserSpecifiedSchema(schemaJSON: String): Option[StructType] =
    Some(JSON.parseFull(schemaJSON).get.asInstanceOf[StructType])

  private def getOptions(optsJSON: String): Map[String,String] =
    JSON.parseFull(optsJSON).get.asInstanceOf[Map[String,String]]

}
