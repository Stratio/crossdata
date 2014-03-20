package com.stratio.meta.sh.help;

import static org.junit.Assert.*;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

import com.stratio.meta.sh.help.generated.MetaHelpLexer;
import com.stratio.meta.sh.help.generated.MetaHelpParser;
import org.apache.log4j.Logger;

public class MetaHelpParserTest {

	/**
	 * Class logger.
	 */
	private final Logger logger = Logger.getLogger(MetaHelpParserTest.class);
	
	/**
	 * Parse a input text and return the equivalent HelpStatement.
	 * @param inputText The input text.
	 * @return A Statement or null if the process failed.
	 */
	private HelpStatement parseStatement(String inputText){
		HelpStatement result = null;
		ANTLRStringStream input = new ANTLRStringStream(inputText);
        MetaHelpLexer lexer = new MetaHelpLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MetaHelpParser parser = new MetaHelpParser(tokens);   
        try {
			result = parser.query();
		} catch (RecognitionException e) {
			logger.error("Cannot parse statement", e);
		}
        return result;
	}       
	
	@Test
	public void help() {
		String inputText = "HELP;";
		String expectedText = "HELP CONSOLE_HELP";
		HelpStatement st = parseStatement(inputText);
		assertNotNull("Cannot parse help - basic", st);
		assertEquals("Cannot parse help - basic", expectedText, st.toString());
	}
	
	@Test
	public void help_types() {
		String [][] types = {
				{"exit", "EXIT"},
				{"quit", "EXIT"},
				{"datatypes", "DATATYPES"},
				{"create", "CREATE"},
				{"create keyspace", "CREATE_KEYSPACE"},
				{"create table", "CREATE_TABLE"},
				{"create index","CREATE_INDEX"},
				{"create default index","CREATE_INDEX"},
				{"create lucene index","CREATE_LUCENE_INDEX"},
				{"update","UPDATE"},
				{"insert","INSERT_INTO"},
				{"insert into","INSERT_INTO"},
				{"truncate","TRUNCATE"},
				{"drop","DROP"},
				{"drop index","DROP_INDEX"},
				{"drop table","DROP_TABLE"},
				{"drop keyspace","DROP_KEYSPACE"},
				{"drop trigger","DROP_TRIGGER"},
				{"select","SELECT"},
				{"add","ADD"},
				{"list","LIST"},
				{"list process","LIST_PROCESS"},
				{"list udf","LIST_UDF"},
				{"list trigger","LIST_TRIGGER"},
				{"remove udf","REMOVE_UDF"},
				{"delete","DELETE"},
				{"set options","SET_OPTIONS"},
				{"explain plan","EXPLAIN_PLAN"},
				{"alter","ALTER"},
				{"alter keyspace","ALTER_KEYSPACE"},
				{"alter table","ALTER_TABLE"},
				{"stop","STOP"},
		};

		for(String [] test : types){
			HelpStatement st = parseStatement("HELP " + test[0] + ";");
			assertNotNull("Cannot parse help - type " + test[1], st);
			assertEquals("Cannot parse help - basic", "HELP "+test[1], st.toString());
		}
	}

}
