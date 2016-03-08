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
package com.stratio.crossdata.common

//import com.stratio.crossdata.common.result.{ErrorResult, SuccessfulQueryResult}

import java.util.UUID

import com.stratio.crossdata.common.result.{ErrorSQLResult, SuccessfulSQLResult}
import com.stratio.crossdata.test.BaseXDTest
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructType
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar

@RunWith(classOf[JUnitRunner])
class SQLResultSpec extends BaseXDTest  with MockitoSugar{

  "An error result" should "have an empty result" in {
    val error = ErrorSQLResult("message")
    error.hasError should be (true)
    a [RuntimeException] should be thrownBy error.resultSet
  }

  "An SuccessfulQueryResult " should "have a resultSet" in {

    val queryId: UUID = UUID.randomUUID()
    val row = mock[Row]
    val result: Array[Row] = Array(row)
    val schema: StructType = mock[StructType]

    val toTest = SuccessfulSQLResult(result, schema)

    //Experimentation
    val res = toTest.resultSet
    val hasError = toTest.hasError

    res should not be (null)
    res should be equals Array(row)
    hasError should be (false)
  }

}
