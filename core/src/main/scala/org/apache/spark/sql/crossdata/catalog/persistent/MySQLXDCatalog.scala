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
import org.apache.spark.sql.crossdata.{CrossdataVersion, XDContext}
import org.apache.spark.sql.crossdata.catalog.interfaces.XDAppsCatalog
import org.apache.spark.sql.crossdata.catalog.{IndexIdentifierNormalized, StringNormalized, TableIdentifierNormalized, XDCatalog, persistent}

import scala.annotation.tailrec
import scala.util.Try

object MySQLXDCatalog {
  // SQLConfig
  val Driver = "jdbc.driver"
  val Url = "jdbc.url"
  val Database = "jdbc.db.name"
  val User = "jdbc.db.user"
  val Pass = "jdbc.db.pass"
  val PrefixConfig = "prefix"

  //Default tables
  val DefaultTablesMetadataTable = "crossdataTables"
  val DefaultViewsMetadataTable = "crossdataViews"
  val DefaultAppsMetadataTable = "crossdataJars"
  val DefaultIndexesMetadataTable = "crossdataIndexes"

  // CatalogFields
  val DatabaseField = "db"
  val TableNameField = "tableName"
  val SchemaField = "tableSchema"
  val DatasourceField = "datasource"
  val PartitionColumnField = "partitionColumn"
  val OptionsField = "options"
  val CrossdataVersionField = "crossdataVersion"

  //IndexMetadataFields
  val IndexNameField = "indexName"
  val IndexTypeField = "indexType"
  val IndexedColsField = "indexedCols"
  val PKField = "pk"

  // ViewMetadataFields (databaseField, tableNameField, sqlViewField, CrossdataVersionField
  val SqlViewField = "sqlView"

  //App values
  val JarPath = "jarPath"
  val AppAlias = "alias"
  val AppClass = "class"
}

/**
  * Default implementation of the [[persistent.PersistentCatalogWithCache]] with persistence using
  * Jdbc.
  * Supported MySQL and PostgreSQL
  *
  * @param catalystConf An implementation of the [[CatalystConf]].
  */
