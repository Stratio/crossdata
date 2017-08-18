package org.apache.spark.sql.crossdata.catalyst.planning

import com.stratio.crossdata.connector.NativeFunctionExecutor
import org.apache.spark.sql.catalyst.expressions.PredicateHelper
import org.apache.spark.sql.catalyst.expressions.NamedExpression
import org.apache.spark.sql.catalyst.expressions.Expression
import org.apache.spark.sql.catalyst.expressions.Attribute
import org.apache.spark.sql.catalyst.expressions.Alias
import org.apache.spark.sql.catalyst.expressions.AttributeReference
import org.apache.spark.sql.catalyst.plans.logical.Aggregate
import org.apache.spark.sql.catalyst.plans.logical.Filter
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.catalyst.plans.logical.Project
import org.apache.spark.sql.crossdata.catalyst.EvaluateNativeUDF
import org.apache.spark.sql.execution.datasources.LogicalRelation
import org.apache.spark.sql.sources.CatalystToCrossdataAdapter
import org.apache.spark.sql.sources.CatalystToCrossdataAdapter.CrossdataExecutionPlan

/**
  * WARNING: The extended physical operation only match when the base relation is an instance of [[NativeFunctionExecutor]]
  * Otherwise, delegate to the next strategy
  */
object ExtendedPhysicalOperation extends PredicateHelper {
  type ReturnType = (Seq[NamedExpression], Seq[Expression], LogicalPlan, CrossdataExecutionPlan)

  def unapply(plan: LogicalPlan): Option[ReturnType] = {
    val (fields, filters, child, _) = collectProjectsAndFilters(plan)
    val projects = fields.getOrElse(child.output)

    child match {
      case LogicalRelation(t: NativeFunctionExecutor, _) =>
        Some((projects, filters, child, CatalystToCrossdataAdapter.getConnectorLogicalPlan(plan, projects, filters))) // TODO it does not apply
      case _ =>
        None
    }
  }

  def collectProjectsAndFilters(plan: LogicalPlan):
  (Option[Seq[NamedExpression]], Seq[Expression], LogicalPlan, Map[Attribute, Expression]) =
    plan match {
      case Project(fields, child) if fields.forall(_.deterministic) =>
        val (_, filters, other, aliases) = collectProjectsAndFilters(child)
        val substitutedFields = fields.map(substitute(aliases)).asInstanceOf[Seq[NamedExpression]]
        (Some(substitutedFields), filters, other, collectAliases(substitutedFields))

      case Filter(condition, child) if condition.deterministic =>
        val (fields, filters, other, aliases) = collectProjectsAndFilters(child)
        val substitutedCondition = substitute(aliases)(condition)
        (fields, filters ++ splitConjunctivePredicates(substitutedCondition), other, aliases)

      case EvaluateNativeUDF(udf, child, attribute) =>
        collectProjectsAndFilters(child)

      case Aggregate(groupingExpressions, aggregateExpression, child) =>
        collectProjectsAndFilters(child)

      case other =>
        (None, Nil, other, Map.empty)
    }

  def collectAliases(fields: Seq[Expression]): Map[Attribute, Expression] = fields.collect {
    case a @ Alias(child, _) => a.toAttribute -> child
  }.toMap

  def substitute(aliases: Map[Attribute, Expression])(expr: Expression): Expression = {
    expr.transform {
      case a @ Alias(ref: AttributeReference, name) =>
        aliases.get(ref).map(Alias(_, name)(a.exprId, a.qualifiers)).getOrElse(a)

      case a: AttributeReference =>
        aliases.get(a).map(Alias(_, a.name)(a.exprId, a.qualifiers)).getOrElse(a)
    }
  }
}