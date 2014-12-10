/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.crossdata.sh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.stratio.crossdata.common.exceptions.ConnectionException;
import com.stratio.crossdata.common.result.IDriverResultHandler;
import com.stratio.crossdata.common.result.QueryResult;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.driver.BasicDriver;
import com.stratio.crossdata.sh.help.HelpContent;
import com.stratio.crossdata.sh.help.HelpManager;
import com.stratio.crossdata.sh.help.HelpStatement;
import com.stratio.crossdata.sh.help.generated.CrossdataHelpLexer;
import com.stratio.crossdata.sh.help.generated.CrossdataHelpParser;
import com.stratio.crossdata.sh.utils.ConsoleUtils;
import com.stratio.crossdata.sh.utils.XDshCompletionHandler;
import com.stratio.crossdata.sh.utils.XDshCompletor;

import jline.console.ConsoleReader;

/**
 * Interactive Crossdata console that makes use of the existing driver.
 */
public class Shell {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(Shell.class);

    /**
     * Help content to be shown when the internal command {@code help} is used.
     */
    private final HelpContent help;

    /**
     * Asynchronous result handler.
     */
    private final IDriverResultHandler resultHandler;

    /**
     * Console reader.
     */
    private ConsoleReader console = null;

    /**
     * History file.
     */
    private File historyFile = null;

    /**
     * Driver that connects to the CROSSDATA servers.
     */
    private BasicDriver crossdataDriver = null;

    /**
     * History date format.
     */
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy");

    /**
     * Whether the asynchronous interface should be used.
     */
    private boolean useAsync = false;

    /**
     * Default String for the Crossdata prompt
     */
    private static final String DEFAULT_PROMPT = "xdsh:";

    private static final char DEFAULT_TEMP_PROMPT = '»';

    /**
     * Class constructor.
     *
     * @param useAsync Whether the queries will use the asynchronous interface.
     */
    public Shell(boolean useAsync) {
        HelpManager hm = new HelpManager();
        help = hm.loadHelpContent();
        this.useAsync = useAsync;
        initialize();
        resultHandler = new ShellDriverResultHandler(this);
    }

    /**
     * Launch the CROSSDATA server shell.
     *
     * @param args The list of arguments. Not supported at the moment.
     */
    public static void main(String[] args) {
        boolean async = true;
        String initScript = null;

        int index = 0;
        while (index < args.length) {
            if ("--sync".equals(args[index])) {
                async = false;
                LOG.info("Using synchronous behaviour");
            } else if ("--script".equals(args[index])) {
                if (index + 1 < args.length) {
                    LOG.info("Load script: " + args[index + 1]);
                    initScript = args[index + 1];
                    index++;
                } else {
                    LOG.error("Invalid --script syntax, file path missing");
                }
            }
            index++;
        }

        Shell sh = new Shell(async);
        if (sh.connect()) {
            if (initScript != null) {
                sh.executeScript(initScript);
            }
            sh.loop();
        }
        sh.closeConsole();
    }

    /**
     * Initialize the console settings.
     */
    private void initialize() {
        crossdataDriver = new BasicDriver();
        // Take the username from the system.
        crossdataDriver.setUserName(System.getProperty("user.name"));
        LOG.debug("Connecting with user: " + crossdataDriver.getUserName());

        try {
            console = new ConsoleReader();
            console.setExpandEvents(false);
            setPrompt(null);
            historyFile = ConsoleUtils.retrieveHistory(console, dateFormat);

            console.setCompletionHandler(new XDshCompletionHandler());
            console.addCompleter(new XDshCompletor());
        } catch (IOException e) {
            LOG.error("Cannot create a console.", e);
        }
    }

    /**
     * Print a message on the console.
     *
     * @param msg The message.
     */
    public void println(String msg) {
        try {
            console.getOutput().write(msg + System.lineSeparator());
        } catch (IOException e) {
            LOG.error("Cannot print to console.", e);
        }
    }

