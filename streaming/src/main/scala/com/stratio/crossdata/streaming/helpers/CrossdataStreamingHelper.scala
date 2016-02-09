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

package com.stratio.crossdata.streaming.helpers


import com.github.nscala_time.time.Imports._
import com.stratio.common.utils.components.logger.impl.SparkLoggerComponent
import com.stratio.crossdata.streaming.constants.KafkaConstants._
import com.stratio.crossdata.streaming.kafka.{KafkaInput, KafkaProducer}
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.crossdata.XDContext._
import org.apache.spark.sql.crossdata.models._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.DStream

import scala.collection.JavaConversions._
import scala.util.Try

object CrossdataStreamingHelper extends SparkLoggerComponent {


  def createContext(ephemeralTable: EphemeralTableModel,
                    sparkConf: SparkConf,
                    zookeeperConf: Map[String, String],
                    kafkaConf: Map[String, String]): StreamingContext = {
    val sparkStreamingWindow = ephemeralTable.options.atomicWindow
    val sparkContext = new SparkContext(sparkConf)
    val streamingContext = new StreamingContext(sparkContext, Seconds(sparkStreamingWindow))

    streamingContext.checkpoint(ephemeralTable.options.checkpointDirectory)

    val kafkaOptions = ephemeralTable.options.kafkaOptions.copy(additionalOptions = kafkaConf)
    val kafkaInput = new KafkaInput(kafkaOptions)
    val kafkaDStream = toWindowDStream(kafkaInput.createStream(streamingContext), ephemeralTable.options)

    kafkaDStream.foreachRDD { rdd =>
      if (rdd.take(1).length > 0) {
        val ephemeralQueries = CrossdataStatusHelper.queriesFromEphemeralTable(zookeeperConf, ephemeralTable.name)

        if (ephemeralQueries.nonEmpty) {
          ephemeralQueries.foreach(ephemeralQuery =>
            executeQuery(rdd, ephemeralQuery, ephemeralTable, kafkaOptions, zookeeperConf)
          )
        }
      }
    }
    streamingContext
  }

  private def parseZookeeperCatalogConfig(zookeeperConf: Map[String, String]): Option[Config] = {
    val zookeeperCatalogConfig = Map(CatalogClassConfigKey -> ZookeeperClass) ++
      zookeeperConf.map { case (key, value) => s"$CatalogConfigKey.$key" -> value }
    Try(ConfigFactory.parseMap(zookeeperCatalogConfig)).toOption
  }

  private def executeQuery(rdd: RDD[(Long, String)],
                           ephemeralQuery: EphemeralQueryModel,
                           ephemeralTable: EphemeralTableModel,
                           kafkaOptions: KafkaOptionsModel,
                           zookeeperConf: Map[String, String]): Unit = {

    val kafkaOptionsMerged = kafkaOptions.copy(
      partitionOutput = ephemeralQuery.options.get(PartitionKey).orElse(kafkaOptions.partitionOutput),
      additionalOptions = ephemeralQuery.options)
    val xdContext = XDContext.getOrCreate(rdd.context, parseZookeeperCatalogConfig(zookeeperConf))
    // TODO add optional schema => xdContext.read.schema; at least -> sampling ratio
    val df = xdContext.read.json(filterRddWithWindow(rdd, ephemeralQuery.window))
    //df.cache()
    if (df.head(1).length > 0) {
      val sqlTableName = s"${ephemeralTable.name}${ephemeralQuery.alias}${DateTime.now.getMillis}"
      df.registerTempTable(sqlTableName)
      val query = ephemeralQuery.sql.replaceAll(ephemeralTable.name, sqlTableName)
      Try {
        val dataFrame = xdContext.sql(query)
        val topic = ephemeralQuery.alias

        ephemeralTable.options.outputFormat match {
          case EphemeralOutputFormat.JSON => saveToKafkaInJSONFormat(dataFrame, topic, kafkaOptionsMerged)
          case _ => saveToKafkaInRowFormat(dataFrame, topic, kafkaOptionsMerged)
        }
        xdContext.dropTempTable(sqlTableName)
      }.getOrElse {
        logger.warn(s"There are problems executing the ephemeral query: $query \n with Schema: ${df.printSchema()} \n" +
          s" and the first row is: ${df.show(1)}")
      }
    }
  }

  private def filterRddWithWindow(rdd: RDD[(Long, String)], window: Int): RDD[String] =
    rdd.flatMap { case (time, row) =>
      if (time > DateTime.now.getMillis - window * 1000) Option(row)
      else None
    }

  private def toWindowDStream(inputStream: DStream[(String, String)],
                              ephemeralOptions: EphemeralOptionsModel): DStream[(Long, String)] =
    // TODO window per query?
    inputStream.mapPartitions{ iterator =>
      val dateTime = DateTime.now.getMillis
      iterator.map{ case (_, kafkaEvent) => (dateTime, kafkaEvent)}
    }.window(Seconds(ephemeralOptions.maxWindow), Seconds(ephemeralOptions.atomicWindow))

  private def saveToKafkaInJSONFormat(dataFrame: DataFrame, topic: String, kafkaOptions: KafkaOptionsModel): Unit =
    dataFrame.toJSON.foreachPartition(values =>
      values.foreach(value => KafkaProducer.put(topic, value, kafkaOptions, kafkaOptions.partitionOutput)))

  private def saveToKafkaInRowFormat(dataFrame: DataFrame, topic: String, kafkaOptions: KafkaOptionsModel): Unit =
    dataFrame.rdd.foreachPartition(values =>
      values.foreach(value =>
        KafkaProducer.put(topic, value.mkString(","), kafkaOptions, kafkaOptions.partitionOutput)))
}
