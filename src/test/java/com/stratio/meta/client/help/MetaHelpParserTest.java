package com.stratio.meta.client.help;

import static org.junit.Assert.*;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.BeforeClass;
import org.junit.Test;

import com.stratio.meta.client.help.generated.MetaHelpLexer;
import com.stratio.meta.client.help.generated.MetaHelpParser;


public class MetaHelpParserTest {

	/**
	 * Class logger.
	 */
	private static final Logger _logger = Logger.getLogger(MetaHelpParserTest.class
			.getName());
	
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
