package org.apache.spark.sql.crossdata.catalog.inmemory

import org.apache.spark.sql.catalyst.{CatalystConf, SimpleCatalystConf}
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.crossdata.XDContext

import scala.collection.mutable
import scala.collection.mutable.HashMap

class HashmapCatalog(
                      val conf: CatalystConf = new SimpleCatalystConf(true),
                      override val xdContext: XDContext
                    ) extends MapCatalog {


  override protected def newMap: mutable.Map[String, LogicalPlan] = new HashMap[String, LogicalPlan]

  override def checkConnectivity: Boolean = true
}