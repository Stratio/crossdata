package com.stratio.crossdata.driver.querybuilder

case class Add(left: Expression, right: Expression) extends BinaryExpression {

  override val tokenStr = "+"

  // TODO review operator precedence
  override def childExpansion(child: Expression): String = child match {
    case _: Add => child.toXDQL
    case _ => s"(${child.toXDQL})"
  }
}
