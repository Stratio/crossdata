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

package com.stratio.crossdata.core.planner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.stratio.crossdata.common.data.AlterOperation;
import com.stratio.crossdata.common.data.AlterOptions;
import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.Cell;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.ConnectorName;
import com.stratio.crossdata.common.data.IndexName;
import com.stratio.crossdata.common.data.Row;
import com.stratio.crossdata.common.data.Status;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.PlanningException;
import com.stratio.crossdata.common.executionplan.ExecutionPath;
import com.stratio.crossdata.common.executionplan.ExecutionType;
import com.stratio.crossdata.common.executionplan.ExecutionWorkflow;
import com.stratio.crossdata.common.executionplan.ManagementWorkflow;
import com.stratio.crossdata.common.executionplan.MetadataWorkflow;
import com.stratio.crossdata.common.executionplan.QueryWorkflow;
import com.stratio.crossdata.common.executionplan.ResultType;
import com.stratio.crossdata.common.executionplan.StorageWorkflow;
import com.stratio.crossdata.common.logicalplan.Filter;
import com.stratio.crossdata.common.logicalplan.GroupBy;
import com.stratio.crossdata.common.logicalplan.Join;
import com.stratio.crossdata.common.logicalplan.Limit;
import com.stratio.crossdata.common.logicalplan.LogicalStep;
import com.stratio.crossdata.common.logicalplan.LogicalWorkflow;
import com.stratio.crossdata.common.logicalplan.OrderBy;
import com.stratio.crossdata.common.logicalplan.PartialResults;
import com.stratio.crossdata.common.logicalplan.Project;
import com.stratio.crossdata.common.logicalplan.Select;
import com.stratio.crossdata.common.logicalplan.TransformationStep;
import com.stratio.crossdata.common.logicalplan.UnionStep;
import com.stratio.crossdata.common.logicalplan.Window;
import com.stratio.crossdata.common.manifest.FunctionType;
import com.stratio.crossdata.common.metadata.CatalogMetadata;
import com.stratio.crossdata.common.metadata.ClusterMetadata;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.ConnectorAttachedMetadata;
import com.stratio.crossdata.common.metadata.ConnectorMetadata;
import com.stratio.crossdata.common.metadata.IndexMetadata;
import com.stratio.crossdata.common.metadata.IndexType;
import com.stratio.crossdata.common.metadata.Operations;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.statements.structures.AsteriskSelector;
import com.stratio.crossdata.common.statements.structures.ColumnSelector;
import com.stratio.crossdata.common.statements.structures.FunctionSelector;
import com.stratio.crossdata.common.statements.structures.Operator;
import com.stratio.crossdata.common.statements.structures.Relation;
import com.stratio.crossdata.common.statements.structures.SelectExpression;
import com.stratio.crossdata.common.statements.structures.IntegerSelector;
import com.stratio.crossdata.common.statements.structures.FloatingPointSelector;
import com.stratio.crossdata.common.statements.structures.StringSelector;
import com.stratio.crossdata.common.statements.structures.BooleanSelector;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.common.utils.StringUtils;
import com.stratio.crossdata.core.metadata.MetadataManager;
import com.stratio.crossdata.core.query.MetadataPlannedQuery;
import com.stratio.crossdata.core.query.MetadataValidatedQuery;
import com.stratio.crossdata.core.query.SelectPlannedQuery;
import com.stratio.crossdata.core.query.SelectValidatedQuery;
import com.stratio.crossdata.core.query.StoragePlannedQuery;
import com.stratio.crossdata.core.query.StorageValidatedQuery;
import com.stratio.crossdata.core.statements.*;
import com.stratio.crossdata.core.structures.InnerJoin;
import com.stratio.crossdata.core.utils.CoreUtils;

/**
 * Class in charge of defining the set of {@link com.stratio.crossdata.common.logicalplan.LogicalStep}
 * required to execute a statement. This set of steps are ordered as a workflow on a {@link
 * com.stratio.crossdata.common.logicalplan.LogicalWorkflow} structure. Notice that the LogicalWorkflow
 * may contain several initial steps, but it will always finish in a single operation.
 */
public class Planner {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(Planner.class);

    /**
     * Define a logical workflow that represents the operations required for executing the {@code SELECT} query sent
     * by the user. After that, an ExecutionWorkflow is creating determining which connectors will execute the
     * different elements of the original query.
     *
     * @param query A {@link com.stratio.crossdata.core.query.SelectValidatedQuery}.
     * @return A {@link com.stratio.crossdata.core.query.SelectPlannedQuery}.
     * @throws com.stratio.crossdata.common.exceptions.PlanningException If the query cannot be planned.
     */
    public SelectPlannedQuery planQuery(SelectValidatedQuery query) throws PlanningException {
        LogicalWorkflow workflow = buildWorkflow(query);
        //Plan the workflow execution into different connectors.
        ExecutionWorkflow executionWorkflow = buildExecutionWorkflow(query.getQueryId(), workflow);
        //Return the planned query.
        return new SelectPlannedQuery(query, executionWorkflow);
    }

    /**
     * Define an execution plan for metadata queries.
     *
     * @param query A {@link com.stratio.crossdata.core.query.MetadataValidatedQuery}.
     * @return A {@link com.stratio.crossdata.core.query.MetadataPlannedQuery}.
     * @throws PlanningException If the query cannot be planned.
     */
    public MetadataPlannedQuery planQuery(MetadataValidatedQuery query) throws PlanningException {
        ExecutionWorkflow executionWorkflow = buildExecutionWorkflow(query);
        return new MetadataPlannedQuery(query, executionWorkflow);
    }

    /**
     * Define an execution plan for storage queries.
     *
     * @param query A {@link com.stratio.crossdata.core.query.StorageValidatedQuery}.
     * @return A {@link com.stratio.crossdata.core.query.StoragePlannedQuery}.
     * @throws PlanningException If the query cannot be planned.
     */
    public StoragePlannedQuery planQuery(StorageValidatedQuery query) throws PlanningException {
        ExecutionWorkflow executionWorkflow = buildExecutionWorkflow(query);
        return new StoragePlannedQuery(query, executionWorkflow);
    }

    /**
     * Build a execution workflow for a query analyzing the existing logical workflow.
     *
     * @param queryId  The query identifier.
     * @param workflow The {@link com.stratio.crossdata.common.logicalplan.LogicalWorkflow} associated with the query.
     * @return A {@link com.stratio.crossdata.common.executionplan.ExecutionWorkflow}.
     * @throws PlanningException If the workflow cannot be defined.
     */
    protected ExecutionWorkflow buildExecutionWorkflow(String queryId, LogicalWorkflow workflow)
                    throws PlanningException {

        //Get the list of tables accessed in this query
        List<TableName> tables = getInitialSteps(workflow.getInitialSteps());

        //Obtain the map of connector that is able to access those tables.
        Map<TableName, List<ConnectorMetadata>> candidatesConnectors = MetadataManager.MANAGER
                        .getAttachedConnectors(Status.ONLINE, tables);

        StringBuilder sb = new StringBuilder("Candidate connectors: ").append(System.lineSeparator());
        for (Map.Entry<TableName, List<ConnectorMetadata>> tableEntry : candidatesConnectors.entrySet()) {
            for (ConnectorMetadata cm : tableEntry.getValue()) {
                sb.append("table: ").append(tableEntry.getKey().toString()).append(" ").append(cm.getName()).append(" ")
                                .append(cm.getActorRef()).append(System.lineSeparator());
            }
        }
        LOG.info(sb.toString());

        List<ExecutionPath> executionPaths = new ArrayList<>();
        Map<UnionStep, Set<ExecutionPath>> unionSteps = new HashMap<>();
        //Iterate through the initial steps and build valid execution paths
        for (LogicalStep step : workflow.getInitialSteps()) {
            TableName targetTable = ((Project) step).getTableName();
            LOG.info("Table: " + targetTable);
            ExecutionPath ep = defineExecutionPath(step, candidatesConnectors.get(targetTable));
            LOG.info("Last step: " + ep.getLast());
            if (UnionStep.class.isInstance(ep.getLast())) {
                Set<ExecutionPath> paths = unionSteps.get(ep.getLast());
                if (paths == null) {
                    paths = new HashSet<>();
                    unionSteps.put((UnionStep) ep.getLast(), paths);
                }
                paths.add(ep);
            } else if (ep.getLast().getNextStep() != null && UnionStep.class.isInstance(ep.getLast().getNextStep())) {
                Set<ExecutionPath> paths = unionSteps.get(ep.getLast().getNextStep());
                if (paths == null) {
                    paths = new HashSet<>();
                    unionSteps.put((UnionStep) (ep.getLast().getNextStep()), paths);
                }
                paths.add(ep);
            }
            executionPaths.add(ep);
        }

        for (ExecutionPath ep : executionPaths) {
            LOG.info("ExecutionPaths: " + ep);
        }

        //Merge execution paths
        return mergeExecutionPaths(queryId, executionPaths, unionSteps);
    }

