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

import org.apache.spark.sql.catalyst.CatalystTypeConverters._
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.plans.logical.{Aggregate, LogicalPlan}
import org.apache.spark.sql.catalyst.{CatalystTypeConverters, expressions}
import org.apache.spark.sql.crossdata.execution.{EvaluateNativeUDF, NativeUDF}
import org.apache.spark.sql.execution.datasources.LogicalRelation
import org.apache.spark.sql.sources
import org.apache.spark.sql.sources.{Filter => SourceFilter}
import org.apache.spark.sql.types.StringType
import org.apache.spark.unsafe.types.UTF8String

object CatalystToCrossdataAdapter {

  abstract class BaseLogicalPlan(val projects: Seq[NamedExpression], val filters: Array[SourceFilter], val udfsMap: Map[Attribute, NativeUDF])

  case class SimpleLogicalPlan(override val projects: Seq[Attribute],
                               override val filters: Array[SourceFilter],
                               override val udfsMap: Map[Attribute, NativeUDF]
                                ) extends BaseLogicalPlan(projects, filters, udfsMap)

  case class AggregationLogicalPlan(override val projects: Seq[NamedExpression],
                                    groupingExpresion: Seq[Expression],
                                    override val filters: Array[SourceFilter],
                                    override val udfsMap: Map[Attribute, NativeUDF]
                                     ) extends BaseLogicalPlan(projects, filters, udfsMap)


