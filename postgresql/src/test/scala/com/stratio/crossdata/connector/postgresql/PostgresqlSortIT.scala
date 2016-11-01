package com.stratio.crossdata.connector.postgresql

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PostgresqlSortIT extends PostgresqlWithSharedContext{

  "The Postgresql connector" should s"support a (SELECT * .. ORDER BY DESC ) natively" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT * FROM $Table ORDER BY id DESC")
    val result = df.collect()
    result(0).getInt(0) should be (10)

  }

  it should s"support a (SELECT * .. ORDER BY ASC) natively" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT * FROM $Table ORDER BY id ASC")
    val result = df.collect()
    result(0).getInt(0) should be (1)

  }
}
