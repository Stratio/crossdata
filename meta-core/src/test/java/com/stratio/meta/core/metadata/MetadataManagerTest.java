/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta.core.metadata;

import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.TableMetadata;
import com.stratio.meta.core.cassandra.BasicCoreCassandraTest;
import com.stratio.meta.core.structures.IndexType;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.metadata.CatalogMetadata;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class MetadataManagerTest extends BasicCoreCassandraTest {

  private static MetadataManager _metadataManager = null;

  @BeforeClass
  public static void setUpBeforeClass(){
    BasicCoreCassandraTest.setUpBeforeClass();
    BasicCoreCassandraTest.loadTestData("demo", "demoCatalog.cql");
    _metadataManager = new MetadataManager(_session, null);
    _metadataManager.loadMetadata();
  }

  @Test
  public void getCatalogMetadata() {
    String catalog = "system";
    int numTables = 16; //Number of system tables in Cassandra 2.0.5
    CatalogMetadata metadata = _metadataManager.getCatalogMetadata(catalog);
    assertNotNull(metadata, "Cannot retrieve catalog metadata");
    assertEquals(catalog, metadata.getName(), "Retrieved catalog name does not match");
    assertEquals(numTables, metadata.getTables().size(), "Invalid number of columns");
  }

  @Test
  public void getTableMetadata() {
    String catalog = "system";
    String [] tables = {
        "IndexInfo", "NodeIdInfo", "batchlog",
        "compaction_history", "compactions_in_progress",
        "hints", "local", "paxos", "peer_events",
        "peers", "range_xfers", "schema_columnfamilies",
        "schema_columns", "schema_catalogs",
        "schema_triggers", "sstable_activity"};
    TableMetadata metadata = null;
    for(String table : tables){
      metadata = _metadataManager.getTableMetadata(catalog, new TableName("", table));
      assertNotNull(metadata, "Cannot retrieve table " + table + " metadata");
      assertEquals(table, metadata.getName(), "Retrieved table name does not match");
    }
  }

  @Test
  public void inspectTableMetadata() {
    //CREATE TABLE schema_catalogs (
    //		  catalog_name text,
    //		  durable_writes boolean,
    //		  strategy_class text,
    //		  strategy_options text,
    //		  PRIMARY KEY (catalog_name))

    String catalog = "system";
    String table = "schema_catalogs";

    int numColumns = 4;

    String [] columnNames = {"catalog_name", "durable_writes", "strategy_class", "strategy_options"};
    Class<?> [] columnClass = {String.class, Boolean.class, String.class, String.class};

    TableMetadata metadata = _metadataManager.getTableMetadata(catalog, new TableName("", table));
    assertNotNull(metadata, "Cannot retrieve table metadata");
    assertEquals(table, metadata.getName(), "Retrieved table name does not match");

    assertEquals(numColumns, metadata.getColumns().size(), "Invalid number of columns");
    for(int columnIndex = 0; columnIndex < numColumns; columnIndex++){
      ColumnMetadata cm = metadata.getColumn(columnNames[columnIndex]);
      assertNotNull(cm, "Cannot retrieve column");
      assertEquals(columnClass[columnIndex], cm.getType().asJavaClass(), "Column type does not match");
    }
  }

  @Test
  public void getCatalogsNames(){
    List<String> catalogs = _metadataManager.getCatalogsNames();
    assertNotNull(catalogs, "Cannot retrieve the list of catalogs");
    assertTrue(catalogs.size() >= 2, "At least two catalogs should be returned: " + catalogs.toString());
    assertTrue(catalogs.contains("system"), "system catalog not found");
    assertTrue(catalogs.contains("system_traces"), "system_traces catalog not found");
  }

  @Test
  public void getTableNames(){
    String catalog = "system";
    String [] systemTables = {
        "IndexInfo", "NodeIdInfo", "batchlog",
        "compaction_history", "compactions_in_progress",
        "hints", "local", "paxos", "peer_events",
        "peers", "range_xfers", "schema_columnfamilies",
        "schema_columns", "schema_catalogs",
        "schema_triggers", "sstable_activity"};
    List<String> tables = _metadataManager.getTablesNames(catalog);
    assertNotNull(tables, "Cannot retrieve the list of table names");
    assertEquals(systemTables.length, tables.size(), "At least two catalogs should be returned");
    for(String table : systemTables){
      assertTrue(tables.contains(table), "system table not found");
    }
  }

  @Test
  public void testGetColumnIndexes(){

    String catalog = "demo";
    String table = "users";
    //Columns with one index: email, name, phrase
    //Columns with two indexes: age, bool, gender

    int numberIndexes = 4;
    int numberIndexedColumnsLucene = 6;
    int numberIndexedColumnsDefault = 1;

    TableMetadata metadata = _metadataManager.getTableMetadata(catalog, new TableName("", table));
    assertNotNull(metadata, "Cannot retrieve table metadata");
    assertEquals(table, metadata.getName(), "Retrieved table name does not match");

    List<CustomIndexMetadata> indexes = _metadataManager.getTableIndex(metadata);
    assertEquals(indexes.size(), numberIndexes, "Invalid number of indexes");

    for(CustomIndexMetadata cim : indexes){
      if(IndexType.LUCENE.equals(cim.getIndexType())){
        assertEquals(cim.getIndexedColumns().size(), numberIndexedColumnsLucene, "Invalid number of mapped columns using Lucene index");
      }else{
        assertEquals(cim.getIndexedColumns().size(), numberIndexedColumnsDefault, "Invalid number of mapped columns using default index");
      }
    }
  }

  @Test
  public void getTableIndexNotFound(){
    String catalog = "demo";
    String table = "users_info";
    TableMetadata metadata = _metadataManager.getTableMetadata(catalog, new TableName("", table));
    assertNotNull(metadata, "Cannot retrieve table metadata");
    List<CustomIndexMetadata> indexes = _metadataManager.getTableIndex(metadata);
    assertNotNull(indexes, "Cannot retrieve list of indexes");
    assertEquals(indexes.size(), 0, "Table should not contain any index.");
  }


  @Test
  public void getTableComment(){
    String catalog = "demo";
    String table = "users";
    String comment = _metadataManager.getTableComment(catalog, table);
    assertNotNull(comment, "Cannot retrieve table comment");
    assertEquals("Users table", comment, "Invalid comment");
  }

  @Test
  public void getLuceneIndexNotFound(){
    String catalog = "demo";
    String table = "users_info";
    TableMetadata metadata = _metadataManager.getTableMetadata(catalog, new TableName("", table));
    assertNotNull(metadata, "Cannot retrieve table metadata");
    CustomIndexMetadata cim = _metadataManager.getLuceneIndex(metadata);
    assertNull(cim, "Table should not contain a Lucene index");
  }

}
