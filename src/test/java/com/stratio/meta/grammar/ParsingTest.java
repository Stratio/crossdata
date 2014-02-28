package com.stratio.meta.grammar;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.exceptions.DriverException;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.stratio.meta.cassandra.CassandraClient;
import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.stratio.meta.statements.CreateIndexStatement;
import com.stratio.meta.statements.MetaStatement;
import com.stratio.meta.structures.ValueProperty;
import com.stratio.meta.utils.AntlrResult;
import com.stratio.meta.utils.MetaUtils;

import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;

import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 * MetaParser tests that recognize the different options of each Statement.
 */
public class ParsingTest {

	/**
	 * Class logger.
	 */
	private static final Logger logger = Logger.getLogger(ParsingTest.class);	
        
	public static void initCassandraConnection(){
            try {
                CassandraClient.connect();
            } catch(NoHostAvailableException ex){
                logger.error("\033[31mCannot connect with Cassandra\033[0m", ex);  
                System.exit(-1);
            }
	}
        
	public static void checkKeyspaces(){
            try {
                CassandraClient.executeQuery("USE testKS", false);
                logger.error("\033[31mKeyspace \'testKs\' already exists\033[0m");  
                System.exit(-1);
            } catch(DriverException ex){
                logger.info("Creating keyspace \'testKS\' in Cassandra for testing purposes");
            }
	}
        
        @BeforeClass
        public static void init(){
            initCassandraConnection();
            checkKeyspaces();
        }
        
        public static void dropKeyspaces(){
            CassandraClient.executeQuery("DROP KEYSPACE IF EXISTS testKS;", false);
	}
        
	public static void closeCassandraConnection(){
            CassandraClient.close();
	}

        @AfterClass
        public static void exit(){
            dropKeyspaces();
            closeCassandraConnection();
        }        	
                        
        public MetaStatement testRegularStatement(String inputText, String methodName) {
            AntlrResult antlrResult = MetaUtils.parseStatement(inputText);
            MetaStatement st = antlrResult.getStatement();
            assertNotNull("Cannot parse "+methodName, st);             
            assertTrue("Cannot parse "+methodName, inputText.equalsIgnoreCase(st.toString()+";"));
            return st;
        }
        
        public void testDriverStatement(MetaStatement st, String methodName){
            Statement driverStmt = st.getDriverStatement();
            ResultSet resultSet;
            if(driverStmt != null){
                resultSet = CassandraClient.executeQuery(driverStmt, false);
            } else {
                resultSet = CassandraClient.executeQuery(st.translateToCQL(), false);
            }            
            assertNotNull("Cannot execute "+methodName+" in Cassandra", resultSet);
        }
        
        public void testStatementWithCassandra(String inputText, String methodName){
            MetaStatement st = testRegularStatement(inputText, methodName);
            testDriverStatement(st, methodName);
        }
        
        public void testMetaError(String inputText){
            AntlrResult antlrResult = MetaUtils.parseStatement(inputText);
            MetaStatement st = antlrResult.getStatement();
            thrown.expect(NullPointerException.class);
            System.out.println(st.toString());
        }
        
        public void testRecoverableError(String inputText, String methodName){
            AntlrResult antlrResult = MetaUtils.parseStatement(inputText);
            MetaStatement st = antlrResult.getStatement();
            assertTrue("No errors reported in "+methodName, antlrResult.getFoundErrors().getNumberOfErrors()>0);
        }
        
        public void testCassandraError(MetaStatement st){
            Statement driverStmt = st.getDriverStatement();
            thrown.expect(DriverException.class); 
            ResultSet resultSet = CassandraClient.executeQuery(driverStmt, false);
        }        	

        // CREATE KEYSPACE
	@Test
	public void createKeyspace_ifNotExists() {
		String inputText = "CREATE KEYSPACE IF NOT EXISTS key_space1 "
				+ "WITH replication = replicationLevel AND durable_writes = false;";
		testRegularStatement(inputText, "createKeyspace_ifNotExists");
	}

	//TODO: Should we support it in this way?
	@Test
	public void createKeyspace_nestedOptions() {
		String inputText = "CREATE KEYSPACE IF NOT EXISTS key_space1 "
				+ "WITH replication = {class: NetworkTopologyStrategy, DC1: 1, DC2: 3} "
				+"AND durable_writes = false;";
		Set<String> properties = new HashSet<>();
		properties.add("class: NetworkTopologyStrategy");
		properties.add("DC1: 1");
		properties.add("DC2: 3");
		AntlrResult antlrResult = MetaUtils.parseStatement(inputText);
		MetaStatement st = antlrResult.getStatement();
		String propResultStr = st.toString().substring(st.toString().indexOf("{")+1, st.toString().indexOf("}"));
		String[] str = propResultStr.split(",");
		Set<String> propertiesResult = new HashSet<>();
		for (String str1 : str) {
			propertiesResult.add(str1.trim());
		}                                
		assertNotNull("Cannot parse create keyspace - nestedOptions", st);                
		assertEquals("Cannot parse create keyspace - nestedOptions", 
				"CREATE KEYSPACE IF NOT EXISTS key_space1 WITH replication = {", 
				st.toString().substring(0, st.toString().indexOf("{")+1));
		assertEquals("Cannot parse create keyspace - nestedOptions", 
				"} AND durable_writes = false;", 
				st.toString().substring(st.toString().indexOf("}"))+";");
		assertTrue("Cannot parse create keyspace - nestedOptions", propertiesResult.containsAll(properties));
		assertTrue("Cannot parse create keyspace - nestedOptions", properties.containsAll(propertiesResult));
	}
        
