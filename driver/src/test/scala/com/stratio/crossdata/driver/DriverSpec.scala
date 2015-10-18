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
package com.stratio.crossdata.driver

import java.util.concurrent.TimeoutException

import akka.util.Timeout
import com.stratio.crossdata.common.SQLCommand
import com.stratio.crossdata.common.result.ErrorResult
import com.stratio.crossdata.test.BaseXDTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.language.postfixOps

@RunWith(classOf[JUnitRunner])
class DriverSpec extends BaseXDTest {

  "Crossdata driver" should "fail with a timeout when there is no server" in {
    val driver = Driver()
    val sqlCommand = SQLCommand("select * from any")
    val result = driver.syncQuery(sqlCommand, Timeout(1 seconds), 1)
    result.hasError should be (true)
    result.queryId should be (sqlCommand.queryId)
    result shouldBe an [ErrorResult]
    result.asInstanceOf[ErrorResult].message should include regex "(?i)timeout was exceed"
  }


  it should "return a future with a timeout when there is no server" in {
    val driver = Driver()
    val future = driver.asyncQuery(SQLCommand("select * from any"), Timeout(1 seconds), 1)
    a [TimeoutException] should be thrownBy Await.result(future, 2 seconds)
  }
}