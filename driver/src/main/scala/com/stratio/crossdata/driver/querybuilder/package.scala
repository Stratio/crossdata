package com.stratio.crossdata.driver

import java.sql.{Date, Timestamp}

import com.stratio.crossdata.driver.querybuilder.dslentities.{Identifier, Literal, XDQLStatement}
import org.apache.spark.sql.types.Decimal

package object querybuilder {

    def select(projections: Expression*): ProjectedSelect = new ProjectedSelect(projections:_*)
    def select(projections: String): ProjectedSelect = select(XDQLStatement(projections))
    def selectAll: ProjectedSelect = new ProjectedSelect(AsteriskExpression())

    //def createTempView(name: String): ViewStatement = new ViewStatement()
    //def createTable
    //def importTable

    //Literals
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
    implicit def decimal2Literal(d: Decimal): Literal = Literal(d)
    implicit def timestamp2Literal(t: Timestamp): Literal = Literal(t)
    implicit def binary2Literal(a: Array[Byte]): Literal = Literal(a)

    //Identifiers
    implicit def symbol2Identifier(s: Symbol): Identifier = Identifier(s.name)

    //Expression Operators
    // TODO use implicits?
    def distinct(e: Expression*): Expression = Distinct(e:_*)
    def sum(e: Expression): Expression = Sum(e)
    def sumDistinct(e: Expression): Expression = SumDistinct(e)
    def count(e: Expression): Expression = Count(e)
    def countDistinct(e: Expression*): Expression = CountDistinct(e:_*)
    def approxCountDistinct(e: Expression, rsd: Double): Expression = ApproxCountDistinct(e, rsd)
    def avg(e: Expression): Expression = Avg(e)
    def min(e: Expression): Expression = Min(e)
    def max(e: Expression): Expression = Max(e)
    def abs(e: Expression): Expression = Abs(e)


}
