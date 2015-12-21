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

import org.apache.spark.sql.Row
import org.apache.spark.sql.crossdata.test.CoreWithSharedContext
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class UdafsIT extends CoreWithSharedContext {

  private val TableName = "tableId"
  private val DatabaseName = "dbId"
  private val DatasourceName = "json"
  private val Schema = StructType(Seq(StructField("col", StringType)))

  "XDContext" should "resolve a query with the UDAF group_concat" in {

    val schema = StructType(Seq(StructField("name", StringType), StructField("age", IntegerType)))
    val df = _xdContext.createDataFrame(_xdContext.sc.parallelize(Seq(Row("Torcuato", 27), Row("Rosalinda", 34), Row("Arthur", 41))), schema)

    df.registerTempTable("udafs_test_gc")

    val result = sql(s"SELECT group_concat(name) FROM udafs_test_gc")

    result.first().getAs[String](0) shouldBe "Torcuato, Rosalinda, Arthur"

  }

}
