package com.stratio.crossdata.driver.querybuilder

import com.stratio.crossdata.driver.querybuilder.dslentities.{SortCriteria, XDQLStatement}


class SimpleRunnableQuery private (
                           projections: Seq[Expression],
                           relation: Relation,
                           filters: Option[Predicate] = None)
  extends RunnableQuery(projections, relation, filters)
  with Sortable
  with Limitable
  with Groupable {

  def this(projections: Seq[Expression], relation: Relation) = this(projections, relation, None)

  // It has to be abstract (simple runnable query has transitions) and concrete
  override def where(condition: Predicate): this.type =
    new SimpleRunnableQuery(projections, relation, Some(combinePredicates(condition))).asInstanceOf[this.type] //TODO: Check this out

}

class GroupedQuery(projections: Seq[Expression],
                   relation: Relation,
                   filters: Option[Predicate] = None,
                   groupingExpressions: Seq[Expression])
  extends RunnableQuery(projections, relation, filters, groupingExpressions)
  with Sortable
  with Limitable {

  def having(expression: Predicate): HavingQuery = new HavingQuery(projections, relation, filters, groupingExpressions, expression)
  def having(expression: String): HavingQuery = having(XDQLStatement(expression))

  // It has to be abstract (simple runnable query has transitions) and concrete
  override def where(condition: Predicate): this.type =
    new GroupedQuery(projections, relation, Some(combinePredicates(condition)), groupingExpressions).asInstanceOf[this.type] //TODO: Check this out


}

class HavingQuery(projections: Seq[Expression],
                  relation: Relation,
                  filters: Option[Predicate] = None,
                  groupingExpressions: Seq[Expression],
                  havingExpressions: Predicate)
  extends RunnableQuery(projections, relation, filters, groupingExpressions, Some(havingExpressions))
  with Sortable
  with Limitable {
  // It has to be abstract (simple runnable query has transitions) and concrete
  override def where(condition: Predicate): this.type =
    new HavingQuery(projections, relation, Some(combinePredicates(condition)), groupingExpressions, havingExpressions).asInstanceOf[this.type]

}

class SortedQuery(projections: Seq[Expression],
                  relation: Relation,
                  filters: Option[Predicate] = None,
                  groupingExpressions: Seq[Expression] = Seq.empty,
                  havingExpressions: Option[Predicate] = None,
                  ordering: SortCriteria)
  extends RunnableQuery(projections, relation, filters, groupingExpressions, havingExpressions, Some(ordering))
  with Limitable {
  // It has to be abstract (simple runnable query has transitions) and concrete
  override def where(condition: Predicate): this.type =
    new SortedQuery(projections, relation, Some(combinePredicates(condition)), groupingExpressions, havingExpressions, ordering).asInstanceOf[this.type]

}

class LimitedQuery(projections: Seq[Expression],
                   relation: Relation,
                   filters: Option[Predicate] = None,
                   groupingExpressions: Seq[Expression] = Seq.empty,
                   havingExpressions: Option[Predicate] = None,
                   ordering: Option[SortCriteria],
                   limit: Int)
  extends RunnableQuery(projections, relation, filters, groupingExpressions, havingExpressions, ordering, Some(limit)) {
  // It has to be abstract (simple runnable query has transitions) and concrete
  override def where(condition: Predicate): this.type =
    new LimitedQuery(projections, relation, Some(combinePredicates(condition)), groupingExpressions, havingExpressions, ordering, limit).asInstanceOf[this.type]


}


object CombineType extends Enumeration {
  type CombineType = Value
  val UnionAll = Value("UNION ALL")
  val Intersect = Value("INTERSECT")
  val Except = Value("EXCEPT")
  val UnionDistinct = Value("UNION DISTINCT")
}


import com.stratio.crossdata.driver.querybuilder.CombineType._

case class CombinationInfo(combineType: CombineType, runnableQuery: RunnableQuery) extends CrossdataSQLStatement {
  override private[querybuilder] def toXDQL: String = s" ${combineType.toString} ${runnableQuery.toXDQL}"
}


class CombinedQuery(projections: Seq[Expression],
                    relation: Relation,
                    filters: Option[Predicate] = None,
                    groupingExpressions: Seq[Expression] = Seq.empty,
                    havingExpressions: Option[Predicate] = None,
                    ordering: Option[SortCriteria],
                    limit: Option[Int],
                    combinationInfo: CombinationInfo)
  extends RunnableQuery(projections, relation, filters, groupingExpressions, havingExpressions, ordering, limit, Some(combinationInfo)) with CombinableQuery {
  def where(condition: Predicate): this.type = throw new Error("Predicates cannot by applied to combined queries")

}
// relation => alias