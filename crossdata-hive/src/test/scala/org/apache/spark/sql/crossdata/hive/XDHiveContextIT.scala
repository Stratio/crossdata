/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.spark.sql.crossdata.hive

import org.apache.spark.sql.crossdata.XDDataFrame
import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class XDHiveContextIT extends SharedXDContextTest {

  "A XDContext" should "perform a collect with a collection" in {

    val df: DataFrame = xdContext.createDataFrame(ctx.sparkContext.parallelize((1 to 5).map(i => Row(s"val_$i"))), StructType(Array(StructField("id", StringType))))
    df.registerTempTable("records")

    val result: Array[Row] = sql("SELECT * FROM records").collect()

    result should have length 5
  }


  it must "return a XDDataFrame when executing a SQL query" in {

    val df: DataFrame = xdContext.createDataFrame(ctx.sparkContext.parallelize((1 to 5).map(i => Row(s"val_$i"))), StructType(Array(StructField("id", StringType))))
    df.registerTempTable("records")

    val dataframe = sql("SELECT * FROM records")
    dataframe shouldBe a[XDDataFrame]
  }


}

