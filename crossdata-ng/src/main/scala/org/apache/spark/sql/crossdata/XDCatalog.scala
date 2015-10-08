/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.spark.sql.crossdata

import org.apache.spark.sql.catalyst.analysis.Catalog
import org.apache.spark.sql.catalyst.plans.logical.{LogicalPlan, Subquery}
import org.apache.spark.sql.catalyst.{CatalystConf, SimpleCatalystConf}


import scala.collection.mutable

/**
 * CrossdataCatalog aims to provide a mechanism to persist the
 * [[org.apache.spark.sql.catalyst.analysis.Catalog]] metadata.
 */
abstract class XDCatalog(val conf: CatalystConf = new SimpleCatalystConf(true)) extends Catalog with Serializable {

  // TODO see Spark 1.5 and JIRA associated

  val tables = new mutable.HashMap[String, LogicalPlan]()

  override def tableExists(tableIdentifier: Seq[String]): Boolean = {
    val tableIdent = processTableIdentifier(tableIdentifier)
    tables.get(getDbTableName(tableIdent)) match {
      case Some(_) => true
      case None => false
    }
  }

  override def getTables(databaseName: Option[String]): Seq[(String, Boolean)] = {
    val dbName = if (conf.caseSensitiveAnalysis) {
      databaseName
    } else {
      if (databaseName.isDefined) Some(databaseName.get.toLowerCase) else None
    }
    (dbName.fold{
      tables.map{
        case (tableName, _) => {
          (tableName, true)}
      }.toSeq
    }{ realDBName =>
      tables.filter {
        case (tableIdentifier, _) => {
          tableIdentifier.split("\\.")(0) == realDBName
        }
        case _ => false
      }.map {
        case (tableName, _) => (tableName.split("\\.")(0)+"."+tableName.split("\\.")(1), true)
      }.toSeq
    }++getPersistenceTables(databaseName)).distinct



  }

  def lookupRelationCache(tableIdentifier: Seq[String], alias: Option[String]): Option[LogicalPlan] = {
    val tableIdent = processTableIdentifier(tableIdentifier)
    val tableFullName = getDbTableName(tableIdent)
    val tableOpt = tables.get(tableFullName)
    tableOpt.fold[Option[LogicalPlan]]{
      None
    } { table =>
      val tableWithQualifiers = Subquery(tableIdent.last, table)
      // If an alias was specified by the lookup, wrap the plan in a subquery so that attributes are
      // properly qualified with this alias.
      Some(alias.map(a => Subquery(a, tableWithQualifiers)).getOrElse(tableWithQualifiers))
    }
  }

  override def registerTable(tableIdentifier: Seq[String], plan: LogicalPlan): Unit = {
    val tableIdent = processTableIdentifier(tableIdentifier)
    tables.put(getDbTableName(tableIdent), plan)
  }

  override def unregisterTable(tableIdentifier: Seq[String]): Unit = {
    val tableIdent = processTableIdentifier(tableIdentifier)
    tables remove getDbTableName(tableIdent)
  }

  override def unregisterAllTables(): Unit = tables.clear()

  override def refreshTable(databaseName: String, tableName: String): Unit = {
    throw new UnsupportedOperationException
  }

  def getPersistenceTables(databaseName: Option[String]): Seq[(String, Boolean)]

  def persistTable(tableIdentifier: Seq[String], crossdataCatalog: CrossdataTable): Unit

  def dropTable(tableIdentifier: Seq[String]): Unit

  def dropAllTables(): Unit
}
