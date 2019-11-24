/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
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
