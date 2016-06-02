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
package org.apache.spark.sql.crossdata.catalog.inmemory

import org.apache.spark.sql.catalyst.{CatalystConf, TableIdentifier}
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.crossdata.catalog.XDCatalog
import XDCatalog.ViewIdentifier
import org.apache.spark.sql.crossdata.catalog.interfaces.XDTemporaryCatalog

import scala.collection.mutable.Map

abstract class MapCatalog(catalystConf: CatalystConf) extends XDTemporaryCatalog {

  // TODO map with catalog/tableIdentifier

  protected def newMap: Map[String, LogicalPlan]

  private val tables: Map[String, LogicalPlan] = newMap
  private val views: Map[String, LogicalPlan] = newMap

  implicit def tableIdent2string(tident: TableIdentifier): String = normalizeTableName(tident)

  override def relation(tableIdent: TableIdentifier, alias: Option[String]): Option[LogicalPlan] =
    (tables get tableIdent) orElse (views get tableIdent) map {
      processAlias(tableIdent, _, alias)
    }

  override def allRelations(databaseName: Option[String]): Seq[TableIdentifier] = {
    val dbName = databaseName.map(normalizeIdentifier)
    (tables ++ views).toSeq collect {
      case (k,_) if dbName.map(_ == k.split("\\.")(0)).getOrElse(true) =>
        k.split("\\.") match {
          case Array(db, tb) => TableIdentifier(tb, Option(tb))
          case Array(tb) => TableIdentifier(tb)       }
    }
  }

  override def saveTable(tableIdentifier: ViewIdentifier, plan: LogicalPlan): Unit = {
    views get tableIdentifier foreach (_ => dropView(tableIdentifier))
    tables put (normalizeTableName(tableIdentifier), plan)
  }

  override def saveView(viewIdentifier: ViewIdentifier, plan: LogicalPlan): Unit = {
    tables get viewIdentifier foreach (_ => dropTable(viewIdentifier))
    views put (normalizeTableName(viewIdentifier), plan)
  }

  override def dropView(viewIdentifier: ViewIdentifier): Unit =
    views remove normalizeTableName(viewIdentifier)

  override def dropAllViews(): Unit = views clear

  override def dropAllTables(): Unit = tables clear

  override def dropTable(tableIdentifier: TableIdentifier): Unit =
    tables remove normalizeTableName(tableIdentifier)

}
