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

import com.github.nscala_time.time.Imports._
import com.stratio.crossdata.streaming.config.StreamingResourceConfig
import com.stratio.crossdata.streaming.constants.ApplicationConstants
import com.stratio.crossdata.streaming.helpers.CrossdataStatusHelper
import com.stratio.crossdata.streaming.kafka.{KafkaInput, KafkaProducer}
import ApplicationConstants._
import CrossdataStatusHelper
import com.stration.crossdata.streaming.kafka.KafkaProducer
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.crossdata.daos.EphemeralTableMapDAO
import org.apache.spark.sql.crossdata.models._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

import scala.util.{Failure, Success, Try}

class CrossdataStreaming(ephemeralTableId: String,
                         ephemeralTableName: String,
                         zookeeperConfiguration: Map[String, Any])
  extends EphemeralTableMapDAO {

  val memoryMap = Map(ZookeeperPrefixName -> zookeeperConfiguration)
  val streamingResourceConfig = new StreamingResourceConfig

  //scalastyle:off
  def init(): Unit = {
    Try {
      val zookeeperResourceConfig = streamingResourceConfig.config.getConfig(ZookeeperPrefixName) match {
        case Some(conf) => conf.toStringMap
        case None => Map.empty[String, String]
      }
      val zookeeperMergedConfig = zookeeperResourceConfig ++ zookeeperConfiguration.map { case (key, value) =>
        (key, value.toString)
      }

      CrossdataStatusHelper.setEphemeralStatus(EphemeralExecutionStatus.Starting,
        zookeeperMergedConfig, ephemeralTableId, ephemeralTableName)
      Try {
        val ephemeralTable = dao.get(ephemeralTableId).getOrElse(//throw new Exception("Table not found"))
          EphemeralTableModel(
            "6",
            "tablename",
            EphemeralOptionsModel(KafkaOptionsModel(Seq(ConnectionHostModel("localhost", "2181", "9092")),
              Seq(TopicModel("crossdata")),
              "1", Option("2")))))

        //TODO remove this
        dao.create(ephemeralTableId, ephemeralTable)

        val sparkResourceConfig = streamingResourceConfig.config.getConfig(SparkPrefixName) match {
          case Some(conf) => conf.toStringMap
          case None => Map.empty[String, String]
        }
        val kafkaResourceConfig = streamingResourceConfig.config.getConfig(KafkaPrefixName) match {
          case Some(conf) => conf.toStringMap
          case None => Map.empty[String, String]
        }
        val sparkMergedConfig = configToSparkConf(sparkResourceConfig, ephemeralTable)
        val kafkaMergedOptions = kafkaResourceConfig ++ ephemeralTable.options.kafkaOptions.additionalOptions
        val ssc = StreamingContext.getOrCreate(ephemeralTable.options.checkpointDirectory,
          () => {
            createContext(ephemeralTable,
              sparkMergedConfig,
              zookeeperMergedConfig,
              kafkaMergedOptions
            )
          })

        logger.info(s"Started Ephemeral Table: $ephemeralTableId")
        CrossdataStatusHelper.setEphemeralStatus(EphemeralExecutionStatus.Started,
          zookeeperMergedConfig, ephemeralTableId, ephemeralTableName)

        ssc.start()
        ssc.awaitTermination()
        ssc
      } match {
        case Success(_) =>
          logger.info(s"Stopping Ephemeral Table: $ephemeralTableId")
          CrossdataStatusHelper.setEphemeralStatus(EphemeralExecutionStatus.Stopped,
            zookeeperMergedConfig, ephemeralTableId, ephemeralTableName)
          CrossdataStatusHelper.close()
        case Failure(exception) =>
          logger.error(exception.getLocalizedMessage, exception)
          CrossdataStatusHelper.setEphemeralStatus(EphemeralExecutionStatus.Error,
            zookeeperMergedConfig, ephemeralTableId, ephemeralTableName)
          CrossdataStatusHelper.close()
      }
    } match {
      case Success(_) =>
        logger.info(s"Ephemeral Table Finished correctly: $ephemeralTableId")
      case Failure(exception) =>
        logger.error(exception.getLocalizedMessage, exception)
        CrossdataStatusHelper.close()
    }
  }

  private def createContext(ephemeralTable: EphemeralTableModel,
                            sparkConf: SparkConf,
                            zookeeperConf: Map[String, String],
                            kafkaConf: Map[String, String]): StreamingContext = {
    val sparkStreamingWindow = ephemeralTable.options.atomicWindow
    val sparkContext = new SparkContext(sparkConf)
    val streamingContext = new StreamingContext(sparkContext, Seconds(sparkStreamingWindow))
    val sqlContext = new SQLContext(sparkContext)
    streamingContext.checkpoint(ephemeralTable.options.checkpointDirectory)
    val kafkaOptions = ephemeralTable.options.kafkaOptions.copy(additionalOptions = kafkaConf)
    val kafkaInput = new KafkaInput(kafkaOptions)
    val kafkaDStream = kafkaInput.createStream(streamingContext).map { case (_, kafkaEvent) =>
      (DateTime.now.getMillis, kafkaEvent)
    }.window(Seconds(ephemeralTable.options.maxWindow), Seconds(ephemeralTable.options.atomicWindow))

    kafkaDStream.foreachRDD(rdd => {

      println("RDD YEEEEEEEEEEEAHHH")

      if (rdd.take(1).length > 0) {
        val streamingQueries = CrossdataStatusHelper.queriesFromEphemeralTable(zookeeperConf, ephemeralTable.id)
        if (streamingQueries.nonEmpty) {
          streamingQueries.foreach(streamingQueryModel => {
            val rddFiltered = rdd.flatMap { case (time, row) =>
              if (time > DateTime.now.getMillis - streamingQueryModel.window * 1000) Some(row)
              else None
            }
            val df = sqlContext.read.json(rddFiltered)
            df.registerTempTable(s"${ephemeralTable.name}${streamingQueryModel.id}")
            val query = streamingQueryModel.sql.replaceAll(ephemeralTable.name,
              s"${ephemeralTable.name}${streamingQueryModel.id}")
            val dataFrame = sqlContext.sql(query)
            val topic = streamingQueryModel.alias.getOrElse(streamingQueryModel.id)

            ephemeralTable.options.outputFormat match {
              case EphemeralOutputFormat.JSON =>
                dataFrame.toJSON.foreachPartition(values =>
                  values.foreach(value => KafkaProducer.put(topic, value, kafkaOptions, kafkaOptions.partition)))
              case _ => dataFrame.rdd.foreachPartition(values =>
                values.foreach(value => KafkaProducer.put(topic, value.mkString, kafkaOptions, kafkaOptions.partition)))
            }
          })
        }
      }
    })
    streamingContext
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
    sparkConfig.map(entry => {
      if (entry._1.startsWith(SparkPrefixName)) entry
      else (s"$SparkPrefixName.${entry._1}", entry._2)
    })
}

