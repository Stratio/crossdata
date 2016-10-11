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
package com.stratio.crossdata.connector.postgresql

import com.stratio.crossdata.connector.TableInventory.Table
import com.stratio.crossdata.connector.{TableInventory, TableManipulation}
import org.apache.spark.sql.{DataFrame, SQLContext, SaveMode}
import org.apache.spark.sql.execution.datasources.jdbc.{JDBCPartitioningInfo, PostgresqlXDRelation, DefaultSource => JdbcDS}
import org.apache.spark.sql.sources.{BaseRelation, CreatableRelationProvider, SchemaRelationProvider}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.execution.datasources.jdbc.PostgresqlUtils._

import scala.util.Try

class DefaultSource
  extends JdbcDS
    with SchemaRelationProvider
    with CreatableRelationProvider
    with TableInventory
    with TableManipulation {

  import DefaultSource._

  override def shortName(): String = "postgresql"

  def getRelationParams(parameters: Map[String, String]) = {

    val properties = mapToPropertiesWithDriver(parameters)

    val url = getRequiredProperty(URL, parameters)
    val table = getRequiredProperty(dbTable, parameters)

    val partitionColumn = parameters.getOrElse("partitionColumn", null)
    val lowerBound = parameters.getOrElse("lowerBound", null)
    val upperBound = parameters.getOrElse("upperBound", null)
    val numPartitions = parameters.getOrElse("numPartitions", null)

    if (partitionColumn != null
      && (lowerBound == null || upperBound == null || numPartitions == null)) {
      sys.error("Partitioning incompletely specified")
    }

    val partitionInfo = if (partitionColumn == null) {
      null
    } else {
      JDBCPartitioningInfo(
        partitionColumn,
        lowerBound.toLong,
        upperBound.toLong,
        numPartitions.toInt)
    }
    val parts = columnPartition(partitionInfo)

    (url, table, parts, properties)

  }

  override def createRelation(sqlContext: SQLContext, parameters: Map[String, String]): BaseRelation = {
    val (url, table, parts, properties) = getRelationParams(parameters)
    new PostgresqlXDRelation(url, table, parts, properties, sqlContext, None)
  }

  override def createRelation(sqlContext: SQLContext,
                              parameters: Map[String, String],
                              schema: StructType): BaseRelation = {
    val (url, table, parts, properties) = getRelationParams(parameters)
    new PostgresqlXDRelation(url, table, parts, properties, sqlContext, Some(schema))
  }

  override def createRelation(sqlContext: SQLContext,
                              mode: SaveMode,
                              parameters: Map[String, String],
                              data: DataFrame): BaseRelation = ???

  override def generateConnectorOpts(item: Table, opts: Map[String, String] = Map.empty): Map[String, String] = ???

  override def dropExternalTable(context: SQLContext,
                                 options: Map[String, String]): Try[Unit] = {


    val table: String = options.getOrElse(dbTable,
      throw new RuntimeException(s"$dbTable property must be declared"))

    Try {
      withClientDo(options){ (client, statement) =>
        statement.executeUpdate(s"DROP TABLE $table")
      }
    }
  }

  override def listTables(context: SQLContext, options: Map[String, String]): Seq[Table] = {

    val tableName: String = options.getOrElse(dbTable,
      throw new RuntimeException(s"$dbTable property must be declared"))

    try{
      withClientDo(options){ (client, statement) =>
        //TODO Test get schemas and tables from a database
        val schemas = client.getMetaData.getSchemas
        client.getMetaData.getCatalogs
      }

      val (table, dbSchema) = getSchemaAndTableName(tableName)
      Seq(Table(table, Option(dbSchema), None))
    } catch {
      case e: IllegalArgumentException =>
        throw e
      case e: Exception =>
        sys.error(e.getMessage)
        Seq.empty
    }

  }

  def getSchemaAndTableName(tableQF: String): (String, String) = {
    val splitTable = tableQF.split(".")
    if(splitTable.length == 2) (splitTable(0), splitTable(1))
    else throw new Exception("Table should be specified in 'schema.table' format")
  }

  override def createExternalTable(context: SQLContext,
                                   tableName: String,
                                   postgresqlSchema: Option[String],
                                   schema: StructType,
                                   options: Map[String, String]): Option[Table] = {

    val stringSchema = structTypeToStringSchema(schema)

    try{
      withClientDo(options){ (client, statement) =>
        statement.execute(s"CREATE TABLE $tableName ($stringSchema)")
      }

      val (table, dbSchema) = getSchemaAndTableName(tableName)
      Option(Table(table, Option(dbSchema), Option(schema)))
    } catch {
      case e: IllegalArgumentException =>
        throw e
      case e: Exception =>
        sys.error(e.getMessage)
        None
    }
  }

}

object DefaultSource {

  val dbTable = "dbtable"
}

