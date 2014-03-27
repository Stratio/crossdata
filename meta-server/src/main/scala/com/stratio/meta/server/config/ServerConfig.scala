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

package com.stratio.meta.server.config


import com.stratio.meta.core.engine.EngineConfig
import com.typesafe.config.{ConfigFactory, Config}

object ServerConfig{
  val SERVER_DEFAULT_CONFIG_FILE = "basic.conf"
  val SERVER_USER_CONFIG_FILE = "meta.conf"
}

trait ServerConfig extends CassandraConfig{

  override lazy val config: Config ={

    val defaultConfig= ConfigFactory.load(ServerConfig.SERVER_DEFAULT_CONFIG_FILE).getConfig("meta")
    val userConfig=ConfigFactory.load(ServerConfig.SERVER_USER_CONFIG_FILE).getConfig("meta")
    val merge = userConfig.withFallback(defaultConfig)
    print(merge)

    ConfigFactory.load(merge)
  }

  lazy val engineConfig:EngineConfig = {
    val result= new EngineConfig()
    result.setCassandraHosts(cassandraHosts)
    result.setCassandraPort(cassandraPort)
    result
  }

}

