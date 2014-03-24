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

import java.util.List;
import java.util.Map;

import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.TableMetadata;

import com.stratio.meta.core.cassandra.BasicCoreCassandraTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;
import static org.testng.Assert.assertEquals;

public class MetadataManagerTest extends BasicCoreCassandraTest {

    private static MetadataManager _metadataManager = null;

    @BeforeClass
    public static void setUpBeforeClass(){
        BasicCoreCassandraTest.setUpBeforeClass();
        BasicCoreCassandraTest.loadTestData("demo", "demoKeyspace.cql");
        _metadataManager = new MetadataManager(_session);
        _metadataManager.loadMetadata();
    }
	
	@Test
	public void getKeyspaceMetadata() {
		String keyspace = "system";
		int numTables = 16; //Number of system tables in Cassandra 2.0.5
		KeyspaceMetadata metadata = _metadataManager.getKeyspaceMetadata(keyspace);
		assertNotNull(metadata, "Cannot retrieve keyspace metadata");
		assertEquals(keyspace, metadata.getName(), "Retrieved keyspace name does not match");
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
			assertNotNull(metadata, "Cannot retrieve table metadata");
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

    //@Test
    public void luceneMetadata(){
        String keyspace = "demo";
        String table = "users";
        int numColumns = 7;

        TableMetadata metadata = _metadataManager.getTableMetadata(keyspace, table);
        assertNotNull(metadata, "Cannot retrieve table metadata");
        assertEquals(table, metadata.getName(), "Retrieved table name does not match");
        assertEquals(numColumns, metadata.getColumns().size(), "Invalid number of columns");

        ColumnMetadata cm = metadata.getColumn("lucene_index_1");
        assertNotNull(cm, "Cannot retrieve lucene indexed column");
        ColumnMetadata.IndexMetadata im = cm.getIndex();
        assertNotNull(im, "No index information found");

        fail("Missing checks on the resulting map");

    }

    @Test
    public void getKeyspacesNames(){
        List<String> keyspaces = _metadataManager.getKeyspacesNames();
        assertNotNull(keyspaces, "Cannot retrieve the list of keyspaces");
        assertTrue(keyspaces.size() >= 2, "At least two keyspaces should be returned: " + keyspaces.toString());
        assertTrue(keyspaces.contains("system"), "system keyspace not found");
        assertTrue(keyspaces.contains("system_traces"), "system_traces keyspace not found");
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
        String [] columns = {
                "age", "bool", "gender", //Two indexes, one on Cassandra and other in Lucene
                "email", "name", "phrase"};

        TableMetadata metadata = _metadataManager.getTableMetadata(keyspace, table);
        assertNotNull(metadata, "Cannot retrieve table metadata");
        assertEquals(table, metadata.getName(), "Retrieved table name does not match");

        Map<String, List<CustomIndexMetadata>> indexes = _metadataManager.getColumnIndexes(metadata);
        //System.out.println("Returned: " + indexes.size() + " -> " + indexes.keySet().toString());
        assertEquals(columns.length, indexes.size(), "Invalid number of indexes");

        for(String column : columns){
            assertTrue(indexes.containsKey(column), "Column does not have an index");
            if(column.equals("age") || column.equals("bool") || column.equals("gender")){
                assertEquals(indexes.get(column).size(), 2,
                        "Invalid number of index associated with column " + column);
            }else{
                assertEquals(indexes.get(column).size(), 1,
                        "Invalid number of index associated with column" + column);
            }
        }

    }

}
