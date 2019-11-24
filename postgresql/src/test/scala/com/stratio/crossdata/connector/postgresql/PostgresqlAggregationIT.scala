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
class PostgresqlAggregationIT extends PostgresqlWithSharedContext{

  "The Postgresql connector" should s"support a (SELECT MAX() ) natively" in {
      assumeEnvironmentIsUpAndRunning

      val df = sql(s"SELECT MAX(id) as maxim FROM $postgresqlSchema.$Table")
      val result = df.collect(ExecutionType.Native)
      result(0).getInt(0) should be (10)

    }

    it should s"support a (SELECT MIN()) natively" in {
      assumeEnvironmentIsUpAndRunning

      val df = sql(s"SELECT MIN(id) FROM $postgresqlSchema.$Table")

      val result = df.collect(ExecutionType.Native)
      result(0).getInt(0) should be (1)

    }

    it should s"support a (SELECT SUM()) natively" in {
      assumeEnvironmentIsUpAndRunning

      val df = sql(s"SELECT SUM(id) FROM $postgresqlSchema.$Table")

      val result = df.collect(ExecutionType.Native)
      result(0).getLong(0) should be (55)

    }

  it should s"support a (SELECT AVG()) natively" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT AVG(id) FROM $postgresqlSchema.$Table")
    val result = df.collect(ExecutionType.Native)
    val avg: Double = 5.5
    result(0).getDouble(0) shouldBe avg
  }

    it should s"support a (SELECT Count()) natively" in {
      assumeEnvironmentIsUpAndRunning

      val df = sql(s"SELECT COUNT(id) FROM $postgresqlSchema.$Table")

      val result = df.collect(ExecutionType.Native)
      result(0).getLong(0) should be (10)

    }

    it should s"support a (SELECT Count()) ... GROUP BY ... natively" in {
      assumeEnvironmentIsUpAndRunning

      val df = sql(s"SELECT comment, COUNT(id) as count FROM $postgresqlSchema.$Table GROUP BY id, comment")
      val result = df.collect(ExecutionType.Native)
      result should have length 10
      result(0).getLong(1) should be (1)
    }

    it should s"support a (SELECT Count()) ...WHERE ...  GROUP BY ... natively" in {
      assumeEnvironmentIsUpAndRunning

      val df = sql(s"SELECT comment, COUNT(id) as count FROM $postgresqlSchema.$Table WHERE id > 5 GROUP BY id, comment")
      val result = df.collect(ExecutionType.Native)
      result should have length 5
      result(0).getLong(1) should be (1)
    }

    it should s"support a (SELECT Count()) ... GROUP BY ... ORDER BY COUNT(id) natively" in {
      assumeEnvironmentIsUpAndRunning

      val df = sql(s"SELECT comment, COUNT(id) as countalias FROM $postgresqlSchema.$Table GROUP BY id, comment ORDER BY COUNT(id)")

      val result = df.collect(ExecutionType.Native)
      result should have length 10
      result(0).getLong(1) should be (1)
    }


    it should s"support a (SELECT Count()) ... GROUP BY ... ORDER BY alias natively" in {
      assumeEnvironmentIsUpAndRunning

      val df = sql(s"SELECT comment, COUNT(id) as countalias FROM $postgresqlSchema.$Table GROUP BY id, comment ORDER BY countalias")

      val result = df.collect(ExecutionType.Native)
      result should have length 10
      result(0).getLong(1) should be (1)
    }


  it should s"support a (SELECT Count()) ... GROUP BY ... ORDER BY alias ... HAVING alias natively" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT comment, COUNT(id) as countalias FROM $postgresqlSchema.$Table GROUP BY id, comment HAVING countalias < 5 ORDER BY countalias ")

    val result = df.collect(ExecutionType.Native)
    result should have length 10
    result(0).getLong(1) should be (1)
  }



  it should s"support a (SELECT Count()) ... GROUP BY ... ORDER BY alias ... HAVING  natively" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT comment, COUNT(id) as countalias FROM $postgresqlSchema.$Table GROUP BY id, comment HAVING COUNT(id) < 5 ORDER BY countalias ")

    val result = df.collect(ExecutionType.Native)
    result should have length 10
    result(0).getLong(1) should be (1)
  }


}