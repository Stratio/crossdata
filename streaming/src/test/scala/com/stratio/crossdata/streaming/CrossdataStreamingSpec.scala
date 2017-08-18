package com.stratio.crossdata.streaming

import akka.util.Timeout
import com.stratio.crossdata.streaming.test.{BaseStreamingXDTest, CommonValues}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.concurrent.duration._

@RunWith(classOf[JUnitRunner])
class CrossdataStreamingSpec extends BaseStreamingXDTest with CommonValues {

  implicit val timeout: Timeout = Timeout(15.seconds)

  "CrossdataStreaming" should "return a empty Sparkconf according to the table options" in {
    val XDStreaming = new CrossdataStreaming(TableName, Map.empty[String, String], Map.empty[String, String])
    val configuration = XDStreaming.configToSparkConf(ephemeralTableModelWithoutSparkOptions).getAll
    val expected = Array.empty[(String, String)]

    configuration should be(expected)
  }

  "CrossdataStreaming" should "return Sparkconf according to the table options" in {
    val XDStreaming = new CrossdataStreaming(TableName, Map.empty[String, String], Map.empty[String, String])
    val configuration = XDStreaming.configToSparkConf(ephemeralTableModelWithSparkOptions).getAll
    val expected = Array(("spark.defaultParallelism", "50"))

    configuration should be(expected)
  }

  "CrossdataStreaming" should "return Sparkconf according to the table options with prefix" in {
    val XDStreaming = new CrossdataStreaming(TableName, Map.empty[String, String], Map.empty[String, String])
    val configuration = XDStreaming.configToSparkConf(ephemeralTableModelWithSparkOptionsPrefix).getAll
    val expected = Array(("spark.defaultParallelism", "50"))

    configuration should be(expected)
  }

  //TODO uncomment when in common utils set correctly the wait time for reconnect
  /*"CrossdataStreaming" should "return Exception because zookeeper is not connected" in {
    val XDStreaming = new CrossdataStreaming(TableName, Map.empty[String, String])
    val exception = XDStreaming.init().isFailure
    val expected = true
    exception should be(expected)
  }*/
}
