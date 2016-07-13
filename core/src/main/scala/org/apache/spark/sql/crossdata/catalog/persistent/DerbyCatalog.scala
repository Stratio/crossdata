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
  val TableWithAppJars = "appJars"

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

  //IndexMetadataFields //TODO: To core-config
  val IndexNameField = "indexName"
  val IndexTypeField = "indexType"
  val IndexedColsField = "indexedCols"
  val PKField = "pk"

  //App values
  val JarPath = "jarPath"
  val AppAlias = "alias"
  val AppClass = "class"

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

      jdbcConnection.createStatement().executeUpdate(
        s"""|CREATE TABLE $DB.$TableWithAppJars (
            |$JarPath VARCHAR(100),
            |$AppAlias VARCHAR(50),
            |$AppClass VARCHAR(100),
            |PRIMARY KEY ($AppAlias))""".stripMargin)
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
            |$PKField VARCHAR(100),
            |$DatasourceField LONG VARCHAR,
            |$OptionsField LONG VARCHAR,
            |$CrossdataVersionField VARCHAR(30),
            |UNIQUE ($IndexNameField, $IndexTypeField),
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

  override def getApp(alias: String): Option[CrossdataApp] = {

    val preparedStatement = connection.prepareStatement(s"SELECT * FROM $DB.$TableWithAppJars WHERE $AppAlias= ?")
    preparedStatement.setString(1, alias)
    val resultSet: ResultSet = preparedStatement.executeQuery()

    if (!resultSet.next) {
      None
    } else {
      val jar = resultSet.getString(JarPath)
      val alias = resultSet.getString(AppAlias)
      val clss = resultSet.getString(AppClass)

      Some(
        CrossdataApp(jar, alias, clss)
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

  override def lookupIndex(indexIdentifier: IndexIdentifier): Option[CrossdataIndex] = {
    val resultSet = selectIndex(indexIdentifier)

    if (!resultSet.next) {
      None
    } else {

      val database = resultSet.getString(DatabaseField)
      val table = resultSet.getString(TableNameField)
      val indexName = resultSet.getString(IndexNameField)
      val indexType = resultSet.getString(IndexTypeField)
      val indexedCols = resultSet.getString(IndexedColsField)
      val pk = resultSet.getString(PKField)
      val datasource = resultSet.getString(DatasourceField)
      val optsJSON = resultSet.getString(OptionsField)
      val version = resultSet.getString(CrossdataVersionField)

      Some(
        CrossdataIndex(TableIdentifier(table, Some(database)), IndexIdentifier(indexType, indexName),
          deserializeSeq(indexedCols), pk, datasource, deserializeOptions(optsJSON), version)
      )
    }
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


  override def persistIndexMetadata(crossdataIndex: CrossdataIndex): Unit =
    try {
      connection.setAutoCommit(false)
      // check if the database-table exist in the persisted catalog
      val resultSet = selectMetadata(TableWithIndexMetadata, crossdataIndex.tableIdentifier)

      val serializedIndexedCols = serializeSeq(crossdataIndex.indexedCols)
      val serializedOptions = serializeOptions(crossdataIndex.opts)

      if (!resultSet.next()) {
        val prepped = connection.prepareStatement(
          s"""|INSERT INTO $DB.$TableWithIndexMetadata (
              | $DatabaseField, $TableNameField, $IndexNameField, $IndexTypeField, $IndexedColsField,
              | $PKField, $DatasourceField, $OptionsField, $CrossdataVersionField
              |) VALUES (?,?,?,?,?,?,?,?,?)
       """.stripMargin)
        prepped.setString(1, crossdataIndex.tableIdentifier.database.getOrElse(""))
        prepped.setString(2, crossdataIndex.tableIdentifier.table)
        prepped.setString(3, crossdataIndex.indexIdentifier.indexName)
        prepped.setString(4, crossdataIndex.indexIdentifier.indexType)
        prepped.setString(5, serializedIndexedCols)
        prepped.setString(6, crossdataIndex.pk)
        prepped.setString(7, crossdataIndex.datasource)
        prepped.setString(8, serializedOptions)
        prepped.setString(9, CrossdataVersion)
        prepped.execute()
      } else {
        //TODO: Support change index metadata?
        sys.error(s"A global index already exists in table ${crossdataIndex.tableIdentifier.unquotedString}")
      }
    } finally {
      connection.setAutoCommit(true)
    }


  override def saveAppMetadata(crossdataApp: CrossdataApp): Unit =
    try {
      connection.setAutoCommit(false)

      val preparedStatement = connection.prepareStatement(s"SELECT * FROM $DB.$TableWithAppJars WHERE $AppAlias= ?")
      preparedStatement.setString(1, crossdataApp.appAlias)
      val resultSet = preparedStatement.executeQuery()

      if (!resultSet.next()) {
        val prepped = connection.prepareStatement(
          s"""|INSERT INTO $DB.$TableWithAppJars (
              | $JarPath, $AppAlias, $AppClass
              |) VALUES (?,?,?)
         """.stripMargin)
        prepped.setString(1, crossdataApp.jar)
        prepped.setString(2, crossdataApp.appAlias)
        prepped.setString(3, crossdataApp.appClass)
        prepped.execute()
      } else {
        val prepped = connection.prepareStatement(
          s"""|UPDATE $DB.$TableWithAppJars SET $JarPath=?, $AppClass=?
              |WHERE $AppAlias='${crossdataApp.appAlias}'
         """.stripMargin)
        prepped.setString(1, crossdataApp.jar)
        prepped.setString(2, crossdataApp.appClass)
        prepped.execute()
      }
      connection.commit()
    } finally {
      connection.setAutoCommit(true)
    }

  override def dropTableMetadata(tableIdentifier: TableIdentifier): Unit =
    executeSQLCommand(
      s"DELETE FROM $DB.$TableWithTableMetadata WHERE tableName='${tableIdentifier.table}' AND db='${tableIdentifier.database.getOrElse("")}'"
    )

  override def dropViewMetadata(viewIdentifier: ViewIdentifier): Unit =
    executeSQLCommand(
      s"DELETE FROM $DB.$TableWithViewMetadata WHERE tableName='${viewIdentifier.table}' AND db='${viewIdentifier.database.getOrElse("")}'"
    )

  override def dropIndexMetadata(indexIdentifier: IndexIdentifier): Unit =
    executeSQLCommand(
      s"DELETE FROM $DB.$TableWithIndexMetadata WHERE $IndexTypeField='${indexIdentifier.indexType}' AND $IndexNameField='${indexIdentifier.indexName}'"
    )

  override def dropIndexMetadata(tableIdentifier: TableIdentifier): Unit =
    executeSQLCommand(
      s"DELETE FROM $DB.$TableWithIndexMetadata WHERE $TableNameField='${tableIdentifier.table}' AND $DatabaseField='${tableIdentifier.database.getOrElse("")}'"
    )


  override def dropAllTablesMetadata(): Unit =
    executeSQLCommand(s"DELETE FROM $DB.$TableWithTableMetadata")

  override def dropAllViewsMetadata(): Unit =
    executeSQLCommand(s"DELETE FROM $DB.$TableWithViewMetadata")

  override def dropAllIndexesMetadata(): Unit =
    executeSQLCommand(s"DELETE FROM $DB.$TableWithIndexMetadata")

  def executeSQLCommand(sql: String): Unit = {
    val statement = connection.createStatement
    try{
      statement.executeUpdate(sql)
    } finally {
      statement.close()
    }
  }


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

  //TODO: Generic close statements in ALL the catalogs
  private def selectIndex(indexIdentifier: IndexIdentifier): ResultSet = {
    val preparedStatement = connection.prepareStatement(s"SELECT * FROM $DB.$TableWithIndexMetadata WHERE $IndexNameField= ? AND $IndexTypeField= ?")
    preparedStatement.setString(1, indexIdentifier.indexName)
    preparedStatement.setString(2, indexIdentifier.indexType)
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
          |WHERE sch.SCHEMANAME='$schema' AND tb.TABLENAME='${table.toUpperCase}'""".stripMargin

    val preparedStatement = connection.prepareStatement(query)
    val resultSet = preparedStatement.executeQuery()

    resultSet.next()
  }

  override def lookupIndexByTableIdentifier(tableIdentifier: TableIdentifier):Option[CrossdataIndex] = {
    val query=
      s"SELECT * FROM $DB.$TableWithIndexMetadata WHERE $TableNameField='${tableIdentifier.table}' AND $DatabaseField='${tableIdentifier.database.getOrElse("")}'"
    val preparedStatement = connection.prepareStatement(query)
    val resultSet = preparedStatement.executeQuery()
    if (!resultSet.next) {
      None
    } else {

      val database = resultSet.getString(DatabaseField)
      val table = resultSet.getString(TableNameField)
      val indexName = resultSet.getString(IndexNameField)
      val indexType = resultSet.getString(IndexTypeField)
      val indexedCols = resultSet.getString(IndexedColsField)
      val pk = resultSet.getString(PKField)
      val datasource = resultSet.getString(DatasourceField)
      val optsJSON = resultSet.getString(OptionsField)
      val version = resultSet.getString(CrossdataVersionField)

      Option(
        CrossdataIndex(TableIdentifier(table, Option(database)), IndexIdentifier(indexType, indexName),
          deserializeSeq(indexedCols), pk, datasource, deserializeOptions(optsJSON), version)
      )
    }
  }
}