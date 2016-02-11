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

import com.stratio.crossdata.streaming.constants.ApplicationConstants._
import com.stratio.crossdata.streaming.helpers.{CrossdataStatusHelper, CrossdataStreamingHelper}
import org.apache.spark.SparkConf
import org.apache.spark.sql.crossdata.daos.EphemeralTableMapDAO
import org.apache.spark.sql.crossdata.models._
import org.apache.spark.streaming.StreamingContext

import scala.util.{Failure, Success, Try}

class CrossdataStreaming(ephemeralTableName: String,
                         zookeeperConfiguration: Map[String, Any])
  extends EphemeralTableMapDAO {

  val memoryMap = Map(ZookeeperPrefixName -> zookeeperConfiguration)

  //TODO remove this
  /*dao.upsert("ephtable", EphemeralTableModel(
    "ephtable",
    EphemeralOptionsModel(KafkaOptionsModel(Seq(ConnectionHostModel("localhost", "2181", "9092")),Seq(TopicModel("ephtable")), "1")))
  )*/


  def init(): Unit = {
    Try {
      val zookeeperConfig = zookeeperConfiguration.mapValues(_.toString)

      CrossdataStatusHelper.setEphemeralStatus(
        EphemeralExecutionStatus.Starting,
        zookeeperConfig,
        ephemeralTableName)

      Try {
        val ephemeralTable = dao.get(ephemeralTableName).getOrElse(throw new Exception("Ephemeral table not found"))

        val sparkConfig = configToSparkConf(ephemeralTable)

        val kafkaConfig = ephemeralTable.options.kafkaOptions

        val ssc = StreamingContext.getOrCreate(ephemeralTable.options.checkpointDirectory,
          () => {
            CrossdataStreamingHelper.createContext(ephemeralTable,
              sparkConfig,
              zookeeperConfig
            )
          })

        logger.info(s"Started Ephemeral Table: $ephemeralTableName")
        CrossdataStatusHelper.setEphemeralStatus(
          EphemeralExecutionStatus.Started,
          zookeeperConfig,
          ephemeralTableName
        )

        ssc.start()
        ssc.awaitTermination()

      } match {
        case Success(_) =>
          logger.info(s"Stopping Ephemeral Table: $ephemeralTableName")
          CrossdataStatusHelper.setEphemeralStatus(
            EphemeralExecutionStatus.Stopped,
            zookeeperConfig,
            ephemeralTableName
          )
          CrossdataStatusHelper.close()
        case Failure(exception) =>
          logger.error(exception.getLocalizedMessage, exception)
          CrossdataStatusHelper.setEphemeralStatus(
            EphemeralExecutionStatus.Error,
            zookeeperConfig,
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

  private def configToSparkConf(ephemeralTable: EphemeralTableModel): SparkConf =
    new SparkConf().setAll(setPrefixSpark(ephemeralTable.options.sparkOptions))


  private def setPrefixSpark(sparkConfig: Map[String, String]): Map[String, String] =
    sparkConfig.map { case entry@(key, value) =>
      if (key.startsWith(SparkPrefixName)) entry
      else (s"$SparkPrefixName.$key", value)
    }
}

