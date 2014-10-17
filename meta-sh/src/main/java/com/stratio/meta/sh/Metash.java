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

package com.stratio.meta.sh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.log4j.Logger;

import com.stratio.meta.common.exceptions.ConnectionException;
import com.stratio.meta.common.exceptions.ManifestException;
import com.stratio.meta.common.result.IResultHandler;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.driver.BasicDriver;
import com.stratio.meta.sh.help.HelpContent;
import com.stratio.meta.sh.help.HelpManager;
import com.stratio.meta.sh.help.HelpStatement;
import com.stratio.meta.sh.help.generated.MetaHelpLexer;
import com.stratio.meta.sh.help.generated.MetaHelpParser;
import com.stratio.meta.sh.utils.ConsoleUtils;
import com.stratio.meta.sh.utils.MetaCompletionHandler;
import com.stratio.meta.sh.utils.MetaCompletor;
import com.stratio.meta2.common.api.Manifest;
import com.stratio.meta2.common.result.Result;

import jline.console.ConsoleReader;

/**
 * Interactive META console.
 */
public class Metash {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(Metash.class);

    /**
     * Help content to be shown when the internal command {@code help} is used.
     */
    private final HelpContent help;
    /**
     * Asynchronous result handler.
     */
    private final IResultHandler resultHandler;
    /**
     * Console reader.
     */
    private ConsoleReader console = null;

    /**
     * History file.
     */
    private File historyFile = null;
    /**
     * Driver that connects to the META servers.
     */
    private BasicDriver metaDriver = null;

    /**
     * History date format.
     */
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy");

    /**
     * Whether the asynchronous interface should be used.
     */
    private boolean useAsync = false;

    /**
     * Class constructor.
     */
    public Metash(boolean useAsync) {
        HelpManager hm = new HelpManager();
        help = hm.loadHelpContent();
        this.useAsync = useAsync;
        initialize();
        resultHandler = new ShellResultHandler(this);
    }

    /**
     * Launch the META server shell.
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

        Metash sh = new Metash(async);
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
        metaDriver = new BasicDriver();
        // Take the username from the system.
        metaDriver.setUserName(System.getProperty("user.name"));
        LOG.debug("Connecting with user: " + metaDriver.getUserName());

        try {
            console = new ConsoleReader();
            console.setExpandEvents(false);
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
    private void setPrompt(String currentCatalog) {
        StringBuilder sb = new StringBuilder("metash-sh:");
        sb.append(metaDriver.getUserName());
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
     *
     * @param inputText The help query.
     */
    private void showHelp(String inputText) {
        HelpStatement h = parseHelp(inputText);
        println(help.searchHelp(h.getType()));
    }

    /**
     * Execute a query on the remote META servers.
     *
     * @param cmd The query.
     */
    private void executeQuery(String cmd) {
        if (this.useAsync) {
            executeAsyncQuery(cmd);
        } else {
            executeSyncQuery(cmd);
        }
    }

    /**
     * Execute a query using synchronous execution.
     *
     * @param cmd The query.
     */
    private void executeSyncQuery(String cmd) {
        LOG.debug("Command: " + cmd);
        long queryStart = System.currentTimeMillis();
        long queryEnd = queryStart;
        Result metaResult;
        try {
            metaResult = metaDriver.executeQuery(cmd);
            queryEnd = System.currentTimeMillis();
            updatePrompt(metaResult);
            println("Result: " + ConsoleUtils.stringResult(metaResult));
            println("Response time: " + ((queryEnd - queryStart) / 1000) + " seconds");
        } catch (Exception e) {
            println("Error: " + e.getMessage());
        }
    }

    /**
     * Remove the {@link com.stratio.meta.common.result.IResultHandler} associated with a query.
     *
     * @param queryId The query identifier.
     */
    protected void removeResultsHandler(String queryId) {
        metaDriver.removeResultHandler(queryId);
    }

    /**
     * Execute a query asynchronously.
     *
     * @param cmd The query.
     */
    private void executeAsyncQuery(String cmd) {
        String queryId;
        try {
            queryId = metaDriver.asyncExecuteQuery(cmd, resultHandler);
            LOG.debug("Async command: " + cmd + " id: " + queryId);
            println("QID: " + queryId);
            println("");
        } catch (ConnectionException e) {
            LOG.error(e.getMessage(), e);
            println("ERROR: " + e.getMessage());
        }

    }