    /**
     * Merge a set of execution paths solving union dependencies along.
     *
     * @param queryId        The query identifier.
     * @param executionPaths The list of execution paths.
     * @param unionSteps     A map of union steps waiting to be merged.
     * @return A {@link com.stratio.crossdata.common.executionplan.ExecutionWorkflow}.
     * @throws PlanningException If the execution paths cannot be merged.
     */
    protected ExecutionWorkflow mergeExecutionPaths(String queryId, List<ExecutionPath> executionPaths,
                    Map<UnionStep, Set<ExecutionPath>> unionSteps) throws PlanningException {

        if (unionSteps.size() == 0) {
            return toExecutionWorkflow(queryId, executionPaths, executionPaths.get(0).getLast(),
                            executionPaths.get(0).getAvailableConnectors(), ResultType.RESULTS);
        }
        LOG.info("UnionSteps: " + unionSteps.toString());
        //Find first UnionStep
        UnionStep mergeStep = null;
        ExecutionPath[] paths = null;
        for (Map.Entry<UnionStep, Set<ExecutionPath>> entry : unionSteps.entrySet()) {
            paths = entry.getValue().toArray(new ExecutionPath[entry.getValue().size()]);
            if (paths.length == 2 && TransformationStep.class.isInstance(paths[0].getLast()) && TransformationStep.class
                            .isInstance(paths[1].getLast())) {
                mergeStep = entry.getKey();
            }
        }

        List<ConnectorMetadata> toRemove = new ArrayList<>();
        List<ConnectorMetadata> mergeConnectors = new ArrayList<>();
        Map<PartialResults, ExecutionWorkflow> triggerResults = new HashMap<>();
        Map<UnionStep, ExecutionWorkflow> triggerWorkflow = new HashMap<>();
        List<ExecutionWorkflow> workflows = new ArrayList<>();
        UnionStep nextUnion = null;
        QueryWorkflow first = null;
        List<ExecutionPath> toMerge = new ArrayList<>(2);
        boolean[] intermediateResults = new boolean[2];
        boolean exit = false;
        while (!exit) {
            //Check whether the list of connectors found in the Execution paths being merged can execute the join
            intermediateResults[0] = false;
            intermediateResults[1] = false;
            mergeConnectors.clear();
            toMerge.clear();
            for (int index = 0; index < paths.length; index++) {
                toRemove.clear();
                for (ConnectorMetadata connector : paths[index].getAvailableConnectors()) {
                    LOG.info("op: " + mergeStep.getOperation() + " -> " + connector.supports(mergeStep.getOperation()));
                    if (!connector.supports(mergeStep.getOperation())) {
                        toRemove.add(connector);
                    }
                }
                if (paths[index].getAvailableConnectors().size() == toRemove.size()) {
                    //Add intermediate result node
                    PartialResults partialResults = new PartialResults(Operations.PARTIAL_RESULTS);
                    partialResults.setNextStep(mergeStep);

                    // TODO: CROSSDATA-464
                    // Add select step before merge step


                    mergeStep.addPreviousSteps(partialResults);
                    mergeStep.removePreviousStep(paths[index].getLast());
                    paths[index].getLast().setNextStep(null);
                    //Create a trigger execution workflow with the partial results step.
                    ExecutionWorkflow w = toExecutionWorkflow(queryId, Arrays.asList(paths[index]),
                                    paths[index].getLast(), paths[index].getAvailableConnectors(),
                                    ResultType.TRIGGER_EXECUTION);
                    w.setTriggerStep(partialResults);

                    triggerResults.put(partialResults, w);

                    workflows.add(w);
                    intermediateResults[index] = true;
                    if (first == null) {
                        first = QueryWorkflow.class.cast(w);
                    }
                } else {
                    paths[index].getAvailableConnectors().removeAll(toRemove);
                    //Add to the list of mergeConnectors.
                    mergeConnectors.addAll(paths[index].getAvailableConnectors());
                    toMerge.add(paths[index]);
                }
            }
            unionSteps.remove(mergeStep);

            ExecutionPath next = defineExecutionPath(mergeStep, mergeConnectors);
            if (Select.class.isInstance(next.getLast())) {
                exit = true;
                ExecutionWorkflow mergeWorkflow = extendExecutionWorkflow(queryId, toMerge, next, ResultType.RESULTS);
                triggerWorkflow.put(mergeStep, mergeWorkflow);
                if (first == null) {
                    first = QueryWorkflow.class.cast(mergeWorkflow);
                }
            } else {
                Set<ExecutionPath> existingPaths = unionSteps.get(next.getLast());
                if (executionPaths == null) {
                    existingPaths = new HashSet<>();
                } else {
                    executionPaths.add(next);
                }
                unionSteps.put(UnionStep.class.cast(next.getLast()), existingPaths);
            }

            if (unionSteps.isEmpty()) {
                exit = true;
            } else {
                mergeStep = nextUnion;
                paths = unionSteps.get(mergeStep).toArray(new ExecutionPath[unionSteps.get(mergeStep).size()]);
            }
        }
        return buildExecutionTree(first, triggerResults, triggerWorkflow);
    }

    /**
     * Build the tree of linked execution workflows.
     *
     * @param first            The first workflow of the list.
     * @param triggerResults   The map of triggering steps.
     * @param triggerWorkflows The map of workflows associated with merge steps.
     * @return A {@link com.stratio.crossdata.common.executionplan.ExecutionWorkflow}.
     */
    public ExecutionWorkflow buildExecutionTree(QueryWorkflow first,
                    Map<PartialResults, ExecutionWorkflow> triggerResults,
                    Map<UnionStep, ExecutionWorkflow> triggerWorkflows) {

        LogicalStep triggerStep = first.getTriggerStep();
        ExecutionWorkflow workflow = first;

        while (!triggerResults.isEmpty()) {
            workflow.setNextExecutionWorkflow(triggerWorkflows.get(triggerStep.getNextStep()));
            triggerResults.remove(triggerStep);
            workflow = workflow.getNextExecutionWorkflow();
            triggerStep = workflow.getTriggerStep();
        }

        return first;
    }

    /**
     * Define an query workflow.
     *
     * @param queryId        The query identifier.
     * @param executionPaths The list of execution paths that will be transformed into initial steps of a
     *                       {@link com.stratio.crossdata.common.logicalplan.LogicalWorkflow}.
     * @param last           The last element of the workflow.
     * @param connectors     The List of available connectors.
     * @return A {@link com.stratio.crossdata.common.executionplan.QueryWorkflow}.
     */
    protected QueryWorkflow toExecutionWorkflow(String queryId, List<ExecutionPath> executionPaths, LogicalStep last,
                    List<ConnectorMetadata> connectors, ResultType type) {

        //Define the list of initial steps.
        List<LogicalStep> initialSteps = new ArrayList<>(executionPaths.size());
        for (ExecutionPath path : executionPaths) {
            initialSteps.add(path.getInitial());
        }
        LogicalWorkflow workflow = new LogicalWorkflow(initialSteps);

        //Select an actor
        ConnectorMetadata connectorMetadata = bestConnector(connectors);
        String selectedActorUri = StringUtils.getAkkaActorRefUri(connectorMetadata.getActorRef());

        updateFunctionsFromSelect(workflow, connectorMetadata.getName());

        if((connectorMetadata.getSupportedOperations().contains(Operations.PAGINATION))
                && (connectorMetadata.getPageSize() > 0)){
            workflow.setPagination(connectorMetadata.getPageSize());
        }

        return new QueryWorkflow(queryId, selectedActorUri, ExecutionType.SELECT, type, workflow);
    }

    private ConnectorMetadata bestConnector(List<ConnectorMetadata> connectors) {
        //TODO: Add logic to this method according to native or not
        //TODO: Add logic to this method according to statistics
        return connectors.get(0);
    }

    private void updateFunctionsFromSelect(LogicalWorkflow workflow, ConnectorName connectorName) {

        if (!Select.class.isInstance(workflow.getLastStep())) {
            return;
        }

        Select selectStep = Select.class.cast(workflow.getLastStep());

        Map<String, ColumnType> typeMap = selectStep.getTypeMap();

        Map<Selector, ColumnType> typeMapFromColumnName = selectStep.getTypeMapFromColumnName();

        for (Selector s : typeMapFromColumnName.keySet()) {
            if (FunctionSelector.class.isInstance(s)) {
                FunctionSelector fs = FunctionSelector.class.cast(s);
                String functionName = fs.getFunctionName();
                FunctionType ft = MetadataManager.MANAGER.getFunction(connectorName, functionName);
                String returningType = StringUtils.getReturningTypeFromSignature(ft.getSignature());
                ColumnType ct = StringUtils.convertXdTypeToColumnType(returningType);
                typeMapFromColumnName.put(fs, ct);
                String stringKey = fs.getFunctionName();
                if (fs.getAlias() != null) {
                    stringKey = fs.getAlias();
                }
                typeMap.put(stringKey, ct);
            }
        }
    }

