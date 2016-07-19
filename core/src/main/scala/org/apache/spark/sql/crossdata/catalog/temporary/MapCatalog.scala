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

import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.catalyst.{CatalystConf, TableIdentifier}
import org.apache.spark.sql.crossdata.catalog.TableIdentifierNormalized
import org.apache.spark.sql.crossdata.catalog.XDCatalog.{CrossdataTable, ViewIdentifier, ViewIdentifierNormalized}
import org.apache.spark.sql.crossdata.catalog.interfaces.XDTemporaryCatalog

import scala.collection.mutable

abstract class MapCatalog(catalystConf: CatalystConf) extends XDTemporaryCatalog {

  // TODO map with catalog/tableIdentifier

  protected def newMap: mutable.Map[String, LogicalPlan]

  private val tables: mutable.Map[String, LogicalPlan] = newMap
  private val views: mutable.Map[String, LogicalPlan] = newMap

  implicit def tableIdent2string(tident: TableIdentifierNormalized): String = normalizeTableName(tident)

  override def relation(tableIdent: TableIdentifierNormalized)(implicit sqlContext: SQLContext): Option[LogicalPlan] =
    (tables get tableIdent) orElse (views get tableIdent)

  override def allRelations(databaseName: Option[String]): Seq[TableIdentifierNormalized] = {
    val dbName = databaseName.map(normalizeIdentifier)
    (tables ++ views).toSeq collect {
      case (k, _) if dbName.map(_ == k.split("\\.")(0)).getOrElse(true) =>
        k.split("\\.") match {
          case Array(db, tb) => TableIdentifierNormalized(tb, Option(db))
          case Array(tb) => TableIdentifierNormalized(tb)
        }
    }
  }

  override def saveTable(
                          tableIdentifier: TableIdentifierNormalized,
                          plan: LogicalPlan,
                          crossdataTable: Option[CrossdataTable] = None): Unit = {

    views get tableIdentifier foreach (_ => dropView(tableIdentifier))
    tables put(normalizeTableName(tableIdentifier), plan)
  }

  override def saveView(
                         viewIdentifier: ViewIdentifierNormalized,
                         plan: LogicalPlan,
                         query: Option[String] = None): Unit = {
    tables get viewIdentifier foreach (_ => dropTable(viewIdentifier))
    views put(normalizeTableName(viewIdentifier), plan)
  }

  override def dropView(viewIdentifier: ViewIdentifierNormalized): Unit =
    views remove normalizeTableName(viewIdentifier)

  override def dropAllViews(): Unit = views clear

  override def dropAllTables(): Unit = tables clear

  override def dropTable(tableIdentifier: TableIdentifierNormalized): Unit =
    tables remove normalizeTableName(tableIdentifier)

}
