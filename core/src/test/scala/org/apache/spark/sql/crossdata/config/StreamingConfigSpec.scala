/*
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

import com.stratio.crossdata.test.BaseXDTest
import org.apache.spark.sql.crossdata.models.{ConnectionModel, ConnectionHostModel, TopicModel, EphemeralOutputFormat}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class StreamingConfigSpec extends BaseXDTest {

  val EphemeralTableName = "ephtable"
  val KafkaGroupId = "xd1"
  val KafkaTopic = "ephtable"
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
    val ephTable =
      StreamingConfig.createEphemeralTableModel(EphemeralTableName, MandatoryTableOptions)
    val options = ephTable.options

    options.atomicWindow shouldBe 5
    options.checkpointDirectory shouldBe s"/tmp/spark/$EphemeralTableName"
    options.maxWindow shouldBe 10
    options.outputFormat shouldBe EphemeralOutputFormat.ROW

    options.sparkOptions should contain("spark.cores.max", "2")
    options.sparkOptions should contain("spark.stopGracefully", "true")

    options.kafkaOptions.connection shouldBe ConnectionHostModel(
        Seq(ConnectionModel("localhost", 2181)),
        Seq(ConnectionModel("localhost", 9092)))
    options.kafkaOptions.storageLevel shouldBe "MEMORY_AND_DISK_SER"

    // table options
    options.kafkaOptions.groupId shouldBe KafkaGroupId
    options.kafkaOptions.topics shouldBe Seq(TopicModel(KafkaTopic, KafkaNumPartitions))

  }

  it should "override default options" in {
    val ephTable =
      StreamingConfig.createEphemeralTableModel(EphemeralTableName, CompleteTableOptions)
    val options = ephTable.options

    options.atomicWindow shouldBe 10
    options.outputFormat shouldBe EphemeralOutputFormat.JSON
    options.sparkOptions should contain("spark.cores.max", "3")
    options.kafkaOptions.storageLevel shouldBe "MEMORY_AND_DISK"

  }

  it should "fail if spark.cores.max is less than 2" in {
    val wrongOptions = CompleteTableOptions + (StreamingConstants.SparkCoresMax -> "1")
    an[Exception] should be thrownBy StreamingConfig.createEphemeralTableModel(EphemeralTableName,
                                                                               wrongOptions)
  }

}
