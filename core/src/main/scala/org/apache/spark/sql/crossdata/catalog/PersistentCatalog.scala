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
package org.apache.spark.sql.crossdata.catalog

import org.apache.spark.sql.catalyst.analysis.UnresolvedRelation
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.catalyst.{CatalystConf, SimpleCatalystConf, TableIdentifier, util}
import org.apache.spark.sql.crossdata.execution.datasources.StreamingRelation
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.crossdata.catalog.XDCatalog.{CrossdataTable, ViewIdentifier}
import org.apache.spark.sql.crossdata.serializers.CrossdataSerializer
import org.apache.spark.sql.types.{Metadata, StructField, StructType}
import org.json4s.jackson.Serialization._

import scala.util.parsing.json.JSON


/**
 * PersistentCatalog aims to provide a mechanism to persist the
 * [[org.apache.spark.sql.catalyst.analysis.Catalog]] metadata.
 */
abstract class PersistentCatalog(val conf: CatalystConf = new SimpleCatalystConf(true),
                                 override val xdContext: XDContext) extends XDCatalogWithPersistence
  with Serializable {

  protected def lookupView(viewIdentifier: ViewIdentifier): Option[String]
  protected def lookupTable(tableIdentifier: TableIdentifier): Option[CrossdataTable]

  override def tableExists(tableIdentifier: TableIdentifier): Boolean = lookupTable(tableIdentifier).isDefined

  override def lookupRelation(relationIdentifier: TableIdentifier, alias: Option[String]): LogicalPlan = {

      logInfo(s"PersistentCatalog: Looking up table ${relationIdentifier.unquotedString}")

      lookupTable(relationIdentifier) match {
        case Some(crossdataTable) =>
          val table: LogicalPlan = createLogicalRelation(crossdataTable)
          processAlias(relationIdentifier, table, alias)

        case None =>
          log.debug(s"Table Not Found: ${relationIdentifier.unquotedString}")
          lookupView(relationIdentifier) match {
            case Some(sqlView) =>
              val viewPlan: LogicalPlan = xdContext.sql(sqlView).logicalPlan
              processAlias(relationIdentifier, viewPlan, alias)

            case None =>
              log.debug(s"View Not Found: ${relationIdentifier.unquotedString}")
              xdContext.streamingCatalog match {
                // TODO PoC => handle exceptions
                case Some(streamingCatalog) if streamingCatalog.existsEphemeralTable(getTableName(relationIdentifier)) =>
                  StreamingRelation(getTableName(relationIdentifier))
                case _ =>
                  sys.error(s"Table/View Not Found: ${relationIdentifier.unquotedString}")
              }
          }
      }

  }

  override def registerTable(tableIdentifier: TableIdentifier, plan: LogicalPlan): Unit = ()
  override def unregisterTable(tableIdentifier: TableIdentifier): Unit = ()

  override def registerView(viewIdentifier: ViewIdentifier, plan: LogicalPlan): Unit = ()
  override def unregisterView(viewIdentifier: ViewIdentifier): Unit = ()

  override def unregisterAllTables(): Unit = ()
  override def unregisterAllViews(): Unit = ()

  override def refreshTable(tableIdentifier: TableIdentifier): Unit = {
    throw new UnsupportedOperationException
  }

  final override def persistTable(crossdataTable: CrossdataTable, table: LogicalPlan): Unit = {
    val tableIdentifier = TableIdentifier(crossdataTable.tableName, crossdataTable.dbName)

    if (tableExists(tableIdentifier)){
      logWarning(s"The table $tableIdentifier already exists")
      throw new UnsupportedOperationException(s"The table $tableIdentifier already exists")
    } else {
      logInfo(s"Persisting table ${crossdataTable.tableName}")
      persistTableMetadata(crossdataTable.copy(schema = Option(table.schema)))
    }
  }

  final override def persistView(tableIdentifier: ViewIdentifier, plan: LogicalPlan, sqlText: String) = {
    def checkPlan(plan: LogicalPlan): Unit = {
      plan collect {
        case UnresolvedRelation(tIdent, _) => tIdent
      } foreach { tIdent =>
        if (!tableExists(tIdent)) {
          throw new RuntimeException("Views only can be created with a previously persisted table")
        }
      }
    }
    checkPlan(plan)
    if (tableExists(tableIdentifier)){
      val msg = s"The view ${tableIdentifier.unquotedString} already exists"
      logWarning(msg)
      throw new UnsupportedOperationException(msg)
    } else {
      logInfo(s"Persisting view ${tableIdentifier.unquotedString}")
      persistViewMetadata(tableIdentifier: TableIdentifier, sqlText)
    }
  }

}

object PersistentCatalog  extends CrossdataSerializer {
  def getUserSpecifiedSchema(schemaJSON: String): Option[StructType] = {

    val jsonMap = JSON.parseFull(schemaJSON).get.asInstanceOf[Map[String, Any]]
    val fields = jsonMap.getOrElse("fields", throw new Error("Fields not found")).asInstanceOf[List[Map[String, Any]]]
    val structFields = fields.map { x =>

      val typeStr = convertToGrammar(x.getOrElse("type", throw new Error("Type not found")))

      StructField(
        x.getOrElse("name", throw new Error("Name not found")).asInstanceOf[String],
        util.DataTypeParser.parse(typeStr),
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