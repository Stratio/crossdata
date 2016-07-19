package org.apache.spark.sql.crossdata.catalog

import com.typesafe.config.{Config, ConfigFactory, ConfigValueFactory}
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.crossdata.catalog.XDCatalog.CrossdataTable
import org.apache.spark.sql.crossdata.catalog.interfaces.XDCatalogCommon
import org.apache.spark.sql.crossdata.catalog.persistent.DerbyCatalogIT
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class InsensitiveCatalogIT extends DerbyCatalogIT {

  override val catalogConfig : Option[Config] =
    Some(ConfigFactory.empty().withValue("crossdata-core.catalog.caseSensitive", ConfigValueFactory.fromAnyRef(false)))


  it should s"persist a table and retrieve it changing some letters to upper case in $catalogName" in {

    val tableNameOriginal = "TableNameInsensitive"
    import XDCatalogCommon._
    val tableIdentifier = TableIdentifier(tableNameOriginal, Some(Database))
    val tableNormalized = tableIdentifier.normalize(xdContext.catalog.conf)
    val crossdataTable = CrossdataTable(tableNormalized, Some(Columns), SourceDatasource, Array[String](Field1Name), OptsJSON)

    xdContext.catalog.persistTableMetadata(crossdataTable)
    xdContext.catalog.tableExists(tableIdentifier) shouldBe true

    val tableNameOriginal2 = "tablenameinsensitive"
    val tableIdentifier2 = TableIdentifier(tableNameOriginal2, Some(Database))
    xdContext.catalog.tableExists(tableIdentifier2) shouldBe true

    val tableNameOriginal3 = "TABLENAMEINSENSITIVE"
    val tableIdentifier3 = TableIdentifier(tableNameOriginal2, Some(Database))
    xdContext.catalog.tableExists(tableIdentifier3) shouldBe true

  }

}
