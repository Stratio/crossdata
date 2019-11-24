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
class PostgresqlSortIT extends PostgresqlWithSharedContext{

  "The Postgresql connector" should s"support a (SELECT * .. ORDER BY DESC ) natively" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT * FROM $postgresqlSchema.$Table ORDER BY id DESC")
    val result = df.collect(ExecutionType.Native)
    result(0).getInt(0) should be (10)

  }

  it should s"support a (SELECT * .. ORDER BY DESC ) with alias natively" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT comment, id as idalias FROM $postgresqlSchema.$Table ORDER BY idalias DESC")
    val result = df.collect(ExecutionType.Native)
    result(0).getString(0) should be ("Comment 10")

  }

  it should s"support a (SELECT * .. ORDER BY ASC) natively" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT * FROM $postgresqlSchema.$Table ORDER BY id ASC")
    val result = df.collect(ExecutionType.Native)
    result(0).getInt(0) should be (1)

  }
}
