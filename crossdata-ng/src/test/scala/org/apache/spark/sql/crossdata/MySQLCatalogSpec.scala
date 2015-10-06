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
class MySQLCatalogSpec extends SharedXDContextTest with MySQLCatalogConstants{

  "it" must "return a dataframe from a persist table without catalog using json datasource" in {
    val fields = Seq[StructField](Field1, Field2)
    val columns = StructType(fields)
    val opts = Map("path"->"/fake_path")
    val tableIdentifier = Seq(TableName)
    // TODO Check datasource type (parquet is HadoopFsRelationProvider)
    val crossdataTable = CrossdataTable(TableName, None,  Option(columns), "org.apache.spark.sql.json", Array[String](), "1.0", opts)
    xdContext.catalog.persistTable(tableIdentifier, crossdataTable)
    val dataframe = xdContext.sql(s"SELECT * FROM $TableName")
    // TODO One test for each case (table not persisted in ms but as temporary, persist uncorrect data.....)
    // TODO test with multiple datasources
    dataframe shouldBe a[XDDataFrame]
  }

  "it" should "persist a table with catalog and partitionColumns in MySQL" in {
    
    val tableIdentifier = Seq(Database, TableName)
    val crossdataTable = CrossdataTable(TableName, Option(Database),  Option(Columns), "org.apache.spark.sql.json", Array[String](Field1Name), "1.0", OptsJSON)
    xdContext.catalog.persistTable(tableIdentifier, crossdataTable)
    xdContext.catalog.tableExists(tableIdentifier) shouldBe true

    val df = xdContext.sql(s"SELECT * FROM $Database.$TableName")
    df shouldBe a [XDDataFrame]

  }

  "it" should "returns list of tables" in{
    xdContext.catalog.dropAllTables()
    val crossdataTable1 = CrossdataTable(TableName, Option(Database),  Option(Columns), "org.apache.spark.sql.json", Array[String](Field1Name), "1.0", OptsJSON)
    val crossdataTable2 = CrossdataTable(TableName, None,  Option(Columns), "org.apache.spark.sql.json", Array[String](Field1Name), "1.0", OptsJSON)
    val tableIdentifier1 = Seq(Database, TableName)
    val tableIdentifier2 = Seq(TableName)
    xdContext.catalog.persistTable(tableIdentifier1, crossdataTable1)
    xdContext.catalog.persistTable(tableIdentifier2, crossdataTable2)

    val dfDatabase = xdContext.sql("SHOW TABLES IN database")
    dfDatabase.count() shouldBe(1)
    val df = xdContext.sql("SHOW TABLES")
    df.count() shouldBe(2)
  }

  override protected def afterAll(){
    xdContext.catalog.dropAllTables()
    super.afterAll()
  }

}

sealed trait MySQLCatalogConstants{
  val Database = "database"
  val TableName = "tableName"
  val AnotherTable = "anotherTable"
  val Field1Name = "column1"
  val Field2Name = "column2"
  val Field1 = StructField(Field1Name, StringType, true)
  val Field2 = StructField(Field2Name, StringType, true)
  val SourceProvider = "com.stratio.crossdata.sql.sources.mongodb"
  val Fields = Seq[StructField](Field1, Field2)
  val Columns = StructType(Fields)
  val OptsJSON = Map("path"->"/fake_path")
}