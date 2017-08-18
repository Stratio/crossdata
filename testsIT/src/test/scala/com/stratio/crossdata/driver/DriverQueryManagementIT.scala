/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
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
import scala.util.Try

@RunWith(classOf[JUnitRunner])
class DriverQueryManagementIT extends EndToEndTest with ScalaFutures {

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

  override def stop(): Unit = Try(super.stop()) //TODO

}
