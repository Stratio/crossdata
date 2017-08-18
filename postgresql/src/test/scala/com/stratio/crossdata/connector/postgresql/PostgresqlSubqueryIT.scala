/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.connector.postgresql

import org.apache.spark.sql.crossdata.ExecutionType
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PostgresqlSubqueryIT extends PostgresqlWithSharedContext {

  "The Postgresql connector" should s"support a SUBQUERY with alias natively" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT A.id FROM (SELECT * FROM $postgresqlSchema.$Table) A")
    val result = df.collect(ExecutionType.Native)
    result should have length 10
  }

  it should s"support JOIN with SUBQUERIES with alias natively" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT A.id FROM (SELECT * FROM $postgresqlSchema.$Table) A JOIN (SELECT * FROM $postgresqlSchema.$Table) B ON A.id= B.id")
    val result = df.collect(ExecutionType.Native)
    result should have length 10
  }


  //TODO change these test when upgrade to Spark 2.1

  ignore should s"support SUBQUERY in projection with alias natively" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT A.id, (SELECT MAX(id) FROM $postgresqlSchema.$Table) FROM $postgresqlSchema.$Table")
    val result = df.collect(ExecutionType.Native)
    result should have length 10
  }


  ignore should s"support a IN (SUBQUERY)" in {
    assumeEnvironmentIsUpAndRunning

      val df = sql(s"SELECT id FROM $postgresqlSchema.$Table WHERE id IN (SELECT id FROM $postgresqlSchema.$Table)")
      val result = df.collect(ExecutionType.Native)
      result should have length 10

  }

  ignore should s"support a EXIST (SUBQUERY)" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT id FROM $postgresqlSchema.$Table WHERE EXISTS(SELECT id FROM $postgresqlSchema.$Table) WHERE id <6")
    val result = df.collect(ExecutionType.Native)
    result should have length 6

  }

  ignore should s"support a select (SUBQUERY with AGGREGRATION)" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT country , (SELECT COUNT(country) FROM $postgresqlSchema.$aggregationTable B WHERE A.country = B.country ) AS countCountryFROM $postgresqlSchema.$aggregationTable A")
    val result = df.collect(ExecutionType.Native)
    result should have length 2

  }

}