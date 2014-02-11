package stratio.sdh.meta;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.log4j.Logger;
import org.junit.Test;

import com.stratio.sdh.meta.generated.MetaLexer;
import com.stratio.sdh.meta.generated.MetaParser;
import com.stratio.sdh.meta.statements.CreateIndexStatement;
import com.stratio.sdh.meta.statements.Statement;
import com.stratio.sdh.meta.structures.ValueProperty;

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

	// CREATE KEYSPACE (IF NOT EXISTS)? <keyspace_name> WITH <properties> ';'
	@Test
	public void createKeyspace_basic() {
		String inputText = "CREATE KEYSPACE key_space1 "
				+ "WITH replication = replicationLevel AND durable_writes = false;";
		
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse create keyspace - basic", st);
		assertEquals("Cannot parse create keyspace - basic", inputText, st.toString()+";");
	}
	
	@Test
	public void createKeyspace_ifNotExists() {
		String inputText = "CREATE KEYSPACE IF NOT EXISTS key_space1 "
				+ "WITH replication = replicationLevel AND durable_writes = false;";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse create keyspace - basic", st);
		assertEquals("Cannot parse create keyspace - basic", inputText, st.toString()+";");
	}
	
	//TODO: Should we support it in this way?
	//@Test
	public void createKeyspace_nestedOptions() {
		String inputText = "CREATE KEYSPACE IF NOT EXISTS key_space1 "
				+ "WITH replication = {class: NetworkTopologyStrategy, DC1 : 1, DC2 : 3} "
				+"AND durable_writes = false;";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse create keyspace - basic", st);
		assertEquals("Cannot parse create keyspace - basic", inputText, st.toString()+";");
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
		assertEquals("Cannot parse basic hash index", inputText, st.toString()+";");
	}
	
	@Test
	public void createIndex_hash_ifNotExist() {
		String inputText = "CREATE HASH INDEX IF NOT EXISTS index1 ON table1 (field1, field2);";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse hash index with if not exists", st);
		assertEquals("Cannot parse hash index with if not exists", inputText, st.toString()+";");
	}
	
	@Test
	public void createIndex_hash_using() {
		String inputText = "CREATE HASH INDEX index1 ON table1 (field1, field2) USING com.company.Index.class;";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse hash index with using clause", st);
		assertEquals("Cannot parse hash index with using clause", inputText, st.toString()+";");
	}
	
	@Test
	public void createIndex_hash_options() {
		String inputText = "CREATE HASH INDEX index1 ON table1 (field1, field2) WITH OPTIONS opt1=val1 AND opt2=val2;";
		int numberOptions = 2;
		
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse hash index with options clause", st);
		CreateIndexStatement cist = CreateIndexStatement.class.cast(st);
		assertEquals("Cannot parse hash index with options clause - name", "index1", cist.getName());
		assertEquals("Cannot parse hash index with options clause - options size", numberOptions, cist.getOptions().size());
		HashMap<String, ValueProperty> options = cist.getOptions();
		
		for(int i = 0; i < numberOptions; i++){
			assertTrue("Cannot parse hash index with options clause - options opt"+i, options.containsKey("opt"+i));
			assertEquals("Cannot parse hash index with options clause - options opt"+i, "val"+i, options.get("opt"+i).toString());
			
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
		assertEquals("Cannot parse hash index with using clause", expected, retrieved);
		
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
		assertEquals("Cannot parse drop index basic", inputText, st.toString()+";");
	}
	
	@Test
	public void dropIndex_ifExists() {
		String inputText = "DROP INDEX IF EXISTS index_name;";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse drop index with if exists clause", st);
		assertEquals("Cannot parse drop index with if exists clause", inputText, st.toString()+";");
	}
	
	//ADD
	
	@Test
	public void add_basic() {
		String inputText = "ADD \"jar_name-v1.0.jar\";";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse add jar basic", st);
		assertEquals("Cannot parse add jar basic", inputText, st.toString()+";");
	}
	
	@Test
	public void add_relative() {
		String inputText = "ADD \"dir/jar_name-v1.0.jar\";";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse add jar with relative path", st);
		assertEquals("Cannot parse add jar with relative path", inputText, st.toString()+";");
	}
	
	@Test
	public void add_absolute() {
		String inputText = "ADD \"/dir/jar_name-v1.0.jar\";";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse add jar with absolute path", st);
		assertEquals("Cannot parse add jar with absolute path", inputText, st.toString()+";");
	}
	
	//LIST
	
	@Test
	public void list_process() {
		String inputText = "LIST PROCESS;";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse list process", st);
		assertEquals("Cannot parse list process", inputText, st.toString()+";");
	}
	
	@Test
	public void list_udf() {
		String inputText = "LIST UDF;";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse list udf", st);
		assertEquals("Cannot parse list udf", inputText, st.toString()+";");
	}
	
	@Test
	public void list_trigger() {
		String inputText = "LIST TRIGGER;";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse list trigger", st);
		assertEquals("Cannot parse list trigger", inputText, st.toString()+";");
	}
	
	//REMOVE UDF
	
	@Test
	public void removeUDF() {
		String inputText = "REMOVE UDF \"jar.name\";";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse remove udf", st);
		assertEquals("Cannot parse remove udf", inputText, st.toString()+";");
	}
	
}
