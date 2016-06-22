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
package com.stratio.crossdata.driver.ignore

import java.nio.file.Paths

import com.stratio.crossdata.common.result.{SQLResult, SuccessfulSQLResult}
import com.stratio.crossdata.driver.Driver
import com.stratio.crossdata.test.BaseXDTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.language.postfixOps

@RunWith(classOf[JUnitRunner])
class DriverIT extends BaseXDTest {

  it should "be able to execute a query involving a temporary table in any server" ignore {// TODO it is ignored until a crossdata-server container can be launched

    val driver = Driver.getOrCreate()

    driver.sql(s"CREATE TEMPORARY TABLE jsonTable USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/tabletest.json").toURI).toString}')").waitForResult()

    for (_ <- 1 to 3) {
      // It assumes that the driver has a round robin policy
      val result = driver.sql("SELECT * FROM jsonTable").waitForResult()
      validateResult(result)
    }

    driver.stop()
  }

  private def validateResult(result: SQLResult): Unit = {
    result shouldBe an[SuccessfulSQLResult]
    result.hasError should be(false)
    val rows = result.resultSet
    rows should have length 2
    rows(0) should have length 2
  }
}