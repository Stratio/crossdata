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

import org.apache.spark.sql.crossdata.{ExecutionType, XDDataFrame}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PostgresqlJoinIT extends PostgresqlWithSharedContext {

  "The Postgresql connector" should s"support a simple JOIN natively" in {
    assumeEnvironmentIsUpAndRunning
    val df = sql(s"SELECT * FROM $postgresqlSchema.$Table JOIN $postgresqlSchema.$aggregationTable ON $postgresqlSchema.$Table.id = $postgresqlSchema.$aggregationTable.id ")
    val result = df.collect(ExecutionType.Native)

    result should have length 10
  }

  // SQLBuilder does not support table alias, so in this case direct query is executed.as second attempt.

  it should s"support a JOIN with alias natively" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT * FROM $postgresqlSchema.$Table A JOIN $postgresqlSchema.$aggregationTable B ON A.id = B.id ")
    val result = df.collect(ExecutionType.Native)

    result should have length 10
  }

  it should s"support a INNER  JOIN with alias natively" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT * FROM $postgresqlSchema.$Table A INNER JOIN $postgresqlSchema.$aggregationTable B ON A.id = B.id ")
    val result = df.collect(ExecutionType.Native)

    result should have length 10
  }

  it should s"support a FULL OUTER JOIN with alias natively" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT * FROM $postgresqlSchema.$Table A FULL OUTER JOIN $postgresqlSchema.$aggregationTable B ON A.id = B.id ")
    val result = df.collect(ExecutionType.Native)

    result should have length 20
  }

  it should s"support a LEFT OUTER JOIN with alias natively" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT * FROM $postgresqlSchema.$Table A LEFT OUTER JOIN $postgresqlSchema.$aggregationTable B ON A.id = B.id ")
    val result = df.collect(ExecutionType.Native)

    result should have length 10
  }

  it should s"support a RIGHT OUTER JOIN with alias natively" in {
    assumeEnvironmentIsUpAndRunning

    val df = sql(s"SELECT * FROM $postgresqlSchema.$Table A RIGHT OUTER JOIN $postgresqlSchema.$aggregationTable B ON A.id = B.id ")
    val result = df.collect(ExecutionType.Native)

    result should have length 20
  }

}
