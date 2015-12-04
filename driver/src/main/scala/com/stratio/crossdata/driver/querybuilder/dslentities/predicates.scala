package com.stratio.crossdata.driver.querybuilder.dslentities

import com.stratio.crossdata.driver.querybuilder.{Predicate, BinaryExpression, Expression}

// Logical predicates

case class And(left: Expression, right: Expression) extends BinaryExpression
  with Predicate {

  override val tokenStr = "AND"

  override def childExpansion(child: Expression): String = child match {
    case _: And => child.toXDQL
    case _ => s"(${child.toXDQL})"
  }
}

case class Or(left: Expression, right: Expression) extends BinaryExpression
  with Predicate {

  override val tokenStr = "OR"

  override def childExpansion(child: Expression): String = child match {
    case _: Or => child.toXDQL
    case _ => s"(${child.toXDQL})"
  }

}

// Comparison predicates

case class Eq(left: Expression, right: Expression) extends BinaryExpression //TODO: Review
  with Predicate {
  override val tokenStr: String = "="
  override private[querybuilder] def toXDQL: String = s"${left.toXDQL} = ${right.toXDQL}"
}
