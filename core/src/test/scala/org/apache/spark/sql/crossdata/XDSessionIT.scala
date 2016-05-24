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

import java.nio.file.Paths
import java.util.UUID

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.crossdata.execution.PersistDataSourceTable
import org.apache.spark.sql.crossdata.test.{SharedXDContextTest, TestXDContext, XDTestUtils}
import org.apache.spark.sql.execution.ExecutedCommand
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class XDSessionIT extends XDTestUtils {

  val sparkCtx = new SparkContext(
    "local[2]",
    "test-xd-context",
    new SparkConf().set("spark.cores.max", "2").set("spark.sql.testkey", "true").set("spark.sql.shuffle.partitions", "3")
  )

  override protected def _xdContext: XDContext = xdSession

  val xdSession: XDContext = new XDSession(sparkCtx, UUID.randomUUID(), None)


  "A DefaultCatalog" should "be case sensitive" in {
    val xdCatalog = xdSession.catalog
    assert(xdCatalog.conf.caseSensitiveAnalysis === true)
  }

  "A xdSession" should "perform a collect with a collection" in {

    val df: DataFrame = xdSession.createDataFrame(xdSession.sparkContext.parallelize((1 to 5).map(i => Row(s"val_$i"))), StructType(Array(StructField("id", StringType))))
    df.registerTempTable("records")

    val result: Array[Row] = xdSession.sql("SELECT * FROM records").collect()

    result should have length 5
  }

  it must "return a XDDataFrame when executing a SQL query" in {

    val df: DataFrame = xdSession.createDataFrame(xdSession.sparkContext.parallelize((1 to 5).map(i => Row(s"val_$i"))), StructType(Array(StructField("id", StringType))))
    df.registerTempTable("records")

    val dataframe = xdSession.sql("SELECT * FROM records")
    dataframe shouldBe a[XDDataFrame]
  }


  it must "plan a PersistDataSource when creating a table " in {
    val dataframe = xdSession.sql(s"CREATE TABLE jsonTable USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/core-reference.conf").toURI()).toString}')")
    val sparkPlan = dataframe.queryExecution.sparkPlan
    xdSession.catalog.dropTable(TableIdentifier("jsonTable", None))
    sparkPlan should matchPattern { case ExecutedCommand(_: PersistDataSourceTable) => }

  }

  it must "plan a query with conflicted column names between two tables resolving by alias preference" in {

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

    val t1: DataFrame = xdSession.createDataFrame(xdSession.sparkContext.parallelize((1 to 5)
      .map(i => Row(s"val_$i", i))), StructType(Array(StructField("id", StringType), StructField("value", IntegerType))))
    t1.registerTempTable("t3")

    val dataFrame = xdSession.sql("SELECT id as id, value as product FROM t3 GROUP BY id, product")

    dataFrame.collect should have length 5

  }
}
