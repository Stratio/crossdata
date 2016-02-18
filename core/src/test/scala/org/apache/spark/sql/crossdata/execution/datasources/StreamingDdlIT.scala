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
package org.apache.spark.sql.crossdata.execution.datasources

import org.apache.spark.sql.Row
import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.apache.spark.sql.types.{StringType, StructType, StructField}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.apache.spark.sql.crossdata.config.StreamingConfig._

@RunWith(classOf[JUnitRunner])
class StreamingDdlIT extends SharedXDContextTest{

  "Ephemeral Table" should "run correctly next sentences" in {

    //TODO zookeeper must be up and running

    val sqlContext = _xdContext

    val create = sql("CREATE EPHEMERAL TABLE ephemeralTest1 (id STRING) OPTIONS(kafka.options.test 'optionalConfig')").collect()
    val getStatus = sql("GET EPHEMERAL STATUS ephemeralTest1").collect()
    val getAllStatus = sql("GET EPHEMERAL STATUSES").collect()
    val getTable = sql("DESCRIBE EPHEMERAL TABLE ephemeralTest1").collect()
    val getAllTables = sql("SHOW EPHEMERAL TABLES").collect()
    val dropTable = sql("DROP EPHEMERAL TABLE ephemeralTest1").collect()
    sql("CREATE EPHEMERAL TABLE ephemeralTest1 (id STRING) OPTIONS(kafka.options.test 'optionalConfig')").collect()
    sql("CREATE EPHEMERAL TABLE ephemeralTest2 (id STRING) OPTIONS(kafka.options.test 'optionalConfig')").collect()
    val dropAllTables = sql("DROP EPHEMERAL TABLES").collect()

    // Results
    val createResult = Seq(Row(createEphemeralTableModel("ephemeralTest1", Option(StructType(Array(StructField("id", StringType)))), Map("kafka.options.test" -> "optionalConfig")).toStringPretty))
    val getResult = Seq(Row("{\n  \"ephemeralTableName\" : \"ephemeralTest1\",\n  \"status\" : \"NotStarted\"\n}"))
    val existsResult = Seq(Row("ephemeralTest1 EXISTS"))
    val getTableResult = Seq(Row(createEphemeralTableModel("ephemeralTest1", Option(StructType(Array(StructField("id", StringType)))), Map("kafka.options.test" -> "updateParam")).toStringPretty))
    val dropResult = Seq(Row("ephemeralTest1"))

    create should be (createResult)
    getStatus should be (getResult)
    getAllStatus should be (getResult)
    getTable should be (getTableResult)
    getAllTables should be (getTableResult)
    dropTable should be (dropResult)
    dropAllTables should contain allOf (Row("ephemeralTest1"), Row("ephemeralTest2"))

  }

}