        @Test
	public void createKeyspace_basicOptions() {
		String inputText = "CREATE KEYSPACE key_space1 WITH replication = {class: SimpleStrategy, replication_factor: 1}"
                        + " AND durable_writes = false;";
		AntlrResult antlrResult = MetaUtils.parseStatement(inputText);
		MetaStatement st = antlrResult.getStatement();
                assertNotNull("Cannot parse createKeyspace_basicOptions", st);
                
		boolean originalOK = false;
                boolean alternative1 = false;
                
                if(inputText.equalsIgnoreCase(st.toString()+";")){
                    originalOK = true;
                }
                
                String alternative1Str = "CREATE KEYSPACE key_space1 WITH replication = {replication_factor: 1, class: SimpleStrategy}"
                        + " AND durable_writes = false;";
                if(alternative1Str.equalsIgnoreCase(st.toString()+";")){
                    alternative1 = true;
                }                
                                
		assertTrue("Cannot parse createKeyspace_basicOptions", (originalOK || alternative1));
	}

	@Test
	public void createKeyspace_durable_writes() {
		String inputText = "CREATE KEYSPACE demo WITH replication = {class: SimpleStrategy, replication_factor: 1} "
                        + "AND durable_writes = false;";
		AntlrResult antlrResult = MetaUtils.parseStatement(inputText);
		MetaStatement st = antlrResult.getStatement();
                assertNotNull("Cannot parse createKeyspace_durable_writes", st);
                
		boolean originalOK = false;
                boolean alternative1 = false;
                
                if(inputText.equalsIgnoreCase(st.toString()+";")){
                    originalOK = true;
                }
                
                String alternative1Str = "CREATE KEYSPACE demo WITH replication = {replication_factor: 1, class: SimpleStrategy} "
                        + "AND durable_writes = false;";
                if(alternative1Str.equalsIgnoreCase(st.toString()+";")){
                    alternative1 = true;
                }
                               
		assertTrue("Cannot parse createKeyspace_durable_writes", (originalOK || alternative1));
	}
        
        @Test
	public void createKeyspace_map_column() {
		String inputText = "CREATE TABLE demo.banks(day text, key uuid, latitude double, longitude double, name text, "
                        + "address text, tags map<text,boolean>, lucene text, PRIMARY KEY (day, key));";
		testRegularStatement(inputText, "createKeyspace_map_column");
	}
        
        @Test
	public void createKeyspace_literal_value() {
		String inputText = "CREATE LUCENE INDEX demo_banks ON demo.banks (lucene) USING org.apache.cassandra.db.index.stratio.RowIndex"
                        + " WITH OPTIONS schema = '{default_analyzer:\"org.apache.lucene.analysis.standard.StandardAnalyzer\", "
                        + "fields: {day: {type: \"date\", pattern: \"yyyy-MM-dd\"}, key: {type:\"uuid\"}}}';";
		AntlrResult antlrResult = MetaUtils.parseStatement(inputText);
		MetaStatement st = antlrResult.getStatement();
                inputText = inputText.replace("'", "");
		assertTrue("Cannot parse createKeyspace_literal_value", inputText.equalsIgnoreCase(st.toString()+";"));
	}
        
        @Test
	public void update_tablename() {
            String inputText = "UPDATE tablename USING prop1 = 342 SET ident1 = term1, ident2 = term2"
				+ " WHERE ident3 IN (term3, term4) IF field1 = 25;";
            MetaStatement st = testRegularStatement(inputText, "update_tablename");
	}

	@Test
	public void insert_into() {
		String inputText = "INSERT INTO mykeyspace.tablename (ident1, ident2) VALUES(term1, term2) "
				+ "IF NOT EXISTS USING COMPACT STORAGE AND prop1 = {innerTerm: result};";
		testRegularStatement(inputText, "insert_into");
	}

	@Test
	public void insert_into_2() {
		String inputText = "INSERT INTO mykeyspace.tablename (column1, column2) VALUES(value1, value2)"
				+ " IF NOT EXISTS USING TTL = 10;";
		testRegularStatement(inputText, "insert_into_2");
	}

