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
package org.apache.spark.sql.crossdata

import java.lang.Boolean
import java.nio.file.Paths

import com.stratio.crossdata.test.BaseXDTest
import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.crossdata.catalog.persistent.DerbyCatalog
import org.apache.spark.sql.crossdata.catalog.temporary.HashmapCatalog
import org.apache.spark.sql.crossdata.catalyst.execution.PersistDataSourceTable
import org.apache.spark.sql.crossdata.session.{XDSessionState, XDSharedState}
import org.apache.spark.sql.execution.ExecutedCommand
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SQLConf, SaveMode}
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.junit.JUnitRunner

import scala.collection.JavaConversions._
import scala.util.Try

@RunWith(classOf[JUnitRunner])
class XDSessionIT extends BaseXDTest with BeforeAndAfterAll {

  var _sparkContext: SparkContext = _

  "XDSession" should "be able to isolate config" in {

    val SparkSQLTungstenProperty = "spark.sql.tungsten.enabled"
    val XDTungstenProperty = "config.spark.sql.tungsten.enabled"

    val xdSession1 = {

      val (coreConfig, sqlConf) = {
        val sessionConfig: Map[String, AnyRef] = Map(XDTungstenProperty -> Boolean.TRUE)
        val coreConfig = ConfigFactory.parseMap(sessionConfig)
        val sqlConf = new SQLConf

        sessionConfig.foreach { case (k, v) => sqlConf.setConfString(k.stripPrefix("config."), v.toString) }
        (coreConfig, sqlConf)
      }

      new XDSession(
        new XDSharedState(_sparkContext,sqlConf, new DerbyCatalog(sqlConf), None, None),
        new XDSessionState(sqlConf, new HashmapCatalog(sqlConf) :: Nil)
      )
    }

    val xdSession2 = createNewDefaultSession

    xdSession1.conf.getConfString(SparkSQLTungstenProperty) shouldBe "true"
    Try(xdSession2.conf.getConfString(SparkSQLTungstenProperty)).toOption shouldBe None

    xdSession1.conf.setConfString(SparkSQLTungstenProperty, "false")
    xdSession2.conf.setConfString(SparkSQLTungstenProperty, "true")

    xdSession1.conf.getConfString(SparkSQLTungstenProperty) shouldBe "false"
    xdSession2.conf.getConfString(SparkSQLTungstenProperty) shouldBe "true"
  }

  "XDSession" should "be able to isolate temporary catalogs and share persistent catalogs" in {

    val tempTableName = "records"
    val persTableName = "recordsPers"

    val xdSession1 = createNewDefaultSession
    val xdSession2 = createNewDefaultSession

    val df: DataFrame = xdSession2.createDataFrame(xdSession2.sparkContext.parallelize((1 to 5).map(i => Row(s"val_$i"))), StructType(Array(StructField("id", StringType))))
    df.registerTempTable(tempTableName)

    xdSession2.table(tempTableName).collect should not be empty
    a [RuntimeException] shouldBe thrownBy{
      xdSession1.table(tempTableName).collect should not be empty
    }

    df.write.format("json").mode(SaveMode.Overwrite).option("path", s"/tmp/$persTableName").saveAsTable(persTableName)

    xdSession2.table(persTableName).collect should not be empty
    xdSession1.table(persTableName).collect should not be empty

    xdSession2.dropAllTables()

  }


  "A XDSession" should "perform a collect with a collection" in {

    val xdSession = createNewDefaultSession

    val df: DataFrame = xdSession.createDataFrame(xdSession.sparkContext.parallelize((1 to 5).map(i => Row(s"val_$i"))), StructType(Array(StructField("id", StringType))))
    df.registerTempTable("records")

    val result: Array[Row] = xdSession.sql("SELECT * FROM records").collect()

    result should have length 5
  }

  it must "return a XDDataFrame when executing a SQL query" in {

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

  it must "plan a query with conflicted column names between two tables resolving by alias preference" in {

    val xdSession = createNewDefaultSession

    val t1: DataFrame = xdSession.createDataFrame(xdSession.sparkContext.parallelize((1 to 5)
      .map(i => Row(s"val_$i", i))), StructType(Array(StructField("id", StringType), StructField("value", IntegerType))))
    t1.registerTempTable("t1")

    val t2: DataFrame = xdSession.createDataFrame(xdSession.sparkContext.parallelize((4 to 8)
      .map(i => Row(s"val_$i", i))), StructType(Array(StructField("name", StringType), StructField("value", IntegerType))))
    t2.registerTempTable("t2")

    val dataFrame = xdSession.sql("SELECT t1.id, t2.name as name, t1.value as total FROM t1 INNER JOIN t2 ON t1.id = t2.name GROUP BY id, name, total")

    dataFrame.show

    dataFrame.collect should have length 2

  }

  it must "plan a query with aliased attributes in the group by clause" in {

    val xdSession = createNewDefaultSession

    val t1: DataFrame = xdSession.createDataFrame(xdSession.sparkContext.parallelize((1 to 5)
      .map(i => Row(s"val_$i", i))), StructType(Array(StructField("id", StringType), StructField("value", IntegerType))))
    t1.registerTempTable("t3")

    val dataFrame = xdSession.sql("SELECT id as id, value as product FROM t3 GROUP BY id, product")

    dataFrame.collect should have length 5

  }

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

  private def createNewDefaultSession: XDSession = {
    val sqlConf = new SQLConf
    new XDSession(
      new XDSharedState(_sparkContext, sqlConf, new DerbyCatalog(sqlConf), None, None),
      new XDSessionState(sqlConf, new HashmapCatalog(sqlConf) :: Nil)
    )
  }

}
