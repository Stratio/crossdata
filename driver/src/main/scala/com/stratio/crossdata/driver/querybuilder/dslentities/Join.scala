package com.stratio.crossdata.driver.querybuilder.dslentities

import com.stratio.crossdata.driver.querybuilder.{Predicate, Expression, Relation}

object JoinType extends Enumeration {
  type JoinType = Value
  val Inner      = Value(join())
  val LeftOuter  = Value(outer("LEFT"))
  val RightOuter = Value(outer("RIGHT"))
  val FullOuter  = Value(outer("FULL"))
  val LeftSemi   = Value("LEFT SEMI JOIN")

  private def outer(tpStr: String): String = join(s"$tpStr OUTER ")
  private def join(tpStr: String = ""): String = s"${tpStr}JOIN"
}

import JoinType._

case class Join(left: Relation,
           right: Relation,
           joinType: JoinType,
           condition: Option[Expression] = None) extends Relation {

  def on(condition: String): Relation = on(XDQLStatement(condition))
  def on(condition: Predicate): Relation =
    Join(left, right, joinType, Some(condition))

  override def toXDQL: String =
    s"${left.toXDQL} ${joinType} ${right.toXDQL}" + condition.map(c => s"ON ${c.toXDQL}").getOrElse("")
}