    /**
     * Define a query workflow composed by several execution paths merging in a
     * {@link com.stratio.crossdata.common.executionplan.ExecutionPath} that starts with a UnionStep.
     *
     * @param queryId        The query identifier.
     * @param executionPaths The list of execution paths.
     * @param mergePath      The merge path with the union step.
     * @param type           The type of results to be returned.
     * @return A {@link com.stratio.crossdata.common.executionplan.QueryWorkflow}.
     */
    protected QueryWorkflow extendExecutionWorkflow(String queryId, List<ExecutionPath> executionPaths,
                    ExecutionPath mergePath, ResultType type) {

        //Define the list of initial steps.
        List<LogicalStep> initialSteps = new ArrayList<>(executionPaths.size());
        for (ExecutionPath path : executionPaths) {
            initialSteps.add(path.getInitial());
            path.getLast().setNextStep(mergePath.getInitial());
        }

        LogicalWorkflow workflow = new LogicalWorkflow(initialSteps);

        //Select an actor
        //TODO Improve actor selection based on cost analysis.
        String selectedActorUri = StringUtils
                        .getAkkaActorRefUri(mergePath.getAvailableConnectors().get(0).getActorRef());
        return new QueryWorkflow(queryId, selectedActorUri, ExecutionType.SELECT, type, workflow);
    }

    /**
     * Define the a execution path that starts with a initial step. This process refines the list of available
     * connectors in order to obtain the list that supports all operations in an execution paths.
     *
     * @param initial             The initial step.
     * @param availableConnectors The list of available connectors.
     * @return An {@link com.stratio.crossdata.common.executionplan.ExecutionPath}.
     * @throws PlanningException If the execution path cannot be determined.
     */
    protected ExecutionPath defineExecutionPath(LogicalStep initial, List<ConnectorMetadata> availableConnectors)
                    throws PlanningException {

        LogicalStep last = null;
        LogicalStep current = initial;
        List<ConnectorMetadata> toRemove = new ArrayList<>();
        boolean exit = false;

        LOG.info("Available connectors: " + availableConnectors);

        while (!exit) {
            // Evaluate the connectors
            for (ConnectorMetadata connector : availableConnectors) {
                if (!connector.supports(current.getOperation())) {
                    // Check selector functions
                    toRemove.add(connector);
                } else {
                /*
                 * This connector support the operation but we also have to check if support for a specific
                 * function is required support.
                 */

                    if(current.getOperation().getOperationsStr().toLowerCase().contains("function")){
                        Set<String> sFunctions = MetadataManager.MANAGER
                                .getSupportedFunctionNames(connector.getName());
                        switch(current.getOperation()){
                            case SELECT_FUNCTIONS:
                                Select select = (Select) current;
                                Set<Selector> cols = select.getColumnMap().keySet();
                                for(Selector selector: cols){
                                    if(selector instanceof FunctionSelector){
                                        FunctionSelector fSelector = (FunctionSelector) selector;
                                        if(!sFunctions.contains(fSelector.getFunctionName())) {
                                            toRemove.add(connector);
                                            break;
                                        }else {
                                            if (!MetadataManager.MANAGER.checkSignature(fSelector, connector.getName())) {
                                                toRemove.add(connector);
                                                break;
                                            }
                                        }
                                    }

                                 }
                            break;
                            default:
                                 throw new PlanningException(current.getOperation() + " not supported yet.");
                        }
                    }
                }
            }
            // Remove invalid connectors
            if (toRemove.size() == availableConnectors.size()) {
                throw new PlanningException(
                                "Cannot determine execution path as no connector supports " + current.toString());
            } else {
                availableConnectors.removeAll(toRemove);

                if (current.getNextStep() == null || UnionStep.class.isInstance(current.getNextStep())) {
                    exit = true;
                    last = current;
                } else {
                    current = current.getNextStep();
                }
            }
            toRemove.clear();

        }
        return new ExecutionPath(initial, last, availableConnectors);
    }

    /**
     * Get the list of tables accessed in a list of initial steps.
     *
     * @param initialSteps The list of initial steps.
     * @return A list of {@link com.stratio.crossdata.common.data.TableName}.
     */
    protected List<TableName> getInitialSteps(List<LogicalStep> initialSteps) {
        List<TableName> tables = new ArrayList<>(initialSteps.size());
        for (LogicalStep ls : initialSteps) {
            tables.add(Project.class.cast(ls).getTableName());
        }
        return tables;
    }

    /**
     * Build a workflow with the {@link com.stratio.crossdata.common.logicalplan.LogicalStep} required to
     * execute a query. This method does not determine which connector will execute which part of the
     * workflow.
     *
     * @param query The query to be planned.
     * @return A Logical workflow.
     */
    protected LogicalWorkflow buildWorkflow(SelectValidatedQuery query) throws PlanningException {
        Map<String, TableMetadata> tableMetadataMap = new HashMap<>();
        for (TableMetadata tm : query.getTableMetadata()) {
            tableMetadataMap.put(tm.getName().getQualifiedName(), tm);
        }
        //Define the list of projects
        Map<String, LogicalStep> processed = getProjects(query, tableMetadataMap);
        addProjectedColumns(processed, query);

        //TODO determine which is the correct target table if the order fails.
        String selectTable = query.getStatement().getTableName().getQualifiedName();

        //Add filters
        if (query.getRelations() != null) {
            processed = addFilter(processed, tableMetadataMap, query);
        }

        //Add window
        SelectStatement ss = SelectStatement.class.cast(query.getStatement());
        if (ss.getWindow() != null) {
            processed = addWindow(processed, ss);
        }

        //Add join
        if (query.getJoin() != null) {
            processed = addJoin(processed, selectTable, query);
        }

        //Prepare the result.
        List<LogicalStep> initialSteps = new ArrayList<>();
        LogicalStep initial = null;
        for (LogicalStep ls : processed.values()) {
            if (!UnionStep.class.isInstance(ls)) {
                initial = ls;
                //Go to the first element of the workflow
                while (initial.getFirstPrevious() != null) {
                    initial = initial.getFirstPrevious();
                }
                if (Project.class.isInstance(initial)) {
                    initialSteps.add(initial);
                }
            }
        }

        //Include Select step for join queries
        boolean firstPath = true;
        for(LogicalStep initialStep: initialSteps){
            LogicalStep step = initialStep;
            LogicalStep previousStepToUnion = initialStep;
            while((step != null) && (!UnionStep.class.isInstance(step))){
                previousStepToUnion = step;
                step = step.getNextStep();
            }
            if(step == null){
                continue;
            } else {
                // Create Select step here
                UnionStep unionStep = (UnionStep) step;
                Map<String, TableMetadata> partialTableMetadataMap = new HashMap<>();
                for(String key: tableMetadataMap.keySet()){
                    if(Project.class.isInstance(initialStep)){
                        Project projectStep = (Project) initialStep;
                        if(projectStep.getTableName().getQualifiedName().equalsIgnoreCase(key)){
                            partialTableMetadataMap.put(key, tableMetadataMap.get(key));
                            break;
                        }
                    }
                }
                // Generate fake Select for Join Table
                SelectStatement partialSelect = ss;
                if(Project.class.cast(initialStep).getTableName().getQualifiedName().equalsIgnoreCase(
                        ss.getJoin().getTablename().getQualifiedName())){
                    List<Selector> selectorList = new ArrayList<>();
                    Selector firstSelector = ss.getSelectExpression().getSelectorList().get(0);
                    if(firstSelector instanceof ColumnSelector){
                        Project currentProject = (Project) initialStep;
                        List<ColumnName> columnsFromProject = currentProject.getColumnList();
                        for(ColumnName col: columnsFromProject){
                            selectorList.add(new ColumnSelector(col));
                        }
                    } else {
                        TableMetadata tableMetadata =
                                MetadataManager.MANAGER.getTable(ss.getJoin().getTablename());
                        for(ColumnMetadata cm: tableMetadata.getColumns().values()){
                            ColumnSelector cs = new ColumnSelector(cm.getName());
                            selectorList.add(cs);
                        }
                    }
                    SelectExpression selectExpression = new SelectExpression(selectorList);
                    TableName tableName = ss.getJoin().getTablename();
                    partialSelect = new SelectStatement(selectExpression, tableName);
                } else {
                    List<Selector> selectorList = new ArrayList<>();
                    Project currentProject = (Project) initialStep;
                    List<ColumnName> columnsFromProject = currentProject.getColumnList();
                    for(ColumnName col: columnsFromProject){
                        selectorList.add(new ColumnSelector(col));
                    }
                    partialSelect = new SelectStatement(new SelectExpression(selectorList), ss.getTableName());
                    partialSelect.setJoin(null);
                    partialTableMetadataMap = tableMetadataMap;
                }
                Select selectStep = generateSelect(partialSelect, partialTableMetadataMap);

                previousStepToUnion.setNextStep(selectStep);

                selectStep.setPrevious(previousStepToUnion);
                selectStep.setNextStep(unionStep);

                List<LogicalStep> previousStepsToUnion = unionStep.getPreviousSteps();
                if(firstPath){
                    previousStepsToUnion.clear();
                    firstPath = false;
                }
                previousStepsToUnion.add(selectStep);
                unionStep.setPreviousSteps(previousStepsToUnion);
            }
        }

        //Find the last element
        LogicalStep last = initial;
        while (last.getNextStep() != null) {
            last = last.getNextStep();
        }

        // GROUP BY clause
        if (ss.isGroupInc()) {
            GroupBy groupBy = new GroupBy(Operations.SELECT_GROUP_BY, ss.getGroupByClause().getSelectorIdentifier());
            last.setNextStep(groupBy);
            groupBy.setPrevious(last);
            last = groupBy;
        }

        // ORDER BY clause
        if (ss.isOrderInc()) {
            OrderBy orderBy = new OrderBy(Operations.SELECT_ORDER_BY, ss.getOrderByClauses());
            last.setNextStep(orderBy);
            orderBy.setPrevious(last);
            last = orderBy;
        }

        //Add LIMIT clause
        if (ss.isLimitInc()) {
            Limit l = new Limit(Operations.SELECT_LIMIT, ss.getLimit());
            last.setNextStep(l);
            l.setPrevious(last);
            last = l;
        }

        //Add SELECT operator
        Select finalSelect = generateSelect(ss, tableMetadataMap);
        last.setNextStep(finalSelect);
        finalSelect.setPrevious(last);

        LogicalWorkflow workflow = new LogicalWorkflow(initialSteps);
        workflow.setLastStep(finalSelect);

        return workflow;
    }

