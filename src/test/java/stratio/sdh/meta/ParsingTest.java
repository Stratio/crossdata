package stratio.sdh.meta;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.Test;
import org.junit.BeforeClass;

import com.stratio.sdh.meta.generated.MetaLexer;
import com.stratio.sdh.meta.generated.MetaParser;
import com.stratio.sdh.meta.statements.CreateIndexStatement;
import com.stratio.sdh.meta.statements.Statement;
import com.stratio.sdh.meta.structures.ValueProperty;
import java.util.HashSet;
import java.util.Set;

/**
 * MetaParser tests that recognize the different options of each Statement.
 */
public class ParsingTest {

	/**
	 * Class logger.
	 */
	private static final Logger _logger = Logger.getLogger(ParsingTest.class
			.getName());
	
	/**
	 * Parse a input text and return the equivalent Statement.
	 * @param inputText The input text.
	 * @return A Statement or null if the process failed.
	 */
	private Statement parseStatement(String inputText){
		Statement result = null;
		ANTLRStringStream input = new ANTLRStringStream(inputText);
        MetaLexer lexer = new MetaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MetaParser parser = new MetaParser(tokens);   
        try {
			result = parser.query();
		} catch (RecognitionException e) {
			_logger.error("Cannot parse statement", e);
		}
        return result;
	}
	
	@BeforeClass
	//TODO Define common logging properties
	public static void initLog(){
		ConsoleAppender console = new ConsoleAppender();
		//String PATTERN = "%d [%p|%c|%C{1}] %m%n";
		String PATTERN = "%d [%p|%c] %m%n";
		console.setLayout(new PatternLayout(PATTERN)); 
		console.setThreshold(Level.INFO);
		console.activateOptions();
		Logger.getRootLogger().addAppender(console);
	}

	// CREATE KEYSPACE (IF NOT EXISTS)? <keyspace_name> WITH <properties> ';'
	@Test
	public void createKeyspace_basic() {
		String inputText = "CREATE KEYSPACE key_space1 "
				+ "WITH replication = replicationLevel AND durable_writes = false;";		
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse create keyspace - basic", st);
		//assertEquals("Cannot parse create keyspace - basic", inputText, st.toString()+";");
                //System.out.println(st.toString());
                //System.out.println(inputText);
                assertTrue("Cannot parse create keyspace - basic", inputText.equalsIgnoreCase(st.toString()+";"));
	}
	
	@Test
	public void createKeyspace_ifNotExists() {
		String inputText = "CREATE KEYSPACE IF NOT EXISTS key_space1 "
				+ "WITH replication = replicationLevel AND durable_writes = false;";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse create keyspace - If Not Exists", st);
		//assertEquals("Cannot parse create keyspace - basic", inputText, st.toString()+";");
                assertTrue("Cannot parse create keyspace - If Not Exists", inputText.equalsIgnoreCase(st.toString()+";"));
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
		Statement st = parseStatement(inputText);
                String propResultStr = st.toString().substring(st.toString().indexOf("{")+1, st.toString().indexOf("}"));
                //System.out.println(propResultStr);
                String[] str = propResultStr.split(",");
                Set<String> propertiesResult = new HashSet<>();
                for (String str1 : str) {
                    propertiesResult.add(str1.trim());
                }                                
		assertNotNull("Cannot parse create keyspace - nestedOptions", st);                
		//assertEquals("Cannot parse create keyspace - nestedOptions", inputText, st.toString()+";");
                //System.out.println(st.toString().substring(0, st.toString().indexOf("{")));
                assertEquals("Cannot parse create keyspace - nestedOptions", 
                        "CREATE KEYSPACE IF NOT EXISTS key_space1 WITH replication = {", 
                        st.toString().substring(0, st.toString().indexOf("{")+1));
                //ystem.out.println(st.toString().substring(st.toString().indexOf("}"))+";");
                assertEquals("Cannot parse create keyspace - nestedOptions", 
                        "} AND durable_writes = false;", 
                        st.toString().substring(st.toString().indexOf("}"))+";");
                //assertEquals("Cannot parse create keyspace - nestedOptions", inputText, st.toString()+";");
                assertTrue("Cannot parse create keyspace - nestedOptions", propertiesResult.containsAll(properties));
                assertTrue("Cannot parse create keyspace - nestedOptions", properties.containsAll(propertiesResult));
	}
        
