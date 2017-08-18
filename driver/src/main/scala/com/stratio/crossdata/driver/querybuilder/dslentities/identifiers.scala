package com.stratio.crossdata.driver.querybuilder.dslentities

import com.stratio.crossdata.driver.querybuilder.{CrossdataSQLStatement, Expression, Relation}

sealed trait Identifier extends Expression with Relation

/**
 * Identifier for tables and columns
 */
case class EntityIdentifier(id: String) extends Identifier {
  override private[querybuilder] def toXDQL: String = id
}

case class AliasIdentifier(underlyingEntity: CrossdataSQLStatement, alias: String) extends Identifier {
  override private[querybuilder] def toXDQL: String = s" ${underlyingEntity.toXDQL} AS $alias"
}