    protected ExecutionWorkflow buildExecutionWorkflow(MetadataValidatedQuery query) throws PlanningException {
        MetadataStatement metadataStatement = query.getStatement();
        ExecutionWorkflow executionWorkflow;

        Set<String> metadataStatements = new HashSet<>();
        metadataStatements.add(CreateCatalogStatement.class.toString());
        metadataStatements.add(CreateTableStatement.class.toString());
        metadataStatements.add(DropCatalogStatement.class.toString());
        metadataStatements.add(DropTableStatement.class.toString());
        metadataStatements.add(AlterCatalogStatement.class.toString());
        metadataStatements.add(AlterTableStatement.class.toString());
        metadataStatements.add(CreateIndexStatement.class.toString());
        metadataStatements.add(DropIndexStatement.class.toString());
        metadataStatements.add(ImportMetadataStatement.class.toString());

        Set<String> managementStatements = new HashSet<>();
        managementStatements.add(AttachClusterStatement.class.toString());
        managementStatements.add(AttachConnectorStatement.class.toString());
        managementStatements.add(DetachConnectorStatement.class.toString());
        managementStatements.add(DetachClusterStatement.class.toString());
        managementStatements.add(AlterClusterStatement.class.toString());

        if (metadataStatements.contains(metadataStatement.getClass().toString())) {
            executionWorkflow = buildMetadataWorkflow(query);
        } else if (managementStatements.contains(metadataStatement.getClass().toString())) {
            executionWorkflow = buildManagementWorkflow(query);
        } else {
            throw new PlanningException("This statement can't be planned: " + metadataStatement.toString());
        }

        return executionWorkflow;
    }

    private MetadataWorkflow buildMetadataWorkflowCreateCatalog(MetadataStatement metadataStatement, String queryId) {
        MetadataWorkflow metadataWorkflow;
        // Create parameters for metadata workflow
        CreateCatalogStatement createCatalogStatement = (CreateCatalogStatement) metadataStatement;
        String actorRefUri = null;
        ExecutionType executionType = ExecutionType.CREATE_CATALOG;
        ResultType type = ResultType.RESULTS;

        metadataWorkflow = new MetadataWorkflow(queryId, actorRefUri, executionType, type);

        // Create & add CatalogMetadata to the MetadataWorkflow
        CatalogName name = createCatalogStatement.getCatalogName();
        Map<Selector, Selector> options = createCatalogStatement.getOptions();
        Map<TableName, TableMetadata> tables = new HashMap<>();
        CatalogMetadata catalogMetadata = new CatalogMetadata(name, options, tables);
        metadataWorkflow.setCatalogName(name);
        metadataWorkflow.setCatalogMetadata(catalogMetadata);
        metadataWorkflow.setIfNotExists(createCatalogStatement.isIfNotExists());
        return metadataWorkflow;
    }

    private MetadataWorkflow buildMetadataWorkflowCreateTable(MetadataStatement metadataStatement, String queryId) {
        MetadataWorkflow metadataWorkflow;
        // Create parameters for metadata workflow
        CreateTableStatement createTableStatement = (CreateTableStatement) metadataStatement;

        ClusterMetadata clusterMetadata = MetadataManager.MANAGER.getCluster(createTableStatement.getClusterName());

        String actorRefUri = null;
        try {
            actorRefUri = findAnyActorRef(clusterMetadata, Status.ONLINE, Operations.CREATE_TABLE);
        } catch (PlanningException pe) {
            LOG.debug("No connector was found to execute CREATE_TABLE", pe);
        }

        ExecutionType executionType = ExecutionType.CREATE_TABLE;
        ResultType type = ResultType.RESULTS;

        metadataWorkflow = new MetadataWorkflow(queryId, actorRefUri, executionType, type);

        metadataWorkflow.setIfNotExists(createTableStatement.isIfNotExists());

        if (!existsCatalogInCluster(createTableStatement.getTableName().getCatalogName(),
                        createTableStatement.getClusterName())) {

            try {
                actorRefUri = findAnyActorRef(clusterMetadata, Status.ONLINE, Operations.CREATE_CATALOG);

                executionType = ExecutionType.CREATE_TABLE_AND_CATALOG;

                // Create MetadataWorkFlow
                metadataWorkflow = new MetadataWorkflow(queryId, actorRefUri, executionType, type);

                // Add CatalogMetadata to the WorkFlow
                metadataWorkflow.setCatalogName(createTableStatement.getTableName().getCatalogName());

                metadataWorkflow.setCatalogMetadata(MetadataManager.MANAGER
                                .getCatalog(createTableStatement.getTableName().getCatalogName()));
            } catch (PlanningException pe) {
                LOG.debug("Cannot determine any connector for the operation: " + Operations.CREATE_CATALOG, pe);
            }
        }

        // Create & add TableMetadata to the MetadataWorkflow
        TableName name = createTableStatement.getTableName();
        Map<Selector, Selector> options = createTableStatement.getProperties();
        LinkedHashMap<ColumnName, ColumnMetadata> columnMap = new LinkedHashMap<>();
        for (Map.Entry<ColumnName, ColumnType> c : createTableStatement.getColumnsWithTypes().entrySet()) {
            ColumnName columnName = c.getKey();
            ColumnMetadata columnMetadata = new ColumnMetadata(columnName, null, c.getValue());
            columnMap.put(columnName, columnMetadata);
        }
        ClusterName clusterName = createTableStatement.getClusterName();
        List<ColumnName> partitionKey = new LinkedList<>();
        partitionKey.addAll(createTableStatement.getPartitionKey());
        List<ColumnName> clusterKey = new LinkedList<>();
        clusterKey.addAll(createTableStatement.getClusterKey());
        Map<IndexName, IndexMetadata> indexes = new HashMap<>();
        TableMetadata tableMetadata = new TableMetadata(name, options, columnMap, indexes, clusterName, partitionKey,
                        clusterKey);
        metadataWorkflow.setTableName(name);
        metadataWorkflow.setTableMetadata(tableMetadata);
        metadataWorkflow.setClusterName(clusterName);

        return metadataWorkflow;
    }

    private MetadataWorkflow buildMetadataWorkflowDropCatalog(MetadataStatement metadataStatement, String queryId)
                    throws PlanningException {
        MetadataWorkflow metadataWorkflow;
        DropCatalogStatement dropCatalogStatement = (DropCatalogStatement) metadataStatement;

        CatalogName catalog = dropCatalogStatement.getCatalogName();
        CatalogMetadata catalogMetadata = null;
        if (MetadataManager.MANAGER.exists(catalog)) {
            catalogMetadata = MetadataManager.MANAGER.getCatalog(catalog);
        }

        if (catalogMetadata == null ||
                        catalogMetadata.getTables().isEmpty() ||
                        catalogMetadata.getTables() == null) {
            MetadataManager.MANAGER.deleteCatalog(catalog, dropCatalogStatement.isIfExists());
            // Create MetadataWorkFlow
            metadataWorkflow = new MetadataWorkflow(queryId, null, ExecutionType.DROP_CATALOG, ResultType.RESULTS);
        } else {
            throw new PlanningException("This statement can't be planned: " + metadataStatement.toString() + ". " +
                            "All tables of the catalog must be removed before dropping the catalog.");
        }
        return metadataWorkflow;
    }

