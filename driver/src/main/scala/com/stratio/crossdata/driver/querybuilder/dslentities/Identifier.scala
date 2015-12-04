package com.stratio.crossdata.driver.querybuilder.dslentities

import com.stratio.crossdata.driver.querybuilder.{Relation, Expression}

case class Identifier(id: String) extends Expression with Relation {
  override def toXDQL: String = id
}
