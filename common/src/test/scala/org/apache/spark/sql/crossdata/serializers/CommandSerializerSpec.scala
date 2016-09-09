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
package org.apache.spark.sql.crossdata.serializers

import com.stratio.crossdata.common.{Command, SQLCommand}
import org.apache.spark.sql.crossdata.serializers.XDSerializationTest.TestCase
import org.json4s.Formats

class CommandSerializerSpec extends XDSerializationTest[Command] with CrossdataCommonSerializer {
  
  override implicit val formats: Formats = json4sJacksonFormats

  override def testCases: Seq[TestCase] = Seq(
    TestCase("marshall & unmarshall a SQLCommand", SQLCommand("select * from highschool"))
  )

}