        @Test
	public void update_tablename() {
		String inputText = "UPDATE tablename USING prop1 = 342 SET ident1 = term1, ident2 = term2"
                        + " WHERE ident3 IN (term3, term4) IF field1 = 25 ;";
		Statement st = parseStatement(inputText);
                //System.out.println(inputText);
                //System.out.println(st.toString()+" ;");
		assertNotNull("Cannot parse update tablename", st);
		//assertEquals("Cannot parse update tablename", inputText, st.toString()+";");
                assertTrue("Cannot parse update tablename", inputText.equalsIgnoreCase(st.toString()+" ;"));
	}
        
        @Test
	public void insert_into() {
		String inputText = "INSERT INTO mykeyspace.tablename (ident1, ident2) VALUES(term1, term2) "
                        + "IF NOT EXISTS USING COMPACT STORAGE AND prop1 = {innerTerm: result};";
		Statement st = parseStatement(inputText);
                //System.out.println(inputText);
                //System.out.println(st.toString()+";");
		assertNotNull("Cannot parse insert into", st);
		//assertEquals("Cannot parse insert into", inputText, st.toString()+";");
                assertTrue("Cannot parse insert into", inputText.equalsIgnoreCase(st.toString()+";"));
	}
	
        @Test
	public void truncate_table() {
		String inputText = "TRUNCATE usersTable;";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse truncate table", st);
		//assertEquals("Cannot parse truncate table", inputText, st.toString()+";");
                assertTrue("Cannot parse truncate table", inputText.equalsIgnoreCase(st.toString()+";"));
	}
                
	//
	//CREATE INDEX
	
	// CREATE <type_index>? INDEX (IF NOT EXISTS)? <identifier>? ON <tablename> '(' <identifier> (',' <identifier>)* ')'
    // ( USING <string> )? WITH OPTIONS? (<maps> AND <maps>...) ';'
	//HASH → Usual inverted index, Hash index. (By default).
	//FULLTEXT → Full text index. 
	//CUSTOM → custom index. (new feature for release 2)

	@Test
	public void createIndex_hash_basic() {
		String inputText = "CREATE HASH INDEX index1 ON table1 (field1, field2);";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse basic hash index", st);
		//assertEquals("Cannot parse basic hash index", inputText, st.toString()+";");
                assertTrue("Cannot parse basic hash index", inputText.equalsIgnoreCase(st.toString()+";"));
	}
	
	@Test
	public void createIndex_hash_ifNotExist() {
		String inputText = "CREATE HASH INDEX IF NOT EXISTS index1 ON table1 (field1, field2);";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse hash index with if not exists", st);
		//assertEquals("Cannot parse hash index with if not exists", inputText, st.toString()+";");
                assertTrue("Cannot parse hash index with if not exists", inputText.equalsIgnoreCase(st.toString()+";"));
	}
	
	@Test
	public void createIndex_hash_using() {
		String inputText = "CREATE HASH INDEX index1 ON table1 (field1, field2) USING com.company.Index.class;";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse hash index with using clause", st);
		//assertEquals("Cannot parse hash index with using clause", inputText, st.toString()+";");
                assertTrue("Cannot parse hash index with using clause", inputText.equalsIgnoreCase(st.toString()+";"));
	}
	
	@Test
	public void createIndex_hash_options() {
		String inputText = "CREATE HASH INDEX index1 ON table1 (field1, field2) WITH OPTIONS opt1=val1 AND opt2=val2;";
		int numberOptions = 2;
		
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse hash index with options clause", st);
		CreateIndexStatement cist = CreateIndexStatement.class.cast(st);
		//assertEquals("Cannot parse hash index with options clause - name", "index1", cist.getName());
                assertTrue("Cannot parse hash index with options clause - name", "index1".equalsIgnoreCase(cist.getName()));
		assertEquals("Cannot parse hash index with options clause - options size", numberOptions, cist.getOptions().size());
		HashMap<String, ValueProperty> options = cist.getOptions();
		
		for(int i = 1; i < numberOptions; i++){
			assertTrue("Cannot parse hash index with options clause - options opt"+i, options.containsKey("opt"+i));
			//assertEquals("Cannot parse hash index with options clause - options opt"+i, "val"+i, options.get("opt"+i).toString());
                        assertTrue("Cannot parse hash index with options clause - options opt"+i, ("val"+i).equalsIgnoreCase(options.get("opt"+i).toString()));
		}

	}
	
