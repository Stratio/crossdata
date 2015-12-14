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
package com.stratio.crossdata.driver.querybuilder

import com.stratio.crossdata.driver.querybuilder.dslentities.{CombinationInfo, SortCriteria, XDQLStatement}


class SimpleRunnableQuery private(context: String => String,
                                  projections: Seq[Expression],
                                  relation: Relation,
                                  filters: Option[Predicate] = None)
  extends RunnableQuery(context, projections, relation, filters)
  with Sortable
  with Limitable
  with Groupable {

  def this(projections: Seq[Expression], relation: Relation, context: String => String) =
    this(context, projections, relation, None)

  // It has to be abstract (simple runnable query has transitions) and concrete
  override def where(condition: Predicate): this.type =
    //Not knew alternatives to `asInstanceOf`: http://stackoverflow.com/a/791157/1893995
    new SimpleRunnableQuery(context, projections, relation, Some(combinePredicates(condition))).asInstanceOf[this.type]

}

class GroupedQuery(context: String => String,
                   projections: Seq[Expression],
                   relation: Relation,
                   filters: Option[Predicate] = None,
                   groupingExpressions: Seq[Expression])
  extends RunnableQuery(context, projections, relation, filters, groupingExpressions)
  with Sortable
  with Limitable {

  def having(expression: String): HavingQuery = having(XDQLStatement(expression))

  def having(expression: Predicate): HavingQuery =
    new HavingQuery(context, projections, relation, filters, groupingExpressions, expression)


  override def where(condition: Predicate): this.type =
    //Not knew alternatices to `asInstanceOf`: http://stackoverflow.com/a/791157/1893995
    new GroupedQuery(
      context,
      projections,
      relation,
      Some(combinePredicates(condition)),
      groupingExpressions
    ).asInstanceOf[this.type]


}

class HavingQuery(context: String => String,
                  projections: Seq[Expression],
                  relation: Relation,
                  filters: Option[Predicate] = None,
                  groupingExpressions: Seq[Expression],
                  havingExpressions: Predicate)
  extends RunnableQuery(
    context, projections, relation,
    filters, groupingExpressions, Some(havingExpressions))
  with Sortable
  with Limitable {

  override def where(condition: Predicate): this.type =
    new HavingQuery(
      context,
      projections,
      relation,
      Some(combinePredicates(condition)),
      groupingExpressions,
      havingExpressions
    ).asInstanceOf[this.type]

}

class SortedQuery(context: String => String,
                  projections: Seq[Expression],
                  relation: Relation,
                  filters: Option[Predicate] = None,
                  groupingExpressions: Seq[Expression] = Seq.empty,
                  havingExpressions: Option[Predicate] = None,
                  ordering: SortCriteria)
  extends RunnableQuery(
    context, projections, relation,
    filters, groupingExpressions, havingExpressions, Some(ordering)
  ) with Limitable {

  override def where(condition: Predicate): this.type =
    new SortedQuery(
      context,
      projections,
      relation,
      Some(combinePredicates(condition)),
      groupingExpressions,
      havingExpressions,
      ordering
    ).asInstanceOf[this.type]

}

class LimitedQuery(context: String => String,
                   projections: Seq[Expression],
                   relation: Relation,
                   filters: Option[Predicate] = None,
                   groupingExpressions: Seq[Expression] = Seq.empty,
                   havingExpressions: Option[Predicate] = None,
                   ordering: Option[SortCriteria],
                   limit: Int)
  extends RunnableQuery(
    context, projections, relation,
    filters, groupingExpressions, havingExpressions, ordering, Some(limit)) {

  override def where(condition: Predicate): this.type =
    new LimitedQuery(
      context,
      projections,
      relation,
      Some(combinePredicates(condition)),
      groupingExpressions,
      havingExpressions,
      ordering,
      limit
    ).asInstanceOf[this.type]

}

class CombinedQuery(context: String => String,
                    projections: Seq[Expression],
                    relation: Relation,
                    filters: Option[Predicate] = None,
                    groupingExpressions: Seq[Expression] = Seq.empty,
                    havingExpressions: Option[Predicate] = None,
                    ordering: Option[SortCriteria],
                    limit: Option[Int],
                    combinationInfo: CombinationInfo)
  extends RunnableQuery(
    context, projections, relation,
    filters, groupingExpressions, havingExpressions, ordering, limit, Some(combinationInfo)
  ) with Combinable {

  def where(condition: Predicate): this.type = throw new Error("Predicates cannot by applied to combined queries")

}