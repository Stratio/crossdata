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

import com.stratio.crossdata.streaming.constants.KafkaConstants._
import com.stratio.crossdata.streaming.test.{BaseStreamingXDTest, CommonValues}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.util.{Failure, Try}

@RunWith(classOf[JUnitRunner])
class KafkaInputSpec extends BaseStreamingXDTest with CommonValues {

  "KafkaInput" should "return a correct connection" in {
    val input = new KafkaInput(kafkaOptionsModel)
    val connection = input.getConnection
    val expected = (ZookeeperConnectionKey, "localhost:2181")

    connection should be(expected)
  }

  "KafkaInput" should "return a default connection" in {
    val input = new KafkaInput(kafkaOptionsModelEmptyConnection)
    val connection = input.getConnection
    val expected = (ZookeeperConnectionKey, "127.0.0.1:2181")

    connection should be(expected)
  }

  "KafkaInput" should "return a correct groupId" in {
    val input = new KafkaInput(kafkaOptionsModel)
    val groupId = input.getGroupId
    val expected = (GroupIdKey, "crossdatagroup")

    groupId should be(expected)
  }

  "KafkaInput" should "return a correct topics" in {
    val input = new KafkaInput(kafkaOptionsModel)
    val topics = input.getTopics
    val expected = Map("topicTest" -> 1)

    topics should be(expected)
  }

  "KafkaInput" should "return a exception topics" in {
    val input = new KafkaInput(kafkaOptionsModelEmptyTopics)
    an[IllegalStateException] should be thrownBy input.getTopics
  }

  "KafkaInput" should "return a correct storageLevel" in {
    val input = new KafkaInput(kafkaOptionsModel)
    val stLevel = input.storageLevel(kafkaOptionsModel.storageLevel)
    val expected = org.apache.spark.storage.StorageLevel.MEMORY_ONLY_SER

    stLevel should be(expected)
  }
}
