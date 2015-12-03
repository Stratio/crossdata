package com.stratio.crossdata.driver.querybuilder.dslentities

import com.stratio.crossdata.driver.querybuilder.Predicate

case class XDQLStatement(queryStr: String) extends Predicate {
  override def toXDQL: String = queryStr
}
