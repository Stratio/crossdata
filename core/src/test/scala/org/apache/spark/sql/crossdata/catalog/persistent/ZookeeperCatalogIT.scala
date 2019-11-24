/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package org.apache.spark.sql.crossdata.catalog.persistent

import com.typesafe.config.{Config, ConfigFactory, ConfigValueFactory}
import org.apache.spark.sql.crossdata.catalog.CatalogConstants
import org.apache.spark.sql.crossdata.config.CoreConfig
import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import CoreConfig.{CatalogClassConfigKey, ZookeeperClass}

import scala.util.Try

@RunWith(classOf[JUnitRunner])
class ZookeeperCatalogIT extends {
  val catalogName = "Zookeeper"
} with SharedXDContextTest with CatalogConstants with GenericCatalogTests with ZookeeperDefaultTestConstants{

  override val coreConfig : Option[Config] = {
    val zkResourceConfig =
      Try(ConfigFactory.load("zookeeper-catalog.conf").getConfig(CoreConfig.ParentConfigName)).toOption

    ZookeeperConnection.fold(zkResourceConfig) {connectionString =>
      zkResourceConfig.flatMap(resourceConfig =>
        Option(resourceConfig.withValue(ZookeeperConnectionKey, ConfigValueFactory.fromAnyRef(connectionString))))
    } map (_.withValue(CatalogClassConfigKey, ConfigValueFactory.fromAnyRef(ZookeeperClass)))
  }

}

sealed trait ZookeeperDefaultTestConstants {
  val ZookeeperConnectionKey = "catalog.zookeeper.connectionString"
  val ZookeeperConnection: Option[String] =
    Try(ConfigFactory.load().getString(ZookeeperConnectionKey)).toOption
}