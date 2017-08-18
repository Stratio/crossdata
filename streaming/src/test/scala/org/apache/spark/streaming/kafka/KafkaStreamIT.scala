package org.apache.spark.streaming.kafka

import com.stratio.crossdata.streaming.kafka.KafkaInput
import com.stratio.crossdata.streaming.test.{BaseSparkStreamingXDTest, CommonValues}
import org.apache.spark.sql.crossdata.models.ConnectionModel
import org.apache.spark.streaming.{Milliseconds, StreamingContext}
import org.apache.spark.{SparkContext, SparkConf}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.collection.mutable
import scala.concurrent.duration._
import scala.language.postfixOps

@RunWith(classOf[JUnitRunner])
class KafkaStreamIT extends BaseSparkStreamingXDTest with CommonValues {

  val sparkConf = new SparkConf().setMaster("local[2]").setAppName(this.getClass.getSimpleName)
  val sc = SparkContext.getOrCreate(sparkConf)
  var ssc: StreamingContext = _
  val kafkaTestUtils: KafkaTestUtils =  new KafkaTestUtils
  kafkaTestUtils.setup()

  after {
    if (ssc != null) {
      ssc.stop(stopSparkContext = false, stopGracefully = false)
      ssc.awaitTerminationOrTimeout(3000)
      ssc = null
    }
  }

  override def afterAll : Unit = {
    kafkaTestUtils.teardown()
  }

  test("Kafka input stream with kafkaOptionsModel from Map of values") {
    ssc = new StreamingContext(sc, Milliseconds(1000))
    val valuesToSent = Map("a" -> 5, "b" -> 3, "c" -> 10)
    kafkaTestUtils.createTopic(TopicTest)
    kafkaTestUtils.sendMessages(TopicTest, valuesToSent)

    val consumerHostZK = connectionHostModel.zkConnection.head.host
    val consumerPortZK = kafkaTestUtils.zkAddress.split(":").last.toInt

    val producerHostKafka = connectionHostModel.kafkaConnection.head.host
    val producerPortKafka = kafkaTestUtils.brokerAddress.split(":").last

    val kafkaStreamModelZk = kafkaStreamModel.copy(
      connection = connectionHostModel.copy(
        zkConnection = Seq(ConnectionModel(consumerHostZK, consumerPortZK)),
        kafkaConnection = Seq(ConnectionModel(producerHostKafka, producerPortKafka.toInt))))

    val input = new KafkaInput(kafkaStreamModelZk)
    val stream = input.createStream(ssc)
    val result = new mutable.HashMap[String, Long]() with mutable.SynchronizedMap[String, Long]

    stream.map(_._2).countByValue().foreachRDD { rdd =>
      val ret = rdd.collect()
      ret.toMap.foreach { case (key, value) =>
        val count = result.getOrElseUpdate(key, 0) + value
        result.put(key, count)
      }
    }

    ssc.start()

    eventually(timeout(10000 milliseconds), interval(1000 milliseconds)) {
      assert(valuesToSent === result)
    }
  }

  test("Kafka input stream with kafkaOptionsModel from list of Strings") {
    ssc = new StreamingContext(sc, Milliseconds(500))
    val valuesToSent = Array("a", "b", "c")
    kafkaTestUtils.createTopic(TopicTestProject)
    kafkaTestUtils.sendMessages(TopicTestProject, valuesToSent)

    val consumerHostZK = connectionHostModel.zkConnection.head.host
    val consumerPortZK = kafkaTestUtils.zkAddress.split(":").last.toInt

    val producerHostKafka = connectionHostModel.kafkaConnection.head.host
    val producerPortKafka = kafkaTestUtils.brokerAddress.split(":").last

    val kafkaStreamModelZk = kafkaStreamModelProject.copy(
      connection = connectionHostModel.copy(
        zkConnection = Seq(ConnectionModel(consumerHostZK, consumerPortZK)),
        kafkaConnection = Seq(ConnectionModel(producerHostKafka, producerPortKafka.toInt))))

    val input = new KafkaInput(kafkaStreamModelZk)
    val stream = input.createStream(ssc)
    val result = new mutable.MutableList[String]()

    stream.map(_._2).foreachRDD { rdd =>
      val ret = rdd.collect()
      ret.foreach(value => result.+=(value))
    }

    ssc.start()

    eventually(timeout(10000 milliseconds), interval(1000 milliseconds)) {
      assert(valuesToSent === result)
    }
  }

}
