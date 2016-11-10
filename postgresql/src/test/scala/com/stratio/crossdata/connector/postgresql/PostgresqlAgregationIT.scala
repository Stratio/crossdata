package com.stratio.crossdata.connector.postgresql

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PostgresqlAgregationIT extends PostgresqlWithSharedContext{

//  "The Postgresql connector" should s"support a (SELECT MAX() ) natively" in {
//    assumeEnvironmentIsUpAndRunning
//
//    val df = sql(s"SELECT MAX(id) as maxim FROM $Table")
//    println(df.queryExecution.optimizedPlan)
//    val result = df.collect()
//    result(0).getInt(0) should be (10)
//
//  }
//
//  it should s"support a (SELECT MIN()) natively" in {
//    assumeEnvironmentIsUpAndRunning
//
//    val df = sql(s"SELECT MIN(id) FROM $Table")
//    println(df.queryExecution.optimizedPlan)
//
//    val result = df.collect()
//    result(0).getInt(0) should be (1)
//
//  }
//
//  it should s"support a (SELECT SUM()) natively" in {
//    assumeEnvironmentIsUpAndRunning
//
//    val df = sql(s"SELECT SUM(id) FROM $Table")
//    println(df.queryExecution.optimizedPlan)
//
//    val result = df.collect()
//    result(0).getLong(0) should be (55)
//
//  }

  it should s"support a (SELECT AVG()) natively" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT AVG(id) FROM $Table")
    println(df.queryExecution.optimizedPlan)

    val result = df.collect()

    result(0).getDecimal(0) shouldBe a [java.math.BigDecimal]

  }

//  it should s"support a (SELECT Count()) natively" in {
//    assumeEnvironmentIsUpAndRunning
//
//    val df = sql(s"SELECT COUNT(id) FROM $Table")
//    println(df.queryExecution.optimizedPlan)
//
//    val result = df.collect()
//    result(0).getLong(0) should be (10)
//
//  }
//
//  it should s"support a (SELECT Count()) ... GROUP BY ... natively" in {
//    assumeEnvironmentIsUpAndRunning
//
//    val df = sql(s"SELECT comment, COUNT(id) as count FROM $Table GROUP BY id, comment")
//    println(df.queryExecution.optimizedPlan)
//    val result = df.collect()
//    result should have length 10
//    result(0).getLong(1) should be (1)
//  }
//
//  it should s"support a (SELECT Count()) ...WHERE ...  GROUP BY ... natively" in {
//    assumeEnvironmentIsUpAndRunning
//
//    val df = sql(s"SELECT comment, COUNT(id) as count FROM $Table WHERE id > 5 GROUP BY id, comment")
//    println(df.queryExecution.optimizedPlan)
//    val result = df.collect()
//    result should have length 5
//    result(0).getLong(1) should be (1)
//  }
//
//  it should s"support a (SELECT Count()) ... GROUP BY ... ORDER BY COUNT(id) natively" in {
//    assumeEnvironmentIsUpAndRunning
//
//    val df = sql(s"SELECT comment, COUNT(id) as countalias FROM $Table GROUP BY id, comment ORDER BY COUNT(id)")
//
//    println(df.queryExecution.optimizedPlan)
//    val result = df.collect()
//    result should have length 10
//    result(0).getLong(1) should be (1)
//  }
//
//
//  it should s"support a (SELECT Count()) ... GROUP BY ... ORDER BY alias natively" in {
//    assumeEnvironmentIsUpAndRunning
//
//    val df = sql(s"SELECT comment, COUNT(id) as countalias FROM $Table GROUP BY id, comment ORDER BY countalias")
//
//    println(df.queryExecution.optimizedPlan)
//    val result = df.collect()
//    result should have length 10
//    result(0).getLong(1) should be (1)
//  }




}