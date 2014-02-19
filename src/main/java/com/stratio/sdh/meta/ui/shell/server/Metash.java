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
import com.stratio.sdh.meta.utils.AntlrResult;
import com.stratio.sdh.meta.utils.ErrorsHelper;
import com.stratio.sdh.meta.utils.MetaUtils;

public class Metash {

	/**
	 * Class logger.
	 */
	private static final Logger _logger = Logger.getLogger(Metash.class.getName());
	
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
	 * @return An AntlrResult object with the parsed Statement (if any) and the found errors (if any).
	 */
	private AntlrResult parseStatement(String inputText){
            Statement result = null;
            ANTLRStringStream input = new ANTLRStringStream(inputText);
            MetaLexer lexer = new MetaLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MetaParser parser = new MetaParser(tokens);   
            ErrorsHelper foundErrors = null;
            try {
            	result = parser.query();
                foundErrors = parser.getFoundErrors();
            } catch (RecognitionException e) {
		_logger.error("Cannot parse statement", e);
            }            
            return new AntlrResult(result, foundErrors);
	}
	
	public void loop(){
            Console input = System.console();
            if(input != null){
                String cmd = "";
                Statement stmt = null;
                //\033[4mmetash-server\033[0m> = underline 
                while((cmd = input.readLine("\033[36mmetash-server\033[0m> ")).compareTo("exit")!= 0){
                    System.out.println("\033[33mCommand: \033[0m" + cmd);
                    AntlrResult antlrResult = parseStatement(cmd);
                    stmt = antlrResult.getStatement();
                    ErrorsHelper foundErrors = antlrResult.getFoundErrors();
                    //if((stmt!=null) && (foundErrors.isEmpty())){
                        _logger.info("\033[32mExecute: \033[0m" + stmt.toString());
                    //} else {
                        MetaUtils.printParserErrors(cmd, antlrResult, true);
                        /*if(foundErrors.isEmpty()){                  
                            System.err.println("\033[31mAntlr exception: \033[0m");
                            System.err.println("\tFatal error");
                        } else {                                                        
                            System.err.println(foundErrors.toString());                              
                        }*/                        
                    //}
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
