package org.apache.spark.sql.crossdata.catalog

import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.analysis.Catalog
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.crossdata.catalog.XDCatalog.{CrossdataTable, ViewIdentifier}
import org.apache.spark.sql.execution.datasources.LogicalRelation

object XDCatalogWithPersistence {
  implicit def asPersistentCatalog(catalog: Catalog): XDCatalogWithPersistence = catalog.asInstanceOf[XDCatalogWithPersistence]
}

trait XDCatalogWithPersistence extends XDCatalog {

  def persistTable(crossdataTable: CrossdataTable, table: LogicalPlan): Unit
  def persistView(tableIdentifier: ViewIdentifier, plan: LogicalPlan, sqlText: String): Unit

  def dropTable(tableIdentifier: TableIdentifier): Unit
  def dropAllTables(): Unit

  def dropView(viewIdentifier: ViewIdentifier): Unit
  def dropAllViews(): Unit

  protected[crossdata] def persistTableMetadata(crossdataTable: CrossdataTable): Unit
  protected[crossdata] def persistViewMetadata(tableIdentifier: ViewIdentifier, sqlText: String): Unit


}
