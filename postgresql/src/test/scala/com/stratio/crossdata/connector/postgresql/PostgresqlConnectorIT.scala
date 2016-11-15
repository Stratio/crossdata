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

import org.apache.spark.sql.crossdata.ExecutionType._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PostgresqlConnectorIT extends PostgresqlWithSharedContext {

  Seq(Native, Spark) foreach { executionType =>

    "The Postgresql connector" should s"support a (SELECT *) for $executionType execution" in {
      assumeEnvironmentIsUpAndRunning
      val dataframe = sql(s"SELECT * FROM $postgresqlSchema.$Table ")
      val schema = dataframe.schema
      val result = dataframe.collect(executionType)

      schema.fieldNames should equal (Seq("id", "age", "comment", "enrolled", "name"))
      result should have length 10
      result(0) should have length 5
    }

    it should s"support a query with limit 0 for $executionType execution" in {
      assumeEnvironmentIsUpAndRunning

      val result = sql(s"SELECT * FROM $postgresqlSchema.$Table LIMIT 0").collect(executionType)
      result should have length 0
    }

    it should s"support a (SELECT column) for $executionType execution" in {

      assumeEnvironmentIsUpAndRunning

      val result = sql(s"SELECT id FROM $postgresqlSchema.$Table ").collect(executionType)
      result should have length 10
      result(0) should have length 1
    }

    it should s"support a (SELECT * ... WHERE PK = _ ) for $executionType execution" in {
      assumeEnvironmentIsUpAndRunning

      val result = sql(s"SELECT * FROM $postgresqlSchema.$Table WHERE id = 1").collect(executionType)
      result should have length 1
    }

    it should s"support a (SELECT * ... WHERE COLUMN IN (...) ) for $executionType execution" in {
      assumeEnvironmentIsUpAndRunning

      val result = sql(s"SELECT * FROM $postgresqlSchema.$Table WHERE id IN (1,5,9)").collect(executionType)
      result should have length 3
    }

    it should s"support a (SELECT * ...  WHERE PK = _ AND FIELD_2 = _ AND FIELD_3 = _) for $executionType execution" in {
      assumeEnvironmentIsUpAndRunning

      val result = sql(s"SELECT * FROM $postgresqlSchema.$Table WHERE id = 3 AND age = 13 AND comment = 'Comment 3' ").collect(executionType)
      result should have length 1
    }

  }

}