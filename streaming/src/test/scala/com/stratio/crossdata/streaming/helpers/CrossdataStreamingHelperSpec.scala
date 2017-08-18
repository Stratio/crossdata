/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.streaming.helpers

import com.stratio.crossdata.streaming.test.{BaseStreamingXDTest, CommonValues}
import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.crossdata.XDContext._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.collection.JavaConversions._

@RunWith(classOf[JUnitRunner])
class CrossdataStreamingHelperSpec extends BaseStreamingXDTest with CommonValues {

  "Helper" should "return a correct config object with empty properties" in {
    val result = CrossdataStreamingHelper.parseCatalogConfig(zookeeperConfEmpty)
    val expected = Some(ConfigFactory.empty())

    result should be(expected)
  }

  "Helper" should "return a empty config object with erroneous properties" in {

    val result = CrossdataStreamingHelper.parseCatalogConfig(zookeeperConfError)
    val expected = None

    result should be(expected)
  }

  "Helper" should "return a merged kafka with empty options" in {

    val result = CrossdataStreamingHelper.mergeKafkaOptions(queryModel, kafkaOptionsModel)
    val expected = kafkaOptionsModel

    result should be(expected)
  }

  "Helper" should "return a merged kafka with options" in {

    val result = CrossdataStreamingHelper.mergeKafkaOptions(queryModel, kafkaStreamModel)
    val expected = kafkaStreamModel

    result should be(expected)
  }

  "Helper" should "return a merged kafka with query options" in {

    val result = CrossdataStreamingHelper.mergeKafkaOptions(queryOptionsModel, kafkaOptionsModel)
    val expected = kafkaOptionsModel.copy(additionalOptions = queryOptionsModel.options)

    result should be(expected)
  }
}
