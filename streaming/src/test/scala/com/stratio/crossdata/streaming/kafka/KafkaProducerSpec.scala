/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.streaming.kafka

import com.stratio.crossdata.streaming.test.{BaseStreamingXDTest, CommonValues}
import kafka.producer.Producer
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class KafkaProducerSpec extends BaseStreamingXDTest with CommonValues {

  after {
    KafkaProducer.deleteProducers()
  }

  "KafkaProducer" should "return a correct key" in {
    val result = KafkaProducer.getKey(connectionHostModel)
    val expected = """ConnectionHostModel([ConnectionModel(localhost,2181)],[ConnectionModel(localhost,9042)])"""

    result should be(expected)
  }

  "KafkaProducer" should "create a correct Producer to Kafka" in {
    val producer = KafkaProducer.getProducer(kafkaOptionsModel)
    val result = producer.isInstanceOf[Producer[String, String]]
    val expected = true

    result should be(expected)
  }

  "KafkaProducer" should "create only one producer" in {
    val producer1 = KafkaProducer.getProducer(kafkaOptionsModel)
    val producer2 = KafkaProducer.getProducer(kafkaOptionsModel)

    producer1 should be theSameInstanceAs producer2

    val result = KafkaProducer.size

    result should be(1)
  }

  "KafkaProducer" should "create two producers" in {
    KafkaProducer.getProducer(kafkaOptionsModel)
    KafkaProducer.getProducer(kafkaOptionsModelEmptyConnection)

    KafkaProducer.size should be(2)
  }

  "KafkaProducer" should "return default producer with empty connection" in {
    val result = KafkaProducer.getProducer(kafkaOptionsModelEmptyConnection).config.brokerList
    val expected = "127.0.0.1:9092"

    result should be(expected)
  }

  "KafkaProducer" should "return additional params" in {
    val result = KafkaProducer.getProducer(kafkaStreamModel).config.props.containsKey("batch.num.messages")
    val expected = true

    result should be(expected)
  }

  "KafkaProducer" should "return a correct additional param" in {
    val result = KafkaProducer.getProducer(kafkaStreamModel).config.props.getString("batch.num.messages")
    val expected = "100"

    result should be(expected)
  }

  "KafkaProducer" should "return empty params" in {
    val result = KafkaProducer.getProducer(kafkaOptionsModel).config.props.containsKey("batch.num.messages")
    val expected = false

    result should be(expected)
  }

  "KafkaProducer" should "delete all producers" in {
    val producer = KafkaProducer.getProducer(kafkaOptionsModel)
    KafkaProducer.deleteProducers()
    val result = KafkaProducer.size
    val expected = 0

    result should be(expected)
  }
}
