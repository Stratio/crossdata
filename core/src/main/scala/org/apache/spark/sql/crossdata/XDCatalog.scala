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
package org.apache.spark.sql.crossdata

import org.apache.spark.Logging
import org.apache.spark.sql.catalyst.analysis.Catalog
import org.apache.spark.sql.catalyst.plans.logical.{LogicalPlan, Subquery}
import org.apache.spark.sql.catalyst.{CatalystConf, SimpleCatalystConf, TableIdentifier}
import org.apache.spark.sql.execution.datasources.{LogicalRelation, ResolvedDataSource}
import org.apache.spark.sql.types._
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization._

import scala.collection.mutable
import scala.util.parsing.json.JSON

/**
 * CrossdataCatalog aims to provide a mechanism to persist the
 * [[org.apache.spark.sql.catalyst.analysis.Catalog]] metadata.
 */
abstract class XDCatalog(val conf: CatalystConf = new SimpleCatalystConf(true),
                         xdContext: XDContext) extends Catalog with Logging with Serializable {

  // TODO We should use a limited cache

  val tables = new mutable.HashMap[String, LogicalPlan]()

  override def tableExists(tableIdentifier: Seq[String]): Boolean = {
    val tableIdent = processTableIdentifier(tableIdentifier)
    if (tables.get(getDbTableName(tableIdent)).isDefined) {
      true
    } else {
      val (table, database) = tableIdToTuple(tableIdentifier)
      lookupTable(table, database).fold(false){ crossdataTable =>
        val logicalPlan: LogicalPlan = createLogicalRelation(crossdataTable)
        registerTable(tableIdentifier, logicalPlan)
        true
      }
    }
  }

  override def getTables(databaseName: Option[String]): Seq[(String, Boolean)] = {
    def processDBIdentifier(dbName: String): String = if (conf.caseSensitiveAnalysis) dbName else dbName.toLowerCase
    def tablesInDb(dbName: String): mutable.HashMap[String, LogicalPlan] = tables.filter {
      case (tableIdentifier, _) =>
        tableIdentifier.split("\\.")(0) == dbName
      case _ =>
        false
    }

    val dbName = databaseName.map(processDBIdentifier)
    val cachedTables = dbName.fold {
      tables.map {
        case (tableName, _) => (tableName, true)
      }.toSeq
    } { definedDBName =>
      tablesInDb(definedDBName).map{
        case (tableName, _) => (tableName.split("\\.")(0) + "." + tableName.split("\\.")(1), true)
      }.toSeq
    }
    // persisted tables replace temporary tables
    (cachedTables.toMap ++ listPersistedTables(databaseName).toMap).toSeq
  }

  override def lookupRelation(tableIdentifier: Seq[String], alias: Option[String]): LogicalPlan = {
    val tableIdent = processTableIdentifier(tableIdentifier)
    lookupRelationCache(tableIdent, alias).getOrElse {
      val (table, database) = tableIdToTuple(tableIdent)
      logInfo(s"XDCatalog: Looking up table ${tableIdent.mkString(".")}")
      lookupTable(table, database) match {
        case Some(crossdataTable) =>
          val table: LogicalPlan = createLogicalRelation(crossdataTable)
          registerTable(tableIdent, table)
          processAlias(tableIdent, table, alias)

        case None =>
          sys.error(s"Table Not Found: ${tableIdent.mkString(".")}")
      }
    }
  }

  def lookupRelationCache(processedTableIdentifier: Seq[String], alias: Option[String]): Option[LogicalPlan] = {
    val tableFullName = getDbTableName(processedTableIdentifier)
    val tableOpt = tables.get(tableFullName)
    tableOpt.fold[Option[LogicalPlan]] {
      None
    } { table =>
      Some( processAlias(processedTableIdentifier, table, alias))
    }
  }

  // TODO: Review it in future Spark versions
  private def processAlias( processedTableIdentifier: Seq[String], lPlan: LogicalPlan, alias: Option[String]) = {
    val tableWithQualifiers = Subquery(processedTableIdentifier.last, lPlan)
    // If an alias was specified by the lookup, wrap the plan in a subquery so that attributes are
    // properly qualified with this alias.
    alias.map(a => Subquery(a, tableWithQualifiers)).getOrElse(tableWithQualifiers)
  }

  override def registerTable(tableIdentifier: Seq[String], plan: LogicalPlan): Unit = {
    val tableIdent = processTableIdentifier(tableIdentifier)
    tables.put(getDbTableName(tableIdent), plan)
  }

  override def unregisterTable(tableIdentifier: Seq[String]): Unit = {
    val tableIdent = processTableIdentifier(tableIdentifier)
    tables remove getDbTableName(tableIdent)
  }

  override def unregisterAllTables(): Unit =
    tables.clear()

  override def refreshTable(tableIdentifier: TableIdentifier): Unit = {
    throw new UnsupportedOperationException
  }

  private def createLogicalRelation(crossdataTable: CrossdataTable): LogicalRelation = {
    val resolved = ResolvedDataSource(xdContext, crossdataTable.userSpecifiedSchema, crossdataTable.partitionColumn, crossdataTable.datasource, crossdataTable.opts)
    LogicalRelation(resolved.relation)
  }

  private def tableIdToTuple(tableIdentifier: Seq[String]): (String, Option[String]) = tableIdentifier match {
    case Seq(db, tableName) => (tableName, Some(db))
    case Seq(tableName) => (tableName, None)
  }

  // Defined by Crossdata

  final def persistTable(crossdataTable: CrossdataTable, table: Option[LogicalPlan] = None): Unit = {
    logInfo(s"XDCatalog: Persisting table ${crossdataTable.tableName}")
    persistTableMetadata(crossdataTable, table)
  }

  final def dropTable(tableIdentifier: Seq[String]): Unit = {
    logInfo(s"XDCatalog: Deleting table ${tableIdentifier.mkString(".")}from catalog")
    val (table, catalog) = tableIdToTuple(tableIdentifier)
    unregisterTable(tableIdentifier)
    dropPersistedTable(table, catalog)
  }

  final def dropAllTables(): Unit = {
    logInfo("XDCatalog: Drop all tables from catalog")
    unregisterAllTables()
    dropAllPersistedTables()
  }

  protected def lookupTable(tableName: String, databaseName: Option[String]): Option[CrossdataTable]

  def listPersistedTables(databaseName: Option[String]): Seq[(String, Boolean)]

  protected def persistTableMetadata(crossdataTable: CrossdataTable, table: Option[LogicalPlan] = None): Unit

  /**
   * Drop table if exists.
   */
  protected def dropPersistedTable(tableName: String, databaseName: Option[String]): Unit

  protected def dropAllPersistedTables(): Unit

}

