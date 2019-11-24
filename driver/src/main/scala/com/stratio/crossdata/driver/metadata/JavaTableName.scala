/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.driver.metadata

/**
 * database can be empty ("")
 */
class JavaTableName(val tableName: java.lang.String, val database: java.lang.String) {

  override def equals(other: Any): Boolean = other match {
    case that: JavaTableName =>
      tableName.equals(that.tableName) && database.equals(that.database)
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(tableName, database)
    state.collect {
      case x if x != null => x.hashCode
    }.foldLeft(0)((a, b) => 31 * a + b)
  }

}
