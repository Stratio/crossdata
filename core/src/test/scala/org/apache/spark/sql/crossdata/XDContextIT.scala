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
package org.apache.spark.sql.crossdata

import org.apache.spark.SparkFiles
import org.apache.spark.sql.crossdata.execution.PersistDataSourceTable
import org.apache.spark.sql.crossdata.test.CoreWithSharedContext
import org.apache.spark.sql.execution.ExecutedCommand
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class XDContextIT extends CoreWithSharedContext {


  "A DefaultCatalog" should "be case sensitive" in {
    val xdCatalog = xdContext.catalog
    assert(xdCatalog.conf.caseSensitiveAnalysis === true)
  }

  "A XDContext" should "perform a collect with a collection" in {

    val df: DataFrame = xdContext.createDataFrame(xdContext.sparkContext.parallelize((1 to 5).map(i => Row(s"val_$i"))), StructType(Array(StructField("id", StringType))))
    df.registerTempTable("records")

    val result: Array[Row] = sql("SELECT * FROM records").collect()

    result should have length 5
  }

  it must "return a XDDataFrame when executing a SQL query" in {

    val df: DataFrame = xdContext.createDataFrame(xdContext.sparkContext.parallelize((1 to 5).map(i => Row(s"val_$i"))), StructType(Array(StructField("id", StringType))))
    df.registerTempTable("records")

    val dataframe = sql("SELECT * FROM records")
    dataframe shouldBe a[XDDataFrame]
  }


  it must "plan a PersistDataSource when creating a table " in {

    val test = _xdContext.sc.parallelize(Seq(1,2,3,4)).map { x =>
      SparkFiles.get("catalog-reference.conf")
    }

    val v = test.foreach(x => println("====================>" + x))

    val dataframe = sql(s"CREATE TABLE jsonTable USING org.apache.spark.sql.json OPTIONS (path '${SparkFiles.get("catalog-reference.conf")}')")
    val sparkPlan = dataframe.queryExecution.sparkPlan
    xdContext.catalog.dropTable(Seq("","jsonTable"))
    sparkPlan should matchPattern { case ExecutedCommand(_: PersistDataSourceTable) => }

  }

}
