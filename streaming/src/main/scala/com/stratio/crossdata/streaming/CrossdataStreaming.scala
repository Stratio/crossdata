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

package com.stratio.crossdata.streaming

import com.stratio.crossdata.streaming.config.StreamingResourceConfig
import com.stratio.crossdata.streaming.constants.ApplicationConstants._
import com.stratio.crossdata.streaming.helpers.CrossdataStatusHelper
import com.stratio.crossdata.streaming.helpers.CrossdataStreamingHelper
import org.apache.spark.SparkConf
import org.apache.spark.sql.crossdata.daos.EphemeralTableMapDAO
import org.apache.spark.sql.crossdata.models._
import org.apache.spark.streaming.StreamingContext

import scala.util.Failure
import scala.util.Success
import scala.util.Try

class CrossdataStreaming(ephemeralTableName: String,
                         zookeeperConfiguration: Map[String, Any])
  extends EphemeralTableMapDAO {

  val memoryMap = Map(ZookeeperPrefixName -> zookeeperConfiguration)
  val streamingResourceConfig = new StreamingResourceConfig

  //TODO remove this
  /*dao.upsert("tablename2", EphemeralTableModel(
    "tablename2",
    EphemeralOptionsModel(KafkaOptionsModel(Seq(ConnectionHostModel("localhost", "2181", "9092")),
      Seq(TopicModel("crossdatajc")),
      "1"))))*/

  //dao.create("crossdataquery", EphemeralQueryModel("tablename2", "select * from tablename2", "crossdataquery"))

  def init(): Unit = {
    Try {
      val zookeeperResourceConfig = streamingResourceConfig.config.getConfig(ZookeeperPrefixName) match {
          case Some(conf) => conf.toStringMap
          case None => Map.empty
        }

      val zookeeperMergedConfig = zookeeperResourceConfig ++ zookeeperConfiguration.map { case (key, value) =>
        (key, value.toString)
      }

      CrossdataStatusHelper.setEphemeralStatus(
        EphemeralExecutionStatus.Starting,
        zookeeperMergedConfig,
        ephemeralTableName)

      Try {
        val ephemeralTable = dao.get(ephemeralTableName).getOrElse(throw new Exception("Ephemeral table not found"))
        val sparkResourceConfig: Map[String, String] = streamingResourceConfig.config.getConfig(SparkPrefixName) match {
          case Some(conf) => conf.toStringMap
          case None => Map.empty
        }
        val kafkaResourceConfig = streamingResourceConfig.config.getConfig(KafkaPrefixName) match {
          case Some(conf) => conf.toStringMap
          case None => Map.empty
        }
        val sparkMergedConfig = configToSparkConf(sparkResourceConfig, ephemeralTable)
        val kafkaMergedOptions = kafkaResourceConfig ++ ephemeralTable.options.kafkaOptions.additionalOptions

        val ssc = StreamingContext.getOrCreate(ephemeralTable.options.checkpointDirectory,
          () => {
            CrossdataStreamingHelper.createContext(ephemeralTable,
              sparkMergedConfig,
              zookeeperMergedConfig,
              kafkaMergedOptions
            )
          })

        logger.info(s"Started Ephemeral Table: $ephemeralTableName")
        CrossdataStatusHelper.setEphemeralStatus(
          EphemeralExecutionStatus.Started,
          zookeeperMergedConfig,
          ephemeralTableName
        )

        ssc.start()
        ssc.awaitTermination()

      } match {
        case Success(_) =>
          logger.info(s"Stopping Ephemeral Table: $ephemeralTableName")
          CrossdataStatusHelper.setEphemeralStatus(
            EphemeralExecutionStatus.Stopped,
            zookeeperMergedConfig,
            ephemeralTableName
          )
          CrossdataStatusHelper.close()
        case Failure(exception) =>
          logger.error(exception.getLocalizedMessage, exception)
          CrossdataStatusHelper.setEphemeralStatus(
            EphemeralExecutionStatus.Error,
            zookeeperMergedConfig,
            ephemeralTableName
          )
          CrossdataStatusHelper.close()
      }
    } match {
      case Success(_) =>
        logger.info(s"Ephemeral Table Finished correctly: $ephemeralTableName")
      case Failure(exception) =>
        logger.error(exception.getMessage, exception)
        CrossdataStatusHelper.close()
    }
  }

  private def configToSparkConf(generalConfig: Map[String, String],
                                ephemeralTable: EphemeralTableModel): SparkConf = {
    val conf = new SparkConf()

    conf.setAll(setPrefixSpark(generalConfig))
    conf.setAll(setPrefixSpark(ephemeralTable.options.sparkOptions))
    conf.set(SparkNameKey, {
      if (conf.contains(SparkNameKey)) s"${conf.get(SparkNameKey)}-${ephemeralTable.name}"
      else ephemeralTable.name
    })
    conf
  }

  private def setPrefixSpark(sparkConfig: Map[String, String]): Map[String, String] =
    sparkConfig.map { case entry@(key, value) =>
      if (key.startsWith(SparkPrefixName)) entry
      else (s"$SparkPrefixName.${entry._1}", value)
    }
}

