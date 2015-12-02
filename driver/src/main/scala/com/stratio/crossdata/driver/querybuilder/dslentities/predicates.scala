package com.stratio.crossdata.driver.querybuilder.dslentities

import com.stratio.crossdata.driver.querybuilder.{Predicate, BinaryExpression, Expression}

case class And(left: Expression, right: Expression) extends BinaryExpression(left, right) with Predicate{

  override def toXDQL: String = Seq(left, right) map(_.toXDQL) mkString " AND "
}
