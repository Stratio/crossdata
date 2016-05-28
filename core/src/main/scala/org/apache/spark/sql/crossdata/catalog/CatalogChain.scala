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
package org.apache.spark.sql.crossdata.catalog

import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.catalyst.{CatalystConf, TableIdentifier}
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.crossdata.catalog.XDCatalog.{CrossdataTable, ViewIdentifier}

import scala.collection.SeqView

/*
  Write through (always true for this class)-> Each write is synchronously done to all catalogs in the chain
  No-Write allocate (always true) -> A miss at levels 0...i-1,i isn't written to these levels when found at level i+1
 */
class CatalogChain(
    firstLevel: XDCatalog,
    fallbackCatalogs: XDCatalog*
  ) extends XDCatalogWithPersistence {

  private implicit def crossdataTable2tableIdentifier(xdTable: CrossdataTable): TableIdentifier =
    TableIdentifier(xdTable.tableName, xdTable.dbName)

  val catalogs = (firstLevel +: fallbackCatalogs)

  private def chainedLookup[R](lookup: XDCatalog => Option[R]): Option[R] =
    catalogs.view map(lookup) collectFirst {
      case Some(res) => res
    }

  private def persistentCatalogsView: SeqView[PersistentCatalog, Seq[_]] = catalogs.view collect {
    case c: PersistentCatalog => c
  }

  // Persistence interface

  override def persistTable(crossdataTable: CrossdataTable, table: LogicalPlan): Unit = {
    registerTable(crossdataTable, table)
    persistentCatalogsView foreach (_.persistTable(crossdataTable, table))
  }

  override def persistView(viewIdentifier: ViewIdentifier, plan: LogicalPlan, sqlText: String): Unit = {
    registerView(viewIdentifier, plan)
    persistentCatalogsView foreach (_.persistView(viewIdentifier, plan, sqlText))
  }

  override def dropTable(tableIdentifier: TableIdentifier): Unit = {
    if (!tableExists(tableIdentifier)) throw new RuntimeException("Table can't be deleted because it doesn't exists")
    logInfo(s"Deleting table ${tableIdentifier.unquotedString}from catalog")
    unregisterTable(tableIdentifier)
    persistentCatalogsView foreach (_.dropTable(tableIdentifier))
  }

  override def dropView(viewIdentifier: ViewIdentifier): Unit = {
    if (!tableExists(viewIdentifier)) throw new RuntimeException("View can't be deleted because it doesn't exists")
    logInfo(s"Deleting table ${viewIdentifier.unquotedString} from catalog")
    unregisterView(viewIdentifier)
    persistentCatalogsView foreach (_.dropView(viewIdentifier))
  }

  override def dropAllTables(): Unit = {
    dropAllViews
    unregisterAllTables
    persistentCatalogsView foreach (_.dropAllTables)
  }

  override def dropAllViews(): Unit = {
    unregisterAllViews
    persistentCatalogsView foreach (_.dropAllViews)
  }

  // Catalog interface

  override def registerTable(tableIdent: TableIdentifier, plan: LogicalPlan): Unit =
    catalogs foreach (_.registerTable(tableIdent, plan))
  override def registerView(viewIdentifier: ViewIdentifier, plan: LogicalPlan): Unit =
    catalogs foreach (_.registerView(viewIdentifier, plan))

  override def unregisterTable(tableIdent: TableIdentifier): Unit = catalogs foreach (_.unregisterTable(tableIdent))
  override def unregisterView(viewIdent: ViewIdentifier): Unit = catalogs foreach (_.unregisterView(viewIdent))

  override def unregisterAllTables(): Unit = catalogs foreach(_.unregisterAllTables)
  override def unregisterAllViews(): Unit = catalogs.foreach(_.unregisterAllViews)

  private def lookupRelationOpt(tableIdent: TableIdentifier, alias: Option[String]): Option[LogicalPlan] =
    chainedLookup { catalog: XDCatalog =>
      if(catalog tableExists tableIdent) Some(catalog.lookupRelation(tableIdent, alias)) else None
    }

  override def lookupRelation(tableIdent: TableIdentifier, alias: Option[String]): LogicalPlan =
    lookupRelationOpt(tableIdent, alias) get


  override def getTables(databaseName: Option[String]): Seq[(String, Boolean)] =
    (Map.empty[String, Boolean] /: (catalogs flatMap (_.getTables(databaseName)))) {
      case (res, entry: (String, Boolean) @ unchecked) =>
        if(res.get(entry._1).getOrElse(true)) res + entry else res
    } toSeq

  override def refreshTable(tableIdent: TableIdentifier): Unit = ()

  override def tableExists(tableIdent: TableIdentifier): Boolean =
    lookupRelationOpt(tableIdent, None) isDefined


  override protected[crossdata] def persistTableMetadata(crossdataTable: CrossdataTable): Unit =
    persistentCatalogsView foreach (_.persistTableMetadata(crossdataTable))

  override protected[crossdata] def persistViewMetadata(tableIdentifier: ViewIdentifier, sqlText: String): Unit =
    persistentCatalogsView foreach (_.persistViewMetadata(tableIdentifier, sqlText))

  /**
    * Check the connection to the set Catalog
    */
  override def checkConnectivity: Boolean = catalogs.forall(_.checkConnectivity)


  override val xdContext: XDContext = catalogs.head.xdContext
  override val conf: CatalystConf = firstLevel.conf //TODO: Own config?

}
