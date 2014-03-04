package com.stratio.meta.ui.shell.server;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.RegularStatement;
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
import com.stratio.meta.utils.AntlrError;
import com.stratio.meta.utils.DeepResult;
import com.stratio.meta.utils.MetaStep;
import com.stratio.meta.utils.ValidationException;
import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.log4j.Logger;

public class Metash {

	/**
	 * Class logger.
	 */
        private static final Logger logger = Logger.getLogger(Metash.class);
	
	private final HelpContent _help;
	
	public Metash(){
		HelpManager hm = new HelpManager();
		_help = hm.loadHelpContent();
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
            logger.error("Cannot parse statement", e);
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
            AntlrResult antlrResult = MetaUtils.parseStatement(cmd);            
            MetaStatement stmt = antlrResult.getStatement();
            ErrorsHelper foundErrors = antlrResult.getFoundErrors();
            if((stmt!=null) && (foundErrors.isEmpty())){
                logger.info("\033[32mParsed:\033[0m " + stmt.toString());
                stmt.setQuery(cmd);
                
                try{
                    stmt.validate();
                } catch(ValidationException ex){
                    logger.error("\033[31mValidation exception:\033[0m "+ex.getMessage());
                    return;
                }
                
                ResultSet resultSet = null;  
                Statement driverStmt = null;
                try{
                    driverStmt = stmt.getDriverStatement();
                    if(driverStmt != null){
                        resultSet = CassandraClient.executeQuery(driverStmt, true);
                    } else {
                        resultSet = CassandraClient.executeQuery(stmt.translateToCQL(), true);   
                    }
                } catch (DriverException | UnsupportedOperationException ex) {
                    if(ex instanceof DriverException){
                        logger.error("\033[31mCassandra exception:\033[0m "+ex.getMessage());
                    } else if (ex instanceof UnsupportedOperationException){
                        logger.error("\033[31mUnsupported operation by C*:\033[0m "+ex.getMessage());
                    }
                    error = true;
                    if(ex.getMessage().contains("line") && ex.getMessage().contains(":")){
                        String queryStr;
                        if(driverStmt != null){
                            queryStr = driverStmt.toString();
                        } else {
                            queryStr = stmt.translateToCQL();
                        }
                        String[] cMessageEx =  ex.getMessage().split(" ");
                        StringBuilder sb = new StringBuilder();
                        sb.append(cMessageEx[2]);
                        for(int i=3; i<cMessageEx.length; i++){
                            sb.append(" ").append(cMessageEx[i]);
                        }
                        AntlrError ae = new AntlrError(cMessageEx[0]+" "+cMessageEx[1], sb.toString());
                        queryStr = MetaUtils.getQueryWithSign(queryStr, ae);
                        logger.error(queryStr);
                    }
                }
                if(!error){
                    logger.info("\033[32mResult:\033[0m "+stmt.parseResult(resultSet)+System.getProperty("line.separator"));
                } else {
                    List<MetaStep> steps = stmt.getPlan();
                    for(MetaStep step: steps){
                        logger.info(step.getPath()+"-->"+step.getQuery());
                    }
                    DeepResult deepResult = stmt.executeDeep();
                    if(deepResult.hasErrors()){
                        logger.error("\033[31mUnsupported operation by Deep:\033[0m "+deepResult.getErrors()+System.getProperty("line.separator"));
                    } else {
                        logger.info("\033[32mResult:\033[0m "+deepResult.getResult()+System.getProperty("line.separator"));
                    }
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
                cmd = input.readLine("\033[36mmetash-server>\033[0m ");                                    
                logger.info("\033[33mCommand:\033[0m " + cmd);
                
                if(cmd.startsWith("help")){
                    showHelp(cmd);
                }else if (!cmd.equalsIgnoreCase("exit")){
                    executeMetaCommand(cmd);
                } 
            }
            CassandraClient.close();
        }else{
            logger.error("Cannot launch Metash, no console present");
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
