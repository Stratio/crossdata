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

package org.apache.spark.sql.execution.datasources.jdbc

import java.sql.{Connection, SQLException, Statement}
import java.util.Properties

import com.stratio.crossdata.util.using
import org.apache.spark.Partition
import org.apache.spark.sql.types._

import scala.collection.mutable.ArrayBuffer

object PostgresqlUtils {

  val URL: String = "url"
  val DRIVER: String = "driver"
  val driverClassName: String = "org.postgresql.Driver"

  def withClientDo[T](parameters: Map[String, String])(f: (Connection, Statement) => T): T = {
//  TODO use using inside withclientDo
//    using (buildConnection(parameters)) { connection =>
//      using(connection.createStatement()){ statement =>
//         f(connection, statement)
//      }
//    }
//
    val connection = buildConnection(parameters)
    val statement = connection.createStatement()
    try {
      f(connection, statement)
    } finally {
      connection.close()
      statement.close()
    }
  }

  def resolveSchema(url: String, table: String, properties: Properties): StructType =
    JDBCRDD.resolveTable(url, table, properties)


  private def buildConnection(parameters: Map[String, String]): Connection = {

    val url: String = parameters.getOrElse(URL, sys.error(s"Option $URL not specified"))
    val properties = mapToPropertiesWithDriver(parameters)

    JdbcUtils.createConnectionFactory(url, properties)()
  }

  def getRequiredProperty(propertyName: String,  parameters: Map[String, String]): String =
    parameters.getOrElse(propertyName, sys.error(s"Option $propertyName not specified"))

  def mapToPropertiesWithDriver(parameters: Map[String, String]): Properties = {
    val properties = new Properties()
    parameters.foreach(kv => properties.setProperty(kv._1, kv._2))
    properties.setProperty(DRIVER, driverClassName)
    properties
  }

  def structTypeToStringSchema(schema: StructType): String = {

    val sb = new StringBuilder()
    schema.fields.foreach{ field =>
      val name = field.name
      val postgresqlType = getPostgresqlType(field.dataType)
      val nullable = if (field.nullable) "" else "NOT NULL"
      sb.append(s", $name $postgresqlType $nullable")
    }
    if (sb.length < 2) "" else sb.substring(2)
  }

  /**
    * Given a partitioning schematic (a column of integral type, a number of
    * partitions, and upper and lower bounds on the column's value), generate
    * WHERE clauses for each partition so that each row in the table appears
    * exactly once.  The parameters minValue and maxValue are advisory in that
    * incorrect values may cause the partitioning to be poor, but no data
    * will fail to be represented.
    */
  //spark code from JDBCRelation
  def columnPartition(partitioning: JDBCPartitioningInfo): Array[Partition] = {
    if (partitioning == null) return Array[Partition](JDBCPartition(null, 0))

    val numPartitions = partitioning.numPartitions
    val column = partitioning.column
    if (numPartitions == 1) return Array[Partition](JDBCPartition(null, 0))
    // Overflow and silliness can happen if you subtract then divide.
    // Here we get a little roundoff, but that's (hopefully) OK.
    val stride: Long = (partitioning.upperBound / numPartitions
      - partitioning.lowerBound / numPartitions)
    var i: Int = 0
    var currentValue: Long = partitioning.lowerBound
    var ans = new ArrayBuffer[Partition]()
    while (i < numPartitions) {
      val lowerBound = if (i != 0) s"$column >= $currentValue" else null
      currentValue += stride
      val upperBound = if (i != numPartitions - 1) s"$column < $currentValue" else null
      val whereClause =
        if (upperBound == null) {
          lowerBound
        } else if (lowerBound == null) {
          upperBound
        } else {
          s"$lowerBound AND $upperBound"
        }
      ans += JDBCPartition(whereClause, i)
      i = i + 1
    }
    ans.toArray
  }

  private def getPostgresqlType(dataType: DataType): String = dataType match {
    case StringType => "TEXT"
    case BinaryType => "BYTEA"
    case BooleanType => "BOOLEAN"
    case FloatType => "FLOAT4"
    case DoubleType => "FLOAT8"
    case ArrayType(elementType, _) => s"${getPostgresqlType(elementType)}[]"
    case IntegerType => "INTEGER"
    case LongType => "BIGINT"
    case ShortType => "SMALLINT"
    case TimestampType => "TIMESTAMP"
    case DateType => "DATE"
    case decimal: DecimalType => s"DECIMAL(${decimal.precision},${decimal.scale})"
    case _ => throw new IllegalArgumentException(s"Unsupported type in postgresql: $dataType")
  }

}

case class JDBCPartitioningInfo(
                                 column: String,
                                 lowerBound: Long,
                                 upperBound: Long,
                                 numPartitions: Int)
