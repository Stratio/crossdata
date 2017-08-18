package org.apache.spark.sql.crossdata.catalyst.analysis

import org.apache.spark.sql.catalyst.analysis.{UnresolvedAttribute, UnresolvedException}
import org.apache.spark.sql.catalyst.expressions.{Expression, Unevaluable}
import org.apache.spark.sql.types.DataType

case class PostponedAttribute(unresolvedAttribute: UnresolvedAttribute) extends Expression with Unevaluable {
  override def nullable: Boolean = throw new UnresolvedException(this, "nullable")

  override def dataType: DataType = throw new UnresolvedException(this, "dataType")

  override def children: Seq[Expression] = Seq()

  override lazy val resolved: Boolean = false
}