    private MetadataWorkflow buildMetadataWorkflowAlterCatalog(MetadataStatement metadataStatement, String queryId) {
        MetadataWorkflow metadataWorkflow;
        AlterCatalogStatement alterCatalogStatement = (AlterCatalogStatement) metadataStatement;
        CatalogName catalog = alterCatalogStatement.getCatalogName();
        CatalogMetadata catalogMetadata = MetadataManager.MANAGER.getCatalog(catalog);
        catalogMetadata.setOptions(alterCatalogStatement.getOptions());

        metadataWorkflow = new MetadataWorkflow(queryId, null, ExecutionType.ALTER_CATALOG, ResultType.RESULTS);

        metadataWorkflow.setCatalogMetadata(catalogMetadata);
        metadataWorkflow.setCatalogName(catalogMetadata.getName());
        return metadataWorkflow;
    }

    private MetadataWorkflow buildMetadataWorkflowCreateIndex(MetadataStatement metadataStatement, String queryId)
                    throws PlanningException {
        MetadataWorkflow metadataWorkflow;
        CreateIndexStatement createIndexStatement = (CreateIndexStatement) metadataStatement;

        TableMetadata tableMetadata = MetadataManager.MANAGER.getTable(createIndexStatement.getTableName());

        ClusterName clusterName = tableMetadata.getClusterRef();
        ClusterMetadata clusterMetadata = MetadataManager.MANAGER.getCluster(clusterName);

        String actorRefUri = findAnyActorRef(clusterMetadata, Status.ONLINE, Operations.CREATE_INDEX);

        metadataWorkflow = new MetadataWorkflow(queryId, actorRefUri, ExecutionType.CREATE_INDEX, ResultType.RESULTS);

        metadataWorkflow.setIndexName(createIndexStatement.getName());
        metadataWorkflow.setIfNotExists(createIndexStatement.isCreateIfNotExists());
        metadataWorkflow.setClusterName(clusterMetadata.getName());
        IndexName name = createIndexStatement.getName();

        Map<ColumnName, ColumnMetadata> columns = new HashMap<>();
        Set<ColumnName> targetColumns = createIndexStatement.getTargetColumns();
        for (ColumnName columnName : targetColumns) {
            ColumnMetadata columnMetadata = MetadataManager.MANAGER.getColumn(columnName);
            columns.put(columnName, columnMetadata);
        }
        IndexType type = createIndexStatement.getType();
        Map<Selector, Selector> options = createIndexStatement.getOptions();
        IndexMetadata indexMetadata = new IndexMetadata(name, columns, type, options);
        metadataWorkflow.setIndexMetadata(indexMetadata);
        return metadataWorkflow;
    }

    private MetadataWorkflow buildMetadataWorkflowDropIndex(MetadataStatement metadataStatement, String queryId)
                    throws PlanningException {

        MetadataWorkflow metadataWorkflow;
        DropIndexStatement dropIndexStatement = (DropIndexStatement) metadataStatement;

        IndexName indexName = dropIndexStatement.getName();

        TableMetadata tableMetadata = MetadataManager.MANAGER.getTable(indexName.getTableName());

        ClusterName clusterName = tableMetadata.getClusterRef();
        ClusterMetadata clusterMetadata = MetadataManager.MANAGER.getCluster(clusterName);

        String actorRefUri = findAnyActorRef(clusterMetadata, Status.ONLINE, Operations.DROP_INDEX);

        metadataWorkflow = new MetadataWorkflow(queryId, actorRefUri, ExecutionType.DROP_INDEX, ResultType.RESULTS);

        metadataWorkflow.setIndexName(dropIndexStatement.getName());
        metadataWorkflow.setIfExists(dropIndexStatement.isDropIfExists());
        metadataWorkflow.setClusterName(clusterMetadata.getName());

        metadataWorkflow.setIndexMetadata(tableMetadata.getIndexes().get(indexName));
        return metadataWorkflow;
    }

    private MetadataWorkflow buildMetadataWorkflowDropTable(MetadataStatement metadataStatement, String queryId)
                    throws PlanningException {

        MetadataWorkflow metadataWorkflow;
        DropTableStatement dropTableStatement = (DropTableStatement) metadataStatement;
        ExecutionType executionType = ExecutionType.DROP_TABLE;
        ResultType type = ResultType.RESULTS;

        TableMetadata tableMetadata = MetadataManager.MANAGER.getTable(dropTableStatement.getTableName());

        ClusterName clusterName = tableMetadata.getClusterRef();
        ClusterMetadata clusterMetadata = MetadataManager.MANAGER.getCluster(clusterName);

        String actorRefUri = findAnyActorRef(clusterMetadata, Status.ONLINE, Operations.DROP_TABLE);

        metadataWorkflow = new MetadataWorkflow(queryId, actorRefUri, executionType, type);

        metadataWorkflow.setTableMetadata(tableMetadata);
        metadataWorkflow.setTableName(tableMetadata.getName());
        metadataWorkflow.setClusterName(clusterMetadata.getName());

        return metadataWorkflow;
    }

    private MetadataWorkflow buildMetadataWorkflowAlterTable(MetadataStatement metadataStatement, String queryId)
                    throws PlanningException {

        MetadataWorkflow metadataWorkflow;
        AlterTableStatement alterTableStatement = (AlterTableStatement) metadataStatement;

        ExecutionType executionType = ExecutionType.ALTER_TABLE;
        ResultType type = ResultType.RESULTS;

        TableMetadata tableMetadata = MetadataManager.MANAGER.getTable(alterTableStatement.getTableName());

        ColumnMetadata alterColumnMetadata = alterTableStatement.getColumnMetadata();

        AlterOptions alterOptions;
        switch (alterTableStatement.getOption()) {
        case ADD_COLUMN:
            alterOptions = new AlterOptions(AlterOperation.ADD_COLUMN, null, alterColumnMetadata);
            break;
        case ALTER_COLUMN:
            alterOptions = new AlterOptions(AlterOperation.ALTER_COLUMN, null, alterColumnMetadata);
            break;
        case DROP_COLUMN:
            alterOptions = new AlterOptions(AlterOperation.DROP_COLUMN, null, alterColumnMetadata);
            break;
        case ALTER_OPTIONS:
            alterOptions = new AlterOptions(AlterOperation.ALTER_OPTIONS, alterTableStatement.getProperties(),
                            alterColumnMetadata);
            break;
        default:
            throw new PlanningException("This statement can't be planned: " + metadataStatement.toString());
        }

        ClusterName clusterName = tableMetadata.getClusterRef();
        ClusterMetadata clusterMetadata = MetadataManager.MANAGER.getCluster(clusterName);

        String actorRefUri = findAnyActorRef(clusterMetadata, Status.ONLINE, Operations.ALTER_TABLE);

        metadataWorkflow = new MetadataWorkflow(queryId, actorRefUri, executionType, type);

        metadataWorkflow.setTableName(tableMetadata.getName());
        metadataWorkflow.setAlterOptions(alterOptions);
        metadataWorkflow.setClusterName(clusterMetadata.getName());

        return metadataWorkflow;
    }

    private MetadataWorkflow buildMetadataWorkflowImportMetadata(MetadataStatement metadataStatement, String queryId)
                    throws PlanningException {

        MetadataWorkflow metadataWorkflow;
        ImportMetadataStatement importMetadataStatement = (ImportMetadataStatement) metadataStatement;

        ExecutionType executionType = ExecutionType.DISCOVER_METADATA;
        if (!importMetadataStatement.isDiscover()) {
            executionType = ExecutionType.IMPORT_CATALOGS;
            if (importMetadataStatement.getTableName() != null) {
                executionType = ExecutionType.IMPORT_TABLE;
            } else if (importMetadataStatement.getCatalogName() != null) {
                executionType = ExecutionType.IMPORT_CATALOG;
            }
        }

        ResultType type = ResultType.RESULTS;

        ClusterName clusterName = importMetadataStatement.getClusterName();
        ClusterMetadata clusterMetadata = MetadataManager.MANAGER.getCluster(clusterName);

        String actorRefUri = findAnyActorRef(clusterMetadata, Status.ONLINE, Operations.IMPORT_METADATA);

        metadataWorkflow = new MetadataWorkflow(queryId, actorRefUri, executionType, type);

        metadataWorkflow.setClusterName(clusterName);
        metadataWorkflow.setCatalogName(importMetadataStatement.getCatalogName());
        metadataWorkflow.setTableName(importMetadataStatement.getTableName());

        return metadataWorkflow;
    }

