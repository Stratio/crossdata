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
import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ConnectorName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.exceptions.ConnectionException;
import com.stratio.crossdata.common.exceptions.ManifestException;
import com.stratio.crossdata.common.manifest.CrossdataManifest;
import com.stratio.crossdata.common.result.CommandResult;
import com.stratio.crossdata.common.result.ErrorResult;
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
     * Constant to transform milliseconds in seconds.
     */
    private static final int MS_TO_SECONDS = 1000;

    /**
     * Constant to define the prefix for explain plan operations.
     */
    private static final String EXPLAIN_PLAN_TOKEN = "explain plan for";

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
    private void setPrompt(String currentCatalog) {
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
     * Execute a query on the remote CROSSDATA servers.
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
        Result crossDataResult;
        try {
            crossDataResult = crossdataDriver.executeQuery(cmd);
            queryEnd = System.currentTimeMillis();
            updatePrompt(crossDataResult);
            println("Result: " + ConsoleUtils.stringResult(crossDataResult));
            println("Response time: " + ((queryEnd - queryStart) / MS_TO_SECONDS) + " seconds");
        } catch (Exception e) {
            println("Error: " + e.getMessage());
        }
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
     * Execute a query asynchronously.
     *
     * @param cmd The query.
     */
    private void executeAsyncQuery(String cmd) {
        String queryId;
        try {
            queryId = crossdataDriver.asyncExecuteQuery(cmd, resultHandler);
            LOG.debug("Async command: " + cmd + " id: " + queryId);
            println("QID: " + queryId);
            println("");
        } catch (ConnectionException e) {
            LOG.error(e.getMessage(), e);
            println("ERROR: " + e.getMessage());
        }

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

    public String executeDescribe(String command){
        String result;
        if (command.toLowerCase().startsWith("describe system")) {
            result = describeSystem();
        } else if(command.toLowerCase().startsWith("describe datastores")){
            result = describeDatastores();
        } else if (command.toLowerCase().startsWith("describe datastore ")) {
            result = describeDatastore(
                    command.toLowerCase().replace("describe datastore ", "").replace(";", "").trim());
        } else if(command.toLowerCase().startsWith("describe clusters")){
            result = describeClusters();
        } else if (command.toLowerCase().startsWith("describe cluster ")) {
            result = describeCluster(
                    command.toLowerCase().replace("describe cluster ", "").replace(";", "").trim());
        } else if (command.toLowerCase().startsWith("describe connectors")) {
            result = describeConnectors();
        } else if(command.toLowerCase().startsWith("describe connector ")){
            result = describeConnector(command.toLowerCase().replace("describe connector ", "").replace(";", "").trim());
        } else if (command.toLowerCase().startsWith("describe catalogs")) {
            result = describeCatalogs();
        } else if(command.toLowerCase().startsWith("describe catalog ")){
            result = describeCatalog(command.toLowerCase().replace("describe catalog ", "").replace(";", "").trim());
        } else if (command.toLowerCase().startsWith("describe tables")) {
            if(command.toLowerCase().startsWith("describe tables from ")){
                result = describeTables(command.toLowerCase().replace("describe tables from ", "").replace(";", "").trim());
            } else if (!crossdataDriver.getCurrentCatalog().isEmpty()){
                result = describeTables(crossdataDriver.getCurrentCatalog());
            } else {
                result = "Catalog not specified";
            }
        } else if(command.toLowerCase().startsWith("describe table ")){
            if(command.toLowerCase().replace(";", "").trim().equalsIgnoreCase("describe table")){
                result = "Table name is missing";
            } else if(command.toLowerCase().startsWith("describe table ") &&
                    command.toLowerCase().replace("describe table ", "").replace(";", "").trim().contains(".")){
                result = describeTable(command.toLowerCase().replace("describe table ", "").replace(";", "").trim());
            } else if (!crossdataDriver.getCurrentCatalog().isEmpty()){
                result = describeTable(crossdataDriver.getCurrentCatalog() + "." +
                        command.toLowerCase().replace("describe table ", "").replace(";", "").trim());
            } else {
                result = "Catalog not specified";
            }
        } else {
            result = "Unknown command";
        }
        return result;
    }

    public boolean executeApiCall(String command){
        boolean apiCallExecuted = false;
        String result = "OK";
        if(command.toLowerCase().startsWith("describe")){
            result = executeDescribe(command);
            apiCallExecuted = true;
        } else if (command.toLowerCase().startsWith("add connector")
                   || command.toLowerCase().startsWith("add datastore")) {
            result = sendManifest(command);
            apiCallExecuted = true;
        } else if (command.toLowerCase().startsWith("reset serverdata")) {
            if(command.toLowerCase().contains("--force")) {
                result = resetServerdata();
            } else {
                String currentPrompt = console.getPrompt();
                String answer = "No";
                try {
                    console.print(" > RESET SERVERDATA will ERASE ALL THE DATA stored in the Crossdata Server, ");
                    console.println("even the one related to datastores, clusters and connectors.");
                    console.print(" > Maybe you can use CLEAN METADATA, ");
                    console.println("which erases only metadata related to catalogs, tables, indexes and columns.");
                    console.setPrompt(" > Do you want to continue? (yes/no): ");
                    answer = console.readLine();
                } catch (IOException e) {
                    LOG.error(e.getMessage());
                }
                answer = answer.replace(";", "").trim();
                if(answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y")){
                    result = resetServerdata();
                }
                console.setPrompt(currentPrompt);
            }
            apiCallExecuted = true;
        } else if (command.toLowerCase().startsWith("clean metadata")){
            result = cleanMetadata();
            apiCallExecuted = true;
        } else if (command.toLowerCase().startsWith("drop datastore")){
            result = dropManifest(
                    CrossdataManifest.TYPE_DATASTORE,
                    command.toLowerCase().replace("drop datastore ", "").replace(";", "").trim());
            apiCallExecuted = true;
        } else if (command.toLowerCase().startsWith("drop connector")){
            result = dropManifest(
                    CrossdataManifest.TYPE_CONNECTOR,
                    command.toLowerCase().replace("drop connector ", "").replace(";", "").trim());
            apiCallExecuted = true;
        } else if (command.toLowerCase().startsWith(EXPLAIN_PLAN_TOKEN)){
            result = explainPlan(command);
        }
        if(apiCallExecuted){
            LOG.info(result);
        }
        return apiCallExecuted;
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
                toExecute = sb.toString().trim();
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
                    } else if (toExecute.toLowerCase().startsWith("use ")) {
                        updateCatalog(toExecute);
                    } else if(executeApiCall(toExecute)) {
                        LOG.debug("API call executed.");
                    } else {
                        executeQuery(toExecute);
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
     * Trigger the explain plan operation using the driver.
     * @param toExecute The user input.
     * @return Execution plan.
     */
    private String explainPlan(String toExecute){
        Result r = crossdataDriver.explainPlan(toExecute.substring(EXPLAIN_PLAN_TOKEN.length()));
        return ConsoleUtils.stringResult(r);
    }

    private String describeConnectors() {
        return ConsoleUtils.stringResult(crossdataDriver.describeConnectors());
    }

    private String describeConnector(String connectorName) {
        return ConsoleUtils.stringResult(crossdataDriver.describeConnector(new ConnectorName(connectorName)));
    }

    private String describeSystem() {
        return ConsoleUtils.stringResult(crossdataDriver.describeSystem());
    }

    private String describeCatalog(String catalogName) {
        return ConsoleUtils.stringResult(crossdataDriver.describeCatalog(new CatalogName(catalogName)));
    }

    private String describeCatalogs() {
        return ConsoleUtils.stringResult(crossdataDriver.listCatalogs());
    }

    private String describeTables(String catalogName) {
        return ConsoleUtils.stringResult(crossdataDriver.describeTables(new CatalogName(catalogName)));
    }

    private String describeDatastore(String datastoreName) {
        return ConsoleUtils.stringResult(crossdataDriver.describeDatastore(new DataStoreName(datastoreName)));
    }

    private String describeCluster(String clusterName) {
        return ConsoleUtils.stringResult(crossdataDriver.describeCluster(new ClusterName(clusterName)));
    }

    private String describeDatastores() {
        return ConsoleUtils.stringResult(crossdataDriver.describeDatastores());
    }

    private String describeClusters() {
        return ConsoleUtils.stringResult(crossdataDriver.describeClusters());
    }

    private String updateCatalog(String toExecute) {
        String newCatalog = toExecute.toLowerCase().replace("use ", "").replace(";", "").trim();
        String currentCatalog = crossdataDriver.getCurrentCatalog();
        if(newCatalog.isEmpty()){
            crossdataDriver.setCurrentCatalog(newCatalog);
            currentCatalog = newCatalog;
        } else {
            List<String> catalogs = crossdataDriver.listCatalogs().getCatalogList();
            if (catalogs.contains(newCatalog.toLowerCase())) {
                crossdataDriver.setCurrentCatalog(newCatalog);
                currentCatalog = newCatalog;
            } else {
                LOG.error("Catalog " + newCatalog + " doesn't exist.");
            }
        }
        setPrompt(currentCatalog);
        return currentCatalog;
    }

    /**
     * Trigger the operation to reset only the metadata information related to catalogs.
     */
    private String cleanMetadata() {
        return ConsoleUtils.stringResult(crossdataDriver.cleanMetadata());
    }

    /**
     * Trigger the operation to reset all the data in the server.
     */
    private String resetServerdata() {
        return ConsoleUtils.stringResult(crossdataDriver.resetServerdata());
    }

    /**
     * Send a manifest through the driver.
     *
     * @param sentence The sentence introduced by the user.
     * @return The operation result.
     */
    public String sendManifest(String sentence) {
        LOG.debug("Command: " + sentence);
        // Get manifest type
        String result = "OK";

        String[] tokens = sentence.split(" ");
        if (tokens.length != 3) {
            return "ERROR: Invalid ADD syntax";
        }

        int typeManifest;
        if (tokens[1].equalsIgnoreCase("datastore")) {
            typeManifest = CrossdataManifest.TYPE_DATASTORE;
        } else if (tokens[1].equalsIgnoreCase("connector")) {
            typeManifest = CrossdataManifest.TYPE_CONNECTOR;
        } else {
            return "ERROR: Unknown type: " + tokens[1];
        }

        // Create CrossdataManifest object from XML file
        CrossdataManifest manifest;
        try {
            manifest = ConsoleUtils.parseFromXmlToManifest(typeManifest,
                    tokens[2].replace(";", "").replace("\"", "").replace("'", ""));
        } catch (ManifestException | FileNotFoundException e) {
            LOG.error("CrossdataManifest couldn't be parsed", e);
            return null;
        }

        long queryStart = System.currentTimeMillis();
        long queryEnd = queryStart;
        Result crossDataResult;

        try {
            crossDataResult = crossdataDriver.addManifest(manifest);
        } catch (ManifestException e) {
            LOG.error("CrossdataManifest couldn't be parsed", e);
            return null;
        }
        queryEnd = System.currentTimeMillis();
        updatePrompt(crossDataResult);
        LOG.info("Response time: " + ((queryEnd - queryStart) / MS_TO_SECONDS) + " seconds");
        if(crossDataResult instanceof ErrorResult){
            result = "Result: " + ConsoleUtils.stringResult(crossDataResult);
        }
        return result;
    }

    private String dropManifest(int manifestType, String manifestName) {
        try {
            Result result = crossdataDriver.dropManifest(manifestType, manifestName);
            String message;
            if(result.hasError()){
                ErrorResult errorResult = (ErrorResult) result;
                message = errorResult.getErrorMessage();
            } else {
                CommandResult commandResult = (CommandResult) result;
                message = commandResult.getResult().toString();
            }
            return message;
        } catch (ManifestException e) {
            return "ERROR";
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
