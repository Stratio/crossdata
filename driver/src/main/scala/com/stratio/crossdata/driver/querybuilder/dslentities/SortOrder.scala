package com.stratio.crossdata.driver.querybuilder.dslentities


import com.stratio.crossdata.driver.querybuilder.{CrossdataSQLStatement, Expression}

object SortDirection extends Enumeration {
  type SortDirection = Value
  val Ascending = Value("ASC")
  val Descending = Value("DESC")
}
/*abstract sealed class SortDirection
case object Ascending extends SortDirection
case object Descending extends SortDirection*/

object SortOrder {

  def apply(expression: Expression,
            direction: SortDirection.SortDirection = SortDirection.Ascending): SortOrder =
    new SortOrder(expression, Some(direction))

  def apply(order: String): SortOrder = SortOrder(XDQLStatement(order))
}

class SortOrder private(val expression: Expression,
                        val direction: Option[SortDirection.SortDirection] = None) extends Expression {
  override def toXDQL: String = s"${expression.toXDQL} ${direction.getOrElse("")}"
}


case class SortCriteria(global: Boolean, expressions: Seq[SortOrder]) extends CrossdataSQLStatement {
  override def toXDQL: String = {if(global) "ORDER" else "SORT"} + " BY " + expressions.map(_.toXDQL).mkString(", ")
}
