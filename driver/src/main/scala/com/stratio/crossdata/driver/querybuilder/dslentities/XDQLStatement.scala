package com.stratio.crossdata.driver.querybuilder.dslentities

import com.stratio.crossdata.driver.querybuilder.{Relation, Predicate}

case class XDQLStatement(queryStr: String) extends Predicate with Relation{
  override private[querybuilder] def toXDQL: String = queryStr
}
