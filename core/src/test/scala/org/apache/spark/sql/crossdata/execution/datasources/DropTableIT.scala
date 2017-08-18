package org.apache.spark.sql.crossdata.execution.datasources

import org.apache.spark.sql.catalyst.{CatalystConf, TableIdentifier}
import org.apache.spark.sql.crossdata.catalog.{CatalogChain, XDCatalog}
import XDCatalog.CrossdataTable
import org.apache.spark.sql.crossdata.catalog.persistent.PersistentCatalogWithCache
import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import org.apache.spark.sql.crossdata.catalog.interfaces.XDCatalogCommon._

@RunWith(classOf[JUnitRunner])
class DropTableIT extends SharedXDContextTest {

  private val TableName = "tableId"
  private val DatabaseName = "dbId"
  private val DatasourceName = "json"
  private val Schema = StructType(Seq(StructField("col", StringType)))

  implicit def catalogToPersistenceWithCache(catalog: XDCatalog): PersistentCatalogWithCache = {
    catalog.asInstanceOf[CatalogChain].persistentCatalogs.head.asInstanceOf[PersistentCatalogWithCache]
  }

  implicit lazy val conf: CatalystConf = xdContext.catalog.conf

  "DropTable command" should "remove a table from Crossdata catalog" in {

    _xdContext.catalog.persistTableMetadata(CrossdataTable(TableIdentifier(TableName, None).normalize, Some(Schema), DatasourceName, opts = Map("path" -> "fakepath")))
    _xdContext.catalog.tableExists(TableIdentifier(TableName)) shouldBe true
    sql(s"DROP TABLE $TableName")
    _xdContext.catalog.tableExists(TableIdentifier(TableName)) shouldBe false
  }

  it should "remove a qualified table from Crossdata catalog" in {
    _xdContext.catalog.persistTableMetadata(CrossdataTable(TableIdentifier(TableName, Some(DatabaseName)).normalize, Some(Schema), DatasourceName, opts = Map("path" -> "fakepath")))
    _xdContext.catalog.tableExists(TableIdentifier(TableName, Some(DatabaseName))) shouldBe true
    sql(s"DROP TABLE $DatabaseName.$TableName")
    _xdContext.catalog.tableExists(TableIdentifier(TableName, Some(DatabaseName))) shouldBe false
  }


  it should "remove a view when table doesn't exists but view with its name exists in the catalog" in {
    val viewName = s"tmpTableDrop$TableName"

    _xdContext.catalog.persistTableMetadata(CrossdataTable(TableIdentifier(TableName, None).normalize, Some(Schema), DatasourceName, opts = Map("path" -> "fakepath")))
    _xdContext.catalog.tableExists(TableIdentifier(TableName)) shouldBe true
    _xdContext.catalog.tableExists(TableIdentifier(viewName)) shouldBe false

    sql(s"CREATE TABLE $viewName AS SELECT col FROM $TableName")
    _xdContext.catalog.tableExists(TableIdentifier(viewName)) shouldBe true

    sql(s"DROP TABLE $viewName")
    _xdContext.catalog.tableExists(TableIdentifier(viewName)) shouldBe false

    sql(s"DROP TABLE $TableName")
    _xdContext.catalog.tableExists(TableIdentifier(TableName)) shouldBe false
  }


}
