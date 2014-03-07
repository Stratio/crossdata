package com.stratio.meta.metadata;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.TableMetadata;
import com.stratio.meta.cassandra.BasicCassandraTest;

public class MetadataManagerTest extends BasicCassandraTest {

	private static MetadataManager _metadataManager = null;

	@BeforeClass
    public static void setUpBeforeClass(){
		init();
		_metadataManager = new MetadataManager();
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

}
