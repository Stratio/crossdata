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
package org.apache.spark.sql.crossdata.execution.datasources

import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.crossdata.catalog.XDCatalog.CrossdataTable
import org.apache.spark.sql.crossdata.test.{SharedXDContextTest, TestXDContext}
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class InsertTableIT extends SharedXDContextTest {

  private val jsonPath = "src/test/resources/foo.json"

  protected override def beforeAll(): Unit = {

    // Ensure we have initialized the context before calling parent code
    super.beforeAll()
  }


  /*it should "insert a row using INSERT INTO table VALUES in CSV" in {
    val foo = _xdContext
      .read
      .format("com.databricks.spark.csv")
      .option("header","true")
      .option("inferSchema","true")
      .load(csvPath)
    foo.printSchema()
  }*/

  it should "insert a row using INSERT INTO table VALUES in JSON" in {
    val foo = _xdContext.read.json(jsonPath)
    foo.registerTempTable("fooJson")
    _xdContext.sql("INSERT INTO fooJson VALUES ('foo2',20)") should be (Row(1)::Nil)
  }

}
