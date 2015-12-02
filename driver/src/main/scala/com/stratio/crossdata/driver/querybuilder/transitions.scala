package com.stratio.crossdata.driver.querybuilder

import com.stratio.crossdata.driver.querybuilder.dslentities.{SortCriteria, SortOrder}


trait Groupable {
  this: RunnableQuery =>

  def groupBy(groupingExpressions: Expression*): GroupedQuery =
    new GroupedQuery(projections, relation, filters, groupingExpressions)

}

trait Sortable {
  this: RunnableQuery =>

  def sortBy(ordering: SortOrder*) : SortedQuery = orderOrSortBy(global = true, ordering)
  def orderBy(ordering: SortOrder*) : SortedQuery = orderOrSortBy(global = true, ordering)

  private def orderOrSortBy(global: Boolean, ordering: Seq[SortOrder]) : SortedQuery =
    new SortedQuery(projections, relation, filters, groupingExpressions, havingExpressions, Some(SortCriteria(global, ordering)))

}

trait Limitable {
  this: RunnableQuery =>

  def limit(value: Int): LimitedQuery = new LimitedQuery(
    projections,
    relation,
    filters,
    groupingExpressions,
    havingExpressions,
    ordering,
    value)
}

trait CombinableQuery {
  def union(runnableQuery: CombinableQuery): CombinableQuery
}