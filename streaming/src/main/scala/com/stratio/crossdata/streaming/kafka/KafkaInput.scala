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

  private def getConnection : (String, String) = {
    val connectionChain =
      options.connection.map(connection => s"${connection.host}:${connection.consumerPort}").mkString(",")

    (ZookeeperConnectionKey, if(connectionChain.isEmpty) s"$DefaultHost:$DefaulConsumerPort" else connectionChain)
  }

  private def getGroupId : (String, String) = (GroupIdKey, options.groupId)

  private def getTopics : Map[String, Int] = {
    if (options.topics.isEmpty) {
      throw new IllegalStateException(s"Invalid configuration, topics must be declared.")
    } else {
     options.topics.map(topicModel => (topicModel.name, topicModel.numPartitions)).toMap
    }
  }

  private def storageLevel(sparkStorageLevel: String): StorageLevel = {
    StorageLevel.fromString(sparkStorageLevel)
  }

}