package com.stratio.sdh.meta.ui.shell.server;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.exceptions.DriverException;
import com.stratio.sdh.meta.cassandra.CassandraClient;
import java.io.Console;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.stratio.sdh.meta.statements.MetaStatement;
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
        //String PATTERN = "%d [%p|%c] %m%n";
        String PATTERN = "%d{dd-MM-yyyy HH:mm:ss.SSS} [%p|%c{1}] %m%n";
        console.setLayout(new PatternLayout(PATTERN)); 
        console.setThreshold(Level.INFO);
        console.activateOptions();
        Logger.getRootLogger().addAppender(console);
    }

    /**
     * Parse a input text and return the equivalent Statement.
     * //@param inputText The input text.
     * //@return An AntlrResult object with the parsed Statement (if any) and the found errors (if any).
     *
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
    }*/

    public void loop(){
        Console input = System.console();
        if(input != null){
            String cmd = "";
            MetaStatement stmt = null;
            CassandraClient.connect();
            //\033[4mmetash-server\033[0m> = underline 
            while(!(cmd = input.readLine("\033[36mmetash-server\033[0m> ")).equalsIgnoreCase("exit")){
                _logger.info("\033[33mCommand: \033[0m" + cmd);
                AntlrResult antlrResult = MetaUtils.parseStatement(cmd, _logger);
                stmt = antlrResult.getStatement();
                ErrorsHelper foundErrors = antlrResult.getFoundErrors();
                if((stmt!=null) && (foundErrors.isEmpty())){
                    _logger.info("\033[32mExecute: \033[0m" + stmt.toString());
                    stmt.setQuery(cmd);                              

                    ResultSet resultSet = null;                        
                    try{
                        Statement driverStmt = stmt.getDriverStatement();
                        if(driverStmt != null){
                            resultSet = CassandraClient.executeQuery(stmt.getDriverStatement());
                        } else {
                            resultSet = CassandraClient.executeQuery(stmt.translateToCQL());                            
                        }
                        //System.out.println("Size: "+resultSet.all().size());
                        //_logger.info("\033[32mResult: \033[0m");
                    } catch (DriverException ex) {
                        _logger.error("\033[31mCassandra exception:\033[0m "+ex.getMessage()+System.getProperty("line.separator"));
                        continue;
                    }

                    _logger.info("\033[32mResult: \033[0m"+System.getProperty("line.separator")+stmt.parseResult(resultSet));

                    /*
                    for(Row row: resultSet){
                        _logger.info(row.toString());
                    }
                    */         

                    //_logger.info(System.getProperty("line.separator"));
                } else {
                    MetaUtils.printParserErrors(cmd, antlrResult, true);
                    /*if(foundErrors.isEmpty()){                  
                        System.err.println("\033[31mAntlr exception: \033[0m");
                        System.err.println("\tFatal error");
                    } else {                                                        
                        System.err.println(foundErrors.toString());                              
                    }*/                        
                }
            }
            CassandraClient.close();
        }else{
            System.err.println("Cannot launch Metash, no console present");
        }
    }

    public static void main(String[] args) {
        Metash sh = new Metash();
        sh.loop();
    }

}
