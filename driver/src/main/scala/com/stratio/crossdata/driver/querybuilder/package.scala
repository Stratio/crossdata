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
package com.stratio.crossdata.driver

import java.sql.{Date, Timestamp}

import com.stratio.crossdata.driver.querybuilder.{ProjectedSelect, Expression}
import com.stratio.crossdata.driver.querybuilder.dslentities._

import scala.language.implicitConversions

trait Literals {
  implicit def boolean2Literal(b: Boolean): Literal = Literal(b)
  implicit def byte2Literal(b: Byte): Literal = Literal(b)
  implicit def short2Literal(s: Short): Literal = Literal(s)
  implicit def int2Literal(i: Int): Literal = Literal(i)
  implicit def long2Literal(l: Long): Literal = Literal(l)
  implicit def float2Literal(f: Float): Literal = Literal(f)
  implicit def double2Literal(d: Double): Literal = Literal(d)
  implicit def string2Literal(s: String): Literal = Literal(s)
  implicit def date2Literal(d: Date): Literal = Literal(d)
  implicit def bigDecimal2Literal(d: BigDecimal): Literal = Literal(d.underlying())
  implicit def bigDecimal2Literal(d: java.math.BigDecimal): Literal = Literal(d)
  implicit def timestamp2Literal(t: Timestamp): Literal = Literal(t)
  implicit def binary2Literal(a: Array[Byte]): Literal = Literal(a)
}

trait Identifiers {
  implicit def symbol2Identifier(s: Symbol): Identifier = EntityIdentifier(s.name)
}

trait InitialPhrases {
  def select(projections: Expression*): ProjectedSelect = new ProjectedSelect(projections: _*)

  def select(projections: String): ProjectedSelect = select(XDQLStatement(projections))

  def selectAll: ProjectedSelect = new ProjectedSelect(AsteriskExpression())

  // TODO def createTempView(name: String): ViewStatement = new ViewStatement()
  // TODO def createTable
  // TODO def importTable
  // TODO drop table
  // TODO insertFromSelect
}

trait ExpressionOperators {
  def distinct(e: Expression*): Expression = Distinct(e: _*)

  def sum(e: Expression): Expression = Sum(e)

  def sumDistinct(e: Expression): Expression = SumDistinct(e)

  def count(e: Expression): Expression = Count(e)

  def countDistinct(e: Expression*): Expression = CountDistinct(e: _*)

  def approxCountDistinct(e: Expression, rsd: Double): Expression = ApproxCountDistinct(e, rsd)

  def avg(e: Expression): Expression = Avg(e)

  def min(e: Expression): Expression = Min(e)

  def max(e: Expression): Expression = Max(e)

  def abs(e: Expression): Expression = Abs(e)

  def all: Expression = AsteriskExpression()
}

package object querybuilder extends InitialPhrases
  with Literals
  with Identifiers
  with ExpressionOperators