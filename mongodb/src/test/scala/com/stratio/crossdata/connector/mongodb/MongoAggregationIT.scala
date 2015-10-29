package com.stratio.crossdata.connector.mongodb

import org.apache.spark.sql.crossdata.ExecutionType._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MongoAggregationIT extends MongoWithSharedContext {

  //(id BIGINT, age INT, description STRING, enrolled BOOLEAN, name STRING, optionalField BOOLEAN)

  "The Mongo connector" should "execute natively a (SELECT count(*) FROM _)" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT count(*) FROM $Collection").collect(Native)
    result should have length 1
    result(0) should have length 1
    result(0).getInt(0) should be(10)
  }

  it should "execute natively a (SELECT max(col), min(col), avg(col), sum(col), first(col), last(col) FROM _)" in {
    assumeEnvironmentIsUpAndRunning
    val dataframe = sql(s"SELECT max(age), min(age), avg(age), sum(age), first(age), last(age) FROM $Collection")
    val result = dataframe.collect(Native)
    result should have length 1
    result(0) should have length 6
    result(0).toSeq should be (Seq(20,10,15,84,1,2))
    // TODO update comparation taking into account real values
  }

  it should "execute natively a (SELECT count(DISTINCT age) FROM _)" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT count(DISTINCT age) FROM $Collection").collect(Native)
    result should have length 1
    result(0) should have length 1
    result(0).getInt(0) should be(10)
  }

  it should "execute natively a (SELECT col, count(*) FROM _ GROUP BY col)" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT enrolled, count(*) FROM $Collection GROUP BY enrolled").collect(Native)
    ???
  }

  it should "execute natively a (SELECT col1, col2, count(*) FROM _ GROUP BY col1, col2)" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT id, enrolled, count(*) FROM $Collection GROUP BY id, enrolled").collect(Native)
    ???
  }

  it should "execute natively a (SELECT DISTINCT id FROM _)" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT DISTINCT id FROM $Collection").collect(Native)
    ???
  }

  it should "execute natively a (SELECT count(*) FROM _ GROUP BY _ WHERE filterCondition)" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT count(*) FROM $Collection  GROUP BY enrolled WHERE id > 5").collect(Native)
    ???
  }

  // Tests SELECT count(distinct age) GROUP BY enrolled WHERE id > 5 (=> project age and id, => filter id => project age => count distinct
  it should "execute natively a (SELECT count(DISTINCT col) FROM _ GROUP BY col WHERE filterCondition)" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT count(DISTINCT age) FROM $Collection GROUP BY enrolled WHERE id > 5").collect(Native)
    ???
  }

  // SELECT name, sum(age) FROM ... WHERE id > 5 GROUP BY name HAVING sum(age) > 25
  it should "execute natively a (SELECT name, sum(age) FROM _ GROUP BY _ HAVING _)" in {
    assumeEnvironmentIsUpAndRunning

    val result = sql(s"SELECT name, sum(age) FROM $Collection  GROUP BY name HAVING sum(age) > 25").collect(Native)
    ???
  }

}