    private ExecutionWorkflow buildMetadataWorkflow(MetadataValidatedQuery query) throws PlanningException {
        MetadataStatement metadataStatement = query.getStatement();
        String queryId = query.getQueryId();
        MetadataWorkflow metadataWorkflow;

        if (metadataStatement instanceof CreateCatalogStatement) {
            metadataWorkflow = buildMetadataWorkflowCreateCatalog(metadataStatement, queryId);
        } else if (metadataStatement instanceof CreateTableStatement) {
            metadataWorkflow = buildMetadataWorkflowCreateTable(metadataStatement, queryId);
        } else if (metadataStatement instanceof DropCatalogStatement) {
            metadataWorkflow = buildMetadataWorkflowDropCatalog(metadataStatement, queryId);
        } else if (metadataStatement instanceof AlterCatalogStatement) {
            metadataWorkflow = buildMetadataWorkflowAlterCatalog(metadataStatement, queryId);
        } else if (metadataStatement instanceof CreateIndexStatement) {
            metadataWorkflow = buildMetadataWorkflowCreateIndex(metadataStatement, queryId);
        } else if (metadataStatement instanceof DropIndexStatement) {
            metadataWorkflow = buildMetadataWorkflowDropIndex(metadataStatement, queryId);
        } else if (metadataStatement instanceof DropTableStatement) {
            metadataWorkflow = buildMetadataWorkflowDropTable(metadataStatement, queryId);
        } else if (metadataStatement instanceof AlterTableStatement) {
            metadataWorkflow = buildMetadataWorkflowAlterTable(metadataStatement, queryId);
        } else if (metadataStatement instanceof ImportMetadataStatement) {
            metadataWorkflow = buildMetadataWorkflowImportMetadata(metadataStatement, queryId);
        } else {
            throw new PlanningException("This statement can't be planned: " + metadataStatement.toString());
        }

        return metadataWorkflow;
    }

    private ExecutionWorkflow buildManagementWorkflow(MetadataValidatedQuery query) throws PlanningException {
        MetadataStatement metadataStatement = query.getStatement();
        String queryId = query.getQueryId();
        ManagementWorkflow managementWorkflow;

        if (metadataStatement instanceof AttachClusterStatement) {

            // Create parameters for metadata workflow
            AttachClusterStatement attachClusterStatement = (AttachClusterStatement) metadataStatement;
            ExecutionType executionType = ExecutionType.ATTACH_CLUSTER;
            ResultType type = ResultType.RESULTS;

            managementWorkflow = new ManagementWorkflow(queryId, null, executionType, type);

            // Add required information
            managementWorkflow.setClusterName(attachClusterStatement.getClusterName());
            managementWorkflow.setDatastoreName(attachClusterStatement.getDatastoreName());
            managementWorkflow.setOptions(attachClusterStatement.getOptions());

        } else if (metadataStatement instanceof DetachClusterStatement) {
            DetachClusterStatement detachClusterStatement = (DetachClusterStatement) metadataStatement;
            ExecutionType executionType = ExecutionType.DETACH_CLUSTER;
            ResultType type = ResultType.RESULTS;

            managementWorkflow = new ManagementWorkflow(queryId, null, executionType, type);
            String clusterName = detachClusterStatement.getClusterName();
            managementWorkflow.setClusterName(new ClusterName(clusterName));

        } else if (metadataStatement instanceof AttachConnectorStatement) {
            // Create parameters for metadata workflow
            AttachConnectorStatement attachConnectorStatement = (AttachConnectorStatement) metadataStatement;
            ExecutionType executionType = ExecutionType.ATTACH_CONNECTOR;
            ResultType type = ResultType.RESULTS;

            ConnectorMetadata connector = MetadataManager.MANAGER
                            .getConnector(attachConnectorStatement.getConnectorName());

            managementWorkflow = new ManagementWorkflow(queryId, connector.getActorRef(), executionType, type);

            // Add required information
            managementWorkflow.setConnectorName(attachConnectorStatement.getConnectorName());
            managementWorkflow.setClusterName(attachConnectorStatement.getClusterName());
            managementWorkflow.setOptions(attachConnectorStatement.getOptions());
            managementWorkflow.setPageSize(attachConnectorStatement.getPagination());

        } else if (metadataStatement instanceof DetachConnectorStatement) {
            DetachConnectorStatement detachConnectorStatement = (DetachConnectorStatement) metadataStatement;
            ExecutionType executionType = ExecutionType.DETACH_CONNECTOR;
            ResultType type = ResultType.RESULTS;

            ConnectorMetadata connector = MetadataManager.MANAGER
                            .getConnector(detachConnectorStatement.getConnectorName());

            managementWorkflow = new ManagementWorkflow(queryId, connector.getActorRef(), executionType, type);
            managementWorkflow.setConnectorName(detachConnectorStatement.getConnectorName());
            managementWorkflow.setClusterName(detachConnectorStatement.getClusterName());

        } else if (metadataStatement instanceof AlterClusterStatement) {
            AlterClusterStatement alterClusterStatement = (AlterClusterStatement) metadataStatement;
            ExecutionType executionType = ExecutionType.ALTER_CLUSTER;
            ResultType type = ResultType.RESULTS;

            ClusterMetadata clusterMetadata = MetadataManager.MANAGER
                            .getCluster(alterClusterStatement.getClusterName());

            managementWorkflow = new ManagementWorkflow(queryId, null, executionType, type);
            managementWorkflow.setClusterName(alterClusterStatement.getClusterName());
            managementWorkflow.setOptions(alterClusterStatement.getOptions());
            managementWorkflow.setDatastoreName(clusterMetadata.getDataStoreRef());

        } else {
            throw new PlanningException("This statement can't be planned: " + metadataStatement.toString());
        }

        return managementWorkflow;
    }

    /**
     * Check if a catalog was already registered in a cluster.
     *
     * @param catalogName catalog to be searched.
     * @param clusterName cluster that should contain the catalog.
     * @return if the catalog was found in the cluster.
     */
    private boolean existsCatalogInCluster(CatalogName catalogName, ClusterName clusterName) {
        ClusterMetadata cluster = MetadataManager.MANAGER.getCluster(clusterName);
        boolean result = false;
        if (cluster.getPersistedCatalogs().contains(catalogName)) {
            return true;
        }
        return result;
    }

    private StorageWorkflow buildExecutionWorkflowInsert(StorageValidatedQuery query, String queryId)
                    throws PlanningException {
        StorageWorkflow storageWorkflow;
        InsertIntoStatement insertIntoStatement = (InsertIntoStatement) query.getStatement();

        String actorRef;

        TableName tableName = insertIntoStatement.getTableName();
        Row row = getInsertRow(insertIntoStatement);

        TableMetadata tableMetadata = getTableMetadata(tableName);
        ClusterMetadata clusterMetadata = getClusterMetadata(tableMetadata.getClusterRef());

        if (insertIntoStatement.isIfNotExists()) {
            actorRef = findAnyActorRef(clusterMetadata, Status.ONLINE, Operations.INSERT_IF_NOT_EXISTS);
        } else {
            actorRef = findAnyActorRef(clusterMetadata, Status.ONLINE, Operations.INSERT);
        }

        storageWorkflow = new StorageWorkflow(queryId, actorRef, ExecutionType.INSERT, ResultType.RESULTS);
        storageWorkflow.setClusterName(tableMetadata.getClusterRef());
        storageWorkflow.setTableMetadata(tableMetadata);
        storageWorkflow.setRow(row);
        storageWorkflow.setIfNotExists(insertIntoStatement.isIfNotExists());

        return storageWorkflow;
    }

    private StorageWorkflow buildExecutionWorkflowDelete(StorageValidatedQuery query, String queryId)
                    throws PlanningException {
        StorageWorkflow storageWorkflow;
        DeleteStatement deleteStatement = (DeleteStatement) query.getStatement();

        // Find connector
        String actorRef;
        TableMetadata tableMetadata = getTableMetadata(deleteStatement.getTableName());
        ClusterMetadata clusterMetadata = getClusterMetadata(tableMetadata.getClusterRef());

        List<Filter> filters = new ArrayList<>();
        Set<Operations> requiredOperations = new HashSet<>();

        List<Relation> relations = deleteStatement.getWhereClauses();
        if ((relations == null) || (relations.isEmpty())) {
            requiredOperations.add(Operations.DELETE_NO_FILTERS);
        } else {
            for (Relation relation : deleteStatement.getWhereClauses()) {
                Operations operation = getFilterOperation(tableMetadata, "DELETE", relation.getLeftTerm(),
                                relation.getOperator());
                Filter filter = new Filter(operation, relation);
                filters.add(filter);
                requiredOperations.add(filter.getOperation());
            }
        }

        actorRef = findAnyActorRef(clusterMetadata, Status.ONLINE,
                        requiredOperations.toArray(new Operations[requiredOperations.size()]));

        storageWorkflow = new StorageWorkflow(queryId, actorRef, ExecutionType.DELETE_ROWS, ResultType.RESULTS);

        storageWorkflow.setClusterName(clusterMetadata.getName());
        storageWorkflow.setTableName(tableMetadata.getName());

        storageWorkflow.setWhereClauses(filters);
        return storageWorkflow;
    }

