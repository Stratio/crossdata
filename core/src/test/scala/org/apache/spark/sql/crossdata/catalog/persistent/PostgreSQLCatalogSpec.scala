package org.apache.spark.sql.crossdata.catalog.persistent

import com.stratio.crossdata.test.BaseXDTest
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.sql.catalyst.{CatalystConf, SimpleCatalystConf}
import org.apache.spark.sql.crossdata.config.CoreConfig
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class PostgreSQLCatalogSpec extends BaseXDTest {

  private class PostgreSQLCatalogPublicMetadata(override val catalystConf: CatalystConf) extends PostgreSQLXDCatalog(catalystConf){
    val tablesPrefixTest = tablesPrefix
    val tableWithTableMetadataTest = tableWithTableMetadata
    val tableWithViewMetadataTest = tableWithViewMetadata
    val tableWithAppJarsTest = tableWithAppJars
    val tableWithIndexMetadataTest = tableWithIndexMetadata
    val configTest = config
  }

  private class PostgreSQLCatalogWithMockedConfig(override val catalystConf: CatalystConf) extends PostgreSQLCatalogPublicMetadata(catalystConf) {
    override lazy val config: Config =
      ConfigFactory.load("catalogspec/postgresql-catalog-test-properties.conf").getConfig(Seq(CoreConfig.ParentConfigName, CoreConfig.CatalogConfigKey) mkString ".")
  }

  it should "get the cluster name from the config if specified" in {
    val catalog = new PostgreSQLCatalogWithMockedConfig(new SimpleCatalystConf(true))
    catalog.configTest.getString("prefix") shouldBe "crossdataPostgresClusterTest"
    catalog.tablesPrefixTest shouldBe "crossdataPostgresClusterTest_"
    catalog.tableWithTableMetadataTest shouldBe "crossdataPostgresClusterTest_crossdataTables"
    catalog.tableWithViewMetadataTest shouldBe "crossdataPostgresClusterTest_crossdataViews"
    catalog.tableWithAppJarsTest shouldBe "crossdataPostgresClusterTest_crossdataJars"
    catalog.tableWithIndexMetadataTest shouldBe "crossdataPostgresClusterTest_crossdataIndexes"
  }

  it should "work with the default values if cluster name is not specified" in {
    val catalog = new PostgreSQLCatalogPublicMetadata(new SimpleCatalystConf(true))
    an[Exception] shouldBe thrownBy(catalog.configTest.getString("prefix"))
    catalog.tablesPrefixTest shouldBe ""
    catalog.tableWithTableMetadataTest shouldBe "crossdataTables"
    catalog.tableWithViewMetadataTest shouldBe "crossdataViews"
    catalog.tableWithAppJarsTest shouldBe "crossdataJars"
    catalog.tableWithIndexMetadataTest shouldBe "crossdataIndexes"
  }

}
