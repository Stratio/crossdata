package com.stratio.crossdata.connector.cassandra

import org.apache.spark.sql.crossdata.ExecutionType._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CassandraFunctionIT extends CassandraWithSharedContext {

  val execTypes: List[ExecutionType] = Native::Spark::Nil

  execTypes.foreach { exec =>

    "The Cassandra connector" should s"be able to ${exec.toString}ly select the built-in functions `now`, `dateOf` and `unixTimeStampOf`" in {
      assumeEnvironmentIsUpAndRunning

      val query = s"SELECT cassandra_now() as t, cassandra_now() as a, cassandra_dateOf(cassandra_now()) as dt, cassandra_unixTimestampOf(cassandra_now()) as ut FROM $Table"
      sql(query).collect(exec) should have length 10
    }
  }

  it should s"be able to resolve non-duplicates functions automatically without specifying the datasource" in {
    assumeEnvironmentIsUpAndRunning

    val query = s"SELECT unixTimestampOf(cassandra_now()) as ut FROM $Table"
    sql(query).collect(Native) should have length 10
  }


}
