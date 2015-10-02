/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.stratio.crossdata.driver.config

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.log4j.Logger

object DriverConfig {
  val DriverConfigFile = "driver-reference.conf"
  val ParentConfigName = "crossdata-driver"
  val DriverConfigResource = "external.config.resource"
  val DriverConfigSeedNodes = "akka.cluster.seed-nodes"
}

trait DriverConfig {

  import DriverConfig._

  lazy val logger: Logger = ???

  val config: Config = {
    val defaultConfig = ConfigFactory.load(DriverConfigFile).getConfig(ParentConfigName)
    val configResource = defaultConfig.getString(DriverConfigResource)

    val mergedConfig = Some(configResource) filter { config =>
      val notFound = config.isEmpty || DriverConfig.getClass.getClassLoader.getResource(config) == null
      if (notFound) logger.warn(s"User file '$configResource' haven't been found in the classpath")
      !notFound
    } map {
      ConfigFactory.parseResources(_).getConfig(ParentConfigName) withFallback defaultConfig
    } getOrElse defaultConfig


    ConfigFactory.load(mergedConfig)

  }

}
