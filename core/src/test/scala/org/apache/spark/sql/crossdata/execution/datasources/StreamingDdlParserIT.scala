package org.apache.spark.sql.crossdata.execution.datasources

import org.apache.spark.sql.crossdata.test.SharedXDContextTest

class StreamingDdlParserIT extends SharedXDContextTest {

  "StreamingDDLParser" should "parse..." in {

    val sqlContext = _xdContext

    val temp = sqlContext.ddlParser.parse("ADD SELECT * FROM t")
    temp.toString()
  }

  it should "parse... 2" in {

    val sqlContext = _xdContext
    sqlContext.sql("CREATE EPHEMERAL TABLE t OPTIONS (ho 'hol')")
    val temp = sqlContext.ddlParser.parse("ADD SELECT * FROM t WITH WINDOW 5 SECS AS topic")
    //temp.toString()
    println("dsf")
  }
}