	@Test
	public void truncate_table() {
		String inputText = "TRUNCATE usersTable;";
		testRegularStatement(inputText, "truncate_table");
	}	

	//
	//CREATE INDEX

	// CREATE <type_index>? INDEX (IF NOT EXISTS)? <identifier>? ON <tablename> '(' <identifier> (',' <identifier>)* ')'
	// ( USING <string> )? WITH OPTIONS? (<maps> AND <maps>...) ';'
	//HASH → Usual inverted index, Hash index. (By default).
	//FULLTEXT → Full text index. 
	//CUSTOM → custom index. (new feature for release 2)

	@Test
	public void createIndex_default_basic() {
		String inputText = "CREATE DEFAULT INDEX index1 ON table1 (field1, field2);";
		testRegularStatement(inputText, "createIndex_default_basic");
	}

	@Test
	public void createIndex_default_ifNotExist() {
		String inputText = "CREATE DEFAULT INDEX IF NOT EXISTS index1 ON table1 (field1, field2);";
		testRegularStatement(inputText, "createIndex_default_ifNotExist");
	}

	@Test
	public void createIndex_default_using() {
		String inputText = "CREATE DEFAULT INDEX index1 ON table1 (field1, field2) USING com.company.Index.class;";
		testRegularStatement(inputText, "createIndex_default_using");
	}

	@Test
	public void createIndex_default_options() {
            String inputText = "CREATE DEFAULT INDEX index1 ON table1 (field1, field2) WITH OPTIONS opt1=val1 AND opt2=val2;";
            int numberOptions = 2;
            AntlrResult antlrResult = MetaUtils.parseStatement(inputText);
            MetaStatement st = antlrResult.getStatement();
            assertNotNull("Cannot parse default index with options clause", st);
            CreateIndexStatement cist = CreateIndexStatement.class.cast(st);
            assertEquals("Cannot parse default index with options clause - name", "index1", cist.getName());
            assertEquals("Cannot parse default index with options clause - options size", numberOptions, cist.getOptions().size());
            HashMap<String, ValueProperty> options = cist.getOptions();
            for(int i = 1; i < numberOptions; i++){
                    assertTrue("Cannot parse default index with options clause - options opt"+i, options.containsKey("opt"+i));
                    assertEquals("Cannot parse default index with options clause - options opt"+i, "val"+i, options.get("opt"+i).toString());
            }
	}

	@Test
	public void createIndex_lucene() {
            String inputText = "CREATE LUCENE INDEX demo_banks ON banks"
                            + "(day, entry_id, latitude, longitude, name, address, tags)"
                            + " USING \'org.apache.cassandra.db.index.stratio.RowIndex\'"
                            + " WITH OPTIONS lucene_options = { \'schema\' : "
                            + " \'{default_analyzer:\"org.apache.lucene.analysis.standard.StandardAnalyzer\","
                            + "fields:"
                            + "{day:{type:\"date\", pattern:\"yyyy-MM-dd\"},"
                            + " entry_id:{type:\"uuid\"}, latitude:{type:\"double\"},"
                            + " longitude:{type:\"double\"}, name:{type:\"text\"},"
                            + " address:{type:\"string\"}, tags:{type:\"boolean\"}}}\'};";
            AntlrResult antlrResult = MetaUtils.parseStatement(inputText);
            MetaStatement st = antlrResult.getStatement();
            assertNotNull("Cannot parse default index with options clause", st);
            CreateIndexStatement cist = CreateIndexStatement.class.cast(st);
	}

	@Test
	public void createIndex_default_all() {
            String inputText = "CREATE DEFAULT INDEX IF NOT EXISTS index1 "
                            + "ON table1 (field1, field2) USING com.company.Index.class "
                            + "WITH OPTIONS opt1=val1 AND opt2=val2;";
            AntlrResult antlrResult = MetaUtils.parseStatement(inputText);
            MetaStatement st = antlrResult.getStatement();
            assertNotNull("Cannot parse default index with options clause", st);
            CreateIndexStatement cist = CreateIndexStatement.class.cast(st);

            String retrieved = cist.toString().substring(0, cist.toString().indexOf("OPTIONS"));
            String expected = inputText.substring(0, inputText.indexOf("OPTIONS"));
            assertEquals("Cannot parse default index with using clause", expected, retrieved);

            assertTrue("Cannot parse default index with options clause - options size", cist.getOptions().size() > 0);
            HashMap<String, ValueProperty> options = cist.getOptions();

            assertTrue("Cannot parse default index with options clause - options opt1", options.containsKey("opt1"));
            assertTrue("Cannot parse default index with options clause - options opt1", options.containsKey("opt2"));
	}

	//DROP INDEX

	@Test
	public void dropIndex_basic() {
		String inputText = "DROP INDEX index_name;";
		testRegularStatement(inputText, "dropIndex_basic");
	}

