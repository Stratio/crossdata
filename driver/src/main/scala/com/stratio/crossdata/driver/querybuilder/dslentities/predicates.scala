/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.driver.querybuilder.dslentities

import com.stratio.crossdata.driver.querybuilder.{BinaryExpression, Expression, Predicate}

// Logical predicates
case class And(left: Expression, right: Expression) extends BinaryExpression
with Predicate {

  override val tokenStr = "AND"

  override def childExpansion(child: Expression): String = child match {
    case _: And => child.toXDQL
    case _: Predicate => s"(${child.toXDQL})"
    case _: Expression => child.toXDQL
    case _ => s"(${child.toXDQL})"
  }
}

case class Or(left: Expression, right: Expression) extends BinaryExpression
with Predicate {

  override val tokenStr = "OR"

  override def childExpansion(child: Expression): String = child match {
    case _: Or => child.toXDQL
    case _: Predicate => s"(${child.toXDQL})"
    case _: Expression => child.toXDQL
    case _ => s"(${child.toXDQL})"
  }

}

private[dslentities] trait EqualityCheckers extends BinaryExpression {
  //TODO: Improve management of cases as `x === y === z`
  override def childExpansion(child: Expression): String = child.toXDQL
}

// Comparison predicates
case class Equal(left: Expression, right: Expression) extends EqualityCheckers
with Predicate {
  override val tokenStr: String = "="
}

case class Different(left: Expression, right: Expression) extends EqualityCheckers
with Predicate {
  override val tokenStr: String = "<>"
}

case class LessThan(left: Expression, right: Expression) extends BinaryExpression
with Predicate {

  override val tokenStr: String = "<"

  override def childExpansion(child: Expression): String = child match {
    case _: Expression => child.toXDQL
    case _ => s"(${child.toXDQL})"
  }
}

case class LessThanOrEqual(left: Expression, right: Expression) extends BinaryExpression
with Predicate {

  override val tokenStr: String = "<="

  override def childExpansion(child: Expression): String = child match {
    case _: Expression => child.toXDQL
    case _ => s"(${child.toXDQL})"
  }
}

case class GreaterThan(left: Expression, right: Expression) extends BinaryExpression //TODO: Review
with Predicate {

  override val tokenStr: String = ">"

  override def childExpansion(child: Expression): String = child match {
    case _: Expression => child.toXDQL
    case _ => s"(${child.toXDQL})"
  }
}

case class GreaterThanOrEqual(left: Expression, right: Expression) extends BinaryExpression
with Predicate {

  override val tokenStr: String = ">="

  override def childExpansion(child: Expression): String = child match {
    case _: Expression => child.toXDQL
    case _ => s"(${child.toXDQL})"
  }
}

case class IsNull(expr: Expression) extends Predicate {
  override private[querybuilder] def toXDQL: String = s" ${expr.toXDQL} IS NULL"
}

case class IsNotNull(expr: Expression) extends Predicate {
  override private[querybuilder] def toXDQL: String = s" ${expr.toXDQL} IS NOT NULL"
}

case class In(left: Expression, right: Expression*) extends Expression with Predicate {
  override private[querybuilder] def toXDQL: String = s" ${left.toXDQL} IN ${right map (_.toXDQL) mkString("(", ",", ")")}"
}

case class Like(left: Expression, right: Expression) extends BinaryExpression with Predicate {
  override val tokenStr = "LIKE"
}