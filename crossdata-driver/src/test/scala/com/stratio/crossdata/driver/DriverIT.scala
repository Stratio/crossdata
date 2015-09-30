/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.stratio.crossdata.driver

import java.nio.file.Paths

import akka.util.Timeout
import com.stratio.crossdata.common.SQLCommand
import com.stratio.crossdata.common.result.{SuccessfulQueryResult, ErrorResult}
import org.apache.spark.sql.AnalysisException
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.concurrent.duration._
import scala.language.postfixOps

@RunWith(classOf[JUnitRunner])
class DriverIT extends EndToEndTest {

  "Crossdata" should "return an ErrorResult when running an unparseable query" in {
    assumeCrossdataUpAndRunning()
    val driver= new Driver
    val sqlCommand = SQLCommand("select select")
    val result = driver.syncQuery(sqlCommand, Timeout(10 seconds), 2)
    result.queryId should be(sqlCommand.queryId)
    result shouldBe an[ErrorResult]
    result.asInstanceOf[ErrorResult].cause.isDefined shouldBe (true)
    result.asInstanceOf[ErrorResult].cause.get shouldBe a [AnalysisException]
    result.asInstanceOf[ErrorResult].cause.get.getMessage should include regex "cannot resolve .*"
  }


  it should "return a SuccessfulQueryResult when executing a select *" in {
    assumeCrossdataUpAndRunning()
    val driver = new Driver
    driver.syncQuery {
      SQLCommand( s"CREATE TEMPORARY TABLE jsonTable USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/tabletest.json").toURI()).toString}')")
    }
    // TODO how to process metadata ops?

    val sqlCommand = SQLCommand("SELECT * FROM jsonTable")
    val result = driver.syncQuery(sqlCommand)
    result shouldBe an[SuccessfulQueryResult]
    result.queryId should be (sqlCommand.queryId)
    result.hasError should be (false)
    result.resultSet.isDefined should be (true)
    val rows = result.resultSet.get
    rows should have length 2
    rows(0) should have length 2
  }

}