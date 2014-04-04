/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.driver.config

import com.typesafe.config.{ ConfigFactory, Config}
import com.stratio.meta.driver.utils.RetryPolitics
import akka.util.Timeout
import java.io.File

object DriverConfig{
  val BASIC_DRIVER_CONFIG= "driver-reference.conf"

  val PARENT_CONFIG_NAME= "meta-driver"
  val DRIVER_RETRY_TIMES_KEY = "config.retry.times"
  val DRIVER_RETRY_SECONDS_KEY="config.retry.duration"
  val DRIVER_CONFIG_FILE="external.config.filename"
  val DRIVER_CONFIG_RESOURCE = "external.config.resource"
}

trait DriverConfig extends MetaServerConfig{
  override val config: Config ={
    var defaultConfig= ConfigFactory.load(DriverConfig.BASIC_DRIVER_CONFIG).getConfig(DriverConfig.PARENT_CONFIG_NAME)
    val configFile= defaultConfig.getString(DriverConfig.DRIVER_CONFIG_FILE)
    val configResource= defaultConfig.getString(DriverConfig.DRIVER_CONFIG_RESOURCE)

    if(configResource != ""){
      val userConfig = ConfigFactory.parseResources(configResource).getConfig(DriverConfig.PARENT_CONFIG_NAME)
      defaultConfig=userConfig.withFallback(defaultConfig)
    }

    if(configFile!="" ){
      val userConfig= ConfigFactory.parseFile(new File(configFile)).getConfig(DriverConfig.PARENT_CONFIG_NAME)
      defaultConfig=userConfig.withFallback(defaultConfig)
    }

    ConfigFactory.load(defaultConfig)
  }

  lazy val retryTimes: Int= config.getInt(DriverConfig.DRIVER_RETRY_TIMES_KEY)
  lazy val retrySeconds: Timeout= new Timeout(config.getMilliseconds(DriverConfig.DRIVER_RETRY_SECONDS_KEY))

  lazy val retryPolitics: RetryPolitics = new RetryPolitics(retryTimes,retrySeconds)

}
