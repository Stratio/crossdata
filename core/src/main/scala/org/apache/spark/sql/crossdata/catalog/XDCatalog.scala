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

import org.apache.spark.sql.catalyst.analysis.{Catalog, UnresolvedRelation}
import org.apache.spark.sql.catalyst.plans.logical.{LogicalPlan, Subquery}
import org.apache.spark.sql.catalyst.{CatalystConf, SimpleCatalystConf, TableIdentifier}
import org.apache.spark.sql.crossdata.catalog.XDCatalog.{CrossdataTable, ViewIdentifier}
import org.apache.spark.sql.crossdata.execution.datasources.StreamingRelation
import org.apache.spark.sql.crossdata.serializers.CrossdataSerializer
import org.apache.spark.sql.crossdata.{CrossdataVersion, XDContext}
import org.apache.spark.sql.execution.datasources.{LogicalRelation, ResolvedDataSource}
import org.apache.spark.sql.sources.{HadoopFsRelationProvider, RelationProvider, SchemaRelationProvider}
import org.apache.spark.sql.types._
import org.json4s.jackson.Serialization._

import scala.collection.mutable


/**
 * CrossdataCatalog aims to provide a mechanism to persist the
 * [[org.apache.spark.sql.catalyst.analysis.Catalog]] metadata.
 */
