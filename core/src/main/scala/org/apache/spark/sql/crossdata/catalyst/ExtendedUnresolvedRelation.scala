package org.apache.spark.sql.crossdata.catalyst

import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.expressions.Attribute
import org.apache.spark.sql.catalyst.plans.logical.{LogicalPlan, UnaryNode}

case class ExtendedUnresolvedRelation(tableIdentifier: TableIdentifier, child: LogicalPlan) extends UnaryNode {
  override def output: Seq[Attribute] = child.output
}