	@Test
	public void createIndex_hash_all() {
		String inputText = "CREATE HASH INDEX IF NOT EXISTS index1 "
				+ "ON table1 (field1, field2) USING com.company.Index.class "
				+ "WITH OPTIONS opt1=val1 AND opt2=val2;";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse hash index with options clause", st);
		CreateIndexStatement cist = CreateIndexStatement.class.cast(st);
		
		String retrieved = cist.toString().substring(0, cist.toString().indexOf("OPTIONS"));
		String expected = inputText.substring(0, inputText.indexOf("OPTIONS"));
		//assertEquals("Cannot parse hash index with using clause", expected, retrieved);
                assertTrue("Cannot parse hash index with using clause", expected.equalsIgnoreCase(retrieved));
		
		assertTrue("Cannot parse hash index with options clause - options size", cist.getOptions().size() > 0);
		HashMap<String, ValueProperty> options = cist.getOptions();
		
		assertTrue("Cannot parse hash index with options clause - options opt1", options.containsKey("opt1"));
		assertTrue("Cannot parse hash index with options clause - options opt1", options.containsKey("opt2"));
	}

	//DROP INDEX

	@Test
	public void dropIndex_basic() {
		String inputText = "DROP INDEX index_name;";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse drop index basic", st);
		//assertEquals("Cannot parse drop index basic", inputText, st.toString()+";");
                assertTrue("Cannot parse drop index basic", inputText.equalsIgnoreCase(st.toString()+";"));
	}
	
	@Test
	public void dropIndex_ifExists() {
		String inputText = "DROP INDEX IF EXISTS index_name;";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse drop index with if exists clause", st);
		//assertEquals("Cannot parse drop index with if exists clause", inputText, st.toString()+";");
                assertTrue("Cannot parse drop index with if exists clause", inputText.equalsIgnoreCase(st.toString()+";"));
	}
        
        @Test
	public void select_statement() {
		String inputText = "SELECT ident1 AS name1, myfunction(innerIdent, anotherIdent) AS functionName "
                        + "FROM newks.newtb WITH WINDOW 5 ROWS INNER JOIN tablename ON field1=field2 WHERE ident1 LIKE whatever"
                        + " ORDER BY id1 ASC GROUP BY col1 LIMIT 50 DISABLE ANALYTICS;";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse select", st);
		//assertEquals("Cannot parse select", inputText, st.toString());
                
                assertTrue("Cannot parse select", inputText.equalsIgnoreCase(st.toString()+";"));
	}
	
	//ADD
	
	@Test
	public void add_basic() {
		String inputText = "ADD \"jar_name-v1.0.jar\";";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse add jar basic", st);
		//assertEquals("Cannot parse add jar basic", inputText, st.toString()+";");
                assertTrue("Cannot parse add jar basic", inputText.equalsIgnoreCase(st.toString()+";"));
	}
	
	@Test
	public void add_relative() {
		String inputText = "ADD \"dir/jar_name-v1.0.jar\";";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse add jar with relative path", st);
		//assertEquals("Cannot parse add jar with relative path", inputText, st.toString()+";");
                assertTrue("Cannot parse add jar with relative path", inputText.equalsIgnoreCase(st.toString()+";"));
	}
	
	@Test
	public void add_absolute() {
		String inputText = "ADD \"/dir/jar_name-v1.0.jar\";";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse add jar with absolute path", st);
		//assertEquals("Cannot parse add jar with absolute path", inputText, st.toString()+";");
                assertTrue("Cannot parse add jar with absolute path", inputText.equalsIgnoreCase(st.toString()+";"));
	}
	
	//LIST
	
	@Test
	public void list_process() {
		String inputText = "LIST PROCESS;";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse list process", st);
		//assertEquals("Cannot parse list process", inputText, st.toString()+";");
                assertTrue("Cannot parse list process", inputText.equalsIgnoreCase(st.toString()+";"));
	}
	
	@Test
	public void list_udf() {
		String inputText = "LIST UDF;";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse list udf", st);
		//assertEquals("Cannot parse list udf", inputText, st.toString()+";");
                assertTrue("Cannot parse list udf", inputText.equalsIgnoreCase(st.toString()+";"));
	}
	
	@Test
	public void list_trigger() {
		String inputText = "LIST TRIGGER;";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse list trigger", st);
		//assertEquals("Cannot parse list trigger", inputText, st.toString()+";");
                assertTrue("Cannot parse list trigger", inputText.equalsIgnoreCase(st.toString()+";"));
	}
	
