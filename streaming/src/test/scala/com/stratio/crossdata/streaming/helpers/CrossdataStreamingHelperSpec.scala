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
