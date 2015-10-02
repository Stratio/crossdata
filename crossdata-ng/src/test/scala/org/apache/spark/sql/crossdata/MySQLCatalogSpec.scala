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

package org.apache.spark.sql.crossdata

import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.apache.spark.sql.types._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class MySQLCatalogSpec extends SharedXDContextTest {

  "it" must "return a dataframe from a persist table" in {
    val field1 = StructField("column1", StringType, true)
    val field2 = StructField("column2", StringType, true)
    val fields = Seq[StructField](field1, field2)
    val columns = StructType(fields)
    val opts = Map("path"->"/home/ccaballero")
    val tableName = "tableName"
    val catalogName = "catalogName"
    val tableIdentifier = Seq(catalogName, tableName)
    // TODO Check datasource type (parquet is HadoopFsRelationProvider)
    //val crossdataTable = CrossdataTable(tableName, Option(catalogName), Option(columns), "org.apache.spark.sql.parquet","1.0", opts)

    val crossdataTable = CrossdataTable(tableName, Option(catalogName),  Option(columns), "org.apache.spark.sql.json", Array[String](), "1.0", opts)

    xdContext.catalog.persistTable(tableIdentifier, crossdataTable)

    val dataframe = xdContext.sql("SELECT * FROM catalogName.tableName")
    // TODO Separate in multiple test. Check is dataframe is empty. Prepare a test environment (insert data)
    // TODO One test for each case (table not persisted in ms but as temporary, persist uncorrect data.....)
    // TODO test with multiple datasources
    dataframe shouldBe a[XDDataFrame]

  }
}