	//REMOVE UDF	
	@Test
	public void removeUDF() {
		String inputText = "REMOVE UDF \"jar.name\";";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse remove udf", st);
		//assertEquals("Cannot parse remove udf", inputText, st.toString()+";");
                assertTrue("Cannot parse remove udf", inputText.equalsIgnoreCase(st.toString()+";"));
	}
	
	//DELETE ( <selection> ( ',' <selection> )* )?
        //FROM <tablename>
        //WHERE <where-clause>	
	@Test
	public void delete_where() {
		String inputText = "DELETE FROM table1 WHERE field1 = value1;";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse delete - where", st);
		//assertEquals("Cannot parse delete - where", inputText, st.toString()+";");
                assertTrue("Cannot parse delete - where", inputText.equalsIgnoreCase(st.toString()+";"));
	}
	
	@Test
	public void delete_selection() {
		String inputText = "DELETE (col1, col2) FROM table1 WHERE field1 = value1;";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse delete - selection", st);
		//assertEquals("Cannot parse delete - selection", inputText, st.toString()+";");
                assertTrue("Cannot parse delete - selection", inputText.equalsIgnoreCase(st.toString()+";"));
	}
	
	@Test
	public void delete_full() {
		String inputText = "DELETE (col1, col2) FROM table1 WHERE field1 = value1 AND field2 = value2;";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse delete - full", st);
		//assertEquals("Cannot parse delete - full", inputText, st.toString()+";");
                assertTrue("Cannot parse delete - full", inputText.equalsIgnoreCase(st.toString()+";"));
	}
	
        @Test
	public void set_basic() {
		String inputText = "SET OPTIONS ANALYTICS=true AND CONSISTENCY=LOCAL_ONE;";
		Statement st = parseStatement(inputText);
                //System.out.println(inputText);
                //System.out.println(st.toString()+";");
		assertNotNull("Cannot parse set - basic", st);
		//assertEquals("Cannot parse set - basic", inputText, st.toString()+";");
                assertTrue("Cannot parse set - basic", inputText.equalsIgnoreCase(st.toString()+";"));
	}
        
	//UPDATE
	
	@Test
	public void update_basic() {
		String inputText = "UPDATE table1 SET field1 = value1 WHERE field3 = value3;";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse update - basic", st);
		//assertEquals("Cannot parse update - basic", inputText, st.toString()+";");
                assertTrue("Cannot parse update - basic", inputText.equalsIgnoreCase(st.toString()+";"));
	}
	
        @Test
	public void explain_plan() {
		String inputText = "EXPLAIN PLAN FOR DROP INDEX indexName;";
		Statement st = parseStatement(inputText);
                //System.out.println("EXPLAIN PLAN FOR DROP INDEX indexName;");
                //System.out.println(st.toString()+";");
		assertNotNull("Cannot parse explain plan", st);
		//assertEquals("Cannot parse explain plan", inputText, st.toString()+";");
                assertTrue("Cannot parse explain plan", inputText.equalsIgnoreCase(st.toString()+";"));
	}
        
        @Test
	public void drop_table() {
		String inputText = "DROP TABLE IF EXISTS lastTable;";
		Statement st = parseStatement(inputText);
                //System.out.println(inputText);
                //System.out.println(st.toString()+";");
		assertNotNull("Cannot parse drop table", st);
		//assertEquals("Cannot parse drop table", inputText, st.toString()+";");
                assertTrue("Cannot parse drop table", inputText.equalsIgnoreCase(st.toString()+";"));
	}
        
	@Test
	public void update_full() {
		String inputText = "UPDATE table1 USING TTL = 400 SET field1 = value1,"
				+ " field2 = value2 WHERE field3 = value3 AND field4 = value4;";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse update - full", st);
		//assertEquals("Cannot parse update - full", inputText, st.toString()+";");
                assertTrue("Cannot parse update - full", inputText.equalsIgnoreCase(st.toString()+";"));
	}
        
        @Test
	public void createTable_basic() {
		String inputText = "create table adsa(algo text primary key, algo2 int, algo3 bool);";
		Statement st = parseStatement(inputText);
                //String comparativeText= "Create table adsa(algo text PRIMARY KEY, algo2 int, algo3 bool)";
		assertNotNull("Cannot parse create table - basic", st);
		//assertEquals("Cannot parse create table - basic", comparativeText, st.toString());
                //System.out.println(st.toString());
                //System.out.println(inputText);
                assertTrue("Cannot parse create table - basic", inputText.equalsIgnoreCase(st.toString()+";"));
	}
        
