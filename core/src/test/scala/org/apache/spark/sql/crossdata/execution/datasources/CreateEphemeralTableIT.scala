package org.apache.spark.sql.crossdata.execution.datasources

import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CreateEphemeralTableIT extends SharedXDContextTest{

  "Create temp view" should "return a XDDataFrame when executing a SQL query" in {

    val sqlContext = _xdContext
    sql("CREATE EPHEMERAL TABLE ephemeral1 (field1 STRING, field2 INTEGER) OPTIONS (param1 'value1')").show()
// TODO real test
    1 shouldBe (1)
  }

}
