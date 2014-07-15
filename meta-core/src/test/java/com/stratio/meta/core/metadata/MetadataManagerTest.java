/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.core.metadata;

import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.TableMetadata;
import com.stratio.meta.core.cassandra.BasicCoreCassandraTest;
import com.stratio.meta.core.structures.IndexType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

public class MetadataManagerTest extends BasicCoreCassandraTest {

  private static MetadataManager _metadataManager = null;

  @BeforeClass
  public static void setUpBeforeClass(){
    BasicCoreCassandraTest.setUpBeforeClass();
    BasicCoreCassandraTest.loadTestData("demo", "demoKeyspace.cql");
    _metadataManager = new MetadataManager(_session, null);
    _metadataManager.loadMetadata();
  }

  @Test
  public void getKeyspaceMetadata() {
    String keyspace = "system";
    int numTables = 16; //Number of system tables in Cassandra 2.0.5
    KeyspaceMetadata metadata = _metadataManager.getKeyspaceMetadata(keyspace);
    assertNotNull(metadata, "Cannot retrieve catalog metadata");
    assertEquals(keyspace, metadata.getName(), "Retrieved catalog name does not match");
    assertEquals(numTables, metadata.getTables().size(), "Invalid number of columns");
  }

  @Test
  public void getTableMetadata() {
    String keyspace = "system";
    String [] tables = {
        "IndexInfo", "NodeIdInfo", "batchlog",
        "compaction_history", "compactions_in_progress",
        "hints", "local", "paxos", "peer_events",
        "peers", "range_xfers", "schema_columnfamilies",
        "schema_columns", "schema_keyspaces",
        "schema_triggers", "sstable_activity"};
    TableMetadata metadata = null;
    for(String table : tables){
      metadata = _metadataManager.getTableMetadata(keyspace, table);
      assertNotNull(metadata, "Cannot retrieve table " + table + " metadata");
      assertEquals(table, metadata.getName(), "Retrieved table name does not match");
    }
  }

  @Test
  public void inspectTableMetadata() {
    //CREATE TABLE schema_keyspaces (
    //		  keyspace_name text,
    //		  durable_writes boolean,
    //		  strategy_class text,
    //		  strategy_options text,
    //		  PRIMARY KEY (keyspace_name))

    String keyspace = "system";
    String table = "schema_keyspaces";

    int numColumns = 4;

    String [] columnNames = {"keyspace_name", "durable_writes", "strategy_class", "strategy_options"};
    Class<?> [] columnClass = {String.class, Boolean.class, String.class, String.class};

    TableMetadata metadata = _metadataManager.getTableMetadata(keyspace, table);
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
  public void getKeyspacesNames(){
    List<String> keyspaces = _metadataManager.getKeyspacesNames();
    assertNotNull(keyspaces, "Cannot retrieve the list of keyspaces");
    assertTrue(keyspaces.size() >= 2, "At least two keyspaces should be returned: " + keyspaces.toString());
    assertTrue(keyspaces.contains("system"), "system catalog not found");
    assertTrue(keyspaces.contains("system_traces"), "system_traces catalog not found");
  }

  @Test
  public void getTableNames(){
    String keyspace = "system";
    String [] systemTables = {
        "IndexInfo", "NodeIdInfo", "batchlog",
        "compaction_history", "compactions_in_progress",
        "hints", "local", "paxos", "peer_events",
        "peers", "range_xfers", "schema_columnfamilies",
        "schema_columns", "schema_keyspaces",
        "schema_triggers", "sstable_activity"};
    List<String> tables = _metadataManager.getTablesNames(keyspace);
    assertNotNull(tables, "Cannot retrieve the list of table names");
    assertEquals(systemTables.length, tables.size(), "At least two keyspaces should be returned");
    for(String table : systemTables){
      assertTrue(tables.contains(table), "system table not found");
    }
  }

  @Test
  public void testGetColumnIndexes(){

    String keyspace = "demo";
    String table = "users";
    //Columns with one index: email, name, phrase
    //Columns with two indexes: age, bool, gender

    int numberIndexes = 4;
    int numberIndexedColumnsLucene = 6;
    int numberIndexedColumnsDefault = 1;

    TableMetadata metadata = _metadataManager.getTableMetadata(keyspace, table);
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
    String keyspace = "demo";
    String table = "users_info";
    TableMetadata metadata = _metadataManager.getTableMetadata(keyspace, table);
    assertNotNull(metadata, "Cannot retrieve table metadata");
    List<CustomIndexMetadata> indexes = _metadataManager.getTableIndex(metadata);
    assertNotNull(indexes, "Cannot retrieve list of indexes");
    assertEquals(indexes.size(), 0, "Table should not contain any index.");
  }


  @Test
  public void getTableComment(){
    String keyspace = "demo";
    String table = "users";
    String comment = _metadataManager.getTableComment(keyspace, table);
    assertNotNull(comment, "Cannot retrieve table comment");
    assertEquals("Users table", comment, "Invalid comment");
  }

  @Test
  public void getLuceneIndexNotFound(){
    String keyspace = "demo";
    String table = "users_info";
    TableMetadata metadata = _metadataManager.getTableMetadata(keyspace, table);
    assertNotNull(metadata, "Cannot retrieve table metadata");
    CustomIndexMetadata cim = _metadataManager.getLuceneIndex(metadata);
    assertNull(cim, "Table should not contain a Lucene index");
  }

}