object XDCatalog{

  def getUserSpecifiedSchema(schemaJSON: String): Option[StructType] = {
    implicit val formats = DefaultFormats

    val jsonMap = JSON.parseFull(schemaJSON).get.asInstanceOf[Map[String, Any]]
    val fields = jsonMap.getOrElse("fields", throw new Error("Fields not found")).asInstanceOf[List[Map[String, Any]]]
    val structFields = fields.map { x =>

      val typeStr = convertToGrammar(x.getOrElse("type", throw new Error("Type not found")))

      StructField(
        x.getOrElse("name", throw new Error("Name not found")).asInstanceOf[String],
        DataTypeParser.parse(typeStr),
        x.getOrElse("nullable", throw new Error("Nullable definition not found")).asInstanceOf[Boolean],
        Metadata.fromJson(write(x.getOrElse("metadata", throw new Error("Metadata not found")).asInstanceOf[Map[String, Any]]))
      )

    }
    structFields.headOption.map(_ => StructType(structFields))

  }


  def getPartitionColumn(partitionColumn: String): Array[String] =
    JSON.parseFull(partitionColumn).toList.flatMap(_.asInstanceOf[List[String]]).toArray

  def getOptions(optsJSON: String): Map[String, String] =
    JSON.parseFull(optsJSON).get.asInstanceOf[Map[String, String]]

  def serializeSchema(schema: StructType): String = {
    implicit val formats = DefaultFormats
    write(schema.jsonValue.values)
  }

  def serializeOptions(options: Map[String, Any]): String = {
    implicit val formats = DefaultFormats
    write(options)
  }

  def serializePartitionColumn(partitionColumn: Array[String]): String = {
    implicit val formats = DefaultFormats
    write(partitionColumn)
  }

  private def convertToGrammar (m: Any) : String = {
    def isStruct(map: Map[String, Any]) = map("type") == "struct"
    def isArray(map: Map[String, Any]) = map("type") == "array"
    def isMap(map: Map[String, Any]) = map("type") == "map"

    m match {
      case tpeMap: Map[String, Any] if isStruct(tpeMap) =>
        val fields =  tpeMap.get("fields").fold(throw new Error("Struct type not found"))(_.asInstanceOf[List[Map[String, Any]]])
        val fieldsStr = fields.map {
          x => s"`${x.getOrElse("name", throw new Error("Name not found"))}`:" + convertToGrammar(x)
        } mkString ","
        s"struct<$fieldsStr>"

      case tpeMap: Map[String, Any] if isArray(tpeMap) =>
        val tpeArray = tpeMap.getOrElse("elementType", throw new Error("Array type not found"))
        s"array<${convertToGrammar(tpeArray)}>"

      case tpeMap: Map[String, Any] if isMap(tpeMap) =>
        val tpeKey = tpeMap.getOrElse("keyType", throw new Error("Key type not found"))
        val tpeValue = tpeMap.getOrElse("valueType", throw new Error("Value type not found"))
        s"map<${convertToGrammar(tpeKey)},${convertToGrammar(tpeValue)}>"

      case tpeMap: Map[String, Any] =>
        convertToGrammar(tpeMap.get("type").getOrElse(throw new Error("Type not found")))

      case basicType: String => basicType

      case _ => throw new Error("Type is not correct")
    }

  }

}