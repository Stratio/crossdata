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
import org.apache.spark.sql.crossdata.catalog.XDCatalog.ViewIdentifier
import org.apache.spark.sql.crossdata.execution.datasources.StreamingRelation
import org.apache.spark.sql.crossdata.models._

/**
 * CrossdataStreamingCatalog aims to provide a mechanism to persist the
 * Streaming metadata executions.
 */
abstract class XDStreamingCatalog(override val xdContext: XDContext) extends XDCatalog
  with CatalogCommon with Serializable {


  /** XDCatalog methods.
    *
    * All state changing XDCatalog operations have neutral implementations since
    * this catalog should only be modified by Stream catalog operations
    *
   */
  override def registerView(viewIdentifier: ViewIdentifier, plan: LogicalPlan): Unit = ()
  override def unregisterAllViews(): Unit = ()
  override def unregisterView(viewIdentifier: ViewIdentifier): Unit = ()
  override def unregisterAllTables(): Unit = ()
  override def unregisterTable(tableIdent: TableIdentifier): Unit = ()
  override def registerTable(tableIdent: TableIdentifier, plan: LogicalPlan): Unit = ()
  override def refreshTable(tableIdent: TableIdentifier): Unit = ()

  // Ephemeral tables will not be taken into account when listing all registered or persisted tables
  override def getTables(databaseName: Option[String]): Seq[(String, Boolean)] = Seq.empty

  override def tableExists(tableIdent: TableIdentifier): Boolean = existsEphemeralTable(getTableName(tableIdent))

  override def lookupRelation(tableIdent: TableIdentifier, alias: Option[String]): LogicalPlan =
    StreamingRelation(getTableName(tableIdent))

  /**
   * Ephemeral Table Functions
   */
  def existsEphemeralTable(tableIdentifier: String): Boolean
  
  def getEphemeralTable(tableIdentifier: String) : Option[EphemeralTableModel]

  def getAllEphemeralTables : Seq[EphemeralTableModel]

  def createEphemeralTable(ephemeralTable: EphemeralTableModel): Either[String, EphemeralTableModel]

  def dropEphemeralTable(tableIdentifier: String): Unit

  def dropAllEphemeralTables(): Unit

  /**
   * Ephemeral Status Functions
   */
  protected[crossdata] def createEphemeralStatus(tableIdentifier: String, ephemeralStatusModel: EphemeralStatusModel): EphemeralStatusModel

  protected[crossdata] def getEphemeralStatus(tableIdentifier: String) : Option[EphemeralStatusModel]

  protected[crossdata] def getAllEphemeralStatuses : Seq[EphemeralStatusModel]

  protected[crossdata] def updateEphemeralStatus(tableIdentifier: String, status: EphemeralStatusModel) : Unit

  protected[crossdata] def dropEphemeralStatus(tableIdentifier: String): Unit
  
  protected[crossdata] def dropAllEphemeralStatus(): Unit

  /**
   * Ephemeral Queries Functions
   */
  def existsEphemeralQuery(queryAlias: String): Boolean

  def getEphemeralQuery(queryAlias: String) : Option[EphemeralQueryModel]

  def getAllEphemeralQueries : Seq[EphemeralQueryModel]

  def createEphemeralQuery(ephemeralQuery: EphemeralQueryModel): Either[String, EphemeralQueryModel]

  def dropEphemeralQuery(queryAlias: String): Unit

  def dropAllEphemeralQueries(): Unit

}