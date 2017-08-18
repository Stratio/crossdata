package org.apache.spark.sql.crossdata.catalog.temporary

import com.stratio.crossdata.util.CacheInvalidator
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.catalyst.CatalystConf
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.crossdata.catalog.{StringNormalized, TableIdentifierNormalized}
import org.apache.spark.sql.crossdata.catalog.XDCatalog.{CrossdataTable, ViewIdentifier, ViewIdentifierNormalized}
import org.apache.spark.sql.crossdata.catalog.interfaces.XDTemporaryCatalog

/**
  * Decorator class providing cache invalidation for non-conservative operations
  *
  * @param underlying Catalog implementation
  * @param invalidator Cache invalidation implementation
  */
class XDTemporaryCatalogWithInvalidation(
                                          val underlying: XDTemporaryCatalog,
                                          invalidator: CacheInvalidator
                                        ) extends XDTemporaryCatalog {

  override def saveTable(
                          tableIdentifier: ViewIdentifierNormalized,
                          plan: LogicalPlan,
                          crossdataTable: Option[CrossdataTable]): Unit = {
    invalidator.invalidateCache
    underlying.saveTable(tableIdentifier, plan, crossdataTable)
  }

  override def saveView(viewIdentifier: ViewIdentifierNormalized, plan: LogicalPlan, query: Option[String]): Unit = {
    invalidator.invalidateCache
    underlying.saveView(viewIdentifier, plan, query)
  }

  override def dropView(viewIdentifier: ViewIdentifierNormalized): Unit = {
    invalidator.invalidateCache
    underlying.dropView(viewIdentifier)
  }

  override def dropAllViews(): Unit = {
    invalidator.invalidateCache
    underlying.dropAllViews()
  }

  override def dropAllTables(): Unit = {
    invalidator.invalidateCache
    underlying.dropAllTables()
  }

  override def dropTable(tableIdentifier: ViewIdentifierNormalized): Unit = {
    invalidator.invalidateCache
    underlying.dropTable(tableIdentifier)
  }

  override def relation(tableIdent: ViewIdentifierNormalized)(implicit sqlContext: SQLContext): Option[LogicalPlan] =
    underlying.relation(tableIdent)

  override def catalystConf: CatalystConf = underlying.catalystConf
  override def isAvailable: Boolean = underlying.isAvailable
  override def allRelations(databaseName: Option[StringNormalized]): Seq[TableIdentifierNormalized] = underlying.allRelations(databaseName)
}
