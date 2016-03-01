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
package com.stratio.crossdata.launcher

import java.util.UUID

import com.typesafe.config.Config
import org.apache.spark.Logging
import org.apache.spark.launcher.SparkLauncher
import org.apache.spark.sql.crossdata.catalog.XDStreamingCatalog
import org.apache.spark.sql.crossdata.config.StreamingConstants._
import org.apache.spark.sql.crossdata.config.{CoreConfig, StreamingConstants}
import org.apache.spark.sql.crossdata.serializers.CrossdataSerializer
import org.json4s.jackson.Serialization._

import scala.collection.JavaConversions._
import scala.concurrent.ExecutionContext
import scala.util.{Properties, Try}

object SparkJobLauncher extends Logging with CrossdataSerializer{

  def getSparkStreamingJob(crossdataConfig: Config, streamingCatalog: XDStreamingCatalog, ephemeralTableName: String)
                          (implicit executionContext: ExecutionContext): Try[SparkJob] = Try {
    val streamingConfig = crossdataConfig.getConfig(StreamingConfPath)
    val sparkHome =
      Properties.envOrNone("SPARK_HOME").orElse(Try(streamingConfig.getString(SparkHomeKey)).toOption).getOrElse(
        throw new RuntimeException("You must set the $SPARK_HOME path in configuration or environment")
      )

    val catalogPropertiesMapString = typeSafeConfigToMapString(crossdataConfig, Option(CoreConfig.CatalogConfigKey))
    val zooKeeperPropertiesMapString = typeSafeConfigToMapString(streamingConfig, Option(ZooKeeperStreamingCatalogPath))
    val eTable = streamingCatalog.getEphemeralTable(ephemeralTableName).getOrElse(notFound(ephemeralTableName))
    val appName = s"${eTable.name}_${UUID.randomUUID()}"
    val appArgs = Seq(eTable.name, write(zooKeeperPropertiesMapString), write(catalogPropertiesMapString))
    val master = streamingConfig.getString(SparkMasterKey)
    val jar = streamingConfig.getString(AppJarKey)
    val jars = Try(streamingConfig.getStringList(ExternalJarsKey).toSeq).getOrElse(Seq.empty)
    val sparkConfig: Map[String, String] = sparkConf(streamingConfig)

    getJob(sparkHome, StreamingConstants.MainClass, appArgs, appName, master, jar, sparkConfig, jars)(executionContext)
  }

  def launchJob(sparkJob: SparkJob): Unit = {
    sparkJob.submit()
  }

  private def getJob(sparkHome: String,
                     appMain: String,
                     appArgs: Seq[String],
                     appName: String,
                     master: String,
                     jar: String,
                     sparkConf: Map[String, String] = Map.empty,
                     externalJars: Seq[String] = Seq.empty
                      )(executionContext: ExecutionContext): SparkJob = {
    val sparkLauncher = new SparkLauncher()
      .setSparkHome(sparkHome)
      .setAppName(appName)
      .setAppResource(jar)
      .setMainClass(appMain)
      .addAppArgs(appArgs: _*)
      .setMaster(master)
      .setDeployMode("cluster")
    externalJars.foreach(sparkLauncher.addJar)
    sparkConf.map({ case (key, value) => sparkLauncher.setConf(key, value) })
    new SparkJob(sparkLauncher)(executionContext)
  }

  private def notFound(resource: String) = sys.error(s"$resource not found")

  private def sparkConf(streamingConfig: Config): Map[String, String] =
    typeSafeConfigToMapString(streamingConfig, Option(SparkConfPath))


  private def typeSafeConfigToMapString(config: Config, path: Option[String]= None): Map[String, String] = {
    val conf = path.map(config.getConfig).getOrElse(config)
    conf.entrySet().toSeq.map( e =>
      (s"${path.fold("")(_+".")+ e.getKey}", conf.getAnyRef(e.getKey).toString)
    ).toMap
  }



}