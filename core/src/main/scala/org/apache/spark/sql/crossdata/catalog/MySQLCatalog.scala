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
import org.apache.spark.sql.catalyst.{CatalystConf, SimpleCatalystConf, TableIdentifier}
import org.apache.spark.sql.crossdata.{XDCatalog, XDContext}
import org.apache.spark.sql.types._
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.write

import scala.annotation.tailrec
import scala.util.parsing.json.JSON


object MySQLCatalog {
  // SQLConfig
  val Driver = "crossdata.catalog.mysql.driver"
  val Ip = "crossdata.catalog.mysql.ip"
  val Port = "crossdata.catalog.mysql.port"
  val Database = "crossdata.catalog.mysql.db.name"
  val Table = "crossdata.catalog.mysql.db.persistTable"
  val User = "crossdata.catalog.mysql.db.user"
  val Pass = "crossdata.catalog.mysql.db.pass"
  // CatalogFields
  val DatabaseField = "db"
  val TableNameField = "tableName"
  val SchemaField = "tableSchema"
  val ProviderField = "provider"
  val PartitionColumnField = "partitionColumn"
  val OptionsField = "options"
  val CrossdataVersionField = "crossdataVersion"
}

/**
 * Default implementation of the [[org.apache.spark.sql.crossdata.XDCatalog]] with persistence using
 * MySQL.
 * @param conf An implementation of the [[CatalystConf]].
 */
