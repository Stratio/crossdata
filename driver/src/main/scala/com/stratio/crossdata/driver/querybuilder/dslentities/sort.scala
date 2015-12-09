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

import com.stratio.crossdata.driver.querybuilder.{CrossdataSQLStatement, Expression}

object SortDirection extends Enumeration {
  type SortDirection = Value
  val Ascending = Value("ASC")
  val Descending = Value("DESC")
}

import SortDirection._

object SortOrder {
  def apply(expression: Expression, direction: SortDirection = Ascending): SortOrder =
    new SortOrder(expression, Some(direction))

  def apply(order: String): SortOrder = SortOrder(XDQLStatement(order))
}

class SortOrder private(val expression: Expression,
                        val direction: Option[SortDirection] = None) extends Expression {
  override private[querybuilder] def toXDQL: String = s"${expression.toXDQL} ${direction.getOrElse("")}"
}


case class SortCriteria(global: Boolean, expressions: Seq[SortOrder]) extends CrossdataSQLStatement {
  override private[querybuilder] def toXDQL: String =
    (if (global) "ORDER" else "SORT") + " BY " + expressions.map(_.toXDQL).mkString(", ")
}
