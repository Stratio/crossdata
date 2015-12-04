package com.stratio.crossdata.driver.querybuilder.dslentities

import com.stratio.crossdata.driver.querybuilder.Expression



/**
 *
 * @param value
 */
case class Literal(value: Any) extends Expression{
  override private[querybuilder] def toXDQL: String = value match {
   // TODO http://spark.apache.org/docs/latest/sql-programming-guide.html
    case _ : String => s"'$value'"
    case _ => value.toString
  }
}
