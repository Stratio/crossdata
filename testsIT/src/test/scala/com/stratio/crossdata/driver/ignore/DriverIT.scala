/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.driver.ignore

import java.nio.file.Paths

import com.stratio.crossdata.common.result.{SQLResult, SuccessfulSQLResult}
import com.stratio.crossdata.driver.test.Utils._
import com.stratio.crossdata.test.BaseXDTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.language.postfixOps
import scala.util.Random

@RunWith(classOf[JUnitRunner])
class DriverIT extends BaseXDTest {

  it should "be able to execute a query involving a temporary table in any server" ignore {// TODO it is ignored until a crossdata-server container can be launched

    withDriverDo { driver =>

      driver.sql(s"CREATE TEMPORARY TABLE jsonTable USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/tabletest.json").toURI).toString}')").waitForResult()

      for (_ <- 1 to 3) {
        // It assumes that the driver has a round robin policy
        val result = driver.sql("SELECT * FROM jsonTable").waitForResult()
        validateResult(result)
      }

    }
  }

  it should "be able to specify custom spark sql parameters" ignore { // TODO make a test

    val randomTable = s"random${Math.abs(Random.nextLong())}"

    withDriverDo { driver =>
      withDriverDo { anotherDriver =>

        driver.sql(s"CREATE TEMPORARY TABLE $randomTable USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/tabletest.json").toURI).toString}')").waitForResult()
        anotherDriver.sql(s"CREATE TEMPORARY TABLE $randomTable USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/tabletest.json").toURI).toString}')").waitForResult()

        driver.sql(s"SET spark.sql.shuffle.partitions=400").waitForResult()
        anotherDriver.sql(s"SET spark.sql.shuffle.partitions=400").waitForResult()

        Thread.sleep(100)

        for (_ <- 1 to 3) {
          // It assumes that the driver has a round robin policy
          val result = driver.sql(s"SELECT title, count(*) FROM $randomTable GROUP BY title").waitForResult()
          validateResult(result)

          val result2 = anotherDriver.sql(s"SELECT title, count(*) FROM $randomTable GROUP BY title").waitForResult()
          validateResult(result2)
        }

      }
    }
  }

  private def validateResult(result: SQLResult): Unit = {
    result shouldBe an[SuccessfulSQLResult]
    result.hasError should be(false)
    val rows = result.resultSet
    rows should have length 2
    rows(0) should have length 2
  }
}