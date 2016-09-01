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
package com.stratio.crossdata.streaming.helpers

import com.github.nscala_time.time.Imports._
import com.stratio.common.utils.components.logger.impl.SparkLoggerComponent
import com.stratio.crossdata.streaming.constants.KafkaConstants._
import com.stratio.crossdata.streaming.kafka.{KafkaInput, KafkaProducer}
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.crossdata.models._
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

import scala.util.{Failure, Try}

object CrossdataStreamingHelper extends SparkLoggerComponent {

  def createContext(ephemeralTable: EphemeralTableModel,
                    sparkConf: SparkConf,
                    zookeeperConf: Map[String, String],
                    crossdataCatalogConf: Map[String, String]): StreamingContext = {
    val sparkStreamingWindow = ephemeralTable.options.atomicWindow
    val sparkContext = SparkContext.getOrCreate(sparkConf)

    // This value is managed only inside the scope of the Spark Driver.
    // Therefore, this value never reaches the Spark workers.
    val countdowns = collection.mutable.Map[String, Int]()

    val streamingContext = new StreamingContext(sparkContext, Seconds(sparkStreamingWindow))

    streamingContext.checkpoint(ephemeralTable.options.checkpointDirectory)

    val kafkaOptions = ephemeralTable.options.kafkaOptions
    val kafkaInput = new KafkaInput(kafkaOptions)
    val kafkaDStream = toWindowDStream(kafkaInput.createStream(streamingContext), ephemeralTable.options)

    // DStream.foreachRDD is a method that is executed in the Spark Driver if and when an output action is not called
    // over a RDD. Thus, the value countdowns can be used inside.
    // More information here:
    // http://spark.apache.org/docs/latest/streaming-programming-guide.html#design-patterns-for-using-foreachrdd
    kafkaDStream.foreachRDD { rdd =>

      if (rdd.take(1).length > 0) {
        val ephemeralQueries = CrossdataStatusHelper.queriesFromEphemeralTable(zookeeperConf, ephemeralTable.name)

        if (ephemeralQueries.nonEmpty) {
          ephemeralQueries.foreach( ephemeralQuery => {
            val alias = ephemeralQuery.alias
            if(!countdowns.contains(alias)){
              countdowns.put(alias, (ephemeralQuery.window / sparkStreamingWindow))
            }
            countdowns.put(alias, countdowns.get(alias).getOrElse(0)-1)
            logDebug(s"Countdowns: ${countdowns.mkString(", ")}")

            countdowns.get(alias) foreach {
              case 0 => {
                countdowns.put(alias, (ephemeralQuery.window / sparkStreamingWindow))
                logInfo(s"Executing streaming query $alias")
                executeQuery(rdd, ephemeralQuery, ephemeralTable, kafkaOptions, zookeeperConf, crossdataCatalogConf)
              }
              case countdown =>
                logDebug(s"Current countdown for $alias: $countdown")
            }
          })
        }
      }
    }
    streamingContext
  }

  def parseCatalogConfig(catalogConf: Map[String, String]): Option[Config] = {
    import collection.JavaConversions._
    Try(ConfigFactory.parseMap(catalogConf)).toOption
  }

  private def executeQuery(rdd: RDD[(Long, String)],
                           ephemeralQuery: EphemeralQueryModel,
                           ephemeralTable: EphemeralTableModel,
                           kafkaOptions: KafkaOptionsModel,
                           zookeeperConf: Map[String, String],
                           catalogConf: Map[String, String]): Unit = {
    val sqlTableName = ephemeralTable.name
    val query = ephemeralQuery.sql
    val kafkaOptionsMerged = mergeKafkaOptions(ephemeralQuery, kafkaOptions)
    val xdContext = XDContext.getOrCreate(rdd.context, parseCatalogConfig(catalogConf))
    // TODO add sampling ratio
    val dfReader = xdContext.read
    val dfReaderWithschema = ephemeralTable.schema.map(dfReader.schema).getOrElse(dfReader)
    val df = dfReaderWithschema.json(filterRddWithWindow(rdd, ephemeralQuery.window))
    //df.cache()
    if (df.head(1).length > 0) {
      df.registerTempTable(sqlTableName)
      Try {
        val dataFrame = xdContext.sql(query)
        val topic = ephemeralQuery.alias

        ephemeralTable.options.outputFormat match {
          case EphemeralOutputFormat.JSON => saveToKafkaInJSONFormat(dataFrame, topic, kafkaOptionsMerged)
          case _ => saveToKafkaInRowFormat(dataFrame, topic, kafkaOptionsMerged)
        }
      } match {
        case Failure(throwable) =>
          logger.warn(
            s"""|There are problems executing the ephemeral query: $query
                |with Schema: ${df.printSchema()}
                |and the first row is: ${df.show(1)}
                |Exception message: ${throwable.getMessage}
                |Exception stackTrace: ${throwable.getStackTraceString}
           """.stripMargin
          )
        case _ =>
      }
      xdContext.dropTempTable(sqlTableName)
    }
  }

  private[streaming] def mergeKafkaOptions(ephemeralQuery: EphemeralQueryModel,
                                           kafkaOptions: KafkaOptionsModel): KafkaOptionsModel = {
    kafkaOptions.copy(
      partitionOutput = ephemeralQuery.options.get(PartitionKey).orElse(kafkaOptions.partitionOutput),
      additionalOptions = kafkaOptions.additionalOptions ++ ephemeralQuery.options)
  }

  private[streaming] def filterRddWithWindow(rdd: RDD[(Long, String)], window: Int): RDD[String] =
    rdd.flatMap { case (time, row) =>
      if (time > DateTime.now.getMillis - window * 1000) Option(row)
      else None
    }

  private[streaming] def toWindowDStream(inputStream: DStream[(String, String)],
                                         ephemeralOptions: EphemeralOptionsModel): DStream[(Long, String)] =
  // TODO window per query?
    inputStream.mapPartitions { iterator =>
      val dateTime = DateTime.now.getMillis
      iterator.map { case (_, kafkaEvent) => (dateTime, kafkaEvent) }
    }.window(Seconds(ephemeralOptions.maxWindow), Seconds(ephemeralOptions.atomicWindow))

  private[streaming] def saveToKafkaInJSONFormat(dataFrame: DataFrame, topic: String, kafkaOptions: KafkaOptionsModel): Unit =
    dataFrame.toJSON.foreachPartition(values =>
      values.foreach(value => KafkaProducer.put(topic, value, kafkaOptions, kafkaOptions.partitionOutput)))

  private[streaming] def saveToKafkaInRowFormat(dataFrame: DataFrame, topic: String, kafkaOptions: KafkaOptionsModel): Unit =
    dataFrame.rdd.foreachPartition(values =>
      values.foreach(value =>
        KafkaProducer.put(topic, value.mkString(","), kafkaOptions, kafkaOptions.partitionOutput)))
}
