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
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.reflect.io.File

@RunWith(classOf[JUnitRunner])
class DriverIT extends EndToEndTest {

  "Crossdata" should "return an ErrorResult when running an unparseable query" in {

    assumeCrossdataUpAndRunning()
    val driver = Driver.getOrCreate()

    val result = driver.sql("select select").waitForResult(10 seconds)
    result shouldBe an[ErrorSQLResult]
    result.asInstanceOf[ErrorSQLResult].cause.isDefined shouldBe (true)
    result.asInstanceOf[ErrorSQLResult].cause.get shouldBe a[Exception]
    result.asInstanceOf[ErrorSQLResult].cause.get.getMessage should include regex "cannot resolve .*"
  }

  it should "return a SuccessfulQueryResult when executing a select *" in {
    assumeCrossdataUpAndRunning()
    val driver = Driver.getOrCreate()

    driver.sql(s"CREATE TEMPORARY TABLE jsonTable USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/tabletest.json").toURI).toString}')").waitForResult()


    // TODO how to process metadata ops?
    val result = driver.sql("SELECT * FROM jsonTable").waitForResult()
    result shouldBe an[SuccessfulSQLResult]
    result.hasError should be(false)
    val rows = result.resultSet
    rows should have length 2
    rows(0) should have length 2

    crossdataServer.flatMap(_.xdContext).foreach(_.dropTempTable("jsonTable"))
  }

  it should "get a list of tables" in {
    assumeCrossdataUpAndRunning
    val driver = Driver.getOrCreate()

    driver.sql(
      s"CREATE TABLE db.jsonTable2 USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/tabletest.json").toURI).toString}')"
    ).waitForResult()

    driver.sql(
      s"CREATE TABLE jsonTable2 USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/tabletest.json").toURI).toString}')"
    ).waitForResult()

    driver.listTables() should contain allOf(("jsonTable2", Some("db")), ("jsonTable2", None))
  }

  "Crossdata Driver" should "be able to close the connection and start it again" in {
    assumeCrossdataUpAndRunning
    var driver = Driver.getOrCreate();
    Driver.getOrCreate()
    val newDriver = Driver.getOrCreate()

    driver should be theSameInstanceAs newDriver


    driver.sql(s"SHOW TABLES")

    driver.stop()

    Thread.sleep(6000)

    driver = Driver.getOrCreate()

    val result = driver.sql(s"SHOW TABLES")

    result.hasError should equal(false)

  }

  it should "indicates that the cluster is alive when there is a server up" in {
    val driver = Driver.getOrCreate()

    driver.isClusterAlive(6 seconds) shouldBe true
  }

  it should "return the addresses of servers up and running" in {
    val driver = Driver.getOrCreate()

    val addresses = Await.result(driver.serversUp(), 6 seconds)

    addresses should have length 1
    addresses.head.host shouldBe Some("127.0.0.1")
  }

  it should "return the current cluster state" in {
    val driver = Driver.getOrCreate()

    val clusterState = Await.result(driver.clusterState(), 6 seconds)

    clusterState.getLeader.host shouldBe Some("127.0.0.1")
  }





        it should "be able to execute ADD JAR Command of an existent file" in {
          assumeCrossdataUpAndRunning

          val file=File(s"/tmp/bulk_${System.currentTimeMillis()}.jar").createFile(false)
          val driver = Driver.getOrCreate()
          val result = driver.addJar(file.path).waitForResult()

          driver.stop()
          file.delete()

          result.hasError should equal (false)
        }

      it should "be return an Error when execute ADD JAR Command of an un-existent file" in {

        val driver = Driver.getOrCreate()
        val result = driver.addJar(s"/tmp/jarnotexists").waitForResult()
        driver.stop()

        result.hasError should equal (true)
      }

  //TODO Uncomment when CD be ready

//  it should "be able to execute ADD APP Command of an existent file" in {
//    assumeCrossdataUpAndRunning
//
//    val filePath = getClass.getResource("/TestAddApp.jar").getPath
//    val driver = Driver.getOrCreate()
//    val result = driver.addAppCommand(filePath, "com.stratio.addApp.AddAppTest.main", Some("testApp")).waitForResult()
//    driver.sql("EXECUTE testApp(rain,bow)").waitForResult()
//    driver.stop()
//    result.hasError should equal(false)
//
//
//  }

  //TODO Uncomment when CD be ready
//  it should "be able to execute ADD APP Command of an existent file with options" in {
//    assumeCrossdataUpAndRunning
//
//    val filePath = getClass.getResource("/TestAddApp.jar").getPath
//    val driver = Driver.getOrCreate()
//    val addAppResult = driver.addAppCommand(filePath, "com.stratio.addApp.AddAppTest.main", Some("testApp")).waitForResult()
//    addAppResult.hasError should equal(false)
//
//    val executeResult = driver.sql("""EXECUTE testApp(rain,bow2) OPTIONS (executor.memory '20G')""").waitForResult()
//
//    executeResult.hasError should equal(false)
//    executeResult.resultSet.length should equal(1)
//    executeResult.resultSet(0).get(0) should equal("Spark app launched")
//
//    driver.stop()
//  }


  //TODO Uncomment when CD be ready
//  it should "be able to execute ADD APP Command of an existent file with options with a sql without alias" in {
//    assumeCrossdataUpAndRunning
//
//    val filePath = getClass.getResource("/TestAddApp.jar").getPath
//    val driver = Driver.getOrCreate()
//    val addAppResult = driver.sql("""add app '/home/jjlopez/Stratio/workspaceCrossdata/Crossdata/testsIT/src/test/resources/TestAddApp.jar' with com.stratio.addApp.AddAppTest.main""").waitForResult()
//    addAppResult.hasError should equal(false)
//
//    val executeResult = driver.sql("""EXECUTE testApp(rain,bow2) OPTIONS (executor.memory '20G')""").waitForResult()
//
//    executeResult.hasError should equal(false)
//    executeResult.resultSet.length should equal(1)
//    executeResult.resultSet(0).get(0) should equal("Spark app launched")
//
//    driver.stop()
//  }

  //TODO Uncomment when CD be ready
//  it should "be able to execute ADD APP Command of an existent file with options with a sql" in {
//    assumeCrossdataUpAndRunning
//
//    val filePath = getClass.getResource("/TestAddApp.jar").getPath
//    val driver = Driver.getOrCreate()
//    val addAppResult = driver.sql("""add app '/home/jjlopez/Stratio/workspaceCrossdata/Crossdata/testsIT/src/test/resources/TestAddApp.jar' as testAppSQL with com.stratio.addApp.AddAppTest.main""").waitForResult()
//    addAppResult.hasError should equal(false)
//
//    val executeResult = driver.sql("""EXECUTE testApp(rain,bow2) OPTIONS (executor.memory '20G')""").waitForResult()
//
//    executeResult.hasError should equal(false)
//    executeResult.resultSet.length should equal(1)
//    executeResult.resultSet(0).get(0) should equal("Spark app launched")
//
//    driver.stop()
//  }
}