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
import org.apache.spark.sql.crossdata.config.StreamingConstants
import org.apache.spark.sql.crossdata.config.StreamingConstants._

import scala.collection.JavaConversions._
import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source
import scala.util.{Failure, Properties, Success, Try}

class SparkJobLauncher(crossdataConfig: Config, streamingCatalog: XDStreamingCatalog) extends Logging{

  // TODO validate
  private val streamingConfig = crossdataConfig.getConfig(StreamingConfPath)

  def doInitSparkStreamingJob(ephemeralTableName: String)(implicit executionContext: ExecutionContext): Unit = {
    Try {
      // TODO logInfo("Init spark launcher: "+ephemeralTableName)
      validateSparkHome()
      val eTable = streamingCatalog.getEphemeralTable(ephemeralTableName).getOrElse(notFound(ephemeralTableName))
      // TODO read appName from config
      val appName = s"${eTable.name}_${UUID.randomUUID()}"
      val appArgs = Seq(eTable.name,streamingConfig.getString(ZooKeeperConnectionKey))
      val master = streamingConfig.getString("spark.master")
      val jar = streamingConfig.getString(AppJarKey)
      val jars = Seq.empty
      //val jars = streamingConfig.getStringList(ExternalJarsKey).toList
      val sparkArgs: Map[String,String] = Map.empty

      launch(StreamingConstants.MainClass, appArgs, appName, master, jar, jars, sparkArgs)(executionContext)
      //val appMain =
      //launch()
      // ARGS (Zookeeper connection ++ EphimeralTableName)
/*      private val PluginsJarsPath = s"$BasePath/${HdfsConfig.getString(AppConstant.PluginsFolder)}/"
      private val Master = ClusterConfig.getString(AppConstant.Master)*/
      // TODO launch(SparktaDriver, hdfsDriverPath, Master, sparkArgs, driverParams)
    } match {
      case Failure(exception) =>
        logError(exception.getMessage, exception)
        // TODO handle error
      case Success(_) => {
        //TODO info
      }
    }
  }

  private def notFound(name: String) = sys.error(s"$name not found")

  private def launch(appMain: String,
                     appArgs: Seq[String],
                     appName: String,
                     master: String,
                     jar: String,
                     externalJars: Seq[String],
                     sparkArgs: Map[String, String] = Map.empty
                      )(executionContext: ExecutionContext): Unit = {
    val sparkLauncher = new SparkLauncher()
      .setSparkHome(sparkHome)
      .setAppName(appName)
      .setAppResource(jar)
      .setMainClass(appMain)
      .addAppArgs(appArgs:_*)
      .setMaster(master)
      .setDeployMode("cluster")

    externalJars.foreach(sparkLauncher.addJar)
    //Spark params (everything starting with spark.)
    sparkConf.map({ case (key: String, value: String) => sparkLauncher.setConf(key, value) })

    val sparkProcess = Try(sparkLauncher.launch()) match {
      case Success(process) => process
      case Failure(exception) => 
        logError(exception.getMessage)
        throw exception
    }

    val launchedProcess = Future [Int]{
      sparkProcess.waitFor()
    } (executionContext)

    launchedProcess.onSuccess {
      case 0 => logInfo("Spark process exited successfully")
      case exitCode =>

        logError(s"Spark process exited with code $exitCode")

        val errorLines = for {
          is <- Try(sparkProcess.getErrorStream)
          source = Source.fromInputStream(is)
        } yield source.getLines()

        errorLines.foreach{ lines =>
          lines.foreach(line => logError(line))
        }
    }(executionContext)

    launchedProcess.onFailure{
      case throwable =>
        logError(throwable.getMessage)
    }(executionContext)

  }

  // TODO require, optional => filter without sparkconfpath
  private def sparkConf: Seq[(String, String)] = {
    val conf = streamingConfig.getConfig(SparkConfPath)
    conf.entrySet().toSeq
      .map(e => (s"$SparkConfPath.${e.getKey}", conf.getAnyRef(e.getKey).toString))
  }


  private def sparkHome: String = Properties.envOrElse("SPARK_HOME", streamingConfig.getString(SparkHomeKey))

  /**
   * Checks if we have a valid Spark home.
   */
  private def validateSparkHome(): Unit = require(Try(sparkHome).isSuccess,
    "You must set the $SPARK_HOME path in configuration or environment")

}