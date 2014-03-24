package com.stratio.meta.sh;

import com.stratio.meta.sh.utils.MetaCompletionHandler;
import com.stratio.meta.sh.utils.MetaCompletor;
import com.stratio.meta.common.result.MetaResult;
import com.stratio.meta.sh.help.HelpContent;
import com.stratio.meta.sh.help.HelpManager;
import com.stratio.meta.sh.help.HelpStatement;
import com.stratio.meta.sh.help.generated.MetaHelpLexer;
import com.stratio.meta.sh.help.generated.MetaHelpParser;
import com.stratio.meta.driver.MetaDriver;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        
    public File retrieveHistory(ConsoleReader console, SimpleDateFormat sdf) throws IOException{
        final int DAYS_HISTORY_ENTRY_VALID = 30;
        Date today = new Date();
        File dir = new File("./src/main/resources/");
        File file = new File(dir.getPath()+"/history.txt");
        if(!dir.exists()){
            dir.mkdirs();
        }
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
                if(Days.daysBetween(new DateTime(lineDate), todayDate).getDays()<DAYS_HISTORY_ENTRY_VALID){
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
        try {
            ConsoleReader console = new ConsoleReader();
            console.setPrompt("\033[36mmetash-server>\033[0m ");
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
            File file = retrieveHistory(console, sdf);            
            
            console.setCompletionHandler(new MetaCompletionHandler());
            console.addCompleter(new MetaCompletor());  
            
            MetaDriver metaDriver = new MetaDriver();
            
            MetaResult connectionResult = metaDriver.connect();
            if(connectionResult.hasError()){
                System.out.println(connectionResult.getErrorMessage());
                return;
            }
            
            String currentKeyspace = "";
            
            String cmd = "";
            while(!cmd.toLowerCase().startsWith("exit") && !cmd.toLowerCase().startsWith("quit")){
                cmd = console.readLine();
                logger.info("\033[34;1mCommand:\033[0m " + cmd);

                if(cmd.toLowerCase().startsWith("help")){
                    showHelp(cmd);
                } else if ((!cmd.toLowerCase().equalsIgnoreCase("exit")) && (!cmd.toLowerCase().equalsIgnoreCase("quit"))){
                    MetaResult metaResult = metaDriver.executeQuery(currentKeyspace, cmd, true);
                    if(metaResult.isKsChanged()){
                        currentKeyspace = metaResult.getCurrentKeyspace();
                    }
                    metaResult.print();
                }
            }
                   
            saveHistory(console, file, sdf);            
            metaDriver.close(); 
            
        } catch (IOException ex) {
            logger.error("Cannot launch Metash, no console present", ex);
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
