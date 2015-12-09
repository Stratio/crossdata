package com.stratio.crossdata.driver.querybuilder.dslentities

import java.sql.{Timestamp, Date}

import com.stratio.crossdata.driver.querybuilder.Expression



/**
 *
 * @param value
 */
case class Literal(value: Any) extends Expression{
  override private[querybuilder] def toXDQL: String = value match {
   // TODO http://spark.apache.org/docs/latest/sql-programming-guide.html

    case _ : String => s"'$value'"
    case _: Date => s"'$value'"
    case _: Timestamp =>  s"'$value'"
/*      implicit def bigDecimal2Literal(d: BigDecimal): Literal = Literal(d.underlying())
      implicit def bigDecimal2Literal(d: java.math.BigDecimal): Literal = Literal(d)
      implicit def decimal2Literal(d: Decimal): Literal = Literal(d)
      implicit def binary2Literal(a: Array[Byte]): Literal = Literal(a)*/
    // TODO do type tests
    // implicit def float2Literal(f: Float): Literal = Literal(f)
    // implicit def double2Literal(d: Double): Literal = Literal(d)
    /*
    implicit def boolean2Literal(b: Boolean): Literal = Literal(b)
      implicit def byte2Literal(b: Byte): Literal = Literal(b)
      implicit def short2Literal(s: Short): Literal = Literal(s)
      implicit def int2Literal(i: Int): Literal = Literal(i)
      implicit def long2Literal(l: Long): Literal = Literal(l)*/
    case _ => value.toString
  }
}