	@Test
	public void dropIndex_ifExists() {
		String inputText = "DROP INDEX IF EXISTS index_name;";
		testRegularStatement(inputText, "dropIndex_ifExists");
	}

	@Test
	public void select_statement() {
		String inputText = "SELECT ident1 AS name1, myfunction(innerIdent, anotherIdent) AS functionName "
				+ "FROM newks.newtb WITH WINDOW 5 ROWS INNER JOIN tablename ON field1=field2 WHERE ident1 LIKE whatever"
				+ " ORDER BY id1 ASC GROUP BY col1 LIMIT 50 DISABLE ANALYTICS;";
		testRegularStatement(inputText, "select_statement");
	}

	@Test
	public void select_withTimeWindow() {
		String inputText = "SELECT column1 FROM table1 WITH WINDOW 5 SECONDS WHERE column2 = 3;";
		testRegularStatement(inputText, "select_withTimeWindow");
	}                
        
	//ADD

	@Test
	public void add_basic() {
		String inputText = "ADD \"jar_name-v1.0.jar\";";
		testRegularStatement(inputText, "add_basic");
	}

	@Test
	public void add_relative() {
		String inputText = "ADD \"dir/jar_name-v1.0.jar\";";
		testRegularStatement(inputText, "add_relative");
	}

	@Test
	public void add_absolute() {
		String inputText = "ADD \"/dir/jar_name-v1.0.jar\";";
		testRegularStatement(inputText, "add_absolute");
	}

	//LIST

	@Test
	public void list_process() {
		String inputText = "LIST PROCESS;";
		testRegularStatement(inputText, "list_process");
	}

	@Test
	public void list_process_lowercase() {
		String inputText = "LIST process;";
		testRegularStatement(inputText, "list_process_lowercase");
	}
	
	@Test
	public void list_udf() {
		String inputText = "LIST UDF;";
		testRegularStatement(inputText, "list_udf");
	}

	@Test
	public void list_trigger() {
		String inputText = "LIST TRIGGER;";
		testRegularStatement(inputText, "list_trigger");
	}

	//REMOVE UDF	
	@Test
	public void removeUDF() {
		String inputText = "REMOVE UDF \"jar.name\";";
		testRegularStatement(inputText, "removeUDF");
	}

	//DELETE ( <selection> ( ',' <selection> )* )?
	//FROM <tablename>
	//WHERE <where-clause>	
	@Test
	public void delete_where() {
		String inputText = "DELETE FROM table1 WHERE field1 = value1;";
		testRegularStatement(inputText, "delete_where");
	}

	@Test
	public void delete_selection() {
		String inputText = "DELETE (col1, col2) FROM table1 WHERE field1 = value1;";
		testRegularStatement(inputText, "delete_selection");
	}

	@Test
	public void delete_full() {
		String inputText = "DELETE (col1, col2) FROM table1 WHERE field1 = value1 AND field2 = value2;";
		testRegularStatement(inputText, "delete_full");
	}

	@Test
	public void set_basic() {
		String inputText = "SET OPTIONS ANALYTICS=true AND CONSISTENCY=LOCAL_ONE;";
		testRegularStatement(inputText, "set_basic");
	}

	//UPDATE

	@Test
	public void update_basic() {
		String inputText = "UPDATE table1 SET field1 = value1 WHERE field3 = value3;";
		testRegularStatement(inputText, "update_basic");
	}

	@Test
	public void explain_plan() {
		String inputText = "EXPLAIN PLAN FOR DROP INDEX indexName;";
		testRegularStatement(inputText, "explain_plan");
	}

	@Test
	public void drop_table() {
		String inputText = "DROP TABLE IF EXISTS lastTable;";
		testRegularStatement(inputText, "drop_table");
	}

	@Test
	public void update_where() {
		String inputText = "UPDATE table1 USING TTL = 400 SET field1 = value1,"
				+ " field2 = value2 WHERE field3 = value3 AND field4 = value4;";
		testRegularStatement(inputText, "update_where");
	}

	@Test
	public void update_full() {
		String inputText = "UPDATE table1 USING TTL = 400 SET field1 = value1,"
				+ " field2 = value2 WHERE field3 = value3 AND field4 = value4"
				+ " IF field5 = transaction_value5;";
		testRegularStatement(inputText, "update_full");
	}

	@Test
	public void createTable_basic() {
		String inputText = "create table adsa(algo text primary key, algo2 int, algo3 bool);";
		testRegularStatement(inputText, "createTable_basic");
	}

	@Test
	public void createTable_basic_2() {
		String inputText = "create table adsa(algo text, algo2 int primary key, algo3 bool);";
		testRegularStatement(inputText, "createTable_basic_2");
	}

