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

import java.nio.file.Paths

import com.stratio.crossdata.common.QueryCancelledReply
import com.stratio.crossdata.common.result.ErrorSQLResult
import com.stratio.crossdata.driver.test.Utils._
import org.junit.runner.RunWith
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.junit.JUnitRunner

import scala.concurrent.duration._

@RunWith(classOf[JUnitRunner])
class DriverQueryManagement extends EndToEndTest with ScalaFutures {

  driverFactories foreach { case (factory, description) =>

    implicit val ctx = DriverTestContext(factory)

    val factoryDesc = s" $description"

    "CrossdataDriver" should "be able to cancel queries" + factoryDesc in {
      assumeCrossdataUpAndRunning()

      withDriverDo { driver =>

        driver.sql(s"CREATE TEMPORARY TABLE jsonTable2 USING org.apache.spark.sql.json OPTIONS (path '${Paths.get (getClass.getResource("/tabletest.json").toURI).toString}')").waitForResult()

        val queryRq = driver.sql("SELECT DEBUG_SLEEP_MS(5000) FROM jsonTable2")
        val cancellationResponseFuture = queryRq.cancelCommand()

        whenReady(cancellationResponseFuture) { res =>
          res shouldBe a[QueryCancelledReply]
        } (PatienceConfig(timeout = 2 seconds))

        whenReady(queryRq.sqlResult) { sqlResult =>
          sqlResult shouldBe a[ErrorSQLResult]
        } (PatienceConfig(timeout = 3 seconds))

      }

    }

  }

}
