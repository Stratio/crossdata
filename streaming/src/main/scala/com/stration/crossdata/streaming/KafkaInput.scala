package com.stration.crossdata.streaming

import kafka.serializer.StringDecoder
import org.apache.spark.sql.crossdata.models.KafkaOptionsModel
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka.KafkaUtils

class KafkaInput(options: KafkaOptionsModel) {

  val DefaultPartition = 1
  val DefaulPort = "2181"
  val DefaultHost = "localhost"
  val ZookeeperConnectionKey = "zookeeper.connect"

  def createStream(ssc: StreamingContext): DStream[(String, String)] = {
    val kafkaParams = options.additionalOptions
    val connection = Map(getConnection)

    KafkaUtils.createStream[String, String, StringDecoder, StringDecoder](
      ssc,
      connection ++ kafkaParams,
      getTopics,
      storageLevel(options.storageLevel))
  }

  private def getConnection : (String, String) = {
    val connectionChain = options.connection.map(connection => s"${connection.host}:${connection.port}").mkString(",")

    (ZookeeperConnectionKey, if(connectionChain.isEmpty) s"$DefaultHost:$DefaulPort" else connectionChain)
  }

  private def getTopics : Map[String, Int] = {
    if (options.topics.isEmpty) {
      throw new IllegalStateException(s"Invalid configuration, topics must be declared.")
    }
    else {
     options.topics.map(topicModel => (topicModel.name, topicModel.partition)).toMap
    }
  }

  private def storageLevel(sparkStorageLevel: String): StorageLevel = {
    StorageLevel.fromString(sparkStorageLevel)
  }

}