        @Test
	public void createTable_basic_2() {
		String inputText = "create table adsa(algo text, algo2 int primary key, algo3 bool);";
		Statement st = parseStatement(inputText);
                //String comparativeText= "Create table adsa(algo text, algo2 int PRIMARY KEY, algo3 bool)";
		assertNotNull("Cannot parse create table - basic_2", st);
		//assertEquals("Cannot parse create table - basic_2", comparativeText, st.toString()+";");
                //assertEquals("Cannot parse create table - basic_2", inputText.equalsIgnoreCase(st.toString()+";"));
                //System.out.println(st.toString());
                //System.out.println(inputText);
                assertTrue("Cannot parse create table - basic_2", inputText.equalsIgnoreCase(st.toString()+";"));
	}
	
        @Test
	public void createTable_basic_3() {
		String inputText = "create table adsa(algo text, algo2 int, algo3 bool primary key);";
		Statement st = parseStatement(inputText);
                //String comparativeText= "Create table adsa(algo text, algo2 int, algo3 bool PRIMARY KEY)";
		assertNotNull("Cannot parse create table - basic_3", st);
		//assertEquals("Cannot parse create table - basic_3", comparativeText, st.toString()+";");
                //System.out.println(st.toString());
                //System.out.println(inputText);
                assertTrue("Cannot parse create table - basic_3", inputText.equalsIgnoreCase(st.toString()+";"));
	}
        
        @Test
	public void createTable_basic_4() {
		String inputText = "create table adsa(algo text, algo2 int, algo3 bool, primary key (algo));";
		Statement st = parseStatement(inputText);
                //String comparativeText= "Create table adsa(algo text, algo2 int, algo3 bool, PRIMARY KEY (algo))";
		assertNotNull("Cannot parse create table - basic_4", st);
		//assertEquals("Cannot parse create table - basic_4", comparativeText, st.toString()+";");
                //System.out.println(st.toString());
                //System.out.println(inputText);
                assertTrue("Cannot parse create table - basic_4", inputText.equalsIgnoreCase(st.toString()+";"));
	}
        
        @Test
        public void createTable_basic_5() {
		String inputText = "create table adsa(algo text, algo2 int, algo3 bool, primary key (algo, algo2));";
		Statement st = parseStatement(inputText);
                //String comparativeText= "Create table adsa(algo text, algo2 int, algo3 bool, PRIMARY KEY (algo, algo2))";
		assertNotNull("Cannot parse create table - basic_5", st);
		//assertEquals("Cannot parse create table - basic_5", comparativeText, st.toString()+";");
                //System.out.println(st.toString());
                //System.out.println(inputText);
                assertTrue("Cannot parse create table - basic_5", inputText.equalsIgnoreCase(st.toString()+";"));
	}
        
        @Test
        public void createTable_basic_6() {
		String inputText = "create table adsa(algo text, algo2 int, algo3 bool, primary key ((algo, algo2), algo3));";
		Statement st = parseStatement(inputText);
                //String comparativeText= "Create table adsa(algo text, algo2 int, algo3 bool, PRIMARY KEY ((algo, algo2), algo3))";
		assertNotNull("Cannot parse create table - basic_6", st);
		//assertEquals("Cannot parse create table - basic_6", comparativeText, st.toString()+";");
                //System.out.println(st.toString());
                //System.out.println(inputText);
                assertTrue("Cannot parse create table - basic_6", inputText.equalsIgnoreCase(st.toString()+";"));
       }
        
       @Test 
       public void createTable_basic_7() {
		String inputText = "create table adsa(algo text, algo2 int, algo3 bool, primary key ((algo, algo2), algo3)) with propiedad1=prop1 and propiedad2=2 and propiedad3=3.0;";
		Statement st = parseStatement(inputText);
                //System.out.println(inputText);
                //System.out.println(st.toString()+";");
		assertNotNull("Cannot parse create table - basic_7", st);
		//assertEquals("Cannot parse create table - basic_7", inputText, st.toString());
                assertTrue("Cannot parse create table - basic_7", inputText.equalsIgnoreCase(st.toString()+";"));
       } 
       
