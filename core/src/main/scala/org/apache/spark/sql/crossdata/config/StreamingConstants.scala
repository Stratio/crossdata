/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
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
  //Connection //format "host0:consumerPort0,host1:consumerPort1,host2:consumerPort2"
  val ZKConnection = "receiver.zk.connection"
  //Connection //format "host0:producerPort0,host1:producerPort1,host2:producerPort2"
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
  val ZooKeeperStreamingCatalogPath = "catalog.zookeeper"
  val SparkHomeKey = "sparkHome"
  val AppJarKey = "appJar"
  val SparkMasterKey = "spark.master"
  val ExternalJarsKey = "jars"
  val SparkCoresMax = "spark.cores.max"
  val HdfsConf = "hdfs"

  // TODO define and validate mandatory options like spark.master, kafka.topic and groupid, ...
  val listMandatoryEphemeralTableKeys = List(
    KafkaTopic,
    KafkaGroupId)


}
