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
import java.io.File
import org.apache.log4j.Logger

object ServerConfig{
  val SERVER_BASIC_CONFIG = "server-reference.conf"
  val PARENT_CONFIG_NAME= "meta-server"


  val SERVER_CLUSTER_NAME_KEY="config.cluster.name"
  val SERVER_ACTOR_NAME_KEY="config.cluster.actor"
  val SERVER_USER_CONFIG_FILE="external.config.filename"
  val SERVER_USER_CONFIG_RESOURCE = "external.config.resource"
}

trait ServerConfig extends CassandraConfig with SparkConfig{

  lazy val logger:Logger = ???

  override val config: Config ={

    var defaultConfig= ConfigFactory.load(ServerConfig.SERVER_BASIC_CONFIG).getConfig(ServerConfig.PARENT_CONFIG_NAME)
    val configFile= defaultConfig.getString(ServerConfig.SERVER_USER_CONFIG_FILE)
    val configResource= defaultConfig.getString(ServerConfig.SERVER_USER_CONFIG_RESOURCE)

    if(configResource != ""){
      val resource = ServerConfig.getClass.getClassLoader.getResource(configResource)
      if(resource !=null) {
        val userConfig = ConfigFactory.parseResources(configResource).getConfig(ServerConfig.PARENT_CONFIG_NAME)
        defaultConfig = userConfig.withFallback(defaultConfig)
      }else{
        logger.warn("User resource (" + configResource + ") haven't been found")
        val file=new File(configResource)
        if(file.exists()) {
          val userConfig = ConfigFactory.parseFile(file).getConfig(ServerConfig.PARENT_CONFIG_NAME)
          defaultConfig = userConfig.withFallback(defaultConfig)
        }else{
          logger.warn("User file (" + configResource + ") haven't been found in classpath")
        }
      }
    }

    if(configFile!="" ){
      val file=new File(configFile)
      if(file.exists()) {
        val userConfig = ConfigFactory.parseFile(file).getConfig(ServerConfig.PARENT_CONFIG_NAME)
        defaultConfig = userConfig.withFallback(defaultConfig)
      }else{
        logger.warn("User file (" + configFile + ") haven't been found")
      }
    }

    ConfigFactory.load(defaultConfig)
  }

  lazy val engineConfig:EngineConfig = {
    val result= new EngineConfig()
    result.setCassandraHosts(cassandraHosts)
    result.setCassandraPort(cassandraPort)
    result.setSparkMaster(sparkMaster)
    result
  }

  lazy val clusterName =  config.getString(ServerConfig.SERVER_CLUSTER_NAME_KEY)
  
  lazy val actorName =  config.getString(ServerConfig.SERVER_ACTOR_NAME_KEY)

}

