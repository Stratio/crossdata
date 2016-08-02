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
class ZookeeperCatalogSpec extends BaseXDTest {

  private class ZookeeperCatalogWithMockedConfig(override val catalystConf: CatalystConf) extends ZookeeperCatalog(catalystConf) {
    override lazy val config: Config = ConfigFactory.load("catalogspec/zookeeper-catalog-test-properties.conf")
  }

  it should "get the cluster name from the config" in {
    val a = new ZookeeperCatalogWithMockedConfig(new SimpleCatalystConf(true))
    a.config.getString("crossdata-core.catalog.clustername") shouldBe "crossdataClusterTest"
    //TODO:println(a.tableDAO.dao.entity)
  }

}
