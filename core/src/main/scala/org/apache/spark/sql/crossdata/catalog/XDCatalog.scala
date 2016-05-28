package org.apache.spark.sql.crossdata.catalog

import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.analysis.Catalog
import org.apache.spark.sql.catalyst.plans.logical.{LogicalPlan, Subquery}
import org.apache.spark.sql.crossdata._
import org.apache.spark.sql.crossdata.catalog.XDCatalog.{CrossdataTable, ViewIdentifier}
import org.apache.spark.sql.execution.datasources.{LogicalRelation, ResolvedDataSource}
import org.apache.spark.sql.sources.{HadoopFsRelationProvider, RelationProvider, SchemaRelationProvider}
import org.apache.spark.sql.types.StructType


trait XDCatalog extends Catalog with CatalogCommon {

  val xdContext: XDContext

  def registerView(viewIdentifier: ViewIdentifier, plan: LogicalPlan)
  def unregisterView(viewIdentifier: ViewIdentifier)
  def unregisterAllViews(): Unit

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

  protected def normalizeDBIdentifier(dbName: String): String =
    if (conf.caseSensitiveAnalysis) dbName else dbName.toLowerCase

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

}

object XDCatalog {

  type ViewIdentifier = TableIdentifier

  case class CrossdataTable(tableName: String, dbName: Option[String],  schema: Option[StructType],
                            datasource: String, partitionColumn: Array[String] = Array.empty,
                            opts: Map[String, String] = Map.empty , crossdataVersion: String = CrossdataVersion)

}
