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
package org.apache.spark.sql.crossdata.launcher

import java.io.File
import java.util.UUID

import com.google.common.io.BaseEncoding
import com.stratio.common.utils.components.logger.impl.SparkLoggerComponent
import com.stratio.crossdata.util.HdfsUtils
import com.typesafe.config.{Config, ConfigRenderOptions}
import org.apache.spark.launcher.SparkLauncher
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.crossdata.config.CoreConfig._
import org.apache.spark.sql.crossdata.config.StreamingConstants._
import org.apache.spark.sql.crossdata.config.{CoreConfig, StreamingConstants}
import org.apache.spark.sql.crossdata.serializers.CrossdataSerializer

import scala.collection.JavaConversions._
import scala.concurrent.ExecutionContext
import scala.util.{Properties, Try}

object SparkJobLauncher extends SparkLoggerComponent with CrossdataSerializer {

  def getSparkStreamingJob(xdContext: XDContext,
                           crossdataConfig: Config,
                           ephemeralTableName: String)(
      implicit executionContext: ExecutionContext): Try[SparkJob] = Try {
    val streamingConfig = crossdataConfig.getConfig(StreamingConfPath)
    val launcherConfig = crossdataConfig.getConfig(LauncherKey)
    val sparkHome = Properties
      .envOrNone("SPARK_HOME")
      .orElse(Try(launcherConfig.getString(SparkHomeKey)).toOption)
      .getOrElse(
          throw new RuntimeException(
              "You must set the $SPARK_HOME path in configuration or environment")
      )

    val eTable = xdContext.catalog
      .getEphemeralTable(ephemeralTableName)
      .getOrElse(notFound(ephemeralTableName))
    val appName = s"${eTable.name}_${UUID.randomUUID()}"
    val zkConfigEncoded: String =
      encode(render(streamingConfig, ZooKeeperStreamingCatalogPath))
    val catalogConfigEncoded: String =
      encode(render(crossdataConfig, CoreConfig.CatalogConfigKey))
    val appArgs = Seq(eTable.name, zkConfigEncoded, catalogConfigEncoded)
    val master = streamingConfig.getString(SparkMasterKey)
    val jar = streamingConfig.getString(AppJarKey)
    val jars = Try(streamingConfig.getStringList(ExternalJarsKey).toSeq)
      .getOrElse(Seq.empty)
    val sparkConfig: Map[String, String] = sparkConf(streamingConfig)
    if (master.toLowerCase.contains("mesos")) {
      val hdfsPath = getHdfsPath(crossdataConfig, jar)
      getJob(sparkHome,
             StreamingConstants.MainClass,
             appArgs,
             appName,
             master,
             hdfsPath,
             sparkConfig,
             jars)(executionContext)

    } else {
      getJob(sparkHome,
             StreamingConstants.MainClass,
             appArgs,
             appName,
             master,
             jar,
             sparkConfig,
             jars)(executionContext)
    }

  }

  /**
    * This method return the HDFS path of the streaming jar and if not exists previously it writes the jar in HDFS
    *
    * @param crossdataConfig The config
    * @param jar             The local path of the streaming jar
    * @return a String with the HDFS path
    */
  private def getHdfsPath(crossdataConfig: Config, jar: String): String = {
    val hdfsConf = crossdataConfig.getConfig(HdfsConf)
    val user = hdfsConf.getString("hadoopUserName")
    val hdfsMaster = hdfsConf.getString("hdfsMaster")
    val destPath = s"/user/$user/streamingJar/"

    val hdfsUtil = HdfsUtils(hdfsConf)

    //send to HDFS if not exists
    val jarName = new File(jar).getName
    if (!hdfsUtil.fileExist(s"$destPath/$jarName")) {
      hdfsUtil.write(jar, destPath)
    }
    s"hdfs://$hdfsMaster/$destPath/$jarName"
  }

  def getSparkJob(launcherConfig: Config,
                  master: String,
                  main: String,
                  args: Seq[String],
                  jar: String,
                  appName: String,
                  submitOptions: Option[Map[String, String]])(
      implicit executionContext: ExecutionContext): Try[SparkJob] = Try {

    val sparkHome = Properties
      .envOrNone("SPARK_HOME")
      .orElse(Try(launcherConfig.getString(SparkHomeKey)).toOption)
      .getOrElse(
          throw new RuntimeException(
              "You must set the $SPARK_HOME path in configuration or environment")
      )

    //due to the parser doesn't allow middle-score symbol and spark submit properties are all with that, we are using '.' instead of '-'. So now we map to '-' again
    val sparkConfig = submitOptions.getOrElse(Map.empty) map {
      case (k, v) =>
        val key = k.replaceAll("\\.", "-")
        ("spark." + key, v)
    }

    getJob(sparkHome, main, args, appName, master, jar, sparkConfig)(
        executionContext)
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
                     externalJars: Seq[String] = Seq.empty)(
      executionContext: ExecutionContext): SparkJob = {
    val sparkLauncher = new SparkLauncher()
      .setSparkHome(sparkHome)
      .setAppName(appName)
      .setAppResource(jar)
      .setMainClass(appMain)
      .addAppArgs(appArgs: _*)
      .setMaster(master)
      .setDeployMode("cluster")
    // TODO startApplication(listener) since 1.6 preferred => Spark 2.0
    externalJars.foreach(sparkLauncher.addJar)
    sparkConf.map({ case (key, value) => sparkLauncher.setConf(key, value) })
    sparkLauncher.launch()
    new SparkJob(sparkLauncher)(executionContext)
  }

  private def notFound(resource: String) = sys.error(s"$resource not found")

  private def sparkConf(streamingConfig: Config): Map[String, String] =
    typeSafeConfigToMapString(streamingConfig, Option(SparkConfPath))

  private def typeSafeConfigToMapString(
      config: Config,
      path: Option[String] = None): Map[String, String] = {
    val conf = path.map(config.getConfig).getOrElse(config)
    conf
      .entrySet()
      .toSeq
      .map(e =>
            (s"${path.fold("")(_ + ".") + e.getKey}",
             conf.getAnyRef(e.getKey).toString))
      .toMap
  }

  private def render(config: Config, path: String): String =
    config
      .getConfig(path)
      .atPath(path)
      .root
      .render(ConfigRenderOptions.concise)

  private def encode(value: String): String =
    BaseEncoding.base64().encode(value.getBytes)
}
