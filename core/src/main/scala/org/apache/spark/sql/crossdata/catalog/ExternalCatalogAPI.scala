package org.apache.spark.sql.crossdata.catalog

import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.crossdata.catalog.XDCatalog.{CrossdataIndex, CrossdataTable, IndexIdentifier, ViewIdentifier}
import org.apache.spark.sql.crossdata.catalog.interfaces.XDAppsCatalog


private[crossdata] trait ExternalCatalogAPI extends XDAppsCatalog{

  def persistTable(crossdataTable: CrossdataTable, table: LogicalPlan): Unit
  def persistView(viewIdentifier: ViewIdentifier, plan: LogicalPlan, sqlText: String): Unit
  def persistIndex(crossdataIndex: CrossdataIndex): Unit

  def dropTable(tableIdentifier: TableIdentifier): Unit
  def dropAllTables(): Unit

  def dropView(viewIdentifier: ViewIdentifier): Unit
  def dropAllViews(): Unit

  def dropIndex(indexIdentifier: IndexIdentifier): Unit //Support multiple index per table?
  def dropAllIndexes(): Unit

  def tableMetadata(tableIdentifier: TableIdentifier): Option[CrossdataTable]
  def indexMetadata(indexIdentifier: IndexIdentifier): Option[CrossdataIndex]
  def indexMetadataByTableIdentifier(tableIdentifier: TableIdentifier): Option[CrossdataIndex]
  def tableHasGlobalIndex(tableIdentifier: TableIdentifier): Boolean =
    indexMetadataByTableIdentifier(tableIdentifier).isDefined
}



