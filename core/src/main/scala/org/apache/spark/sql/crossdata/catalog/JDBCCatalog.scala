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
package org.apache.spark.sql.crossdata.catalog

import java.sql.{Connection, DriverManager, ResultSet}

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.Logging
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.catalyst.{CatalystConf, SimpleCatalystConf, TableIdentifier}
import org.apache.spark.sql.crossdata.{XDCatalog, XDContext}
import org.apache.spark.sql.types._

import scala.annotation.tailrec


object JDBCCatalog {
  // SQLConfig
  val Driver = "crossdata.catalog.jdbc.driver"
  val Url = "crossdata.catalog.jdbc.url"
  val Database = "crossdata.catalog.jdbc.db.name"
  val Table = "crossdata.catalog.jdbc.db.table"
  val User = "crossdata.catalog.jdbc.db.user"
  val Pass = "crossdata.catalog.jdbc.db.pass"
  // CatalogFields
  val DatabaseField = "db"
  val TableNameField = "tableName"
  val SchemaField = "tableSchema"
  val DatasourceField = "datasource"
  val PartitionColumnField = "partitionColumn"
  val OptionsField = "options"
  val CrossdataVersionField = "crossdataVersion"
}

/**
 * Default implementation of the [[org.apache.spark.sql.crossdata.XDCatalog]] with persistence using
 * Jdbc.
 * @param conf An implementation of the [[CatalystConf]].
 */
