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
package org.apache.spark.sql.crossdata.catalog.persistent

import org.apache.spark.sql.catalyst.{CatalystConf, TableIdentifier}
import org.apache.spark.sql.catalyst.plans.logical.LocalRelation
import org.apache.spark.sql.crossdata._
import org.apache.spark.sql.crossdata.catalog.XDCatalog.{CrossdataApp, CrossdataIndex, CrossdataTable, IndexIdentifier}
import org.apache.spark.sql.crossdata.catalog.temporary.MapCatalog
import org.apache.spark.sql.crossdata.catalog.{CatalogChain, CatalogConstants, XDCatalog}
import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.apache.spark.sql.execution.datasources.LogicalRelation
import org.apache.spark.sql.types._
import org.apache.spark.sql.crossdata.catalog.interfaces.XDCatalogCommon._


trait GenericCatalogTests extends SharedXDContextTest with CatalogConstants {

  def catalogName: String

  implicit lazy val conf: CatalystConf = xdContext.catalog.conf

  implicit def catalogToPersistenceWithCache(catalog: XDCatalog): PersistentCatalogWithCache = {
    catalog.asInstanceOf[CatalogChain].persistentCatalogs.head.asInstanceOf[PersistentCatalogWithCache]
  }

  implicit def catalogToTemporaryCatalog(catalog: XDCatalog): MapCatalog = {
    catalog.asInstanceOf[CatalogChain].temporaryCatalogs.head.asInstanceOf[MapCatalog]
  }

  s"${catalogName}CatalogSpec" must "return a dataframe from a persist table without catalog using json datasource" in {
    val fields = Seq[StructField](Field1, Field2)
    val columns = StructType(fields)
    val opts = Map("path" -> "/fake_path")
    val tableIdentifier = Seq(TableName)
    val crossdataTable = CrossdataTable(TableIdentifier(TableName, None).normalize, Some(Columns), SourceDatasource, Array[String](), opts)
    xdContext.catalog.persistTableMetadata(crossdataTable)
    val dataframe = xdContext.sql(s"SELECT * FROM $TableName")

    dataframe shouldBe a[XDDataFrame]
  }

  it should s"persist a table with catalog and partitionColumns in $catalogName" in {

    val tableIdentifier = TableIdentifier(TableName, Some(Database))
    val crossdataTable = CrossdataTable(TableIdentifier(TableName, Some(Database)).normalize, Some(Columns), SourceDatasource, Array[String](Field1Name), OptsJSON)
    xdContext.catalog.persistTableMetadata(crossdataTable)
    xdContext.catalog.tableExists(tableIdentifier) shouldBe true

    val df = xdContext.sql(s"SELECT * FROM $Database.$TableName")
    df shouldBe a[XDDataFrame]
  }

  it should s"drop view" in {

    val viewIdentifier = TableIdentifier(ViewName, Option(Database))
    val plan = new LocalRelation(Seq.empty)
    xdContext.catalog.persistView(viewIdentifier, plan, sqlView)
    xdContext.catalog.tableExists(viewIdentifier) shouldBe true
    xdContext.catalog.dropView(viewIdentifier)
    xdContext.catalog.tableExists(viewIdentifier) shouldBe false
  }


  it should s"not drop view that not exists " in {
    a[RuntimeException] shouldBe thrownBy {
      val viewIdentifier = TableIdentifier(ViewName, Option(Database))
      xdContext.catalog.dropView(viewIdentifier)
    }

  }

  it should s"persist a table with catalog and partitionColumns with multiple subdocuments as schema in $catalogName" in {
    xdContext.catalog.dropAllTables()
    val tableIdentifier = TableIdentifier(TableName, Some(Database))
    val crossdataTable = CrossdataTable(TableIdentifier(TableName, Some(Database)).normalize, Some(ColumnsWithSubColumns), SourceDatasource, Array.empty[String], OptsJSON)
    xdContext.catalog.persistTableMetadata(crossdataTable)

    xdContext.catalog.unregisterTable(tableIdentifier)
    xdContext.catalog.tableExists(tableIdentifier) shouldBe true
    val df = xdContext.sql(s"SELECT $FieldWithSubcolumnsName FROM $Database.$TableName")

    df shouldBe a[XDDataFrame]
    df.schema.apply(0).dataType.asInstanceOf[StructType].size shouldBe (2)
  }

  it should s"persist a table with catalog and partitionColumns with arrays as schema in $catalogName" in {
    xdContext.catalog.dropAllTables()
    val tableIdentifier = TableIdentifier(TableName, Some(Database))
    val crossdataTable = CrossdataTable(TableIdentifier(TableName, Some(Database)).normalize, Some(ColumnsWithArrayString), SourceDatasource, Array.empty[String], OptsJSON)
    xdContext.catalog.persistTableMetadata(crossdataTable)

    xdContext.catalog.unregisterTable(tableIdentifier)
    val df = xdContext.sql(s"SELECT $SubField2Name FROM $Database.$TableName")
    df shouldBe a[XDDataFrame]
    df.schema.apply(0).dataType shouldBe (ArrayType(StringType))
  }

