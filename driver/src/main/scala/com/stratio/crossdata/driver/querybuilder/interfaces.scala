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
package com.stratio.crossdata.driver.querybuilder

import com.stratio.crossdata.driver.querybuilder.dslentities.SortDirection.{Ascending, Descending}
import com.stratio.crossdata.driver.querybuilder.dslentities._

import scala.language.implicitConversions

trait CrossdataSQLStatement {
  private[querybuilder] def toXDQL: String
}

object Expression {
  implicit def exp2sortorder(exp: Expression): SortOrder = SortOrder(exp, Ascending)
}

trait Expression extends CrossdataSQLStatement {

  def unary_- : Expression = Minus(this)

  def unary_! : Predicate = Not(this)

  def +(other: Expression): Expression = Add(this, other)

  def -(other: Expression): Expression = Subtract(this, other)

  def *(other: Expression): Expression = Multiply(this, other)

  def /(other: Expression): Expression = Divide(this, other)

  def %(other: Expression): Expression = Remainder(this, other)

  def &&(other: Expression): Predicate = And(this, other)

  def ||(other: Expression): Predicate = Or(this, other)

  def <(other: Expression): Predicate = LessThan(this, other)

  def <=(other: Expression): Predicate = LessThanOrEqual(this, other)

  def >(other: Expression): Predicate = GreaterThan(this, other)

  def >=(other: Expression): Predicate = GreaterThanOrEqual(this, other)

  def ===(other: Expression): Predicate = Equal(this, other)

  def asc: SortOrder = SortOrder(this, Ascending)

  def desc: SortOrder = SortOrder(this, Descending)


  def in(list: Expression*): Expression = In(this, list: _*)

  def like(other: Expression): Expression = Like(this, other)


  def isNull: Predicate = IsNull(this)

  def isNotNull: Predicate = IsNotNull(this)


  def as(alias: String): Identifier = AliasIdentifier(this, alias)

  def as(alias: Symbol): Identifier = AliasIdentifier(this, alias.name)

}

trait Predicate extends Expression


trait Relation extends CrossdataSQLStatement {

  def join(other: Relation): Join = innerJoin(other)

  def innerJoin(other: Relation): Join = Join(this, other, JoinType.Inner)

  def leftOuterJoin(other: Relation): Join = Join(this, other, JoinType.LeftOuter)

  def rightOuterJoin(other: Relation): Join = Join(this, other, JoinType.RightOuter)

  def fullOuterJoin(other: Relation): Join = Join(this, other, JoinType.FullOuter)

  def leftSemiJoin(other: Relation): Join = Join(this, other, JoinType.LeftSemi)
}


trait UnaryExpression extends Expression {

  val child: Expression
  val tokenStr: String

  def childExpansion(child: Expression): String = child.toXDQL

  override private[querybuilder] def toXDQL: String =
    s" $tokenStr ${childExpansion(child)}"
}

trait BinaryExpression extends Expression {

  val left: Expression
  val right: Expression

  val tokenStr: String

  def childExpansion(child: Expression): String = child.toXDQL

  override private[querybuilder] def toXDQL: String =
    Seq(left, right) map childExpansion mkString s" $tokenStr "

}