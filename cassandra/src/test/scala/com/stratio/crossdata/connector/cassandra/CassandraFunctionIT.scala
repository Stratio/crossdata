package com.stratio.crossdata.connector.cassandra

import org.apache.spark.sql.crossdata.ExecutionType._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CassandraFunctionIT extends CassandraWithSharedContext {

  val execTypes: List[ExecutionType] = Native::Spark::Nil

  execTypes.foreach { exec =>

    "The Cassandra connector" should s"be able to ${exec.toString}ly select the built-in funcions `now`, `dateOf` and `unixTimeStampOf`" in {
      assumeEnvironmentIsUpAndRunning

      val query = s"SELECT now() as t, now() as a, dateOf(now()) as dt, unixTimestampOf(now()) as ut FROM $Table"
      sql(query).collect(exec) should have length 10
    }
  }

}
