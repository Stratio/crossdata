/*
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
package org.apache.spark.sql.crossdata.catalyst.analysis

import org.apache.spark.sql.catalyst.CatalystConf
import org.apache.spark.sql.catalyst.analysis._
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.plans.logical
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.catalyst.rules._
import org.apache.spark.sql.crossdata.catalog.XDCatalog
import org.apache.spark.sql.crossdata.catalyst.ExtendedUnresolvedRelation
import org.apache.spark.sql.crossdata.catalyst.globalindex.IndexUtils

import scala.annotation.tailrec

// SELECT sth as alias GROUP BY alias is allowed thanks to the rule
object ResolveAggregateAlias extends Rule[LogicalPlan] {

  def apply(plan: LogicalPlan): LogicalPlan = plan resolveOperators {
    case p: LogicalPlan if !p.childrenResolved => p

    case a@Aggregate(grouping, aggregateExp, child) if child.resolved && !a.resolved && groupingExpressionsContainAlias(grouping, aggregateExp) =>
      val newGrouping = grouping.map { groupExpression =>
        groupExpression transformUp {
          case PostponedAttribute(u@UnresolvedAttribute(Seq(aliasCandidate))) =>
            aggregateExp.collectFirst {
              case Alias(resolvedAttribute, aliasName) if aliasName == aliasCandidate =>
                resolvedAttribute
            }.getOrElse(u)
        }
      }
      a.copy(groupingExpressions = newGrouping)

  }

  private def groupingExpressionsContainAlias(groupingExpressions: Seq[Expression], aggregateExpressions: Seq[NamedExpression]): Boolean = {
    def aggregateExpressionsContainAliasReference(aliasCandidate: String) =
      aggregateExpressions.exists {
        case Alias(resolvedAttribute, aliasName) if aliasName == aliasCandidate =>
          true
        case _ =>
          false
      }

    groupingExpressions.exists {
      case PostponedAttribute(UnresolvedAttribute(Seq(aliasCandidate))) =>
        aggregateExpressionsContainAliasReference(aliasCandidate)
      case _ => false
    }
  }

  // Catalyst config cannot be read, so the most restrictive resolver is used
  val resolver: Resolver = caseSensitiveResolution

}

object PrepareAggregateAlias extends Rule[LogicalPlan] {

  override def apply(plan: LogicalPlan): LogicalPlan = plan resolveOperators {

    case a@Aggregate(grouping, aggregateExp, child) if !child.resolved && !a.resolved && groupingExpressionsContainUnresolvedAlias(grouping, aggregateExp) =>
      val newGrouping = grouping.map { groupExpression =>
        groupExpression transformUp {
          case u@UnresolvedAttribute(Seq(aliasCandidate)) =>
            aggregateExp.collectFirst {
              case UnresolvedAlias(Alias(unresolvedAttr, aliasName)) if aliasName == aliasCandidate =>
                PostponedAttribute(u)
            }.getOrElse(u)
        }
      }
      a.copy(groupingExpressions = newGrouping)
  }

  private def groupingExpressionsContainUnresolvedAlias(groupingExpressions: Seq[Expression], aggregateExpressions: Seq[NamedExpression]): Boolean = {
    def aggregateExpressionsContainAliasReference(aliasCandidate: String) =
      aggregateExpressions.exists {
        case UnresolvedAlias(Alias(unresolvedAttribute, aliasName)) if aliasName == aliasCandidate =>
          true
        case _ =>
          false
      }

    groupingExpressions.exists {
      case UnresolvedAttribute(Seq(aliasCandidate)) =>
        aggregateExpressionsContainAliasReference(aliasCandidate)
      case _ => false
    }
  }

}

case class WrapRelationWithGlobalIndex(catalog: XDCatalog) extends Rule[LogicalPlan] {

  override def apply(plan: LogicalPlan): LogicalPlan = plan resolveOperators {

    case pp if planWithAvailableIndex(pp) =>
      plan resolveOperators {
        case unresolvedRelation: UnresolvedRelation =>
          ExtendedUnresolvedRelation(unresolvedRelation.tableIdentifier, unresolvedRelation)
      }
  }


  def planWithAvailableIndex(plan: LogicalPlan): Boolean = {

    //Get filters and escape projects to check if plan could be resolved using Indexes
    @tailrec
    def helper(filtersConditions: Seq[Expression], actual: LogicalPlan): Boolean = actual match {

      case logical.Filter(condition, child: LogicalPlan) =>
        helper(filtersConditions :+ condition, child)

      case p@logical.Project(_, child: LogicalPlan) =>
        helper(filtersConditions, child)

      case u: UnresolvedRelation =>
        //Check if table has index and if there are some Filter that have all its attributes indexed
        catalog.indexMetadataByTableIdentifier(u.tableIdentifier).map { index =>
          filtersConditions exists { condition =>
            IndexUtils.areAllAttributeIndexedInExpr(condition, index.indexedCols)
          }
        } getOrElse {
          false
        }

      case _ =>
        false
    }

    helper(Seq.empty, plan)
  }


}


/**
  * TODO Spark2.0 remove rule once Spark supports qualified table names
  */
case class ResolveReferencesXD(conf: CatalystConf) extends Rule[LogicalPlan] {

  def resolver: Resolver = {
    if (conf.caseSensitiveAnalysis) {
      caseSensitiveResolution
    } else {
      caseInsensitiveResolution
    }
  }

  def apply(plan: LogicalPlan): LogicalPlan = plan resolveOperators {
    // return the same logicalPlan when catalyst applies specific changes in ResolveReferences rule
    case p: LogicalPlan if !p.childrenResolved => p
    case p @ Project(projectList, child) if containsStar(projectList) => p
    case t: ScriptTransformation if containsStar(t.input) => t
    case a: Aggregate if containsStar(a.aggregateExpressions) => a
    case j @ Join(left, right, _, _) if !j.selfJoinResolved => j
    case s @ Sort(ordering, global, child) if child.resolved && !s.resolved => s
    case g @ Generate(generator, join, outer, qualifier, output, child)
      if child.resolved && !generator.resolved => g

    case q: LogicalPlan =>
      q transformExpressionsUp  {
        case u @ UnresolvedAttribute(nameParts) =>
          withPosition(u) {
            import XDLogicalPlanFunctions._
            q.resolveUsingChildren(nameParts, resolver).getOrElse(u)
          }
      }
  }

  def containsStar(exprs: Seq[Expression]): Boolean =
  exprs.exists(_.collect { case _: Star => true }.nonEmpty)

}