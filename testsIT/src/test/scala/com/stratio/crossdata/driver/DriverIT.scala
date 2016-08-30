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

import com.stratio.crossdata.common.result.{ErrorSQLResult, SuccessfulSQLResult}
import com.stratio.crossdata.driver.test.Utils._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.reflect.io.File

@RunWith(classOf[JUnitRunner])
class DriverIT extends EndToEndTest {

  "CrossdataDriver" should "return an ErrorResult when running an unparseable query" in {

    assumeCrossdataUpAndRunning()
    withDriverDo { driver =>

      val result = driver.sql("select select").waitForResult(10 seconds)
      result shouldBe an[ErrorSQLResult]
      result.asInstanceOf[ErrorSQLResult].cause.isDefined shouldBe (true)
      result.asInstanceOf[ErrorSQLResult].cause.get shouldBe a[Exception]
      result.asInstanceOf[ErrorSQLResult].cause.get.getMessage should include regex "cannot resolve .*"
    }
  }

  it should "return a SuccessfulQueryResult when executing a select *" in {
    assumeCrossdataUpAndRunning()
    withDriverDo { driver =>

      driver.sql(s"CREATE TEMPORARY TABLE jsonTable USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/tabletest.json").toURI).toString}')").waitForResult()

      val result = driver.sql("SELECT * FROM jsonTable").waitForResult()
      result shouldBe an[SuccessfulSQLResult]
      result.hasError should be(false)
      val rows = result.resultSet
      rows should have length 2
      rows(0) should have length 2
    }
  }

  it should "get a list of tables" in {
    assumeCrossdataUpAndRunning
    withDriverDo { driver =>

      driver.sql(
        s"CREATE TABLE db.jsonTable2 USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/tabletest.json").toURI).toString}')"
      ).waitForResult()

      driver.sql(
        s"CREATE TABLE jsonTable2 USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/tabletest.json").toURI).toString}')"
      ).waitForResult()

      driver.listTables() should contain allOf(("jsonTable2", Some("db")), ("jsonTable2", None))
    }
  }



  it should "indicates that the cluster is alive when there is a server up" in {
    withDriverDo { driver =>

      driver.isClusterAlive(6 seconds) shouldBe true
    }
  }

  it should "return the addresses of servers up and running" in {
    withDriverDo { driver =>

      val addresses = Await.result(driver.serversUp(), 6 seconds)

      addresses should have length 1
      addresses.head.host shouldBe Some("127.0.0.1")
    }
  }

  it should "return the current cluster state" in {
    withDriverDo { driver =>

      val clusterState = Await.result(driver.clusterState(), 6 seconds)

      clusterState.getLeader.host shouldBe Some("127.0.0.1")
    }
  }

  //TODO: Enable this tests
  it should "be able to execute ADD JAR Command of an existent file" ignore {
    // TODO restore before merging session to master
    assumeCrossdataUpAndRunning

    val file = File(s"/tmp/bulk_${System.currentTimeMillis()}.jar").createFile(false)
    withDriverDo { driver =>
      val result = driver.addJar(file.path).waitForResult()
      file.delete()
      result.hasError should equal(false)
    }

  }

  //TODO: Enable this test
  it should "be return an Error when execute ADD JAR Command of an un-existent file" ignore {

    withDriverDo { driver =>
      val result = driver.addJar(s"/tmp/jarnotexists").waitForResult()
      result.hasError should equal(true)
    }
  }

  it should "be able to execute ADD APP Command of an existent file" ignore {
    assumeCrossdataUpAndRunning

    val filePath = getClass.getResource("/TestAddApp.jar").getPath
    withDriverDo { driver =>
      val result = driver.addAppCommand(filePath, "com.stratio.addApp.AddAppTest.main", Some("testApp")).waitForResult()
      driver.sql("EXECUTE testApp(rain,bow)").waitForResult()
      result.hasError should equal(false)
    }

  }

  it should "be able to execute ADD APP Command of an existent file with options" ignore {
    assumeCrossdataUpAndRunning

    val filePath = getClass.getResource("/TestAddApp.jar").getPath

    withDriverDo { driver =>
      val addAppResult = driver.addAppCommand(filePath, "com.stratio.addApp.AddAppTest.main", Some("testApp")).waitForResult()
      addAppResult.hasError should equal(false)

      val executeResult = driver.sql("""EXECUTE testApp(rain,bow2) OPTIONS (executor.memory '20G')""").waitForResult()

      executeResult.hasError should equal(false)
      executeResult.resultSet.length should equal(1)
      executeResult.resultSet(0).get(0) should equal("Spark app launched")

    }
  }


  it should "allow running multiple drivers per JVM" in {

    val driverTable = "drvtable"
    val anotherDriverTable = "anotherTable"

    withDriverDo { driver =>
      withDriverDo { anotherDriver =>
        driver shouldNot be theSameInstanceAs anotherDriver
        driver.listTables().size shouldBe anotherDriver.listTables().size

        driver.sql(s"CREATE TEMPORARY TABLE $driverTable USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/tabletest.json").toURI).toString}')").waitForResult()
        driver.sql(s"SELECT * FROM $driverTable").waitForResult().resultSet should not be empty
        anotherDriver.sql(s"SELECT * FROM $driverTable").waitForResult().hasError shouldBe true

        anotherDriver.sql(s"CREATE TEMPORARY TABLE $anotherDriverTable USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/tabletest.json").toURI).toString}')").waitForResult()
        anotherDriver.sql(s"SELECT * FROM $anotherDriverTable").waitForResult().resultSet should not be empty
        driver.sql(s"SELECT * FROM $anotherDriverTable").waitForResult().hasError shouldBe true

      }
      // Once 'anotherDriver' closes its session, 'driver' should be still alive
      driver.sql(s"SELECT * FROM $driverTable").waitForResult().resultSet should not be empty
      driver.sql(s"SELECT * FROM $anotherDriverTable").waitForResult().hasError shouldBe true
    }
  }

}