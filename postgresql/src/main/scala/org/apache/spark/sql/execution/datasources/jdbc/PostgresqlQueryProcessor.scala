/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package org.apache.spark.sql.execution.datasources.jdbc

import java.sql.ResultSet
import java.util.Properties

import com.stratio.common.utils.components.logger.impl.SparkLoggerComponent
import org.apache.spark.sql.catalyst.expressions.{GenericInternalRowWithSchema, Literal}
import org.apache.spark.sql.catalyst.plans.logical.{Limit, LogicalPlan}
import org.apache.spark.sql.catalyst.util.{DateTimeUtils, GenericArrayData}
import org.apache.spark.sql.catalyst.{InternalRow, SQLBuilder}
import org.apache.spark.sql.types.{Decimal, _}
import org.apache.spark.unsafe.types.UTF8String

import scala.util.Try

object PostgresqlQueryProcessor {

  type PostgresQuery = String
  type ColumnName = String
  val DefaultLimit = 10000

  def apply(postgresRelation: PostgresqlXDRelation, logicalPlan: LogicalPlan, props: Properties, sqlText: String): PostgresqlQueryProcessor =
    new PostgresqlQueryProcessor(postgresRelation, logicalPlan, props, sqlText)

}


class PostgresqlQueryProcessor(postgresRelation: PostgresqlXDRelation,
                               logicalPlan: LogicalPlan,
                               props: Properties,
                               sqlText: String)
  extends SparkLoggerComponent {

  import PostgresqlQueryProcessor._

  def execute(): Option[Array[InternalRow]] = {

    def executeQuery(sql: String): Array[InternalRow] = {
      logInfo(s"QUERY: $sql")

      import scala.collection.JavaConversions._
      PostgresqlUtils.withClientDo(props.toMap) { (_, stm) =>
        val resultSet = stm.executeQuery(sql)
        sparkResultFromPostgresql(resultSet, logicalPlan.schema)
      }
    }

    val limit: Option[Int] = logicalPlan.collectFirst { case Limit(Literal(num: Int, _), _) => num }

    try {
      if (limit.exists(_ == 0)) Some(Array.empty[InternalRow])
      else {
        lazy val sqlWithLimit = s"$sqlText LIMIT ${limit.getOrElse(DefaultLimit)}"
        lazy val executeDirectQuery = Some(executeQuery(sqlWithLimit))
        new SQLBuilder(logicalPlan).toSQL.fold(executeDirectQuery){ sqlQuery =>
          Try(Some(executeQuery(sqlQuery))).getOrElse{executeDirectQuery}
        }
      }
    } catch {
      case exc: Exception => log.warn(s"Exception executing the native query $logicalPlan", exc); None
    }
  }
  //spark code
  private def getValue(idx: Int, rs: ResultSet, schema: StructType) : Any = {
    val metadata = schema.fields(idx).metadata
    val rsIdx= idx+1
    schema.fields(idx).dataType match {
      case StringType => UTF8String.fromString(rs.getString(rsIdx))
      case ByteType => rs.getByte(rsIdx)
      case BinaryType => rs.getBinaryStream(rsIdx)
      case ShortType => rs.getInt(rsIdx)
      case IntegerType => rs.getInt(rsIdx)
      case LongType if metadata.contains("binarylong") =>
        val bytes = rs.getBytes(rsIdx)
        var ans = 0L
        var j = 0
        while (j < bytes.size) {
          ans = 256 * ans + (255 & bytes(j))
          j = j + 1
        }
        ans
      case LongType => rs.getLong(rsIdx)
      case DoubleType => rs.getDouble(rsIdx)
      case decimal: DecimalType =>
        val p = decimal.precision
        val s = decimal.scale
        val decimalVal = rs.getBigDecimal(rsIdx)
        if (decimalVal == null) {
          null
        } else Decimal(decimalVal, p, s)

      case FloatType => rs.getFloat(rsIdx)
      case BooleanType => rs.getBoolean(rsIdx)
      case DateType =>  val dateVal = rs.getDate(rsIdx)
        if (dateVal != null) {
          DateTimeUtils.fromJavaDate(dateVal)
        } else null

      case TimestampType =>
        val t = rs.getTimestamp(rsIdx)
        if (t != null) {
          DateTimeUtils.fromJavaTimestamp(t)
        } else null

      case ArrayType(elementType, nullable) =>
        val array = rs.getArray(rsIdx).getArray
        if (array != null) {
          val data = elementType match {
            case TimestampType =>
              array.asInstanceOf[Array[java.sql.Timestamp]].map { timestamp =>
                nullSafeConvert(timestamp, DateTimeUtils.fromJavaTimestamp)
              }
            case StringType =>
              array.asInstanceOf[Array[java.lang.String]]
                .map(UTF8String.fromString)
            case DateType =>
              array.asInstanceOf[Array[java.sql.Date]].map { date =>
                nullSafeConvert(date, DateTimeUtils.fromJavaDate)
              }
            case decimal: DecimalType =>
              val p = decimal.precision
              val s = decimal.scale
              array.asInstanceOf[Array[java.math.BigDecimal]].map { decimal =>
                nullSafeConvert[java.math.BigDecimal](decimal, d => Decimal(d, p, s))
              }
            case LongType if metadata.contains("binarylong") =>
              throw new IllegalArgumentException(s"Unsupported array element conversion $rsIdx")
            case _: ArrayType =>
              throw new IllegalArgumentException("Nested arrays unsupported")
            case _ => array.asInstanceOf[Array[Any]]
          }
          new GenericArrayData(data)
        } else null

      case NullType => null
      case dataType => throw new IllegalArgumentException(s"Unsupported DataType $dataType")
    }
  }

  private def nullSafeConvert[T](input: T, f: T => Any): Any = {
    if (input == null) {
      null
    } else {
      f(input)
    }
  }

  private[this] def resultToRow(nCols: Int, rs: ResultSet, schema: StructType): InternalRow = {
    val values = new Array[Any](nCols)
    (0 until nCols).foreach( i => values(i) = getValue(i, rs, schema))

    new GenericInternalRowWithSchema(values, schema)
  }

  //to convert ResultSet to Array[Row]

  private[this] def sparkResultFromPostgresql(resultSet: ResultSet, schema: StructType): Array[InternalRow] = {
    val nCols = resultSet.getMetaData.getColumnCount

    new Iterator[InternalRow] {
      private var hasnext: Boolean = resultSet.next
      override def hasNext: Boolean = hasnext
      override def next(): InternalRow = {
        val rs = resultToRow(nCols, resultSet, schema)
        hasnext = resultSet.next
        rs
      }
    } toArray
  }

}