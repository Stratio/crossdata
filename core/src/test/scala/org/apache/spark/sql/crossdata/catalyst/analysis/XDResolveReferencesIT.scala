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
package org.apache.spark.sql.crossdata.catalyst.analysis

import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class XDResolveReferencesIT extends SharedXDContextTest{

  override def beforeAll(): Unit = {
    super.beforeAll()

    val t1: DataFrame = xdContext.createDataFrame(xdContext.sparkContext.parallelize((1 to 5)
      .map(i => Row(s"val_$i", i))), StructType(Array(StructField("id", StringType), StructField("value", IntegerType))))
    t1.registerTempTable("test.t1")

    val t2: DataFrame = xdContext.createDataFrame(xdContext.sparkContext.parallelize((4 to 8)
      .map(i => Row(s"val_$i", i))), StructType(Array(StructField("id", StringType), StructField("value", IntegerType))))
    t2.registerTempTable("test.t2")

    t1.registerTempTable("test2.t1")
  }

  it must "plan a query" in {

    val dataFrame = xdContext.sql("SELECT t1.id, test.t1.id, id FROM test.t1")
    dataFrame.show
  }

  it must "plana" in {

    val dataFrame = xdContext.sql("SELECT t1.id, test.t1.id, id FROM test.t1")
    dataFrame.show
  }


  it must "planb" in {
    an [Exception] shouldBe thrownBy (xdContext.sql("SELECT otro.test.t1.id FROM test.t1").show)
  }

  it must "pla be ambiguous" in {

    an [Exception] shouldBe thrownBy (xdContext.sql("SELECT  id FROM test.t1 INNER JOIN test.t2").show)
  }

  it must "planc" in {

    an [Exception] shouldBe thrownBy (xdContext.sql("SELECT t1.id, otra.t2 FROM test.t1 INNER JOIN test.t2 otra").show)
  }

  it must "pland" in {
    //once there is an alias, KO qualif??
    an [Exception] shouldBe thrownBy (xdContext.sql("SELECT * FROM test.t1 INNER JOIN test.t2 otra ON t1.id = t2.id").show)
  }

  it must "plane" in {

    val dataFrame = xdContext.sql("SELECT t1.id FROM test.t1 INNER JOIN test.t2")
    dataFrame.show
  }

  it must "planf" in {

    val dataFrame = xdContext.sql("SELECT t1.id, otra.id FROM test.t1 INNER JOIN test.t2 otra")
    dataFrame.show
  }

  it must "plang" in {

    val dataFrame = xdContext.sql("SELECT t1.id, t2.id, test.t1.id, test.t2.id FROM test.t1 INNER JOIN test.t2")
    dataFrame.show
  }

  it must "planh" in {

    val dataFrame = xdContext.sql("SELECT * FROM test.t1 INNER JOIN test.t2 ON t1.id = t2.id")
    dataFrame.show
  }

  it must "plani" in {

    val dataFrame = xdContext.sql("SELECT * FROM test.t1 INNER JOIN test.t2 ON test.t1.id = test.t2.id")
    dataFrame.show
  }

  it must "planj" in {

    val dataFrame = xdContext.sql("SELECT test.t1.id, t2.id FROM test.t1 INNER JOIN test.t2 ON t1.id = test.t2.id")
    dataFrame.show
  }

  it must "plank" in {

    val dataFrame = xdContext.sql("SELECT test.t1.id FROM test.t1 INNER JOIN test.t2 ON t1.id = test.t2.id GROUP BY t1.id")
    dataFrame.show
  }

  it must "planl" in {

    val dataFrame = xdContext.sql("SELECT test.t1.id FROM test.t1 INNER JOIN test.t2 ON t1.id = test.t2.id GROUP BY test.t1.id")
    dataFrame.show
  }

  it must "planm" in {
    // ambiguous
    an [Exception] shouldBe thrownBy (xdContext.sql("SELECT * FROM test.t1 INNER JOIN test2.t1 ON t1.id = t1.id").show)
  }

  it must "plano" in {
    val dataFrame = xdContext.sql("SELECT * FROM test.t1 INNER JOIN test2.t1 ON test.t1.id = test2.t1.id")
    dataFrame.show
  }


}
