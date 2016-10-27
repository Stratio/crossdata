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

import java.sql.{Date, ResultSet, ResultSetMetaData, Timestamp}
import java.util.Properties

import com.stratio.common.utils.components.logger.impl.SparkLoggerComponent
import org.apache.spark.sql.catalyst.expressions.aggregate.Count
import org.apache.spark.sql.catalyst.expressions.{Alias, Expression, GenericRowWithSchema, Literal, NamedExpression}
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.planning.PhysicalOperation
import org.apache.spark.sql.catalyst.plans.logical.{Aggregate, Limit, LogicalPlan}
import org.apache.spark.sql.catalyst.util.{DateTimeUtils, GenericArrayData}
import org.apache.spark.sql.execution.datasources.jdbc.{PostgresqlUtils, PostgresqlXDRelation}
import org.apache.spark.sql.sources.CatalystToCrossdataAdapter._
import org.apache.spark.sql.sources.{Filter => SourceFilter}
import org.apache.spark.sql.sources._
import org.apache.spark.sql.types.{Decimal, _}
import org.apache.spark.unsafe.types.UTF8String

object PostgresqlQueryProcessor {

  type PostgresQuery = String
  type ColumnName = String
  val DefaultLimit = 10000

  case class PostgresqlPlan(basePlan: BaseLogicalPlan, limit: Option[Int]){
    def projects: Seq[NamedExpression] = basePlan.projects
    def filters: Array[SourceFilter] = basePlan.filters
  }

  def quoteString(in: Any): String = in match {
    case s @ (_:String | _: Timestamp | _: Date) => s"'$s'"
    case other => other.toString
  }

  private def columnName(att: String): String = att.split("#").head.trim

  def apply(postgresRelation: PostgresqlXDRelation, logicalPlan: LogicalPlan, props: Properties): PostgresqlQueryProcessor =
    new PostgresqlQueryProcessor(postgresRelation, logicalPlan, props)

  def buildNativeQuery(tableQN: String,
                       requiredColumns: Seq[String],
                       filters: Array[SourceFilter],
                       limit: Int): PostgresQuery = {

    def filterToSQL(filter: SourceFilter): Option[String] = Option(filter match {
        case EqualTo(attr, value) => s"${columnName(attr)} = ${quoteString(value)}"
        case EqualNullSafe(attr, value) =>
          s"(NOT (${columnName(attr)} != ${quoteString(value)} OR ${columnName(attr)} IS NULL OR " +
            s"${quoteString(value)} IS NULL) OR (${columnName(attr)} IS NULL AND ${quoteString(value)} IS NULL))"
        case LessThan(attr, value) => s"${columnName(attr)} < ${quoteString(value)}"
        case GreaterThan(attr, value) => s"${columnName(attr)} > ${quoteString(value)}"
        case LessThanOrEqual(attr, value) => s"${columnName(attr)} <= ${quoteString(value)}"
        case GreaterThanOrEqual(attr, value) => s"${columnName(attr)} >= ${quoteString(value)}"
        case IsNull(attr) => s"${columnName(attr)} IS NULL"
        case IsNotNull(attr) => s"${columnName(attr)} IS NOT NULL"
        case StringStartsWith(attr, value) => s"${columnName(attr)} LIKE '$value%'"
        case StringEndsWith(attr, value) => s"${columnName(attr)} LIKE '%$value'"
        case StringContains(attr, value) => s"${columnName(attr)} LIKE '%$value%'"
        case In(attr, values) => s"${columnName(attr)} IN ${values.map(quoteString).mkString("(", ",", ")")}"
        case Not(f) => filterToSQL(f).map(p => s"(NOT ($p))").getOrElse(null)
        case Or(f1, f2) =>
          // We can't compile Or filter unless both sub-filters are compiled successfully.
          // It applies too for the following And filter.
          // If we can make sure compileFilter supports all filters, we can remove this check.
          val or = Seq(f1, f2).flatMap(filterToSQL)
          if (or.size == 2) {
            "(" + or.map(p => s"($p)").mkString(" OR ") + ")"
          } else {
            null
          }
        case And(f1, f2) =>
          val and = Seq(f1, f2).flatMap(filterToSQL)
          if (and.size == 2) {
            "(" + and.map(p => s"($p)").mkString(" AND ") + ")"
          } else {
            null
          }
        case _ => null
      })

    // TODO review this. AND is needed?
    val filter = if (filters.nonEmpty) filters.flatMap(filterToSQL).mkString("WHERE ", " AND ", "") else ""
    val columns = requiredColumns.map(columnName).mkString(", ")

    s"SELECT $columns FROM $tableQN $filter LIMIT $limit"
  }

}


