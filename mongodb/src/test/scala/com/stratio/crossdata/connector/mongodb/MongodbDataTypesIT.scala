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
package com.stratio.crossdata.connector.mongodb

import org.apache.spark.sql.crossdata.ExecutionType
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MongodbDataTypesIT extends MongoDataTypesCollection {

  override val emptyTypesSetError: String = "Type test entries should have been already inserted"

  doTypesTest("The MongoDB connector")

  it should "be able to natively select array elements using their index" in {
    assumeEnvironmentIsUpAndRunning

    val df =
      sql(s"SELECT arraystring, arraystring[2], arraystring[-1], arrayint[0] FROM typesCheckTable")
    val firstRow = df.collect(ExecutionType.Native).head

    firstRow(0) shouldBe a[Seq[_]] // Whole `arraystring` column
    firstRow(1) shouldBe a[String] // Access to a single element within a string array
    Option(firstRow(2)) shouldBe None // Access to an out-of-bounds index
    firstRow(3) shouldBe a[Integer] // Access to a single element within an int array

  }

  it should "to natively filter by array column indexed elements" in {
    assumeEnvironmentIsUpAndRunning

    val query = """|SELECT arraystring, arraystring[2], arraystring[-1], arrayint[0]
         | FROM typesCheckTable
         | WHERE (arrayint[0] = 1 OR arrayint[1] = 1) AND arrayint[2] = 3
      """.stripMargin.replace("\n", "")

    val df = sql(query)
    val res = df.collect(ExecutionType.Native)

    res.length should equal(10)

  }

}
