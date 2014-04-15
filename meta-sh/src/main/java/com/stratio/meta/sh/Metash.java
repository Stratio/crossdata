/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.sh;

import com.stratio.meta.common.result.Result;
import com.stratio.meta.driver.BasicDriver;
import com.stratio.meta.sh.help.HelpContent;
import com.stratio.meta.sh.help.HelpManager;
import com.stratio.meta.sh.help.HelpStatement;
import com.stratio.meta.sh.help.generated.MetaHelpLexer;
import com.stratio.meta.sh.help.generated.MetaHelpParser;
import com.stratio.meta.sh.utils.ConsoleUtils;
import com.stratio.meta.sh.utils.MetaCompletionHandler;
import com.stratio.meta.sh.utils.MetaCompletor;
import jline.console.ConsoleReader;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class Metash {

    /**
     * Class logger.
     */
    private static final Logger logger = Logger.getLogger(Metash.class);
    private static final String user = "TEST_USER";

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

    /**
     * Shell loop that receives user commands until a {@code exit} or {@code quit} command
     * is introduced.
     */
    public void loop(){        
        try {
            ConsoleReader console = new ConsoleReader();
            console.setPrompt("\033[36mmetash-sh:"+System.getProperty("user.name")+">\033[0m ");
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
            File file = ConsoleUtils.retrieveHistory(console, sdf);
            
            console.setCompletionHandler(new MetaCompletionHandler());
            console.addCompleter(new MetaCompletor());  
            
            //MetaDriver metaDriver = new MetaDriver();
            BasicDriver metaDriver = new BasicDriver();
            
            Result connectionResult = metaDriver.connect(user);
            if(connectionResult.hasError()){
                logger.error(connectionResult.getErrorMessage());
                return;
            }
            logger.info("Driver connections established");
            logger.info(ConsoleUtils.stringResult(connectionResult));
            
            String currentKeyspace = "";
            
            String cmd = "";
            while(!cmd.toLowerCase().startsWith("exit") && !cmd.toLowerCase().startsWith("quit")){
                cmd = console.readLine();
                if(cmd.toLowerCase().trim().startsWith("random")){
                    insertRandomData(cmd.toLowerCase().trim(), metaDriver);
                    continue;
                }
                if(cmd.equalsIgnoreCase("") || cmd.equalsIgnoreCase(System.lineSeparator())){
                    System.out.println();
                    continue;
                }
                System.out.println("\033[34;1mCommand:\033[0m " + cmd);
                try {
                    if(cmd.toLowerCase().startsWith("help")){
                        showHelp(cmd);
                    } else if ((!cmd.toLowerCase().equalsIgnoreCase("exit")) && (!cmd.toLowerCase().equalsIgnoreCase("quit"))){

                        long queryStart = System.currentTimeMillis();
                        Result metaResult = metaDriver.executeQuery(user, currentKeyspace, cmd);
                        long queryEnd = System.currentTimeMillis();

                        if(metaResult.isKsChanged()){
                            currentKeyspace = metaResult.getCurrentKeyspace();
                            if(currentKeyspace.isEmpty()){
                                console.setPrompt("\033[36mmetash-sh:"+System.getProperty("user.name")+">\033[0m ");
                            } else {
                                console.setPrompt("\033[36mmetash-sh:"+System.getProperty("user.name")+":"+currentKeyspace+">\033[0m ");
                            }
                        }
                        if(metaResult.hasError()){
                            System.err.println("\033[31mError:\033[0m "+metaResult.getErrorMessage());
                            continue;
                        }

                        System.out.println("\033[32mResult:\033[0m "+ ConsoleUtils.stringResult(metaResult));
                        System.out.println("Response time: "+((queryEnd-queryStart)/1000)+" seconds");
                        System.out.println("Display time: "+((System.currentTimeMillis()-queryEnd)/1000)+" seconds");
                    }
                } catch(Exception exc){
                    System.err.println("\033[31mError:\033[0m "+exc.getMessage());
                }
            }
            ConsoleUtils.saveHistory(console, file, sdf);
            logger.info("History saved");
            metaDriver.close(); 
            logger.info("Driver connections closed");
        } catch (IOException ex) {
            logger.error("Cannot launch Metash, no console present", ex);
        }
    }

    private void insertRandomData(String cmd, BasicDriver metaDriver) {
        int limit = 0;
        String[] namesGroup = {"Max", "Molly", "Buddy", "Bella", "Jake", "Lucy", "Bailey", "Maggie",
                "Rocky", "Daisy", "Charlie", "Sadie", "Jack", "Chloe", "Toby", "Sophie",
                "Cody", "Bailey", "Buster", "Zoe", "Duke", "Lola", "Cooper", "Abby"};
        String[] gender = {"Male", "Female"};
        if(cmd.contains(";")){
            cmd = cmd.replace(";", "").trim();
        }
        limit = Integer.parseInt(cmd.split(" ", 3)[2]);
        if(cmd.startsWith("random clients")){

            for(int i=0; i<limit; i++){
                int random = (int) (Math.random()*99.0);
                cmd = "INSERT INTO demo.clients (alias, address, comment, year, previous) VALUES " +
                        "('"+ RandomStringUtils.randomAlphabetic(3).toLowerCase() +"', " +
                        "'"+ RandomStringUtils.randomAlphabetic(15) +"', " +
                        "'"+ RandomStringUtils.randomAlphabetic(15) +"', "
                        + ((int) (Math.random()*2014.0)) +", "
                        + ((random % 2) == 0) +");";
                metaDriver.executeQuery(user, "", cmd);
            }
        }
        if(cmd.startsWith("random customers")){

            for(int i=0; i<limit; i++){
                int random = (int) (Math.random()*99.00);
                cmd = "INSERT INTO demo.customers (name, job, origin, value, gender) VALUES " +
                        "('"+ RandomStringUtils.randomAlphabetic(3).toLowerCase() +"', " +
                        "'"+ RandomStringUtils.randomAlphabetic(10) +"', " +
                        "'"+ RandomStringUtils.randomAlphabetic(12) +"', " +
                        ((int) (Math.random()*9000.0))+", " +
                        "'"+gender[random % 2]+"');";
                metaDriver.executeQuery(user, "", cmd);
            }
        }
    }

    /**
     * Launch the META server shell.
     * @param args The list of arguments. Not supported at the moment.
     */
    public static void main(String[] args) {
        Metash sh = new Metash();
        sh.loop();
        System.exit(0);
    }

}
