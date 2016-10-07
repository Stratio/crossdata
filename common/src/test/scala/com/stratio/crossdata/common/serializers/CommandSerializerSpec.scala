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
package com.stratio.crossdata.common.serializers

import java.util.UUID

import com.stratio.crossdata.common.serializers.XDSerializationTest.TestCase
import com.stratio.crossdata.common._
import org.json4s.Formats
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.concurrent.duration._

@RunWith(classOf[JUnitRunner])
class CommandSerializerSpec extends XDSerializationTest[Command] with CrossdataCommonSerializer {
  
  override implicit val formats: Formats = json4sJacksonFormats

  override def testCases: Seq[TestCase] = Seq(
    TestCase("marshall & unmarshall a SQLCommand 0", SQLCommand("select * from highschool")),
    TestCase("marshall & unmarshall a SQLCommand 1", SQLCommand("select * from highschool", flattenResults = true)),
    TestCase("marshall & unmarshall a SQLCommand 2", SQLCommand("select * from highschool", timeout = Some(5 seconds))),
    TestCase("marshall & unmarshall an OpenSessionCommand", OpenSessionCommand()),
    TestCase("marshall & unmarshall an CloseSessionCommand", CloseSessionCommand()),
    TestCase("marshall & unmarshall an CancelQueryExecution", CancelQueryExecution(UUID.randomUUID()))
  )

}