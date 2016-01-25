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

import java.util.Properties
import com.stratio.crossdata.streaming.constants.KafkaConstants
import kafka.producer.{KeyedMessage, Producer, ProducerConfig}
import org.apache.spark.sql.crossdata.models.{ConnectionHostModel, KafkaOptionsModel}

import scala.collection.mutable

object KafkaProducer {

  import KafkaConstants._

  private val producers: mutable.Map[String, Producer[String, String]] =
    mutable.Map.empty[String, Producer[String, String]]

  def put(topic: String,
          message: String,
          options : KafkaOptionsModel,
          partition: Option[String] = None): Unit = {
    val keyedMessage = {
      partition.fold(new KeyedMessage[String, String](topic, message)) { key =>
        new KeyedMessage[String, String](topic, key, message)
      }
    }
    getProducer(options).send(keyedMessage)
  }

  private def getProducer(options: KafkaOptionsModel): Producer[String, String] = {
    KafkaProducer.getInstance(getKey(options.connection), options)
  }

  private def getKey(connection: Seq[ConnectionHostModel]): String =
    connection.map(_.toString).mkString(".")

  private def getInstance(key: String, options: KafkaOptionsModel): Producer[String, String] = {
    producers.getOrElse(key, {
      val producer = createProducer(options)
      producers.put(key, producer)
      producer
    })
  }

  private def createProducer(options: KafkaOptionsModel): Producer[String, String] = {
    val properties: Properties = new Properties()

    properties.put(BrokerListKey, getBrokerList(options.connection))
    properties.put(SerializerKey, DefaultSerializer)
    options.additionalOptions.foreach{ case (key, value) =>
      producerProperties.get(key).foreach(kafkaKey => properties.put(kafkaKey, value))
    }

    val producerConfig = new ProducerConfig(properties)
    new Producer[String, String](producerConfig)
  }

  private def getBrokerList(connection: Seq[ConnectionHostModel],
                            defaultHost: String = DefaultHost,
                            defaultPort: String = DefaulProducerPort): String = {
    val connectionString = connection.map(c => s"${c.host}:${c.producerPort}").mkString(",")

    if (connectionString.isEmpty) s"$defaultHost:$defaultPort" else connectionString
  }
}