class MySQLXDCatalog(override val catalystConf: CatalystConf)
  extends PersistentCatalogWithCache(catalystConf) {

  import MySQLXDCatalog._
  import XDCatalog._


  protected lazy val config = XDContext.catalogConfig
  private lazy val db = config.getString(Database)
  protected lazy val tablesPrefix = Try(s"${config.getString(PrefixConfig)}_") getOrElse ("") //prefix_
  protected lazy val tableWithTableMetadata = s"$tablesPrefix$DefaultTablesMetadataTable"
  protected lazy val tableWithViewMetadata = s"$tablesPrefix$DefaultViewsMetadataTable"
  protected lazy val tableWithAppJars = s"$tablesPrefix$DefaultAppsMetadataTable"
  protected lazy val tableWithIndexMetadata = s"$tablesPrefix$DefaultIndexesMetadataTable"

  @transient lazy val connection: Connection = {

    val driver = config.getString(Driver)
    val user = config.getString(User)
    val pass = config.getString(Pass)
    val url = config.getString(Url)

    Class.forName(driver)
    try {
      val jdbcConnection = DriverManager.getConnection(url, user, pass)

      // CREATE PERSISTENT METADATA TABLE

      jdbcConnection.createStatement().executeUpdate(s"CREATE SCHEMA IF NOT EXISTS $db")


      jdbcConnection.createStatement().executeUpdate(
        s"""|CREATE TABLE IF NOT EXISTS $db.$tableWithTableMetadata (
            |$DatabaseField VARCHAR(50),
            |$TableNameField VARCHAR(50),
            |$SchemaField TEXT,
            |$DatasourceField TEXT,
            |$PartitionColumnField TEXT,
            |$OptionsField TEXT,
            |$CrossdataVersionField TEXT,
            |PRIMARY KEY ($DatabaseField,$TableNameField))""".stripMargin)

      jdbcConnection.createStatement().executeUpdate(
        s"""|CREATE TABLE IF NOT EXISTS $db.$tableWithViewMetadata (
            |$DatabaseField VARCHAR(50),
            |$TableNameField VARCHAR(50),
            |$SqlViewField TEXT,
            |$CrossdataVersionField VARCHAR(30),
            |PRIMARY KEY ($DatabaseField,$TableNameField))""".stripMargin)

      jdbcConnection.createStatement().executeUpdate(
        s"""|CREATE TABLE $db.$tableWithAppJars (
            |$JarPath VARCHAR(100),
            |$AppAlias VARCHAR(50),
            |$AppClass VARCHAR(100),
            |PRIMARY KEY ($AppAlias))""".stripMargin)

      //Index support
      jdbcConnection.createStatement().executeUpdate(
        s"""|CREATE TABLE IF NOT EXISTS $db.$tableWithIndexMetadata (
            |$DatabaseField VARCHAR(50),
            |$TableNameField VARCHAR(50),
            |$IndexNameField VARCHAR(50),
            |$IndexTypeField VARCHAR(50),
            |$IndexedColsField TEXT,
            |$PKField VARCHAR(100),
            |$DatasourceField TEXT,
            |$OptionsField TEXT,
            |$CrossdataVersionField VARCHAR(30),
            |UNIQUE ($IndexNameField, $IndexTypeField),
            |PRIMARY KEY ($DatabaseField,$TableNameField))""".stripMargin)

      jdbcConnection
    } catch {
      case e: Exception =>
        logError(e.getMessage)
        null
    }
  }


  override def lookupTable(tableIdentifier: TableIdentifierNormalized): Option[CrossdataTable] = {

    val resultSet = selectMetadata(tableWithTableMetadata, tableIdentifier)

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
        CrossdataTable(TableIdentifierNormalized(table, Some(database)), Option(deserializeUserSpecifiedSchema(schemaJSON)), datasource, deserializePartitionColumn(partitionColumn), deserializeOptions(optsJSON), version)
      )
    }
  }


  override def allRelations(databaseName: Option[StringNormalized]): Seq[TableIdentifierNormalized] = {
    @tailrec
    def getSequenceAux(resultset: ResultSet, next: Boolean, set: Set[TableIdentifierNormalized] = Set.empty): Set[TableIdentifierNormalized] = {
      if (next) {
        val database = resultset.getString(DatabaseField)
        val table = resultset.getString(TableNameField)
        val tableId = if (database.trim.isEmpty) TableIdentifierNormalized(table) else TableIdentifierNormalized(table, Option(database))
        getSequenceAux(resultset, resultset.next(), set + tableId)
      } else {
        set
      }
    }

    val statement = connection.createStatement
    val dbFilter = databaseName.fold("")(dbName => s"WHERE $DatabaseField ='${dbName.normalizedString}'")
    val resultSet = statement.executeQuery(s"SELECT $DatabaseField, $TableNameField FROM $db.$tableWithTableMetadata $dbFilter")

    getSequenceAux(resultSet, resultSet.next).toSeq
  }

  override def persistTableMetadata(crossdataTable: CrossdataTable): Unit =
    try {

      val tableSchema = serializeSchema(crossdataTable.schema.getOrElse(schemaNotFound()))
      val tableOptions = serializeOptions(crossdataTable.opts)
      val partitionColumn = serializePartitionColumn(crossdataTable.partitionColumn)

      connection.setAutoCommit(false)

      // check if the database-table exist in the persisted catalog
      val resultSet = selectMetadata(tableWithTableMetadata, crossdataTable.tableIdentifier)

      if (!resultSet.isBeforeFirst) {
        resultSet.close()
        val prepped = connection.prepareStatement(
          s"""|INSERT INTO $db.$tableWithTableMetadata (
              | $DatabaseField, $TableNameField, $SchemaField, $DatasourceField, $PartitionColumnField, $OptionsField, $CrossdataVersionField
              |) VALUES (?,?,?,?,?,?,?)
       """.stripMargin)
        prepped.setString(1, crossdataTable.tableIdentifier.database.getOrElse(""))
        prepped.setString(2, crossdataTable.tableIdentifier.table)
        prepped.setString(3, tableSchema)
        prepped.setString(4, crossdataTable.datasource)
        prepped.setString(5, partitionColumn)
        prepped.setString(6, tableOptions)
        prepped.setString(7, CrossdataVersion)
        prepped.execute()
        prepped.close()
      } else {
        resultSet.close()
        val prepped =
          connection.prepareStatement(
            s"""|UPDATE $db.$tableWithTableMetadata
                |SET $SchemaField=?, $DatasourceField=?,$PartitionColumnField=?,$OptionsField=?,$CrossdataVersionField=?
                |WHERE $DatabaseField='${crossdataTable.tableIdentifier.database.getOrElse("")}' AND $TableNameField='${crossdataTable.tableIdentifier.table}';
       """.stripMargin.replaceAll("\n", " "))

        prepped.setString(1, tableSchema)
        prepped.setString(2, crossdataTable.datasource)
        prepped.setString(3, partitionColumn)
        prepped.setString(4, tableOptions)
        prepped.setString(5, CrossdataVersion)
        prepped.execute()
        prepped.close()
      }
      connection.commit()

    } finally {
      connection.setAutoCommit(true)
    }


  override def dropTableMetadata(tableIdentifier: ViewIdentifierNormalized): Unit =
    connection.createStatement.executeUpdate(s"DELETE FROM $db.$tableWithTableMetadata WHERE tableName='${tableIdentifier.table}' AND db='${tableIdentifier.database.getOrElse("")}'")

  override def dropAllTablesMetadata(): Unit =
    connection.createStatement.executeUpdate(s"TRUNCATE $db.$tableWithTableMetadata")


  override def lookupView(tableIdentifier: TableIdentifierNormalized): Option[String] = {
    val resultSet = selectMetadata(tableWithViewMetadata, tableIdentifier)
    if (!resultSet.isBeforeFirst) {
      None
    } else {
      resultSet.next()
      Option(resultSet.getString(SqlViewField))
    }
  }

  override def persistViewMetadata(tableIdentifier: TableIdentifierNormalized, sqlText: String): Unit =
    try {
      connection.setAutoCommit(false)
      val resultSet = selectMetadata(tableWithViewMetadata, tableIdentifier)

      if (!resultSet.isBeforeFirst) {
        resultSet.close()
        val prepped = connection.prepareStatement(
          s"""|INSERT INTO $db.$tableWithViewMetadata (
              | $DatabaseField, $TableNameField, $SqlViewField, $CrossdataVersionField
              |) VALUES (?,?,?,?)
       """.stripMargin)
        prepped.setString(1, tableIdentifier.database.getOrElse(""))
        prepped.setString(2, tableIdentifier.table)
        prepped.setString(3, sqlText)
        prepped.setString(4, CrossdataVersion)
        prepped.execute()
        prepped.close()
      } else {
        resultSet.close()
        val prepped =
          connection.prepareStatement(
            s"""|UPDATE $db.$tableWithViewMetadata SET $SqlViewField=?
                |WHERE $DatabaseField='${tableIdentifier.database.getOrElse("")}' AND $TableNameField='${tableIdentifier.table}'
             """.stripMargin.replaceAll("\n", " "))

        prepped.setString(1, sqlText)
        prepped.execute()
        prepped.close()
      }
      connection.commit()

    } finally {
      connection.setAutoCommit(true)
    }

  private def selectMetadata(targetTable: String, tableIdentifier: TableIdentifierNormalized): ResultSet = {

    val preparedStatement = connection.prepareStatement(s"SELECT * FROM $db.$targetTable WHERE $DatabaseField= ? AND $TableNameField= ?")

    preparedStatement.setString(1, tableIdentifier.database.getOrElse(""))
    preparedStatement.setString(2, tableIdentifier.table)
    preparedStatement.executeQuery()

  }


  override def dropViewMetadata(viewIdentifier: ViewIdentifierNormalized): Unit =
    connection.createStatement.executeUpdate(
      s"DELETE FROM $db.$tableWithViewMetadata WHERE tableName='${viewIdentifier.table}' AND db='${viewIdentifier.database.getOrElse("")}'")


  override def dropAllViewsMetadata(): Unit = {
    connection.createStatement.executeUpdate(s"DELETE FROM $db.$tableWithViewMetadata")
  }


  override def saveAppMetadata(crossdataApp: CrossdataApp): Unit =
    try {
      connection.setAutoCommit(false)

      val preparedStatement = connection.prepareStatement(s"SELECT * FROM $db.$tableWithAppJars WHERE $AppAlias= ?")
      preparedStatement.setString(1, crossdataApp.appAlias)
      val resultSet = preparedStatement.executeQuery()
      preparedStatement.close()

      if (!resultSet.next()) {
        resultSet.close()
        val prepped = connection.prepareStatement(
          s"""|INSERT INTO $db.$tableWithAppJars (
              | $JarPath, $AppAlias, $AppClass
              |) VALUES (?,?,?)
         """.stripMargin)
        prepped.setString(1, crossdataApp.jar)
        prepped.setString(2, crossdataApp.appAlias)
        prepped.setString(3, crossdataApp.appClass)
        prepped.execute()
        prepped.close()
      } else {
        resultSet.close()
        val prepped = connection.prepareStatement(
          s"""|UPDATE $db.$tableWithAppJars SET $JarPath=?, $AppClass=?
              |WHERE $AppAlias='${crossdataApp.appAlias}'
         """.stripMargin)
        prepped.setString(1, crossdataApp.jar)
        prepped.setString(2, crossdataApp.appClass)
        prepped.execute()
        prepped.close()
      }
      connection.commit()
    } finally {
      connection.setAutoCommit(true)
    }

  override def getApp(alias: String): Option[CrossdataApp] = {

    val preparedStatement = connection.prepareStatement(s"SELECT * FROM $db.$tableWithAppJars WHERE $AppAlias= ?")
    preparedStatement.setString(1, alias)
    val resultSet = preparedStatement.executeQuery()

    if (!resultSet.next) {
      resultSet.close()
      preparedStatement.close()
      None
    } else {

      val jar = resultSet.getString(JarPath)
      val alias = resultSet.getString(AppAlias)
      val clss = resultSet.getString(AppClass)
      resultSet.close()
      preparedStatement.close()
      Some(
        CrossdataApp(jar, alias, clss)
      )
    }
  }

  override def isAvailable: Boolean = Option(connection).isDefined


  override def persistIndexMetadata(crossdataIndex: CrossdataIndex): Unit =
    try {
      connection.setAutoCommit(false)
      // check if the database-table exist in the persisted catalog
      val resultSet = selectMetadata(tableWithIndexMetadata, crossdataIndex.tableIdentifier)

      val serializedIndexedCols = serializeSeq(crossdataIndex.indexedCols)
      val serializedOptions = serializeOptions(crossdataIndex.opts)

      if (!resultSet.next()) {
        val prepped = connection.prepareStatement(
          s"""|INSERT INTO $db.$tableWithIndexMetadata (
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

  override def dropIndexMetadata(indexIdentifier: IndexIdentifierNormalized): Unit =
    connection.createStatement.executeUpdate(
      s"DELETE FROM $db.$tableWithIndexMetadata WHERE $IndexTypeField='${indexIdentifier.indexType}' AND $IndexNameField='${indexIdentifier.indexName}'"
    )

  override def dropAllIndexesMetadata(): Unit =
    connection.createStatement.executeUpdate(s"DELETE FROM $db.$tableWithIndexMetadata")

  override def lookupIndex(indexIdentifier: IndexIdentifierNormalized): Option[CrossdataIndex] = {
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

      Option(
        CrossdataIndex(TableIdentifierNormalized(table, Option(database)), IndexIdentifierNormalized(indexType, indexName),
          deserializeSeq(indexedCols), pk, datasource, deserializeOptions(optsJSON), version)
      )
    }
  }

  private def selectIndex(indexIdentifier: IndexIdentifierNormalized): ResultSet = {
    val preparedStatement = connection.prepareStatement(s"SELECT * FROM $db.$tableWithIndexMetadata WHERE $IndexNameField= ? AND $IndexTypeField= ?")
    preparedStatement.setString(1, indexIdentifier.indexName)
    preparedStatement.setString(2, indexIdentifier.indexType)
    preparedStatement.executeQuery()
  }

  override def dropIndexMetadata(tableIdentifier: TableIdentifierNormalized): Unit =
    connection.createStatement.executeUpdate(
      s"DELETE FROM $db.$tableWithIndexMetadata WHERE $TableNameField='${tableIdentifier.table}' AND $DatabaseField='${tableIdentifier.database.getOrElse("")}'"
    )

  override def lookupIndexByTableIdentifier(tableIdentifier: TableIdentifierNormalized): Option[CrossdataIndex] = {
    val query =
      s"SELECT * FROM $db.$tableWithIndexMetadata WHERE $TableNameField='${tableIdentifier.table}' AND $DatabaseField='${tableIdentifier.database.getOrElse("")}'"
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
        CrossdataIndex(TableIdentifierNormalized(table, Option(database)), IndexIdentifierNormalized(indexType, indexName),
          deserializeSeq(indexedCols), pk, datasource, deserializeOptions(optsJSON), version)
      )
    }
  }
}