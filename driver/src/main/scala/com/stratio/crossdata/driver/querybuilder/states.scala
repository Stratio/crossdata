/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
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