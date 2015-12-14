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

import com.stratio.crossdata.driver.querybuilder.dslentities._


trait Groupable {
  this: RunnableQuery =>

  def groupBy(groupingExpressions: String): GroupedQuery = groupBy(XDQLStatement(groupingExpressions))

  def groupBy(groupingExpressions: Expression*): GroupedQuery =
    new GroupedQuery(context, projections, relation, filters, groupingExpressions)

}

trait Sortable {
  this: RunnableQuery =>

  def sortBy(ordering: String): SortedQuery = sortBy(SortOrder(ordering))

  def sortBy(ordering: Symbol): SortedQuery = sortBy(SortOrder(ordering))

  def sortBy(ordering: SortOrder*): SortedQuery = orderOrSortBy(global = false, ordering)

  def orderBy(ordering: Symbol): SortedQuery = orderBy(SortOrder(ordering))

  def orderBy(ordering: String): SortedQuery = orderBy(SortOrder(ordering))

  def orderBy(ordering: SortOrder*): SortedQuery = orderOrSortBy(global = true, ordering)

  private def orderOrSortBy(global: Boolean, ordering: Seq[SortOrder]): SortedQuery =
    new SortedQuery(
      context,
      projections,
      relation,
      filters,
      groupingExpressions,
      havingExpressions,
      SortCriteria(global, ordering)
    )

}

trait Limitable {
  this: RunnableQuery =>

  def limit(value: Int): LimitedQuery = new LimitedQuery(
    context,
    projections,
    relation,
    filters,
    groupingExpressions,
    havingExpressions,
    ordering,
    value)
}

trait Combinable extends CrossdataSQLStatement {
  this: RunnableQuery =>

  import CombineType._

  def unionAll(newQuery: RunnableQuery): CombinedQuery =
      generateCombinedQuery {
        computeCombinationInfo(newQuery, UnionAll, query => query.unionAll(newQuery))
      }

  def unionDistinct(newQuery: RunnableQuery): CombinedQuery =
    generateCombinedQuery {
      computeCombinationInfo(newQuery, UnionDistinct, query => query.unionDistinct(newQuery))
    }


  def intersect(newQuery: RunnableQuery): CombinedQuery =
    generateCombinedQuery {
      computeCombinationInfo(newQuery, Intersect, query => query.intersect(newQuery))
    }

  def except(newQuery: RunnableQuery): CombinedQuery =
    generateCombinedQuery {
      computeCombinationInfo(newQuery, Except, query => query.except(newQuery))
    }


  /**
   * Computes the new combination info after receiving a new query.
   *
   * See example below:
   * q1 UNION ALL q2 UNION DISTINCT q3
   * 1º) "q1 UNION ALL q2" generates a combination info for q1 (UNION ALL, q2)
   * 2º) "q1_q2 UNION DISTINCT q3" generates:
   * q1 should have a combination info (UNION ALL, q2 UNION DISTINCT q3)
   * q2 should have a combination info (UNION DISTINCT, q3)
   *
   * @param newQuery incoming query
   * @param newCombineType the incoming [[CombineType]]
   * @param childCombination function to generate a combined query from a runnable query.
   *                         It will be applied if the query is already a combined query
   * @return the new combination info
   */
  private def computeCombinationInfo( //TODO: Simplify this
                                      newQuery: RunnableQuery,
                                      newCombineType: CombineType,
                                      childCombination: RunnableQuery => CombinedQuery): CombinationInfo =
    composition map {
      case CombinationInfo(combType, previous) => CombinationInfo(combType, childCombination(previous))
    } getOrElse {
      CombinationInfo(newCombineType, newQuery)
    }

  private def generateCombinedQuery(combinationInfo: CombinationInfo): CombinedQuery =
    new CombinedQuery(
      context,
      projections,
      relation,
      filters,
      groupingExpressions,
      havingExpressions,
      ordering,
      limit,
      combinationInfo)
}