  def getConnectorLogicalPlan(logicalPlan: LogicalPlan,
                              projects: Seq[NamedExpression],
                              filterPredicates: Seq[Expression]): (BaseLogicalPlan, Boolean) = {


    val relation = logicalPlan.collectFirst { case l@LogicalRelation(_) => l }.get
    implicit val att2udf = logicalPlan.collect { case EvaluateNativeUDF(udf, child, att) => att -> udf } toMap

    val requestedCols: Map[Boolean, Seq[Attribute]] = projects.flatMap (
      _.references flatMap {
        case nat: AttributeReference if (att2udf contains nat) =>
          udfFlattenedActualParameters(nat, at => false -> relation.attributeMap(at)) :+ (true -> nat)
        case x => Seq(true -> relation.attributeMap(x))
      }
    ) groupBy (_._1) mapValues (_.map(_._2))

    val pushedFilters = filterPredicates.map {
      _ transform {
        case a: AttributeReference if(att2udf contains a) => a
        case a: Attribute => relation.attributeMap(a) // Match original case of attributes.
      }
    }

    val (filters, ignored) = selectFilters(pushedFilters, att2udf.keySet)

    val aggregatePlan: Option[(Seq[Expression], Seq[NamedExpression])] = logicalPlan.collectFirst {
      case Aggregate(groupingExpression, aggregationExpression, child) => (groupingExpression, aggregationExpression)
    }

    val baseLogicalPlan = aggregatePlan.fold[BaseLogicalPlan] {
      SimpleLogicalPlan(requestedCols(true), filters.toArray, att2udf)
    } { case (groupingExpression, selectExpression) =>
      AggregationLogicalPlan(selectExpression, groupingExpression, filters, att2udf)
    }
    (baseLogicalPlan, ignored)
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
  private[this] def selectFilters(filters: Seq[Expression], udfs: Set[Attribute]): (Array[SourceFilter], Boolean) = {
    var ignored = false
    def translate(predicate: Expression): Option[SourceFilter] = predicate match {
      case expressions.EqualTo(a: Attribute, Literal(v, t)) =>
        Some(sources.EqualTo(a.name, convertToScala(v, t)))
      case expressions.EqualTo(Literal(v, t), a: Attribute) =>
        Some(sources.EqualTo(a.name, convertToScala(v, t)))
      case expressions.EqualTo(a: AttributeReference, b: Attribute) if(udfs contains a) =>
        Some(sources.EqualTo(b.name, a))
      case expressions.EqualTo(b: Attribute, a: AttributeReference) if(udfs contains a) =>
        Some(sources.EqualTo(b.name, a))

      /* TODO
      case expressions.EqualNullSafe(a: Attribute, Literal(v, t)) =>
        Some(sources.EqualNullSafe(a.name, convertToScala(v, t)))
      case expressions.EqualNullSafe(Literal(v, t), a: Attribute) =>
        Some(sources.EqualNullSafe(a.name, convertToScala(v, t)))
      */

      case expressions.GreaterThan(a: Attribute, Literal(v, t)) =>
        Some(sources.GreaterThan(a.name, convertToScala(v, t)))
      case expressions.GreaterThan(Literal(v, t), a: Attribute) =>
        Some(sources.LessThan(a.name, convertToScala(v, t)))
      case expressions.GreaterThan(b: Attribute, a: AttributeReference) if(udfs contains a) =>
        Some(sources.GreaterThan(b.name, a))
      case expressions.GreaterThan(a: AttributeReference, b: Attribute) if(udfs contains a) =>
        Some(sources.LessThan(b.name, a))


      case expressions.LessThan(a: Attribute, Literal(v, t)) =>
        Some(sources.LessThan(a.name, convertToScala(v, t)))
      case expressions.LessThan(Literal(v, t), a: Attribute) =>
        Some(sources.GreaterThan(a.name, convertToScala(v, t)))
      case expressions.LessThan(b: Attribute, a: AttributeReference) if(udfs contains a) =>
        Some(sources.LessThan(b.name, a))
      case expressions.LessThan(a: AttributeReference, b: Attribute) if(udfs contains a) =>
        Some(sources.GreaterThan(b.name, a))

      case expressions.GreaterThanOrEqual(a: Attribute, Literal(v, t)) =>
        Some(sources.GreaterThanOrEqual(a.name, convertToScala(v, t)))
      case expressions.GreaterThanOrEqual(Literal(v, t), a: Attribute) =>
        Some(sources.LessThanOrEqual(a.name, convertToScala(v, t)))
      case expressions.GreaterThanOrEqual(b: Attribute, a: AttributeReference) if(udfs contains a) =>
        Some(sources.GreaterThanOrEqual(b.name, a))
      case expressions.GreaterThanOrEqual(a: AttributeReference, b: Attribute) if(udfs contains a) =>
        Some(sources.LessThanOrEqual(b.name, a))

      case expressions.LessThanOrEqual(a: Attribute, Literal(v, t)) =>
        Some(sources.LessThanOrEqual(a.name, convertToScala(v, t)))
      case expressions.LessThanOrEqual(Literal(v, t), a: Attribute) =>
        Some(sources.GreaterThanOrEqual(a.name, convertToScala(v, t)))
      case expressions.LessThanOrEqual(b: Attribute, a: AttributeReference) if(udfs contains a) =>
        Some(sources.LessThanOrEqual(b.name, a))
      case expressions.LessThanOrEqual(a: AttributeReference, b: Attribute) if(udfs contains a) =>
        Some(sources.GreaterThanOrEqual(b.name, a))

      case expressions.InSet(a: Attribute, set) =>
        val toScala = CatalystTypeConverters.createToScalaConverter(a.dataType)
        Some(sources.In(a.name, set.toArray.map(toScala)))

      // Because we only convert In to InSet in Optimizer when there are more than certain
      // items. So it is possible we still get an In expression here that needs to be pushed
      // down.
      case expressions.In(a: Attribute, list) if !list.exists(!_.isInstanceOf[Literal]) =>
        val hSet = list.map(e => e.eval(EmptyRow))
        val toScala = CatalystTypeConverters.createToScalaConverter(a.dataType)
        Some(sources.In(a.name, hSet.toArray.map(toScala)))

      case expressions.IsNull(a: Attribute) =>
        Some(sources.IsNull(a.name))
      case expressions.IsNotNull(a: Attribute) =>
        Some(sources.IsNotNull(a.name))

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
        Some(sources.StringStartsWith(a.name, v.toString))

      case expressions.EndsWith(a: Attribute, Literal(v: UTF8String, StringType)) =>
        Some(sources.StringEndsWith(a.name, v.toString))

      case expressions.Contains(a: Attribute, Literal(v: UTF8String, StringType)) =>
        Some(sources.StringContains(a.name, v.toString))

      case _ =>
        ignored = true
        None

    }
    val convertibleFilters = filters.flatMap(translate).toArray

    (convertibleFilters, ignored)
  }

}