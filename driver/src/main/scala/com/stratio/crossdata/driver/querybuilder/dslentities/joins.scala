/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.driver.querybuilder.dslentities

import com.stratio.crossdata.driver.querybuilder.{Expression, Predicate, Relation}

object JoinType extends Enumeration {
  type JoinType = Value
  val Inner = Value(join())
  val LeftOuter = Value(outer("LEFT"))
  val RightOuter = Value(outer("RIGHT"))
  val FullOuter = Value(outer("FULL"))
  val LeftSemi = Value(join("LEFT SEMI"))

  private def outer(tpStr: String): String = join(s" $tpStr OUTER ")

  private def join(tpStr: String = ""): String = s" ${tpStr} JOIN"
}

import JoinType._

case class Join(private val left: Relation,
                private val right: Relation,
                private val joinType: JoinType,
                private val condition: Option[Expression] = None) extends Relation {

  def on(condition: String): Relation = on(XDQLStatement(condition))

  def on(condition: Predicate): Relation =
    Join(left, right, joinType, Some(condition))

  override private[querybuilder] def toXDQL: String =
    s"${left.toXDQL} $joinType ${right.toXDQL}" + condition.map(c => s" ON ${c.toXDQL}").getOrElse("")
}