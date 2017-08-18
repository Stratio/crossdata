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