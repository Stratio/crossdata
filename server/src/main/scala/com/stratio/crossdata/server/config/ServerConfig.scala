/*
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
import java.util.concurrent.TimeUnit

import com.typesafe.config.{Config, ConfigFactory, ConfigValueFactory}
import org.apache.log4j.Logger
import org.apache.spark.sql.crossdata.config.CoreConfig

import scala.collection.JavaConversions._
import scala.concurrent.duration._
import scala.util.Try

object ServerConfig {

  val ServerBasicConfig = "server-reference.conf"
  val ParentConfigName = "crossdata-server"


  val SparkSqlConfigPrefix = CoreConfig.SparkSqlConfigPrefix

  val ClientExpectedHeartbeatPeriod = "config.client.ExpectedHeartbeatPeriod"

  //  akka cluster values
  val ServerClusterNameKey = "config.cluster.name"
  val ServerActorNameKey = "config.cluster.actor"
  val ServerUserConfigFile = "external.config.filename"
  val ServerUserConfigResource = "external.config.resource"

  // Retry policy parameters
  val ServerRetryMaxAttempts = "config.queries.attempts"
  val ServerRetryCountWindow = "config.queries.retrycountwindow"

  // Job management settings
  val FinishedJobTTL = "config.jobs.finished.ttl_ms"

  // Host
  val Host = "akka.remote.netty.tcp.hostname"

  // Jars Repo
  val RepoJars = "config.externalJarsRepo"

  // Http Server Port
  val HttpServerPort = "config.HttpServerPort"

  val IsHazelcastProviderEnabledProperty = "config.hazelcast.enabled"
}

trait ServerConfig extends NumberActorConfig {

  val logger: Logger

  lazy val clusterName = config.getString(ServerConfig.ServerClusterNameKey)
  lazy val actorName = config.getString(ServerConfig.ServerActorNameKey)

  lazy val retryNoAttempts: Int = Try(config.getInt(ServerConfig.ServerRetryMaxAttempts)).getOrElse(0)
  lazy val retryCountWindow: Duration = Try(
    config.getDuration(ServerConfig.ServerRetryCountWindow, TimeUnit.MILLISECONDS)
  ) map (Duration(_, TimeUnit.MILLISECONDS)) getOrElse (Duration.Inf)

  lazy val completedJobTTL: Duration = extractDurationField(ServerConfig.FinishedJobTTL)

  lazy val expectedClientHeartbeatPeriod: FiniteDuration =
    extractDurationField(ServerConfig.ClientExpectedHeartbeatPeriod) match {
      case d: FiniteDuration =>
        Seq(11 seconds, d) max // Alarm period need to be at least twice the hear beat period (5 seconds)
      case _ => 2 minute // Default value
    }

  lazy val isHazelcastEnabled = config.getBoolean(ServerConfig.IsHazelcastProviderEnabledProperty)

  override val config: Config = {

    var defaultConfig = ConfigFactory.load(ServerConfig.ServerBasicConfig).getConfig(ServerConfig.ParentConfigName)
    val envConfigFile = Option(System.getProperties.getProperty(ServerConfig.ServerUserConfigFile))
    val configFile = envConfigFile.getOrElse(defaultConfig.getString(ServerConfig.ServerUserConfigFile))
    val configResource = defaultConfig.getString(ServerConfig.ServerUserConfigResource)

    if (configResource != "") {
      val resource = ServerConfig.getClass.getClassLoader.getResource(configResource)
      if (resource != null) {
        val userConfig = ConfigFactory.parseResources(configResource).getConfig(ServerConfig.ParentConfigName)
        defaultConfig = userConfig.withFallback(defaultConfig)
        logger.info("User resource (" + configResource + ") found in resources")
      } else {
        logger.warn("User resource (" + configResource + ") hasn't been found")
        val file = new File(configResource)
        if (file.exists()) {
          val userConfig = ConfigFactory.parseFile(file).getConfig(ServerConfig.ParentConfigName)
          defaultConfig = userConfig.withFallback(defaultConfig)
          logger.info("User resource (" + configResource + ") found in classpath")
        } else {
          logger.warn("User file (" + configResource + ") hasn't been found in classpath")
        }
      }
    }

    if (configFile != "") {
      val file = new File(configFile)
      if (file.exists()) {
        val parsedConfig = ConfigFactory.parseFile(file)
        if(parsedConfig.hasPath(ServerConfig.ParentConfigName)){
          val userConfig = ConfigFactory.parseFile(file).getConfig(ServerConfig.ParentConfigName)
          defaultConfig = userConfig.withFallback(defaultConfig)
          logger.info("External file (" + configFile + ") found")
        } else{
          logger.info(s"External file ($configFile) found but not configuration found under ${ServerConfig.ParentConfigName}")
        }
      } else {
        logger.warn("External file (" + configFile + ") hasn't been found")
      }
    }

    // System properties
    val systemPropertiesConfig =
      Try(
        ConfigFactory.parseProperties(System.getProperties).getConfig(ServerConfig.ParentConfigName)
      ).getOrElse(
        ConfigFactory.parseProperties(System.getProperties)
      )

    defaultConfig = systemPropertiesConfig.withFallback(defaultConfig)

    val finalConfig = {
      if (defaultConfig.hasPath("akka.cluster.server-nodes")) {
        val serverNodes = defaultConfig.getString("akka.cluster.server-nodes")
        defaultConfig.withValue(
          "akka.cluster.seed-nodes",
          ConfigValueFactory.fromIterable(serverNodes.split(",").toList))
      } else {
        defaultConfig
      }
    }

    ConfigFactory.load(finalConfig)
  }

  private def extractDurationField(key: String): Duration = Try(
    config.getDuration(key, TimeUnit.MILLISECONDS)
  ) map (FiniteDuration(_, TimeUnit.MILLISECONDS)) getOrElse (Duration.Inf)

}

