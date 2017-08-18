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
