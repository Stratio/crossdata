package org.apache.spark.sql.crossdata.catalog.interfaces

import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.analysis.Catalog
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.crossdata.catalog.XDCatalog._

trait XDCatalogTemporary {

  this: Catalog =>

  override def registerTable(tableIdent: TableIdentifier, plan: LogicalPlan): Unit
  def registerView(viewIdentifier: ViewIdentifier, plan: LogicalPlan): Unit

  override def unregisterTable(tableIdent: TableIdentifier): Unit
  def unregisterView(viewIdent: ViewIdentifier): Unit

  override def unregisterAllTables(): Unit
  def unregisterAllViews(): Unit

}
