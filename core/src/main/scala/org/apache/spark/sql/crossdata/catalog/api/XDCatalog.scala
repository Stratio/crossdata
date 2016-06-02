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
package org.apache.spark.sql.crossdata.catalog.api

import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.analysis.Catalog
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.catalyst.util.DataTypeParser
import org.apache.spark.sql.crossdata
import org.apache.spark.sql.crossdata.catalog.api.XDCatalog.ViewIdentifier
import org.apache.spark.sql.crossdata.serializers.CrossdataSerializer
import org.apache.spark.sql.types.{Metadata, StructField, StructType}
import org.json4s.jackson.Serialization._

import scala.util.parsing.json.JSON

object XDCatalog extends CrossdataSerializer {

implicit def asXDCatalog (catalog: Catalog): XDCatalog = catalog.asInstanceOf[XDCatalog]

  type ViewIdentifier = TableIdentifier

  case class CrossdataTable(tableName: String, dbName: Option[String], schema: Option[StructType],
                            datasource: String, partitionColumn: Array[String] = Array.empty,
                            opts: Map[String, String] = Map.empty, crossdataVersion: String = crossdata.CrossdataVersion)

  //TODO add merge serialization
  def getUserSpecifiedSchema(schemaJSON: String): Option[StructType] = {

    val jsonMap = JSON.parseFull(schemaJSON).get.asInstanceOf[Map[String, Any]]
    val fields = jsonMap.getOrElse("fields", throw new Error("Fields not found")).asInstanceOf[List[Map[String, Any]]]
    val structFields = fields.map {
      x =>

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

  private def convertToGrammar(m: Any): String = {
    def isStruct(map: Map[String, Any]) = map("type") == "struct"
    def isArray(map: Map[String, Any]) = map("type") == "array"
    def isMap(map: Map[String, Any]) = map("type") == "map"

    m match {
      case tpeMap: Map[String@unchecked, _] if isStruct(tpeMap) =>
        val fields = tpeMap.get("fields").fold(throw new Error("Struct type not found"))(_.asInstanceOf[List[Map[String, Any]]])
        val fieldsStr = fields.map {
          x => s"`${
            x.getOrElse("name", throw new Error("Name not found"))
          }`:" + convertToGrammar(x)
        } mkString ","
        s"struct<$fieldsStr>"

      case tpeMap: Map[String@unchecked, _] if isArray(tpeMap) =>
        val tpeArray = tpeMap.getOrElse("elementType", throw new Error("Array type not found"))
        s"array<${
          convertToGrammar(tpeArray)
        }>"

      case tpeMap: Map[String@unchecked, _] if isMap(tpeMap) =>
        val tpeKey = tpeMap.getOrElse("keyType", throw new Error("Key type not found"))
        val tpeValue = tpeMap.getOrElse("valueType", throw new Error("Value type not found"))
        s"map<${
          convertToGrammar(tpeKey)
        },${
          convertToGrammar(tpeValue)
        }>"

      case tpeMap: Map[String@unchecked, _] =>
        convertToGrammar(tpeMap.getOrElse("type", throw new Error("Type not found")))

      case basicType: String => basicType

      case _ => throw new Error("Type is not correct")
    }

  }

}

trait XDCatalog extends Catalog
with ExternalCatalogAPI
with StreamingCatalogAPI {

  def registerView(viewIdentifier: ViewIdentifier, logicalPlan: LogicalPlan): Unit

  def unregisterView(viewIdentifier: ViewIdentifier): Unit

  /**
   * Check the connection to the set Catalog
   */
  def checkConnectivity: Boolean

}



