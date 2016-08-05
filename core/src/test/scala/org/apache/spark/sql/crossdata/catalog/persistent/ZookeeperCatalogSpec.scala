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
import org.apache.spark.sql.crossdata.catalog.streaming.ZookeeperStreamingCatalog
import org.apache.spark.sql.crossdata.config.CoreConfig
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class ZookeeperCatalogSpec extends BaseXDTest {

  private class ZookeeperCatalogWithMockedConfig(override val catalystConf: CatalystConf) extends ZookeeperCatalog(catalystConf) {
    override lazy val config: Config =
      ConfigFactory.load("catalogspec/zookeeper-catalog-test-properties.conf").getConfig(Seq(CoreConfig.ParentConfigName, CoreConfig.CatalogConfigKey) mkString ".")
  }

  it should "get the cluster name from the config" in {
    val catalog = new ZookeeperCatalogWithMockedConfig(new SimpleCatalystConf(true))
    catalog.config.getString("prefix") shouldBe "crossdataClusterTest"
    catalog.tableDAO.dao.entity shouldBe "stratio/crossdata/crossdataClusterTest_tables"
    catalog.viewDAO.dao.entity shouldBe "stratio/crossdata/crossdataClusterTest_views"
    catalog.appDAO.dao.entity shouldBe "stratio/crossdata/crossdataClusterTest_apps"
    catalog.indexDAO.dao.entity shouldBe "stratio/crossdata/crossdataClusterTest_indexes"

    //Ephemeral
    val streamingCatalog = new ZookeeperStreamingCatalog(
      new SimpleCatalystConf(true),
      ConfigFactory.load("catalogspec/zookeeper-streaming-catalog-with-prefix.conf").getConfig(CoreConfig.ParentConfigName)
    )
    streamingCatalog.streamingConfig.getString("catalog.zookeeper.prefix") shouldBe "crossdataClusterTest"
    streamingCatalog.ephemeralQueriesDAO.dao.entity shouldBe "stratio/crossdata/crossdataClusterTest_ephemeralqueries"
    streamingCatalog.ephemeralTableDAO.dao.entity shouldBe "stratio/crossdata/crossdataClusterTest_ephemeraltables"
    streamingCatalog.ephemeralTableStatusDAO.dao.entity shouldBe "stratio/crossdata/crossdataClusterTest_ephemeraltablestatus"
  }

  it should "work with the default values if cluster name is not specified" in {
    val catalog = new ZookeeperCatalog(new SimpleCatalystConf(true))
    an[Exception] shouldBe thrownBy(catalog.config.getString("prefix"))
    catalog.tableDAO.dao.entity shouldBe "stratio/crossdata/tables"
    catalog.viewDAO.dao.entity shouldBe "stratio/crossdata/views"
    catalog.appDAO.dao.entity shouldBe "stratio/crossdata/apps"
    catalog.indexDAO.dao.entity shouldBe "stratio/crossdata/indexes"

    //Ephemeral
    val streamingCatalog = new ZookeeperStreamingCatalog(
      new SimpleCatalystConf(true),
      ConfigFactory.load("catalogspec/zookeeper-streaming-catalog-without-prefix.conf").getConfig(CoreConfig.ParentConfigName)
    )
    an[Exception] shouldBe thrownBy(catalog.config.getString("streaming.catalog.zookeeper.prefix"))
    streamingCatalog.ephemeralQueriesDAO.dao.entity shouldBe "stratio/crossdata/ephemeralqueries"
    streamingCatalog.ephemeralTableDAO.dao.entity shouldBe "stratio/crossdata/ephemeraltables"
    streamingCatalog.ephemeralTableStatusDAO.dao.entity shouldBe "stratio/crossdata/ephemeraltablestatus"
  }

}
