package org.apache.spark.sql.crossdata.config


object StreamingConstants {

  val streamingConfFilePath = "streaming.confFilePath"

  val loggerName = "Streaming"

  /**
    * Ephemeral table config
    */

  //kafka
  //Connection //format "host0:consumerPort0:producerPort0,host1:consumerPort1:producerPort1,host2:consumerPort2:producerPort2"
  val kafkaConnection = "kafka.connection"

  //format "topicName1:1,topicName1:2,topicName1:3"
  val kafkaTopic= "kafka.topic"
  val kafkaGroupId = "kafka.groupId"
  val kafkaPartition = "kafka.partition" //optional
  //would go through additionalOptions param.
  // One param for each element map. key = kafka.additionalOptions.x -> value = value
  val kafkaAdditionalOptionsKey = "kafka.options" //optional
  val kafkaStorageLevel = "kafka.storageLevel" //optional
  val atomicWindow ="atomicWindow"
  val maxWindow = "maxWindow"
  val outputFormat = "outputFormat" //optional
  val checkpointDirectory = "checkpointDirectory" //optional
  // One param for each element map. key = sparkOptions.x -> value = value
  val sparkOptionsKey = "sparkoptions" // optional is a Map

  val listAllEphemeralTableKeys = List(
      kafkaConnection,
      kafkaTopic,
      kafkaGroupId,
      kafkaAdditionalOptionsKey,
      kafkaStorageLevel,
      atomicWindow,
      maxWindow,
      outputFormat,
      checkpointDirectory,
      sparkOptionsKey)

  // Default mandatory values
  val defaultKafkaConnection = "127.0.0.1:2181:9092"
  val defaultKafkaTopic = "XDTopic:1"
  val defaultKafkaGroupId = "XDgroup"
  val defaultAtomicWindow = "5000" //ms
  val defaultMaxWindow = "20000" //ms

  // Default values
  val defaultKafkaStorageLevel = "MEMORY_AND_DISK_SER"
  val defaultOutputFormat = "ROW"
  val defaultCheckpointDirectory = "checkpoint/crossdata"

  val listMandatoryEphemeralTableKeys = List(
    kafkaConnection,
    kafkaTopic,
    kafkaGroupId,
    atomicWindow,
    maxWindow)

  // Default mandatory Map
  val defaultEphemeralTableMapConfig = Map(
    kafkaConnection -> defaultKafkaConnection,
    kafkaTopic -> defaultKafkaTopic,
    kafkaGroupId -> defaultKafkaGroupId,
    atomicWindow -> defaultAtomicWindow,
    maxWindow -> defaultMaxWindow)

  /**
    * Ephemeral Query config
    */

  val querySql = "sql"
  val queryAlias = "alias"
  val queryWindow = "window"
  val queryOptionsKey = "queryoptions"
}