    private StorageWorkflow buildExecutionWorkflowUpdate(StorageValidatedQuery query, String queryId)
                    throws PlanningException {
        StorageWorkflow storageWorkflow;
        UpdateTableStatement updateTableStatement = (UpdateTableStatement) query.getStatement();

        // Find connector
        String actorRef;
        TableMetadata tableMetadata = getTableMetadata(updateTableStatement.getTableName());
        ClusterMetadata clusterMetadata = getClusterMetadata(tableMetadata.getClusterRef());

        List<Filter> filters = new ArrayList<>();
        Set<Operations> requiredOperations = new HashSet<>();

        List<Relation> relations = updateTableStatement.getWhereClauses();
        if ((relations == null) || (relations.isEmpty())) {
            requiredOperations.add(Operations.UPDATE_NO_FILTERS);
        } else {
            for (Relation relation : updateTableStatement.getWhereClauses()) {
                Operations operation = getFilterOperation(tableMetadata, "UPDATE", relation.getLeftTerm(),
                                relation.getOperator());
                Filter filter = new Filter(operation, relation);
                filters.add(filter);
                requiredOperations.add(filter.getOperation());
            }
        }

        actorRef = findAnyActorRef(clusterMetadata, Status.ONLINE,
                        requiredOperations.toArray(new Operations[requiredOperations.size()]));

        storageWorkflow = new StorageWorkflow(queryId, actorRef, ExecutionType.UPDATE_TABLE, ResultType.RESULTS);

        storageWorkflow.setClusterName(clusterMetadata.getName());
        storageWorkflow.setTableName(tableMetadata.getName());
        storageWorkflow.setAssignments(updateTableStatement.getAssignations());

        storageWorkflow.setWhereClauses(filters);

        return storageWorkflow;
    }

    private StorageWorkflow buildExecutionWorkflowTruncate(StorageValidatedQuery query, String queryId)
                    throws PlanningException {
        StorageWorkflow storageWorkflow;
        TruncateStatement truncateStatement = (TruncateStatement) query.getStatement();

        // Find connector
        String actorRef;
        TableMetadata tableMetadata = getTableMetadata(truncateStatement.getTableName());
        ClusterMetadata clusterMetadata = getClusterMetadata(tableMetadata.getClusterRef());

        actorRef = findAnyActorRef(clusterMetadata, Status.ONLINE, Operations.TRUNCATE_TABLE);

        storageWorkflow = new StorageWorkflow(queryId, actorRef, ExecutionType.TRUNCATE_TABLE, ResultType.RESULTS);

        storageWorkflow.setClusterName(clusterMetadata.getName());
        storageWorkflow.setTableName(tableMetadata.getName());
        return storageWorkflow;
    }

    protected ExecutionWorkflow buildExecutionWorkflow(StorageValidatedQuery query) throws PlanningException {

        StorageWorkflow storageWorkflow;
        String queryId = query.getQueryId();

        if (query.getStatement() instanceof InsertIntoStatement) {
            storageWorkflow = buildExecutionWorkflowInsert(query, queryId);
        } else if (query.getStatement() instanceof DeleteStatement) {
            storageWorkflow = buildExecutionWorkflowDelete(query, queryId);
        } else if (query.getStatement() instanceof UpdateTableStatement) {
            storageWorkflow = buildExecutionWorkflowUpdate(query, queryId);
        } else if (query.getStatement() instanceof TruncateStatement) {
            storageWorkflow = buildExecutionWorkflowTruncate(query, queryId);
        } else {
            throw new PlanningException("This statement is not supported yet");
        }

        return storageWorkflow;
    }

    private Row getInsertRow(InsertIntoStatement statement) throws PlanningException {
        Row row = new Row();

        List<Selector> values = statement.getCellValues();
        List<ColumnName> ids = statement.getIds();

        for (int i = 0; i < ids.size(); i++) {
            ColumnName columnName = ids.get(i);
            Selector value = values.get(i);
            CoreUtils coreUtils = CoreUtils.create();
            Object cellContent = coreUtils.convertSelectorToObject(value, columnName);
            Cell cell = new Cell(cellContent);
            row.addCell(columnName.getName(), cell);
        }
        return row;
    }

    private ClusterMetadata getClusterMetadata(ClusterName clusterRef) throws PlanningException {
        ClusterMetadata clusterMetadata = MetadataManager.MANAGER.getCluster(clusterRef);

        if (clusterMetadata == null) {
            throw new PlanningException("There is not cluster metadata for Storage Operation");
        }
        return clusterMetadata;
    }

    private TableMetadata getTableMetadata(TableName tableName) throws PlanningException {
        TableMetadata tableMetadata = MetadataManager.MANAGER.getTable(tableName);
        if (tableMetadata == null) {
            throw new PlanningException("There is not specified Table for Storage Operation");
        }
        return tableMetadata;
    }

    /**
     * Add the columns that need to be retrieved to the initial steps map.
     *
     * @param projectSteps The map associating table names to Project steps.
     * @param query        The query to be planned.
     */
    private void addProjectedColumns(Map<String, LogicalStep> projectSteps, SelectValidatedQuery query) {
        for (ColumnName cn : query.getColumns()) {
            Project.class.cast(projectSteps.get(cn.getTableName().getQualifiedName())).addColumn(cn);
        }
    }

    /**
     * Get the filter operation depending on the type of column and the selector of the where clauses.
     *
     * @param tableMetadata The table metadata.
     * @param statement     Statement type.
     * @param selector      The relationship selector.
     * @param operator      The relationship operator.
     * @return An {@link com.stratio.crossdata.common.metadata.Operations} object.
     */
    protected Operations getFilterOperation(final TableMetadata tableMetadata, final String statement,
                    final Selector selector, final Operator operator) {
        StringBuilder sb = new StringBuilder(statement.toUpperCase());
        sb.append("_");
        ColumnSelector cs = ColumnSelector.class.cast(selector);
        if (tableMetadata.isPK(cs.getName())) {
            sb.append("PK_");
        } else if (tableMetadata.isIndexed(cs.getName())) {
            sb.append("INDEXED_");
        } else {
            sb.append("NON_INDEXED_");
        }
        sb.append(operator.name());
        return Operations.valueOf(sb.toString());
    }

    /**
     * Add Filter operations after the Project. The Filter operations to be applied are associated
     * with the where clause found.
     *
     * @param lastSteps        The map associating table names to Project steps.
     * @param tableMetadataMap A map with the table metadata indexed by table name.
     * @param query            The query to be planned.
     * @return The resulting map of logical steps.
     */
    private Map<String, LogicalStep> addFilter(Map<String, LogicalStep> lastSteps,
                    Map<String, TableMetadata> tableMetadataMap, SelectValidatedQuery query) {
        LogicalStep previous;
        TableMetadata tm;
        Selector s;
        for (Relation r : query.getRelations()) {
            s = r.getLeftTerm();
            //TODO Support left-side functions that contain columns of several tables.
            tm = tableMetadataMap.get(s.getSelectorTablesAsString());
            if (tm != null) {
                Operations op = getFilterOperation(tm, "FILTER", s, r.getOperator());
                Filter f = new Filter(op, r);
                previous = lastSteps.get(s.getSelectorTablesAsString());
                previous.setNextStep(f);
                f.setPrevious(previous);
                lastSteps.put(s.getSelectorTablesAsString(), f);
            } else {
                LOG.error("Cannot determine Filter for relation " + r.toString() + " on table " + s
                                .getSelectorTablesAsString());
            }

        }
        return lastSteps;
    }

    /**
     * Add a window operator for streaming queries.
     *
     * @param lastSteps The map associating table names to Project steps
     * @param stmt      The select statement.
     * @return The resulting map of logical steps.
     */
    private Map<String, LogicalStep> addWindow(Map<String, LogicalStep> lastSteps, SelectStatement stmt) {
        Window w = new Window(Operations.SELECT_WINDOW, stmt.getWindow());
        LogicalStep previous = lastSteps.get(stmt.getTableName().getQualifiedName());
        previous.setNextStep(w);
        w.setPrevious(previous);
        lastSteps.put(stmt.getTableName().getQualifiedName(), w);
        return lastSteps;
    }

