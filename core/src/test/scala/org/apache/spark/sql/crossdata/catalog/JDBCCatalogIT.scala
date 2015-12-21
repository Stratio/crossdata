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
package org.apache.spark.sql.crossdata.catalog

import org.apache.spark.sql.crossdata._
import org.apache.spark.sql.crossdata.test.CoreWithSharedContext
import org.apache.spark.sql.execution.datasources.LogicalRelation
import org.apache.spark.sql.types._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class JDBCCatalogIT extends CoreWithSharedContext with JDBCCatalogConstants {

  "JdbcCatalogSpec" must "return a dataframe from a persist table without catalog using json datasource" in {
    val fields = Seq[StructField](Field1, Field2)
    val columns = StructType(fields)
    val opts = Map("path" -> "/fake_path")
    val tableIdentifier = Seq(TableName)
    val crossdataTable = CrossdataTable(TableName, None, Some(Columns), SourceDatasource, Array[String](), opts)
    xdContext.catalog.persistTableMetadata(crossdataTable)
    val dataframe = xdContext.sql(s"SELECT * FROM $TableName")

    dataframe shouldBe a[XDDataFrame]
  }

  it should "persist a table with catalog and partitionColumns in Jdbc" in {

    val tableIdentifier = Seq(Database, TableName)
    val crossdataTable = CrossdataTable(TableName, Some(Database), Some(Columns), SourceDatasource, Array[String](Field1Name), OptsJSON)
    xdContext.catalog.persistTableMetadata(crossdataTable)
    xdContext.catalog.tableExists(tableIdentifier) shouldBe true

    val df = xdContext.sql(s"SELECT * FROM $Database.$TableName")
    df shouldBe a[XDDataFrame]

  }

  it should "persist a table with catalog and partitionColumns with multiple subdocuments as schema in JDBC" in {
    xdContext.catalog.dropAllTables()
    val tableIdentifier = Seq(Database, TableName)
    val crossdataTable = CrossdataTable(TableName, Some(Database), Some(ColumnsWithSubColumns), SourceDatasource, Array.empty[String], OptsJSON)
    xdContext.catalog.persistTableMetadata(crossdataTable)

    xdContext.catalog.unregisterTable(tableIdentifier)
    xdContext.catalog.tableExists(tableIdentifier) shouldBe true
    val df = xdContext.sql(s"SELECT $FieldWithSubcolumnsName FROM $Database.$TableName")

    df shouldBe a[XDDataFrame]
    df.schema.apply(0).dataType.asInstanceOf[StructType].size shouldBe (2)
  }

  it should "persist a table with catalog and partitionColumns with arrays as schema in JDBC" in {
    xdContext.catalog.dropAllTables()
    val tableIdentifier = Seq(Database, TableName)
    val crossdataTable = CrossdataTable(TableName, Some(Database), Some(ColumnsWithArrayString), SourceDatasource, Array.empty[String], OptsJSON)
    xdContext.catalog.persistTableMetadata(crossdataTable)

    xdContext.catalog.unregisterTable(tableIdentifier)
    val df = xdContext.sql(s"SELECT $SubField2Name FROM $Database.$TableName")
    df shouldBe a[XDDataFrame]
    df.schema.apply(0).dataType shouldBe (ArrayType(StringType))
  }

  it should "persist a table with catalog and partitionColumns with array of integers as schema in JDBC" in {
    xdContext.catalog.dropAllTables()
    val tableIdentifier = Seq(Database, TableName)
    val crossdataTable = CrossdataTable(TableName, Some(Database), Some(ColumnsWithArrayInteger), SourceDatasource, Array.empty[String], OptsJSON)
    xdContext.catalog.persistTableMetadata(crossdataTable)

    xdContext.catalog.unregisterTable(tableIdentifier)
    xdContext.sql(s"DESCRIBE $Database.$TableName")
    val df = xdContext.sql(s"SELECT $SubField2Name FROM $Database.$TableName")
    df shouldBe a[XDDataFrame]
    df.schema.apply(0).dataType shouldBe (ArrayType(IntegerType))
  }

  it should "persist a table with catalog and partitionColumns with arrays with subdocuments and strange characters in Field names as schema in JDBC" in {
    xdContext.catalog.dropAllTables()
    val tableIdentifier = Seq(Database, TableName)
    val crossdataTable = CrossdataTable(TableName, Some(Database), Some(ColumnsWithArrayWithSubdocuments), SourceDatasource, Array.empty[String], OptsJSON)
    xdContext.catalog.persistTableMetadata(crossdataTable)

    xdContext.catalog.unregisterTable(tableIdentifier)
    val schemaDF = xdContext.sql(s"DESCRIBE $Database.$TableName")
    schemaDF.count() should be (3)
    val df = xdContext.sql(s"SELECT `$FieldWitStrangeChars` FROM $Database.$TableName")
    df shouldBe a[XDDataFrame]
    df.schema.apply(0).dataType shouldBe (ArrayType(StructType(Seq(Field1, Field2))))
  }

  it should "persist a table with catalog and partitionColumns with map with arrays with subdocuments and strange characters in Field names as schema in JDBC" in {
    xdContext.catalog.dropAllTables()
    val tableIdentifier = Seq(Database, TableName)
    val crossdataTable = CrossdataTable(TableName, Some(Database), Some(ColumnsWithMapWithArrayWithSubdocuments), SourceDatasource, Array.empty[String], OptsJSON)
    xdContext.catalog.persistTableMetadata(crossdataTable)
    xdContext.catalog.unregisterTable(tableIdentifier)
    val schemaDF = xdContext.sql(s"DESCRIBE $Database.$TableName")
    schemaDF.count() should be (1)
    val df = xdContext.sql(s"SELECT `$Field1Name` FROM $Database.$TableName")
    df shouldBe a[XDDataFrame]
    df.schema.apply(0).dataType shouldBe (MapType(ColumnsWithSubColumns, ColumnsWithArrayWithSubdocuments))
  }

  it should "returns list of tables" in {
    xdContext.catalog.dropAllTables()
    val crossdataTable1 = CrossdataTable(TableName, Some(Database), Some(Columns), SourceDatasource, Array[String](Field1Name), OptsJSON)
    val crossdataTable2 = CrossdataTable(TableName, None, Some(Columns), SourceDatasource, Array[String](Field1Name), OptsJSON)

    xdContext.catalog.persistTableMetadata(crossdataTable1)
    xdContext.catalog.persistTableMetadata(crossdataTable2)

    val tables = xdContext.catalog.getTables(Some(Database))
    tables.size shouldBe (1)
    val tables2 = xdContext.catalog.getTables(None)
    tables2.size shouldBe (2)
  }

  it should "drop tables" in {
    xdContext.catalog.dropAllTables()

    val crossdataTable1 = CrossdataTable(TableName, Some(Database), Some(Columns), SourceDatasource, Array[String](Field1Name), OptsJSON)
    val crossdataTable2 = CrossdataTable(TableName, None, Some(Columns), SourceDatasource, Array[String](Field1Name), OptsJSON)
    val tableIdentifier1 = Seq(Database, TableName)
    val tableIdentifier2 = Seq(TableName)
    xdContext.catalog.persistTableMetadata(crossdataTable1)
    xdContext.catalog.persistTableMetadata(crossdataTable2)

    val tables = xdContext.catalog.getTables(None)
    tables.size shouldBe 2
    xdContext.catalog.dropTable(tableIdentifier2)
    val tables2 = xdContext.catalog.getTables(None)
    tables2.size shouldBe 1
    xdContext.catalog.dropTable(tableIdentifier1)
    val tables3 = xdContext.catalog.getTables(None)
    tables3.size shouldBe 0
  }

  it should "check if tables map is correct" in {
    xdContext.catalog.dropAllTables()
    val crossdataTable1 = CrossdataTable(TableName, Some(Database), Some(Columns), SourceDatasource, Array[String](Field1Name), OptsJSON)
    val tableIdentifier2 = Seq(TableName)

    xdContext.catalog.persistTableMetadata(crossdataTable1)

    xdContext.catalog.registerTable(tableIdentifier2, LogicalRelation(new MockBaseRelation))
    xdContext.catalog.unregisterAllTables()
    xdContext.catalog.tables.size shouldBe 0
    val tables = xdContext.catalog.getTables(None)
    tables.size shouldBe 1

  }

  it should "check if persisted tables are marked as not temporary" in {
    xdContext.catalog.dropAllTables()
    val crossdataTable1 = CrossdataTable(TableName, Some(Database), Some(Columns), SourceDatasource, Array[String](Field1Name), OptsJSON)
    val tableIdentifier2 = Seq(TableName)
    xdContext.catalog.persistTableMetadata(crossdataTable1)
    xdContext.catalog.registerTable(tableIdentifier2, LogicalRelation(new MockBaseRelation))
    val tables = xdContext.catalog.getTables(None).toMap
    tables(s"$Database.$TableName") shouldBe false
    tables(TableName) shouldBe true
  }

  it should "describe a table persisted and non persisted with subcolumns" in {
    xdContext.catalog.dropAllTables()
    val crossdataTable = CrossdataTable(TableName, Some(Database), Some(ColumnsWithSubColumns), SourceDatasource, Array.empty[String], OptsJSON)
    xdContext.catalog.persistTableMetadata(crossdataTable)
    xdContext.sql(s"DESCRIBE $Database.$TableName").count() should not be 0
  }

  override protected def afterAll() {
    xdContext.catalog.dropAllTables()
    super.afterAll()
  }

}

