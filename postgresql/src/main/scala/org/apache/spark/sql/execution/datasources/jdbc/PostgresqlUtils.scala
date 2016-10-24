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

import org.apache.spark.Partition
import org.apache.spark.sql.types._

import scala.collection.mutable.ArrayBuffer

object PostgresqlUtils {

  val URL: String = "url"
  val DRIVER: String = "driver"
  val driverClassName: String = "org.postgresql.Driver"

  def withClientDo[T](parameters: Map[String, String])(f: (Connection, Statement) => T): T = {
    val connection = buildConnection(parameters)
    val statement = connection.createStatement()
    try {
      f(connection, statement)
    } finally {
      connection.close()
      statement.close()
    }
  }

  /**
    * Maps a JDBC type to a Catalyst type.
    *
    * @param sqlType - A field of java.sql.Types
    * @return The Catalyst type corresponding to sqlType.
    */
  def getCatalystType( sqlType: Int,
                       precision: Int,
                       scale: Int,
                       signed: Boolean): DataType = {
    val answer = sqlType match {
      // scalastyle:off
      case java.sql.Types.ARRAY         => null
      case java.sql.Types.BIGINT        => if (signed) { LongType } else { DecimalType(20,0) }
      case java.sql.Types.BINARY        => BinaryType
      case java.sql.Types.BIT           => BooleanType // @see JdbcDialect for quirks
      case java.sql.Types.BLOB          => BinaryType
      case java.sql.Types.BOOLEAN       => BooleanType
      case java.sql.Types.CHAR          => StringType
      case java.sql.Types.CLOB          => StringType
      case java.sql.Types.DATALINK      => null
      case java.sql.Types.DATE          => DateType
      case java.sql.Types.DECIMAL
        if precision != 0 || scale != 0 => DecimalType.bounded(precision, scale)
      case java.sql.Types.DECIMAL       => DecimalType.SYSTEM_DEFAULT
      case java.sql.Types.DISTINCT      => null
      case java.sql.Types.DOUBLE        => DoubleType
      case java.sql.Types.FLOAT         => FloatType
      case java.sql.Types.INTEGER       => if (signed) { IntegerType } else { LongType }
      case java.sql.Types.JAVA_OBJECT   => null
      case java.sql.Types.LONGNVARCHAR  => StringType
      case java.sql.Types.LONGVARBINARY => BinaryType
      case java.sql.Types.LONGVARCHAR   => StringType
      case java.sql.Types.NCHAR         => StringType
      case java.sql.Types.NCLOB         => StringType
      case java.sql.Types.NULL          => null
      case java.sql.Types.NUMERIC
        if precision != 0 || scale != 0 => DecimalType.bounded(precision, scale)
      case java.sql.Types.NUMERIC       => DecimalType.SYSTEM_DEFAULT
      case java.sql.Types.NVARCHAR      => StringType
      case java.sql.Types.OTHER         => null
      case java.sql.Types.REAL          => DoubleType
      case java.sql.Types.REF           => StringType
      case java.sql.Types.ROWID         => LongType
      case java.sql.Types.SMALLINT      => IntegerType
      case java.sql.Types.SQLXML        => StringType
      case java.sql.Types.STRUCT        => StringType
      case java.sql.Types.TIME          => TimestampType
      case java.sql.Types.TIMESTAMP     => TimestampType
      case java.sql.Types.TINYINT       => IntegerType
      case java.sql.Types.VARBINARY     => BinaryType
      case java.sql.Types.VARCHAR       => StringType
      case _                            => null
      // scalastyle:on
    }

    if (answer == null) throw new SQLException("Unsupported type " + sqlType)
    answer
  }

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
    case ByteType => throw new IllegalArgumentException(s"Unsupported type in postgresql: $dataType")//TODO delete this
    case _ => throw new IllegalArgumentException(s"Unsupported type in postgresql: $dataType")
  }

}

case class JDBCPartitioningInfo(
                                 column: String,
                                 lowerBound: Long,
                                 upperBound: Long,
                                 numPartitions: Int)
