/**
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
package org.apache.spark.sql.sources


import org.apache.spark.sql.catalyst.CatalystTypeConverters
import org.apache.spark.sql.catalyst.CatalystTypeConverters.convertToScala
import org.apache.spark.sql.catalyst.expressions
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.plans.logical.Aggregate
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.crossdata.execution.EvaluateNativeUDF
import org.apache.spark.sql.crossdata.execution.NativeUDF
import org.apache.spark.sql.execution.datasources.LogicalRelation
import org.apache.spark.sql.sources
import org.apache.spark.sql.sources.{Filter => SourceFilter}
import org.apache.spark.sql.types.ArrayType
import org.apache.spark.sql.types.StringType
import org.apache.spark.unsafe.types.UTF8String

import scala.collection.mutable.ListBuffer

object CatalystToCrossdataAdapter {

  case class FilterReport(filtersIgnored: Seq[Expression], ignoredNativeUDFReferences: Seq[AttributeReference])
  case class ProjectReport(expressionsIgnored: Seq[Expression])

  abstract class BaseLogicalPlan(
                                  val projects: Seq[NamedExpression],
                                  val filters: Array[SourceFilter],
                                  val udfsMap: Map[Attribute, NativeUDF],
                                  val collectionRandomAccesses: Map[Attribute, GetArrayItem]
                                )

  case class SimpleLogicalPlan(override val projects: Seq[Attribute],
                               override val filters: Array[SourceFilter],
                               override val udfsMap: Map[Attribute, NativeUDF],
                               override val collectionRandomAccesses: Map[Attribute, GetArrayItem]
                                ) extends BaseLogicalPlan(projects, filters, udfsMap, collectionRandomAccesses)

  case class AggregationLogicalPlan(override val projects: Seq[NamedExpression],
                                    groupingExpresion: Seq[Expression],
                                    override val filters: Array[SourceFilter],
                                    override val udfsMap: Map[Attribute, NativeUDF],
                                    override val collectionRandomAccesses: Map[Attribute, GetArrayItem]
                                     ) extends BaseLogicalPlan(projects, filters, udfsMap, collectionRandomAccesses)


  /**
   * Transforms a Catalyst Logical Plan to a Crossdata Logical Plan
   * @param logicalPlan catalyst logical plan
   * @return A tuple of (Crossdata BaseLogicalPlan, FilterReport)
   */
  def getConnectorLogicalPlan(logicalPlan: LogicalPlan,
                              projects: Seq[NamedExpression],
                              filterPredicates: Seq[Expression]): (BaseLogicalPlan, ProjectReport, FilterReport) = {

    val relation = logicalPlan.collectFirst { case lr: LogicalRelation => lr }.get
    implicit val att2udf = logicalPlan.collect { case EvaluateNativeUDF(udf, child, att) => att -> udf } toMap
    implicit val att2itemAccess: Map[Attribute, GetArrayItem] =(projects ++ filterPredicates).flatMap { c =>
      c.collect {
        case gi @ GetArrayItem(a@AttributeReference(name, ArrayType(etype, _), nullable, md), _) =>
          AttributeReference(name, etype, true)() -> gi
      }
    } toMap

    val itemAccess2att: Map[GetArrayItem, Attribute] = att2itemAccess.map(_.swap)

    object ExpressionType extends Enumeration {
      type ExpressionType = Value
      val Requested, Found, Ignored = Value
    }

    import ExpressionType._

    def extractRequestedColumns(namedExpression: Expression): Seq[(ExpressionType, Expression)] = namedExpression match {

      case Alias(child, _) =>
        extractRequestedColumns(child)

      case aRef: AttributeReference =>
        Seq(Requested -> aRef)

      case nudf: NativeUDF =>
        nudf.references flatMap {
        case nat: AttributeReference if att2udf contains nat =>
          udfFlattenedActualParameters(nat, at => Found -> relation.attributeMap(at)) :+ (Requested -> nat)
       } toSeq

      case c: GetArrayItem if itemAccess2att contains c =>
        c.references.map(Found -> relation.attributeMap(_)).toSeq :+ (Requested -> itemAccess2att(c))

      // TODO should these expressions be ignored? We are ommitting expressions within structfields
      case c: GetStructField  => c.references flatMap {
        case x => Seq(Requested -> relation.attributeMap(x))
      } toSeq

      case ignoredExpr =>
        Seq(Ignored -> ignoredExpr)
    }


    val columnExpressions: Map[ExpressionType, Seq[Expression]] = projects.flatMap {
      extractRequestedColumns
    } groupBy (_._1) mapValues (_.map(_._2))

    val pushedFilters = filterPredicates.map {
      _ transform {
        case getitem: GetArrayItem if itemAccess2att contains getitem => itemAccess2att(getitem)
        case a: AttributeReference if att2udf contains a => a
        case a: Attribute => relation.attributeMap(a) // Match original case of attributes.
      }
    }

    val (filters, filterReport) = selectFilters(pushedFilters, att2udf.keySet, att2itemAccess)

    val aggregatePlan: Option[(Seq[Expression], Seq[NamedExpression])] = logicalPlan.collectFirst {
      case Aggregate(groupingExpression, aggregationExpression, child) => (groupingExpression, aggregationExpression)
    }


    val baseLogicalPlan = aggregatePlan.fold[BaseLogicalPlan] {
      val requestedColumns: Seq[Attribute] = columnExpressions(Requested) collect { case a: Attribute => a }
      SimpleLogicalPlan(requestedColumns, filters.toArray, att2udf, att2itemAccess)
    } { case (groupingExpression, selectExpression) =>
      AggregationLogicalPlan(selectExpression, groupingExpression, filters, att2udf, att2itemAccess)
    }
    val projectReport = columnExpressions.getOrElse(Ignored, Seq.empty)
    (baseLogicalPlan, ProjectReport(projectReport), filterReport)
  }

  def udfFlattenedActualParameters[B](
                                       udfAttr: AttributeReference,
                                       f: Attribute => B
                                       )(implicit udfs: Map[Attribute, NativeUDF]): Seq[B] = {
    udfs(udfAttr).children.flatMap { case att: AttributeReference =>
      if(udfs contains att) udfFlattenedActualParameters(att, f) else Seq(f(att))
    }
  }

  /**
   * Selects Catalyst predicate [[Expression]]s which are convertible into data source [[Filter]]s,
   * and convert them.
   *
   * @param filters catalyst filters
   * @return filters which are convertible and a boolean indicating whether any filter has been ignored.
   */
  private[this] def selectFilters(
                                   filters: Seq[Expression],
                                   udfs: Set[Attribute],
                                   att2arrayaccess: Map[Attribute, GetArrayItem]
                                 ): (Array[SourceFilter], FilterReport) = {
    val ignoredExpressions: ListBuffer[Expression] = ListBuffer.empty
    val ignoredNativeUDFReferences: ListBuffer[AttributeReference] = ListBuffer.empty

    def attAsOperand(att: Attribute): String = att2arrayaccess.get(att).map {
      case GetArrayItem(child, ordinal) => s"${att.name}[${ordinal.toString()}]"
    } getOrElse(att.name)

    def translate(predicate: Expression): Option[SourceFilter] = predicate match {
      case expressions.EqualTo(a: Attribute, Literal(v, t)) =>
        Some(sources.EqualTo(attAsOperand(a), convertToScala(v, t)))
      case expressions.EqualTo(Literal(v, t), a: Attribute) =>
        Some(sources.EqualTo(attAsOperand(a), convertToScala(v, t)))
      case expressions.EqualTo(a: AttributeReference, b: Attribute) if udfs contains a =>
        Some(sources.EqualTo(attAsOperand(b), a))
      case expressions.EqualTo(b: Attribute, a: AttributeReference) if udfs contains a =>
        Some(sources.EqualTo(attAsOperand(b), a))
      case expressions.EqualTo(Cast(a:Attribute, StringType), Literal(v, t)) =>
        Some(sources.EqualTo(attAsOperand(a), convertToScala(Cast(Literal(v.toString), a.dataType).eval(EmptyRow), a
          .dataType)))

      /* TODO
      case expressions.EqualNullSafe(a: Attribute, Literal(v, t)) =>
        Some(sources.EqualNullSafe(a.name, convertToScala(v, t)))
      case expressions.EqualNullSafe(Literal(v, t), a: Attribute) =>
        Some(sources.EqualNullSafe(a.name, convertToScala(v, t)))
      */

      case expressions.GreaterThan(a: Attribute, Literal(v, t)) =>
        Some(sources.GreaterThan(attAsOperand(a), convertToScala(v, t)))
      case expressions.GreaterThan(Literal(v, t), a: Attribute) =>
        Some(sources.LessThan(attAsOperand(a), convertToScala(v, t)))
      case expressions.GreaterThan(b: Attribute, a: AttributeReference) if udfs contains a =>
        Some(sources.GreaterThan(attAsOperand(b), a))
      case expressions.GreaterThan(a: AttributeReference, b: Attribute) if udfs contains a =>
        Some(sources.LessThan(attAsOperand(b), a))
      case expressions.GreaterThan(Cast(a:Attribute, StringType), Literal(v, t)) =>
        Some(sources.GreaterThan(attAsOperand(a),
          convertToScala(Cast(Literal(v.toString), a.dataType).eval(EmptyRow), a.dataType)))

      case expressions.LessThan(a: Attribute, Literal(v, t)) =>
        Some(sources.LessThan(attAsOperand(a), convertToScala(v, t)))
      case expressions.LessThan(Literal(v, t), a: Attribute) =>
        Some(sources.GreaterThan(attAsOperand(a), convertToScala(v, t)))
      case expressions.LessThan(b: Attribute, a: AttributeReference) if udfs contains a =>
        Some(sources.LessThan(attAsOperand(b), a))
      case expressions.LessThan(a: AttributeReference, b: Attribute) if udfs contains a =>
        Some(sources.GreaterThan(attAsOperand(b), a))
      case expressions.LessThan(Cast(a:Attribute, StringType), Literal(v, t)) =>
        Some(sources.LessThan(attAsOperand(a),
          convertToScala(Cast(Literal(v.toString), a.dataType).eval(EmptyRow), a.dataType)))

      case expressions.GreaterThanOrEqual(a: Attribute, Literal(v, t)) =>
        Some(sources.GreaterThanOrEqual(attAsOperand(a), convertToScala(v, t)))
      case expressions.GreaterThanOrEqual(Literal(v, t), a: Attribute) =>
        Some(sources.LessThanOrEqual(attAsOperand(a), convertToScala(v, t)))
      case expressions.GreaterThanOrEqual(b: Attribute, a: AttributeReference) if udfs contains a =>
        Some(sources.GreaterThanOrEqual(attAsOperand(b), a))
      case expressions.GreaterThanOrEqual(a: AttributeReference, b: Attribute) if udfs contains a =>
        Some(sources.LessThanOrEqual(attAsOperand(b), a))
      case expressions.GreaterThanOrEqual(Cast(a:Attribute,StringType), Literal(v, t)) =>
        Some(sources.GreaterThanOrEqual(attAsOperand(a),
          convertToScala(Cast(Literal(v.toString), a.dataType).eval(EmptyRow), a.dataType)))

      case expressions.LessThanOrEqual(a: Attribute, Literal(v, t)) =>
        Some(sources.LessThanOrEqual(attAsOperand(a), convertToScala(v, t)))
      case expressions.LessThanOrEqual(Literal(v, t), a: Attribute) =>
        Some(sources.GreaterThanOrEqual(attAsOperand(a), convertToScala(v, t)))
      case expressions.LessThanOrEqual(b: Attribute, a: AttributeReference) if udfs contains a =>
        Some(sources.LessThanOrEqual(attAsOperand(b), a))
      case expressions.LessThanOrEqual(a: AttributeReference, b: Attribute) if udfs contains a =>
        Some(sources.GreaterThanOrEqual(attAsOperand(b), a))
      case expressions.LessThanOrEqual(Cast(a:Attribute,StringType), Literal(v, t)) =>
        Some(sources.LessThanOrEqual(attAsOperand(a),
          convertToScala(Cast(Literal(v.toString), a.dataType).eval(EmptyRow), a.dataType)))


      case expressions.InSet(a: Attribute, set) =>
        val toScala = CatalystTypeConverters.createToScalaConverter(a.dataType)
        Some(sources.In(attAsOperand(a), set.toArray.map(toScala)))

      // Because we only convert In to InSet in Optimizer when there are more than certain
      // items. So it is possible we still get an In expression here that needs to be pushed
      // down.
      case expressions.In(a: Attribute, list) if !list.exists(!_.isInstanceOf[Literal]) =>
        val hSet = list.map(e => e.eval(EmptyRow))
        val toScala = CatalystTypeConverters.createToScalaConverter(a.dataType)
        Some(sources.In(attAsOperand(a), hSet.toArray.map(toScala)))

      case expressions.IsNull(a: Attribute) =>
        Some(sources.IsNull(attAsOperand(a)))
      case expressions.IsNotNull(a: Attribute) =>
        Some(sources.IsNotNull(attAsOperand(a)))

      case expressions.And(left, right) =>
        (translate(left) ++ translate(right)).reduceOption(sources.And)

      case expressions.Or(left, right) =>
        for {
          leftFilter <- translate(left)
          rightFilter <- translate(right)
        } yield sources.Or(leftFilter, rightFilter)

      case expressions.Not(child) =>
        translate(child).map(sources.Not)

      case expressions.StartsWith(a: Attribute, Literal(v: UTF8String, StringType)) =>
        Some(sources.StringStartsWith(attAsOperand(a), v.toString))

      case expressions.EndsWith(a: Attribute, Literal(v: UTF8String, StringType)) =>
        Some(sources.StringEndsWith(attAsOperand(a), v.toString))

      case expressions.Contains(a: Attribute, Literal(v: UTF8String, StringType)) =>
        Some(sources.StringContains(attAsOperand(a), v.toString))

      case expression =>
        ignoredExpressions += expression
        ignoredNativeUDFReferences ++= expression.collect{case att: AttributeReference if udfs contains att => att}
        None

    }
    val convertibleFilters = filters.flatMap(translate).toArray

    val filterReport = FilterReport(ignoredExpressions, ignoredNativeUDFReferences)
    (convertibleFilters, filterReport)
  }

}
