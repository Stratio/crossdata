package org.apache.spark.sql.crossdata.catalog


import org.apache.spark.sql.catalyst.{CatalystConf, TableIdentifier}
import org.apache.spark.sql.catalyst.analysis.Catalog
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.crossdata
import org.apache.spark.sql.crossdata.catalog.XDCatalog.{CrossdataTable, ViewIdentifier}
import org.apache.spark.sql.crossdata.catalog.interfaces.XDCatalogCommon
import org.apache.spark.sql.crossdata.serializers.CrossdataSerializer
import org.apache.spark.sql.types.StructType
import org.json4s.jackson.Serialization._


object XDCatalog extends CrossdataSerializer {

implicit def asXDCatalog (catalog: Catalog): XDCatalog = catalog.asInstanceOf[XDCatalog]

  type ViewIdentifier = TableIdentifier
  type ViewIdentifierNormalized = TableIdentifierNormalized

  case class IndexIdentifier(indexType: String, indexName: String) {
    def quotedString: String = s"`$indexName`.`$indexType`"
    def unquotedString: String = s"$indexName.$indexType"
    override def toString: String = quotedString
    def asTableIdentifier: TableIdentifier = TableIdentifier(indexType,Option(indexName))
  }

  case class CrossdataTable(tableIdentifier: TableIdentifierNormalized, schema: Option[StructType],
                            datasource: String, partitionColumn: Array[String] = Array.empty,
                            opts: Map[String, String] = Map.empty, crossdataVersion: String = crossdata.CrossdataVersion)


  case class CrossdataIndex(tableIdentifier: TableIdentifierNormalized, indexIdentifier: IndexIdentifierNormalized,
                            indexedCols: Seq[String], pk: String, datasource: String,
                            opts: Map[String, String] = Map.empty, crossdataVersion: String = crossdata.CrossdataVersion)


  case class CrossdataApp(jar: String, appAlias: String, appClass: String)


  def serializeSchema(schema: StructType): String = write(schema)

  def deserializeUserSpecifiedSchema(schemaJSON: String): StructType = read[StructType](schemaJSON)

  def serializePartitionColumn(partitionColumn: Array[String]): String = write(partitionColumn)

  def deserializePartitionColumn(partitionColumn: String): Array[String] = read[Array[String]](partitionColumn)

  def serializeOptions(options: Map[String, String]): String =  write(options)

  def deserializeOptions(optsJSON: String): Map[String, String] = read[Map[String, String]](optsJSON)

  def serializeSeq(seq: Seq[String]): String = write(seq)

  def deserializeSeq(seqJSON: String): Seq[String] = read[Seq[String]](seqJSON)


}

trait XDCatalog extends Catalog
with ExternalCatalogAPI
with StreamingCatalogAPI {

  def registerTable(tableIdent: TableIdentifier, plan: LogicalPlan, crossdataTable: Option[CrossdataTable]): Unit
  def registerView(viewIdentifier: ViewIdentifier, logicalPlan: LogicalPlan, sql: Option[String] = None): Unit

  final def registerTable(tableIdent: TableIdentifier, plan: LogicalPlan): Unit =
    registerTable(tableIdent, plan, None)


  def unregisterView(viewIdentifier: ViewIdentifier): Unit

  /**
   * Check the connection to the set Catalog
   */
  def checkConnectivity: Boolean

}



