/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.crossdata.server.config

import java.io.File

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.log4j.Logger
import scala.collection.JavaConversions.enumerationAsScalaIterator

object ServerConfig {
  val SERVER_BASIC_CONFIG = "server-reference.conf"
  val PARENT_CONFIG_NAME = "crossdata-server"

  //  akka cluster values
  val SERVER_CLUSTER_NAME_KEY = "config.cluster.name"
  val SERVER_ACTOR_NAME_KEY = "config.cluster.actor"
  val SERVER_USER_CONFIG_FILE = "external.config.filename"
  val SERVER_USER_CONFIG_RESOURCE = "external.config.resource"

  // spark context values
  val SERVER_CONFIG_SPARK_MASTER="config.spark.master"
  val SERVER_CONFIG_SPARK_DRIVER_MEMORY="config.spark.driver.memory"
  val SERVER_CONFIG_SPARK_EXECUTOR_MEMORY="config.spark.executor.memory"
  val SERVER_CONFIG_SPARK_CORES="config.spark.cores.max"
  val SERVER_CONFIG_SPARK_HEARTBEAT="config.spark.akka.heartbeat.interval"

  //val SERVER_ACTOR_NUM= "config.akka.number.server-actor"
}

trait ServerConfig extends NumberActorConfig {

  def getLocalIPs(): List[String] = {
    val addresses = for {
      networkInterface <- java.net.NetworkInterface.getNetworkInterfaces()
      address <- networkInterface.getInetAddresses
    } yield address.toString
    val filterthese = List(".*127.0.0.1", ".*localhost.*", ".*::1", ".*0:0:0:0:0:0:0:1")
    for {r <- addresses.toList; if (filterthese.find(e => r.matches(e)).isEmpty)} yield r
  }

  val ips = getLocalIPs()

  val logger: Logger


  lazy val clusterName = config.getString(ServerConfig.SERVER_CLUSTER_NAME_KEY)
  lazy val actorName = config.getString(ServerConfig.SERVER_ACTOR_NAME_KEY)
  lazy val sparkMaster = config.getString(ServerConfig.SERVER_CONFIG_SPARK_MASTER)
  lazy val sparkDriverMemory = config.getString(ServerConfig.SERVER_CONFIG_SPARK_DRIVER_MEMORY)
  lazy val sparkExecutorMemory = config.getString(ServerConfig.SERVER_CONFIG_SPARK_EXECUTOR_MEMORY)
  lazy val sparkCores = config.getString(ServerConfig.SERVER_CONFIG_SPARK_CORES)
  //lazy val sparkHeartbeat = config.getString(ServerConfig.SERVER_CONFIG_SPARK_HEARTBEAT)

  override val config: Config = {

    var defaultConfig = ConfigFactory.load(ServerConfig.SERVER_BASIC_CONFIG).getConfig(ServerConfig.PARENT_CONFIG_NAME)
    val configFile = defaultConfig.getString(ServerConfig.SERVER_USER_CONFIG_FILE)
    val configResource = defaultConfig.getString(ServerConfig.SERVER_USER_CONFIG_RESOURCE)

    if (configResource != "") {
      val resource = ServerConfig.getClass.getClassLoader.getResource(configResource)
      if (resource != null) {
        val userConfig = ConfigFactory.parseResources(configResource).getConfig(ServerConfig.PARENT_CONFIG_NAME)
        defaultConfig = userConfig.withFallback(defaultConfig)
      } else {
        logger.warn("User resource (" + configResource + ") haven't been found")
        val file = new File(configResource)
        if (file.exists()) {
          val userConfig = ConfigFactory.parseFile(file).getConfig(ServerConfig.PARENT_CONFIG_NAME)
          defaultConfig = userConfig.withFallback(defaultConfig)
        } else {
          logger.warn("User file (" + configResource + ") haven't been found in classpath")
        }
      }
    }

    if (configFile != "") {
      val file = new File(configFile)
      if (file.exists()) {
        val userConfig = ConfigFactory.parseFile(file).getConfig(ServerConfig.PARENT_CONFIG_NAME)
        defaultConfig = userConfig.withFallback(defaultConfig)
      } else {
        logger.warn("User file (" + configFile + ") haven't been found")
      }
    }

    ConfigFactory.load(defaultConfig)
  }


}

