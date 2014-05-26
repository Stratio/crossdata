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
import org.apache.log4j.Logger

object DriverConfig{
  /**
   * Reference configuration name
   */
  val BASIC_DRIVER_CONFIG= "driver-reference.conf"
  /**
   * Root config name
   */
  val PARENT_CONFIG_NAME= "meta-driver"
  /**
   * Key to retry times
   */
  val DRIVER_RETRY_TIMES_KEY = "config.retry.times"
  /**
   * Key to duration limit
   */
  val DRIVER_RETRY_SECONDS_KEY="config.retry.duration"
  /**
   *
   */
  val DRIVER_CONFIG_FILE="external.config.filename"
  val DRIVER_CONFIG_RESOURCE = "external.config.resource"
}

trait DriverConfig extends MetaServerConfig{
  lazy val logger:Logger = ???

  override val config: Config ={
    var defaultConfig= ConfigFactory.load(DriverConfig.BASIC_DRIVER_CONFIG).getConfig(DriverConfig.PARENT_CONFIG_NAME)
    val configFile= defaultConfig.getString(DriverConfig.DRIVER_CONFIG_FILE)
    val configResource= defaultConfig.getString(DriverConfig.DRIVER_CONFIG_RESOURCE)

    if(configResource != ""){
      val resource = DriverConfig.getClass.getClassLoader.getResource(configResource)
      if(resource !=null) {
        val userConfig = ConfigFactory.parseResources(configResource).getConfig(DriverConfig.PARENT_CONFIG_NAME)
        defaultConfig = userConfig.withFallback(defaultConfig)
      }else{
        logger.warn("User resource (" + configResource + ") haven't been found")
        val file=new File(configResource)
        if(file.exists()) {
          val userConfig = ConfigFactory.parseFile(file).getConfig(DriverConfig.PARENT_CONFIG_NAME)
          defaultConfig = userConfig.withFallback(defaultConfig)
        }else{
          logger.warn("User file (" + configResource + ") haven't been found in classpath")
        }
      }
    }

    if(configFile!="" ){
      val file=new File(configFile)
      if(file.exists()) {
        val userConfig = ConfigFactory.parseFile(file).getConfig(DriverConfig.PARENT_CONFIG_NAME)
        defaultConfig = userConfig.withFallback(defaultConfig)
      }else{
        logger.warn("User file (" + configFile + ") haven't been found")
      }
    }

    ConfigFactory.load(defaultConfig)
  }

  lazy val retryTimes: Int= config.getInt(DriverConfig.DRIVER_RETRY_TIMES_KEY)
  lazy val retrySeconds: Timeout= new Timeout(config.getMilliseconds(DriverConfig.DRIVER_RETRY_SECONDS_KEY))
  lazy val retryPolitics: RetryPolitics = new RetryPolitics(retryTimes,retrySeconds)

}
