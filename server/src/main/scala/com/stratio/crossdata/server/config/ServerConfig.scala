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

import com.typesafe.config.{ConfigValueFactory, Config, ConfigFactory}
import scala.collection.JavaConversions._
import org.apache.log4j.Logger

import scala.concurrent.duration._
import scala.util.Try

object ServerConfig {
  val ServerBasicConfig = "server-reference.conf"
  val ParentConfigName = "crossdata-server"


  val SparkSqlConfigPrefix = "config.spark.sql"

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
  val repoJars = "config.externalJarsRepo"

  // Http Server Port
  val httpServerPort = "config.HttpServerPort"
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
      case d: FiniteDuration => d
      case _ => 1 minute
    }

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
        val userConfig = ConfigFactory.parseFile(file).getConfig(ServerConfig.ParentConfigName)
        defaultConfig = userConfig.withFallback(defaultConfig)
        logger.info("External file (" + configFile + ") found")
      } else {
        logger.warn("External file (" + configFile + ") hasn't been found")
      }
    }

    // System properties
    defaultConfig = ConfigFactory.parseProperties(System.getProperties).withFallback(defaultConfig)

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

