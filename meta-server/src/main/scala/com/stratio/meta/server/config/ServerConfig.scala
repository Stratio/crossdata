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
  val SERVER_DEFAULT_CONFIG_FILE = "basic-server.conf"
  val SERVER_USER_CONFIG_FILE = "meta-server.conf"

  val SERVER_CLUSTER_NAME_KEY="server.cluster.name"
  val SERVER_ACTOR_NAME_KEY="server.cluster.actor.name"
}

trait ServerConfig extends CassandraConfig{

  override lazy val config: Config ={

    val defaultConfig1= ConfigFactory.load(ServerConfig.SERVER_DEFAULT_CONFIG_FILE)
    println(defaultConfig1)
    val defaultConfig= defaultConfig1.getConfig("meta")
    val userConfig=ConfigFactory.load(ServerConfig.SERVER_USER_CONFIG_FILE).getConfig("meta")
    val merge = userConfig.withFallback(defaultConfig)
    ConfigFactory.load(merge)
  }

  lazy val engineConfig:EngineConfig = {
    val result= new EngineConfig()
    result.setCassandraHosts(cassandraHosts)
    result.setCassandraPort(cassandraPort)
    result
  }

  lazy val clusterName =  config.getString(ServerConfig.SERVER_CLUSTER_NAME_KEY)
  
  lazy val actorName =  config.getString(ServerConfig.SERVER_ACTOR_NAME_KEY)

}

