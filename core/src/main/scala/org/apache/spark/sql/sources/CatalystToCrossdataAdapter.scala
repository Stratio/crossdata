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
import org.apache.spark.sql.catalyst.{CatalystTypeConverters, expressions}
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.execution.crossdata.{NativeUDFAttribute, NativeUDF, EvaluateNativeUDF}
import org.apache.spark.sql.execution.datasources.LogicalRelation
import org.apache.spark.sql.sources
import org.apache.spark.sql.sources.{Filter => SourceFilter}
import org.apache.spark.sql.types.StringType
import org.apache.spark.unsafe.types.UTF8String

object CatalystToCrossdataAdapter {


  def getFilterProject(logicalPlan: LogicalPlan,
                       projects: Seq[NamedExpression],
                       filterPredicates: Seq[Expression]):
  (Array[Attribute], Array[SourceFilter], Map[Attribute, NativeUDF], Boolean) = {

    val relation = logicalPlan.collectFirst { case l@LogicalRelation(_) => l}.get
    val att2udf = logicalPlan.collect { case EvaluateNativeUDF(udf, child, att) => att -> udf } toMap

    def udfFlattenedActualParameters[B](udfAttr: NativeUDFAttribute)(f: Attribute => B): Seq[B] = {
      att2udf(udfAttr).children.flatMap {
        case nat: NativeUDFAttribute => udfFlattenedActualParameters(nat)(f)
        case att: AttributeReference => Seq(f(att))
      }
    }

    val requestedCols: Map[Boolean, Seq[Attribute]] = projects.flatMap (
      _.references flatMap {
        case nat: NativeUDFAttribute =>
          udfFlattenedActualParameters(nat)(at => false -> relation.attributeMap(at)) :+ (true -> nat)
        case x => Seq(true -> relation.attributeMap(x))
      }
    ) groupBy(_._1) mapValues(_.map(_._2))

    val pushedFilters = filterPredicates.map {
      _ transform {
        case a: NativeUDFAttribute => a
        case a: Attribute => relation.attributeMap(a) // Match original case of attributes.
      }
    }

    //`required` is the collection of columns which should be present at the table (all referenced columns)
    val required = AttributeSet {
      requestedCols.values.reduce[Seq[Attribute]]((a,b) => a ++ b) filter {
        case _: NativeUDFAttribute => false
        case _ => true
      }
    }

    val (filters, ignored) = selectFilters(pushedFilters)
    (requestedCols(true).toArray, filters.toArray, att2udf, ignored)

  }

  /**
   * Selects Catalyst predicate [[Expression]]s which are convertible into data source [[Filter]]s,
   * and convert them.
   *
   * @param filters catalyst filters
   * @return filters which are convertible and a boolean indicating whether any filter has been ignored.
   */
  private[this] def selectFilters(filters: Seq[Expression]): (Array[SourceFilter], Boolean) = {
    var ignored = false
    def translate(predicate: Expression): Option[SourceFilter] = predicate match {
      case expressions.EqualTo(a: Attribute, Literal(v, t)) =>
        Some(sources.EqualTo(a.name, convertToScala(v, t)))
      case expressions.EqualTo(Literal(v, t), a: Attribute) =>
        Some(sources.EqualTo(a.name, convertToScala(v, t)))
      case expressions.EqualTo(a: NativeUDFAttribute, b: Attribute) =>
        Some(sources.EqualTo(b.name, a))
      case expressions.EqualTo(b: Attribute, a: NativeUDFAttribute) =>
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
      case expressions.GreaterThan(b: Attribute, a: NativeUDFAttribute) =>
        Some(sources.GreaterThan(b.name, a))
      case expressions.GreaterThan(a: NativeUDFAttribute, b: Attribute) =>
        Some(sources.LessThan(b.name, a))


      case expressions.LessThan(a: Attribute, Literal(v, t)) =>
        Some(sources.LessThan(a.name, convertToScala(v, t)))
      case expressions.LessThan(Literal(v, t), a: Attribute) =>
        Some(sources.GreaterThan(a.name, convertToScala(v, t)))
      case expressions.LessThan(b: Attribute, a: NativeUDFAttribute) =>
        Some(sources.LessThan(b.name, a))
      case expressions.LessThan(a: NativeUDFAttribute, b: Attribute) =>
        Some(sources.GreaterThan(b.name, a))

      case expressions.GreaterThanOrEqual(a: Attribute, Literal(v, t)) =>
        Some(sources.GreaterThanOrEqual(a.name, convertToScala(v, t)))
      case expressions.GreaterThanOrEqual(Literal(v, t), a: Attribute) =>
        Some(sources.LessThanOrEqual(a.name, convertToScala(v, t)))
      case expressions.GreaterThanOrEqual(b: Attribute, a: NativeUDFAttribute) =>
        Some(sources.GreaterThanOrEqual(b.name, a))
      case expressions.GreaterThanOrEqual(a: NativeUDFAttribute, b: Attribute) =>
        Some(sources.LessThanOrEqual(b.name, a))

      case expressions.LessThanOrEqual(a: Attribute, Literal(v, t)) =>
        Some(sources.LessThanOrEqual(a.name, convertToScala(v, t)))
      case expressions.LessThanOrEqual(Literal(v, t), a: Attribute) =>
        Some(sources.GreaterThanOrEqual(a.name, convertToScala(v, t)))
      case expressions.LessThanOrEqual(b: Attribute, a: NativeUDFAttribute) =>
        Some(sources.LessThanOrEqual(b.name, a))
      case expressions.LessThanOrEqual(a: NativeUDFAttribute, b: Attribute) =>
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

    // TODO fix bug, filtersIgnored could be false when some child filters within an 'Or', 'And' , 'Not' are ignored
    // TODO the bug above has been resolved but the variable use should be revised.
    (convertibleFilters, ignored)
  }

}