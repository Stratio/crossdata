/**
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
package com.stratio.crossdata.connector.cassandra

import org.apache.spark.sql.crossdata.ExecutionType._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CassandraFunctionIT extends CassandraWithSharedContext {

  val execTypes: List[ExecutionType] = Native::Spark::Nil

  execTypes.foreach { exec =>

    "The Cassandra connector" should s"be able to ${exec.toString}ly select the built-in functions `now`, `dateOf` and `unixTimeStampOf`" in {
      assumeEnvironmentIsUpAndRunning

      val query = s"SELECT cassandra_now() as t, cassandra_now() as a, cassandra_dateOf(cassandra_now()) as dt, cassandra_unixTimestampOf(cassandra_now()) as ut FROM $Table"
      sql(query).collect(exec) should have length 10
    }
  }

  it should s"be able to resolve non-duplicates functions automatically without specifying the datasource" in {
    assumeEnvironmentIsUpAndRunning

    val query = s"SELECT unixTimestampOf(now()) as ut FROM $Table"
    sql(query).collect(Native) should have length 10
  }


}
