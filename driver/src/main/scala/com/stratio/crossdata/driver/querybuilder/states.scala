package com.stratio.crossdata.driver.querybuilder

import com.stratio.crossdata.driver.querybuilder.dslentities.SortCriteria


class SimpleRunnableQuery private (
                           projections: Seq[Expression],
                           relation: Relation,
                           filters: Option[Predicate] = None)
  extends RunnableQuery(projections, relation)
  with Sortable
  with Limitable
  with Groupable {

  def this(projections: Seq[Expression], relation: Relation) = this(projections, relation, None)

  def having(expression: Expression*): HavingQuery = new HavingQuery(projections, relation, filters, groupingExpressions, expression)

  // It has to be abstract (simple runnable query has transitions) and concrete
  override def where(condition: Predicate): this.type =
    new SimpleRunnableQuery(projections, relation, Some(condition)).asInstanceOf[this.type] //TODO: Check this out

  override def union(runnableQuery: CombinableQuery): CombinableQuery = ???
}

class GroupedQuery(projections: Seq[Expression],
                   relation: Relation,
                   filters: Option[Predicate] = None,
                   groupingExpressions: Seq[Expression])
  extends RunnableQuery(projections, relation, filters)
  with Sortable
  with Limitable {

  def having(expression: Expression*): HavingQuery = new HavingQuery(projections, relation, filters, groupingExpressions, expression)

  // It has to be abstract (simple runnable query has transitions) and concrete
  override def where(condition: Predicate): this.type = ???

  override def union(runnableQuery: CombinableQuery): CombinableQuery = ???
}

class HavingQuery(projections: Seq[Expression],
                  relation: Relation,
                  filters: Option[Predicate] = None,
                  groupingExpressions: Seq[Expression],
                  havingExpressions: Seq[Expression])
  extends RunnableQuery(projections, relation, filters, groupingExpressions, havingExpressions)
  with Sortable
  with Limitable {
  // It has to be abstract (simple runnable query has transitions) and concrete
  override def where(condition: Predicate): this.type = ???

  override def union(runnableQuery: CombinableQuery): CombinableQuery = ???
}

class SortedQuery(projections: Seq[Expression],
                  relation: Relation,
                  filters: Option[Predicate] = None,
                  groupingExpressions: Seq[Expression] = Seq.empty,
                  havingExpressions: Seq[Expression] = Seq.empty,
                  ordering: Option[SortCriteria])
  extends RunnableQuery(projections, relation, filters, groupingExpressions, havingExpressions, ordering)
  with Limitable {
  // It has to be abstract (simple runnable query has transitions) and concrete
  override def where(condition: Predicate): this.type = ???

  override def union(runnableQuery: CombinableQuery): CombinableQuery = ???
}

class LimitedQuery(projections: Seq[Expression],
                   relation: Relation,
                   filters: Option[Predicate] = None,
                   groupingExpressions: Seq[Expression] = Seq.empty,
                   havingExpressions: Seq[Expression] = Seq.empty,
                   ordering: Option[SortCriteria],
                   limit: Int)
  extends RunnableQuery(projections, relation, filters, groupingExpressions, havingExpressions, ordering, Some(limit)) {
  // It has to be abstract (simple runnable query has transitions) and concrete
  override def where(condition: Predicate): this.type = ???

  override def union(runnableQuery: CombinableQuery): CombinableQuery = ???
}


object CombineType extends Enumeration {
  type CombineType = Value
  val UnionAll, Intersect, Except, FullOuter, LeftSemi = Value
}

import com.stratio.crossdata.driver.querybuilder.CombineType._

class CombinedQuery(leftQuery: RunnableQuery, combineType: CombineType, rightQuery: RunnableQuery) extends Relation with CombinableQuery {
  override def union(runnableQuery: CombinableQuery): CombinableQuery = ???

  override def toXDQL: String = ???
}
// relation => alias