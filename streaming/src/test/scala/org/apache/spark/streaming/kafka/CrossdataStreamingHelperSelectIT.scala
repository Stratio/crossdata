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
package org.apache.spark.streaming.kafka

import java.util.Properties

import com.stratio.crossdata.streaming.helpers.CrossdataStreamingHelper._
import com.stratio.crossdata.streaming.test.{BaseSparkStreamingXDTest, CommonValues}
import kafka.consumer.{Consumer, ConsumerConfig, ConsumerConnector}
import org.apache.spark.sql.catalyst.SimpleCatalystConf
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.crossdata.catalog.streaming.ZookeeperStreamingCatalog
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.language.postfixOps

@RunWith(classOf[JUnitRunner])
class CrossdataStreamingHelperSelectIT
    extends BaseSparkStreamingXDTest
    with CommonValues {

  private val sparkConf = new SparkConf()
    .setMaster("local[2]")
    .setAppName(this.getClass.getSimpleName)
  private var sc: SparkContext = _
  private var kafkaTestUtils: KafkaTestUtils = _
  private var zookeeperConf: Map[String, String] = _
  private var catalogConf: Map[String, String] = _
  private var xDContext: XDContext = _
  private var zookeeperStreamingCatalog: ZookeeperStreamingCatalog = _
  private var consumer: ConsumerConnector = _
  private var ssc: StreamingContext = _

  override def beforeAll {
    sc = SparkContext.getOrCreate(sparkConf)

    if (kafkaTestUtils == null) {
      kafkaTestUtils = new KafkaTestUtils
      kafkaTestUtils.setup()
      zookeeperConf = Map("connectionString" -> kafkaTestUtils.zkAddress)
      catalogConf = parseZookeeperCatalogConfig(zookeeperConf)
      xDContext = XDContext.getOrCreate(sc, parseCatalogConfig(catalogConf))
      zookeeperStreamingCatalog = new ZookeeperStreamingCatalog(
          new SimpleCatalystConf(true),
          XDContext.xdConfig)
    }

    if (consumer == null) {
      val props = new Properties()
      props.put("zookeeper.connect", kafkaTestUtils.zkAddress)
      props.put("group.id", GroupId)
      val config = new ConsumerConfig(props)
      consumer = Consumer.create(config)
    }
  }

  override def afterAll {

    if (consumer != null) {
      consumer.shutdown()
      consumer = null
    }

    if (ssc != null) {
      ssc.stop(stopSparkContext = true, stopGracefully = false)
      ssc.awaitTerminationOrTimeout(6000)
      ssc = null
    }

    if (kafkaTestUtils != null) {
      kafkaTestUtils.teardown()
      kafkaTestUtils = null
    }
  }

  /*
  test("Crossdata streaming must save into the kafka output the sql results") {

    deletePath(checkpointDirectorySelect)
    val expectedResult = Array("a", "c")

    val consumerHostZK = connectionHostModel.zkConnection.head.host
    val consumerPortZK = kafkaTestUtils.zkAddress.split(":").last.toInt

    val producerHostKafka = connectionHostModel.kafkaConnection.head.host
    val producerPortKafka = kafkaTestUtils.brokerAddress.split(":").last

    val kafkaStreamModelZk = kafkaStreamModelSelect.copy(
      connection = connectionHostModel.copy(
        zkConnection = Seq(ConnectionModel(consumerHostZK, consumerPortZK)),
        kafkaConnection = Seq(ConnectionModel(producerHostKafka, producerPortKafka.toInt))))

    val ephemeralTableKafka = ephemeralTableModelStreamKafkaOptionsSelect.copy(
      options = ephemeralOptionsStreamKafkaSelect.copy(kafkaOptions = kafkaStreamModelZk))

    zookeeperStreamingCatalog.createEphemeralQuery(querySelectModel)
    zookeeperStreamingCatalog.createEphemeralTable(ephemeralTableKafka)
    zookeeperStreamingCatalog.getEphemeralTable(TableNameSelect) match {
      case Some(ephemeralTable) =>
        ssc = CrossdataStreamingHelper.createContext(ephemeralTable, sparkConf, zookeeperConf, catalogConf)
        val valuesToSent = Array( """{"name": "a"}""", """{"name": "c"}""")
        kafkaTestUtils.createTopic(TopicTestSelect)
        kafkaTestUtils.sendMessages(TopicTestSelect, valuesToSent)
        val resultList = new mutable.MutableList[String]()

        ssc.start()

        eventually(timeout(20000 milliseconds), interval(7000 milliseconds)) {
          val topicCountMap = Map(AliasNameSelect -> 1)
          val consumerMap = consumer.createMessageStreams(topicCountMap)
          val streams = consumerMap.get(AliasNameSelect).get
          for (stream <- streams) {
            val it = stream.iterator()
            while (it.hasNext() && resultList.size < 1) {
              synchronized(resultList.+=(new String(it.next().message())))
            }
          }
          assert(resultList.forall(elem => expectedResult.contains(elem)))
        }

      case None => throw new Exception("Ephemeral table not created")
    }
  }
 */
}
