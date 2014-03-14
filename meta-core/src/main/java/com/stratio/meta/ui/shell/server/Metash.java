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

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.exceptions.DriverException;
import com.stratio.meta.statements.SelectStatement;
import com.stratio.meta.utils.AntlrError;
import com.stratio.meta.utils.DeepResult;
import com.stratio.meta.utils.MetaStep;
import com.stratio.meta.utils.ValidationException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import jline.console.ConsoleReader;
import jline.console.history.History;
import jline.console.history.History.Entry;
import jline.console.history.MemoryHistory;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;

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
                     
        private void executeMetaCommand(String cmd, boolean selectWithFiltering){
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
                
                if(selectWithFiltering){
                    if(stmt instanceof SelectStatement){
                        SelectStatement selectStmt = (SelectStatement) stmt;
                        selectStmt.setNeedsAllowFiltering(selectWithFiltering);
                        stmt = selectStmt;
                    } else {
                        return;
                    }
                }
                
                try{
                    driverStmt = stmt.getDriverStatement();
                    if(driverStmt != null){
                        resultSet = CassandraClient.executeQuery(driverStmt, true);
                    } else {
                        resultSet = CassandraClient.executeQuery(stmt.translateToCQL(), true);   
                    }
                } catch (DriverException | UnsupportedOperationException ex) {
                    Exception e = ex;
                    if(ex instanceof DriverException){
                        logger.error("\033[31mCassandra exception:\033[0m "+ex.getMessage());
                        if(ex.getMessage().contains("ALLOW FILTERING")){
                            logger.info("Executing again including ALLOW FILTERING");
                            executeMetaCommand(cmd, true);
                            return;
                        }
                    } else if (ex instanceof UnsupportedOperationException){
                        logger.error("\033[31mUnsupported operation by C*:\033[0m "+ex.getMessage());
                    }
                    error = true;
                    if(e.getMessage().contains("line") && e.getMessage().contains(":")){
                        String queryStr;
                        if(driverStmt != null){
                            queryStr = driverStmt.toString();
                        } else {
                            queryStr = stmt.translateToCQL();
                        }
                        String[] cMessageEx =  e.getMessage().split(" ");
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
        
	private void executeMetaCommand(String cmd){
            executeMetaCommand(cmd, false);
	}
        
    public File retrieveHistory(ConsoleReader console, int days, SimpleDateFormat sdf) throws IOException{
        Date today = new Date();
        File file = new File("./src/main/resources/history.txt");
        if (!file.exists()){
            file.createNewFile();
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
        try {
            History oldHistory = new MemoryHistory();                                
            DateTime todayDate = new DateTime(today);
            String line;
            String[] lineArray;
            Date lineDate;
            String lineStatement;
            while ((line = br.readLine()) != null) {
                lineArray = line.split("\\|");
                lineDate = sdf.parse(lineArray[0]);
                if(Days.daysBetween(new DateTime(lineDate), todayDate).getDays()<days){
                    lineStatement = lineArray[1];
                    oldHistory.add(lineStatement);
                }
            }
            console.setHistory(oldHistory);
            return file;
        } catch (ParseException ex) {
            logger.error("Cannot retrieve previous history");
            logger.error(ex);
            return null;
        }
    }

    public void saveHistory(ConsoleReader console, File file, SimpleDateFormat sdf) throws IOException{
        if (!file.exists()) {
            file.createNewFile();
        }        
        FileWriter fileWritter = new FileWriter(file, true);            
        try (BufferedWriter bufferWritter = new BufferedWriter(fileWritter)) {
            History history = console.getHistory();
            ListIterator<Entry> histIter = history.entries();                                 
            while(histIter.hasNext()){
                Entry entry = histIter.next();          
                bufferWritter.write(sdf.format(new Date()));
                bufferWritter.write("|");
                bufferWritter.write(entry.value().toString());
                bufferWritter.newLine();
            }
            bufferWritter.flush();
        }
    }
    
    /**
     * Shell loop that receives user commands until a {@code exit} or {@code quit} command
     * is introduced.
     */
    public void loop(){
        final int DAYS_HISTORY_ENTRY_VALID = 30;
        try {
            ConsoleReader console = new ConsoleReader();
            console.setPrompt("\033[36mmetash-server>\033[0m ");
            
            /// RETRIEVE HISTORY  
            SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
            File file = retrieveHistory(console, DAYS_HISTORY_ENTRY_VALID, sdf);            
            //////
            
            console.setCompletionHandler(new MetaCompletionHandler());
            console.addCompleter(new MetaCompletor());            
            String cmd = "";
            CassandraClient.connect();
            while(!cmd.toLowerCase().startsWith("exit") && !cmd.toLowerCase().startsWith("quit")){
                cmd = console.readLine();
                logger.info("\033[34;1mCommand:\033[0m " + cmd);

                if(cmd.toLowerCase().startsWith("help")){
                    showHelp(cmd);
                } else if ((!cmd.toLowerCase().equalsIgnoreCase("exit")) && (!cmd.toLowerCase().equalsIgnoreCase("quit"))){
                    executeMetaCommand(cmd); 
                }
            }
            
            /// SAVE HISTORY             
            saveHistory(console, file, sdf);
            //////
            
            CassandraClient.close(); 
        } catch (IOException ex) {
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