    /**
     * Add the join logical steps.
     *
     * @param stepMap     The map of last steps after adding filters.
     * @param targetTable The target table of the join.
     * @param query       The query.
     * @return The resulting map of logical steps.
     */
    private Map<String, LogicalStep> addJoin(Map<String, LogicalStep> stepMap, String targetTable,
                    SelectValidatedQuery query) {
        InnerJoin queryJoin = query.getJoin();
        String id = new StringBuilder(targetTable).append("$").append(queryJoin.getTablename().getQualifiedName())
                        .toString();
        Join j = new Join(Operations.SELECT_INNER_JOIN, id);
        j.addSourceIdentifier(targetTable);
        j.addSourceIdentifier(queryJoin.getTablename().getQualifiedName());
        j.addJoinRelations(queryJoin.getOrderedRelations());
        StringBuilder sb = new StringBuilder(targetTable).append("$")
                        .append(queryJoin.getTablename().getQualifiedName());
        //Attach to input tables path
        LogicalStep t1 = stepMap.get(targetTable);
        LogicalStep t2 = stepMap.get(queryJoin.getTablename().getQualifiedName());
        t1.setNextStep(j);
        t2.setNextStep(j);
        j.addPreviousSteps(t1, t2);
        stepMap.put(sb.toString(), j);
        return stepMap;
    }

    /**
     * Get a Map associating fully qualified table names with their Project logical step.
     *
     * @param query            The query to be planned.
     * @param tableMetadataMap Map of table metadata.
     * @return A map with the projections.
     */
    protected Map<String, LogicalStep> getProjects(SelectValidatedQuery query,
                    Map<String, TableMetadata> tableMetadataMap) {
        Map<String, LogicalStep> projects = new HashMap<>();
        for (TableName tn : query.getTables()) {
            Project p = new Project(Operations.PROJECT, tn,
                            tableMetadataMap.get(tn.getQualifiedName()).getClusterRef());
            projects.put(tn.getQualifiedName(), p);
        }
        return projects;
    }

    /**
     * Generate a select operand.
     *
     * @param selectStatement  The source select statement.
     * @param tableMetadataMap A map with the table metadata indexed by table name.
     * @return A {@link com.stratio.crossdata.common.logicalplan.Select}.
     */
    protected Select generateSelect(SelectStatement selectStatement, Map<String, TableMetadata> tableMetadataMap)
                    throws PlanningException {
        LinkedHashMap<Selector, String> aliasMap = new LinkedHashMap<>();
        LinkedHashMap<String, ColumnType> typeMap = new LinkedHashMap<>();
        LinkedHashMap<Selector, ColumnType> typeMapFromColumnName = new LinkedHashMap<>();
        boolean addAll = false;
        Operations currentOperation = Operations.SELECT_OPERATOR;
        for (Selector s : selectStatement.getSelectExpression().getSelectorList()) {
            if (AsteriskSelector.class.isInstance(s)) {
                addAll = true;
            } else if (ColumnSelector.class.isInstance(s)) {
                ColumnSelector cs = ColumnSelector.class.cast(s);
                String alias;
                if (cs.getAlias() != null) {

                    alias = cs.getAlias();
                    if (aliasMap.containsValue(alias)) {
                        alias = cs.getColumnName().getTableName().getName() + "_" + cs.getAlias();
                    }

                    aliasMap.put(cs, alias);

                } else {

                    alias = cs.getName().getName();
                    if (aliasMap.containsValue(alias)) {
                        alias = cs.getColumnName().getTableName().getName() + "_" + cs.getName().getName();
                    }

                    aliasMap.put(cs, alias);

                }

                ColumnType colType = tableMetadataMap.get(cs.getSelectorTablesAsString()).getColumns().get(cs.getName())
                                .getColumnType();
                typeMapFromColumnName.put(cs, colType);

                typeMap.put(alias, colType);

            } else if (FunctionSelector.class.isInstance(s)) {
                currentOperation = Operations.SELECT_FUNCTIONS;
                FunctionSelector fs = FunctionSelector.class.cast(s);
                ColumnType ct = null;
                String alias;
                if (fs.getAlias() != null) {
                    alias = fs.getAlias();
                    if (aliasMap.containsValue(alias)) {
                        alias = fs.getTableName().getName() + "_" + fs.getAlias();
                    }

                } else {
                    alias = fs.getFunctionName();
                    if (aliasMap.containsValue(alias)) {
                        alias = fs.getTableName().getName() + "_" + fs.getFunctionName();
                    }
                }
                aliasMap.put(fs, alias);
                typeMapFromColumnName.put(fs, ct);
                typeMap.put(alias, ct);

            } else if (IntegerSelector.class.isInstance(s)) {
                generateLiteralSelect(aliasMap, typeMap, typeMapFromColumnName, s, ColumnType.INT);
            } else if (FloatingPointSelector.class.isInstance(s)) {
                generateLiteralSelect(aliasMap, typeMap, typeMapFromColumnName, s, ColumnType.DOUBLE);
            } else if (BooleanSelector.class.isInstance(s)) {
                generateLiteralSelect(aliasMap, typeMap, typeMapFromColumnName, s, ColumnType.BOOLEAN);
            } else if (StringSelector.class.isInstance(s)) {
                generateLiteralSelect(aliasMap, typeMap, typeMapFromColumnName, s, ColumnType.TEXT);
            } else {
                throw new PlanningException(s.getClass().getCanonicalName() + " is not supported yet.");
            }
        }

        if (addAll) {
            TableMetadata metadata = tableMetadataMap.get(selectStatement.getTableName().getQualifiedName());
            for (Map.Entry<ColumnName, ColumnMetadata> column : metadata.getColumns().entrySet()) {
                ColumnSelector cs = new ColumnSelector(column.getKey());

                String alias = column.getKey().getName();
                if (aliasMap.containsValue(alias)) {
                    alias = cs.getColumnName().getTableName().getName() + "_" + column.getKey().getName();
                }

                aliasMap.put(cs, alias);
                typeMapFromColumnName.put(cs, column.getValue().getColumnType());
                typeMap.put(alias, column.getValue().getColumnType());
            }
            if (selectStatement.getJoin() != null) {
                TableMetadata metadataJoin = tableMetadataMap
                                .get(selectStatement.getJoin().getTablename().getQualifiedName());
                for (Map.Entry<ColumnName, ColumnMetadata> column : metadataJoin.getColumns().entrySet()) {
                    ColumnSelector cs = new ColumnSelector(column.getKey());

                    String alias = column.getKey().getName();
                    if (aliasMap.containsValue(alias)) {
                        alias = column.getKey().getTableName().getName() + "_" + column.getKey().getName();
                    }

                    aliasMap.put(cs, alias);
                    typeMapFromColumnName.put(cs, column.getValue().getColumnType());
                    typeMap.put(alias, column.getValue().getColumnType());
                }
            }
        }

        return new Select(currentOperation, aliasMap, typeMap, typeMapFromColumnName);
    }

    private void generateLiteralSelect(LinkedHashMap<Selector, String> aliasMap,
                    LinkedHashMap<String, ColumnType> typeMap,
                    LinkedHashMap<Selector, ColumnType> typeMapFromColumnName, Selector selector,
                    ColumnType columnType) {

        String alias;
        if (selector.getAlias() != null) {
            alias = selector.getAlias();
            if (aliasMap.containsValue(alias)) {
                alias = selector.getColumnName().getTableName().getName() + "_" + selector.getAlias();
            }

        } else {
            alias = selector.getStringValue();

        }

        aliasMap.put(selector, alias);
        typeMapFromColumnName.put(selector, columnType);
        typeMap.put(alias, columnType);
    }

    private String findAnyActorRef(ClusterMetadata clusterMetadata, Status status, Operations... requiredOperations)
                    throws PlanningException {
        String actorRef = null;

        Map<ConnectorName, ConnectorAttachedMetadata> connectorAttachedRefs = clusterMetadata
                        .getConnectorAttachedRefs();

        Iterator it = connectorAttachedRefs.keySet().iterator();
        boolean found = false;
        while (it.hasNext() && !found) {
            ConnectorName connectorName = (ConnectorName) it.next();
            ConnectorMetadata connectorMetadata = MetadataManager.MANAGER.getConnector(connectorName);
            if ((connectorMetadata.getStatus() == status) && connectorMetadata.getSupportedOperations()
                            .containsAll(Arrays.asList(requiredOperations))) {
                actorRef = StringUtils.getAkkaActorRefUri(connectorMetadata.getActorRef());
                found = true;
            }
        }
        if (!found) {
            throw new PlanningException("There is no any attached connector supporting: " +
                            System.lineSeparator() + Arrays.toString(requiredOperations));
        }

        return actorRef;
    }

}
