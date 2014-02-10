package stratio.sdh.meta;

import static org.junit.Assert.*;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import com.stratio.sdh.meta.generated.MetaLexer;
import com.stratio.sdh.meta.generated.MetaParser;
import com.stratio.sdh.meta.statements.Statement;

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

	//
	//CREATE INDEX
	
	// CREATE <type_index>? INDEX (IF NOT EXISTS)? <identifier>? ON <tablename> '(' <identifier> (',' <identifier>)* ')'
    //( USING <string> )? WITH OPTIONS? (<maps> AND <maps>...) ';'
	//HASH → Usual inverted index, Hash index. (By default).
	//FULLTEXT → Full text index. 
	//CUSTOM → custom index. (new feature for release 2)
	//NUMBER → Order numeric index.(new feature for release 2)
	//GEO → Geospatial index. (new feature for release 2)
	//GEOM → Geometrical index. (new feature for release 2)
	
	@Test
	public void createIndex_hash_basic() {
		String inputText = "CREATE HASH INDEX ON table1 (field1, field2);";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse basic hash index", st);
		assertEquals("Cannot parse basic hash index", inputText, st.toString());
	}
	
	@Test
	public void createIndex_hash_ifNotExist() {
		String inputText = "CREATE HASH INDEX IF NOT EXISTS ON table1 (field1, field2);";
		Statement st = parseStatement(inputText);
		assertNotNull("Cannot parse hash index with if not exists", st);
		assertEquals("Cannot parse hash index with if not exists", inputText, st.toString());
	}

	//DROP INDEX
		//ADD
		//LIST
		//REMOVE UDF
		//
	
}
