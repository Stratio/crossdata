package com.stratio.crossdata.driver.querybuilder

import com.stratio.crossdata.driver.querybuilder.dslentities.And
import com.stratio.crossdata.driver.querybuilder.dslentities.GreaterThan
import com.stratio.crossdata.driver.querybuilder.dslentities.GreaterThanOrEqual
import com.stratio.crossdata.driver.querybuilder.dslentities.IsNotNull
import com.stratio.crossdata.driver.querybuilder.dslentities.IsNull
import com.stratio.crossdata.driver.querybuilder.dslentities.LessThan
import com.stratio.crossdata.driver.querybuilder.dslentities.LessThanOrEqual
import com.stratio.crossdata.driver.querybuilder.dslentities.Or
import com.stratio.crossdata.driver.querybuilder.dslentities.SortDirection.Ascending
import com.stratio.crossdata.driver.querybuilder.dslentities.SortDirection.Descending
import com.stratio.crossdata.driver.querybuilder.dslentities.SortOrder
import com.stratio.crossdata.driver.querybuilder.dslentities._


trait CrossdataSQLStatement{
  private[querybuilder] def toXDQL: String
}

object Expression {
  implicit def exp2sortorder(exp: Expression): SortOrder = SortOrder(exp, Ascending)
}

trait Expression extends CrossdataSQLStatement {

  def unary_- : Expression = Minus(this)
  def unary_! : Predicate = Not(this)

  def + (other: Expression): Expression = Add(this, other)
  def - (other: Expression): Expression = Subtract(this, other)
  def * (other: Expression): Expression = Multiply(this, other)
  def / (other: Expression): Expression = Divide(this, other)
  def % (other: Expression): Expression = Remainder(this, other)

  def && (other: Expression): Predicate = And(this, other)
  def || (other: Expression): Predicate = Or(this, other)

  def < (other: Expression): Predicate = LessThan(this, other)
  def <= (other: Expression): Predicate = LessThanOrEqual(this, other)
  def > (other: Expression): Predicate = GreaterThan(this, other)
  def >= (other: Expression): Predicate = GreaterThanOrEqual(this, other)
  def === (other: Expression): Predicate = Equal(this, other)

  def asc: SortOrder = SortOrder(this, Ascending)
  def desc: SortOrder = SortOrder(this, Descending)


  def in(list: Expression*): Expression = In(this, list :_*)

  def like(other: Expression): Expression = Like(this, other)


  def isNull: Predicate = IsNull(this)
  def isNotNull: Predicate = IsNotNull(this)


  def as(alias: String): Expression = SelectExpressionAlias(this, alias)
  def as(alias: Symbol): Expression = SelectExpressionAlias(this, alias.name)

}

trait BinaryExpression extends Expression{
  val left: Expression
  val right: Expression

  val tokenStr: String
  def childExpansion(child: Expression): String = child.toXDQL

  override private[querybuilder] def toXDQL: String = Seq(left, right) map childExpansion mkString s" $tokenStr "

}

trait UnaryExpression extends Expression{
  val child: Expression
  val tokenStr: String

  def childExpansion(child: Expression): String = child.toXDQL
  override private[querybuilder] def toXDQL: String = s" $tokenStr ${childExpansion(child)}"
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