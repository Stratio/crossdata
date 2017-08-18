/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
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
