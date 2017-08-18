package com.stratio.crossdata.driver.querybuilder.dslentities

import com.stratio.crossdata.driver.querybuilder.CrossdataSQLStatement
import com.stratio.crossdata.driver.querybuilder.Expression

object SortDirection extends Enumeration {
  type SortDirection = Value
  val Ascending = Value("ASC")
  val Descending = Value("DESC")
  val Default = Value("")
}

import com.stratio.crossdata.driver.querybuilder.dslentities.SortDirection._

object SortOrder {
  def apply(expression: Expression, direction: SortDirection = Default): SortOrder =
    new SortOrder(expression, Some(direction))

  def apply(order: String): SortOrder = SortOrder(XDQLStatement(order))

}


class SortOrder private(val expression: Expression,
                        val direction: Option[SortDirection] = None) extends Expression {
  override private[querybuilder] def toXDQL: String = s"${expression.toXDQL} ${direction.getOrElse("")}"
}


case class SortCriteria(global: Boolean, expressions: Seq[SortOrder]) extends CrossdataSQLStatement {
  override private[querybuilder] def toXDQL: String =
    (if (global) "ORDER" else "SORT") + " BY " + expressions.map(_.toXDQL).mkString(", ")
}
