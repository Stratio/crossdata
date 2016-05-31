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

import org.apache.spark.sql.catalyst.{CatalystConf, TableIdentifier}
import org.apache.spark.sql.catalyst.analysis.Catalog
import org.apache.spark.sql.catalyst.plans.logical.{LogicalPlan, Subquery}
import org.apache.spark.sql.crossdata._
import org.apache.spark.sql.crossdata.catalog.XDCatalog.{CrossdataTable, ViewIdentifier}
import org.apache.spark.sql.crossdata.catalog.interfaces._
import org.apache.spark.sql.crossdata.models.{EphemeralQueryModel, EphemeralStatusModel, EphemeralTableModel}
import org.apache.spark.sql.execution.datasources.{LogicalRelation, ResolvedDataSource}
import org.apache.spark.sql.sources.{HadoopFsRelationProvider, RelationProvider, SchemaRelationProvider}
import org.apache.spark.sql.types.StructType

import scala.collection.SeqView

class XDCatalog(
                 protected val temporary: XDCatalogTemporary with XDCatalogCommon,
                 protected val persistent: XDCatalogPersistence with XDCatalogCommon,
                 protected val streaming: XDCatalogStreaming with XDCatalogCommon
               )(override val conf: CatalystConf) extends Catalog
  with XDCatalogCommon
  with XDCatalogTemporary
  with XDCatalogPersistence
  with XDCatalogStreaming {

  private val catalogs: Seq[XDCatalogCommon] = temporary :: persistent :: streaming :: Nil

  private implicit def crossdataTable2tableIdentifier(xdTable: CrossdataTable): TableIdentifier =
    TableIdentifier(xdTable.tableName, xdTable.dbName)

  //Lookup methods

  // From XDCatalogCommon

  override def relation(tableIdent: ViewIdentifier, alias: Option[String]): Option[LogicalPlan] =
    catalogs.view map (_.relation(tableIdent, alias)) collectFirst {
      case Some(lp) => lp
    }

  override def allRelations(databaseName: Option[String]): Seq[(TableIdentifier, Boolean)] =
    (Map.empty[TableIdentifier, Boolean] /: (catalogs flatMap (_.allRelations(databaseName)))) {
      case (res, entry: (TableIdentifier, Boolean) @ unchecked) =>
        if(res.get(entry._1).getOrElse(true)) res + entry else res
    } toSeq

  // From Catalog

  override def lookupRelation(tableIdent: ViewIdentifier, alias: Option[String]): LogicalPlan =
    relation(tableIdent, alias) get


  override def tableExists(tableIdent: ViewIdentifier): Boolean = contains(tableIdent)

  override def refreshTable(tableIdent: ViewIdentifier): Unit = ???

  // From XDCatalogStreaming

  /**
    * Ephemeral Table Functions
    */
  override def existsEphemeralTable(tableIdentifier: String): Boolean =
    streaming.existsEphemeralTable(tableIdentifier)

  override def getEphemeralTable(tableIdentifier: String): Option[EphemeralTableModel] =
    streaming.getEphemeralTable(tableIdentifier)

  override def getAllEphemeralTables: Seq[EphemeralTableModel] = streaming.getAllEphemeralTables

  override protected[crossdata] def getAllEphemeralStatuses: Seq[EphemeralStatusModel] =
    streaming.getAllEphemeralStatuses

  override protected[crossdata] def getEphemeralStatus(tableIdentifier: String): Option[EphemeralStatusModel] =
    streaming.getEphemeralStatus(tableIdentifier)

  /**
    * Ephemeral Queries Functions
    */
  override def existsEphemeralQuery(queryAlias: String): Boolean = streaming.existsEphemeralQuery(queryAlias)

  override def getEphemeralQuery(queryAlias: String): Option[EphemeralQueryModel] =
    streaming.getEphemeralQuery(queryAlias)

  override def getAllEphemeralQueries: Seq[EphemeralQueryModel] = streaming.getAllEphemeralQueries

  //State change methods

  // From XDCatalogTemporary & Catalog

  override def registerTable(tableIdent: TableIdentifier, plan: LogicalPlan): Unit =
    temporary.registerTable(tableIdent, plan)

  override def unregisterTable(tableIdent: TableIdentifier): Unit = temporary.unregisterTable(tableIdent)

  override def unregisterAllTables(): Unit = temporary.unregisterAllTables

  // From XDCatalogTemporary

  override def registerView(viewIdentifier: ViewIdentifier, plan: LogicalPlan): Unit =
    temporary.registerView(viewIdentifier, plan)

  override def unregisterView(viewIdent: ViewIdentifier): Unit = temporary.unregisterView(viewIdent)

  override def unregisterAllViews(): Unit = temporary.unregisterAllViews()

  // From XDCatalogPersistence

  override def persistTable(crossdataTable: CrossdataTable, table: LogicalPlan): Unit = {
    registerTable(crossdataTable, table)
    persistent.persistTable(crossdataTable, table)
  }

  override def persistView(tableIdentifier: ViewIdentifier, plan: LogicalPlan, sqlText: String): Unit = {
    registerView(tableIdentifier, plan)
    persistent.persistView(tableIdentifier, plan, sqlText)
  }

  override def dropTable(tableIdentifier: TableIdentifier): Unit = {
    temporary relation(tableIdentifier) foreach (temporary.unregisterTable(_))
    persistent dropTable tableIdentifier
  }

  override def dropAllTables(): Unit = {
    allRelations(None) foreach {
      case (ti, false) => unregisterTable(ti)
    }
    persistent dropAllTables
  }

  override def dropView(viewIdentifier: ViewIdentifier): Unit = {
    temporary relation(viewIdentifier) foreach(temporary.unregisterView(_))
    persistent dropView viewIdentifier
  }

  override def dropAllViews(): Unit = {
    allRelations(None) foreach {
      case (ti, false) if isView(ti) => unregisterTable(ti)
    }
    persistent dropAllViews
  }

  override protected[crossdata] def persistTableMetadata(crossdataTable: CrossdataTable): Unit =
    persistent.persistTableMetadata(crossdataTable)

  override protected[crossdata] def persistViewMetadata(tableIdentifier: ViewIdentifier, sqlText: String): Unit =
    persistent.persistViewMetadata(tableIdentifier, sqlText)

  // From XDCatalogStreaming

  override def createEphemeralTable(ephemeralTable: EphemeralTableModel): Either[String, EphemeralTableModel] = ???

  override def dropEphemeralTable(tableIdentifier: String): Unit = ???

  override def dropAllEphemeralTables(): Unit = ???

  /**
    * Ephemeral Status Functions
    */
  override protected[crossdata] def createEphemeralStatus(tableIdentifier: String, ephemeralStatusModel: EphemeralStatusModel): EphemeralStatusModel = ???

  override protected[crossdata] def updateEphemeralStatus(tableIdentifier: String, status: EphemeralStatusModel): Unit = ???

  override protected[crossdata] def dropEphemeralStatus(tableIdentifier: String): Unit = ???

  override protected[crossdata] def dropAllEphemeralStatus(): Unit = ???

  override def createEphemeralQuery(ephemeralQuery: EphemeralQueryModel): Either[String, EphemeralQueryModel] = ???

  override def dropEphemeralQuery(queryAlias: String): Unit = ???

  override def dropAllEphemeralQueries(): Unit = ???


  val xdContext: XDContext

  /*
    /**
      * Get the table name of TableIdentifier for temporary tables.
      */
    override protected def getTableName(tableIdent: TableIdentifier): String =
      if (conf.caseSensitiveAnalysis) {
        tableIdent.unquotedString
      } else {
        tableIdent.unquotedString.toLowerCase
      }

    protected def processAlias( tableIdentifier: TableIdentifier, lPlan: LogicalPlan, alias: Option[String]) = {
      val tableWithQualifiers = Subquery(getTableName(tableIdentifier), lPlan)
      // If an alias was specified by the lookup, wrap the plan in a subquery so that attributes are
      // properly qualified with this alias.
      alias.map(a => Subquery(a, tableWithQualifiers)).getOrElse(tableWithQualifiers)
    }



    //TODO: Possible bad use or design of this at ddl.scala
    protected[crossdata] def createLogicalRelation(crossdataTable: CrossdataTable): LogicalRelation = {

      /** Although table schema is inferred and persisted in XDCatalog, the schema can't be specified in some cases because
        *the source does not implement SchemaRelationProvider (e.g. JDBC) */

      val tableSchema = ResolvedDataSource.lookupDataSource(crossdataTable.datasource).newInstance() match {
        case _: SchemaRelationProvider | _: HadoopFsRelationProvider =>
          crossdataTable.schema
        case _: RelationProvider =>
          None
        case other =>
          val msg = s"Unexpected datasource: $other"
          logError(msg)
          throw new RuntimeException(msg)
      }

      val resolved = ResolvedDataSource(xdContext, tableSchema, crossdataTable.partitionColumn, crossdataTable.datasource, crossdataTable.opts)
      LogicalRelation(resolved.relation)
    }

    /**
      * Check the connection to the set Catalog
      */
    def checkConnectivity: Boolean
    */

}

object XDCatalog {

  type ViewIdentifier = TableIdentifier

  case class CrossdataTable(tableName: String, dbName: Option[String], schema: Option[StructType],
                            datasource: String, partitionColumn: Array[String] = Array.empty,
                            opts: Map[String, String] = Map.empty, crossdataVersion: String = CrossdataVersion)

}
