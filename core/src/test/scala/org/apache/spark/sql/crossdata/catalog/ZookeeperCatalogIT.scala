/**
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

package org.apache.spark.sql.crossdata.catalog

import com.typesafe.config.{Config, ConfigFactory, ConfigValueFactory}
import org.apache.spark.sql.crossdata.config.CoreConfig
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.util.Try

@RunWith(classOf[JUnitRunner])
class ZookeeperCatalogIT extends GenericCatalogTests
with ZookeeperDefaultTestConstants{

  override val catalogName = "Zookeeper"

  override val catalogConfig : Option[Config] = {
    val zkResourceConfig =
      Try(ConfigFactory.load("zookeeper-catalog.conf").getConfig(CoreConfig.ParentConfigName)).toOption

    ZookeeperConnection.fold(zkResourceConfig) {connectionString =>
      zkResourceConfig.flatMap(resourceConfig =>
        Option(resourceConfig.withValue(ZookeeperConnectionKey, ConfigValueFactory.fromAnyRef(connectionString))))
    }
  }

}

sealed trait ZookeeperDefaultTestConstants {
  val ZookeeperConnectionKey = "catalog.zookeeper.connectionString"
  val ZookeeperConnection: Option[String] =
    Try(ConfigFactory.load().getString(ZookeeperConnectionKey)).toOption
}