class PostgresqlQueryProcessor(postgresRelation: PostgresqlXDRelation, logicalPlan: LogicalPlan, props: Properties)
  extends SparkLoggerComponent {

  import PostgresqlQueryProcessor._

  def execute(): Option[Array[Row]] = {

    def buildAggregationExpression(names: Expression): String = {
      names match {
        case Alias(child, _) => buildAggregationExpression(child)
        case Count(children) => s"count(${children.map(buildAggregationExpression).mkString(",")})"
        case Literal(1, _) => "*"
      }
    }

    try {
      validatedNativePlan.map { postgresqlPlan =>
        if (postgresqlPlan.limit.exists(_ == 0)) {
          Array.empty[Row]
        } else {
          val projectsString: Seq[String] = postgresqlPlan.basePlan match {
            case SimpleLogicalPlan(projects, _, _, _) =>
              projects.map(_.toString())

            case AggregationLogicalPlan(projects, groupingExpression, _, _, _) =>
              require(groupingExpression.isEmpty)

              projects.map(buildAggregationExpression)
          }

          val sqlQuery = buildNativeQuery(
            postgresRelation.table,
            projectsString,
            postgresqlPlan.filters,
            postgresqlPlan.limit.getOrElse(PostgresqlQueryProcessor.DefaultLimit)
          )
          logInfo("QUERY: " + sqlQuery)

          import scala.collection.JavaConversions._
          PostgresqlUtils.withClientDo(props.toMap) { (_, stm) =>
            val resultSet = stm.executeQuery(sqlQuery)
            sparkResultFromPostgresql(postgresqlPlan.projects.map(_.name).toArray, resultSet)
          }

        }
      }

    } catch {
      case exc: Exception => log.warn(s"Exception executing the native query $logicalPlan", exc.getMessage); None
    }

  }

  def validatedNativePlan: Option[PostgresqlPlan] = {
    lazy val limit: Option[Int] = logicalPlan.collectFirst { case Limit(Literal(num: Int, _), _) => num }

    def findBasePlan(lplan: LogicalPlan): Option[BaseLogicalPlan] = {
      lplan match {
        // TODO lines below seem to be duplicated in ExtendedPhysicalOperation when finding filters and projects
        case Limit(_, child) =>
          findBasePlan(child)

        case Aggregate(_, _, child) =>
          findBasePlan(child)

        case PhysicalOperation(projectList, filterList, _) =>
          CatalystToCrossdataAdapter.getConnectorLogicalPlan(logicalPlan, projectList, filterList) match {
            case CrossdataExecutionPlan(_, ProjectReport(exprIgnored), FilterReport(filtersIgnored, _)) if filtersIgnored.nonEmpty || exprIgnored.nonEmpty =>
              None
            case CrossdataExecutionPlan(basePlan, _, _) =>
              Some(basePlan)
          }

      }
    }
    findBasePlan(logicalPlan) collect{ case bp if checkNativeFilters(bp.filters) => PostgresqlPlan(bp, limit)}
  }

  private[this] def checkNativeFilters(filters: Array[SourceFilter]): Boolean = filters.forall {
      case _: EqualTo => true
      case _: EqualNullSafe => true
      case _: LessThan => true
      case _: GreaterThan => true
      case _: LessThanOrEqual => true
      case _: GreaterThanOrEqual => true
      case _: IsNull => true
      case _: IsNotNull => true
      case _: StringStartsWith => true
      case _: StringEndsWith => true
      case _: StringContains => true
      case _: In => true
      case _: Not => true
      case _: Or => true
      case _: And  => true
      case _ => false
    }

  private[this] def resultSchema(rs: ResultSet): StructType = {
    try {
      val rsmd = rs.getMetaData
      val ncols = rsmd.getColumnCount
      val fields = new Array[StructField](ncols)
      var i = 0
      while (i < ncols) {
        val columnName = rsmd.getColumnLabel(i + 1)
        val dataType = rsmd.getColumnType(i + 1)
        //val typeName = rsmd.getColumnTypeName(i + 1)
        val fieldSize = rsmd.getPrecision(i + 1)
        val fieldScale = rsmd.getScale(i + 1)
        val isSigned = rsmd.isSigned(i + 1)
        val nullable = rsmd.isNullable(i + 1) != ResultSetMetaData.columnNoNulls
        val metadata = new MetadataBuilder().putString("name", columnName)
        val columnType = PostgresqlUtils.getCatalystType(dataType, fieldSize, fieldScale, isSigned)
        fields(i) = StructField(columnName, columnType, nullable, metadata.build())
        i = i + 1
      }
      new StructType(fields)
    } catch {
      case e: Exception => throw new Exception(e.getMessage)
    }
  }

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

  private[this] def resultToRow(nCols: Int, rs: ResultSet, schema: StructType): Row = {
    val values = new Array[Any](nCols)
    (0 until nCols).foreach( i => values(i) = getValue(i, rs, schema))
    new GenericRowWithSchema(values, schema)
  }

  //to convert ResultSet to Array[Row]
  private[this] def sparkResultFromPostgresql(requiredColumns: Array[ColumnName], resultSet: ResultSet): Array[Row] = {

    val nCols = resultSet.getMetaData.getColumnCount
    val schema = resultSchema(resultSet)

    new Iterator[Row] {
      private var hasnext: Boolean = resultSet.next
      override def hasNext: Boolean = hasnext
      override def next(): Row = {
        val rs = resultToRow(nCols, resultSet, schema)
        hasnext = resultSet.next
        rs
      }
    } toArray
  }

}