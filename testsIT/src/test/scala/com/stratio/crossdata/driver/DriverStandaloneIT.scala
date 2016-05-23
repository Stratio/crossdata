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
package com.stratio.crossdata.driver

import java.util.concurrent.TimeoutException

import com.stratio.crossdata.common.result.ErrorSQLResult
import com.stratio.crossdata.test.BaseXDTest

import scala.concurrent.Await
import scala.concurrent.duration._

class DriverStandaloneIT extends BaseXDTest {

  "Crossdata driver" should "fail with a timeout when there is no server" in {
    val driver = Driver.getOrCreate()

    val result = driver.sql("select * from any").waitForResult(1 seconds)

    result.hasError should be(true)
    a[RuntimeException] should be thrownBy result.resultSet

    result shouldBe an[ErrorSQLResult]
    result.asInstanceOf[ErrorSQLResult].message should include regex "(?i)timeout was exceed"

  }


  it should "return a future with a timeout when there is no server" in {
    val driver = Driver.getOrCreate()
    val future = driver.sql("select * from any").sqlResult
    a[TimeoutException] should be thrownBy Await.result(future, 2 seconds)
  }

  it should "indicates that the cluster is not alive when there is no server" in {
    val driver = Driver.getOrCreate()
    driver.isClusterAlive(1 second) shouldBe false
  }

}
