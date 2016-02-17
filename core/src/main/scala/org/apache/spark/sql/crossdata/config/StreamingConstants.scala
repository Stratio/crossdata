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
package org.apache.spark.sql.crossdata.config


object StreamingConstants {

  val MainClass = "com.stratio.crossdata.streaming.CrossdataStreamingApplication"
  val StreamingConfPath = "streaming"
  val SparkConfPath = "spark"

  /**
   * Ephemeral table config
   */
  /**
   * Receiver
   */
  //Connection //format "host0:consumerPort0:producerPort0,host1:consumerPort1:producerPort1,host2:consumerPort2:producerPort2"
  val KafkaConnection = "receiver.kafka.connection"
  //format "topicName1:1,topicName1:2,topicName1:3"
  val KafkaTopic= "receiver.kafka.topic"
  val KafkaGroupId = "receiver.kafka.groupId"
  val KafkaPartition = "receiver.kafka.numPartitions" //optional
  //would go through additionalOptions param.
  // One param for each element map. key = kafka.additionalOptions.x -> value = value
  val KafkaAdditionalOptionsKey = "receiver.kafka.options" //optional
  val ReceiverStorageLevel = "receiver.storageLevel" //optional

  /**
   * Streaming generic options
   */
  val AtomicWindow = "atomicWindow"
  val MaxWindow = "maxWindow"
  val OutputFormat = "outputFormat"
  val CheckpointDirectory = "checkpointDirectory"

  /**
   * SparkOptions
   */
  // One param for each element map. key = sparkOptions.x -> value = value
  val ZooKeeperConnectionKey = "catalog.zookeeper.connectionString"
  val SparkHomeKey = "sparkHome"
  val AppJarKey = "appJar"
  val SparkMasterKey = "spark.master"
  val ExternalJarsKey = "jars"


  // TODO define and validate mandatory options like spark.master, kafka.topic and groupid, ...
  val listMandatoryEphemeralTableKeys = List(
    KafkaTopic,
    KafkaGroupId)


}