	@Test
	public void createTable_basic_3() {
		String inputText = "create table adsa(algo text, algo2 int, algo3 bool primary key);";
		testRegularStatement(inputText, "createTable_basic_3");
	}

	@Test
	public void createTable_basic_4() {
		String inputText = "create table adsa(algo text, algo2 int, algo3 bool, primary key (algo));";
		testRegularStatement(inputText, "createTable_basic_4");
	}

	@Test
	public void createTable_basic_5() {
		String inputText = "create table adsa(algo text, algo2 int, algo3 bool, primary key (algo, algo2));";
		testRegularStatement(inputText, "createTable_basic_5");
	}

	@Test
	public void createTable_basic_6() {
		String inputText = "create table adsa(algo text, algo2 int, algo3 bool, primary key ((algo, algo2), algo3));";
		testRegularStatement(inputText, "createTable_basic_6");
	}

	@Test 
	public void createTable_basic_7() {
		String inputText = "create table adsa(algo text, algo2 int, algo3 bool, primary key ((algo, algo2), algo3)) "
                        + "with propiedad1=prop1 and propiedad2=2 and propiedad3=3.0;";
		testRegularStatement(inputText, "createTable_basic_7");
	} 
                
        @Test 
	public void createTable_with_many_properties() {
            String inputText = "CREATE TABLE key_space1.users(name varchar, password varchar, color varchar, gender varchar,"
                    + " food varchar, animal varchar, age int, code int, PRIMARY KEY ((name, gender), color, animal)) "
                    + "WITH compression={sstable_compression: DeflateCompressor, chunk_length_kb: 64} AND "
                    + "compaction={class: SizeTieredCompactionStrategy, min_threshold: 6} AND read_repair_chance=1.0;";
            AntlrResult antlrResult = MetaUtils.parseStatement(inputText);
            MetaStatement st = antlrResult.getStatement();
            assertNotNull("Cannot parse createTable_with_many_properties", st);

            boolean originalOK = false;
            boolean alternative1 = false;
            boolean alternative2 = false;
            boolean alternative3 = false;

            if(inputText.equalsIgnoreCase(st.toString()+";")){
                originalOK = true;
            }

            String alternative1Str = "CREATE TABLE key_space1.users(name varchar, password varchar, color varchar, gender varchar,"
                    + " food varchar, animal varchar, age int, code int, PRIMARY KEY ((name, gender), color, animal)) "
                    + "WITH compression={chunk_length_kb: 64, sstable_compression: DeflateCompressor} AND "
                    + "compaction={class: SizeTieredCompactionStrategy, min_threshold: 6} AND read_repair_chance=1.0;";
            if(alternative1Str.equalsIgnoreCase(st.toString()+";")){
                alternative1 = true;
            }
            String alternative2Str = "CREATE TABLE key_space1.users(name varchar, password varchar, color varchar, gender varchar,"
                    + " food varchar, animal varchar, age int, code int, PRIMARY KEY ((name, gender), color, animal)) "
                    + "WITH compression={sstable_compression: DeflateCompressor, chunk_length_kb: 64} AND "
                    + "compaction={min_threshold: 6, class: SizeTieredCompactionStrategy} AND read_repair_chance=1.0;";
            if(alternative2Str.equalsIgnoreCase(st.toString()+";")){
                alternative2 = true;
            }
            String alternative3Str = "CREATE TABLE key_space1.users(name varchar, password varchar, color varchar, gender varchar,"
                    + " food varchar, animal varchar, age int, code int, PRIMARY KEY ((name, gender), color, animal)) "
                    + "WITH compression={chunk_length_kb: 64, sstable_compression: DeflateCompressor} AND "
                    + "compaction={min_threshold: 6, class: SizeTieredCompactionStrategy} AND read_repair_chance=1.0;";
            if(alternative3Str.equalsIgnoreCase(st.toString()+";")){
                alternative3 = true;
            }

            assertTrue("Cannot parse createTable_with_many_properties", (originalOK || alternative1 || alternative2 || alternative3));
	}
        
        @Test 
	public void createTable_compact_storage() {
		String inputText = "CREATE TABLE key_space1.sblocks(block_id uuid, subblock_id uuid, data blob, PRIMARY KEY "
                        + "(block_id, subblock_id)) WITH COMPACT STORAGE;";
		testRegularStatement(inputText, "createTable_compact_storage");
	}
        
        @Test 
	public void createTable_clustering() {
		String inputText = "create table key_space1.timeseries(event_type text, insertion_time timestamp, event blob,"
                        + " PRIMARY KEY (event_type, insertion_time)) WITH CLUSTERING ORDER BY (insertion_time DESC);";
		testRegularStatement(inputText, "createTable_clustering");
	}
        
