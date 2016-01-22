package org.apache.spark.sql.crossdata.catalyst.analysis

import org.apache.spark.sql.catalyst.analysis._
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.catalyst.rules._

// SELECT sth as alias GROUP BY alias is allowed thanks to the rule
object ResolveAggregateAlias extends Rule[LogicalPlan] {

  def apply(plan: LogicalPlan): LogicalPlan = plan resolveOperators {
    case p: LogicalPlan if !p.childrenResolved => p

    case a@Aggregate(grouping, aggregateExp, child) if child.resolved && !a.resolved && groupingExpressionsContainAlias(grouping, aggregateExp) =>
      val newGrouping = grouping.map { groupExpression =>
        groupExpression transformUp {
          case u@UnresolvedAttribute(Seq(aliasCandidate)) =>
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
      case u@UnresolvedAttribute(Seq(aliasCandidate)) =>
        aggregateExpressionsContainAliasReference(aliasCandidate)
      case _ => false
    }
  }

  // Catalyst config cannot be read, so the most restrictive resolver is used
  val resolver: Resolver = caseSensitiveResolution

}
