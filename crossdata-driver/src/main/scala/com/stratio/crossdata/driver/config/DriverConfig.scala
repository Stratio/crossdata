/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.crossdata.driver.config

import java.io.File

import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.log4j.Logger

object DriverConfig {
  /**
   * Reference configuration name
   */
  val BASIC_DRIVER_CONFIG = "driver-reference.conf"
  /**
   * Root config name
   */
  val PARENT_CONFIG_NAME = "crossdata-driver"
  /**
   * Key to retry times
   */
  val DRIVER_RETRY_TIMES_KEY = "config.retry.times"
  /**
   * Key to duration limit
   */
  val DRIVER_RETRY_SECONDS_KEY = "config.retry.duration"

  /**
   *
   */
  val DRIVER_CONFIG_FILE = "external.config.filename"
  val DRIVER_CONFIG_RESOURCE = "external.config.resource"
}

trait DriverConfig extends CrossdataServerConfig {
  lazy val logger: Logger = ???
  lazy val retryTimes: Int = config.getInt(DriverConfig.DRIVER_RETRY_TIMES_KEY)
  lazy val retryDuration: Timeout = new Timeout(config.getMilliseconds(DriverConfig.DRIVER_RETRY_SECONDS_KEY))

  override val config: Config = {
    var defaultConfig = ConfigFactory.load(DriverConfig.BASIC_DRIVER_CONFIG).getConfig(DriverConfig.PARENT_CONFIG_NAME)
    val configFile = defaultConfig.getString(DriverConfig.DRIVER_CONFIG_FILE)
    val configResource = defaultConfig.getString(DriverConfig.DRIVER_CONFIG_RESOURCE)

    if (configResource != "") {
      val resource = DriverConfig.getClass.getClassLoader.getResource(configResource)
      if (resource != null) {
        val userConfig = ConfigFactory.parseResources(configResource).getConfig(DriverConfig.PARENT_CONFIG_NAME)
        defaultConfig = userConfig.withFallback(defaultConfig)
      } else {
        logger.warn("User resource (" + configResource + ") haven't been found")
        val file = new File(configResource)
        if (file.exists()) {
          val userConfig = ConfigFactory.parseFile(file).getConfig(DriverConfig.PARENT_CONFIG_NAME)
          defaultConfig = userConfig.withFallback(defaultConfig)
        } else {
          logger.warn("User file (" + configResource + ") haven't been found in classpath")
        }
      }
    }

    if (configFile != "") {
      val file = new File(configFile)
      if (file.exists()) {
        val userConfig = ConfigFactory.parseFile(file).getConfig(DriverConfig.PARENT_CONFIG_NAME)
        defaultConfig = userConfig.withFallback(defaultConfig)
      } else {
        logger.warn("User file (" + configFile + ") haven't been found")
      }
    }

    ConfigFactory.load(defaultConfig)
  }


}
