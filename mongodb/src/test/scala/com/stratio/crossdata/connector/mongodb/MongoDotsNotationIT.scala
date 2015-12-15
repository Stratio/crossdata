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
package com.stratio.crossdata.connector.mongodb

import org.apache.spark.sql.crossdata.ExecutionType
import org.apache.spark.sql.crossdata.exceptions.CrossdataException
import org.apache.spark.sql.types.StructField
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MongoDotsNotationIT extends MongoWithSharedContext {


  it should "supports Projection with DOT notation using Spark" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT bigint, structofstruct.field1 FROM $DataTypesCollection").collect(ExecutionType.Spark)

    sparkRow.head.schema.size should be (2)
    sparkRow.head.schema.head.isInstanceOf[StructField] should be (true)
  }

  it should "supports Projection with DOT notation with no ExecutionType defined" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT bigint, structofstruct.field1 FROM $DataTypesCollection").collect()

    sparkRow.head.schema.size should be (2)
    sparkRow.head.schema.head.isInstanceOf[StructField] should be (true)
  }

  it should "Does not supports Projection with DOT notation in Native" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT bigint, structofstruct.field1 FROM $DataTypesCollection")

    an [CrossdataException] should be thrownBy df.collect(ExecutionType.Native)
  }

  it should "supports Filters with DOT notation with no ExecutionType defined" in {
    assumeEnvironmentIsUpAndRunning

    val sparkRow = sql(s"SELECT int FROM $DataTypesCollection WHERE struct.field2=3").collect()

    sparkRow.length should be (10)
  }

  it should "Does not supports Filters with DOT notation in Native" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT int FROM $DataTypesCollection WHERE struct.field2=3")

    an [CrossdataException] should be thrownBy df.collect(ExecutionType.Native)
  }
}
