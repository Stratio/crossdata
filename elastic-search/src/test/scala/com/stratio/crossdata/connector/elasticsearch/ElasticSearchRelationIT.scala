package com.stratio.crossdata.connector.elasticsearch

import org.apache.spark.sql.crossdata.ExecutionType._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ElasticSearchRelationIT extends ElasticWithSharedContext {

  "The ElasticSearch Connector" should "execute natively a select *" in {
    assumeEnvironmentIsUpAndRunning
    val result = sql(s"SELECT * FROM $Type ").collect(Native)
    result should have length 10
  }

  it should "execute natively a select with simple filter" in {
    assumeEnvironmentIsUpAndRunning
    val result = sql(s"SELECT * FROM $Type where _id = 1").collect(Native)
    result should have length 1
  }
}

