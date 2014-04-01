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

import com.typesafe.config.{ConfigFactory, Config}
import com.stratio.meta.driver.utils.RetryPolitics
import akka.util.Timeout
import scala.concurrent.duration._

object DriverConfig{
  val DRIVER_DEFAULT_CONFIG_FILE = "basic-driver.conf"
  val DRIVER_USER_CONFIG_FILE = "meta-driver.conf"

  val DRIVER_RETRY_TIMES_KEY = "driver.retry.times"
  val DRIVER_RETRY_SECONDS_KEY="driver.retry.seconds"
}

trait DriverConfig extends MetaServerConfig{
  override val config: Config ={
    val defaultConfig= ConfigFactory.load(DriverConfig.DRIVER_DEFAULT_CONFIG_FILE).getConfig("meta")
    val userConfig=ConfigFactory.load(DriverConfig.DRIVER_USER_CONFIG_FILE).getConfig("meta")
    val merge = userConfig.withFallback(defaultConfig)
    merge
  }

  lazy val retryTimes: Int= config.getInt(DriverConfig.DRIVER_RETRY_TIMES_KEY)
  lazy val retrySeconds: Timeout= config.getInt(DriverConfig.DRIVER_RETRY_SECONDS_KEY).seconds

  lazy val retryPolitics: RetryPolitics = new RetryPolitics(retryTimes,retrySeconds)

}