    /**
     * Flush the console output and show the current prompt.
     */
    protected void flush() {
        try {
            console.getOutput().write(console.getPrompt());
            console.flush();
        } catch (IOException e) {
            LOG.error("Cannot flush console.", e);
        }

    }

    /**
     * Set the console prompt.
     *
     * @param currentCatalog The currentCatalog.
     */
    public void setPrompt(String currentCatalog) {
        StringBuilder sb = new StringBuilder(DEFAULT_PROMPT);
        sb.append(crossdataDriver.getUserName());
        if ((currentCatalog != null) && (!currentCatalog.isEmpty())) {
            sb.append(":");
            sb.append(currentCatalog);
        }
        sb.append("> ");
        console.setPrompt(sb.toString());
    }

    /**
     * Parse a input text and return the equivalent HelpStatement.
     *
     * @param inputText The input text.
     * @return A Statement or null if the process failed.
     */
    private HelpStatement parseHelp(String inputText) {
        HelpStatement result = null;
        ANTLRStringStream input = new ANTLRStringStream(inputText);
        CrossdataHelpLexer lexer = new CrossdataHelpLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CrossdataHelpParser parser = new CrossdataHelpParser(tokens);
        try {
            result = parser.query();
        } catch (RecognitionException e) {
            LOG.error("Cannot parse statement", e);
        }
        return result;
    }

    /**
     * Show the help associated with a query.
     *
     * @param inputText The help query.
     */
    private void showHelp(String inputText) {
        HelpStatement h = parseHelp(inputText);
        println(help.searchHelp(h.getType()));
    }

    /**
     * Remove the {@link com.stratio.crossdata.common.result.IDriverResultHandler} associated with a query.
     *
     * @param queryId The query identifier.
     */
    protected void removeResultsHandler(String queryId) {
        crossdataDriver.removeResultHandler(queryId);
    }

    /**
     * Update the current prompt if a {@link com.stratio.crossdata.common.result.QueryResult} is returned,
     * and the current catalog has changed.
     *
     * @param result The result returned by the driver.
     */
    protected void updatePrompt(Result result) {
        if (QueryResult.class.isInstance(result)) {
            QueryResult qr = QueryResult.class.cast(result);
            if (qr.isCatalogChanged()) {
                String currentCatalog = qr.getCurrentCatalog();
                if (!currentCatalog.isEmpty()) {
                    crossdataDriver.setCurrentCatalog(currentCatalog);
                    setPrompt(currentCatalog);
                }
            }
        }
    }

    /**
     * Establish the connection with the CROSSDATA servers.
     *
     * @return Whether the connection has been successfully established.
     */
    public boolean connect() {
        boolean result = true;
        try {
            Result connectionResult = crossdataDriver.connect(crossdataDriver.getUserName());
            LOG.info("Driver connections established");
            LOG.info(ConsoleUtils.stringResult(connectionResult));
        } catch (ConnectionException ce) {
            result = false;
            LOG.error(ce.getMessage());
        }
        return result;
    }

    /**
     * Close the underlying driver and save the user history.
     */
    public void closeConsole() {
        try {
            ConsoleUtils.saveHistory(console, historyFile, dateFormat);
            LOG.debug("History saved");

            crossdataDriver.close();
            LOG.info("Driver connections closed");

        } catch (IOException ex) {
            LOG.error("Cannot save user history", ex);
        }
    }

