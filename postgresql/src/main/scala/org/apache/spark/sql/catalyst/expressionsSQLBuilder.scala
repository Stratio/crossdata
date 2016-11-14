/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Modifications and adaptations - Copyright (C) 2015 Stratio (http://stratio.com)
 */

package org.apache.spark.sql.catalyst

import javax.xml.bind.DatatypeConverter

import org.apache.spark.sql.catalyst.expressions.{Abs, Alias, And, Ascending, AttributeReference, BinaryExpression, BinaryOperator, CaseWhen, Cast, Concat, ConcatWs, Contains, Descending, EndsWith, Expression, GetStructField, If, In, InSet, IsNotNull, IsNull, Literal, MonotonicallyIncreasingID, NamedExpression, Not, Or, Pmod, SortDirection, StartsWith, StringRegexExpression, TernaryExpression, UnaryExpression, UnaryMathExpression, UnaryMinus, UnaryPositive}
import org.apache.spark.sql.catalyst.expressions.aggregate.{AggregateExpression, AggregateFunction, Average}
import org.apache.spark.sql.catalyst.plans._
import org.apache.spark.sql.catalyst.plans.logical.{Aggregate, LogicalPlan}
import org.apache.spark.sql.catalyst.util.DateTimeUtils
import org.apache.spark.sql.types._
import org.apache.spark.unsafe.types.UTF8String

// TODO remove when upgrading to Spark2.0: this methods have already been implemented in Spark2.0
object expressionsSQLBuilder {

  case class AliasContext(aliasSupported: Boolean = true, logicalPlan: Option[LogicalPlan] = None)

  def prettyName(any: Any): String = any.getClass.getSimpleName.toLowerCase

/*
  def aggExpressionContains(aggregateExpression: Seq[NamedExpression], namedExpression: NamedExpression): Boolean = {
      aggregateExpression.contains(namedExpression) || {
        aggregateExpression.exists{
          case Alias(aExp: AggregateExpression, _) =>
            aExp.references.contains(namedExpression)
          case _ => false
        }
      }
    }
*/

  def partFunctionNamedExpressionToSQL(aliasContext: AliasContext): PartialFunction[Expression, String] = {
    case attributeReference: AttributeReference =>

      val name = if (aliasContext.aliasSupported || aliasContext.logicalPlan.isEmpty) {
        attributeReference.name
      } else {
        aliasContext.logicalPlan.get.collectFirst {
          // postgres does not support alias in the group by, so the alias in the aggregation is replaced by the sql of the expression
          // TODO use pattern matching + unapply instead
          case Aggregate(_, aggregateExpressions, _) =>
            aggregateExpressions.collectFirst {
              case Alias(aExp: Expression, aliasName) if attributeReference.name == aliasName =>
                aExp.sql
            } getOrElse attributeReference.name
          // TODO case Project(...) => check whether the project contains alias or not
        } getOrElse attributeReference.name
      }

      val qualifiersString =
        if (attributeReference.qualifiers.isEmpty) "" else attributeReference.qualifiers.map("" + _ + "").mkString("", ".", ".")
      s"$qualifiersString$name"

    case al: Alias =>
      val qualifiersString =
        if (al.qualifiers.isEmpty) "" else al.qualifiers.map("" + _ + "").mkString("", ".", ".")
      s"${al.child.sql} AS $qualifiersString${al.name}"
  }


  def partFunctionExpressionNoNamedToSQL(aliasContext: AliasContext): PartialFunction[Expression, String] = {

    case Literal(value, dataType) => (value, dataType) match {
      case (_, NullType | _: ArrayType | _: MapType | _: StructType) if value == null => "NULL"
      case _ if value == null => s"CAST(NULL AS ${dataType.sql})"
      case (v: UTF8String, StringType) =>
        // Escapes all backslashes and single quotes.
        "'" + v.toString.replace("\\", "\\\\").replace("'", "\\'") + "'"
      case (v: Byte, ByteType) => v.toString
      case (v: Short, ShortType) => v.toString
      case (v: Long, LongType) => v.toString
      // Float type doesn't have a suffix
      case (v: Float, FloatType) =>
        val castedValue = v match {
          case _ if v.isNaN => "'NaN'"
          case Float.PositiveInfinity => "'Infinity'"
          case Float.NegativeInfinity => "'-Infinity'"
          case _ => v.toString
        }
        s"CAST($castedValue AS ${FloatType.sql})"
      case (v: Double, DoubleType) =>
        v match {
          case _ if v.isNaN => s"CAST('NaN' AS ${DoubleType.sql})"
          case Double.PositiveInfinity => s"CAST('Infinity' AS ${DoubleType.sql})"
          case Double.NegativeInfinity => s"CAST('-Infinity' AS ${DoubleType.sql})"
          case _ => v.toString
        }
      case (v: Decimal, t: DecimalType) => v.toString
      case (v: Int, DateType) => s"DATE '${DateTimeUtils.toJavaDate(v)}'"
      case (v: Long, TimestampType) => s"TIMESTAMP '${DateTimeUtils.toJavaTimestamp(v)}'"
      case (v: Array[Byte], BinaryType) => s"X'${DatatypeConverter.printHexBinary(v)}'"
      case _ => value.toString
    }
    case IsNull(child) => s"(${child.sql(aliasContext)} IS NULL)"
    case IsNotNull(child) => s"(${child.sql(aliasContext)} IS NOT NULL)"
    case Abs(child) => s"${Abs.getClass.getSimpleName.toLowerCase}(${child.sql(aliasContext)})"
    case InSet(child, hset) =>
      val valueSQL = child.sql(aliasContext)
      val listSQL = hset.toSeq.map(Literal(_).sql(aliasContext)).mkString(", ")
      s"($valueSQL IN ($listSQL))"
    case in@ In(value, list) =>
      val childrenSQL = in.children.map(_.sql(aliasContext))
      val valueSQL = childrenSQL.head
      val listSQL = childrenSQL.tail.mkString(", ")
      s"($valueSQL IN ($listSQL))"
    case And(left, right) => s"(${left.sql(aliasContext)} AND ${right.sql(aliasContext)})"
    case Or(left, right) => s"(${left.sql(aliasContext)} OR ${right.sql(aliasContext)})"

    case uMath: UnaryMathExpression =>
      s"${uMath.funcName}(${uMath.child.sql(aliasContext)})"

    case UnaryMinus(child) =>
      s"(-${child.sql(aliasContext)})"

    case UnaryPositive(child) =>
      s"(+${child.sql(aliasContext)})"

    case Cast(child, dataType) =>
      dataType match {
        // HiveQL doesn't allow casting to complex types. For logical plans translated from HiveQL, this
        // type of casting can only be introduced by the analyzer, and can be omitted when converting
        // back to SQL query string.
        case _: ArrayType | _: MapType | _: StructType => child.sql
        case _ => s"CAST(${child.sql} AS ${dataType.sql})"
      }

    case Not(child) =>
      s"(NOT ${child.sql(aliasContext)})"

    case strRegexExp: StringRegexExpression => strRegexExp match {
      case bExpression: BinaryExpression => s"${bExpression.left.sql} ${prettyName(strRegexExp).toUpperCase} ${bExpression.right.sql}"
      case _ => throw new RuntimeException("expression not supported") //TODO
    }
    case c@Concat(children) => s"${prettyName(c)}(${children.map(_.sql(aliasContext)).mkString(", ")})"
    case c@ConcatWs(children) => s"concat_ws(${children.map(_.sql(aliasContext)).mkString(", ")})"
    case Pmod(left, right) => s"@ (${left.sql(aliasContext)} % ${right.sql(aliasContext)})"
    case CaseWhen(branches) =>
      val branchesSQL = branches.map(_.sql(aliasContext))
      val (cases, maybeElse) = if (branches.length % 2 == 0) {
        (branchesSQL, None)
      } else {
        (branchesSQL.init, Some(branchesSQL.last))
      }

      val head = s"CASE "
      val tail = maybeElse.map(e => s" ELSE $e").getOrElse("") + " END"
      val body = cases.grouped(2).map {
        case Seq(whenExpr, thenExpr) => s"WHEN $whenExpr THEN $thenExpr"
      }.mkString(" ")

      head + body + tail

    case AggregateExpression(aggregateFunction, _, isDistinct) =>
      aggregateFunction.sql(isDistinct)

    case m: MonotonicallyIncreasingID =>
      s"${prettyName(m)}()"

    // GENERIC Unary and Binary Expression
    case bOperator: BinaryOperator =>
      s"(${bOperator.left.sql(aliasContext)} ${bOperator.symbol} ${bOperator.right.sql(aliasContext)})"

    case uExpr: UnaryExpression => s"(${prettyName(uExpr)}(${uExpr.child.sql(aliasContext)}))"

    case StartsWith(left, Literal(value, StringType)) =>
      s"${left.sql} LIKE '${value.toString}%'"
    case EndsWith(left, Literal(value, StringType)) =>
      s"${left.sql} LIKE '%$value'"
    case Contains(left, Literal(value, StringType)) =>
      s"${left.sql} LIKE '%$value%'"

    case bExpr: BinaryExpression =>
      s"${prettyName(bExpr)}(${bExpr.left.sql(aliasContext)}, ${bExpr.right.sql(aliasContext)})"

    case ternaryExp: TernaryExpression =>
      val childrenSQL = ternaryExp.children.map(_.sql(aliasContext)).mkString(", ")
      s"${prettyName(ternaryExp)}($childrenSQL)"

    case expression => throw new RuntimeException(s"Expression ${expression.prettyString} not supported") // TODO improve exception message
  }


  def namedExpressionToSQL(namedExpression: NamedExpression, aliasContext: AliasContext): String =
  partFunctionNamedExpressionToSQL(aliasContext)(namedExpression)

  def expressionToSQL(expression: Expression,aliasContext: AliasContext): String ={
    val aa = partFunctionNamedExpressionToSQL(aliasContext) orElse partFunctionExpressionNoNamedToSQL(aliasContext)
    aa(expression)
  }


  implicit class ExpressionWithSQL(expression: Expression){
    def sql: String = sql(AliasContext())
    def sql(aliasContext: AliasContext): String = expressionToSQL(expression, aliasContext)
  }

  implicit class JoinTypeWithSQL(joinType: JoinType){
    def sql = joinType match {
      case Inner => "INNER"
      case FullOuter => "LEFT OUTER"
      case LeftOuter => "RIGHT OUTER"
      case RightOuter => "FULL OUTER"
      case LeftSemi => "LEFT SEMI"
    }
  }

  implicit class SortDirectionWithSQL(sortDirection: SortDirection){
    def sql = sortDirection match {
      case Ascending => "ASC"
      case Descending => "DESC"
    }
  }

  implicit class DataTypeWithSQL(dataType: DataType) {
    def sql: String = dataType match {
      case MapType(keyType, valueType, _) => s"MAP<${keyType.sql}, ${valueType.sql}>"
      case StructType(fields) =>
        val fieldTypes = fields.map(f => s"${f.name}: ${f.dataType.sql}")
        s"STRUCT<${fieldTypes.mkString(", ")}>"
      case udt: UserDefinedType[_] => udt.sqlType.sql
      case ArrayType(elementType, _) => s"ARRAY<${elementType.sql}>"
      case _ => dataType.simpleString.toUpperCase
    }
  }

  implicit class AggregateFunctionWithSQL(aggFunction: AggregateFunction){
    def sql(isDistinct: Boolean) = {
      val distinct = if (isDistinct) "DISTINCT " else " "
      val functionName = aggFunction match {
        case _: Average => "avg"
        case _ => prettyName(aggFunction)
      }
      s"$functionName($distinct${aggFunction.children.map(_.sql).mkString(", ")})"
    }
  }


}
