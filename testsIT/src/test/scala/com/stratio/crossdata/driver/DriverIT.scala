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

import java.nio.file.Paths

import com.stratio.crossdata.common.result.{ErrorSQLResult, SuccessfulSQLResult}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

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
    var driver = Driver.getOrCreate()

    driver.sql( s"SHOW TABLES")

    driver.stop()

    Thread.sleep(6000)

    driver = Driver.getOrCreate()

    val result = driver.sql(s"SHOW TABLES")

    result.hasError should equal (false)

  }


  "Crossdata Driver" should "be able to execute ADD JAR Command of an existent file" in {
    val file=File("/tmp/jar").createFile(false)
    val driver = Driver.getOrCreate()
    val result = driver.addJar(s"/tmp/jar").waitForResult()

    driver.stop
    file.delete

    result.hasError should equal (false)
  }

  "Crossdata Driver" should "be return an Error when execute ADD JAR Command of an un-existent file" in {

    val driver = Driver.getOrCreate()
    val result = driver.addJar(s"/tmp/jarnotexists").waitForResult()
    driver.stop

    result.hasError should equal (true)
  }

  "Crossdata Driver" should "be able to execute ADD JAR Command of any HDFS file" in {

    val driver = Driver.getOrCreate()
    val result = driver.addJar(s"hdfs://repo/file.jar").waitForResult()
    driver.stop

    result.hasError should equal (false)
  }

}