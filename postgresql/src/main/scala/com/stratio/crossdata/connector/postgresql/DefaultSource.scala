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

import java.sql.{Connection, ResultSet, Statement}
import java.util.Properties

import com.stratio.crossdata.connector.TableInventory.Table
import com.stratio.crossdata.connector.{TableInventory, TableManipulation}
import org.apache.spark.sql.execution.datasources.jdbc.PostgresqlUtils._
import org.apache.spark.sql.execution.datasources.jdbc.{JDBCPartitioningInfo, PostgresqlXDRelation, DefaultSource => JdbcDS}
import org.apache.spark.sql.sources.{BaseRelation, CreatableRelationProvider, SchemaRelationProvider}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SQLContext, SaveMode}
import org.postgresql.util.PSQLException

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

  override def generateConnectorOpts(item: Table, opts: Map[String, String] = Map.empty): Map[String, String] = Map(
      dbTable -> s"${item.database.get}.${item.tableName}"
    ) ++ opts

  override def dropExternalTable(context: SQLContext,
                                 options: Map[String, String]): Try[Unit] = {

    val table: String = options.getOrElse(dbTable,
      throw new RuntimeException(s"$dbTable property must be declared"))

    //schema and table have to be specified in dbtable parameter
    require(table.split("[.]").length == 2)
    val dropTableQueryString = s"DROP TABLE $table"

    Try {
      withClientDo(options){ (_, statement) =>
        statement.execute(dropTableQueryString)
      }
    }
  }

  override def listTables(context: SQLContext, options: Map[String, String]): Seq[Table] = {

    val optionURL = options.get(url)
    require(optionURL.nonEmpty)
    val URL = optionURL.get
    val properties = mapToPropertiesWithDriver(options)
    val postgresqlSchema =  options.get(schema)
    val tableQF = options.get(dbTable)
    try{
      withClientDo(options){ (client, statement) =>
        if(postgresqlSchema.isDefined){
          val rsTables = client.getMetaData.getTables(null, postgresqlSchema.get, "%", Array("TABLE"))
          val itTables = new Iterator[String]{
            def hasNext = rsTables.next()
            def next: String = rsTables.getString("TABLE_NAME")
          }
          itTables.map{ table =>
            val sparkSchema = resolveSchema(URL, s"${postgresqlSchema.get}.$table", properties)
            Table(table, postgresqlSchema, Some(sparkSchema))
          }.toSeq
        }
        else if(tableQF.isDefined) {
          val tableAndSchema = tableQF.get.split("[.]")
          require(tableAndSchema.length == 2)
          val sparkSchema = resolveSchema(URL, tableQF.get, properties)
          Seq(Table(tableAndSchema(1), Some(tableAndSchema(0)), Some(sparkSchema)))
        }
        else {
          val metadata = client.getMetaData
          val rsSchemas = metadata.getSchemas
          val schemas = new Iterator[String] {
            def hasNext = rsSchemas.next()
            def next() : String = rsSchemas.getString(1).trim
          }.toList
          schemas.flatMap{ schema =>
            val rsTables = metadata.getTables(null, schema, "%", Array("TABLE"))

            val itTables = new Iterator[String]{
              def hasNext = rsTables.next()
              def next: String = rsTables.getString("TABLE_NAME")
            }

            itTables.map{ table =>
              val sparkSchema = resolveSchema(URL, s"$schema.$table", properties)
              Table(table, Some(schema), Some(sparkSchema))
            }
          }
        }
      }

    } catch {
      case e: IllegalArgumentException => throw e
      case e: PSQLException => throw e //TODO
      case e: Exception =>
        sys.error(e.getMessage)
        Seq.empty
    }
  }

  def getSchemaAndTableName(tableQF: String): (String, String) = {
    val splitTable = tableQF.split("[.]")
    if(splitTable.length == 2) (splitTable(0), splitTable(1))
    else throw new Exception("dbtable should be specified in 'schema.table' format")
  }

//  def resultSetToIterator[T](rs: ResultSet, next :(ResultSet) => T) : Iterator[T] = new Iterator[T] {
//    def hasNext = rs.next()
//    def next() : T = next
//  }

  def createSchemaIfNotExists(conn: Connection, statement: Statement, postgresqlSchema: String) : Unit = {
    val rs = conn.getMetaData.getSchemas
//    val schemas = resultSetToIterator[String](resultSet, resultSet => resultSet.getString(1))
    val schemas = new Iterator[String] {
          def hasNext = rs.next()
          def next() : String = rs.getString(1)
    }

    if(!schemas.contains(postgresqlSchema.trim.toLowerCase()))
      statement.execute(s"CREATE SCHEMA $postgresqlSchema")
  }

  override def createExternalTable(context: SQLContext,
                                   tableName: String,
                                   postgresqlSchema: Option[String],
                                   schema: StructType,
                                   options: Map[String, String]): Option[Table] = {

    require(postgresqlSchema.nonEmpty)
    val dbSchema = postgresqlSchema.get
    val tableQF = s"$dbSchema.$tableName"

    val stringSchema = structTypeToStringSchema(schema)
    val pkFields = options.get(pkKey).map(fields => fields.split(",").mkString(","))
    val pkString = if(pkFields.nonEmpty) s", PRIMARY KEY(${pkFields.get})" else ""

    try {
      withClientDo(options){ (conn, statement) =>
        createSchemaIfNotExists(conn, statement, dbSchema)
        statement.execute(s"CREATE TABLE $tableQF ($stringSchema$pkString)")
      }

      Option(Table(tableName, postgresqlSchema, Option(schema)))
    } catch {
      case e: IllegalArgumentException => throw e
      case e: PSQLException => throw e // TODO What should we do when table already exists?
      case e: Exception =>
        sys.error(e.getMessage)
        None
    }
  }

}

object DefaultSource {

  val url = "url"
  val schema = "schema"
  val dbTable = "dbtable"
  //comma separed columns
  val pkKey = "primary_key"  //TODO Document this parameter

}

