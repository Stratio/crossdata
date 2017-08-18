/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.driver.querybuilder.dslentities

import java.sql.{Date, Timestamp}

import com.stratio.crossdata.driver.querybuilder.Expression

case class Literal(value: Any) extends Expression {
  override private[querybuilder] def toXDQL: String = value match {
    // TODO http://spark.apache.org/docs/latest/sql-programming-guide.html
    case _: String => s"'$value'"
    case _: Date => s"'$value'"
    case _: Timestamp => s"'$value'"
    case _ => value.toString
  }
}
