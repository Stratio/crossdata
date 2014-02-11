package com.stratio.sdh.meta.ui.shell.server;

import java.io.Console;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.stratio.sdh.meta.generated.MetaLexer;
import com.stratio.sdh.meta.generated.MetaParser;
import com.stratio.sdh.meta.statements.Statement;

public class Metash {

	/**
	 * Class logger.
	 */
	private static final Logger _logger = Logger.getLogger(Metash.class
			.getName());
	
	public Metash(){
		initLog();
	}

	private void initLog(){
		ConsoleAppender console = new ConsoleAppender();
		//String PATTERN = "%d [%p|%c|%C{1}] %m%n";
		String PATTERN = "%d [%p|%c] %m%n";
		console.setLayout(new PatternLayout(PATTERN)); 
		console.setThreshold(Level.INFO);
		console.activateOptions();
		Logger.getRootLogger().addAppender(console);
	}
	
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
	
	public void loop(){
		Console input = System.console();
		if(input != null){
			String cmd = "";
			Statement stmt = null;
			while((cmd = input.readLine("metash-server> ")).compareTo("exit")!= 0){
				System.out.println("Command: " + cmd);
				if((stmt = parseStatement(cmd))!=null){
					_logger.info("Execute: " + stmt.toString());
				}
				
			}
		}else{
			System.err.println("Cannot launch Metash, no console present");
		}
	}
	
	public static void main(String[] args) {
		Metash sh = new Metash();
		sh.loop();
	}

}
