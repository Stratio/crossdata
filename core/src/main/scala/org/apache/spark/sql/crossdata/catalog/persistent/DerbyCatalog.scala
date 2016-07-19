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

import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet}

import com.stratio.crossdata.util.using
import org.apache.spark.sql.catalyst.{CatalystConf, TableIdentifier}
import org.apache.spark.sql.crossdata.CrossdataVersion
import org.apache.spark.sql.crossdata.catalog.{IndexIdentifierNormalized, TableIdentifierNormalized, XDCatalog, persistent}

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
class DerbyCatalog(override val catalystConf: CatalystConf)
  extends PersistentCatalogWithCache(catalystConf) {

  import DerbyCatalog._
  import XDCatalog._

  @transient lazy val connection: Connection = {

    val driver = "org.apache.derby.jdbc.EmbeddedDriver"
    val url = "jdbc:derby:sampledb/crossdata;create=true"

    Class.forName(driver)
    val jdbcConnection = DriverManager.getConnection(url)

    def executeUpdate(sql: String) = using(jdbcConnection.createStatement()) { statement =>
      statement.executeUpdate(sql)
    }

    def schemaExists(schema: String, connection: Connection): Boolean =
      withStatement(s"SELECT * FROM SYS.SYSSCHEMAS WHERE schemaname='$schema'") { statement =>
        withResultSet(statement) { resultSet =>
          resultSet.next()
        }
      }(connection)

    // CREATE PERSISTENT METADATA TABLE

    if (!schemaExists(DB, jdbcConnection)) {
      executeUpdate(s"CREATE SCHEMA $DB")


      executeUpdate(
        s"""|CREATE TABLE $DB.$TableWithTableMetadata (
            |$DatabaseField VARCHAR(50),
            |$TableNameField VARCHAR(50),
            |$SchemaField LONG VARCHAR,
            |$DatasourceField LONG VARCHAR,
            |$PartitionColumnField LONG VARCHAR,
            |$OptionsField LONG VARCHAR,
            |$CrossdataVersionField LONG VARCHAR,
            |PRIMARY KEY ($DatabaseField,$TableNameField))""".stripMargin)

      executeUpdate(
        s"""|CREATE TABLE $DB.$TableWithViewMetadata (
            |$DatabaseField VARCHAR(50),
            |$TableNameField VARCHAR(50),
            |$SqlViewField LONG VARCHAR,
            |$CrossdataVersionField VARCHAR(30),
            |PRIMARY KEY ($DatabaseField,$TableNameField))""".stripMargin)

      executeUpdate(
        s"""|CREATE TABLE $DB.$TableWithAppJars (
            |$JarPath VARCHAR(100),
            |$AppAlias VARCHAR(50),
            |$AppClass VARCHAR(100),
            |PRIMARY KEY ($AppAlias))""".stripMargin)
    }

    //Index support
    if (!indexTableExists(DB, jdbcConnection)) {
      executeUpdate(
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
            |PRIMARY KEY ($DatabaseField,$TableNameField))""".stripMargin)
    }

    jdbcConnection
  }


  def executeSQLCommand(sql: String): Unit = synchronized {
    using(connection.createStatement()) { statement =>
      statement.executeUpdate(sql)
    }
  }

  private def withConnectionWithoutCommit[T](f: Connection => T): T = synchronized {
    try {
      connection.setAutoCommit(false)
      f(connection)
    } finally {
      connection.setAutoCommit(true)
    }
  }

  private def withStatement[T](sql: String)(f: PreparedStatement => T)(implicit conn: Connection = connection): T =
    synchronized {
      using(conn.prepareStatement(sql)) { statement =>
        f(statement)
      }
    }

  private def withResultSet[T](prepared: PreparedStatement)(f: ResultSet => T): T = synchronized {
    using(prepared.executeQuery()) { resultSet =>
      f(resultSet)
    }
  }

  override def lookupTable(tableIdentifier: TableIdentifierNormalized): Option[CrossdataTable] =
    selectMetadata(TableWithTableMetadata, tableIdentifier) { resultSet =>
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
          CrossdataTable(TableIdentifierNormalized(table, Some(database)), Option(deserializeUserSpecifiedSchema(schemaJSON)), datasource,
            deserializePartitionColumn(partitionColumn), deserializeOptions(optsJSON), version)
        )
      }
    }

  override def getApp(alias: String): Option[CrossdataApp] =
    withStatement(s"SELECT * FROM $DB.$TableWithAppJars WHERE $AppAlias= ?") { statement =>
      statement.setString(1, alias)
      withResultSet(statement) { resultSet =>
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
    }


  override def lookupView(viewIdentifier: ViewIdentifierNormalized): Option[String] =
    selectMetadata(TableWithViewMetadata, viewIdentifier) { resultSet =>
      if (!resultSet.next)
        None
      else
        Option(resultSet.getString(SqlViewField))
    }

  override def lookupIndex(indexIdentifier: IndexIdentifierNormalized): Option[CrossdataIndex] =
    selectIndex(indexIdentifier) { resultSet =>

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
          CrossdataIndex(TableIdentifierNormalized(table, Some(database)), IndexIdentifierNormalized(indexType, indexName),
            deserializeSeq(indexedCols), pk, datasource, deserializeOptions(optsJSON), version)
        )
      }
    }


  override def persistTableMetadata(crossdataTable: CrossdataTable): Unit =
    withConnectionWithoutCommit { implicit conn =>
      val tableSchema = serializeSchema(crossdataTable.schema.getOrElse(schemaNotFound()))
      val tableOptions = serializeOptions(crossdataTable.opts)
      val partitionColumn = serializePartitionColumn(crossdataTable.partitionColumn)

      // check if the database-table exist in the persisted catalog
      selectMetadata(TableWithTableMetadata, crossdataTable.tableIdentifier) { resultSet =>

        if (!resultSet.next()) {
          withStatement(
            s"""|INSERT INTO $DB.$TableWithTableMetadata (
                | $DatabaseField, $TableNameField, $SchemaField, $DatasourceField, $PartitionColumnField, $OptionsField, $CrossdataVersionField
                |) VALUES (?,?,?,?,?,?,?)
        """.stripMargin) { statement2 =>
            statement2.setString(1, crossdataTable.tableIdentifier.database.getOrElse(""))
            statement2.setString(2, crossdataTable.tableIdentifier.table)
            statement2.setString(3, tableSchema)
            statement2.setString(4, crossdataTable.datasource)
            statement2.setString(5, partitionColumn)
            statement2.setString(6, tableOptions)
            statement2.setString(7, CrossdataVersion)
            statement2.execute()
          }

        } else {
          withStatement(
            s"""|UPDATE $DB.$TableWithTableMetadata
                |SET $SchemaField=?, $DatasourceField=?,$PartitionColumnField=?,$OptionsField=?,$CrossdataVersionField=?
                |WHERE $DatabaseField='${crossdataTable.tableIdentifier.database.getOrElse("")}' AND $TableNameField='${crossdataTable.tableIdentifier.table}'""".stripMargin) {
            statement2 =>
              statement2.setString(1, tableSchema)
              statement2.setString(2, crossdataTable.datasource)
              statement2.setString(3, partitionColumn)
              statement2.setString(4, tableOptions)
              statement2.setString(5, CrossdataVersion)
              statement2.execute()
          }
        }
        connection.commit()
      }
    }


  override def persistViewMetadata(tableIdentifier: TableIdentifierNormalized, sqlText: String): Unit =
    withConnectionWithoutCommit { implicit conn =>
      selectMetadata(TableWithViewMetadata, tableIdentifier) { resultSet =>
        if (!resultSet.next()) {
          withStatement(
            s"""|INSERT INTO $DB.$TableWithViewMetadata (
                | $DatabaseField, $TableNameField, $SqlViewField, $CrossdataVersionField
                |) VALUES (?,?,?,?)""".stripMargin) { statement2 =>

            statement2.setString(1, tableIdentifier.database.getOrElse(""))
            statement2.setString(2, tableIdentifier.table)
            statement2.setString(3, sqlText)
            statement2.setString(4, CrossdataVersion)
            statement2.execute()
          }
        } else {
          val prepped = connection.prepareStatement(
            s"""|UPDATE $DB.$TableWithViewMetadata SET $SqlViewField=?
                |WHERE $DatabaseField='${tableIdentifier.database.getOrElse("")}' AND $TableNameField='${tableIdentifier.table}'
         """.stripMargin)
          prepped.setString(1, sqlText)
          prepped.execute()
        }
        connection.commit()
      }
    }


  override def persistIndexMetadata(crossdataIndex: CrossdataIndex): Unit =
    withConnectionWithoutCommit { implicit conn =>

      selectMetadata(TableWithIndexMetadata, crossdataIndex.tableIdentifier) { resultSet =>
        val serializedIndexedCols = serializeSeq(crossdataIndex.indexedCols)
        val serializedOptions = serializeOptions(crossdataIndex.opts)

        if (!resultSet.next()) {
          withStatement(
            s"""|INSERT INTO $DB.$TableWithIndexMetadata (
                | $DatabaseField, $TableNameField, $IndexNameField, $IndexTypeField, $IndexedColsField,
                | $PKField, $DatasourceField, $OptionsField, $CrossdataVersionField
                |) VALUES (?,?,?,?,?,?,?,?,?)""".stripMargin) { statement2 =>
            statement2.setString(1, crossdataIndex.tableIdentifier.database.getOrElse(""))
            statement2.setString(2, crossdataIndex.tableIdentifier.table)
            statement2.setString(3, crossdataIndex.indexIdentifier.indexName)
            statement2.setString(4, crossdataIndex.indexIdentifier.indexType)
            statement2.setString(5, serializedIndexedCols)
            statement2.setString(6, crossdataIndex.pk)
            statement2.setString(7, crossdataIndex.datasource)
            statement2.setString(8, serializedOptions)
            statement2.setString(9, CrossdataVersion)
            statement2.execute()
          }
        } else {
          //TODO: Support change index metadata?
          sys.error(s"A global index already exists in table ${crossdataIndex.tableIdentifier.unquotedString}")
        }
      }
    }


  override def saveAppMetadata(crossdataApp: CrossdataApp): Unit =
    withConnectionWithoutCommit { implicit conn =>

      withStatement(s"SELECT * FROM $DB.$TableWithAppJars WHERE $AppAlias= ?") { statement =>
        statement.setString(1, crossdataApp.appAlias)

        withResultSet(statement) { resultSet =>
          if (!resultSet.next()) {
            withStatement(s"INSERT INTO $DB.$TableWithAppJars ($JarPath, $AppAlias, $AppClass) VALUES (?,?,?)") { statement2 =>
              statement2.setString(1, crossdataApp.jar)
              statement2.setString(2, crossdataApp.appAlias)
              statement2.setString(3, crossdataApp.appClass)
              statement2.execute()
            }
          } else {
            withStatement(s"UPDATE $DB.$TableWithAppJars SET $JarPath=?, $AppClass=? WHERE $AppAlias='${crossdataApp.appAlias}'") { statement2 =>
              statement2.setString(1, crossdataApp.jar)
              statement2.setString(2, crossdataApp.appClass)
              statement2.execute()
            }
          }
          conn.commit()
        }
      }
    }

  override def dropTableMetadata(tableIdentifier: TableIdentifierNormalized): Unit =
    executeSQLCommand(
      s"DELETE FROM $DB.$TableWithTableMetadata WHERE tableName='${tableIdentifier.table}' AND db='${tableIdentifier.database.getOrElse("")}'"
    )

  override def dropViewMetadata(viewIdentifier: ViewIdentifierNormalized): Unit =
    executeSQLCommand(
      s"DELETE FROM $DB.$TableWithViewMetadata WHERE tableName='${viewIdentifier.table}' AND db='${viewIdentifier.database.getOrElse("")}'"
    )

  override def dropIndexMetadata(indexIdentifier: IndexIdentifierNormalized): Unit =
    executeSQLCommand(
      s"DELETE FROM $DB.$TableWithIndexMetadata WHERE $IndexTypeField='${indexIdentifier.indexType}' AND $IndexNameField='${indexIdentifier.indexName}'"
    )

  override def dropIndexMetadata(tableIdentifier: TableIdentifierNormalized): Unit =
    executeSQLCommand(
      s"DELETE FROM $DB.$TableWithIndexMetadata WHERE $TableNameField='${tableIdentifier.table}' AND $DatabaseField='${tableIdentifier.database.getOrElse("")}'"
    )


  override def dropAllTablesMetadata(): Unit =
    executeSQLCommand(s"DELETE FROM $DB.$TableWithTableMetadata")

  override def dropAllViewsMetadata(): Unit =
    executeSQLCommand(s"DELETE FROM $DB.$TableWithViewMetadata")

  override def dropAllIndexesMetadata(): Unit =
    executeSQLCommand(s"DELETE FROM $DB.$TableWithIndexMetadata")


  override def isAvailable: Boolean = true

  override def allRelations(databaseName: Option[String]): Seq[TableIdentifierNormalized] = synchronized {
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
    val dbFilter = databaseName.fold("")(dbName => s"WHERE $DatabaseField ='$dbName'")
    val resultSet = statement.executeQuery(s"SELECT $DatabaseField, $TableNameField FROM $DB.$TableWithTableMetadata $dbFilter")

    getSequenceAux(resultSet, resultSet.next).toSeq
  }

  private def selectMetadata[T](targetTable: String, tableIdentifier: TableIdentifierNormalized)(f: ResultSet => T): T =
    withStatement(s"SELECT * FROM $DB.$targetTable WHERE $DatabaseField= ? AND $TableNameField= ?") { statement =>
      statement.setString(1, tableIdentifier.database.getOrElse(""))
      statement.setString(2, tableIdentifier.table)

      withResultSet(statement) { resultSet =>
        f(resultSet)
      }
    }


  private def selectIndex[T](indexIdentifier: IndexIdentifierNormalized)(f: ResultSet => T): T =
    withStatement(s"SELECT * FROM $DB.$TableWithIndexMetadata WHERE $IndexNameField= ? AND $IndexTypeField= ?") { statement =>
      statement.setString(1, indexIdentifier.indexName)
      statement.setString(2, indexIdentifier.indexType)

      withResultSet(statement) { resultSet =>
        f(resultSet)
      }
    }


  private def indexTableExists(schema: String, connection: Connection): Boolean = tableSchemaExists(schema, TableWithIndexMetadata, connection)

  private def tableSchemaExists(schema: String, table: String, connection: Connection): Boolean =
    withStatement(
      s"""|SELECT * FROM SYS.SYSSCHEMAS sch
          |LEFT JOIN SYS.SYSTABLES tb ON tb.schemaid = sch.schemaid
          |WHERE sch.SCHEMANAME='$schema' AND tb.TABLENAME='${table.toUpperCase}'""".stripMargin) { statement =>
      withResultSet(statement) { resultSet =>
        resultSet.next()
      }
    }(connection)

  override def lookupIndexByTableIdentifier(tableIdentifier: TableIdentifierNormalized): Option[CrossdataIndex] = {
    val query =
      s"SELECT * FROM $DB.$TableWithIndexMetadata WHERE $TableNameField='${tableIdentifier.table}' AND $DatabaseField='${tableIdentifier.database.getOrElse("")}'"

    withStatement(query) { statement =>
      withResultSet(statement) { resultSet =>
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
            CrossdataIndex(TableIdentifierNormalized(table, Some(database)), IndexIdentifierNormalized(indexType, indexName),
              deserializeSeq(indexedCols), pk, datasource, deserializeOptions(optsJSON), version)
          )
        }
      }
    }
  }
}