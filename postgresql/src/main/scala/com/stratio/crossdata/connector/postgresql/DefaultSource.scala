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
import org.apache.spark.Partition
import org.apache.spark.sql.SaveMode.{Append, ErrorIfExists, Ignore, Overwrite}
import org.apache.spark.sql.execution.datasources.jdbc.PostgresqlUtils._
import org.apache.spark.sql.execution.datasources.jdbc.{PostgresqlRelationUtils, PostgresqlXDRelation, DefaultSource => JdbcDS}
import org.apache.spark.sql.sources.{BaseRelation, CreatableRelationProvider, SchemaRelationProvider}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SQLContext, SaveMode}
import org.postgresql.util.PSQLException

import scala.util.{Failure, Try}

class DefaultSource
  extends JdbcDS
    with SchemaRelationProvider
    with CreatableRelationProvider
    with TableInventory
    with TableManipulation
    with PostgresqlRelationUtils {

  import DefaultSource._

  override def shortName(): String = "postgresql"

  protected def getRelationParams(parameters: Map[String, String]): (String, String, Array[Partition], Properties) = {

    val properties = mapToPropertiesWithDriver(parameters)

    val url = getRequiredProperty(URL, parameters)
    val table = getRequiredProperty(dbTable, parameters)

    val partitionColumn = parameters.get("partitionColumn")
    val lowerBound = parameters.get("lowerBound")
    val upperBound = parameters.get("upperBound")
    val numPartitions = parameters.get("numPartitions")

    if(partitionColumn.nonEmpty && Seq(lowerBound, upperBound, numPartitions).exists(_.isEmpty))
      sys.error("Partitioning incompletely specified")

    val parts = columnPartition {
      partitionColumn map { pColum =>
        createJDBCPartitioningInfo(
          pColum,
          lowerBound.get.toLong,
          upperBound.get.toLong,
          numPartitions.get.toInt)
      } orNull
    }

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


  protected def tableExists(parameters: Map[String, String], tableQF: String): Boolean = {

    val schemaTable = tableQF.split("[.]")
    val Seq(schema, table) = 0 to 1 map schemaTable

    withClientDo(parameters){ (conn, _) =>
      val rs = conn.getMetaData.getTables(null, schema, table, Array("TABLE"))
      rs.next()
    }
  }

  override def createRelation(sqlContext: SQLContext,
                              mode: SaveMode,
                              parameters: Map[String, String],
                              data: DataFrame): BaseRelation = {

    val (url, table, parts, properties) = getRelationParams(parameters)

    val postgresqlRelation = new PostgresqlXDRelation(url, table, parts, properties, sqlContext, Some(data.schema))
    mode match {
      case Append => postgresqlRelation.insert(data, overwrite = false)
      case Overwrite => postgresqlRelation.insert(data, overwrite = true)
      case ErrorIfExists => if (tableExists(parameters, table)) postgresqlRelation.insert(data, overwrite = false)
      else throw new UnsupportedOperationException(s"SaveMode is set to ErrorIfExists and $table already exists")
      case Ignore => if (tableExists(parameters, table)) postgresqlRelation.insert(data, overwrite = false)

    }
    postgresqlRelation
  }

  override def generateConnectorOpts(item: Table, opts: Map[String, String] = Map.empty): Map[String, String] = Map(
    dbTable -> s"${item.database.get}.${item.tableName}"
  ) ++ opts

  override def dropExternalTable(context: SQLContext,
                                 options: Map[String, String]): Try[Unit] = {
    options.get(dbTable) map { table =>
      Try {
        //schema and table have to be specified in dbtable parameter
        require(table.split("[.]").length == 2)

        val dropTableQueryString = s"DROP TABLE $table";

        withClientDo[Unit](options) { (_, statement) =>
          statement.execute(dropTableQueryString)
        }
      }
    } getOrElse Failure(new RuntimeException(s"$dbTable property must be declared"))
  }

  protected def resultSetToIterator(rs: ResultSet, functionNext: ResultSet => String) : Iterator[String] = new Iterator[String] {
    def hasNext: Boolean = rs.next()
    def next(): String = functionNext(rs)
  }

  override def listTables(context: SQLContext, options: Map[String, String]): Seq[Table] = {

    val urlChain = options(urlConnectionChainKey)
    val properties = mapToPropertiesWithDriver(options)
    val postgresqlSchema =  options.get(schema)
    val tableQF = options.get(dbTable)
    withClientDo(options){ (client, _) =>
      if(postgresqlSchema.isDefined){
        val rsTables = client.getMetaData.getTables(null, postgresqlSchema.get, "%", Array("TABLE"))
        val itTables = resultSetToIterator(rsTables, rsTables => rsTables.getString("TABLE_NAME"))

        itTables.map{ table =>
          val sparkSchema = resolveSchema(urlChain, s"${postgresqlSchema.get}.$table", properties)
          Table(table, postgresqlSchema, Some(sparkSchema))
        }.toSeq
      }
      else if(tableQF.isDefined) {
        val tableAndSchema = tableQF.get.split("[.]")
        require(tableAndSchema.length == 2)
        val sparkSchema = resolveSchema(urlChain, tableQF.get, properties)
        Seq(Table(tableAndSchema(1), Some(tableAndSchema(0)), Some(sparkSchema)))
      }
      else {
        val metadata = client.getMetaData
        val rsSchemas = metadata.getSchemas
        val itSchemas = resultSetToIterator(rsSchemas, rsSchemas => rsSchemas.getString(1).trim).toList

        itSchemas.flatMap{ schema =>
          val rsTables = metadata.getTables(null, schema, "%", Array("TABLE"))
          val itTables = resultSetToIterator(rsTables, rsTables => rsTables.getString("TABLE_NAME"))

          itTables.map{ table =>
            val sparkSchema = resolveSchema(urlChain, s"$schema.$table", properties)
            Table(table, Some(schema), Some(sparkSchema))
          }
        }
      }
    }
  }

  private def createSchemaIfNotExists(conn: Connection, statement: Statement, postgresqlSchema: String) : Unit = {
    val rs = conn.getMetaData.getSchemas
    val schemas = resultSetToIterator(rs, rs => rs.getString(1))

    if(!schemas.contains(postgresqlSchema.trim.toLowerCase()))
      statement.execute(s"CREATE SCHEMA $postgresqlSchema")
  }

  override def createExternalTable(context: SQLContext,
                                   tableName: String,
                                   postgresqlSchema: Option[String],
                                   schema: StructType,
                                   options: Map[String, String]): Option[Table] = {

    require(postgresqlSchema.nonEmpty, "Postgresql Schema can't be empty ")
    val dbSchema = postgresqlSchema.get
    val tableQF = s"$dbSchema.$tableName"

    val stringSchema = structTypeToStringSchema(schema)
    val pkFields = options.get(pkKey)
    val pkString = if(pkFields.nonEmpty) s", PRIMARY KEY(${pkFields.get})" else ""

    try {
      withClientDo(options){ (conn, statement) =>
        createSchemaIfNotExists(conn, statement, dbSchema)
        statement.execute(s"CREATE TABLE $tableQF ($stringSchema$pkString)")
      }

      Option(Table(tableName, postgresqlSchema, Option(schema)))
    } catch {
      case psqlException: PSQLException => throw new RuntimeException("Error creating external table.", psqlException)
      case e: Exception =>
        sys.error(e.getMessage)
        None
    }
  }
}

object DefaultSource {

  val urlConnectionChainKey = "url"
  val schema = "schema"
  val dbTable = "dbtable"
  //comma separed columns
  val pkKey = "primary_key"

}