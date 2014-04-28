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
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Interactive META console.
 */
public class Metash {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(Metash.class);

    /**
     * Default user to connect to the meta server.
     */
    private static final String DEFAULT_USER = "META_USER";

    /**
     * Help content to be shown when the internal command {@code help} is used.
     */
    private final HelpContent help;

    /**
     * Console reader.
     */
    private ConsoleReader console = null;

    /**
     * History file.
     */
    private File historyFile = null;

    /**
     * Current active user in the system.
     */
    private String currentUser = null;

    /**
     * Current keyspace from the point of view of the user session.
     */
    private String currentKeyspace = "";

    /**
     * Driver that connects to the META servers.
     */
    private BasicDriver metaDriver = null;

    /**
     * History date format.
     */
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy");

    /**
     * Class constructor.
     */
    public Metash(){
        HelpManager hm = new HelpManager();
        help = hm.loadHelpContent();
        initialize();
    }

    /**
     * Initialize the console settings.
     */
    private void initialize(){

        //Take the username from the system.
        currentUser = System.getProperty("user.name");
        if(currentUser == null){
            currentUser = DEFAULT_USER;
        }
        LOG.debug("Connecting with user: " + currentUser);

        try {
            console = new ConsoleReader();
            setPrompt(null);
            historyFile = ConsoleUtils.retrieveHistory(console, dateFormat);

            console.setCompletionHandler(new MetaCompletionHandler());
            console.addCompleter(new MetaCompletor());
        } catch (IOException e) {
            LOG.error("Cannot create a console.", e);
        }
    }

    /**
     * Print a message on the console.
     * @param msg The message.
     */
    private void println(String msg){
        try {
            console.getOutput().write(msg + System.lineSeparator());
        } catch (IOException e) {
            LOG.error("Cannot print to console.", e);
        }
    }

    /**
     * Set the console prompt.
     * @param currentKeyspace The currentKeyspace.
     */
    private void setPrompt(String currentKeyspace){
        StringBuilder sb = new StringBuilder("\033[36mmetash-sh:");
        if(currentKeyspace == null) {
            sb.append(currentUser);
        }else{
            sb.append(currentUser);
            sb.append(":");
            sb.append(currentKeyspace);
        }
        sb.append(">\033[0m ");
        console.setPrompt(sb.toString());
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
            LOG.error("Cannot parse statement", e);
        }
        return result;
    }

    /**
     * Show the help associated with a query.
     * @param inputText The help query.
     */
    private void showHelp(String inputText){
        HelpStatement h = parseHelp(inputText);
        println(help.searchHelp(h.getType()));
    }

    /**
     * Execute a query on the remote META servers.
     * @param cmd The query.
     */
    private void executeQuery(String cmd){

        LOG.debug("Command: " + cmd);
        long queryStart = System.currentTimeMillis();
        Result metaResult = metaDriver.executeQuery(currentUser, currentKeyspace, cmd);
        long queryEnd = System.currentTimeMillis();

        if(metaResult.isKsChanged()){
            currentKeyspace = metaResult.getCurrentKeyspace();
            if(!currentKeyspace.isEmpty()){
                setPrompt(currentKeyspace);
            }
        }

        if(metaResult.hasError()){
            println("\033[31mError:\033[0m " + metaResult.getErrorMessage());
        }else {
            println("\033[32mResult:\033[0m " + ConsoleUtils.stringResult(metaResult));
            println("Response time: " + ((queryEnd - queryStart) / 1000) + " seconds");
        }
    }

    /**
     * Establish the connection with the META servers.
     * @return Whether the connection has been successfully established.
     */
    public boolean connect(){
        boolean result = true;
        metaDriver = new BasicDriver();
        Result connectionResult = metaDriver.connect(currentUser);
        if(connectionResult.hasError()){
            LOG.error(connectionResult.getErrorMessage());
            result = false;
        }
        LOG.info("Driver connections established");
        LOG.info(ConsoleUtils.stringResult(connectionResult));

        return result;
    }

    /**
     * Close the underlying driver and save the user history.
     */
    public void closeConsole(){
        try{
            ConsoleUtils.saveHistory(console, historyFile, dateFormat);
            LOG.debug("History saved");

            metaDriver.close();
            LOG.info("Driver connections closed");

        } catch (IOException ex) {
            LOG.error("Cannot save user history", ex);
        }
    }

    /**
     * Shell loop that receives user commands until a {@code exit} or {@code quit} command
     * is introduced.
     */
    public void loop(){
        try {
            String cmd = "";

            while(!cmd.toLowerCase().startsWith("exit") && !cmd.toLowerCase().startsWith("quit")){  
                cmd += " "+console.readLine();
                if(cmd.trim().endsWith(";")){
                    if(" ".equalsIgnoreCase(cmd) || System.lineSeparator().equalsIgnoreCase(cmd)){
                        println("");
                    }else if(cmd.toLowerCase().startsWith("help")){
                        showHelp(cmd);
                    }else if(!cmd.toLowerCase().startsWith("exit") && !cmd.toLowerCase().startsWith("quit")){
                        executeQuery(cmd);
                    }
                    cmd = "";
                }
            }

        } catch (IOException ex) {
            LOG.error("Cannot read from console.", ex);
        }
    }

    /**
     * Launch the META server shell.
     * @param args The list of arguments. Not supported at the moment.
     */
    public static void main(String[] args) {
        Metash sh = new Metash();
        if(sh.connect()) {
            sh.loop();
        }
        sh.closeConsole();
    }

}