        @Test 
	public void createTable_with_properties() {
            String inputText = "CREATE TABLE key_space1.test(name varchar, color varchar, gender varchar, food varchar, "
                    + "animal varchar, PRIMARY KEY (name)) WITH compression={sstable_compression: DeflateCompressor, "
                    + "chunk_length_kb: 64} AND compaction={class: SizeTieredCompactionStrategy, min_threshold: 6} AND "
                    + "read_repair_chance=1.0;";
            AntlrResult antlrResult = MetaUtils.parseStatement(inputText);
            MetaStatement st = antlrResult.getStatement();
            assertNotNull("Cannot parse createTable_with_properties", st);

            boolean originalOK = false;
            boolean alternative1 = false;
            boolean alternative2 = false;
            boolean alternative3 = false;

            if(inputText.equalsIgnoreCase(st.toString()+";")){
                originalOK = true;
            }

            String alternative1Str = "CREATE TABLE key_space1.test(name varchar, color varchar, gender varchar, food varchar, "
                    + "animal varchar, PRIMARY KEY (name)) WITH compression={chunk_length_kb: 64, "
                    + "sstable_compression: DeflateCompressor} AND compaction={class: SizeTieredCompactionStrategy, min_threshold: 6} AND "
                    + "read_repair_chance=1.0;";
            if(alternative1Str.equalsIgnoreCase(st.toString()+";")){
                alternative1 = true;
            }
            String alternative2Str = "CREATE TABLE key_space1.test(name varchar, color varchar, gender varchar, food varchar, "
                    + "animal varchar, PRIMARY KEY (name)) WITH compression={sstable_compression: DeflateCompressor, "
                    + "chunk_length_kb: 64} AND compaction={min_threshold: 6, class: SizeTieredCompactionStrategy} AND "
                    + "read_repair_chance=1.0;";
            if(alternative2Str.equalsIgnoreCase(st.toString()+";")){
                alternative2 = true;
            }
            String alternative3Str = "CREATE TABLE key_space1.test(name varchar, color varchar, gender varchar, food varchar, "
                    + "animal varchar, PRIMARY KEY (name)) WITH compression={chunk_length_kb: 64, "
                    + "sstable_compression: DeflateCompressor} AND compaction={min_threshold: 6, class: SizeTieredCompactionStrategy} AND "
                    + "read_repair_chance=1.0;";
            if(alternative3Str.equalsIgnoreCase(st.toString()+";")){
                alternative3 = true;
            }

            assertTrue("Cannot parse createTable_with_properties", (originalOK || alternative1 || alternative2 || alternative3));
	}
        
	@Test
	public void alterKeyspace() {
		String inputText = "ALTER KEYSPACE mykeyspace WITH ident1 = value1 AND ident2 = 54;";
		testRegularStatement(inputText, "alterKeyspace");		
	}

	@Test
	public void dropKeyspace() {
		String inputText = "drop keyspace IF EXISTS mykeyspace;";
		testRegularStatement(inputText, "dropKeyspace");
	}

	@Test
	public void alterTable_basic() {
		String inputText = "alter table tabla1 alter column1 type int;";
		testRegularStatement(inputText, "alterTable_basic");
	} 

	@Test
	public void alterTable_basic_1() {
		String inputText = "alter table tabla1 add column1 int;";
		testRegularStatement(inputText, "alterTable_basic_1");
	} 

	@Test
	public void alterTable_basic_2() {
		String inputText = "alter table tabla1 drop column1;";
		testRegularStatement(inputText, "alterTable_basic_2");
	}

	@Test
	public void alterTable_basic_3() {
		String inputText = "Alter table tabla1 with property1=value1 and property2=2 and property3=3.0;";
		testRegularStatement(inputText, "alterTable_basic_3");
	}

	@Test
	public void stopProcess() {
		String inputText = "STOP process process1;";
		testRegularStatement(inputText, "stopProcess");
	}

	@Test
	public void createTrigger() {
		String inputText = "create trigger trigger1 on table_name USING triggerClassName;";
		testRegularStatement(inputText, "createTrigger");
	}

	@Test
	public void dropTrigger() {
            String inputText = "drop trigger trigger1 on table_name;";
            testRegularStatement(inputText, "dropTrigger");
	}

        // CASSANDRA TESTS
	public void createKeyspace_basic() {
            String inputText = "CREATE KEYSPACE testKS WITH replication = {class: SimpleStrategy, replication_factor: 1}"
                    + " AND durable_writes = false;";		
            AntlrResult antlrResult = MetaUtils.parseStatement(inputText);
            MetaStatement st = antlrResult.getStatement();
            assertNotNull("Cannot parse createKeyspace_basic", st);

            boolean originalOK = false;
            boolean alternative1 = false;

            if(inputText.equalsIgnoreCase(st.toString()+";")){
                originalOK = true;
            }

            String alternative1Str = "CREATE KEYSPACE testKS WITH replication = {replication_factor: 1, class: SimpleStrategy}"
                    + " AND durable_writes = false;";
            if(alternative1Str.equalsIgnoreCase(st.toString()+";")){
                alternative1 = true;
            }

            assertTrue("Cannot parse createKeyspace_basic", (originalOK || alternative1));    
            
            testDriverStatement(st, "createKeyspace_basic");
	}
        
