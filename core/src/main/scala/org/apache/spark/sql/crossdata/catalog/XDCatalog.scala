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

import org.apache.spark.sql.catalyst.analysis.{UnresolvedRelation, Catalog}
import org.apache.spark.sql.catalyst.plans.logical.{Project, LogicalPlan, Subquery}
import org.apache.spark.sql.catalyst.{CatalystConf, SimpleCatalystConf, TableIdentifier}
import org.apache.spark.sql.crossdata.catalog.XDCatalog.CrossdataTable
import org.apache.spark.sql.crossdata.execution.datasources.StreamingRelation
import org.apache.spark.sql.crossdata.serializers.CrossdataSerializer
import org.apache.spark.sql.crossdata.{CrossdataVersion, XDContext}
import org.apache.spark.sql.execution.datasources.{LogicalRelation, ResolvedDataSource}
import org.apache.spark.sql.types._
import org.json4s.jackson.Serialization.write

import scala.collection.mutable
import scala.util.parsing.json.JSON

/**
 * CrossdataCatalog aims to provide a mechanism to persist the
 * [[org.apache.spark.sql.catalyst.analysis.Catalog]] metadata.
 */
abstract class XDCatalog(val conf: CatalystConf = new SimpleCatalystConf(true),
                         xdContext: XDContext) extends Catalog with CatalogCommon with Serializable {

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

  def tableExistsInCatalog(tableIdentifier: Seq[String]): Boolean = (lookupTable _ tupled tableIdToTuple(tableIdentifier)).fold(false)(crossdataTable=>true)

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
          log.debug(s"Table Not Found: ${tableIdent.mkString(".")}")
          lookupView(table, database) match {
            case Some(sqlView) =>
              val viewPlan: LogicalPlan = xdContext.sql(sqlView).logicalPlan
              registerView(tableIdent, viewPlan)
              processAlias(tableIdent, viewPlan, alias)

            case None =>
              log.debug(s"View Not Found: ${tableIdent.mkString(".")}")
              xdContext.streamingCatalog match {
                // TODO PoC => handle exceptions
                // TODO We should replace tableIdent.mkString with TableIdentifier
                case Some(streamingCatalog) if streamingCatalog.existsEphemeralTable(tableIdent.mkString(".")) =>
                  StreamingRelation(tableIdent.mkString("."))
                case _ =>
                  sys.error(s"Table/View Not Found: ${tableIdent.mkString(".")}")
              }
          }
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

  protected[crossdata] def createLogicalRelation(crossdataTable: CrossdataTable): LogicalRelation = {
    val resolved = ResolvedDataSource(xdContext, crossdataTable.schema, crossdataTable.partitionColumn, crossdataTable.datasource, crossdataTable.opts)
    LogicalRelation(resolved.relation)
  }

  private def tableIdToTuple(tableIdentifier: Seq[String]): (String, Option[String]) = tableIdentifier match {
    case Seq(db, tableName) => (tableName, Some(db))
    case Seq(tableName) => (tableName, None)
  }

  // Defined by Crossdata

  final def persistTable(crossdataTable: CrossdataTable, table: LogicalPlan): Unit = {
    val tableIdentifier = TableIdentifier(crossdataTable.tableName, crossdataTable.dbName).toSeq

    if (tableExists(tableIdentifier)){
      logWarning(s"The table $tableIdentifier already exists")
      throw new UnsupportedOperationException(s"The table $tableIdentifier already exists")
    } else {
      logInfo(s"XDCatalog: Persisting table ${crossdataTable.tableName}")
      persistTableMetadata(crossdataTable.copy(schema = Option(table.schema)))
      val tableIdentifier = TableIdentifier(crossdataTable.tableName,crossdataTable.dbName).toSeq
      registerTable(tableIdentifier, table)
    }
  }



  final def persistView(tableIdentifier: TableIdentifier, plan: LogicalPlan, sqlText: String) = {
    def checkPlan(plan:LogicalPlan) {
      plan collect {
        case rel: UnresolvedRelation =>
          if (!tableExistsInCatalog(rel.tableIdentifier)) throw new RuntimeException("Views only can be created with a previously persisted table")
        case project:Project =>
          checkPlan(project.child)
        case _=>throw new RuntimeException("Views only can be created with a previously persisted table")
      }
    }
    val tableIdent = tableIdentifier.toSeq
    checkPlan(plan)
    if (tableExists(tableIdent)){
      logWarning(s"The view ${tableIdent mkString "."} already exists")
      throw new UnsupportedOperationException(s"The view $tableIdentifier already exists")
    } else {
      logInfo(s"XDCatalog: Persisting view ${tableIdent mkString "."}")
      persistViewMetadata(tableIdentifier: TableIdentifier, sqlText)
      registerView(tableIdentifier.toSeq, plan)
    }
  }

  final def dropTable(tableIdentifier: Seq[String]): Unit = {
    if (!tableExists(tableIdentifier)) throw new RuntimeException("Table can't be deleted because it doesn't exists")
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

  final def dropView(viewIdentifier: Seq[String]): Unit = {
    if (!tableExists(viewIdentifier)) throw new RuntimeException("View can't be deleted because it doesn't exists")
    logInfo(s"XDCatalog: Deleting table ${viewIdentifier.mkString(".")}from catalog")
    val (view, catalog) = tableIdToTuple(viewIdentifier)
    unregisterTable(viewIdentifier)
    dropPersistedView(view, catalog)
  }

  final def dropAllViews(): Unit = {
    logInfo("XDCatalog: Drop all tables from catalog")
    dropAllPersistedViews()
  }


  def registerView(tableIdentifier: Seq[String], plan: LogicalPlan) =
    registerTable(tableIdentifier, plan)

  protected def lookupView(tableName: String, databaseName: Option[String]): Option[String]

  protected def lookupTable(tableName: String, databaseName: Option[String]): Option[CrossdataTable]

  def listPersistedTables(databaseName: Option[String]): Seq[(String, Boolean)]

  // TODO protected catalog

  protected[crossdata] def persistTableMetadata(crossdataTable: CrossdataTable): Unit

  protected[crossdata] def persistViewMetadata(tableIdentifier: TableIdentifier, sqlText: String): Unit

  /**
   * Drop table(s)/view(s) if exists.
   */
  protected def dropPersistedTable(tableName: String, databaseName: Option[String]): Unit

  protected def dropAllPersistedTables(): Unit

  protected def dropPersistedView(viewName:String, databaseName: Option[String]): Unit

  protected def dropAllPersistedViews(): Unit

}

object XDCatalog extends CrossdataSerializer {

  implicit def asXDCatalog(catalog: Catalog): XDCatalog = catalog.asInstanceOf[XDCatalog]


  case class CrossdataTable(tableName: String, dbName: Option[String],  schema: Option[StructType],
                            datasource: String, partitionColumn: Array[String] = Array.empty,
                            opts: Map[String, String] = Map.empty , crossdataVersion: String = CrossdataVersion)

  def getUserSpecifiedSchema(schemaJSON: String): Option[StructType] = {

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
    write(schema.jsonValue.values)
  }

  def serializeOptions(options: Map[String, Any]): String = {
    write(options)
  }

  def serializePartitionColumn(partitionColumn: Array[String]): String = {
    write(partitionColumn)
  }

  private def convertToGrammar (m: Any) : String = {
    def isStruct(map: Map[String, Any]) = map("type") == "struct"
    def isArray(map: Map[String, Any]) = map("type") == "array"
    def isMap(map: Map[String, Any]) = map("type") == "map"

    m match {
      case tpeMap: Map[String @unchecked, _] if isStruct(tpeMap) =>
        val fields =  tpeMap.get("fields").fold(throw new Error("Struct type not found"))(_.asInstanceOf[List[Map[String, Any]]])
        val fieldsStr = fields.map {
          x => s"`${x.getOrElse("name", throw new Error("Name not found"))}`:" + convertToGrammar(x)
        } mkString ","
        s"struct<$fieldsStr>"

      case tpeMap: Map[String @unchecked, _] if isArray(tpeMap) =>
        val tpeArray = tpeMap.getOrElse("elementType", throw new Error("Array type not found"))
        s"array<${convertToGrammar(tpeArray)}>"

      case tpeMap: Map[String @unchecked, _] if isMap(tpeMap) =>
        val tpeKey = tpeMap.getOrElse("keyType", throw new Error("Key type not found"))
        val tpeValue = tpeMap.getOrElse("valueType", throw new Error("Value type not found"))
        s"map<${convertToGrammar(tpeKey)},${convertToGrammar(tpeValue)}>"

      case tpeMap: Map[String @unchecked, _] =>
        convertToGrammar(tpeMap.getOrElse("type", throw new Error("Type not found")))

      case basicType: String => basicType

      case _ => throw new Error("Type is not correct")
    }

  }

}