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

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import org.apache.log4j.Logger

object CoreConfig {
  val CoreBasicConfig = "core-reference.conf"
  val ParentConfigName = "crossdata-core.catalog"
  val CoreUserConfigFile = "external.config.filename"
  val CoreUserConfigResource = "external.config.resource"
}

trait CoreConfig  {

  val logger: Logger

 val config: Config = {

    var defaultConfig = ConfigFactory.load(CoreConfig.CoreBasicConfig).getConfig(CoreConfig.ParentConfigName)
    val envConfigFile = Option(System.getProperties.getProperty(CoreConfig.CoreUserConfigFile))
    val configFile = envConfigFile.getOrElse(defaultConfig.getString(CoreConfig.CoreUserConfigFile))
    val configResource = defaultConfig.getString(CoreConfig.CoreUserConfigResource)

    if (configResource != "") {
      val resource = CoreConfig.getClass.getClassLoader.getResource(configResource)
      if (resource != null) {
        val userConfig = ConfigFactory.parseResources(configResource).getConfig(CoreConfig.ParentConfigName)
        defaultConfig = userConfig.withFallback(defaultConfig)
        logger.info("User resource (" + configResource + ") found in resources")
      } else {
        logger.warn("User resource (" + configResource + ") hasn't been found")
        val file = new File(configResource)
        if (file.exists()) {
          val userConfig = ConfigFactory.parseFile(file).getConfig(CoreConfig.ParentConfigName)
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
        val userConfig = ConfigFactory.parseFile(file).getConfig(CoreConfig.ParentConfigName)
        defaultConfig = userConfig.withFallback(defaultConfig)
        logger.info("External file (" + configFile + ") found")
      } else {
        logger.warn("External file (" + configFile + ") hasn't been found")
      }
    }

    // TODO Improve implementation
    // System properties
    defaultConfig = ConfigFactory.parseProperties(System.getProperties).withFallback(defaultConfig)

    ConfigFactory.load(defaultConfig)
  }


}

