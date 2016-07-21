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
package org.apache.spark.sql.crossdata.catalog.interfaces

import com.stratio.common.utils.components.logger.impl.SparkLoggerComponent
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.catalyst.plans.logical.{LogicalPlan, Subquery}
import org.apache.spark.sql.catalyst.{CatalystConf, TableIdentifier}
import org.apache.spark.sql.crossdata.catalog.{IndexIdentifierNormalized, StringNormalized, TableIdentifierNormalized, XDCatalog}
import XDCatalog.{CrossdataApp, CrossdataIndex, CrossdataTable, IndexIdentifier, ViewIdentifier, ViewIdentifierNormalized}
import org.apache.spark.sql.crossdata.models.{EphemeralQueryModel, EphemeralStatusModel, EphemeralTableModel}

object XDCatalogCommon {

  implicit class RichTableIdentifier(tableIdentifier: TableIdentifier) {
    def normalize(implicit conf: CatalystConf): TableIdentifierNormalized = {
      val normalizedDatabase = tableIdentifier.database.map(normalizeIdentifier(_,conf))
      TableIdentifierNormalized(normalizeIdentifier(tableIdentifier.table, conf), normalizedDatabase)
    }
  }

  implicit class RichIndexIdentifier(indexIdentifier: IndexIdentifier) {
    def normalize(implicit conf: CatalystConf): IndexIdentifierNormalized = {
      val normalizedIndexName = normalizeIdentifier(indexIdentifier.indexName, conf)
      val normalizedIndexType = normalizeIdentifier(indexIdentifier.indexType, conf)
      IndexIdentifierNormalized(normalizedIndexType, normalizedIndexName)
    }
  }

  def stringifyTableIdentifierNormalized(tableIdent: TableIdentifierNormalized): String =
    tableIdent.unquotedString

  def normalizeTableIdentifier(tableIdent: TableIdentifier, conf: CatalystConf): String =
    stringifyTableIdentifierNormalized(tableIdent.normalize(conf))


  def normalizeIdentifier(identifier: String, conf: CatalystConf): String =
    if (conf.caseSensitiveAnalysis) {
      identifier
    } else {
      identifier.toLowerCase
    }

  def processAlias(tableIdentifier: TableIdentifier, lPlan: LogicalPlan, alias: Option[String])(conf: CatalystConf) = {
    val tableWithQualifiers = Subquery(normalizeTableIdentifier(tableIdentifier, conf), lPlan)
    // If an alias was specified by the lookup, wrap the plan in a subquery so that attributes are
    // properly qualified with this alias.
    alias.map(a => Subquery(a, tableWithQualifiers)).getOrElse(tableWithQualifiers)
  }
}

sealed trait XDCatalogCommon extends SparkLoggerComponent {

  def catalystConf: CatalystConf

  def relation(tableIdent: TableIdentifierNormalized)(implicit sqlContext: SQLContext): Option[LogicalPlan]

  def allRelations(databaseName: Option[StringNormalized] = None): Seq[TableIdentifierNormalized]

  def isAvailable: Boolean

  protected def notFound(resource: String) = {
    val message = s"$resource not found"
    logWarning(message)
    throw new RuntimeException(message)
  }


}

trait XDTemporaryCatalog extends XDCatalogCommon {

  def saveTable(
                 tableIdentifier: TableIdentifierNormalized,
                 plan: LogicalPlan,
                 crossdataTable: Option[CrossdataTable] = None): Unit

  def saveView(
                viewIdentifier: ViewIdentifierNormalized,
                plan: LogicalPlan,
                query: Option[String] = None): Unit

  def dropTable(tableIdentifier: TableIdentifierNormalized): Unit

  def dropView(viewIdentifier: ViewIdentifierNormalized): Unit

  def dropAllTables(): Unit

  def dropAllViews(): Unit

}


trait XDPersistentCatalog extends XDCatalogCommon {

  def refreshCache(tableIdent: TableIdentifierNormalized): Unit

  def saveTable(crossdataTable: CrossdataTable, plan: LogicalPlan)(implicit sqlContext: SQLContext): Unit

  def saveView(tableIdentifier: ViewIdentifierNormalized, plan: LogicalPlan, sqlText: String)(implicit sqlContext: SQLContext): Unit

  def saveIndex(crossdataIndex: CrossdataIndex): Unit

  def dropTable(tableIdentifier: TableIdentifierNormalized): Unit

  def dropView(viewIdentifier: ViewIdentifierNormalized): Unit

  def dropIndex(indexIdentifier: IndexIdentifierNormalized): Unit

  def tableHasIndex(tableIdentifier: TableIdentifierNormalized): Boolean =
    lookupIndexByTableIdentifier(tableIdentifier).isDefined

  def dropIndexesFromTable(tableIdentifier: TableIdentifierNormalized): Unit

  def dropAllTables(): Unit

  def dropAllViews(): Unit

  def dropAllIndexes(): Unit

  def lookupTable(tableIdentifier: TableIdentifierNormalized): Option[CrossdataTable]

  def lookupIndex(indexIdentifier: IndexIdentifierNormalized): Option[CrossdataIndex] //TODO: Index operations to trait

  def lookupIndexByTableIdentifier(tableIdentifier: TableIdentifierNormalized): Option[CrossdataIndex]

  def getApp(alias: String): Option[CrossdataApp]

  def saveAppMetadata(crossdataApp: CrossdataApp): Unit

}

trait XDAppsCatalog {

  def lookupApp(alias: String): Option[CrossdataApp]

  def persistAppMetadata(crossdataApp: CrossdataApp): Unit

}

trait XDStreamingCatalog extends XDCatalogCommon {

  //TODO: TableIdentifier shouldn't be a String

  /**
   * Ephemeral Table Functions
   */
  def existsEphemeralTable(tableIdentifier: String): Boolean

  def getEphemeralTable(tableIdentifier: String): Option[EphemeralTableModel]

  def getAllEphemeralTables: Seq[EphemeralTableModel]

  def createEphemeralTable(ephemeralTable: EphemeralTableModel): Either[String, EphemeralTableModel]

  def dropEphemeralTable(tableIdentifier: String): Unit

  def dropAllEphemeralTables(): Unit

  /**
   * Ephemeral Status Functions
   */
  protected[crossdata] def createEphemeralStatus(tableIdentifier: String, ephemeralStatusModel: EphemeralStatusModel): EphemeralStatusModel

  protected[crossdata] def getEphemeralStatus(tableIdentifier: String): Option[EphemeralStatusModel]

  protected[crossdata] def getAllEphemeralStatuses: Seq[EphemeralStatusModel]

  protected[crossdata] def updateEphemeralStatus(tableIdentifier: String, status: EphemeralStatusModel): Unit

  protected[crossdata] def dropEphemeralStatus(tableIdentifier: String): Unit

  protected[crossdata] def dropAllEphemeralStatus(): Unit

  /**
   * Ephemeral Queries Functions
   */
  def existsEphemeralQuery(queryAlias: String): Boolean

  def getEphemeralQuery(queryAlias: String): Option[EphemeralQueryModel]

  def getAllEphemeralQueries: Seq[EphemeralQueryModel]

  def createEphemeralQuery(ephemeralQuery: EphemeralQueryModel): Either[String, EphemeralQueryModel]

  def dropEphemeralQuery(queryAlias: String): Unit

  def dropAllEphemeralQueries(): Unit

}
