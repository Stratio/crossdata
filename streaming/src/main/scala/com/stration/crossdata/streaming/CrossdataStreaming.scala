/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stration.crossdata.streaming

import com.github.nscala_time.time.Imports._
import com.stration.crossdata.streaming.config.StreamingResourceConfig
import com.stration.crossdata.streaming.constants.ApplicationConstants._
import com.stration.crossdata.streaming.helpers.StreamingQueriesHelper
import com.stration.crossdata.streaming.kafka.{KafkaInput, KafkaProducer}
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.crossdata.daos.EphemeralTableMapDAOComponent
import org.apache.spark.sql.crossdata.models._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

class CrossdataStreaming(ephemeralTableId: String, zookeeperConfiguration: Map[String, Any])
  extends EphemeralTableMapDAOComponent {

  val memoryMap = zookeeperConfiguration
  val streamingResourceConfig =  new StreamingResourceConfig

  def init(): Unit = {

    val ephemeralTable = dao.get(ephemeralTableId).getOrElse(//throw new Exception("Table not found"))
      EphemeralTableModel(
        "1",
        "tableName",
        "schema",
        EphemeralOptionsModel(KafkaOptionsModel(Seq(ConnectionHostModel("localhost", "2181", "9092")),
          Seq(TopicModel("crossdata")),
          "1", Option("2")))))

    val sparkResourceConfig = streamingResourceConfig.config.getConfig(SparkPrefixName) match {
      case Some(conf) => conf.toStringMap
      case None => Map.empty[String, String]
    }
    val sparkConfig = configToSparkConf(sparkResourceConfig, ephemeralTable)

    val zookeeperResourceConfig = streamingResourceConfig.config.getConfig(ZookeeperPrefixName) match {
      case Some(conf) => conf.toStringMap
      case None => Map.empty[String, String]
    }

    val kafkaResourceConfig = streamingResourceConfig.config.getConfig(KafkaPrefixName) match {
      case Some(conf) => conf.toStringMap
      case None => Map.empty[String, String]
    }

    val ssc = StreamingContext.getOrCreate(ephemeralTable.options.checkpointDirectory,
      () => {
        createContext(ephemeralTable, sparkConfig, zookeeperResourceConfig ++ config.toStringMap, kafkaResourceConfig)
      })

    ssc.start()
    ssc.awaitTermination()
  }

  private def createContext(ephemeralTable: EphemeralTableModel,
                            sparkConf: SparkConf,
                            zookeeperConf: Map[String, String],
                           kafkaResourceConf: Map[String, String]): StreamingContext = {
    val sparkStreamingWindow = ephemeralTable.options.atomicWindow
    val sparkContext = new SparkContext(sparkConf)
    val streamingContext = new StreamingContext(sparkContext, Seconds(sparkStreamingWindow))
    val sqlContext = new SQLContext(sparkContext)
    streamingContext.checkpoint(ephemeralTable.options.checkpointDirectory)

    val kafkaOptions = ephemeralTable.options.kafkaOptions.copy(
      additionalOptions = kafkaResourceConf ++ ephemeralTable.options.kafkaOptions.additionalOptions)
    val kafkaInput = new KafkaInput(kafkaOptions)

    //Pasamos a un key value con la fecha
    val kafkaDStream = kafkaInput.createStream(streamingContext).map{ case (_ , kafkaEvent) =>
      (DateTime.now.getMillis, kafkaEvent)
    }

    //UpdateStateByKey....

    kafkaDStream.foreachRDD(rdd => {
      if (rdd.take(1).length > 0) {
        //rdd.foreachPartition(_.foreach(println(_)))

        //en un companion object?? lo tengo que pasar a un actor dentro del companion
        val streamingQueryHelper = new StreamingQueriesHelper(zookeeperConf)
        val streamingQueries =
          streamingQueryHelper.findQueriesFromEphemeralTable(ephemeralTable.id)

        if (streamingQueries.nonEmpty) {
          streamingQueries.foreach(streamingQueryModel => {

            //Mejorar la eficiencia del filter + map = flatMap
            val rddFiltered = rdd.filter{case (time, _) =>
              time > DateTime.now.getMillis - streamingQueryModel.window * 1000
            }.map(_._2)

            val df = sqlContext.read.json(rddFiltered)
            df.registerTempTable(ephemeralTable.name)

            val dataFrame = sqlContext.sql(streamingQueryModel.sql)
            dataFrame.toJSON.foreachPartition(values => {
              values.foreach(value => KafkaProducer.put(streamingQueryModel.alias.getOrElse(streamingQueryModel.id),
                value,
                kafkaOptions,
                kafkaOptions.partition))
            })

          })
        }

        //Si lo metemos en un companion object no hay que parar
        streamingQueryHelper.repository.stop

        //PRUEBAS
        val df = sqlContext.read.json(rdd.map{case (a, b) => b})
        df.registerTempTable(ephemeralTable.name)
        val query = "select name from tableName"
        sqlContext.sql(query).show()
        df.toJSON.foreachPartition(values =>
          values.foreach(value => KafkaProducer.put("result",
            value,
            kafkaOptions,
            kafkaOptions.partition)))
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
      if (conf.contains(SparkNameKey)) s"${conf.get(SparkNameKey)}-${ephemeralTable.name}" else ephemeralTable.name
    })
    conf
  }

  private def setPrefixSpark(sparkConfig: Map[String, String]): Map[String, String] =
    sparkConfig.map(entry => {
      if (entry._1.startsWith(SparkPrefixName)) entry
      else (s"$SparkPrefixName.${entry._1}", entry._2)
    })
}

