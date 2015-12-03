package com.stratio.crossdata.driver.querybuilder.dslentities

import com.stratio.crossdata.driver.querybuilder.{Predicate, Expression, Relation}

object JoinType extends Enumeration {
  type JoinType = Value
  val Inner, LeftOuter, RightOuter, FullOuter, LeftSemi = Value
}

import JoinType._

case class Join(left: Relation,
           right: Relation,
           joinType: JoinType,
           condition: Option[Expression] = None) extends Relation {

  def on(condition: String): Relation = on(XDQLStatement(condition))
  def on(condition: Predicate): Relation =
    Join(left, right, joinType, Some(condition))

  override def toXDQL: String = ???
}