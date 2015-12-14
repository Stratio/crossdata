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