package org.apache.spark.sql.crossdata.catalog.temporary

import org.apache.spark.sql.catalyst.CatalystConf
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan

import scala.collection.mutable

class HashmapCatalog(override val catalystConf: CatalystConf) extends MapCatalog(catalystConf) {

  override protected def newMap: mutable.Map[String, LogicalPlan] = new mutable.HashMap[String, LogicalPlan]

  override def isAvailable: Boolean = true
}