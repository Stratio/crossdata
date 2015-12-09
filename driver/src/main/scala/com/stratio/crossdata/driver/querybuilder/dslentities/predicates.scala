package com.stratio.crossdata.driver.querybuilder.dslentities

import com.stratio.crossdata.driver.querybuilder.{Predicate, BinaryExpression, Expression}

// Logical predicates

case class And(left: Expression, right: Expression) extends BinaryExpression
  with Predicate {

  override val tokenStr = "AND"

  override def childExpansion(child: Expression): String = child match {
    case _: And => child.toXDQL
    case _: Literal => child.toXDQL
    case _ => s"(${child.toXDQL})"
  }
}

case class Or(left: Expression, right: Expression) extends BinaryExpression
  with Predicate {

  override val tokenStr = "OR"

  override def childExpansion(child: Expression): String = child match {
    case _: Or => child.toXDQL
    case _: Literal => child.toXDQL
    case _ => s"(${child.toXDQL})"
  }

}

// Comparison predicates

case class Equal(left: Expression, right: Expression) extends BinaryExpression //TODO: Review
  with Predicate {

  override val tokenStr: String = "="
  override def childExpansion(child: Expression): String = child match {
    case _: Literal => child.toXDQL
    case _ => s"(${child.toXDQL})"
  }
}

case class LessThan(left: Expression, right: Expression) extends BinaryExpression //TODO: Review
with Predicate {

  override val tokenStr: String = "<"
  override def childExpansion(child: Expression): String = child match {
    case _: Literal => child.toXDQL
    case _ => s"(${child.toXDQL})"
  }
}

case class LessThanOrEqual(left: Expression, right: Expression) extends BinaryExpression //TODO: Review
with Predicate {

  override val tokenStr: String = "<="
  override def childExpansion(child: Expression): String = child match {
    case _: Literal => child.toXDQL
    case _ => s"(${child.toXDQL})"
  }
}

case class GreaterThan(left: Expression, right: Expression) extends BinaryExpression //TODO: Review
with Predicate {

  override val tokenStr: String = ">"
  override def childExpansion(child: Expression): String = child match {
    case _: Literal => child.toXDQL
    case _ => s"(${child.toXDQL})"
  }
}

case class GreaterThanOrEqual(left: Expression, right: Expression) extends BinaryExpression //TODO: Review
with Predicate {

  override val tokenStr: String = ">="
  override def childExpansion(child: Expression): String = child match {
    case _: Literal => child.toXDQL
    case _ => s"(${child.toXDQL})"
  }
}


case class IsNull(expr: Expression) extends Predicate {
  override private[querybuilder] def toXDQL: String = s" ${expr.toXDQL} IS NULL"
}

case class IsNotNull(expr: Expression) extends Predicate {
  override private[querybuilder] def toXDQL: String = s" ${expr.toXDQL} IS NOT NULL"
}