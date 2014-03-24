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
