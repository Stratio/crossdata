/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package org.apache.spark.sql.crossdata.catalog.temporary

import org.apache.spark.sql.catalyst.CatalystConf
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan

import scala.collection.mutable

class HashmapCatalog(override val catalystConf: CatalystConf) extends MapCatalog(catalystConf) {

  override protected def newMap: mutable.Map[String, LogicalPlan] = new mutable.HashMap[String, LogicalPlan]

  override def isAvailable: Boolean = true
}