    /**
     * Update the current prompt if a {@link com.stratio.meta.common.result.QueryResult} is returned,
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
                    metaDriver.setCurrentCatalog(currentCatalog);
                    setPrompt(currentCatalog);
                }
            }
        }
    }

    /**
     * Establish the connection with the META servers.
     *
     * @return Whether the connection has been successfully established.
     */
    public boolean connect() {
        boolean result = true;
        try {
            Result connectionResult = metaDriver.connect(metaDriver.getUserName());
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

            metaDriver.close();
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
            while (!cmd.trim().toLowerCase().startsWith("exit")
                    && !cmd.trim().toLowerCase().startsWith("quit")) {
                cmd = console.readLine();
                sb.append(cmd).append(" ");
                toExecute = sb.toString().trim();
                if (toExecute.endsWith(";")) {
                    if (" ".equalsIgnoreCase(sb.toString())
                            || System.lineSeparator().equalsIgnoreCase(sb.toString())) {
                        println("");
                    } else if (toExecute.toLowerCase().startsWith("help")) {
                        showHelp(sb.toString());
                    } else if (toExecute.toLowerCase().startsWith("add connector") || toExecute.toLowerCase()
                            .startsWith("add datastore")) {
                        sendManifest(toExecute);
                        println("");
                    } else if(toExecute.toLowerCase().startsWith("reset metadata")){
                        resetMetadata();
                    } else if (toExecute.toLowerCase().startsWith("use ")) {
                        updateCatalog(toExecute);
                        println("");
                    } else {
                        executeQuery(toExecute);
                        println("");
                    }
                    sb = new StringBuilder();
                } else if (toExecute.toLowerCase().startsWith("help")) {
                    showHelp(sb.toString());
                    sb = new StringBuilder();
                } else if (toExecute.toLowerCase().startsWith("script")) {
                    String[] params = toExecute.split(" ");
                    if (params.length == 2) {
                        executeScript(params[1]);
                    } else {
                        showHelp(sb.toString());
                    }
                    sb = new StringBuilder();
                }
            }
        } catch (IOException ex) {
            LOG.error("Cannot read from console.", ex);
        } catch (Exception e) {
            LOG.error("Cannot read from console.", e);
        }
    }

    private String updateCatalog(String toExecute) {
        String currentCatalog = metaDriver.getCurrentCatalog();
        String newCatalog = toExecute.toLowerCase().replace("use ", "").replace(";", "").trim();

        List<String> catalogs = metaDriver.listCatalogs().getCatalogList();
        if(catalogs.contains(newCatalog.toLowerCase())){
            metaDriver.setCurrentCatalog(newCatalog);
            currentCatalog = metaDriver.getCurrentCatalog();
        } else {
            LOG.error("Catalog "+newCatalog+" doesn't exist.");
        }

        setPrompt(currentCatalog);
        return currentCatalog;
    }

    public void resetMetadata(){
        metaDriver.resetMetadata();
    }

    public String sendManifest(String sentence) {
        LOG.debug("Command: " + sentence);
        // Get manifest type

        String [] tokens = sentence.split(" ");
        if(tokens.length != 3){
            return "ERROR: Invalid ADD syntax";
        }

        int type_manifest;
        if(tokens[1].equalsIgnoreCase("datastore")){
            type_manifest = Manifest.TYPE_DATASTORE;
        }else if (tokens[1].equalsIgnoreCase("connector")) {
            type_manifest = Manifest.TYPE_CONNECTOR;
        }else{
            return "ERROR: Unknown type: " + tokens[1];
        }

        // Create Manifest object from XML file
        Manifest manifest;
        try {
            manifest = ConsoleUtils.parseFromXmlToManifest(type_manifest,
                    tokens[2].replace(";", "").replace("\"", "").replace("'", ""));
        } catch (ManifestException | FileNotFoundException e) {
            LOG.error("Manifest couldn't be parsed", e);
            return null;
        }

        long queryStart = System.currentTimeMillis();
        long queryEnd = queryStart;
        Result metaResult;

        try {
            metaResult = metaDriver.addManifest(manifest);
        } catch (ManifestException e) {
            LOG.error("Manifest couldn't be parsed", e);
            return null;
        }
        queryEnd = System.currentTimeMillis();
        updatePrompt(metaResult);
        println("Result: " + ConsoleUtils.stringResult(metaResult));
        println("Response time: " + ((queryEnd - queryStart) / 1000) + " seconds");

        return "OK";
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
        try {
            input =
                    new BufferedReader(new InputStreamReader(new FileInputStream(new File(scriptPath)), "UTF-8"));

            while ((query = input.readLine()) != null) {
                query = query.trim();
                if (query.length() > 0 && !query.startsWith("#")) {
                    executeQuery(query);
                    numberOps++;
                }
            }
        } catch (UnsupportedEncodingException e) {
            LOG.error("Invalid encoding on script: " + scriptPath, e);
        } catch (FileNotFoundException e) {
            LOG.error("Invalid path: " + scriptPath, e);
        } catch (IOException e) {
            LOG.error("Cannot read script: " + scriptPath, e);
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
