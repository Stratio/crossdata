package com.stratio.crossdata.connector.postgresql

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PostgresqlSortIT extends PostgresqlWithSharedContext{

  "The Postgresql connector" should s"support a (SELECT * .. ORDER BY ) natively" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT * FROM $Table ORDER BY id ASC, comment DESC")
      println(df.queryExecution.toString())
      df.collect() should have length 0
  }

}
