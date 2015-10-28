package com.stratio.crossdata.connector.mongodb

import org.apache.spark.sql.crossdata.ExecutionType._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MongoAggregationIT extends MongoWithSharedContext {

  //(id BIGINT, age INT, description STRING, enrolled BOOLEAN, name STRING, optionalField BOOLEAN)

  "The Mongo connector" should "execute natively a (SELECT count(*) FROM _)" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT count(*) FROM $Collection ").collect(Native)
    result should have length 1
    result(0) should have length 1
    result(0).getInt(0) should be(10)
  }

  it should "execute natively a (SELECT count(DISTINCT age) FROM _)" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT count(*) FROM $Collection ").collect(Native)
    result should have length 1
    result(0) should have length 1
    result(0).getInt(0) should be(10)
  }

  it should "execute natively a (SELECT max(age), min(age), avg(age), sum(age), first(age), last(age)  AS alias FROM _)" in {
    assumeEnvironmentIsUpAndRunning
    val dataframe = sql(s"SELECT count(*) as agg FROM $Collection ")
    val result = dataframe.collect(Native)
    result should have length 1
    result(0) should have length 1
    result(0).getInt(0) should be(10)
    dataframe.schema.fieldNames should be(Array("agg"))
  }

  // SELECT enrolled, count(*) GROUP BY enrolled

  // SELECT id, enrolled, count(*) GROUP BY id, enrolled

  // SELECT DISTINCT id FROM ...

  // SELECT count(*) GROUP BY enrolled WHERE id > 5

  // SELECT name, sum(age) FROM ... WHERE id > 5 GROUP BY name HAVING sum(age) > 25

}
