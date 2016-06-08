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
package org.apache.spark.sql.crossdata.catalog.persistent

import java.sql.{Connection, DriverManager, ResultSet}

import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.catalyst.{CatalystConf, TableIdentifier}
import org.apache.spark.sql.crossdata.CrossdataVersion
import org.apache.spark.sql.crossdata.catalog.{XDCatalog, persistent}

import scala.annotation.tailrec

// TODO refactor SQL catalog implementations
object DerbyCatalog {
  val DB = "CROSSDATA"
  val TableWithTableMetadata = "xdtables"
  val TableWithViewMetadata = "xdviews"
  val TableWithIndexMetadata = "xdindexes"
  // TableMetadataFields
  val DatabaseField = "db"
  val TableNameField = "tableName"
  val SchemaField = "tableSchema"
  val DatasourceField = "datasource"
  val PartitionColumnField = "partitionColumn"
  val OptionsField = "options"
  val CrossdataVersionField = "crossdataVersion"
  // ViewMetadataFields (databaseField, tableNameField, sqlViewField, CrossdataVersionField
  val SqlViewField = "sqlView"
  //IndexMetadataFields
  val IndexNameField = "indexName"
  val IndexTypeField = "indexType"
  val IndexedColsField = "indexedCols"
  val PKColsField = "pkCols"
}

/**
 * Default implementation of the [[persistent.PersistentCatalogWithCache]] with persistence using
 * Derby.
 *
 * @param catalystConf An implementation of the [[CatalystConf]].
 */
