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

import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.crossdata.CrossdataTable
import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DropTableIT extends SharedXDContextTest {

  private val TableName = "tableId"
  private val DatabaseName = "dbId"
  private val DatasourceName = "json"
  private val Schema = StructType(Seq(StructField("col", StringType)))

  "DropTable command" should "remove a table from Crossdata catalog" in {

    _xdContext.catalog.persistTable(CrossdataTable(TableName, None, Some(Schema), DatasourceName, opts = Map("path" -> "fakepath")))
    _xdContext.catalog.tableExists(TableIdentifier(TableName).toSeq) shouldBe true
    sql(s"DROP TABLE $TableName")
    _xdContext.catalog.tableExists(TableIdentifier(TableName).toSeq) shouldBe false
  }

  it should "remove a qualified table from Crossdata catalog" in {
    _xdContext.catalog.persistTable(CrossdataTable(TableName, Some(DatabaseName), Some(Schema), DatasourceName, opts = Map("path" -> "fakepath")))
    _xdContext.catalog.tableExists(TableIdentifier(TableName, Some(DatabaseName)).toSeq) shouldBe true
    sql(s"DROP TABLE $DatabaseName.$TableName")
    _xdContext.catalog.tableExists(TableIdentifier(TableName, Some(DatabaseName)).toSeq) shouldBe false
  }
}
