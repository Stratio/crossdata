/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
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