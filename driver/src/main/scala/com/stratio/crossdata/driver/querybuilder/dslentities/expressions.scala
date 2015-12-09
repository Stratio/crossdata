/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.crossdata.driver.querybuilder.dslentities

import com.stratio.crossdata.driver.querybuilder.{BinaryExpression, Expression, Predicate, UnaryExpression}


case class AsteriskExpression() extends Expression {
  override private[querybuilder] def toXDQL: String = "*"
}

case class Minus(child: Expression) extends UnaryExpression {
  override val tokenStr: String = " -"

  override def childExpansion(child: Expression): String = child match {
    case _: Literal | _: Identifier => child.toXDQL
    case _ => s"(${child.toXDQL})"
  }
}

case class Not(child: Expression) extends Predicate {
  override private[querybuilder] def toXDQL: String = child match {
    case _: Predicate => s"(${child.toXDQL})"
    case _: Expression => s" !${child.toXDQL}} "
    case _ => s" !(${child.toXDQL})} "
  }
}

case class Add(left: Expression, right: Expression) extends BinaryExpression {

  override val tokenStr = "+"

  // TODO review operator precedence
  override def childExpansion(child: Expression): String = child match {
    case _: Add => child.toXDQL
    case _: Literal | _: Identifier => child.toXDQL
    case _ => s"(${child.toXDQL})"
  }
}

case class Subtract(left: Expression, right: Expression) extends BinaryExpression {
  override val tokenStr = "-"

  override def childExpansion(child: Expression): String = child match {
    case _: Literal | _: Identifier => child.toXDQL
    case _ => s"(${child.toXDQL})"
  }
}

case class Multiply(left: Expression, right: Expression) extends BinaryExpression {
  override val tokenStr = "*"

  override def childExpansion(child: Expression): String = child match {
    case _: Literal | _: Identifier => child.toXDQL
    case _ => s"(${child.toXDQL})"
  }
}

case class Divide(left: Expression, right: Expression) extends BinaryExpression {
  override val tokenStr = "/"

  override def childExpansion(child: Expression): String = child match {
    case _: Literal | _: Identifier => child.toXDQL
    case _ => s"(${child.toXDQL})"
  }
}

case class Remainder(left: Expression, right: Expression) extends BinaryExpression {
  override val tokenStr = "%"

  override def childExpansion(child: Expression): String = child match {
    case _: Literal | _: Identifier => child.toXDQL
    case _ => s"(${child.toXDQL})"
  }
}

// Other cases
case class In(left: Expression, right: Expression*) extends Expression {
  override private[querybuilder] def toXDQL: String = s" ${left.toXDQL} IN ${right map (_.toXDQL) mkString("(", ",", ")")}"
}

case class Like(left: Expression, right: Expression) extends BinaryExpression {
  override val tokenStr = "LIKE"
}

//Select expressions
case class Distinct(expr: Expression*) extends Expression {
  override private[querybuilder] def toXDQL: String = s" DISTINCT ${expr.map(_.toXDQL) mkString ","}"
}

case class Sum(expr: Expression) extends Expression {
  override private[querybuilder] def toXDQL: String = s" sum(${expr.toXDQL})"
}

case class SumDistinct(expr: Expression) extends Expression {
  override private[querybuilder] def toXDQL: String = s" sum( DISTINCT ${expr.toXDQL})"
}

case class Count(expr: Expression) extends Expression {
  override private[querybuilder] def toXDQL: String = s" count(${expr.toXDQL})"
}

case class CountDistinct(expr: Expression*) extends Expression {
  override private[querybuilder] def toXDQL: String = s" count( DISTINCT ${expr.map(_.toXDQL) mkString ","})"
}

case class ApproxCountDistinct(expr: Expression, rsd: Double) extends Expression {
  override private[querybuilder] def toXDQL: String = s" APPROXIMATE ($rsd) count ( DISTINCT ${expr.toXDQL})"
}

case class Avg(expr: Expression) extends Expression {
  override private[querybuilder] def toXDQL: String = s" avg(${expr.toXDQL})"
}

case class Min(expr: Expression) extends Expression {
  override private[querybuilder] def toXDQL: String = s" min(${expr.toXDQL})"
}

case class Max(expr: Expression) extends Expression {
  override private[querybuilder] def toXDQL: String = s" max(${expr.toXDQL})"
}

case class Abs(expr: Expression) extends Expression {
  override private[querybuilder] def toXDQL: String = s" abs(${expr.toXDQL})"
}