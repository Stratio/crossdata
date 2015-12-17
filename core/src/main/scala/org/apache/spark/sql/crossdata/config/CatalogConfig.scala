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
package org.apache.spark.sql.crossdata.config

import java.io.File

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.log4j.Logger

object CatalogConfig {
  val CatalogBasicConfig = "core-reference.conf"
  val ParentConfigName = "crossdata.catalog"
  val CatalogUserConfigFile = "external.config.filename"
  val CatalogUserConfigResource = "external.config.resource"
}

trait CatalogConfig  {

  val logger: Logger

 val config: Config = {

    var defaultConfig = ConfigFactory.load(CatalogConfig.CatalogBasicConfig).getConfig(CatalogConfig.ParentConfigName)
    val configFile = defaultConfig.getString(CatalogConfig.CatalogUserConfigFile)
    val configResource = defaultConfig.getString(CatalogConfig.CatalogUserConfigResource)

    if (configResource != "") {
      val resource = CatalogConfig.getClass.getClassLoader.getResource(configResource)
      if (resource != null) {
        val userConfig = ConfigFactory.parseResources(configResource).getConfig(CatalogConfig.ParentConfigName)
        defaultConfig = userConfig.withFallback(defaultConfig)
        logger.info("User resource (" + configResource + ") found in resources")
      } else {
        logger.warn("User resource (" + configResource + ") hasn't been found")
        val file = new File(configResource)
        if (file.exists()) {
          val userConfig = ConfigFactory.parseFile(file).getConfig(CatalogConfig.ParentConfigName)
          defaultConfig = userConfig.withFallback(defaultConfig)
          logger.info("User resource (" + configResource + ") found in classpath")
        } else {
          logger.warn("User file (" + configResource + ") hasn't been found in classpath")
        }
      }
    }

    if (configFile != "") {
      val file = new File(configFile)
      if (file.exists()) {
        val userConfig = ConfigFactory.parseFile(file).getConfig(CatalogConfig.ParentConfigName)
        defaultConfig = userConfig.withFallback(defaultConfig)
        logger.info("External file (" + configFile + ") found")
      } else {
        logger.warn("External file (" + configFile + ") hasn't been found")
      }
    }
    ConfigFactory.load(defaultConfig)
  }


}

