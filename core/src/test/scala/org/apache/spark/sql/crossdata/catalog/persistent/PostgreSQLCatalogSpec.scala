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
package org.apache.spark.sql.crossdata.catalog.persistent

import com.stratio.crossdata.test.BaseXDTest
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.sql.catalyst.{CatalystConf, SimpleCatalystConf}
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.crossdata.config.CoreConfig
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class PostgreSQLCatalogSpec extends BaseXDTest {

  private class PostgreSQLCatalogPublicMetadata(override val catalystConf: CatalystConf, val catalogConfig: Config) extends PostgreSQLXDCatalog(catalystConf, catalogConfig){
    val tablesPrefixTest = tablesPrefix
    val tableWithTableMetadataTest = tableWithTableMetadata
    val tableWithViewMetadataTest = tableWithViewMetadata
    val tableWithAppJarsTest = tableWithAppJars
    val tableWithIndexMetadataTest = tableWithIndexMetadata
  }

  lazy val defaultConfig: Config = new CoreConfig().catalogConfig

  lazy val mockedConfig: Config =
    ConfigFactory.load("catalogspec/postgresql-catalog-test-properties.conf").getConfig(Seq(CoreConfig.ParentConfigName, CoreConfig.CatalogConfigKey) mkString ".")

  it should "get the cluster name from the config if specified" in {
    val catalog = new PostgreSQLCatalogPublicMetadata(new SimpleCatalystConf(true), mockedConfig)
    catalog.catalogConfig.getString("prefix") shouldBe "crossdataPostgresClusterTest"
    catalog.tablesPrefixTest shouldBe "crossdataPostgresClusterTest_"
    catalog.tableWithTableMetadataTest shouldBe "crossdataPostgresClusterTest_crossdataTables"
    catalog.tableWithViewMetadataTest shouldBe "crossdataPostgresClusterTest_crossdataViews"
    catalog.tableWithAppJarsTest shouldBe "crossdataPostgresClusterTest_crossdataJars"
    catalog.tableWithIndexMetadataTest shouldBe "crossdataPostgresClusterTest_crossdataIndexes"
  }

  it should "work with the default values if cluster name is not specified" in {
    val catalog = new PostgreSQLCatalogPublicMetadata(new SimpleCatalystConf(true), defaultConfig)
    an[Exception] shouldBe thrownBy(catalog.catalogConfig.getString("prefix"))
    catalog.tablesPrefixTest shouldBe ""
    catalog.tableWithTableMetadataTest shouldBe "crossdataTables"
    catalog.tableWithViewMetadataTest shouldBe "crossdataViews"
    catalog.tableWithAppJarsTest shouldBe "crossdataJars"
    catalog.tableWithIndexMetadataTest shouldBe "crossdataIndexes"
  }

}
