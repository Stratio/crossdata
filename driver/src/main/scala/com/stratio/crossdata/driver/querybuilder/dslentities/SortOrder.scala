package com.stratio.crossdata.driver.querybuilder.dslentities

import com.stratio.crossdata.driver.querybuilder.{CrossdataSQLStatement, Expression}

abstract sealed class SortDirection
case object Ascending extends SortDirection
case object Descending extends SortDirection

object SortOrder {
  def apply(expression: Expression, direction: SortDirection = Ascending): SortOrder =
    new SortOrder(expression, Some(direction))
  def apply(order: String): SortOrder = SortOrder(XDQLStatement(order))
}

class SortOrder private(val expression: Expression, val direction: Option[SortDirection] = None) extends Expression {
  override def toXDQL: String = ???
}


case class SortCriteria(global: Boolean, expressions: Seq[SortOrder]) extends CrossdataSQLStatement {
  override def toXDQL: String = ???
}
