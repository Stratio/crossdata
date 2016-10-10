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

import com.stratio.crossdata.common.security.Session
import com.stratio.crossdata.common.{CancelQueryExecution, CommandEnvelope, OpenSessionCommand, SQLCommand}
import com.stratio.crossdata.common.serializers.XDSerializationTest.TestCase
import org.json4s.Formats
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CommandEnvelopeSpec extends XDSerializationTest[CommandEnvelope] with CrossdataCommonSerializer {

  override implicit val formats: Formats = json4sJacksonFormats

  lazy val session = Session(UUID.randomUUID(), None)

  override def testCases: Seq[TestCase] = Seq(
    TestCase(
      "marshall & unmarshall a SQLCommand within a CommandEnvelope",
      CommandEnvelope(SQLCommand("select * from highschool"), session, "usr")
    ),
    TestCase(
      "marshall & unmarshall a OpenSessionCommand within a CommandEnvelope",
      CommandEnvelope(OpenSessionCommand(), session, "usr")
    ),
    TestCase(
      "marshall & unmarshall a CancelQueryExecution within a CommandEnvelope",
      CommandEnvelope(CancelQueryExecution(UUID.randomUUID()), session, "usr")
    )
  )
}