	public void createTable_basic_simple() {
		String inputText = "CREATE TABLE testKS.users(name varchar, password varchar, color varchar, gender varchar,"
                        + " food varchar, animal varchar, age int, code int, PRIMARY KEY ((name, gender), color, animal));";
		testStatementWithCassandra(inputText, "createTable_basic_simple");
	}
        
	public void insert_into_simple_1() {
            String inputText = "INSERT INTO testKS.users (name, gender, color, animal, food, password, age, code)"
                    + " VALUES(pepito, male, orange, whale, plants, efg, 12, 44);";
            testStatementWithCassandra(inputText, "insert_into_simple_1");
	}
        
        public void insert_into_simple_2() {
            String inputText = "INSERT INTO testKS.users (name, gender, color, animal, food, password, age, code) "
                    + "VALUES(pepito, male, black, whale, plants, efg, 12, 44) IF NOT EXISTS USING TTL = 86400;"; 
            testStatementWithCassandra(inputText, "insert_into_simple_2");
        }
        
        public void insert_into_simple_3() {
            String inputText = "INSERT INTO testKS.users (name, gender, color, animal, food, password, age, code) "
                    + "VALUES(pepita, female, blue, dog, meat, sdh, 5, 33);"; 
            testStatementWithCassandra(inputText, "insert_into_simple_3");
        }
        
        public void insert_into_simple_4() {
            String inputText = "INSERT INTO testKS.users (name, gender, color, animal, food, password, age, code) "
                    + "VALUES(pepita, female, red, cat, mice, kdf, 3, 22);";
            testStatementWithCassandra(inputText, "insert_into_simple_4");
        }
        
        public void insert_into_simple_5() {
            String inputText = "INSERT INTO testKS.users (name, gender, color, animal, food, password, age, code) "
                    + "VALUES(pepita, female, black, fish, plankton, dfp, 3, 11);"; 
            testStatementWithCassandra(inputText, "insert_into_simple_5");
        }
        
        public void insert_into_simple_6() {
            String inputText = "INSERT INTO testKS.users (name, gender, color, animal, food, password, age, code) "
                    + "VALUES(pepito, male, green, cat, cookies, cas, 5, 55);";
            testStatementWithCassandra(inputText, "insert_into_simple_6");
        }
        
	public void select_all() {
            String inputText = "SELECT * FROM testKS.users;";
            testStatementWithCassandra(inputText, "select_all");
	}
        
	public void select_order() {
		String inputText = "SELECT name, gender, color, animal AS rage, age, food FROM testKS.users "
                        + "WHERE name = pepita AND gender = female ORDER BY color DESC LIMIT 2;";
		testStatementWithCassandra(inputText, "select_order");
	}
                        
	public void select_count() {
		String inputText = "SELECT COUNT(*) FROM testKS.users;";
		testStatementWithCassandra(inputText, "select_count");
	}
                        
	public void select_distinct() {
		String inputText = "SELECT DISTINCT name, gender FROM testKS.users;";
		testStatementWithCassandra(inputText, "select_distinct");
	}
        
	public void truncate_tableWithKeyspace() {
		String inputText = "TRUNCATE testKS.users;";
		testStatementWithCassandra(inputText, "truncate_tableWithKeyspace");
	}
        
	public void dropKeyspace_simple() {
		String inputText = "drop keyspace IF EXISTS testKS;";
		testStatementWithCassandra(inputText, "dropKeyspace_simple");
	}
        
        @Test
        public void test_cassandra_1(){
            createKeyspace_basic();
            createTable_basic_simple();
            insert_into_simple_1();
            insert_into_simple_2();
            insert_into_simple_3();
            insert_into_simple_4(); 
            insert_into_simple_5();
            insert_into_simple_6();
            select_all();
            select_order();
            select_count();
            select_distinct();
            truncate_tableWithKeyspace();
            dropKeyspace_simple();
        }
        
	// TEST META EXCEPTIONS
	@Rule
	public ExpectedException thrown = ExpectedException.none();       

	@Test
	public void update_for_invalid_assignment(){
		String inputText = "UPDATE table1 SET field1 = value1 WHERE field3: value3;";
                testMetaError(inputText);		
	}

	@Test
	public void update_wrong_spelling(){
		String inputText = "UPDDATE table1 SET field1 = value1 WHERE field3: value3;";
		testMetaError(inputText);
	}

	@Test
	public void unknown_first_word_of_statement(){
		String inputText = "WINDOWS GO HOME;";
		testMetaError(inputText);
	}

