/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.spark.sql.sources.crossdata

import org.apache.spark.sql.catalyst.expressions
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.plans.logical.{Filter, LogicalPlan}
import org.apache.spark.sql.sources
import org.apache.spark.sql.sources.{Filter => SourceFilter, LogicalRelation}
import org.apache.spark.sql.types.{StringType, UTF8String}

object CatalystToCrossdataAdapter {


  def getFilterProject(logicalPlan: LogicalPlan, projects: Seq[NamedExpression],
                    filterPredicates: Seq[Expression]): (Array[String], Array[SourceFilter]) = {

    val projectSet = AttributeSet(projects.flatMap(_.references))
    val relation = logicalPlan.collectFirst { case l@LogicalRelation(_) => l}.get
    val pushedFilters = filterPredicates.map {
      _ transform {
        case a: AttributeReference => relation.attributeMap(a) // Match original case of attributes.
      }
    }

    val requestedColumns = projectSet.map(relation.attributeMap).toSeq

    (requestedColumns.map(_.name).toArray, selectFilters(pushedFilters).toArray)

  }

  /**
   * Selects Catalyst predicate [[Expression]]s which are convertible into data source [[Filter]]s,
   * and convert them.
   *
   */
  private[this] def selectFilters(filters: Seq[Expression]) = {
    def translate(predicate: Expression): Option[SourceFilter] = predicate match {
      // TODO support more type of filters
      // TODO filters which are not supported shouldn't be ignored when working with native connectors
      case expressions.EqualTo(a: Attribute, Literal(v, _)) =>
        Some(sources.EqualTo(a.name, v))
      case expressions.EqualTo(Literal(v, _), a: Attribute) =>
        Some(sources.EqualTo(a.name, v))

      case expressions.GreaterThan(a: Attribute, Literal(v, _)) =>
        Some(sources.GreaterThan(a.name, v))
      case expressions.GreaterThan(Literal(v, _), a: Attribute) =>
        Some(sources.LessThan(a.name, v))

      case expressions.LessThan(a: Attribute, Literal(v, _)) =>
        Some(sources.LessThan(a.name, v))
      case expressions.LessThan(Literal(v, _), a: Attribute) =>
        Some(sources.GreaterThan(a.name, v))

      case expressions.GreaterThanOrEqual(a: Attribute, Literal(v, _)) =>
        Some(sources.GreaterThanOrEqual(a.name, v))
      case expressions.GreaterThanOrEqual(Literal(v, _), a: Attribute) =>
        Some(sources.LessThanOrEqual(a.name, v))

      case expressions.LessThanOrEqual(a: Attribute, Literal(v, _)) =>
        Some(sources.LessThanOrEqual(a.name, v))
      case expressions.LessThanOrEqual(Literal(v, _), a: Attribute) =>
        Some(sources.GreaterThanOrEqual(a.name, v))

      case expressions.InSet(a: Attribute, set) =>
        Some(sources.In(a.name, set.toArray))

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

      case _ => None
    }

    filters.flatMap(translate)
  }
}
