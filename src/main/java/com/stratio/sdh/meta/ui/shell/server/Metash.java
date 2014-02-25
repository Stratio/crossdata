package com.stratio.sdh.meta.ui.shell.server;

import java.io.Console;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.stratio.meta.client.help.HelpContent;
import com.stratio.meta.client.help.HelpManager;
import com.stratio.meta.client.help.HelpStatement;
import com.stratio.meta.client.help.generated.MetaHelpLexer;
import com.stratio.meta.client.help.generated.MetaHelpParser;
import com.stratio.sdh.meta.generated.MetaLexer;
import com.stratio.sdh.meta.generated.MetaParser;
import com.stratio.sdh.meta.statements.Statement;

public class Metash {

	/**
	 * Class logger.
	 */
	private static final Logger _logger = Logger.getLogger(Metash.class
			.getName());
	
	private final HelpContent _help;
	
	public Metash(){
		initLog();
		HelpManager hm = new HelpManager();
		_help = hm.loadHelpContent();
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
	 * Parse a input text and return the equivalent HelpStatement.
	 * @param inputText The input text.
	 * @return A Statement or null if the process failed.
	 */
	private HelpStatement parseHelp(String inputText){
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
	
	/**
	 * Show the help associated with a query.
	 * @param inputText The help query.
	 */
	private void showHelp(String inputText){
		HelpStatement h = parseHelp(inputText);
		System.out.println(_help.searchHelp(h.getType()));
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
	
	/**
	 * Shell loop that receives user commands until a {@code exit} or {@code quit} command
	 * is introduced.
	 */
	public void loop(){
		Console input = System.console();
		if(input != null){
			String cmd = "";
			Statement stmt = null;
			
			while(!cmd.startsWith("exit") && !cmd.startsWith("quit")){
				cmd = input.readLine("metash-server> ");
				//System.out.println("Command: " + cmd);
				if(cmd.startsWith("help")){
					showHelp(cmd);
				}else if((stmt = parseStatement(cmd))!=null){
					_logger.info("Execute: " + stmt.toString());
				}
			}
		}else{
			System.err.println("Cannot launch Metash, no console present");
		}
	}
	
	/**
	 * Launch the META server shell.
	 * @param args The list of arguments. Not supported at the moment.
	 */
	public static void main(String[] args) {
		Metash sh = new Metash();
		sh.loop();
	}

}