  it should s"persist a table with catalog and partitionColumns with array of integers as schema in $catalogName" in {
    xdContext.catalog.dropAllTables()
    val tableIdentifier = TableIdentifier(TableName, Some(Database))
    val crossdataTable = CrossdataTable(TableIdentifier(TableName, Some(Database)).normalize, Some(ColumnsWithArrayInteger), SourceDatasource, Array.empty[String], OptsJSON)
    xdContext.catalog.persistTableMetadata(crossdataTable)

    xdContext.catalog.unregisterTable(tableIdentifier)
    xdContext.sql(s"DESCRIBE $Database.$TableName")
    val df = xdContext.sql(s"SELECT $SubField2Name FROM $Database.$TableName")
    df shouldBe a[XDDataFrame]
    df.schema.apply(0).dataType shouldBe (ArrayType(IntegerType))
  }

  it should s"persist a table with catalog and partitionColumns with arrays with subdocuments and strange " +
    s"characters in Field names as schema in $catalogName" in {
    xdContext.catalog.dropAllTables()
    val tableIdentifier = TableIdentifier(TableName, Some(Database))
    val crossdataTable = CrossdataTable(TableIdentifier(TableName, Some(Database)).normalize, Some(ColumnsWithArrayWithSubdocuments), SourceDatasource, Array.empty[String], OptsJSON)
    xdContext.catalog.persistTableMetadata(crossdataTable)

    xdContext.catalog.unregisterTable(tableIdentifier)
    val schemaDF = xdContext.sql(s"DESCRIBE $Database.$TableName")
    schemaDF.count() should be(3)
    val df = xdContext.sql(s"SELECT `$FieldWitStrangeChars` FROM $Database.$TableName")
    df shouldBe a[XDDataFrame]
    df.schema.apply(0).dataType shouldBe (ArrayType(StructType(Seq(Field1, Field2))))
  }

  it should s"persist a table with catalog and partitionColumns with map with arrays with subdocuments and strange " +
    s"characters in Field names as schema in $catalogName" in {
    xdContext.catalog.dropAllTables()
    val tableIdentifier = TableIdentifier(TableName, Some(Database))
    val crossdataTable = CrossdataTable(TableIdentifier(TableName, Some(Database)).normalize, Some(ColumnsWithMapWithArrayWithSubdocuments), SourceDatasource, Array.empty[String], OptsJSON)
    xdContext.catalog.persistTableMetadata(crossdataTable)
    xdContext.catalog.unregisterTable(tableIdentifier)
    val schemaDF = xdContext.sql(s"DESCRIBE $Database.$TableName")
    schemaDF.count() should be(1)
    val df = xdContext.sql(s"SELECT `$Field1Name` FROM $Database.$TableName")
    df shouldBe a[XDDataFrame]
    df.schema.apply(0).dataType shouldBe (MapType(ColumnsWithSubColumns, ColumnsWithArrayWithSubdocuments))
  }

  it should "returns list of tables" in {
    xdContext.catalog.dropAllTables()
    val crossdataTable1 = CrossdataTable(TableIdentifier(TableName, Some(Database)).normalize, Some(Columns), SourceDatasource, Array[String](Field1Name), OptsJSON)
    val crossdataTable2 = CrossdataTable(TableIdentifier(TableName, None).normalize, Some(Columns), SourceDatasource, Array[String](Field1Name), OptsJSON)

    xdContext.catalog.persistTableMetadata(crossdataTable1)
    xdContext.catalog.persistTableMetadata(crossdataTable2)

    val tables = xdContext.catalog.getTables(Some(Database))
    tables.size shouldBe (1)
    val tables2 = xdContext.catalog.getTables(None)
    tables2.size shouldBe (2)
  }