       @Test
       public void alterKeyspace() {
		String inputText = "ALTER KEYSPACE mykeyspace WITH ident1 = value1 AND ident2 = 54;";
		Statement st = parseStatement(inputText);
                //String comparativeText= "ALTER KEYSPACE mykeyspace WITH ident1 = value1 AND ident2 = 54";
                //assertTrue("Cannot parse alter keyspace", comparativeText.equalsIgnoreCase(st.toString()));
		assertNotNull("Cannot parse alter keyspace", st);
                //assertEquals("Cannot parse alter keyspace", comparativeText, st.toString());
                assertTrue("Cannot parse alter keyspace", inputText.equalsIgnoreCase(st.toString()+";"));		
       }
       
       @Test
       public void dropKeyspace() {
		String inputText = "drop keyspace IF EXISTS mykeyspace;";
		Statement st = parseStatement(inputText);
                //System.out.println(inputText);
                //System.out.println(st.toString()+";");
		assertNotNull("Cannot parse drop keyspace", st);
		//assertEquals("Cannot parse drop keyspace", comparativeText, st.toString());
                assertTrue("Cannot parse drop keyspace", inputText.equalsIgnoreCase(st.toString()+";"));
       }
       
       @Test
       public void alterTable_basic() {
		String inputText = "alter table tabla1 alter column1 type int;";
		Statement st = parseStatement(inputText);
                //String comparativeText= "Alter table tabla1 alter column1 type int";
		assertNotNull("Cannot parse alter table - basic", st);
		//assertEquals("Cannot parse alter table - basic", comparativeText, st.toString()+";");
                assertTrue("Cannot parse alter table - basic", inputText.equalsIgnoreCase(st.toString()+";"));
       } 
       
       @Test
       public void alterTable_basic_1() {
		String inputText = "alter table tabla1 add column1 int;";
		Statement st = parseStatement(inputText);
                //String comparativeText= "Alter table tabla1 add column1 int";
		assertNotNull("Cannot parse alter table - basic_1", st);
		//assertEquals("Cannot parse alter table - basic_1", comparativeText, st.toString()+";");
                assertTrue("Cannot parse alter table - basic_1", inputText.equalsIgnoreCase(st.toString()+";"));
       } 
       
       @Test
       public void alterTable_basic_2() {
		String inputText = "alter table tabla1 drop column1;";
		Statement st = parseStatement(inputText);
                //String comparativeText= "Alter table tabla1 drop column1";
		assertNotNull("Cannot parse alter table - basic_2", st);
		//assertEquals("Cannot parse alter table - basic_2", comparativeText, st.toString()+";");
                assertTrue("Cannot parse alter table - basic_2", inputText.equalsIgnoreCase(st.toString()+";"));
       }
       
       @Test
       public void alterTable_basic_3() {
		String inputText = "Alter table tabla1 with property1=value1 and property2=2 and property3=3.0;";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse alter table - basic_3", st);
		//assertEquals("Cannot parse alter table - basic_3", inputText, st.toString()+";");
                //System.out.println(inputText);
                //System.out.println(st.toString());
                assertTrue("Cannot parse alter table - basic_3", inputText.equalsIgnoreCase(st.toString()+";"));
       }
       
       @Test
       public void stopProcess() {
		String inputText = "STOP process process1;";
		Statement st = parseStatement(inputText);
                //String comparativeText= "Stop process process1";
		assertNotNull("Cannot parse stop process", st);
		//assertEquals("Cannot parse stop process", comparativeText, st.toString()+";");
                assertTrue("Cannot parse stop process", inputText.equalsIgnoreCase(st.toString()+";"));
       }
       
       @Test
       public void createTrigger() {
		String inputText = "create trigger trigger1 on table_name USING triggerClassName;";
		Statement st = parseStatement(inputText);
                //String comparativeText= "create trigger trigger1 on table_name USING triggerClassName";
		assertNotNull("Cannot parse create trigger", st);
		//assertEquals("Cannot parse create trigger", comparativeText, st.toString()+";");
                assertTrue("Cannot parse create trigger", inputText.equalsIgnoreCase(st.toString()+";"));
       }
       
       @Test
       public void dropTrigger() {
		String inputText = "drop trigger trigger1 on table_name;";
		Statement st = parseStatement(inputText);
                //String comparativeText= "Drop trigger trigger1 on table_name";
		assertNotNull("Cannot parse drop trigger", st);
		//assertEquals("Cannot parse drop trigger", comparativeText, st.toString()+";");
                assertTrue("Cannot parse drop trigger", inputText.equalsIgnoreCase(st.toString()+";"));
       }
}
