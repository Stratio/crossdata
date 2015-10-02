package com.stratio.crossdata.common

import java.util.UUID

import com.stratio.crossdata.common.result.{SuccessfulQueryResult, ErrorResult}
import com.stratio.crossdata.test.BaseXDTest
import org.apache.spark.sql.Row
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SQLResultSpec extends BaseXDTest {

  "An error result" should "have an empty result" in {
    val error = ErrorResult(UUID.randomUUID(),"message")
    error.hasError should be (true)
    error.resultSet should be (None)
  }

  "A successful result" should "return an array of rows" in {
    val error = SuccessfulQueryResult(UUID.randomUUID(), Array(Row(1)))
    error.hasError should be (false)
    error.resultSet.get shouldEqual Array(Row(1))
  }

}