class DerbyCatalog(sqlContext: SQLContext, override val catalystConf: CatalystConf)
  extends PersistentCatalogWithCache(sqlContext, catalystConf) {

  import DerbyCatalog._
  import XDCatalog._

  @transient lazy val connection: Connection = {

    val driver = "org.apache.derby.jdbc.EmbeddedDriver"
    val url = "jdbc:derby:sampledb/crossdata;create=true"

    Class.forName(driver)
    val jdbcConnection = DriverManager.getConnection(url)

    // CREATE PERSISTENT METADATA TABLE

    if(!schemaExists(DB, jdbcConnection)) {
      jdbcConnection.createStatement().executeUpdate(s"CREATE SCHEMA $DB")


      jdbcConnection.createStatement().executeUpdate(
        s"""|CREATE TABLE $DB.$TableWithTableMetadata (
           |$DatabaseField VARCHAR(50),
           |$TableNameField VARCHAR(50),
           |$SchemaField LONG VARCHAR,
           |$DatasourceField LONG VARCHAR,
           |$PartitionColumnField LONG VARCHAR,
           |$OptionsField LONG VARCHAR,
           |$CrossdataVersionField LONG VARCHAR,
           |PRIMARY KEY ($DatabaseField,$TableNameField))""".stripMargin)

      jdbcConnection.createStatement().executeUpdate(
        s"""|CREATE TABLE $DB.$TableWithViewMetadata (
            |$DatabaseField VARCHAR(50),
            |$TableNameField VARCHAR(50),
            |$SqlViewField LONG VARCHAR,
            |$CrossdataVersionField VARCHAR(30),
            |PRIMARY KEY ($DatabaseField,$TableNameField))""".stripMargin)

    }

    //Index support
    if(!indexTableExists(DB, jdbcConnection)) {
      jdbcConnection.createStatement().executeUpdate( //TODO: Relational way using other table for the columns??
        s"""|CREATE TABLE $DB.$TableWithIndexMetadata (
            |$DatabaseField VARCHAR(50),
            |$TableNameField VARCHAR(50),
            |$IndexNameField VARCHAR(50),
            |$IndexTypeField VARCHAR(50),
            |$IndexedColsField LONG VARCHAR,
            |$PKColsField LONG VARCHAR,
            |$DatasourceField LONG VARCHAR,
            |$OptionsField LONG VARCHAR,
            |$CrossdataVersionField VARCHAR(30),
            |PRIMARY KEY ($DatabaseField,$TableNameField))""".stripMargin) //TODO: Multiple indexing??
    }

    jdbcConnection
  }


  override def lookupTable(tableIdentifier: ViewIdentifier): Option[CrossdataTable] = {
    val resultSet = selectMetadata(TableWithTableMetadata, tableIdentifier)

    if (!resultSet.next) {
      None
    } else {

      val database = resultSet.getString(DatabaseField)
      val table = resultSet.getString(TableNameField)
      val schemaJSON = resultSet.getString(SchemaField)
      val partitionColumn = resultSet.getString(PartitionColumnField)
      val datasource = resultSet.getString(DatasourceField)
      val optsJSON = resultSet.getString(OptionsField)
      val version = resultSet.getString(CrossdataVersionField)

      Some(
        CrossdataTable(table, Some(database), Option(deserializeUserSpecifiedSchema(schemaJSON)), datasource,
          deserializePartitionColumn(partitionColumn), deserializeOptions(optsJSON), version)
      )
    }
  }

  override def lookupView(viewIdentifier: ViewIdentifier): Option[String] = {
    val resultSet = selectMetadata(TableWithViewMetadata, viewIdentifier)
    if (!resultSet.next)
      None
    else
      Option(resultSet.getString(SqlViewField))
  }



  override def persistTableMetadata(crossdataTable: CrossdataTable): Unit =
    try {

      val tableSchema = serializeSchema(crossdataTable.schema.getOrElse(schemaNotFound()))
      val tableOptions = serializeOptions(crossdataTable.opts)
      val partitionColumn = serializePartitionColumn(crossdataTable.partitionColumn)

      connection.setAutoCommit(false)

      // check if the database-table exist in the persisted catalog
      val resultSet = selectMetadata(TableWithTableMetadata, TableIdentifier(crossdataTable.tableName, crossdataTable.dbName))

      if (!resultSet.next()) {
        val prepped = connection.prepareStatement(
          s"""|INSERT INTO $DB.$TableWithTableMetadata (
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
          s"""|UPDATE $DB.$TableWithTableMetadata SET $SchemaField=?, $DatasourceField=?,$PartitionColumnField=?,$OptionsField=?,$CrossdataVersionField=?
              |WHERE $DatabaseField='${crossdataTable.dbName.getOrElse("")}' AND $TableNameField='${crossdataTable.tableName}'
       """.stripMargin)
        prepped.setString(1, tableSchema)
        prepped.setString(2, crossdataTable.datasource)
        prepped.setString(3, partitionColumn)
        prepped.setString(4, tableOptions)
        prepped.setString(5, CrossdataVersion)
        prepped.execute()
      }
      connection.commit()

    } finally {
      connection.setAutoCommit(true)
    }

  override def persistViewMetadata(tableIdentifier: TableIdentifier, sqlText: String): Unit =
    try {
      connection.setAutoCommit(false)
      val resultSet = selectMetadata(TableWithViewMetadata, tableIdentifier)

      if (!resultSet.next()) {
        val prepped = connection.prepareStatement(
          s"""|INSERT INTO $DB.$TableWithViewMetadata (
              | $DatabaseField, $TableNameField, $SqlViewField, $CrossdataVersionField
              |) VALUES (?,?,?,?)
         """.stripMargin)
        prepped.setString(1, tableIdentifier.database.getOrElse(""))
        prepped.setString(2, tableIdentifier.table)
        prepped.setString(3, sqlText)
        prepped.setString(4, CrossdataVersion)
        prepped.execute()
      } else {
        val prepped = connection.prepareStatement(
          s"""|UPDATE $DB.$TableWithViewMetadata SET $SqlViewField=?
              |WHERE $DatabaseField='${tableIdentifier.database.getOrElse("")}' AND $TableNameField='${tableIdentifier.table}'
         """.stripMargin)
        prepped.setString(1, sqlText)
        prepped.execute()
      }
      connection.commit()
    } finally {
      connection.setAutoCommit(true)
    }

  override def dropTableMetadata(tableIdentifier: TableIdentifier): Unit =
    connection.createStatement.executeUpdate(
      s"DELETE FROM $DB.$TableWithTableMetadata WHERE tableName='${tableIdentifier.table}' AND db='${tableIdentifier.database.getOrElse("")}'"
    )

  override def dropViewMetadata(viewIdentifier: ViewIdentifier): Unit =
    connection.createStatement.executeUpdate(
      s"DELETE FROM $DB.$TableWithViewMetadata WHERE tableName='${viewIdentifier.table}' AND db='${viewIdentifier.database.getOrElse("")}'"
    )


  override def dropAllTablesMetadata(): Unit =
    connection.createStatement.executeUpdate(s"DELETE FROM $DB.$TableWithTableMetadata")

  override def dropAllViewsMetadata(): Unit =
    connection.createStatement.executeUpdate(s"DELETE FROM $DB.$TableWithViewMetadata")

  override def isAvailable: Boolean = true

  override def allRelations(databaseName: Option[String]): Seq[TableIdentifier] = {
    @tailrec
    def getSequenceAux(resultset: ResultSet, next: Boolean, set: Set[TableIdentifier] = Set.empty): Set[TableIdentifier] = {
      if (next) {
        val database = resultset.getString(DatabaseField)
        val table = resultset.getString(TableNameField)
        val tableId = if (database.trim.isEmpty) TableIdentifier(table) else TableIdentifier(table, Option(database))
        getSequenceAux(resultset, resultset.next(), set + tableId)
      } else {
        set
      }
    }

    val statement = connection.createStatement
    val dbFilter = databaseName.fold("")(dbName => s"WHERE $DatabaseField ='$dbName'")
    val resultSet = statement.executeQuery(s"SELECT $DatabaseField, $TableNameField FROM $DB.$TableWithTableMetadata $dbFilter")

    getSequenceAux(resultSet, resultSet.next).toSeq
  }

  private def selectMetadata(targetTable: String, tableIdentifier: TableIdentifier): ResultSet = {

    val preparedStatement = connection.prepareStatement(s"SELECT * FROM $DB.$targetTable WHERE $DatabaseField= ? AND $TableNameField= ?")
    preparedStatement.setString(1, tableIdentifier.database.getOrElse(""))
    preparedStatement.setString(2, tableIdentifier.table)
    preparedStatement.executeQuery()

  }
  private def schemaExists(schema: String, connection: Connection): Boolean = {
    val preparedStatement = connection.prepareStatement(s"SELECT * FROM SYS.SYSSCHEMAS WHERE schemaname='$schema'")
    val resultSet = preparedStatement.executeQuery()

    resultSet.next()
  }

  private def indexTableExists(schema: String, connection: Connection): Boolean = tableSchemaExists(schema, TableWithIndexMetadata, connection)

  private def tableSchemaExists(schema: String, table: String, connection: Connection): Boolean =  {
    val query =
      s"""|SELECT * FROM SYS.SYSSCHEMAS sch
          |LEFT JOIN SYS.SYSTABLES tb ON tb.schemaid = sch.schemaid
          |WHERE sch.SCHEMANAME='$schema' AND tb.TABLENAME='$table'""".stripMargin

    val preparedStatement = connection.prepareStatement(query)
    val resultSet = preparedStatement.executeQuery()

    resultSet.next()
  }
}