/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.streaming.kafka

import com.stratio.crossdata.streaming.constants.KafkaConstants
import kafka.serializer.StringDecoder
import org.apache.spark.sql.crossdata.models.KafkaOptionsModel
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka.KafkaUtils
import KafkaConstants._

class KafkaInput(options: KafkaOptionsModel) {

  def createStream(ssc: StreamingContext): DStream[(String, String)] = {
    val kafkaParams = options.additionalOptions
    val connection = Map(getConnection)
    val groupId = Map(getGroupId)

    KafkaUtils.createStream[String, String, StringDecoder, StringDecoder](
      ssc,
      connection ++ groupId ++ kafkaParams,
      getTopics,
      storageLevel(options.storageLevel))
  }

  private[streaming] def getConnection : (String, String) = {

    val connectionChain = (
      for(zkConnection <- options.connection.zkConnection) yield (s"${zkConnection.host}:${zkConnection.port}")
      ).mkString(",")

    (ZookeeperConnectionKey, if(connectionChain.isEmpty) s"$DefaultHost:$DefaultConsumerPort" else connectionChain)
  }

  private[streaming] def getGroupId : (String, String) = (GroupIdKey, options.groupId)

  private[streaming] def getTopics : Map[String, Int] = {
    if (options.topics.isEmpty) {
      throw new IllegalStateException(s"Invalid configuration, topics must be declared.")
    } else {
     options.topics.map(topicModel => (topicModel.name, topicModel.numPartitions)).toMap
    }
  }

  private[streaming] def storageLevel(sparkStorageLevel: String): StorageLevel = {
    StorageLevel.fromString(sparkStorageLevel)
  }

}