    /**
     * Shell loop that receives user commands until a {@code exit} or {@code quit} command is
     * introduced.
     */
    public void loop() {
        try {
            String cmd = "";
            StringBuilder sb = new StringBuilder(cmd);
            String toExecute;
            String currentPrompt = "";
            while (!cmd.trim().toLowerCase().startsWith("exit")
                    && !cmd.trim().toLowerCase().startsWith("quit")) {
                cmd = console.readLine();
                sb.append(cmd).append(" ");
                toExecute = sb.toString().replaceAll("\\s+", " ").trim();
                if (toExecute.startsWith("//") || toExecute.startsWith("#")) {
                    LOG.debug("Comment: " + toExecute);
                    sb = new StringBuilder();
                } else if (toExecute.startsWith("/*")) {
                    LOG.debug("Multiline comment START");
                    if(console.getPrompt().startsWith(DEFAULT_PROMPT)){
                        currentPrompt = console.getPrompt();
                        String tempPrompt =
                                StringUtils.repeat(" ", DEFAULT_PROMPT.length()-1) + DEFAULT_TEMP_PROMPT + " ";
                        console.setPrompt(tempPrompt);
                    }
                    if(toExecute.endsWith("*/")){
                        LOG.debug("Multiline comment END");
                        sb = new StringBuilder();
                        if(!console.getPrompt().startsWith(DEFAULT_PROMPT)){
                            console.setPrompt(currentPrompt);
                        }
                    }
                } else if (toExecute.endsWith(";")) {
                    if (toExecute.toLowerCase().startsWith("help")) {
                        showHelp(sb.toString());
                    } else {
                        try {
                            Result result = crossdataDriver.executeRawQuery(toExecute, resultHandler);
                            LOG.info(ConsoleUtils.stringResult(result, this));
                        } catch (Exception ex) {
                            LOG.error("Execution failed: ", ex);
                        }
                    }
                    sb = new StringBuilder();
                    if(!console.getPrompt().startsWith(DEFAULT_PROMPT)){
                        console.setPrompt(currentPrompt);
                    }
                    println("");
                } else if (toExecute.toLowerCase().startsWith("help")) {
                    showHelp(sb.toString());
                    sb = new StringBuilder();
                    println("");
                } else if (toExecute.toLowerCase().startsWith("script")) {
                    String[] params = toExecute.split(" ");
                    if (params.length == 2) {
                        executeScript(params[1]);
                    } else {
                        showHelp(sb.toString());
                    }
                    sb = new StringBuilder();
                    println("");
                } else if(!toExecute.isEmpty()) { // Multiline code
                    if(console.getPrompt().startsWith(DEFAULT_PROMPT)){
                        currentPrompt = console.getPrompt();
                        String tempPrompt =
                                StringUtils.repeat(" ", DEFAULT_PROMPT.length()-1) + DEFAULT_TEMP_PROMPT + " ";
                        console.setPrompt(tempPrompt);
                    }
                }
            }
        } catch (IOException ex) {
            LOG.error("Cannot read from console.", ex);
        } catch (Exception e) {
            LOG.error("Cannot read from console.", e);
        }
    }

    /**
     * Execute the sentences found in a script. The file may contain empty lines and comment lines
     * using the prefix #.
     *
     * @param scriptPath The script path.
     */
    public void executeScript(String scriptPath) {
        BufferedReader input = null;
        String query;
        int numberOps = 0;
        Result result;
        try {
            input = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(
                                        new File(scriptPath)), "UTF-8"));

            while ((query = input.readLine()) != null) {
                query = query.trim();
                if (query.length() > 0 && !query.startsWith("#")) {
                    LOG.info("Executing: "+query);
                    if(useAsync){
                        result = crossdataDriver.executeRawQuery(query, resultHandler);
                        Thread.sleep(1000);
                    } else {
                        result = crossdataDriver.executeRawQuery(query);
                    }
                    LOG.info(ConsoleUtils.stringResult(result, this));
                    numberOps++;
                }
            }
        } catch (UnsupportedEncodingException e) {
            LOG.error("Invalid encoding on script: " + scriptPath, e);
        } catch (FileNotFoundException e) {
            LOG.error("Invalid path: " + scriptPath, e);
        } catch (IOException e) {
            LOG.error("Cannot read script: " + scriptPath, e);
        } catch (InterruptedException e) {
            LOG.error(e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    LOG.error(e);
                }
            }
        }
        println("Script " + scriptPath + " executed (" + numberOps + " sentences)");
    }

}
