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

import org.apache.spark.Logging
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.catalyst.{CatalystConf, SimpleCatalystConf, TableIdentifier}
import org.apache.spark.sql.crossdata.{XDCatalog, XDContext}
import org.apache.spark.sql.types._
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.write

import scala.annotation.tailrec
import scala.util.parsing.json.JSON


object DerbyCatalog {
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
 * Derby.
 * @param conf An implementation of the [[CatalystConf]].
 */
class DerbyCatalog(override val conf: CatalystConf = new SimpleCatalystConf(true), xdContext: XDContext)
  extends XDCatalog(conf, xdContext) with Logging {

  import JDBCCatalog._
  import org.apache.spark.sql.crossdata._


  private val db = "crossdata"
  private val table = "crossdatatables"

  lazy val connection: Connection = {

    val driver = "org.apache.derby.jdbc.EmbeddedDriver"
    val url = "jdbc:derby:sampledb/crossdata;create=true"

    Class.forName(driver)
    val jdbcConnection = DriverManager.getConnection(url)

    // CREATE PERSISTENT METADATA TABLE

    if(!schemaExists(s"$db", jdbcConnection)) {
      jdbcConnection.createStatement().executeUpdate(s"CREATE SCHEMA $db")


      jdbcConnection.createStatement().executeUpdate(
        s"""|CREATE TABLE $db.$table (
           |$DatabaseField VARCHAR(50),
           |$TableNameField VARCHAR(50),
           |$SchemaField LONG VARCHAR,
           |$DatasourceField LONG VARCHAR,
           |$PartitionColumnField LONG VARCHAR,
           |$OptionsField LONG VARCHAR,
           |$CrossdataVersionField LONG VARCHAR,
           |PRIMARY KEY ($DatabaseField,$TableNameField))""".stripMargin)
    }
    jdbcConnection
  }


  override def lookupTable(tableName: String, databaseName: Option[String]): Option[CrossdataTable] = {

    val preparedStatement = connection.prepareStatement(s"SELECT * FROM $db.$table WHERE $DatabaseField= ? AND $TableNameField= ?")
    preparedStatement.setString(1, databaseName.getOrElse(""))
    preparedStatement.setString(2, tableName)
    val resultSet = preparedStatement.executeQuery()

    if (!resultSet.next) {
      None
    } else {

      val database = resultSet.getString(DatabaseField)
      val table = resultSet.getString(TableNameField)
      val schemaJSON = resultSet.getString(SchemaField)
      val partitionColumn = resultSet.getString(PartitionColumnField)
      val provider = resultSet.getString(DatasourceField)
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

    if (!resultSet.next()) {
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
     val prepped = connection.prepareStatement(s"""|UPDATE $db.$table SET $SchemaField=?, $DatasourceField=?,$PartitionColumnField=?,$OptionsField=?,$CrossdataVersionField=?
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
    connection.createStatement.executeUpdate(s"DELETE FROM $db.$table")
  }


  private def getUserSpecifiedSchema(schemaJSON: String): Option[StructType] = {
    implicit val formats = DefaultFormats

    val jsonMap = JSON.parseFull(schemaJSON).get.asInstanceOf[Map[String, Any]]
    val fields = jsonMap.getOrElse("fields", throw new Error("Fields not found")).asInstanceOf[List[Map[String, Any]]]
    val structFields = fields.map { x => {


      val typeStr: String = x.getOrElse("type", throw new Error("Type not found")) match {
        case structType: Map[String, Any] => convertToGrammar(structType)
        case simpleType: String => simpleType
        case _ => throw new Error("Invalid type")
      }
      StructField(
        x.getOrElse("name", throw new Error("Name not found")).asInstanceOf[String],
        DataTypeParser.parse(typeStr),
        x.getOrElse("nullable", throw new Error("Nullable definition not found")).asInstanceOf[Boolean],
        Metadata.fromJson(write(x.getOrElse("metadata", throw new Error("Metadata not found")).asInstanceOf[Map[String, Any]]))
      )
    }
    }
    Some(StructType(structFields))
  }

  private def convertToGrammar (m: Map[String, Any]) : String = {
    if(m.contains("fields")) {
      val fields = m.get("fields").get.asInstanceOf[List[Map[String, Any]]].map(x=>{x.getOrElse("name", throw new Error("Name not found"))+":"+convertToGrammar(x)}) mkString ","
      "struct<"+fields+">"
    }
    else m.getOrElse("type", throw new Error("Type not found")).asInstanceOf[String]
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

  private def schemaExists(schema: String, connection: Connection) : Boolean= {
    val preparedStatement = connection.prepareStatement(s"SELECT * FROM SYS.SYSSCHEMAS WHERE schemaname='CROSSDATA'")
    //preparedStatement.setString(1, databaseName.getOrElse(""))
    val resultSet = preparedStatement.executeQuery()

    resultSet.next()

  }

}