class JDBCCatalog(override val conf: CatalystConf = new SimpleCatalystConf(true), xdContext: XDContext)
  extends XDCatalog(conf, xdContext) with Logging {

  import JDBCCatalog._
  import XDCatalog._
  import org.apache.spark.sql.crossdata._

  private val config: Config = ConfigFactory.load
  private val db = config.getString(Database)
  private val table = config.getString(Table)

  lazy val connection: Connection = {

    val driver = config.getString(Driver)
    val user = config.getString(User)
    val pass = config.getString(Pass)
    val url = config.getString(Url)

    Class.forName(driver)
    val jdbcConnection = DriverManager.getConnection(url, user, pass)

    // CREATE PERSISTENT METADATA TABLE

    jdbcConnection.createStatement().executeUpdate(s"CREATE SCHEMA IF NOT EXISTS $db")


    jdbcConnection.createStatement().executeUpdate(
        s"""|CREATE TABLE IF NOT EXISTS $db.$table (
           |$DatabaseField VARCHAR(50),
           |$TableNameField VARCHAR(50),
           |$SchemaField TEXT,
           |$DatasourceField TEXT,
           |$PartitionColumnField TEXT,
           |$OptionsField TEXT,
           |$CrossdataVersionField TEXT,
           |PRIMARY KEY ($DatabaseField,$TableNameField))""".stripMargin)

    jdbcConnection
  }


  override def lookupTable(tableName: String, databaseName: Option[String]): Option[CrossdataTable] = {

    val preparedStatement = connection.prepareStatement(s"SELECT * FROM $db.$table WHERE $DatabaseField= ? AND $TableNameField= ?")
    preparedStatement.setString(1, databaseName.getOrElse(""))
    preparedStatement.setString(2, tableName)
    val resultSet = preparedStatement.executeQuery()

    if (!resultSet.isBeforeFirst) {
      None
    } else {
      resultSet.next()
      val database = resultSet.getString(DatabaseField)
      val table = resultSet.getString(TableNameField)
      val schemaJSON = resultSet.getString(SchemaField)
      val partitionColumn = resultSet.getString(PartitionColumnField)
      val datasource = resultSet.getString(DatasourceField)
      val optsJSON = resultSet.getString(OptionsField)
      val version = resultSet.getString(CrossdataVersionField)

      Some(
        CrossdataTable(table, Some(database), getUserSpecifiedSchema(schemaJSON), datasource, getPartitionColumn(partitionColumn), getOptions(optsJSON), version)
      )
    }
  }

  override def listPersistedTables(databaseName: Option[String]): Seq[(String, Boolean)] = {
    @tailrec
    def getSequenceAux(resultset: ResultSet, next: Boolean, set: Set[String] = Set()): Set[String] = {
      if (next) {
        val database = resultset.getString(DatabaseField)
        val table = resultset.getString(TableNameField)
        val tableId = if (database.trim.isEmpty) table else s"$database.$table"
        getSequenceAux(resultset, resultset.next(), set + tableId)
      } else {
        set
      }
    }

    val statement = connection.createStatement
    val dbFilter = databaseName.fold("")(dbName => s"WHERE $DatabaseField ='$dbName'")
    val resultSet = statement.executeQuery(s"SELECT $DatabaseField, $TableNameField FROM $db.$table $dbFilter")

    getSequenceAux(resultSet, resultSet.next).map(tableId => (tableId, true)).toSeq
  }

  override def persistTableMetadata(crossdataTable: CrossdataTable, logicalRelation: Option[LogicalPlan]): Unit = {

    val tableSchema = serializeSchema(crossdataTable.userSpecifiedSchema.getOrElse(new StructType()))

    val tableOptions = serializeOptions(crossdataTable.opts)
    val partitionColumn = serializePartitionColumn(crossdataTable.partitionColumn)

    connection.setAutoCommit(false)

    // check if the database-table exist in the persisted catalog
    val preparedStatement = connection.prepareStatement(s"SELECT * FROM $db.$table WHERE $DatabaseField= ? AND $TableNameField= ?")
    preparedStatement.setString(1, crossdataTable.dbName.getOrElse(""))
    preparedStatement.setString(2, crossdataTable.tableName)
    val resultSet = preparedStatement.executeQuery()

    if (!resultSet.isBeforeFirst) {
      val prepped = connection.prepareStatement(
       s"""|INSERT INTO $db.$table (
           | $DatabaseField, $TableNameField, $SchemaField, $DatasourceField, $PartitionColumnField, $OptionsField, $CrossdataVersionField
           |) VALUES (?,?,?,?,?,?,?)
       """.stripMargin)
      prepped.setString(1, crossdataTable.dbName.getOrElse(""))
      prepped.setString(2, crossdataTable.tableName)
      prepped.setString(3, tableSchema)
      prepped.setString(4, crossdataTable.datasource)
      prepped.setString(5, partitionColumn)
      prepped.setString(6, tableOptions)
      prepped.setString(7, CrossdataVersion)
      prepped.execute()
    }
    else {
     val prepped = connection.prepareStatement(
      s"""|UPDATE $db.$table SET $SchemaField=?, $DatasourceField=?,$PartitionColumnField=?,$OptionsField=?,$CrossdataVersionField=?
          |WHERE $DatabaseField='${crossdataTable.dbName.getOrElse("")}' AND $TableNameField='${crossdataTable.tableName}';
       """.stripMargin.replaceAll("\n", " "))

      prepped.setString(1, tableSchema)
      prepped.setString(2, crossdataTable.datasource)
      prepped.setString(3, partitionColumn)
      prepped.setString(4, tableOptions)
      prepped.setString(5, CrossdataVersion)
      prepped.execute()
    }
    connection.commit()
    connection.setAutoCommit(true)

    val tableIdentifier = TableIdentifier(crossdataTable.tableName,crossdataTable.dbName).toSeq
    //Try to register the table.
    registerTable(tableIdentifier, logicalRelation.getOrElse(lookupRelation(tableIdentifier))
    )
  }

  override def dropPersistedTable(tableName: String, databaseName: Option[String]): Unit = {
    connection.createStatement.executeUpdate(s"DELETE FROM $db.$table WHERE tableName='$tableName' AND db='${databaseName.getOrElse("")}'")
  }

  override def dropAllPersistedTables(): Unit = {
    connection.createStatement.executeUpdate(s"TRUNCATE $db.$table")
  }

}