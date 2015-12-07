package com.stratio.crossdata.driver.querybuilder

import com.stratio.crossdata.driver.querybuilder.dslentities._
import com.stratio.crossdata.driver.querybuilder.dslentities.SortDirection._


trait CrossdataSQLStatement{
  private[querybuilder] def toXDQL: String
}

object Expression {
  implicit def exp2sortorder(exp: Expression): SortOrder = SortOrder(exp, Ascending)
}

trait Expression extends CrossdataSQLStatement {
  def && (other: Expression): Predicate = And(this, other)
  def || (other: Expression): Predicate = Or(this, other)
  def === (other: Expression): Predicate = Eq(this, other)

  def + (other: Expression): Expression = Add(this, other)

  def ASC: SortOrder = SortOrder(this, Ascending)
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