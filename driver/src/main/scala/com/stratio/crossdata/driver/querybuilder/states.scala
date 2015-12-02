package com.stratio.crossdata.driver.querybuilder

import com.stratio.crossdata.driver.querybuilder.dslentities.SortCriteria


class SimpleRunnableQuery(projections: Seq[Expression],
                   relation: Relation)
  extends RunnableQuery(projections, relation)
  with Sortable
  with Limitable
  with Groupable {

  def having(expression: Expression*): HavingQuery = new HavingQuery(projections, relations, filters, groupingExpressions, expression)

}

class GroupedQuery(projections: Seq[Expression],
                   relation: Relation,
                   filters: Option[Predicate] = None,
                   groupingExpressions: Seq[Expression])
  extends RunnableQuery(projections, relation, filters)
  with Sortable
  with Limitable {

  def having(expression: Expression*): HavingQuery = new HavingQuery(projections, relations, filters, groupingExpressions, expression)

}

class HavingQuery(projections: Seq[Expression],
                  relation: Relation,
                  filters: Option[Predicate] = None,
                  groupingExpressions: Seq[Expression],
                  havingExpressions: Seq[Expression])
  extends RunnableQuery(projections, relation, filters, groupingExpressions, havingExpressions)
  with Sortable
  with Limitable

class SortedQuery(projections: Seq[Expression],
                  relation: Relation,,
                  filters: Option[Predicate] = None,
                  groupingExpressions: Seq[Expression] = Seq.empty,
                  havingExpressions: Seq[Expression] = Seq.empty,
                  ordering: Option[SortCriteria])
  extends RunnableQuery(projections, relation, filters, groupingExpressions, havingExpressions, ordering)
  with Limitable

class LimitedQuery(projections: Seq[Expression],
                   relation: Relation,,
                   filters: Option[Predicate] = None,
                   groupingExpressions: Seq[Expression] = Seq.empty,
                   havingExpressions: Seq[Expression] = Seq.empty,
                   ordering: Option[SortCriteria],
                   limit: Int)
  extends RunnableQuery(projections, relation, filters, groupingExpressions, havingExpressions, ordering, Some(limit))


object CombineType extends Enumeration {
  type CombineType = Value
  val UnionAll, Intersect, Except, FullOuter, LeftSemi = Value
}

import com.stratio.crossdata.driver.querybuilder.CombineType._

class CombinedQuery(leftQuery: RunnableQuery, combineType: CombineType, rightQuery: RunnableQuery) extends Relation with CombinableQuery
// relation => alias