class MySQLCatalog(override val conf: CatalystConf = new SimpleCatalystConf(true), xdContext: XDContext)
  extends XDCatalog(conf, xdContext) with Logging {

  import MySQLCatalog._
  import org.apache.spark.sql.crossdata._

  private val config: Config = ConfigFactory.load
  private val db = config.getString(Database)
  private val table = config.getString(Table)

  lazy val connection: Connection = {

    val ip = config.getString(Ip)
    val port = config.getString(Port)
    val driver = config.getString(Driver)
    val user = config.getString(User)
    val pass = config.getString(Pass)
    val url = s"jdbc:mysql://$ip:$port"

    Class.forName(driver)
    val mysqlConnection = DriverManager.getConnection(url, user, pass)

    // CREATE PERSISTENT METADATA TABLE
    mysqlConnection.createStatement().executeUpdate(s"CREATE DATABASE IF NOT EXISTS $db")
    mysqlConnection.createStatement().executeUpdate(
      s"""|CREATE TABLE IF NOT EXISTS $db.$table (
          |$DatabaseField VARCHAR(50),
          |$TableNameField VARCHAR(50),
          |$SchemaField TEXT,
          |$ProviderField TEXT,
          |$PartitionColumnField TEXT,
          |$OptionsField TEXT,
          |$CrossdataVersionField TEXT,
          |PRIMARY KEY ($DatabaseField,$TableNameField))""".stripMargin)
    mysqlConnection
  }


  override def lookupTable(tableName: String, databaseName: Option[String]): Option[CrossdataTable] = {

    val preparedStatement = connection.prepareStatement(s"SELECT * FROM $db.$table WHERE $DatabaseField= ? AND $TableNameField= ?")
    preparedStatement.setString(1, databaseName.getOrElse(""))
    preparedStatement.setString(2, tableName)
    val resultSet = preparedStatement.executeQuery()

    //val statement = connection.createStatement
    //val resultSet = preparedStatement.executeQuery(s"SELECT * FROM $db.$table WHERE db='${databaseName.getOrElse("")}' AND tableName='$tableName'")

    if (!resultSet.isBeforeFirst) {
      None
    } else {
      resultSet.next()
      val database = resultSet.getString(DatabaseField)
      val table = resultSet.getString(TableNameField)
      val schemaJSON = resultSet.getString(SchemaField)
      val partitionColumn = resultSet.getString(PartitionColumnField)
      val provider = resultSet.getString(ProviderField)
      val optsJSON = resultSet.getString(OptionsField)
      val version = resultSet.getString(CrossdataVersionField)

      Some(
        CrossdataTable(table, Some(database), getUserSpecifiedSchema(schemaJSON), provider, getPartitionColumn(partitionColumn), getOptions(optsJSON), version)
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

  override def persistTableMetadata(crossdataTable: CrossdataTable): Unit = {

    // TODO: Evaluate userSpecifiedSchema as Options
    // TODO: Test StructTypes with multiple subdocuments
    val tableSchema = serializeSchema(crossdataTable.userSpecifiedSchema.getOrElse(new StructType()))
    val tableOptions = serializeOptions(crossdataTable.opts)
    val partitionColumn = serializePartitionColumn(crossdataTable.partitionColumn)

    connection.setAutoCommit(false)
    val prepped = connection.prepareStatement(
      s"""|INSERT INTO $db.$table (
          | $DatabaseField, $TableNameField, $SchemaField, $ProviderField, $PartitionColumnField, $OptionsField, $CrossdataVersionField
          |) VALUES (?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE
          |$SchemaField = VALUES ($SchemaField),
          |$ProviderField = VALUES ($ProviderField),
          |$PartitionColumnField = VALUES ($PartitionColumnField),
          |$OptionsField = VALUES ($OptionsField),
          |$CrossdataVersionField = VALUES ($CrossdataVersionField)
       """.stripMargin)

    prepped.setString(1, crossdataTable.dbName.getOrElse(""))
    prepped.setString(2, crossdataTable.tableName)
    prepped.setString(3, tableSchema)
    prepped.setString(4, crossdataTable.provider)
    prepped.setString(5, partitionColumn)
    prepped.setString(6, tableOptions)
    prepped.setString(7, CrossdataVersion)
    prepped.execute()
    connection.commit()
    connection.setAutoCommit(true)

    //Try to register the table.
    lookupRelation(TableIdentifier(crossdataTable.tableName, crossdataTable.dbName).toSeq)
  }

  override def dropPersistedTable(tableName: String, databaseName: Option[String]): Unit = {
    connection.createStatement.executeUpdate(s"DELETE FROM $db.$table WHERE tableName='$tableName' AND db='${databaseName.getOrElse("")}'")
  }

  override def dropAllPersistedTables(): Unit = {
    connection.createStatement.executeUpdate(s"TRUNCATE $db.$table")
  }


  private def getUserSpecifiedSchema(schemaJSON: String): Option[StructType] = {
    val jsonMap = JSON.parseFull(schemaJSON).get.asInstanceOf[Map[String, Any]]
    // TODO Create new Exception?
    // TODO pass metadata to StructFields x.getOrElse("metadata", "")
    val fields = jsonMap.getOrElse("fields", throw new Exception).asInstanceOf[List[Map[String, Any]]]
    val structFields = fields.map { x =>
      StructField(
        x.getOrElse("name", "").asInstanceOf[String],
        DataTypeParser.parse(x.getOrElse("type", "").asInstanceOf[String]),
        x.getOrElse("nullable", "").asInstanceOf[Boolean]
      )
    }
    Some(StructType(structFields))
  }

  private def getPartitionColumn(partitionColumn: String): Array[String] =
    JSON.parseFull(partitionColumn).toList.flatMap(_.asInstanceOf[List[String]]).toArray

  private def getOptions(optsJSON: String): Map[String, String] =
    JSON.parseFull(optsJSON).get.asInstanceOf[Map[String, String]]

  private def serializeSchema(schema: StructType): String = {
    implicit val formats = DefaultFormats
    write(schema.jsonValue.values)
  }

  private def serializeOptions(options: Map[String, Any]): String = {
    implicit val formats = DefaultFormats
    write(options)
  }

  private def serializePartitionColumn(partitionColumn: Array[String]): String = {
    implicit val formats = DefaultFormats
    write(partitionColumn)
  }

}