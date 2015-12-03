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
package org.apache.spark.sql.crossdata.execution.udaf

import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner


case class Student(name: String, age: Int)

@RunWith(classOf[JUnitRunner])
class UdafsIT extends SharedXDContextTest {

  private val TableName = "tableId"
  private val DatabaseName = "dbId"
  private val DatasourceName = "json"
  private val Schema = StructType(Seq(StructField("col", StringType)))

  "XDContext" should "resolve a query with the UDAF group_concat" in {
    val tempContext = _xdContext
    import tempContext.implicits._

    val df = _xdContext.sc.parallelize(
      List(Student("Torcuato", 27), Student("Rosalinda", 34), Student("Arthur", 41))).toDF

    df.registerTempTable("Udafs_test_gc")

    val result = sql(s"SELECT group_concat(name) FROM Udafs_test_gc")

    result.first().getAs[String](0) shouldBe "Torcuato, Rosalinda, Arthur"

  }

}
