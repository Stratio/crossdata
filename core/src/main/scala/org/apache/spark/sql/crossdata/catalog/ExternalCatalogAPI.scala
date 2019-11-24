/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
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



