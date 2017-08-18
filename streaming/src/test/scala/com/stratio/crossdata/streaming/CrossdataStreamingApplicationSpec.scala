package com.stratio.crossdata.streaming

import com.stratio.crossdata.streaming.constants.ApplicationConstants
import com.stratio.crossdata.streaming.test.{CommonValues, BaseStreamingXDTest}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.util.Try

@RunWith(classOf[JUnitRunner])
class CrossdataStreamingApplicationSpec extends BaseStreamingXDTest with CommonValues {

  "CrossdataStreamingApplication" should "return a Exception with incorrect arguments" in {

    val result = Try(CrossdataStreamingApplication.main(Array.empty[String])).isFailure
    val expected = true

    result should be(expected)
  }

  "CrossdataStreamingApplication" should "parse correctly the zookeeper argument" in {

    val result = CrossdataStreamingApplication.parseMapArguments("""{"connectionString":"localhost:2181"}""")
    val expected = Try(Map("connectionString" -> "localhost:2181"))

    result should be(expected)
  }
}