	@Test
	public void wrong_plan_token(){
		String inputText = "EXPLAIN PLAANS FOR DROP INDEX indexName;";
		testMetaError(inputText);
	}

	@Test
	public void wrong_into_token(){
		String inputText = "INSERT INTI mykeyspace.tablename (ident1, ident2) VALUES(term1, term2)"
				+ " IF NOT EXISTS USING COMPACT STORAGE AND prop1 = {innerTerm: result};";
                testRecoverableError(inputText, "wrong_into_token");
	}

	@Test
	public void create_table_wrong_column_definition(){
		String inputText = "CREATE TABLE adsa (algo text, primary key ([algo, algo2],algo3));";
		testMetaError(inputText);
	}

	@Test
	public void insert_into_wrong_values_token(){
		String inputText = "INSERT INTO mykeyspace.tablename (ident1, ident2) VALUED(term1, term2)"
				+ " IF NOT EXISTS USING COMPACT STORAGE AND prop1 = {innerTerm: result};";
		testMetaError(inputText);;
	}

	@Test
	public void create_keyspace_wrong_identifier(){
		String inputText = "CREATE KEYSPACE name.key_space1 WITH replication = replicationLevel;";
		testMetaError(inputText);
	}

	@Test
	public void truncate_wrong_identifier(){
		String inputText = "TRUNCATE companyKS..usersTable;";
		testRecoverableError(inputText, "truncate_wrong_identifier");
	}

	@Test
	public void alter_wrong_keyspace_token(){
		String inputText = "ALTER KEYSPACES mykeyspace WITH ident1 = value1;";
		testMetaError(inputText);
	}

	@Test
	public void drop_wrong_place_for_if_exists(){
		String inputText = "DROP KEYSPACE mykeyspace IF EXISTS;";
		testMetaError(inputText);
	}

	@Test
	public void set_wrong_boolean(){
		String inputText = "SET OPTIONS ANALYTICS=5;";
		testMetaError(inputText);
	}

	@Test
	public void alter_wrong_property_identifier(){
		String inputText = "ALTER TABLE tabla1 with 2property1=value1;";
		testMetaError(inputText);
	}

	@Test
	public void drop_not_missing(){
		String inputText = "DROP TABLE IF EXISTS _lastTable;";
		testRecoverableError(inputText, "drop_not_missing");
	}

	@Test
	public void create_index_wrong_option_assignment(){
		String inputText = "CREATE LUCENE INDEX index1 ON table1 (field1, field2) WITH OPTIONS opt1:val1;";
		testRecoverableError(inputText, "create_index_wrong_option_assignment");
	}

	@Test
	public void drop_index_wrong_not_word(){
		String inputText = "DROP INDEX IF NOT EXISTS index_name;";
		testRecoverableError(inputText, "drop_index_wrong_not_word");
	}

	@Test
	public void delete_wrong_property_assignment(){
		String inputText = "DELETE (col1 AND col2) FROM table1 WHERE field1: value1;";
		testRecoverableError(inputText, "delete_wrong_property_assignment");
	}

	@Test
	public void select_wrong_like_word(){
		String inputText = "SELECT ident1, myfunction(innerIdent, anotherIdent) LIKE ident1 FROM newks.newtb;";
		testMetaError(inputText);
	}

	@Test
	public void add_ending_quote_missing(){
		String inputText = "ADD \"/dir/jar_name-v1.0.jar;";
		testRecoverableError(inputText, "add_ending_quote_missing");
	}

	@Test
	public void list_reserved_word_use(){
		String inputText = "LIST PROCESS LAST;";
		testMetaError(inputText);
	}

	@Test
	public void remove_udf_not_expected_word() {
		String inputText = "REMOVE UDF \"jar.name\" NOW;";
		testMetaError(inputText);
	}

	@Test
	public void stop_wrong_process_identifier(){
		String inputText = "STOP process kstest.process1;";
		testMetaError(inputText);
	}

	@Test
	public void create_trigger_wrong_as_word_use(){
		String inputText = "create trigger trigger1 on table_name USING triggerClassName AS ident1;";
		testMetaError(inputText);
	}

	@Test
	public void drop_trigger_wrong_assignment(){
		String inputText = "drop trigger trigger1 on table_name = 20;";
		testMetaError(inputText);
	}
        
        // TEST CASSANDRA EXCEPTIONS
	public void insert_into_wrong_data_type(){   
            String inputText = "INSERT INTO testKS.users (name, gender, color, animal, food, password, age, code)"
                    + " VALUES(pepito, male, black, whale, plants, efg, twelve, 44);";
            MetaStatement st = testRegularStatement(inputText, "insert_into_wrong_data_type");
            testCassandraError(st);                       
	}
        
        @Test
	public void test_cassandra_wrong_1(){   
            createKeyspace_basic();
            createTable_basic_simple();
            insert_into_wrong_data_type();
            dropKeyspace_simple();
	}        
        
}