sealed trait JDBCCatalogConstants {
  val Database = "database"
  val TableName = "tableName"
  val AnotherTable = "anotherTable"
  val Field1Name = "column1"
  val Field2Name = "column2"
  val FieldWitStrangeChars = "1+x"
  val SubField1Name = "subcolumn1"
  val SubField2Name = "subcolumn2"
  val FieldWithSubcolumnsName = "columnWithSubcolumns"
  val Field1 = StructField(Field1Name, StringType, nullable = true)
  val Field2 = StructField(Field2Name, StringType, nullable = true)
  val SubField = StructField(SubField1Name, StringType, nullable = true)
  val SubField2 = StructField(SubField2Name, StringType, nullable = true)
  val arrayField = StructField(SubField2Name, ArrayType(StringType), nullable = true)
  val arrayFieldIntegers = StructField(SubField2Name, ArrayType(IntegerType), nullable = true)
  val arrayFieldWithSubDocs = StructField(FieldWitStrangeChars, ArrayType(StructType(Seq(Field1, Field2))))
  val SourceDatasource = "org.apache.spark.sql.json"
  val Fields = Seq[StructField](Field1, Field2)
  val SubFields = Seq(SubField, SubField2)
  val Columns = StructType(Fields)
  val ColumnsWithSubColumns = StructType(Seq(StructField(Field1Name, StringType, nullable = true), StructField(FieldWithSubcolumnsName, StructType(SubFields), nullable = true)) )
  val ColumnsWithArrayString = StructType(Seq(StructField(Field1Name, StringType, nullable = true), StructField(FieldWithSubcolumnsName, StructType(SubFields), nullable = true), arrayField) )
  val ColumnsWithArrayInteger = StructType(Seq(StructField(Field1Name, StringType, nullable = true), StructField(FieldWithSubcolumnsName, StructType(SubFields), nullable = true), arrayFieldIntegers) )
  val ColumnsWithArrayWithSubdocuments = StructType(Seq(StructField(Field1Name, StringType, nullable = true), StructField(FieldWithSubcolumnsName, StructType(SubFields), nullable = true), arrayFieldWithSubDocs) )
  val ColumnsWithMapWithArrayWithSubdocuments = StructType(Seq(StructField(Field1Name, MapType(ColumnsWithSubColumns, ColumnsWithArrayWithSubdocuments))))
  val OptsJSON = Map("path" -> "/fake_path")
}