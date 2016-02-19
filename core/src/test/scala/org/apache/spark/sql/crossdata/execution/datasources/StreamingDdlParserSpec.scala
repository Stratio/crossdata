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

import com.stratio.crossdata.test.BaseXDTest
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.crossdata.config.CoreConfig
import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar

import scala.util.Try


@RunWith(classOf[JUnitRunner])
class StreamingDdlParserSpec extends BaseXDTest with MockitoSugar{

    val xdContext = mock[XDContext]
    val parser = new XDDdlParser(_ => null, xdContext)

  override val catalogConfig : Option[Config] =
    Try(ConfigFactory.load("core-catalog.conf").getConfig(CoreConfig.ParentConfigName)).toOption


  DescribeEphemeralTable
  ShowEphemeralTables
  CreateEphemeralTable
  DropEphemeralTable
  DropAllEphemeralTables
  ShowEphemeralStatus
  ShowAllEphemeralStatuses
  ShowEphemeralQueries
  AddEphemeralQuery
  DropEphemeralQuery
  DropAllEphemeralQueries
  StartProcess
  StopProcess

  "StreamingDDLParser" should "parse a create ephemeral table" in {

    val sqlContext = _xdContext

    val temp = sqlContext.ddlParser.parse("ADD SELECT * FROM t")

    temp.toString()
  }

/*  val create = sql("CREATE EPHEMERAL TABLE ephemeralTest1 (id STRING) OPTIONS(kafka.options.test 'optionalConfig')").collect()
  val getStatus = sql("GET EPHEMERAL STATUS ephemeralTest1").collect()
  val getAllStatus = sql("GET EPHEMERAL STATUSES").collect()
  val getTable = sql("DESCRIBE EPHEMERAL TABLE ephemeralTest1").collect()
  val getAllTables = sql("SHOW EPHEMERAL TABLES").collect()
  val dropTable = sql("DROP EPHEMERAL TABLE ephemeralTest1").collect()
  sql("CREATE EPHEMERAL TABLE ephemeralTest1 (id STRING) OPTIONS(kafka.options.test 'optionalConfig')").collect()
  sql("CREATE EPHEMERAL TABLE ephemeralTest2 (id STRING) OPTIONS(kafka.options.test 'optionalConfig')").collect()
  val dropAllTables = sql("DROP EPHEMERAL TABLES").collect()
  */


/*
 it should "parse... 2" in {

    val sqlContext = _xdContext
    sqlContext.sql("CREATE EPHEMERAL TABLE t OPTIONS (receiver.kafka.topic 'ephtable:1', receiver.kafka.groupId 'xd1')")
   // val temp = sqlContext.ddlParser.parse("ADD SELECT * FROM t WITH WINDOW 5 SECS AS topic")
    sqlContext.sql("ADD SELECT count(*) FROM t WITH WINDOW 5 SECS AS qalias")
    //temp.toString()
  }*/

  it should "parse... 2" in {

    val sqlContext = _xdContext
    sqlContext.sql("START t")
    Thread.sleep(50000)
    sqlContext.sql("STOP t")

  }

}

