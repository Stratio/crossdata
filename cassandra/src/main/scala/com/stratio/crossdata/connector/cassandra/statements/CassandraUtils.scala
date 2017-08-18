package com.stratio.crossdata.connector.cassandra.statements

import com.datastax.spark.connector.types._
import org.apache.spark.sql.types.ByteType
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.types.ArrayType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.BinaryType
import org.apache.spark.sql.types.ShortType
import org.apache.spark.sql.types.LongType
import org.apache.spark.sql.types.{BooleanType => SparkSqlBooleanType}
import org.apache.spark.sql.types.{DataType => SparkSqlDataType}
import org.apache.spark.sql.types.{DecimalType => SparkSqlDecimalType}
import org.apache.spark.sql.types.{DoubleType => SparkSqlDoubleType}
import org.apache.spark.sql.types.{FloatType => SparkSqlFloatType}
import org.apache.spark.sql.types.{MapType => SparkSqlMapType}
import org.apache.spark.sql.types.{TimestampType => SparkSqlTimestampType}

object CassandraUtils {


  /** Returns natural Cassandra type for representing data of the given Spark SQL type */
  def fromSparkSqlType(dataType: SparkSqlDataType): ColumnType[_] = {

    def unsupportedType() = throw new IllegalArgumentException(s"Unsupported type: $dataType")

    dataType match {
      case ByteType => IntType
      case ShortType => IntType
      case IntegerType => IntType
      case LongType => BigIntType
      case SparkSqlFloatType => FloatType
      case SparkSqlDoubleType => DoubleType
      case StringType => VarCharType
      case BinaryType => BlobType
      case SparkSqlBooleanType => BooleanType
      case SparkSqlTimestampType => TimestampType
      //case SparkSqlDateType => DateType TODO support this type
      case SparkSqlDecimalType() => DecimalType
      case ArrayType(sparkSqlElementType, containsNull) =>
        val argType = fromSparkSqlType(sparkSqlElementType)
        ListType(argType)
      case SparkSqlMapType(sparkSqlKeyType, sparkSqlValueType, containsNull) =>
        val keyType = fromSparkSqlType(sparkSqlKeyType)
        val valueType = fromSparkSqlType(sparkSqlValueType)
        MapType(keyType, valueType)
      case _ =>
        unsupportedType()
    }
  }
}
