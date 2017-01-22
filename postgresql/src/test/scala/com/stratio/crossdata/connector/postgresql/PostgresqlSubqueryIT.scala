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