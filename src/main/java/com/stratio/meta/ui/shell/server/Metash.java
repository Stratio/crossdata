package com.stratio.meta.ui.shell.server;

import com.stratio.meta.cassandra.CassandraClient;
import com.stratio.meta.client.help.HelpContent;
import com.stratio.meta.client.help.HelpManager;
import com.stratio.meta.client.help.HelpStatement;
import com.stratio.meta.client.help.generated.MetaHelpLexer;
import com.stratio.meta.client.help.generated.MetaHelpParser;
import com.stratio.meta.statements.MetaStatement;
import com.stratio.meta.utils.AntlrResult;
import com.stratio.meta.utils.ErrorsHelper;
import com.stratio.meta.utils.MetaUtils;

import java.io.Console;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.exceptions.DriverException;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

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
		//String PATTERN = "%d [%p|%c] %m%n";
		String PATTERN = "%d{dd-MM-yyyy HH:mm:ss.SSS} [%p|%c{1}] %m%n";
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
		


	
	private void executeMetaCommand(String cmd){
		boolean error = false;
        AntlrResult antlrResult = MetaUtils.parseStatement(cmd, _logger);
        MetaStatement stmt = antlrResult.getStatement();
        ErrorsHelper foundErrors = antlrResult.getFoundErrors();
        if((stmt!=null) && (foundErrors.isEmpty())){
            _logger.info("\033[32mExecute: \033[0m" + stmt.toString());
            stmt.setQuery(cmd);                              

            ResultSet resultSet = null;                        
            try{
                Statement driverStmt = stmt.getDriverStatement();
                if(driverStmt != null){
                    resultSet = CassandraClient.executeQuery(driverStmt);
                } else {
                    resultSet = CassandraClient.executeQuery(stmt.translateToCQL());                            
                }
            } catch (DriverException ex) {
                _logger.error("\033[31mCassandra exception:\033[0m "+ex.getMessage()+System.getProperty("line.separator"));
                error = true;
            }
            
            if(!error){
            	_logger.info("\033[32mResult: \033[0m"+stmt.parseResult(resultSet));
            }
            
        } else {
            MetaUtils.printParserErrors(cmd, antlrResult, true);                        
        }
	}

	/**
	 * Shell loop that receives user commands until a {@code exit} or {@code quit} command
	 * is introduced.
	 */
    public void loop(){
        Console input = System.console();
        if(input != null){
            String cmd = "";
            CassandraClient.connect();
            while(!cmd.startsWith("exit") && !cmd.startsWith("quit")){
				cmd = input.readLine("\033[36mmetash-server\033[0m> ");
                _logger.info("\033[33mCommand: \033[0m" + cmd);
                
                if(cmd.startsWith("help")){
                    showHelp(cmd);
                }else if (!cmd.equalsIgnoreCase("exit")){
                    executeMetaCommand(cmd);
                } 
            }
            CassandraClient.close();
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