abstract class XDCatalog(val conf: CatalystConf = new SimpleCatalystConf(true),
                         xdContext: XDContext) extends Catalog with CatalogCommon with Serializable {
  // TODO should we use a cache?
  val tables = new mutable.HashMap[String, LogicalPlan]()

  override def tableExists(tableIdentifier: TableIdentifier): Boolean = {
    if (tables.get(getTableName(tableIdentifier)).isDefined) {
      true
    } else {
      lookupTable(tableIdentifier).fold(false){ crossdataTable =>
        val logicalPlan: LogicalPlan = createLogicalRelation(crossdataTable)
        registerTable(tableIdentifier, logicalPlan)
        true
      }
    }
  }

  def tableExistsInPersistedCatalog(tableIdentifier: TableIdentifier): Boolean =
    lookupTable(tableIdentifier).isDefined


  override def getTables(databaseName: Option[String]): Seq[(String, Boolean)] = {
    def processDBIdentifier(dbName: String): String = if (conf.caseSensitiveAnalysis) dbName else dbName.toLowerCase
    def tablesInDb(dbName: String): mutable.HashMap[String, LogicalPlan] = tables.filter {
      case (tableIdentifier, _) =>
        tableIdentifier.split("\\.")(0) == dbName
      case _ =>
        false
    }

    val dbName = databaseName.map(processDBIdentifier)
    val cachedTables = dbName.fold {
      tables.map {
        case (tableName, _) => (tableName, true)
      }.toSeq
    } { definedDBName =>
      tablesInDb(definedDBName).map{
        case (tableName, _) => (tableName.split("\\.")(0) + "." + tableName.split("\\.")(1), true)
      }.toSeq
    }
    // persisted tables replace temporary tables
    (cachedTables.toMap ++ listPersistedTables(databaseName).toMap).toSeq
  }



  override def lookupRelation(relationIdentifier: TableIdentifier, alias: Option[String]): LogicalPlan = {

    lookupRelationCache(relationIdentifier, alias).getOrElse {
      logInfo(s"XDCatalog: Looking up table ${relationIdentifier.unquotedString}")

      lookupTable(relationIdentifier) match {
        case Some(crossdataTable) =>
          val table: LogicalPlan = createLogicalRelation(crossdataTable)
          registerTable(relationIdentifier, table)
          processAlias(relationIdentifier, table, alias)

        case None =>
          log.debug(s"Table Not Found: ${relationIdentifier.unquotedString}")
          lookupView(relationIdentifier) match {
            case Some(sqlView) =>
              val viewPlan: LogicalPlan = xdContext.sql(sqlView).logicalPlan
              registerView(relationIdentifier, viewPlan)
              processAlias(relationIdentifier, viewPlan, alias)

            case None =>
              log.debug(s"View Not Found: ${relationIdentifier.unquotedString}")
              xdContext.streamingCatalog match {
                // TODO PoC => handle exceptions
                case Some(streamingCatalog) if streamingCatalog.existsEphemeralTable(getTableName(relationIdentifier)) =>
                  StreamingRelation(getTableName(relationIdentifier))
                case _ =>
                  sys.error(s"Table/View Not Found: ${relationIdentifier.unquotedString}")
              }
          }
      }
    }
  }

  def lookupRelationCache(tableIdentifier: TableIdentifier, alias: Option[String]): Option[LogicalPlan] = {
    val tableFullName = getTableName(tableIdentifier)
    val tableOpt = tables.get(tableFullName)
    tableOpt.fold[Option[LogicalPlan]] {
      None
    } { table =>
      Some( processAlias(tableIdentifier, table, alias))
    }
  }

  private def processAlias( tableIdentifier: TableIdentifier, lPlan: LogicalPlan, alias: Option[String]) = {
    val tableWithQualifiers = Subquery(getTableName(tableIdentifier), lPlan)
    // If an alias was specified by the lookup, wrap the plan in a subquery so that attributes are
    // properly qualified with this alias.
    alias.map(a => Subquery(a, tableWithQualifiers)).getOrElse(tableWithQualifiers)
  }

  /**
   * Get the table name of TableIdentifier for temporary tables.
   */
  override protected def getTableName(tableIdent: TableIdentifier): String =
    if (conf.caseSensitiveAnalysis) {
      tableIdent.unquotedString
    } else {
      tableIdent.unquotedString.toLowerCase
    }
  

  override def registerTable(tableIdentifier: TableIdentifier, plan: LogicalPlan): Unit =
    tables.put(getTableName(tableIdentifier), plan)


  override def unregisterTable(tableIdentifier: TableIdentifier): Unit =
    tables remove getTableName(tableIdentifier)


  override def unregisterAllTables(): Unit =
    tables.clear()

  override def refreshTable(tableIdentifier: TableIdentifier): Unit = {
    throw new UnsupportedOperationException
  }

  protected[crossdata] def createLogicalRelation(crossdataTable: CrossdataTable): LogicalRelation = {

    /** Although table schema is inferred and persisted in XDCatalog, the schema can't be specified in some cases because
    the source does not implement SchemaRelationProvider (e.g. JDBC) */

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

  // Defined by Crossdata

  final def persistTable(crossdataTable: CrossdataTable, table: LogicalPlan): Unit = {
    val tableIdentifier = TableIdentifier(crossdataTable.tableName, crossdataTable.dbName)

    if (tableExists(tableIdentifier)){
      logWarning(s"The table $tableIdentifier already exists")
      throw new UnsupportedOperationException(s"The table $tableIdentifier already exists")
    } else {
      logInfo(s"XDCatalog: Persisting table ${crossdataTable.tableName}")
      persistTableMetadata(crossdataTable.copy(schema = Option(table.schema)))
      val tableIdentifier = TableIdentifier(crossdataTable.tableName,crossdataTable.dbName)
      registerTable(tableIdentifier, table)
    }
  }



  final def persistView(tableIdentifier: ViewIdentifier, plan: LogicalPlan, sqlText: String) = {
    def checkPlan(plan: LogicalPlan): Unit = {
      plan collect {
        case UnresolvedRelation(tIdent, _) => tIdent
      } foreach { tIdent =>
        if (!tableExistsInPersistedCatalog(tIdent)) {
          throw new RuntimeException("Views only can be created with a previously persisted table")
        }
      }
    }
    checkPlan(plan)
    if (tableExists(tableIdentifier)){
      val msg = s"The view ${tableIdentifier.unquotedString} already exists"
      logWarning(msg)
      throw new UnsupportedOperationException(msg)
    } else {
      logInfo(s"XDCatalog: Persisting view ${tableIdentifier.unquotedString}")
      persistViewMetadata(tableIdentifier: TableIdentifier, sqlText)
      registerView(tableIdentifier, plan)
    }
  }

  final def dropTable(tableIdentifier: TableIdentifier): Unit = {
    if (!tableExists(tableIdentifier)) throw new RuntimeException("Table can't be deleted because it doesn't exists")
    logInfo(s"XDCatalog: Deleting table ${tableIdentifier.unquotedString}from catalog")
    unregisterTable(tableIdentifier)
    dropPersistedTable(tableIdentifier)
  }

  final def dropAllTables(): Unit = {
    logInfo("XDCatalog: Drop all tables from catalog")
    unregisterAllTables()
    dropAllPersistedTables()
  }

  final def dropView(viewIdentifier: ViewIdentifier): Unit = {
    if (!tableExists(viewIdentifier)) throw new RuntimeException("View can't be deleted because it doesn't exists")
    logInfo(s"XDCatalog: Deleting table ${viewIdentifier.unquotedString} from catalog")
    unregisterTable(viewIdentifier)
    dropPersistedView(viewIdentifier)
  }

  final def dropAllViews(): Unit = {
    logInfo("XDCatalog: Drop all tables from catalog")
    dropAllPersistedViews()
  }


  def registerView(tableIdentifier: ViewIdentifier, plan: LogicalPlan) =
    registerTable(tableIdentifier, plan)

  protected def lookupView(viewIdentifier: ViewIdentifier): Option[String]

  protected def lookupTable(tableIdentifier: TableIdentifier): Option[CrossdataTable]

  def listPersistedTables(databaseName: Option[String]): Seq[(String, Boolean)]

  // TODO protected catalog
  protected[crossdata] def persistTableMetadata(crossdataTable: CrossdataTable): Unit

  protected[crossdata] def persistViewMetadata(tableIdentifier: ViewIdentifier, sqlText: String): Unit

  /**
   * Drop table(s)/view(s) if exists.
   */
  protected def dropPersistedTable(tableIdentifier: TableIdentifier): Unit

  protected def dropAllPersistedTables(): Unit

  protected def dropPersistedView(viewIdentifier: ViewIdentifier): Unit

  protected def dropAllPersistedViews(): Unit


  /**
   * Check the connection to the set Catalog
   */
  def checkConnectivity: Boolean

}

object XDCatalog extends CrossdataSerializer {

  implicit def asXDCatalog(catalog: Catalog): XDCatalog = catalog.asInstanceOf[XDCatalog]

  type ViewIdentifier = TableIdentifier
  
  case class CrossdataTable(tableName: String, dbName: Option[String],  schema: Option[StructType],
                            datasource: String, partitionColumn: Array[String] = Array.empty,
                            opts: Map[String, String] = Map.empty , crossdataVersion: String = CrossdataVersion)

  def serializeSchema(schema: StructType): String = write(schema)

  def deserializeUserSpecifiedSchema(schemaJSON: String): StructType = read[StructType](schemaJSON)

  def serializePartitionColumn(partitionColumn: Array[String]): String = write(partitionColumn)

  def deserializePartitionColumn(partitionColumn: String): Array[String] = read[Array[String]](partitionColumn)

  def serializeOptions(options: Map[String, String]): String =  write(options)

  def deserializeOptions(optsJSON: String): Map[String, String] = read[Map[String, String]](optsJSON)

  def requireSchema() = throw new RuntimeException("the schema must be non empty")

}