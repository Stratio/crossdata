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

