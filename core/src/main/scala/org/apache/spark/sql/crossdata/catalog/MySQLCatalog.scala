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
import org.apache.spark.sql.catalyst.plans.logical.{LogicalPlan, Subquery}
import org.apache.spark.sql.catalyst.{TableIdentifier, CatalystConf, SimpleCatalystConf}
import org.apache.spark.sql.crossdata.{CrossdataTable, XDCatalog, XDContext}
import org.apache.spark.sql.execution.datasources.{ResolvedDataSource, LogicalRelation}
import org.apache.spark.sql.types._
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.write
import org.apache.spark.sql.crossdata._

import scala.annotation.tailrec
import scala.util.parsing.json.JSON

/**
 * Default implementation of the [[org.apache.spark.sql.crossdata.XDCatalog]] with persistence using
 * MySQL.
 * @param conf An implementation of the [[CatalystConf]].
 */
class MySQLCatalog(override val conf: CatalystConf = new SimpleCatalystConf(true), xDContext: XDContext)
  extends XDCatalog(conf) with Logging  {

  def config: Config = ConfigFactory.load

  lazy val ip= config.getString(Ip)
  lazy val port= config.getString(Port)
  lazy val driver= config.getString(Driver)
  lazy val db= config.getString(Database)
  lazy val table= config.getString(Table)
  lazy val user= config.getString(User)
  lazy val pass= config.getString(Pass)
  lazy val crossdataVersion= config.getString(CrossdataVersion)

  lazy val url=s"jdbc:mysql://$ip:$port"

  lazy val connection: Connection = {
    Class.forName(driver)
    DriverManager.getConnection(url, user, pass)
  }

  //CREATE PERSISTENT METADATA TABLE
  connection.createStatement().executeUpdate(s"CREATE DATABASE IF NOT EXISTS $db")
  connection.createStatement().executeUpdate(s"""CREATE TABLE IF NOT EXISTS $db.$table (db VARCHAR(50),
                                                                                        | tableName VARCHAR(50),
                                                                                        | sch TEXT,
                                                                                        | provider TEXT,
                                                                                        | partitionColumn TEXT,
                                                                                        | options TEXT,
                                                                                        | crossdataVersion TEXT,
                                                                                        | PRIMARY KEY (db,tablename))""".stripMargin)

  /**
   * Persist in XD Catalog
   */
  override def persistTable(crossdataTable: CrossdataTable):
  Unit = {

    //super.registerTable(tableName, plan)
    logInfo("XDCatalog: Persist Table")

    // TODO: Evaluate userSpecifiedSchema as Options
    // TODO: Test StructTypes with multiple subdocuments
    val tableSchema = serializeSchema(crossdataTable.userSpecifiedSchema.get)
    val tableOptions = serializeOptions(crossdataTable.opts)
    val partitionColumn = serializePartitionColumn(crossdataTable.partitionColumn)

    val statement = connection.createStatement

    connection.setAutoCommit(false)
    val prepped = connection.prepareStatement(
      s"""INSERT INTO $db.$table (db, tableName, sch, provider, partitionColumn, options, crossdataVersion) VALUES(?,?,?,?,?,?,?)
                                  |ON DUPLICATE KEY UPDATE
                                  |sch = VALUES (sch),
                                  |provider = VALUES (provider),
                                  |partitionColumn = VALUES (partitionColumn),
                                  |options = VALUES (options),
                                  |crossdataVersion = VALUES (crossdataVersion)
       """.stripMargin)

    crossdataTable.dbName match {
      case Some(db) => prepped.setString(1, db)
      case None => prepped.setString(1, "")
    }
    prepped.setString(2, crossdataTable.tableName)
    prepped.setString(3, tableSchema)
    prepped.setString(4, crossdataTable.provider)
    prepped.setString(5, partitionColumn)
    prepped.setString(6, tableOptions)
    prepped.setString(7, crossdataVersion)
    prepped.execute()

    connection.commit()
    connection.setAutoCommit(true)

    //Try to register the table.
    lookupRelation(TableIdentifier(crossdataTable.tableName, crossdataTable.dbName).toSeq)
  }


  /**
   * Drop all tables of catalog
   */
  override def dropAllTables(): Unit = {
    logInfo("XDCatalog: Drop all tables from catalog")
    val statement = connection.createStatement
    statement.executeUpdate(s"""TRUNCATE $db.$table""")
    super.unregisterAllTables()
  }

  /**
   * Drop table from XD catalog
   */
  override def dropTable(tableIdentifier: Seq[String]): Unit = {
    logInfo("XDCatalog: Delete Table from catalog")
    val (database, tableName) = {
      val auxSeq = Seq("").filter(x=>tableIdentifier.length < 2) ++ tableIdentifier
      (auxSeq zip auxSeq.tail).head
    }
    val statement = connection.createStatement
    if(database == "") {
      val res = statement.executeUpdate( s"""DELETE FROM $db.$table WHERE tableName='$tableName' AND db=''""")

    }
    else
      statement.executeUpdate(s"""DELETE FROM $db.$table WHERE tableName='$tableName' AND db='$database'""")
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

    val (database, tablename) = {
      val auxSeq = Seq("").filter(x=>tableIdentifier.length < 2) ++ tableIdentifier
      (auxSeq zip auxSeq.tail).head
    }

    val statement = connection.createStatement
    val resultSet=statement.executeQuery(
      s"""SELECT * FROM $db.$table WHERE db='$database' AND tableName='$tablename'""".stripMargin)

    if (!resultSet.isBeforeFirst() ) {
      None
    }else{
      resultSet.next()
      val database = resultSet.getString("db")
      val table = resultSet.getString("tableName")
      val schemaJSON = resultSet.getString("sch")
      val partitionColumn = resultSet.getString("partitionColumn")
      val provider= resultSet.getString("provider")
      val optsJSON = resultSet.getString("options")
      val version = resultSet.getString(("crossdataVersion"))

      Some(CrossdataTable(table,Some(database),getUserSpecifiedSchema(schemaJSON),provider, getPartitionColumn(partitionColumn),
        getOptions(optsJSON), version))
    }
  }

  private def createLogicalRelation(crossdataTable: CrossdataTable): LogicalRelation = {
    val resolved = ResolvedDataSource(xDContext,  crossdataTable.userSpecifiedSchema, crossdataTable.partitionColumn, crossdataTable.provider, crossdataTable.opts)
    LogicalRelation(resolved.relation)
  }

  private def getUserSpecifiedSchema(schemaJSON: String): Option[StructType] = {
    //JSON.parseFull(schemaJSON).get.asInstanceOf[StructType]
    val jsonMap = JSON.parseFull(schemaJSON).get.asInstanceOf[Map[String, Any]]
    // TODO Create new Exception?
    // TODO pass metadata to StructFields
    // Metadata.build() ??? x.getOrElse("metadata", "")
    val structFields = jsonMap.getOrElse("fields", throw new Exception).asInstanceOf[List[Map[String, Any]]]
      .map(x => StructField(x.getOrElse("name", "").asInstanceOf[String], DataTypeParser.parse(x.getOrElse("type", "").asInstanceOf[String]), x.getOrElse("nullable", "").asInstanceOf[Boolean]))
    Option(StructType(structFields))



  }

  private def getPartitionColumn(partitionColumn: String): Array[String] =
    JSON.parseFull(partitionColumn).toList flatMap(_.asInstanceOf[List[String]]) toArray

  private def getOptions(optsJSON: String): Map[String,String] =
    JSON.parseFull(optsJSON).get.asInstanceOf[Map[String,String]]

  private def serializeSchema(schema: StructType) : String = {
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

  override
  def getPersistenceTables(databaseName: Option[String]): Seq[(String, Boolean)] = {

    @tailrec
    def getSequenceAux(resultset: ResultSet, next: Boolean, set: Set[String] = Set()): Set[String] = {
      if (next) {
        val database = resultset.getString("db")
        val table = resultset.getString("tableName")
        val tableId = if (database.trim.isEmpty) table else  s"""$database.$table"""
        getSequenceAux(resultset,resultset.next(), set + tableId)
      } else {
        set
      }
    }

    val statement = connection.createStatement
    val resultSet = databaseName match{
      case Some(database) => statement.executeQuery(s"""SELECT * FROM $db.$table WHERE db='$database'""".stripMargin)
      case None => statement.executeQuery(s"""SELECT db, tableName FROM $db.$table""".stripMargin)
    }
    getSequenceAux(resultSet, resultSet.next).map( tableId => (tableId, true)).toSeq
  }


}
