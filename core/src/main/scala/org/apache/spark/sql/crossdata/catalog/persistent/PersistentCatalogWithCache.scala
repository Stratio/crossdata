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
package org.apache.spark.sql.crossdata.catalog.persistent

import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.catalyst.analysis.UnresolvedRelation
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.catalyst.{CatalystConf, TableIdentifier}
import org.apache.spark.sql.crossdata.catalog.XDCatalog
import XDCatalog.{CrossdataIndex, CrossdataTable, IndexIdentifier, ViewIdentifier}
import org.apache.spark.sql.crossdata.catalog.interfaces.XDPersistentCatalog
import org.apache.spark.sql.crossdata.util.CreateRelationUtil

import scala.collection.mutable


/**
  * PersistentCatalog aims to provide a mechanism to persist the
  * [[org.apache.spark.sql.catalyst.analysis.Catalog]] metadata.
  */
abstract class PersistentCatalogWithCache(sqlContext: SQLContext, catalystConf: CatalystConf) extends XDPersistentCatalog
  with Serializable {

  import CreateRelationUtil._

  val tableCache: mutable.Map[TableIdentifier, LogicalPlan] = mutable.Map.empty
  val viewCache: mutable.Map[TableIdentifier, LogicalPlan] = mutable.Map.empty
  val indexCache: mutable.Map[TableIdentifier, CrossdataIndex] = mutable.Map.empty

  override final def relation(relationIdentifier: TableIdentifier, alias: Option[String]): Option[LogicalPlan] =
    // TODO refactor (nonCachedLookup)
    (tableCache get relationIdentifier) orElse (viewCache get relationIdentifier) orElse {
      logInfo(s"PersistentCatalog: Looking up table ${relationIdentifier.unquotedString}")
      lookupTable(relationIdentifier) map { crossdataTable =>
        val table: LogicalPlan = createLogicalRelation(sqlContext, crossdataTable)
        tableCache.put(relationIdentifier, table)
        table
      }
    } orElse {
      log.debug(s"Table Not Found: ${relationIdentifier.unquotedString}")
      lookupView(relationIdentifier).map { sqlView =>
        val viewPlan: LogicalPlan = sqlContext.sql(sqlView).logicalPlan
        viewCache.put(relationIdentifier, viewPlan)
        viewPlan
      }
    } map (processAlias(relationIdentifier, _, alias))

  override final def refreshCache(tableIdent: ViewIdentifier): Unit = tableCache clear

  override final def saveView(viewIdentifier: ViewIdentifier, plan: LogicalPlan, sqlText: String): Unit = {
    def checkPlan(plan: LogicalPlan): Unit = {
      plan collect {
        case UnresolvedRelation(tIdent, _) => tIdent
      } foreach { tIdent =>
        if (relation(tIdent).isEmpty) {
          throw new RuntimeException("Views only can be created with a previously persisted table")
        }
      }
    }

    checkPlan(plan)
    if (relation(viewIdentifier).isDefined) {
      val msg = s"The view ${viewIdentifier.unquotedString} already exists"
      logWarning(msg)
      throw new UnsupportedOperationException(msg)
    } else {
      logInfo(s"Persisting view ${viewIdentifier.unquotedString}")
      viewCache.put(viewIdentifier, plan)
      persistViewMetadata(viewIdentifier, sqlText)
    }
  }

  override final def saveTable(crossdataTable: CrossdataTable, table: LogicalPlan): Unit = {

    val tableIdentifier = TableIdentifier(crossdataTable.tableName, crossdataTable.dbName)
    if (relation(tableIdentifier).isDefined) {
      logWarning(s"The table $tableIdentifier already exists")
      throw new UnsupportedOperationException(s"The table $tableIdentifier already exists")
    } else {
      logInfo(s"Persisting table ${crossdataTable.tableName}")
      tableCache.put(tableIdentifier, table)
      persistTableMetadata(crossdataTable.copy(schema = Option(table.schema)))
    }
  }

  override final def saveIndex(crossdataIndex: CrossdataIndex): Unit = {

    val indexIdentifier = crossdataIndex.indexIdentifier

    if(lookupIndex(indexIdentifier).isDefined) {
      logWarning(s"The index $indexIdentifier already exists")
      throw new UnsupportedOperationException(s"The index $indexIdentifier already exists")
    } else {
      logInfo(s"Persisting index ${crossdataIndex.indexIdentifier}")
      indexCache.put(crossdataIndex.tableIdentifier, crossdataIndex)
      persistIndexMetadata(crossdataIndex)
    }

  }

  override final def dropTable(tableIdentifier: TableIdentifier): Unit = {
    tableCache remove tableIdentifier
    dropTableMetadata(tableIdentifier)
    dropIndexesFromTable(tableIdentifier)
  }

  override final def dropView(viewIdentifier: ViewIdentifier): Unit = {
    viewCache remove viewIdentifier
    dropViewMetadata(viewIdentifier)
  }

  override final def dropIndexesFromTable(tableIdentifier: TableIdentifier): Unit = {
    indexCache remove tableIdentifier
    dropIndexMetadata(tableIdentifier)
  }

  override final def dropIndex(indexIdentifer: IndexIdentifier): Unit = {

    val found: Option[(TableIdentifier, CrossdataIndex)] = indexCache find { case(key,value) => value.indexIdentifier == indexIdentifer}

    if(found.isDefined) indexCache remove found.get._1

    dropIndexMetadata(indexIdentifer)
  }

  override final def tableHasIndex(tableIdentifier: TableIdentifier): Boolean = {
    val found: Option[(TableIdentifier, CrossdataIndex)] = indexCache find { case (key, value) => key == tableIdentifier }
    found.isDefined
  }

  override final def dropAllViews(): Unit = {
    viewCache.clear
    dropAllViewsMetadata()
  }

  override final def dropAllTables(): Unit = {
    tableCache.clear
    dropAllTablesMetadata()
  }

  override final def dropAllIndexes(): Unit = {
    indexCache.clear
    dropAllIndexesMetadata()
  }

  protected def schemaNotFound() = throw new RuntimeException("the schema must be non empty")

  //New Methods


  def lookupView(viewIdentifier: ViewIdentifier): Option[String]

  def persistTableMetadata(crossdataTable: CrossdataTable): Unit

  def persistViewMetadata(tableIdentifier: TableIdentifier, sqlText: String): Unit

  def persistIndexMetadata(crossdataIndex: CrossdataIndex): Unit

  def dropTableMetadata(tableIdentifier: TableIdentifier): Unit

  def dropViewMetadata(viewIdentifier: ViewIdentifier): Unit

  def dropIndexMetadata(indexIdentifier: IndexIdentifier): Unit

  def dropIndexMetadata(tableIdentifier: TableIdentifier): Unit

  def dropAllViewsMetadata(): Unit

  def dropAllTablesMetadata(): Unit

  def dropAllIndexesMetadata(): Unit

}