  it should "drop tables" in {
    xdContext.catalog.dropAllTables()

    val crossdataTable1 = CrossdataTable(TableIdentifier(TableName, Some(Database)).normalize, Some(Columns), SourceDatasource, Array[String](Field1Name), OptsJSON)
    val crossdataTable2 = CrossdataTable(TableIdentifier(TableName, None).normalize, Some(Columns), SourceDatasource, Array[String](Field1Name), OptsJSON)
    val tableIdentifier1 = TableIdentifier(TableName, Some(Database))
    val tableIdentifier2 = TableIdentifier(TableName)
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

  it should "not drop tables that not exists" in {
    xdContext.catalog.dropAllTables()

    val tableIdentifier = TableIdentifier(TableName, Some(Database))

    a[RuntimeException] shouldBe thrownBy{
      xdContext.catalog.dropTable(tableIdentifier)
    }
  }

  it should "check if tables map is correct with databaseName" in {
    xdContext.catalog.dropAllTables()
    val crossdataTable1 = CrossdataTable(TableIdentifier(TableName, Some(Database)).normalize, Some(Columns), SourceDatasource, Array[String](Field1Name), OptsJSON)
    val tableIdentifier2 = TableIdentifier(TableName)

    xdContext.catalog.persistTableMetadata(crossdataTable1)

    xdContext.catalog.registerTable(tableIdentifier2, LogicalRelation(new MockBaseRelation))
    xdContext.catalog.unregisterAllTables()
    xdContext.catalog.tableCache.size shouldBe 0
    val tables = xdContext.catalog.getTables(Some(Database))
    tables.size shouldBe 1
  }

  it should "check if tables map is correct without databaseName " in {
    xdContext.catalog.dropAllTables()
    val crossdataTable1 = CrossdataTable(TableIdentifier(TableName, Some(Database)).normalize, Some(Columns), SourceDatasource, Array[String](Field1Name), OptsJSON)
    val tableIdentifier2 = TableIdentifier(TableName)

    xdContext.catalog.persistTableMetadata(crossdataTable1)

    xdContext.catalog.registerTable(tableIdentifier2, LogicalRelation(new MockBaseRelation))
    xdContext.catalog.unregisterAllTables()
    xdContext.catalog.tableCache.size shouldBe 0
    val tables = xdContext.catalog.getTables(None)
    tables.size shouldBe 1
  }

  it should "check if persisted tables are marked as not temporary" in {
    xdContext.catalog.dropAllTables()
    val crossdataTable1 = CrossdataTable(TableIdentifier(TableName, Some(Database)).normalize, Some(Columns), SourceDatasource, Array[String](Field1Name), OptsJSON)
    val tableIdentifier2 = TableIdentifier(TableName)
    xdContext.catalog.persistTableMetadata(crossdataTable1)
    xdContext.catalog.registerTable(tableIdentifier2, LogicalRelation(new MockBaseRelation))
    val tables = xdContext.catalog.getTables(None).toMap

    if(xdContext.conf.caseSensitiveAnalysis){
      tables(s"$Database.$TableName") shouldBe false
      tables(TableName) shouldBe true
    } else{
      tables(s"${Database.toLowerCase}.${TableName.toLowerCase}") shouldBe false
      tables(TableName.toLowerCase) shouldBe true
    }

  }

  it should "describe a table persisted and non persisted with subcolumns" in {
    xdContext.catalog.dropAllTables()
    val crossdataTable = CrossdataTable(TableIdentifier(TableName, Some(Database)).normalize, Some(ColumnsWithSubColumns), SourceDatasource, Array.empty[String], OptsJSON)
    xdContext.catalog.persistTableMetadata(crossdataTable)
    xdContext.sql(s"DESCRIBE $Database.$TableName").count() should not be 0
  }


  it should s"persist an App in catalog "in {

    val crossdataApp = CrossdataApp("hdfs://url/myjar.jar", "myApp", "com.stratio.app.main")

    xdContext.catalog.persistAppMetadata(crossdataApp)
    val res:CrossdataApp=xdContext.catalog.lookupApp("myApp").get
    res shouldBe a[CrossdataApp]
    res.jar shouldBe "hdfs://url/myjar.jar"
    res.appAlias shouldBe "myApp"
    res.appClass shouldBe "com.stratio.app.main"
  }

  it should "persist index in catalog" in {
    val tableIdentifier = TableIdentifier("tableIndex").normalize
    val indexIdentifier = IndexIdentifier("global", "myIndex").normalize
    val indexedCols = Seq("colIndexed")
    val pk = "primaryCol"
    val dataSource = "mongo"
    val opts = Map[String, String]()
    val version = "1.5.0"
    val crossdataIndex = CrossdataIndex(tableIdentifier, indexIdentifier, indexedCols, pk, dataSource, opts, version)

    val crossdataTable = CrossdataTable(tableIdentifier, Some(Columns), SourceDatasource, Array[String](Field1Name), OptsJSON)
    xdContext.catalog.persistTableMetadata(crossdataTable)

    xdContext.catalog.persistIndex(crossdataIndex)

    val res = xdContext.catalog.lookupIndex(indexIdentifier)

    res.get shouldBe a[CrossdataIndex]
    res.get.indexIdentifier shouldBe indexIdentifier
  }

  it should "drop index from catalog" in {
    val tableIdentifier = TableIdentifier("tableIndex2").normalize
    val indexIdentifier = IndexIdentifier("global2", "myIndex2").normalize
    val indexedCols = Seq("colIndexed")
    val pk = "primaryCol"
    val dataSource = "mongo"
    val opts = Map[String, String]()
    val version = "1.5.0"
    val crossdataIndex = CrossdataIndex(tableIdentifier, indexIdentifier, indexedCols, pk, dataSource, opts, version)

    val crossdataTable = CrossdataTable(tableIdentifier, Some(Columns), SourceDatasource, Array[String](Field1Name), OptsJSON)
    xdContext.catalog.persistTableMetadata(crossdataTable)
    xdContext.catalog.persistIndex(crossdataIndex)

    xdContext.catalog.dropIndex(indexIdentifier)
    val res = xdContext.catalog.lookupIndex(indexIdentifier)

    res shouldBe None
  }

  it should "obtain index from catalog with tableIdentifier" in {
    val tableIdentifier = TableIdentifier("tableIndex3").normalize
    val indexIdentifier = IndexIdentifier("global3", "myIndex3").normalize
    val indexedCols = Seq("colIndexed")
    val pk = "primaryCol"
    val dataSource = "mongo"
    val opts = Map[String, String]()
    val version = "1.5.0"
    val crossdataIndex = CrossdataIndex(tableIdentifier, indexIdentifier, indexedCols, pk, dataSource, opts, version)

    val crossdataTable = CrossdataTable(tableIdentifier, Some(Columns), SourceDatasource, Array[String](Field1Name), OptsJSON)
    xdContext.catalog.persistTableMetadata(crossdataTable)
    xdContext.catalog.persistIndex(crossdataIndex)

    val res = xdContext.catalog.indexMetadataByTableIdentifier(tableIdentifier.toTableIdentifier)

    res.get shouldBe a[CrossdataIndex]
    res.get.indexIdentifier shouldBe indexIdentifier
  }

  it should "drop all indexes" in {
    val tableIdentifier = TableIdentifier("tableIndex4").normalize
    val indexIdentifier = IndexIdentifier("global4", "myIndex4").normalize
    val indexedCols = Seq("colIndexed")
    val pk = "primaryCol"
    val dataSource = "mongo"
    val opts = Map[String, String]()
    val version = "1.5.0"
    val crossdataIndex = CrossdataIndex(tableIdentifier, indexIdentifier, indexedCols, pk, dataSource, opts, version)

    val crossdataTable = CrossdataTable(tableIdentifier, Some(Columns), SourceDatasource, Array[String](Field1Name), OptsJSON)
    xdContext.catalog.persistTableMetadata(crossdataTable)
    xdContext.catalog.persistIndex(crossdataIndex)

    xdContext.catalog.dropAllIndexes()

    val res = xdContext.catalog.lookupIndex(indexIdentifier)
    res shouldBe None

  }

  it should "remove an index associated to a table" in {

    val tableIdentifier = TableIdentifier("testTable", Option("dbTest")).normalize
    val indexIdentifier = IndexIdentifier("myIndex", "gidx").normalize

    val table = CrossdataTable(tableIdentifier, None, "com.stratio.crossdata.connector.mongodb")

    val index = CrossdataIndex(tableIdentifier, indexIdentifier,
      Seq(), "pk", "com.stratio.crossdata.connector.elasticsearch")

    xdContext.catalog.persistTable(table, LocalRelation())
    xdContext.catalog.persistIndex(index)

    xdContext.catalog.dropTable(tableIdentifier.toTableIdentifier)

    val res = xdContext.catalog.lookupIndex(indexIdentifier)
    res shouldBe None
  }

  it should "remove an index associated to a table and the table generated for the index" in {

    val tableIdentifier = TableIdentifier("testTable", Option("dbTest")).normalize
    val indexIdentifier = IndexIdentifier("myIndex", "gidx").normalize

    val table = CrossdataTable(tableIdentifier, None, "com.stratio.crossdata.connector.mongodb")

    val index = CrossdataIndex(tableIdentifier, indexIdentifier,
      Seq(), "pk", "com.stratio.crossdata.connector.elasticsearch")

    val tableGenerated = CrossdataTable(indexIdentifier.asTableIdentifierNormalized,
      None, "com.stratio.crossdata.connector.elasticsearch")

    xdContext.catalog.persistTable(table, LocalRelation())
    xdContext.catalog.persistIndex(index)
    xdContext.catalog.persistTable(tableGenerated, LocalRelation())

    xdContext.catalog.dropTable(tableIdentifier.toTableIdentifier)

    val res = xdContext.catalog.lookupIndex(indexIdentifier)
    res shouldBe None

    val resGenerated = xdContext.catalog.lookupTable(indexIdentifier.asTableIdentifierNormalized)
    resGenerated shouldBe None
  }

  override protected def afterAll() {
    xdContext.catalog.dropAllTables()
    xdContext.catalog.dropAllViews()
    xdContext.catalog.dropAllIndexes()
    super.afterAll()
  }

}
