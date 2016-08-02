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
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class PostgreSQLCatalogSpec extends BaseXDTest {

  private class PostgreSQLCatalogWithMockedConfig(override val catalystConf: CatalystConf) extends PostgreSQLXDCatalog(catalystConf) {
    override lazy val config: Config = ConfigFactory.load("catalogspec/postgresql-catalog-test-properties.conf")
  }

  it should "get the cluster name from the config if specified" in {
    val catalog = new PostgreSQLCatalogWithMockedConfig(new SimpleCatalystConf(true))
    catalog.config.getString("crossdata-core.catalog.clustername") shouldBe "crossdataPostgresClusterTest"
    catalog.tablesPrefix shouldBe "crossdataPostgresClusterTest_"
    catalog.tableWithTableMetadata shouldBe "crossdataPostgresClusterTest_crossdataTables"
    catalog.tableWithViewMetadata shouldBe "crossdataPostgresClusterTest_crossdataViews"
    catalog.tableWithAppJars shouldBe "crossdataPostgresClusterTest_crossdataJars"
    catalog.tableWithIndexMetadata shouldBe "crossdataPostgresClusterTest_crossdataIndexes"
  }

  it should "work with the default values if cluster name is not specified" in {
    val catalog = new PostgreSQLXDCatalog(new SimpleCatalystConf(true))
    an[Exception] shouldBe thrownBy(catalog.config.getString("crossdata-core.catalog.clustername"))
    catalog.tablesPrefix shouldBe ""
    catalog.tableWithTableMetadata shouldBe "crossdataTables"
    catalog.tableWithViewMetadata shouldBe "crossdataViews"
    catalog.tableWithAppJars shouldBe "crossdataJars"
    catalog.tableWithIndexMetadata shouldBe "crossdataIndexes"
  }

}
