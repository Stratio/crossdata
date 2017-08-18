/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package org.apache.spark.sql.crossdata.config

import com.stratio.crossdata.test.BaseXDTest
import org.apache.spark.sql.crossdata.models.{ConnectionModel, ConnectionHostModel, TopicModel, EphemeralOutputFormat}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class StreamingConfigSpec extends BaseXDTest {

  val EphemeralTableName = "ephtable"
  val KafkaGroupId = "xd1"
  val KafkaTopic= "ephtable"
  val KafkaNumPartitions = 1

  val EmptyTableOptions: Map[String, String] = Map.empty

  val MandatoryTableOptions: Map[String, String] = Map(
    "receiver.kafka.topic" -> s"$KafkaTopic:$KafkaNumPartitions",
    "receiver.kafka.groupId" -> KafkaGroupId
  )

  val CompleteTableOptions: Map[String, String] = Map(
    "receiver.storageLevel" -> "MEMORY_AND_DISK",
    "atomicWindow" -> "10",
    "outputFormat" -> "JSON",
    "spark.cores.max" -> "3"
  ) ++ MandatoryTableOptions


  it should "add default options to the ephemeral table" in {
    val ephTable = StreamingConfig.createEphemeralTableModel(EphemeralTableName, MandatoryTableOptions)
    val options = ephTable.options

    options.atomicWindow shouldBe 5
    options.checkpointDirectory shouldBe s"/tmp/spark/$EphemeralTableName"
    options.maxWindow shouldBe 10
    options.outputFormat shouldBe EphemeralOutputFormat.ROW

    options.sparkOptions should contain ("spark.cores.max", "2")
    options.sparkOptions should contain ("spark.stopGracefully", "true")

    options.kafkaOptions.connection shouldBe ConnectionHostModel(
      Seq(ConnectionModel("localhost", 2181)),
      Seq(ConnectionModel("localhost", 9092)))
    options.kafkaOptions.storageLevel shouldBe "MEMORY_AND_DISK_SER"

    // table options
    options.kafkaOptions.groupId shouldBe KafkaGroupId
    options.kafkaOptions.topics shouldBe Seq(TopicModel(KafkaTopic, KafkaNumPartitions))

  }

  it should "override default options" in {
    val ephTable = StreamingConfig.createEphemeralTableModel(EphemeralTableName, CompleteTableOptions)
    val options = ephTable.options

    options.atomicWindow shouldBe 10
    options.outputFormat shouldBe EphemeralOutputFormat.JSON
    options.sparkOptions should contain ("spark.cores.max", "3")
    options.kafkaOptions.storageLevel shouldBe "MEMORY_AND_DISK"

  }

  it should "fail if spark.cores.max is less than 2" in {
    val wrongOptions = CompleteTableOptions + (StreamingConstants.SparkCoresMax -> "1")
    an [Exception] should be thrownBy StreamingConfig.createEphemeralTableModel(EphemeralTableName, wrongOptions)
  }

}
