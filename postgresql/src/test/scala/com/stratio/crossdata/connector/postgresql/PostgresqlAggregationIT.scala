/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.crossdata.connector.postgresql

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.crossdata.{ExecutionType, XDDataFrame}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PostgresqlAggregationIT extends PostgresqlWithSharedContext{

  "The Postgresql connector" should s"support a (SELECT MAX() ) natively" in {
      assumeEnvironmentIsUpAndRunning

      val df = sql(s"SELECT MAX(id) as maxim FROM $postgresqlSchema.$Table")
      println(df.queryExecution.optimizedPlan)
      val result = df.collect(ExecutionType.Native)
      result(0).getInt(0) should be (10)

    }

    it should s"support a (SELECT MIN()) natively" in {
      assumeEnvironmentIsUpAndRunning

      val df = sql(s"SELECT MIN(id) FROM $postgresqlSchema.$Table")
      println(df.queryExecution.optimizedPlan)

      val result = df.collect(ExecutionType.Native)
      result(0).getInt(0) should be (1)

    }

    it should s"support a (SELECT SUM()) natively" in {
      assumeEnvironmentIsUpAndRunning

      val df = sql(s"SELECT SUM(id) FROM $postgresqlSchema.$Table")
      println(df.queryExecution.optimizedPlan)

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
      println(df.queryExecution.optimizedPlan)

      val result = df.collect(ExecutionType.Native)
      result(0).getLong(0) should be (10)

    }

    it should s"support a (SELECT Count()) ... GROUP BY ... natively" in {
      assumeEnvironmentIsUpAndRunning

      val df = sql(s"SELECT comment, COUNT(id) as count FROM $postgresqlSchema.$Table GROUP BY id, comment")
      println(df.queryExecution.optimizedPlan)
      val result = df.collect(ExecutionType.Native)
      result should have length 10
      result(0).getLong(1) should be (1)
    }

    it should s"support a (SELECT Count()) ...WHERE ...  GROUP BY ... natively" in {
      assumeEnvironmentIsUpAndRunning

      val df = sql(s"SELECT comment, COUNT(id) as count FROM $postgresqlSchema.$Table WHERE id > 5 GROUP BY id, comment")
      println(df.queryExecution.optimizedPlan)
      val result = df.collect(ExecutionType.Native)
      result should have length 5
      result(0).getLong(1) should be (1)
    }

    it should s"support a (SELECT Count()) ... GROUP BY ... ORDER BY COUNT(id) natively" in {
      assumeEnvironmentIsUpAndRunning

      val df = sql(s"SELECT comment, COUNT(id) as countalias FROM $postgresqlSchema.$Table GROUP BY id, comment ORDER BY COUNT(id)")

      println(df.queryExecution.optimizedPlan)
      val result = df.collect(ExecutionType.Native)
      result should have length 10
      result(0).getLong(1) should be (1)
    }


    it should s"support a (SELECT Count()) ... GROUP BY ... ORDER BY alias natively" in {
      assumeEnvironmentIsUpAndRunning

      val df = sql(s"SELECT comment, COUNT(id) as countalias FROM $postgresqlSchema.$Table GROUP BY id, comment ORDER BY countalias")

      println(df.queryExecution.optimizedPlan)
      val result = df.collect(ExecutionType.Native)
      result should have length 10
      result(0).getLong(1) should be (1)
    }


  it should s"support a (SELECT Count()) ... GROUP BY ... ORDER BY alias ... HAVING alias natively" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT comment, COUNT(id) as countalias FROM $postgresqlSchema.$Table GROUP BY id, comment HAVING countalias < 5 ORDER BY countalias ")

    println(df.queryExecution.optimizedPlan)
    val result = df.collect(ExecutionType.Native)
    result should have length 10
    result(0).getLong(1) should be (1)
  }



  it should s"support a (SELECT Count()) ... GROUP BY ... ORDER BY alias ... HAVING  natively" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT comment, COUNT(id) as countalias FROM $postgresqlSchema.$Table GROUP BY id, comment HAVING COUNT(id) < 5 ORDER BY countalias ")

    println(df.queryExecution.optimizedPlan)
    val result = df.collect(ExecutionType.Native)
    result should have length 10
    result(0).getLong(1) should be (1)
  }


}