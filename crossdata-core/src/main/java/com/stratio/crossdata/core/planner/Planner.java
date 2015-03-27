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
import java.util.Collection;
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
import com.stratio.crossdata.common.data.JoinType;
import com.stratio.crossdata.common.data.Row;
import com.stratio.crossdata.common.data.Status;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.IgnoreQueryException;
import com.stratio.crossdata.common.exceptions.PlanningException;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.common.executionplan.ExecutionPath;
import com.stratio.crossdata.common.executionplan.ExecutionType;
import com.stratio.crossdata.common.executionplan.ExecutionWorkflow;
import com.stratio.crossdata.common.executionplan.ManagementWorkflow;
import com.stratio.crossdata.common.executionplan.MetadataWorkflow;
import com.stratio.crossdata.common.executionplan.QueryWorkflow;
import com.stratio.crossdata.common.executionplan.ResultType;
import com.stratio.crossdata.common.executionplan.StorageWorkflow;
import com.stratio.crossdata.common.logicalplan.Disjunction;
import com.stratio.crossdata.common.logicalplan.Filter;
import com.stratio.crossdata.common.logicalplan.GroupBy;
import com.stratio.crossdata.common.logicalplan.IOperand;
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
import com.stratio.crossdata.common.logicalplan.Virtualizable;
import com.stratio.crossdata.common.logicalplan.Window;
import com.stratio.crossdata.common.manifest.FunctionType;
import com.stratio.crossdata.common.metadata.CatalogMetadata;
import com.stratio.crossdata.common.metadata.ClusterMetadata;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.ConnectorAttachedMetadata;
import com.stratio.crossdata.common.metadata.ConnectorMetadata;
import com.stratio.crossdata.common.metadata.DataType;
import com.stratio.crossdata.common.metadata.IndexMetadata;
import com.stratio.crossdata.common.metadata.IndexType;
import com.stratio.crossdata.common.metadata.Operations;
import com.stratio.crossdata.common.metadata.TableMetadata;
import com.stratio.crossdata.common.statements.structures.AbstractRelation;
import com.stratio.crossdata.common.statements.structures.AsteriskSelector;
import com.stratio.crossdata.common.statements.structures.BooleanSelector;
import com.stratio.crossdata.common.statements.structures.CaseWhenSelector;
import com.stratio.crossdata.common.statements.structures.ColumnSelector;
import com.stratio.crossdata.common.statements.structures.FloatingPointSelector;
import com.stratio.crossdata.common.statements.structures.FunctionSelector;
import com.stratio.crossdata.common.statements.structures.IntegerSelector;
import com.stratio.crossdata.common.statements.structures.Operator;
import com.stratio.crossdata.common.statements.structures.Relation;
import com.stratio.crossdata.common.statements.structures.RelationDisjunction;
import com.stratio.crossdata.common.statements.structures.RelationSelector;
import com.stratio.crossdata.common.statements.structures.SelectExpression;
import com.stratio.crossdata.common.statements.structures.SelectSelector;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.common.statements.structures.StringSelector;
import com.stratio.crossdata.common.utils.Constants;
import com.stratio.crossdata.common.utils.StringUtils;
import com.stratio.crossdata.core.metadata.MetadataManager;
import com.stratio.crossdata.core.query.BaseQuery;
import com.stratio.crossdata.core.query.MetadataPlannedQuery;
import com.stratio.crossdata.core.query.MetadataValidatedQuery;
import com.stratio.crossdata.core.query.SelectParsedQuery;
import com.stratio.crossdata.core.query.SelectPlannedQuery;
import com.stratio.crossdata.core.query.SelectValidatedQuery;
import com.stratio.crossdata.core.query.StoragePlannedQuery;
import com.stratio.crossdata.core.query.StorageValidatedQuery;
import com.stratio.crossdata.core.statements.*;
import com.stratio.crossdata.core.structures.ExtendedSelectSelector;
import com.stratio.crossdata.core.structures.InnerJoin;
import com.stratio.crossdata.core.utils.CoreUtils;
import com.stratio.crossdata.core.validator.Validator;

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
        query.optimizeQuery();
        LogicalWorkflow workflow = buildWorkflow(query);
        //Plan the workflow execution into different connectors.
        ExecutionWorkflow executionWorkflow = buildExecutionWorkflow(query.getQueryId(), workflow);
        //Return the planned query.
        SelectPlannedQuery plannedQuery = new SelectPlannedQuery(query, executionWorkflow);
        //Add the sql direct query to the logical workflow.
        ((QueryWorkflow)plannedQuery
                .getExecutionWorkflow())
                .getWorkflow()
                .setSqlDirectQuery(query.getStatement().toSQLString());
        return plannedQuery;
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

        logCandidateConnectors(candidatesConnectors);

        List<ExecutionPath> executionPaths = new ArrayList<>();
        Map<UnionStep, Set<ExecutionPath>> unionSteps = new HashMap<>();
        //Iterate through the initial steps and build valid execution paths
        for (LogicalStep initialStep : workflow.getInitialSteps()) {
            TableName targetTable = ((Project) initialStep).getTableName();
            LOG.info("Table: " + targetTable);
            ExecutionPath ep = defineExecutionPath(initialStep, candidatesConnectors.get(targetTable));
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

    private void logCandidateConnectors(Map<TableName, List<ConnectorMetadata>> candidatesConnectors) {
        StringBuilder sb = new StringBuilder("Candidate connectors: ").append(System.lineSeparator());
        for (Map.Entry<TableName, List<ConnectorMetadata>> tableEntry: candidatesConnectors.entrySet()) {
            for (ConnectorMetadata cm: tableEntry.getValue()) {
                sb.append("table: ").append(tableEntry.getKey().toString()).append(" ").append(cm.getName()).append(" ")
                        .append(cm.getActorRef()).append(System.lineSeparator());
            }
        }
        LOG.info(sb.toString());
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
            if (TransformationStep.class.isInstance(paths[0].getLast()) && TransformationStep.class
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
        List<ClusterName> involvedClusters = new ArrayList<>(executionPaths.size());

        for (ExecutionPath path : executionPaths) {
            Project project = (Project) path.getInitial();
            involvedClusters.add(project.getClusterName());
            initialSteps.add(path.getInitial());
        }
        LogicalWorkflow workflow = new LogicalWorkflow(initialSteps);

        //Select an actor
        ConnectorMetadata connectorMetadata = findBestConnector(connectors, involvedClusters);

        String selectedActorUri = StringUtils.getAkkaActorRefUri(connectorMetadata.getActorRef(), false);

        updateFunctionsFromSelect(workflow, connectorMetadata.getName());

        if ((connectorMetadata.getSupportedOperations().contains(Operations.PAGINATION)) && (
                connectorMetadata.getPageSize() > 0)) {
            workflow.setPagination(connectorMetadata.getPageSize());
        }

        return new QueryWorkflow(queryId, selectedActorUri, ExecutionType.SELECT, type, workflow);
    }

    private ConnectorMetadata findBestConnector(List<ConnectorMetadata> connectors, List<ClusterName> clusters) {
        //TODO: Add logic to this method according to native or not
        //TODO: Add logic to this method according to statistics
        ConnectorMetadata highestPriorityConnector = null;
        int minPriority = Integer.MAX_VALUE;

        for (ConnectorMetadata connector : connectors) {
            if (connector.getPriorityFromClusterNames(clusters) < minPriority) {
                minPriority = connector.getPriorityFromClusterNames(clusters);
                highestPriorityConnector = connector;
                LOG.debug("New top priority connector found: " + connector.getName());
            }
        }

        return highestPriorityConnector;
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
        List<ClusterName> involvedClusters = new ArrayList<>(executionPaths.size());

        for (ExecutionPath path : executionPaths) {
            Project project = (Project) path.getInitial();
            involvedClusters.add(project.getClusterName());
            initialSteps.add(project);
            path.getLast().setNextStep(mergePath.getInitial());
        }

        LogicalWorkflow workflow = new LogicalWorkflow(initialSteps);

        //Select an actor
        ConnectorMetadata connectorMetadata = findBestConnector(mergePath.getAvailableConnectors(), involvedClusters);
        String selectedActorUri = StringUtils.getAkkaActorRefUri(connectorMetadata.getActorRef(), false);

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

                    if (current.getOperation().getOperationsStr().toLowerCase().contains("function")) {
                        Set<String> sFunctions = MetadataManager.MANAGER.getSupportedFunctionNames(connector.getName());
                        switch (current.getOperation()) {
                        case SELECT_FUNCTIONS:
                            Select select = (Select) current;
                            Set<Selector> cols = select.getColumnMap().keySet();
                            if (!checkFunctionsConsistency(connector, sFunctions, cols)) {
                                toRemove.add(connector);
                            }
                            break;
                        default:
                            throw new PlanningException(current.getOperation() + " not supported yet.");
                        }
                    }

                    if (current instanceof Virtualizable && ((Virtualizable) current).isVirtual() && !connector
                            .supports(Operations.SELECT_SUBQUERY)) {
                        toRemove.add(connector);
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
     * Checks whether the selectors are consistent with the functions supported by the connector.
     *
     * @param connectorMetadata  The connector metadata.
     * @param supportedFunctions The functions which are supported by the connector.
     * @param selectors          The set of selector
     * @return true if the selectors are consistent with the functions;
     * @throws PlanningException
     */
    private boolean checkFunctionsConsistency(ConnectorMetadata connectorMetadata, Set<String> supportedFunctions,
            Set<Selector> selectors) throws PlanningException {

        boolean areFunctionsConsistent = true;
        Iterator<Selector> selectorIterator = selectors.iterator();

        while (selectorIterator.hasNext() && areFunctionsConsistent) {
            Selector selector = selectorIterator.next();
            if (selector instanceof FunctionSelector) {
                FunctionSelector fSelector = (FunctionSelector) selector;
                if (!supportedFunctions.contains(fSelector.getFunctionName())) {
                    areFunctionsConsistent = false;
                    break;
                } else {
                    if (!MetadataManager.MANAGER.checkInputSignature(fSelector, connectorMetadata.getName())) {
                        areFunctionsConsistent = false;
                        break;
                    }
                }
                areFunctionsConsistent = checkFunctionsConsistency(connectorMetadata, supportedFunctions,
                        new HashSet<>(fSelector.getFunctionColumns()));
            }
        }
        return areFunctionsConsistent;
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

        List<LogicalStep> initialSteps = new ArrayList<>();

        Map<String, TableMetadata> tableMetadataMap = new LinkedHashMap<>();
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
        if (!query.getJoinList().isEmpty()) {
            processed = addJoin((LinkedHashMap) processed, selectTable, query);
        }

        //Initial steps.
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

        //Include previous Select step for join queries
        boolean firstPath = true;
        for (LogicalStep initialStep : initialSteps) {
            LogicalStep step = initialStep;
            LogicalStep previousStepToUnion = initialStep;
            while ((step != null) && (!UnionStep.class.isInstance(step))) {
                previousStepToUnion = step;
                step = step.getNextStep();
            }
            if (step == null) {
                continue;
            } else {
                // Create Select step here
                UnionStep unionStep = (UnionStep) step;
                //Store all the project steps
                Map<String, TableMetadata> partialTableMetadataMap = new LinkedHashMap<>();
                for (String key : tableMetadataMap.keySet()) {
                    if (Project.class.isInstance(initialStep)) {
                        Project projectStep = (Project) initialStep;
                        if (projectStep.getTableName().getQualifiedName().equals(key)) {
                            partialTableMetadataMap.put(key, tableMetadataMap.get(key));
                            break;
                        }
                    }
                }

                // Generate a list of fake Select for Join Table
                List<SelectStatement> partialSelectList = new ArrayList<>();

                if (!ss.getJoinList().isEmpty()) {
                    for (InnerJoin innerJoin : ss.getJoinList()) {
                        if (Project.class.cast(initialStep).getTableName().getQualifiedName().equalsIgnoreCase(
                                innerJoin.getTablename().getQualifiedName())) {
                            List<Selector> selectorList = new ArrayList<>();
                            Selector firstSelector = ss.getSelectExpression().getSelectorList().get(0);
                            if (firstSelector instanceof ColumnSelector) {
                                Project currentProject = (Project) initialStep;
                                List<ColumnName> columnsFromProject = currentProject.getColumnList();
                                for (ColumnName col : columnsFromProject) {
                                    selectorList.add(new ColumnSelector(col));
                                }
                            } else {
                                TableMetadata tableMetadata =
                                        MetadataManager.MANAGER.getTable(innerJoin.getTablename());
                                for (ColumnMetadata cm : tableMetadata.getColumns().values()) {
                                    ColumnSelector cs = new ColumnSelector(cm.getName());
                                    selectorList.add(cs);
                                }
                            }
                            SelectExpression selectExpression = new SelectExpression(selectorList);
                            TableName tableNameJoin = innerJoin.getTablename();
                            partialSelectList.add(new SelectStatement(selectExpression, tableNameJoin));
                        } else {
                            List<Selector> selectorList = new ArrayList<>();
                            Project currentProject = (Project) initialStep;
                            List<ColumnName> columnsFromProject = currentProject.getColumnList();
                            for (ColumnName col : columnsFromProject) {
                                selectorList.add(new ColumnSelector(col));
                            }
                            partialSelectList.add(new SelectStatement(new SelectExpression(selectorList),
                                    ss.getTableName()));

                        }

                    }
                } else {
                    List<Selector> selectorList = new ArrayList<>();
                    Project currentProject = (Project) initialStep;
                    List<ColumnName> columnsFromProject = currentProject.getColumnList();
                    for (ColumnName col : columnsFromProject) {
                        selectorList.add(new ColumnSelector(col));
                    }
                    partialSelectList.add(new SelectStatement(new SelectExpression(selectorList), ss.getTableName()));
                    partialTableMetadataMap = tableMetadataMap;
                }

                //link previous select to the join
                for (SelectStatement partialSelect : removeDuplicateSelects(partialSelectList)) {
                    Select selectStep = generateSelect(partialSelect, partialTableMetadataMap);

                    previousStepToUnion.setNextStep(selectStep);

                    selectStep.setPrevious(previousStepToUnion);
                    selectStep.setNextStep(unionStep);

                    List<LogicalStep> previousStepsToUnion = unionStep.getPreviousSteps();
                    if (firstPath) {
                        previousStepsToUnion.clear();
                        firstPath = false;
                    }
                    previousStepsToUnion.add(selectStep);
                    unionStep.setPreviousSteps(previousStepsToUnion);
                    previousStepsToUnion=removeDuplicateLS(unionStep.getPreviousSteps());
                    previousStepsToUnion=removeProjects(previousStepsToUnion);
                    unionStep.setPreviousSteps(previousStepsToUnion);
                }
            }
        }

        //Inject select post union step
        for (LogicalStep initialStep : initialSteps) {
            LogicalStep step = initialStep;
            while ((step != null) && (!UnionStep.class.isInstance(step))) {
                step = step.getNextStep();
            }
            if (step == null) {
                continue;
            } else {
                // Create Select step here
                UnionStep unionStep = (UnionStep) step;
                //Generate a select for next step of union step.
                if ((unionStep.getNextStep() == null) || UnionStep.class.isInstance(unionStep.getNextStep())) {
                    List<Selector> selectorJoinList = new ArrayList<>();
                    Map<String, TableMetadata> joinTableMetadataMap = new HashMap<>();

                    List<LogicalStep> projects = unionStep.getPreviousSteps();
                    for (LogicalStep ls : projects) {
                        if (Select.class.isInstance(ls)) {
                            for (Selector selector : ((Select) ls).getColumnMap().keySet()) {
                                selectorJoinList.add((ColumnSelector) selector);
                                joinTableMetadataMap.put(selector.getColumnName().getTableName().getQualifiedName(),
                                        tableMetadataMap.get(selector.getColumnName().getTableName().getQualifiedName
                                                ()));
                            }
                        }
                        if (Project.class.isInstance(ls)) {
                            List<ColumnName> columnsFromProject = ((Project) ls).getColumnList();
                            for (ColumnName col : columnsFromProject) {
                                selectorJoinList.add(new ColumnSelector(col));
                                joinTableMetadataMap.put(col.getTableName().getQualifiedName(),
                                        tableMetadataMap.get(col.getTableName().getQualifiedName
                                                ()));
                            }
                        }
                    }
                    TableName tableName = tableMetadataMap.get(((Join) unionStep).getSourceIdentifiers().get(0))
                            .getName();

                    SelectStatement joinNextSelect = new SelectStatement(new SelectExpression(selectorJoinList),
                            tableName);
                    Select joinSelect = generateSelect(joinNextSelect, joinTableMetadataMap);
                    if (UnionStep.class.isInstance(unionStep.getNextStep())) {
                        LogicalStep nextUnion = unionStep.getNextStep();
                        nextUnion.getPreviousSteps().add(joinSelect);
                        joinSelect.setNextStep(nextUnion);
                        joinSelect.setPrevious(unionStep);
                        unionStep.setNextStep(joinSelect);
                    } else {
                        unionStep.setNextStep(joinSelect);
                        joinSelect.setPrevious(unionStep);
                    }
                }
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
        if (Select.class.isInstance(last)) {
            //redirect last position to final select
            last = initial;
            while (last.getNextStep().getNextStep() != null) {
                last = last.getNextStep();
            }

        }
        last.setNextStep(finalSelect);
        finalSelect.setPrevious(last);

        LogicalWorkflow workflow = new LogicalWorkflow(initialSteps);

        if (query.getSubqueryValidatedQuery() != null) {
            LogicalWorkflow subqueryWorkflow = buildWorkflow(query.getSubqueryValidatedQuery());
            workflow = rearrangeWorkflow(workflow, subqueryWorkflow);
        }

        workflow.setLastStep(finalSelect);

        return workflow;
    }

    private List<LogicalStep> removeProjects(List<LogicalStep> previousSteps) {
        List<LogicalStep> newList=new ArrayList<>();
        for (LogicalStep ls:previousSteps){
            if(!Project.class.isInstance(ls)){
                newList.add(ls);
            }
        }
        return newList;
    }

    private List<LogicalStep> removeDuplicateLS(List<LogicalStep> previousSteps) {
        for(LogicalStep ls1:previousSteps){
            for(LogicalStep ls2:previousSteps){
                if(ls1!=ls2){
                    if (ls1.toString().equals(ls2.toString())){
                        previousSteps.remove(ls2);
                        break;
                    }
                }
            }
        }
        return previousSteps;
    }

    private List<SelectStatement> removeDuplicateSelects(List<SelectStatement> partialSelectList) {

        for (SelectStatement ss1:partialSelectList){
            for (SelectStatement ss2:partialSelectList){
               if (ss1!=ss2){
                   if (ss1.toString().equals(ss2.toString())){
                       partialSelectList.remove(ss2);
                       break;
                   }
               }
            }
        }
        return partialSelectList;
    }

    private LogicalWorkflow rearrangeWorkflow(LogicalWorkflow workflow, LogicalWorkflow subqueryWorkflow) {

        if (workflow.getInitialSteps().size() == 1) {
            ((Project) workflow.getInitialSteps().get(0)).setPrevious(subqueryWorkflow.getLastStep());
            subqueryWorkflow.getLastStep().setNextStep(workflow.getInitialSteps().get(0));
        } else {

            Collection<String> outputAliasSelectors = ((Select) subqueryWorkflow.getLastStep()).getColumnMap().values();
            Collection<String> inputAliasSelectors;
            Iterator<LogicalStep> workflowIterator = workflow.getInitialSteps().iterator();
            while (workflowIterator.hasNext()) {
                inputAliasSelectors = new HashSet<>();
                Project wProject = (Project) workflowIterator.next();
                for (ColumnName columnName : wProject.getColumnList()) {
                    inputAliasSelectors.add(columnName.getName());
                }
                if (outputAliasSelectors.containsAll(inputAliasSelectors)) {
                    wProject.setPrevious(subqueryWorkflow.getLastStep());
                    subqueryWorkflow.getLastStep().setNextStep(wProject);
                } else {
                    subqueryWorkflow.getInitialSteps().add(wProject);
                }
            }
        }
        return subqueryWorkflow;
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

    private MetadataWorkflow buildMetadataWorkflowCreateTable(MetadataStatement metadataStatement, String queryId) throws PlanningException{
        MetadataWorkflow metadataWorkflow = null;
        // Create parameters for metadata workflow
        CreateTableStatement createTableStatement = (CreateTableStatement) metadataStatement;
        String actorRefUri = null;
        ResultType type = ResultType.RESULTS;
        ExecutionType executionType = null;
        ClusterMetadata clusterMetadata = null;

        if (!createTableStatement.isExternal()) {
            executionType = ExecutionType.CREATE_TABLE;
            clusterMetadata = MetadataManager.MANAGER.getCluster(createTableStatement.getClusterName());
            Set<ConnectorName> connectorNames = clusterMetadata.getConnectorAttachedRefs().keySet();
            if (connectorNames.isEmpty()){
                throw new PlanningException("There is no connector attached to cluster "+clusterMetadata.getName().getName());
            }
            try {
                actorRefUri = findAnyActorRef(clusterMetadata, Status.ONLINE, Operations.CREATE_TABLE);
            } catch (PlanningException pe) {
                LOG.debug( "No connector was found to execute CREATE_TABLE: " + System.lineSeparator() + pe.getMessage());
                for (ConnectorName connectorName : connectorNames) {
                    if (MetadataManager.MANAGER.getConnector(connectorName).getSupportedOperations().contains(Operations.CREATE_TABLE)){
                        throw new PlanningException(connectorName.getQualifiedName()+" supports CREATE_TABLE but no connector was found to execute CREATE_TABLE");
                    }
                }
            }
        }else{
            executionType = ExecutionType.REGISTER_TABLE;
        }

        metadataWorkflow = new MetadataWorkflow(queryId, actorRefUri, executionType, type);
        metadataWorkflow.setIfNotExists(createTableStatement.isIfNotExists());

        if (!existsCatalogInCluster(createTableStatement.getTableName().getCatalogName(),
                createTableStatement.getClusterName())) {

            try {
                if (!createTableStatement.isExternal()) {
                    actorRefUri = findAnyActorRef(clusterMetadata, Status.ONLINE, Operations.CREATE_CATALOG);
                    executionType = ExecutionType.CREATE_TABLE_AND_CATALOG;
                }else {
                   LOG.debug("The catalog should have been created before registering table");
                    for (ConnectorName connectorName : clusterMetadata.getConnectorAttachedRefs().keySet()) {
                        if (MetadataManager.MANAGER.getConnector(connectorName).getSupportedOperations().contains(Operations.CREATE_CATALOG)){
                            throw new PlanningException("The catalog should have been created before registering table. The connector: "+connectorName.getQualifiedName()+" supports CREATE_CATALOG");
                        }
                    }
                }

                // Create MetadataWorkFlow
                metadataWorkflow = new MetadataWorkflow(queryId, actorRefUri, executionType, type);

                // Add CatalogMetadata to the WorkFlow
                metadataWorkflow.setCatalogName(createTableStatement.getTableName().getCatalogName());

                metadataWorkflow.setCatalogMetadata(MetadataManager.MANAGER
                        .getCatalog(createTableStatement.getTableName().getCatalogName()));
            } catch (PlanningException pe) {
                LOG.debug("Cannot determine any connector for the operation: " + Operations.CREATE_CATALOG
                        + System.lineSeparator() + pe.getMessage());
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

            managementWorkflow = new ManagementWorkflow(queryId, "", executionType, type);

            // Add required information
            managementWorkflow.setClusterName(attachClusterStatement.getClusterName());
            managementWorkflow.setDatastoreName(attachClusterStatement.getDatastoreName());
            managementWorkflow.setOptions(attachClusterStatement.getOptions());

        } else if (metadataStatement instanceof DetachClusterStatement) {
            DetachClusterStatement detachClusterStatement = (DetachClusterStatement) metadataStatement;
            ExecutionType executionType = ExecutionType.DETACH_CLUSTER;
            ResultType type = ResultType.RESULTS;

            managementWorkflow = new ManagementWorkflow(queryId, "", executionType, type);
            String clusterName = detachClusterStatement.getClusterName();
            managementWorkflow.setClusterName(new ClusterName(clusterName));

        } else if (metadataStatement instanceof AttachConnectorStatement) {
            // Create parameters for metadata workflow
            AttachConnectorStatement attachConnectorStatement = (AttachConnectorStatement) metadataStatement;
            ExecutionType executionType = ExecutionType.ATTACH_CONNECTOR;
            ResultType type = ResultType.RESULTS;

            ConnectorMetadata connector = MetadataManager.MANAGER
                    .getConnector(attachConnectorStatement.getConnectorName());

            managementWorkflow = new ManagementWorkflow(queryId, connector.getActorRefs(), executionType, type);

            // Add required information
            managementWorkflow.setConnectorName(attachConnectorStatement.getConnectorName());
            managementWorkflow.setClusterName(attachConnectorStatement.getClusterName());
            managementWorkflow.setOptions(attachConnectorStatement.getOptions());
            managementWorkflow.setPageSize(attachConnectorStatement.getPagination());
            managementWorkflow.setPriority(attachConnectorStatement.getPriority());

        } else if (metadataStatement instanceof DetachConnectorStatement) {
            DetachConnectorStatement detachConnectorStatement = (DetachConnectorStatement) metadataStatement;
            ExecutionType executionType = ExecutionType.DETACH_CONNECTOR;
            ResultType type = ResultType.RESULTS;

            ConnectorMetadata connector = MetadataManager.MANAGER
                    .getConnector(detachConnectorStatement.getConnectorName());

            managementWorkflow = new ManagementWorkflow(queryId, connector.getActorRefs(), executionType, type);
            managementWorkflow.setConnectorName(detachConnectorStatement.getConnectorName());
            managementWorkflow.setClusterName(detachConnectorStatement.getClusterName());

        } else if (metadataStatement instanceof AlterClusterStatement) {
            AlterClusterStatement alterClusterStatement = (AlterClusterStatement) metadataStatement;
            ExecutionType executionType = ExecutionType.ALTER_CLUSTER;
            ResultType type = ResultType.RESULTS;

            ClusterMetadata clusterMetadata = MetadataManager.MANAGER
                    .getCluster(alterClusterStatement.getClusterName());

            managementWorkflow = new ManagementWorkflow(queryId, "", executionType, type);
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
        StorageWorkflow storageWorkflow = null;
        InsertIntoStatement insertIntoStatement = (InsertIntoStatement) query.getStatement();

        TableName tableName = insertIntoStatement.getTableName();
        TableMetadata tableMetadata = getTableMetadata(tableName);
        ClusterMetadata clusterMetadata = getClusterMetadata(tableMetadata.getClusterRef());

        String actorRef;

        if (insertIntoStatement.isIfNotExists()) {
            actorRef = findAnyActorRef(clusterMetadata, Status.ONLINE, Operations.INSERT_IF_NOT_EXISTS);
        } else {
            actorRef = findAnyActorRef(clusterMetadata, Status.ONLINE, Operations.INSERT);
        }

        if (insertIntoStatement.getTypeValues() == InsertIntoStatement.TYPE_VALUES_CLAUSE) {

            storageWorkflow = new StorageWorkflow(queryId, actorRef, ExecutionType.INSERT, ResultType.RESULTS);
            storageWorkflow.setClusterName(tableMetadata.getClusterRef());
            storageWorkflow.setTableMetadata(tableMetadata);
            storageWorkflow.setIfNotExists(insertIntoStatement.isIfNotExists());

            Row row = getInsertRow(insertIntoStatement);
            storageWorkflow.setRow(row);

        } else if (insertIntoStatement.getTypeValues() == InsertIntoStatement.TYPE_SELECT_CLAUSE) {

            // PLAN SELECT
            SelectStatement selectStatement = insertIntoStatement.getSelectStatement();

            BaseQuery selectBaseQuery = new BaseQuery(
                    query.getQueryId(),
                    selectStatement.toString(),
                    query.getDefaultCatalog());
            SelectParsedQuery selectParsedQuery = new SelectParsedQuery(selectBaseQuery, selectStatement);

            Validator validator = new Validator();
            SelectValidatedQuery selectValidatedQuery;
            try {
                selectValidatedQuery = (SelectValidatedQuery) validator.validate(selectParsedQuery);
            } catch (ValidationException | IgnoreQueryException e) {
                throw new PlanningException(e.getMessage());
            }

            selectValidatedQuery.optimizeQuery();
            LogicalWorkflow selectLogicalWorkflow = buildWorkflow(selectValidatedQuery);
            selectLogicalWorkflow = addAliasFromInsertToSelect(insertIntoStatement, selectLogicalWorkflow);
            ExecutionWorkflow selectExecutionWorkflow = buildExecutionWorkflow(
                    selectValidatedQuery.getQueryId(),
                    selectLogicalWorkflow);

            // FIND CANDIDATES
            List<ClusterName> involvedClusters = new ArrayList<>();
            involvedClusters.add(clusterMetadata.getName());
            for (TableName tableNameFromSelect : insertIntoStatement.getSelectStatement().getFromTables()) {
                TableMetadata tableMetadataFromSelect = getTableMetadata(tableNameFromSelect);
                if (!involvedClusters.contains(tableMetadataFromSelect.getClusterRef())) {
                    involvedClusters.add(tableMetadataFromSelect.getClusterRef());
                }
            }

            Set<Operations> requiredOperations = new HashSet<>();
            requiredOperations.add(Operations.INSERT_FROM_SELECT);

            List<ConnectorMetadata> candidates = findCandidates(
                    involvedClusters,
                    requiredOperations);

            selectExecutionWorkflow.setResultType(ResultType.TRIGGER_EXECUTION);
            if (insertIntoStatement.isIfNotExists()) {
                selectExecutionWorkflow.setTriggerStep(new PartialResults(Operations.INSERT_IF_NOT_EXISTS));
            } else {
                selectExecutionWorkflow.setTriggerStep(new PartialResults(Operations.INSERT));
            }

            if ((candidates != null) && (!candidates.isEmpty())) {
                // Build a unique workflow
                ConnectorMetadata bestConnector = findBestConnector(candidates, involvedClusters);

                storageWorkflow = new StorageWorkflow(
                        queryId,
                        bestConnector.getActorRef(),
                        ExecutionType.INSERT_FROM_SELECT,
                        ResultType.RESULTS);
                storageWorkflow.setClusterName(tableMetadata.getClusterRef());
                storageWorkflow.setTableMetadata(tableMetadata);
                storageWorkflow.setIfNotExists(insertIntoStatement.isIfNotExists());
                storageWorkflow.setPreviousExecutionWorkflow(selectExecutionWorkflow);

            } else {
                // Build a workflow for select and insert
                storageWorkflow = new StorageWorkflow(queryId, actorRef, ExecutionType.INSERT_BATCH,
                        ResultType.RESULTS);
                storageWorkflow.setClusterName(tableMetadata.getClusterRef());
                storageWorkflow.setTableMetadata(tableMetadata);
                storageWorkflow.setIfNotExists(insertIntoStatement.isIfNotExists());
                storageWorkflow.setPreviousExecutionWorkflow(selectExecutionWorkflow);
            }

        }

        return storageWorkflow;
    }

    private LogicalWorkflow addAliasFromInsertToSelect(
            InsertIntoStatement insertIntoStatement,
            LogicalWorkflow selectLogicalWorkflow) {
        Select lastStep = (Select) selectLogicalWorkflow.getLastStep();

        List<ColumnName> insertColumns = insertIntoStatement.getColumns();

        // COLUMN MAP
        Map<Selector, String> columnMap = lastStep.getColumnMap();
        Map<Selector, String> newColumnMap = new LinkedHashMap<>();
        int i = 0;
        for (Map.Entry<Selector, String> column : columnMap.entrySet()) {
            ColumnName columnName = insertColumns.get(i);
            Selector newSelector = column.getKey();
            newSelector.setAlias(columnName.getName());
            newColumnMap.put(newSelector, columnName.getName());
            i++;
        }
        lastStep.setColumnMap(newColumnMap);

        //
        Map<String, ColumnType> typeMap = lastStep.getTypeMap();
        Map<String, ColumnType> newTypeMap = new LinkedHashMap<>();
        i = 0;
        for (Map.Entry<String, ColumnType> column : typeMap.entrySet()) {
            ColumnName columnName = insertColumns.get(i);
            ColumnType columnType = column.getValue();
            newTypeMap.put(columnName.getName(), columnType);
            i++;
        }
        lastStep.setTypeMap(newTypeMap);

        //
        Map<Selector, ColumnType> typeMapFromColumnName = lastStep.getTypeMapFromColumnName();
        Map<Selector, ColumnType> newTypeMapFromColumnName = new LinkedHashMap<>();
        i = 0;
        for (Map.Entry<Selector, ColumnType> column : typeMapFromColumnName.entrySet()) {
            ColumnName columnName = insertColumns.get(i);
            Selector newSelector = column.getKey();
            newSelector.setAlias(columnName.getName());
            ColumnType columnType = column.getValue();
            newTypeMapFromColumnName.put(newSelector, columnType);
            i++;
        }
        lastStep.setTypeMapFromColumnName(newTypeMapFromColumnName);

        return selectLogicalWorkflow;
    }

    private List<ConnectorMetadata> findCandidates(List<ClusterName> involvedClusters,
            Set<Operations> requiredOperations) {
        List<ConnectorMetadata> candidates = new ArrayList<>();
        if ((involvedClusters != null) && (requiredOperations != null)) {
            List<ConnectorMetadata> allConnectors = MetadataManager.MANAGER.getConnectors();
            for (ConnectorMetadata connectorMetadata : allConnectors) {
                if (connectorMetadata.getClusterRefs().containsAll(involvedClusters)) {
                    if (connectorMetadata.getSupportedOperations().containsAll(requiredOperations)) {
                        candidates.add(connectorMetadata);
                    }
                }
            }
        }
        return candidates;
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
        if (operator==Operator.BETWEEN){
            return Operations.SELECT_WHERE_BETWEEN;
        } else {
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
                    Map<String, TableMetadata> tableMetadataMap, SelectValidatedQuery query) throws PlanningException {
        for(AbstractRelation ar: query.getRelations()){
            if(ar instanceof Relation){
                Relation r = (Relation) ar;
                Selector s = r.getLeftTerm();
                Operations op = createOperation(tableMetadataMap, s, r);
                if (op != null) {
                    convertSelectSelectors(r);
                    Filter f = new Filter(op, r);
                    LogicalStep previous = lastSteps.get(s.getSelectorTablesAsString());
                    previous.setNextStep(f);
                    f.setPrevious(previous);
                    lastSteps.put(s.getSelectorTablesAsString(), f);
                } else {
                    LOG.error("Cannot determine Filter for relation " + r.toString() +
                            " on table " + s.getSelectorTablesAsString());
                }
            } else if(ar instanceof RelationDisjunction) {
                RelationDisjunction rd = (RelationDisjunction) ar;
                Operations op = Operations.FILTER_DISJUNCTION;
                List<IOperand> leftOperands = new ArrayList<>();
                List<IOperand> rightOperands = new ArrayList<>();
                for(AbstractRelation innerRelation: rd.getLeftRelations()){
                    leftOperands.addAll(createFilter(tableMetadataMap, innerRelation));
                }
                for(AbstractRelation innerRelation: rd.getRightRelations()){
                    rightOperands.addAll(createFilter(tableMetadataMap, innerRelation));
                }
                Disjunction d = new Disjunction(op, leftOperands, rightOperands);
                LogicalStep previous = lastSteps.get(rd.getSelectorTablesAsString());
                previous.setNextStep(d);
                d.setPrevious(previous);
                lastSteps.put(rd.getSelectorTablesAsString(), d);
            }
        }
        return lastSteps;
    }

    private Operations createOperation(Map<String, TableMetadata> tableMetadataMap, Selector s, Relation r)
            throws PlanningException {
        Operations op = null;
        //TODO Support left-side functions that contain columns of several tables.
        TableMetadata tm = tableMetadataMap.get(s.getSelectorTablesAsString());
        if (tm != null) {
            op = getFilterOperation(tm, "FILTER", s, r.getOperator());
        } else if (s.getTableName().isVirtual()) {
            op = Operations.valueOf("FILTER_NON_INDEXED_" + r.getOperator().name());
        }
        if(op != null){
            convertSelectSelectors(r);
        }
        return op;
    }

    private List<IOperand> createFilter(
            Map<String, TableMetadata> tableMetadataMap,
            AbstractRelation abstractRelation) throws PlanningException {
        List<IOperand> operands = new ArrayList<>();
        if(abstractRelation instanceof Relation){
            Relation relation = (Relation) abstractRelation;
            Operations op = createOperation(tableMetadataMap, relation.getLeftTerm(), relation);
            operands.add(new Filter(op, relation));
        } else if(abstractRelation instanceof RelationDisjunction){
            RelationDisjunction rd = (RelationDisjunction) abstractRelation;
            List<IOperand> leftOperands = new ArrayList<>();
            List<IOperand> rightOperands = new ArrayList<>();
            for(AbstractRelation innerRelation: rd.getLeftRelations()){
                leftOperands.addAll(createFilter(tableMetadataMap, innerRelation));
            }
            for(AbstractRelation innerRelation: rd.getRightRelations()){
                rightOperands.addAll(createFilter(tableMetadataMap, innerRelation));
            }
            operands.add(new Disjunction(Operations.FILTER_DISJUNCTION, leftOperands, rightOperands));
        }
        return operands;
    }

    private void convertSelectSelectors(Relation relation) throws PlanningException {
        Relation currentRelation = relation;
        while(currentRelation.getRightTerm() instanceof RelationSelector){
            currentRelation.setLeftTerm(convertSelectSelector(currentRelation.getLeftTerm()));
            currentRelation.setRightTerm(convertSelectSelector(currentRelation.getRightTerm()));
            currentRelation = ((RelationSelector) currentRelation.getRightTerm()).getRelation();
        }
        currentRelation.setLeftTerm(convertSelectSelector(currentRelation.getLeftTerm()));
        currentRelation.setRightTerm(convertSelectSelector(currentRelation.getRightTerm()));
    }

    private Selector convertSelectSelector(Selector selector) throws PlanningException {
        Selector result = selector;
        if(selector instanceof ExtendedSelectSelector){
            ExtendedSelectSelector extendedSelectSelector = (ExtendedSelectSelector) selector;
            SelectSelector selectSelector = new SelectSelector(selector.getTableName(), extendedSelectSelector.getSelectQuery());
            LogicalWorkflow innerWorkflow = buildWorkflow(extendedSelectSelector.getSelectValidatedQuery());
            selectSelector.setQueryWorkflow(innerWorkflow);
            result = selectSelector;
        }
        return result;
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
    private Map<String, LogicalStep> addJoin(LinkedHashMap<String, LogicalStep> stepMap, String targetTable,
            SelectValidatedQuery query) {

        for (InnerJoin queryJoin : query.getJoinList()) {

            Join innerJoin = new Join(Operations.SELECT_INNER_JOIN, "innerJoin");
            Join leftJoin = new Join(Operations.SELECT_LEFT_OUTER_JOIN, "leftJoin");
            Join rightJoin = new Join(Operations.SELECT_RIGHT_OUTER_JOIN, "rightJoin");
            Join fullOuterJoin = new Join(Operations.SELECT_FULL_OUTER_JOIN, "fullOuterJoin");
            Join crossJoin = new Join(Operations.SELECT_CROSS_JOIN, "crossJoin");

            StringBuilder sb = new StringBuilder();
            Relation firstRelation = (Relation) queryJoin.getRelations().get(0);
            sb.append(firstRelation.getLeftTerm().getTableName().getQualifiedName())
                    .append("$").append(firstRelation.getRightTerm().getTableName()
                    .getQualifiedName());

            //Attach to input tables path
            LogicalStep t1 = stepMap.get(firstRelation.getLeftTerm().getSelectorTablesAsString());
            LogicalStep t2 = stepMap.get(firstRelation.getRightTerm().getSelectorTablesAsString());
            List<AbstractRelation> relations;
            switch (queryJoin.getType()) {
            case INNER:
                innerJoin.setType(JoinType.INNER);
                relations = queryJoin.getRelations();
                for (AbstractRelation ar: relations) {
                    Relation r = (Relation) ar;

                    if(Filter.class.isInstance(t1)){
                        innerJoin.addSourceIdentifier(((Filter)t1).getRelation().getLeftTerm().getTableName().getQualifiedName());
                    }else if (Project.class.isInstance(t1)){
                        innerJoin.addSourceIdentifier(((Project)t1).getTableName().getQualifiedName());
                    }else{
                        innerJoin.addSourceIdentifier(r.getLeftTerm().getTableName().getQualifiedName());
                    }

                    if(Filter.class.isInstance(t2)){
                        innerJoin.addSourceIdentifier(((Filter)t2).getRelation().getLeftTerm().getTableName()
                                .getQualifiedName());
                    }else if (Project.class.isInstance(t2)){
                        innerJoin.addSourceIdentifier(((Project)t2).getTableName().getQualifiedName());
                    }else{
                        innerJoin.addSourceIdentifier(r.getRightTerm().getTableName().getQualifiedName());
                    }                                      

                }
                innerJoin.addJoinRelations(queryJoin.getOrderedRelations());

                if (t1.getNextStep() != null) {
                    t1.getNextStep().setNextStep(innerJoin);
                } else {
                    t1.setNextStep(innerJoin);
                }
                if (t2.getNextStep() != null) {
                    t2.getNextStep().setNextStep(innerJoin);
                } else {
                    t2.setNextStep(innerJoin);
                }
                innerJoin.addPreviousSteps(t1, t2);
                stepMap.put(sb.toString(), innerJoin);

                break;
            case CROSS:
                crossJoin.setType(JoinType.CROSS);
                relations = queryJoin.getRelations();
                for (AbstractRelation ar: relations) {
                    Relation r = (Relation) ar;
                    if(Filter.class.isInstance(t1)){
                        crossJoin.addSourceIdentifier(((Filter)t1).getRelation().getLeftTerm().getTableName().getQualifiedName());
                    }else if (Project.class.isInstance(t1)){
                        crossJoin.addSourceIdentifier(((Project)t1).getTableName().getQualifiedName());
                    }else{
                        crossJoin.addSourceIdentifier(r.getLeftTerm().getTableName().getQualifiedName());
                    }

                    if(Filter.class.isInstance(t2)){
                        crossJoin.addSourceIdentifier(((Filter)t2).getRelation().getLeftTerm().getTableName()
                                .getQualifiedName());
                    }else if (Project.class.isInstance(t2)){
                        crossJoin.addSourceIdentifier(((Project)t2).getTableName().getQualifiedName());
                    }else{
                        crossJoin.addSourceIdentifier(r.getRightTerm().getTableName().getQualifiedName());
                    }
                }
                crossJoin.addJoinRelations(queryJoin.getOrderedRelations());

                if (t1.getNextStep() != null) {
                    t1.getNextStep().setNextStep(crossJoin);
                } else {
                    t1.setNextStep(crossJoin);
                }
                if (t2.getNextStep() != null) {
                    t2.getNextStep().setNextStep(crossJoin);
                } else {
                    t2.setNextStep(crossJoin);
                }
                crossJoin.addPreviousSteps(t1, t2);
                stepMap.put(sb.toString(), crossJoin);

                break;
            case LEFT_OUTER:
                leftJoin.setType(JoinType.LEFT_OUTER);
                relations = queryJoin.getRelations();
                for (AbstractRelation ar: relations) {
                    Relation r = (Relation) ar;
                    if(Filter.class.isInstance(t1)){
                        leftJoin.addSourceIdentifier(((Filter)t1).getRelation().getLeftTerm().getTableName().getQualifiedName());
                    }else if (Project.class.isInstance(t1)){
                        leftJoin.addSourceIdentifier(((Project)t1).getTableName().getQualifiedName());
                    }else{
                        leftJoin.addSourceIdentifier(r.getLeftTerm().getTableName().getQualifiedName());
                    }

                    if(Filter.class.isInstance(t2)){
                        leftJoin.addSourceIdentifier(((Filter)t2).getRelation().getLeftTerm().getTableName()
                                .getQualifiedName());
                    }else if (Project.class.isInstance(t2)){
                        leftJoin.addSourceIdentifier(((Project)t2).getTableName().getQualifiedName());
                    }else{
                        leftJoin.addSourceIdentifier(r.getRightTerm().getTableName().getQualifiedName());
                    }
                }
                leftJoin.addJoinRelations(queryJoin.getOrderedRelations());

                if (t1.getNextStep() != null) {
                    t1.getNextStep().setNextStep(leftJoin);
                } else {
                    t1.setNextStep(leftJoin);
                }
                if (t2.getNextStep() != null) {
                    t2.getNextStep().setNextStep(leftJoin);
                } else {
                    t2.setNextStep(leftJoin);
                }
                leftJoin.addPreviousSteps(t1, t2);
                stepMap.put(sb.toString(), leftJoin);

                break;
            case FULL_OUTER:
                fullOuterJoin.setType(JoinType.FULL_OUTER);
                relations = queryJoin.getRelations();
                for (AbstractRelation ar: relations) {
                    Relation r = (Relation) ar;
                    if(Filter.class.isInstance(t1)){
                        fullOuterJoin.addSourceIdentifier(((Filter)t1).getRelation().getLeftTerm().getTableName().getQualifiedName());
                    }else if (Project.class.isInstance(t1)){
                        fullOuterJoin.addSourceIdentifier(((Project)t1).getTableName().getQualifiedName());
                    }else{
                        fullOuterJoin.addSourceIdentifier(r.getLeftTerm().getTableName().getQualifiedName());
                    }

                    if(Filter.class.isInstance(t2)){
                        fullOuterJoin.addSourceIdentifier(((Filter)t2).getRelation().getLeftTerm().getTableName()
                                .getQualifiedName());
                    }else if (Project.class.isInstance(t2)){
                        fullOuterJoin.addSourceIdentifier(((Project)t2).getTableName().getQualifiedName());
                    }else{
                        fullOuterJoin.addSourceIdentifier(r.getRightTerm().getTableName().getQualifiedName());
                    }
                }
                fullOuterJoin.addJoinRelations(queryJoin.getOrderedRelations());

                if (t1.getNextStep() != null) {
                    t1.getNextStep().setNextStep(fullOuterJoin);
                } else {
                    t1.setNextStep(fullOuterJoin);
                }
                if (t2.getNextStep() != null) {
                    t2.getNextStep().setNextStep(fullOuterJoin);
                } else {
                    t2.setNextStep(fullOuterJoin);
                }
                fullOuterJoin.addPreviousSteps(t1, t2);
                stepMap.put(sb.toString(), fullOuterJoin);

                break;
            case RIGHT_OUTER:
                rightJoin.setType(JoinType.RIGHT_OUTER);
                relations = queryJoin.getRelations();
                for (AbstractRelation ar: relations) {
                    Relation r = (Relation) ar;
                    if(Filter.class.isInstance(t1)){
                        rightJoin.addSourceIdentifier(((Filter)t1).getRelation().getLeftTerm().getTableName().getQualifiedName());
                    }else if (Project.class.isInstance(t1)){
                        rightJoin.addSourceIdentifier(((Project)t1).getTableName().getQualifiedName());
                    }else{
                        rightJoin.addSourceIdentifier(r.getLeftTerm().getTableName().getQualifiedName());
                    }

                    if(Filter.class.isInstance(t2)){
                        rightJoin.addSourceIdentifier(((Filter)t2).getRelation().getLeftTerm().getTableName()
                                .getQualifiedName());
                    }else if (Project.class.isInstance(t2)){
                        rightJoin.addSourceIdentifier(((Project)t2).getTableName().getQualifiedName());
                    }else{
                        rightJoin.addSourceIdentifier(r.getRightTerm().getTableName().getQualifiedName());
                    }
                }
                rightJoin.addJoinRelations(queryJoin.getOrderedRelations());

                if (t1.getNextStep() != null) {
                    t1.getNextStep().setNextStep(rightJoin);
                } else {
                    t1.setNextStep(rightJoin);
                }
                if (t2.getNextStep() != null) {
                    t2.getNextStep().setNextStep(rightJoin);
                } else {
                    t2.setNextStep(rightJoin);
                }
                rightJoin.addPreviousSteps(t1, t2);
                stepMap.put(sb.toString(), rightJoin);

                break;
            }
        }
        return stepMap;
    }

    /**
     * Get a Map associating fully qualified table names with their Project logical step.
     *
     * @param query            The query to be planned.
     * @param tableMetadataMap Map of table metadata.
     * @return A map with the projections.
     */
    protected LinkedHashMap<String, LogicalStep> getProjects(SelectValidatedQuery query,
            Map<String, TableMetadata> tableMetadataMap) {

        LinkedHashMap<String, LogicalStep> projects = new LinkedHashMap<>();
        //for (TableName tn : query.getTables()) {
        for (TableName tn : query.getStatement().getFromTables()) {
            Project p;
            if (tn.isVirtual()) {
                p = new Project(Operations.PROJECT, tn, new ClusterName(Constants.VIRTUAL_CATALOG_NAME));
                projects.put(tn.getQualifiedName(), p);
            } else {
                p = new Project(Operations.PROJECT, tn, tableMetadataMap.get(tn.getQualifiedName()).getClusterRef());

            }
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

                ColumnType colType = null;
                //TODO avoid null types
                if (!cs.getTableName().isVirtual()) {
                    colType = tableMetadataMap.get(cs.getSelectorTablesAsString()).getColumns().get(cs.getName())
                            .getColumnType();
                }
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
                generateLiteralSelect(aliasMap, typeMap, typeMapFromColumnName, s, new ColumnType(DataType.INT));
            } else if (FloatingPointSelector.class.isInstance(s)) {
                generateLiteralSelect(aliasMap, typeMap, typeMapFromColumnName, s, new ColumnType(DataType.DOUBLE));
            } else if (BooleanSelector.class.isInstance(s)) {
                generateLiteralSelect(aliasMap, typeMap, typeMapFromColumnName, s, new ColumnType(DataType.BOOLEAN));
            } else if (StringSelector.class.isInstance(s)) {
                generateLiteralSelect(aliasMap, typeMap, typeMapFromColumnName, s, new ColumnType(DataType.TEXT));
            } else if (CaseWhenSelector.class.isInstance(s)){
                generateCaseWhenSelect(aliasMap, typeMap, typeMapFromColumnName, s);
                currentOperation=Operations.SELECT_CASE_WHEN;
            } else {
                throw new PlanningException(s.getClass().getCanonicalName() + " is not supported yet.");
            }
        }

        if (addAll) {
            //TODO check whether it is dead code
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
            //Change to admit n joins
            if (!selectStatement.getJoinList().isEmpty()) {
                for (InnerJoin innerJoin : selectStatement.getJoinList()) {
                    TableMetadata metadataJoin = tableMetadataMap
                            .get(innerJoin.getTablename().getQualifiedName());
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
        }

        return new Select(currentOperation, aliasMap, typeMap, typeMapFromColumnName);
    }

    private void generateCaseWhenSelect(LinkedHashMap<Selector, String> aliasMap,
            LinkedHashMap<String, ColumnType> typeMap, LinkedHashMap<Selector, ColumnType> typeMapFromColumnName,
            Selector selector) {

        String alias;
        if (selector.getAlias() != null) {
            alias = selector.getAlias();
            if (aliasMap.containsValue(alias)) {
                alias = selector.getColumnName().getTableName().getName() + "_" + selector.getAlias();
            }

        } else {
            alias = selector.getStringValue();
            selector.setAlias(alias);

        }
        aliasMap.put(selector, alias);
        ColumnType ct = null;
        typeMapFromColumnName.put(selector, ct);
        typeMap.put(alias, ct);
    }

    private void generateLiteralSelect(LinkedHashMap<Selector, String> aliasMap,
            LinkedHashMap<String, ColumnType> typeMap,
            LinkedHashMap<Selector, ColumnType> typeMapFromColumnName, Selector selector, ColumnType columnType)
            throws PlanningException {

        String alias;
        if (selector.getAlias() != null) {
            alias = selector.getAlias();
            if (aliasMap.containsValue(alias)) {
                alias = selector.getColumnName().getTableName().getName() + "_" + selector.getAlias();
            }

        } else {
            alias = selector.getStringValue();
            selector.setAlias(alias);

        }
        aliasMap.put(selector, alias);
        typeMapFromColumnName.put(selector, columnType);
        typeMap.put(alias, columnType);
    }

    private ConnectorMetadata findAnyConnector(ClusterMetadata clusterMetadata, Status status,
            Operations... requiredOperations) throws PlanningException {
        ConnectorMetadata connectorMetadata = null;

        Map<ConnectorName, ConnectorAttachedMetadata> connectorAttachedRefs = clusterMetadata
                .getConnectorAttachedRefs();

        Iterator it = connectorAttachedRefs.keySet().iterator();
        boolean found = false;
        while (it.hasNext() && !found) {
            ConnectorName connectorName = (ConnectorName) it.next();
            connectorMetadata = MetadataManager.MANAGER.getConnector(connectorName);
            if ((connectorMetadata.getStatus() == status) && connectorMetadata.getSupportedOperations()
                    .containsAll(Arrays.asList(requiredOperations))) {
                found = true;
            }
        }
        if (!found) {
            throw new PlanningException("There is no any attached connector supporting: " +
                    System.lineSeparator() + Arrays.toString(requiredOperations));
        }

        return connectorMetadata;
    }

    private String findAnyActorRef(ClusterMetadata clusterMetadata, Status status, Operations... requiredOperations)
            throws PlanningException {
        ConnectorMetadata connectorMetadata = findAnyConnector(clusterMetadata, status, requiredOperations);
        return StringUtils.getAkkaActorRefUri(connectorMetadata.getActorRef(), false);
    }

}
