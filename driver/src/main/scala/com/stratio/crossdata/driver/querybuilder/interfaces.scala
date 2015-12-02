package com.stratio.crossdata.driver.querybuilder

import com.stratio.crossdata.driver.querybuilder.dslentities.{Join, JoinType}

trait CrossdataSQLStatement{
  def toXDQL: String
}

trait Expression extends CrossdataSQLStatement

trait BinaryExpression extends Expression{
  val left: Expression
  val right: Expression
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