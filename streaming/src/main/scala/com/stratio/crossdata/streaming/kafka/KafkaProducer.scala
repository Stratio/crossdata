package com.stratio.crossdata.streaming.kafka

import java.util.Properties

import com.stratio.crossdata.streaming.constants.KafkaConstants
import kafka.producer.{KeyedMessage, Producer, ProducerConfig}
import org.apache.spark.sql.crossdata.models.{ConnectionHostModel, KafkaOptionsModel}

import scala.collection.mutable

object KafkaProducer {

  import KafkaConstants._

  private val producers: mutable.Map[String, Producer[String, String]] = mutable.Map.empty

  def put(topic: String,
          message: String,
          options: KafkaOptionsModel,
          partition: Option[String] = None): Unit = {
    val keyedMessage = kafkaMessage(topic, message, partition)

    sendMessage(keyedMessage, options)
  }

  private[streaming] def kafkaMessage(topic: String,
                                  message: String,
                                  partition: Option[String]): KeyedMessage[String, String] = {
    partition.fold(new KeyedMessage[String, String](topic, message)) { key =>
      new KeyedMessage[String, String](topic, key, message)
    }
  }

  private[streaming] def sendMessage(message: KeyedMessage[String, String], options: KafkaOptionsModel): Unit = {
    getProducer(options).send(message)
  }

  private[streaming] def getProducer(options: KafkaOptionsModel): Producer[String, String] = {
    KafkaProducer.getInstance(getKey(options.connection), options)
  }

  private[streaming] def getKey(connection: ConnectionHostModel): String =
    s"ConnectionHostModel([${connection.zkConnection.map(_.toString).mkString(",")}],[${connection.kafkaConnection.map(_.toString).mkString(",")}])"

  private[streaming] def getInstance(key: String, options: KafkaOptionsModel): Producer[String, String] =
    producers.getOrElse(key, {
      val producer = createProducer(options)
      producers.put(key, producer)
      producer
    })

  private[streaming] def createProducer(options: KafkaOptionsModel): Producer[String, String] = {
    val properties = new Properties()

    properties.put(BrokerListKey, getBrokerList(options.connection))
    properties.put(SerializerKey, DefaultSerializer)
    options.additionalOptions.foreach { case (key, value) =>
      producerProperties.get(key).foreach(kafkaKey => properties.put(kafkaKey, value))
    }

    val producerConfig = new ProducerConfig(properties)
    new Producer[String, String](producerConfig)
  }

  private[streaming] def getBrokerList(connection: ConnectionHostModel,
                                   defaultHost: String = DefaultHost,
                                   defaultPort: String = DefaultProducerPort): String = {

    val connectionStr = (
      for (kafkaConnection <- connection.kafkaConnection) yield (s"${kafkaConnection.host}:${kafkaConnection.port}")
      ).mkString(",")

    if (connectionStr.isEmpty) s"$defaultHost:$defaultPort" else connectionStr
  }

  private[streaming] def deleteProducers(): Unit = {
    producers.foreach { case (key, producer) =>
      producer.close()
      producers.remove(key)
    }
  }

  private[streaming] def size: Int = producers.size
}
