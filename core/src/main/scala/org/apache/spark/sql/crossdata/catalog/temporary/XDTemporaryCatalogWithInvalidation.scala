/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.spark.sql.crossdata.catalog.temporary

import com.stratio.crossdata.util.CacheInvalidator
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.catalyst.CatalystConf
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.crossdata.catalog.XDCatalog.{CrossdataTable, ViewIdentifier}
import org.apache.spark.sql.crossdata.catalog.interfaces.XDTemporaryCatalog

/**
  * Decorator class providing cache invalidation for non-conservative operations
  *
  * @param underlying Catalog implementation
  * @param invalidator Cache invalidation implementation
  */
class XDTemporaryCatalogWithInvalidation(
                                          underlying: XDTemporaryCatalog,
                                          invalidator: CacheInvalidator
                                        ) extends XDTemporaryCatalog {

  override def saveTable(
                          tableIdentifier: ViewIdentifier,
                          plan: LogicalPlan,
                          crossdataTable: Option[CrossdataTable]): Unit = {
    invalidator.invalidateCache
    underlying.saveTable(tableIdentifier, plan, crossdataTable)
  }

  override def saveView(viewIdentifier: ViewIdentifier, plan: LogicalPlan, query: Option[String]): Unit = {
    invalidator.invalidateCache
    underlying.saveView(viewIdentifier, plan, query)
  }

  override def dropView(viewIdentifier: ViewIdentifier): Unit = {
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

  override def dropTable(tableIdentifier: ViewIdentifier): Unit = {
    invalidator.invalidateCache
    underlying.dropTable(tableIdentifier)
  }

  override def relation(tableIdent: ViewIdentifier)(implicit sqlContext: SQLContext): Option[LogicalPlan] =
    underlying.relation(tableIdent)

  override def catalystConf: CatalystConf = underlying.catalystConf
  override def isAvailable: Boolean = underlying.isAvailable
  override def allRelations(databaseName: Option[String]): Seq[ViewIdentifier] = underlying.allRelations(databaseName)
}
