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

import org.apache.commons.configuration.PropertiesConfiguration
import com.stratio.meta.core.engine.EngineConfig

object ServerConfig{
  val SERVER_PROPERTIES_FILE = "meta-config.properties"

  val CASSANDRA_HOSTS_KEY = "meta.server.cassandra.hosts"

  val CASSANDRA_PORT_KEY =  "meta.server.cassandra.port"

}

trait ServerConfig {
  private val config =new PropertiesConfiguration(ServerConfig.SERVER_PROPERTIES_FILE)

  val cassandraHosts: Array[String] = config.getStringArray(ServerConfig.CASSANDRA_HOSTS_KEY)
  val cassandraPort= config.getInt(ServerConfig.CASSANDRA_PORT_KEY,9042)

  lazy val engineConfig:EngineConfig = {
    val result= new EngineConfig()
    result.setCassandraHosts(cassandraHosts)
    result.setCassandraPort(cassandraPort)
    result
  }

}
