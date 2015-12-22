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
package com.stratio.crossdata.driver.config

import java.io.File

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.log4j.Logger

object DriverConfig {

  val DriverConfigDefault = "driver-reference.conf"
  val ParentConfigName = "crossdata-driver"
  val DriverConfigResource = "external.config.resource"
  val DriverConfigFile = "external.config.filename"
  val DriverConfigHosts = "config.cluster.hosts"
}

trait DriverConfig {

  import DriverConfig._

  val logger: Logger

  val config: Config = {

    val defaultConfig = ConfigFactory.load(DriverConfigDefault).getConfig(ParentConfigName)
    val envConfigFile = Option(System.getProperties.getProperty(DriverConfig.DriverConfigFile))
    val configFile = envConfigFile.getOrElse(defaultConfig.getString(DriverConfig.DriverConfigFile))
    val configResource = defaultConfig.getString(DriverConfigResource)

    //Get the driver-application.conf properties if exists in resources
    val configWithResource: Config = {
      val resource = DriverConfig.getClass.getClassLoader.getResource(DriverConfigResource)
      Option(resource).fold {
        logger.warn("User resource (" + configResource + ") haven't been found")
        val file = new File(configResource)
        if (file.exists()) {
          val userConfig = ConfigFactory.parseFile(file).getConfig(ParentConfigName)
          userConfig.withFallback(defaultConfig)
        } else {
          logger.warn("User file (" + configResource + ") haven't been found in classpath")
          defaultConfig
        }
      } { resTemp =>
        val userConfig = ConfigFactory.parseResources(DriverConfigResource).getConfig(ParentConfigName)
        userConfig.withFallback(defaultConfig)
      }
    }

    //Get the user external driver-application.conf properties if exists
    val finalConfig: Config = {
      if(configFile.isEmpty){
        configWithResource
      }else{
        val file = new File(configFile)
        if (file.exists()) {
          val userConfig = ConfigFactory.parseFile(file).getConfig(ParentConfigName)
          userConfig.withFallback(configWithResource)
        } else {
          logger.error("User file (" + configFile + ") haven't been found")
          configWithResource
        }
      }
    }

    // TODO Improve implementation
    // System properties
    val finalConfigWithSystemProperties = ConfigFactory.parseProperties(System.getProperties).withFallback(finalConfig)

    ConfigFactory.load(finalConfigWithSystemProperties)
  }

}