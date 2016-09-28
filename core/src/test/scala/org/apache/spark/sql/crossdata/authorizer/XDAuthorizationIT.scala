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
package org.apache.spark.sql.crossdata.authorizer

import java.util.UUID

import com.stratio.crossdata.test.BaseXDTest
import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.crossdata.config.CoreConfig
import org.apache.spark.sql.crossdata.session.{BasicSessionProvider, XDSessionProvider}
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SaveMode}
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.junit.JUnitRunner
import org.apache.spark.sql.crossdata.authorizer.SecurityManagerTestConstants._
import scala.util.Try

@RunWith(classOf[JUnitRunner])
class XDAuthorizationIT extends BaseXDTest with BeforeAndAfterAll {

  var _sparkContext: SparkContext = _

  val simplePersistentTable = "simpletable"

  "A SMAllowingAnyResource" should "authorize any plan requiring authorization" in {

    val sessionWithSMallowingAnyResource = createNewBasicProvider(classOf[SMAllowingAnyResource].getName).newSession(UUID.randomUUID(), XDUser).get
    val df: DataFrame = sessionWithSMallowingAnyResource.createDataFrame(sessionWithSMallowingAnyResource.sparkContext.parallelize((1 to 5).map(i => Row(s"val_$i"))), StructType(Array(StructField("id", StringType))))

    Try (
      df.write.format("json").mode(SaveMode.Overwrite).option("path", s"/tmp/$simplePersistentTable").saveAsTable(simplePersistentTable)
    ).isSuccess shouldBe true

    Try (
    sessionWithSMallowingAnyResource.sql(s"SELECT * FROM $simplePersistentTable").collect()
    ).isSuccess shouldBe true
  }


  "A SMDenyingAnyResource" should "no authorize plans requiring authorization" in {

    val sessionWithSDenyingAnyResource = createNewBasicProvider(classOf[SMDenyingAnyResource].getName).newSession(UUID.randomUUID(), XDUser).get
    val df: DataFrame = sessionWithSDenyingAnyResource.createDataFrame(sessionWithSDenyingAnyResource.sparkContext.parallelize((1 to 5).map(i => Row(s"val_$i"))), StructType(Array(StructField("id", StringType))))


    the [Exception] thrownBy {
      df.write.format("json").mode(SaveMode.Overwrite).option("path", s"/tmp/$simplePersistentTable").saveAsTable(simplePersistentTable)
    } should have message "Operation not authorized"

    the [Exception] thrownBy {
      sessionWithSDenyingAnyResource.sql(s"SELECT * FROM $simplePersistentTable").collect()
    } should have message "Operation not authorized"

  }

  "A SMAllowingWriteCatalog" should "accept catalog writes and reject select statements" in {

    val sessionWithSMallowingWriteCatalog = createNewBasicProvider(classOf[SMAllowingWriteCatalog].getName).newSession(UUID.randomUUID(), XDUser).get
    val df: DataFrame = sessionWithSMallowingWriteCatalog.createDataFrame(sessionWithSMallowingWriteCatalog.sparkContext.parallelize((1 to 5).map(i => Row(s"val_$i"))), StructType(Array(StructField("id", StringType))))

    Try {
      df.write.format("json").mode(SaveMode.Overwrite).option("path", s"/tmp/$simplePersistentTable").saveAsTable(simplePersistentTable)
    }.isSuccess shouldBe true


    the[Exception] thrownBy {
      sessionWithSMallowingWriteCatalog.sql(s"SELECT * FROM $simplePersistentTable").collect()
    } should have message "Operation not authorized"

  }
  // TODO test user, instances and catalog



  // TODO authorize temporary plans, logicalRDD, etc... tables (with db, caseSensitive...), etc...
  /*  it must "return a XDDataFrame when executing a SQL query" in {

      val xdSession = createNewDefaultSession

      val df: DataFrame = xdSession.createDataFrame(xdSession.sparkContext.parallelize((1 to 5).map(i => Row(s"val_$i"))), StructType(Array(StructField("id", StringType))))
      df.registerTempTable("records")

      val dataframe = xdSession.sql("SELECT * FROM records")
      dataframe shouldBe a[XDDataFrame]
    }


    it must "plan a PersistDataSource when creating a table " in {

      val xdSession = createNewDefaultSession

      val dataframe = xdSession.sql(s"CREATE TABLE jsonTable USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/core-reference.conf").toURI()).toString}')")
      val sparkPlan = dataframe.queryExecution.sparkPlan
      xdSession.catalog.dropTable(TableIdentifier("jsonTable", None))
      sparkPlan should matchPattern { case ExecutedCommand(_: PersistDataSourceTable) => }

    }


    */


  override protected def beforeAll(): Unit = {
    _sparkContext = new SparkContext(
      "local[2]",
      "test-xdsession",
      new SparkConf().set("spark.cores.max", "2").set("spark.sql.testkey", "true").set("spark.sql.shuffle.partitions", "3")
    )
  }

  override protected def afterAll(): Unit = {
    _sparkContext.stop()
  }

  private def createNewBasicProvider(securityManagerClass: String = CoreConfig.DefaultSecurityManager): XDSessionProvider = {
    val securityManagerConfigString = s"${CoreConfig.SecurityClassConfigKey}=$securityManagerClass"
    new BasicSessionProvider(_sparkContext, ConfigFactory.parseString(securityManagerConfigString))
  }

}
