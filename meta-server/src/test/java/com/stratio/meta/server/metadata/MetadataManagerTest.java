package com.stratio.meta.server.metadata;

import com.stratio.meta.server.metadata.MetadataManager;
import static org.junit.Assert.assertEquals;
import org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.TableMetadata;
import com.stratio.meta.server.cassandra.BasicCassandraTest;

import java.util.List;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MetadataManagerTest extends BasicCassandraTest {

    private static MetadataManager _metadataManager = null;

    @BeforeClass
    public static void setUpBeforeClass(){
        BasicCassandraTest.setUpBeforeClass();
        _metadataManager = new MetadataManager();
        assertTrue("Cannot connect MetadataManager to C*", _metadataManager.connect("127.0.0.1"));
        _metadataManager.loadMetadata();
    }
	
	@Test
	public void getKeyspaceMetadata() {
		String keyspace = "system";
		int numTables = 16; //Number of system tables in Cassandra 2.0.5
		KeyspaceMetadata metadata = _metadataManager.getKeyspaceMetadata(keyspace);
		assertNotNull("Cannot retrieve keyspace metadata", metadata);
		assertEquals("Retrieved keyspace name does not match", keyspace, metadata.getName());
		assertTrue("Invalid numver of columns", metadata.getTables().size() == numTables);
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
			assertNotNull("Cannot retrieve table metadata", metadata);
			assertEquals("Retrieved table name does not match", table, metadata.getName());
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
		assertNotNull("Cannot retrieve table metadata", metadata);
		assertEquals("Retrieved table name does not match", table, metadata.getName());

		assertTrue("Invalid number of columns", metadata.getColumns().size() == numColumns);
		for(int columnIndex = 0; columnIndex < numColumns; columnIndex++){
			ColumnMetadata cm = metadata.getColumn(columnNames[columnIndex]);
			assertNotNull("Cannot retrieve column", cm);
			assertEquals("Column type does not match", columnClass[columnIndex], cm.getType().asJavaClass());
		}
	}

    @Test
    public void luceneMetadata(){
        String keyspace = "demo";
        String table = "users";
        int numColumns = 12;

        TableMetadata metadata = _metadataManager.getTableMetadata(keyspace, table);
        assertNotNull("Cannot retrieve table metadata", metadata);
        assertEquals("Retrieved table name does not match", table, metadata.getName());
        assertTrue("Invalid number of columns", metadata.getColumns().size() == numColumns);

        ColumnMetadata cm = metadata.getColumn("lucene");
        assertNotNull("Cannot retrieve lucene indexed column", cm);
        ColumnMetadata.IndexMetadata im = cm.getIndex();
        assertNotNull("No index information found", im);

        System.out.println("index.asCQLQuery: " + im.asCQLQuery());
        System.out.println("IndexClassName: " + im.getIndexClassName());
        System.out.println("IndexedColumn: " + im.getIndexedColumn());
        System.out.println("IndexName: " + im.getName());
        System.out.println("isCustomIndex: " + im.isCustomIndex());


    }

    @Test
    public void getKeyspacesNames(){
        List<String> keyspaces = _metadataManager.getKeyspacesNames();
        assertNotNull("Cannot retrieve the list of keyspaces", keyspaces);
        assertTrue("At least two keyspaces should be returned", keyspaces.size() > 2);
        assertTrue("system keyspace not found", keyspaces.contains("system"));
        assertTrue("system_traces keyspace not found", keyspaces.contains("system_traces"));
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
        assertNotNull("Cannot retrieve the list of table names", tables);
        assertTrue("At least two keyspaces should be returned", tables.size() == systemTables.length);
        for(String table : systemTables){
            assertTrue("system table not found", tables.contains(table));
        }
    }

}
