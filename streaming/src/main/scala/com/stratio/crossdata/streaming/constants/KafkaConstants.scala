package com.stratio.crossdata.streaming.constants

object KafkaConstants {

  /**
   * Default parameters
   */
  val DefaultPartition = 1
  val DefaultConsumerPort = "2181"
  val DefaultProducerPort = "9092"
  val DefaultHost = "127.0.0.1"
  val DefaultSerializer = "kafka.serializer.StringEncoder"

  /**
   * Kafka Spark consumer keys
   */
  val ZookeeperConnectionKey = "zookeeper.connect"
  val GroupIdKey = "group.id"

  /**
   * Kafka native producer keys
   */
  val SerializerKey = "serializer.class"
  val BrokerListKey = "metadata.broker.list"
  val PartitionKey = "partition"

  val RequiredAckKey = "requiredAcks"
  val CompressionCodecKey = "compressionCodec"
  val ProducerTypeKey = "producerType"
  val BatchSizeKey = "batchSize"
  val MaxRetriesKey = "maxRetries"
  val ClientIdKey = "clientId"

  val producerProperties = Map(
    RequiredAckKey -> "request.required.acks",
    CompressionCodecKey -> "compression.codec",
    ProducerTypeKey -> "producer.type",
    BatchSizeKey -> "batch.num.messages",
    MaxRetriesKey -> "message.send.max.retries",
    ClientIdKey -> "client.id")
}
