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
package com.stratio.crossdata.connector.elasticsearch

import org.apache.spark.sql.crossdata.ExecutionType._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ElasticSearchRelationIT extends ElasticWithSharedContext {

  "The ElasticSearch Connector" should "execute natively a select *" in {
    assumeEnvironmentIsUpAndRunning

    //Experimentation
    val dataframe = sql(s"SELECT * FROM $Type ")

    //Expectations
    val schema = dataframe.schema
    val result = dataframe.collect(Native)

    result should have length 10
    schema.fieldNames should equal (Seq("id", "age", "description", "enrolled", "name", "optionalField"))
    result.head.toSeq(2).toString.contains("description") should be (true)
  }

  ignore should "execute natively a select with simple filter" in {
    assumeEnvironmentIsUpAndRunning
    val result = sql(s"SELECT * FROM $Type where _id = 1").collect(Native)
    result should have length 1
  }
}

