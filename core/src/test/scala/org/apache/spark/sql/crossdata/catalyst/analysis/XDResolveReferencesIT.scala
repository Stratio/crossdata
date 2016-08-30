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

  //TABLE test.t1 COLUMNS id value
  //TABLE test.t2 COLUMNS id value
  //TABLE test2.t1 COLUMNS id value

  //TABLE test COLUMNS test.id test.test
  //TABLE test.test COLUMNS col.id col.test
  override def beforeAll(): Unit = {
    super.beforeAll()

    val t1: DataFrame = xdContext.createDataFrame(xdContext.sparkContext.parallelize((1 to 5)
      .map(i => Row(s"val_$i", i))), StructType(Array(StructField("id", StringType), StructField("value", IntegerType))))
    t1.registerTempTable("test.t1")

    val t2: DataFrame = xdContext.createDataFrame(xdContext.sparkContext.parallelize((4 to 8)
      .map(i => Row(s"val_$i", i))), StructType(Array(StructField("id", StringType), StructField("value", IntegerType))))
    t2.registerTempTable("test.t2")
    t1.registerTempTable("test2.t1")


    //columns test.id and test.test
    val rows = xdContext.sparkContext.parallelize(1 to 5).map(i => Row(Row(s"val_$i", i)))
    val strType = StructType(Array(StructField("test", StructType(Array(StructField("id", StringType), StructField("test", IntegerType))))))
    xdContext.createDataFrame(rows, strType).registerTempTable("test")

    val rows2= xdContext.sparkContext.parallelize(4 to 8).map(i => Row(Row(s"val_$i", i)))
    val strType2 = StructType(Array(StructField("col", StructType(Array(StructField("id", StringType), StructField("test", IntegerType))))))
    xdContext.createDataFrame(rows2, strType2).registerTempTable("test.test")


  }

  it must "resolve partially qualified columns" in {
    val rows = xdContext.sql("SELECT t1.id, id FROM test.t1").collect()
    rows(0)(0) shouldBe rows(0)(1)
  }

  it must "resolve fully qualified columns" in {
    val rows = xdContext.sql("SELECT test.t1.id, id FROM test.t1").collect()
    rows(0)(0) shouldBe rows(0)(1)
  }

  it must "fail when using non-existing qualifiers in the first part of the identifier" in {
    an [Exception] shouldBe thrownBy (xdContext.sql("SELECT fake.test.t1.id FROM test.t1").show)
  }

  it must "fail when querying ambiguous columns" in {
    an [Exception] shouldBe thrownBy (xdContext.sql("SELECT id FROM test.t1 INNER JOIN test.t2").show)
  }

  it must "keep supporting qualified table aliases" in {
    val rows = xdContext.sql("SELECT t1.id, als.id FROM test.t1 INNER JOIN test.t1 als").collect()
    rows(0)(0) shouldBe rows(0)(1)
  }

  it must "fail when using fully qualified columns after aliasing the table" in {
    an [Exception] shouldBe thrownBy (xdContext.sql("SELECT t1.id, test.t2.id FROM test.t1 INNER JOIN test.t2 als").show)
    an [Exception] shouldBe thrownBy (xdContext.sql("SELECT * FROM test.t1 INNER JOIN test.t2 otra ON t1.id = t2.id").show)
  }

  it must "resolve qualified columns when joining tables" in {
    val rows = xdContext.sql("SELECT t1.id, test.t1.id, t2.id, test.t2.id FROM test.t1 INNER JOIN test.t2").collect()
    rows(0)(0) shouldBe rows(0)(1)
    rows(0)(2) shouldBe rows(0)(3)
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

  //TABLE test COLUMNS test.id test.test
  //TABLE test.test COLUMNS col.id col.test
  it must "planp" in {
    xdContext.sql("SELECT * FROM test.test").show
    xdContext.sql("SELECT col FROM test.test").show
    xdContext.sql("SELECT col.id FROM test.test").show
    xdContext.sql("SELECT col.test FROM test.test").show
    xdContext.sql("SELECT test.col.test FROM test.test").show
    xdContext.sql("SELECT test.test.col.test FROM test.test").show
  }

  it must "planq" in {
    xdContext.sql("SELECT * FROM test").show
    xdContext.sql("SELECT test FROM test").show
    xdContext.sql("SELECT test.test FROM test").show
    xdContext.sql("SELECT test.test.test FROM test").show

  }

  it must "planr" in {
    xdContext.sql("SELECT test FROM test WHERE test.test.test = 4").show
     // TODO length 1

  }

  it must "plans" in {
    xdContext.sql("SELECT * FROM test INNER JOIN test.test").show

    xdContext.sql("SELECT col FROM test INNER JOIN test.test").show
    xdContext.sql("SELECT col.test FROM test INNER JOIN test.test").show
    xdContext.sql("SELECT test FROM test INNER JOIN test.test").show
    xdContext.sql("SELECT test.test FROM test INNER JOIN test.test").show
    xdContext.sql("SELECT test.test.test FROM test INNER JOIN test.test").show
  }

  it must "plant" in {
    xdContext.sql("SELECT * FROM test INNER JOIN test.test ON test.test.test = col.test").show
  }

}
