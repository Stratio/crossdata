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
