package com.stratio.crossdata.connector.cassandra.statements

import com.datastax.spark.connector.types._
import com.stratio.crossdata.connector.cassandra.DefaultSource.CassandraDataSourcePrimaryKeyNameProperty
import org.apache.spark.sql.types.ByteType
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.ArrayType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.BinaryType
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.ShortType
import org.apache.spark.sql.types.LongType
import org.apache.spark.sql.types.{BooleanType => SparkSqlBooleanType}
import org.apache.spark.sql.types.{DataType => SparkSqlDataType}
import org.apache.spark.sql.types.{DecimalType => SparkSqlDecimalType}
import org.apache.spark.sql.types.{DoubleType => SparkSqlDoubleType}
import org.apache.spark.sql.types.{FloatType => SparkSqlFloatType}
import org.apache.spark.sql.types.{MapType => SparkSqlMapType}
import org.apache.spark.sql.types.{TimestampType => SparkSqlTimestampType}

object CreateTableStatement {

  val PRIMARY_SINGLE = 1
  val PRIMARY_COMPOSED = 2
  val PRIMARY_AND_CLUSTERING_SPECIFIED = 3

}

case class CreateTableStatement(tableName: String,
                                schema: StructType,
                                options: Map[String, String]
                               ) {

  override def toString(): String = {
    val cqlCommand = StringBuilder.newBuilder
    cqlCommand.append("CREATE TABLE ")

    if (ifNotExists) {
      cqlCommand.append("IF NOT EXISTS ")
    }

    cqlCommand.append(keyspace).append(".").append(tableName)

    cqlCommand.append(" (")
    schema.fields.foreach { field: StructField =>

      val cassandraDataType = fromSparkSqlType(field.dataType)
      cqlCommand.append(field.name).append(" ").append(cassandraDataType.cqlTypeName).append(", ")
    }
    cqlCommand.append(getPKString).append(")")


    cqlCommand.toString()
  }

  /**
    * The type of primary key. Accepted values are:
    * <ul>
    * <li>1: If the primary key contains a single column.</li>
    * <li>2: If the primary key is composed of several columns but it does not contain a clustering
    * key.</li>
    * <li>3: If both the primary key and clustering key are specified.</li>
    * </ul>
    */
  lazy val primaryKeyType: Int = {
    options.getOrElse("primaryKeyType", "1").toInt
  }

  /**
    * Obtain the composite primary key.
    * @return a string with the primary key.
    */
  lazy val getPKString:String = {
    import CreateTableStatement._

    val primaryKeyString = StringBuilder.newBuilder
    primaryKeyString.append("PRIMARY KEY (")

    var colon = ""
    primaryKeyType match {
      case PRIMARY_COMPOSED | PRIMARY_SINGLE =>{
        primaryKeys.foreach{ primkey =>
          primaryKeyString.append(colon).append(primkey)
          colon = ","
        }
        primaryKeyString.append(")")
      }
      case PRIMARY_AND_CLUSTERING_SPECIFIED => {
        //TODO
        ???
      }
    }


    primaryKeyString.toString()
  }

  lazy val ifNotExists: Boolean = {
    options.contains("ifNotExist")
  }

  lazy val keyspace: String = {
    require(options.contains(CassandraDataSourcePrimaryKeyNameProperty), s"$CassandraDataSourcePrimaryKeyNameProperty required when use CREATE EXTERNAL TABLE command")
    options.get("keyspace").get
  }

  lazy val primaryKeys:Seq[String] = {
    require(options.contains(CassandraDataSourcePrimaryKeyNameProperty), s"$CassandraDataSourcePrimaryKeyNameProperty required when use CREATE EXTERNAL TABLE command")
    options.get(CassandraDataSourcePrimaryKeyNameProperty).get.split(",")
  }


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
      //case SparkSqlDateType => DateType
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
