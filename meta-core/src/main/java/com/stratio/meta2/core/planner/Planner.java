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

package com.stratio.meta2.core.planner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.stratio.meta.common.connector.Operations;
import com.stratio.meta.common.data.Cell;
import com.stratio.meta.common.data.Row;
import com.stratio.meta.common.exceptions.PlanningException;
import com.stratio.meta.common.executionplan.ExecutionPath;
import com.stratio.meta.common.executionplan.ExecutionType;
import com.stratio.meta.common.executionplan.ExecutionWorkflow;
import com.stratio.meta.common.executionplan.ManagementWorkflow;
import com.stratio.meta.common.executionplan.MetadataWorkflow;
import com.stratio.meta.common.executionplan.QueryWorkflow;
import com.stratio.meta.common.executionplan.ResultType;
import com.stratio.meta.common.executionplan.StorageWorkflow;
import com.stratio.meta.common.logicalplan.Filter;
import com.stratio.meta.common.logicalplan.Join;
import com.stratio.meta.common.logicalplan.Limit;
import com.stratio.meta.common.logicalplan.LogicalStep;
import com.stratio.meta.common.logicalplan.LogicalWorkflow;
import com.stratio.meta.common.logicalplan.PartialResults;
import com.stratio.meta.common.logicalplan.Project;
import com.stratio.meta.common.logicalplan.Select;
import com.stratio.meta.common.logicalplan.TransformationStep;
import com.stratio.meta.common.logicalplan.UnionStep;
import com.stratio.meta.common.logicalplan.Window;
import com.stratio.meta.common.statements.structures.relationships.Operator;
import com.stratio.meta.common.statements.structures.relationships.Relation;
import com.stratio.meta.common.utils.StringUtils;
import com.stratio.meta.core.structures.InnerJoin;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.data.ClusterName;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.ConnectorName;
import com.stratio.meta2.common.data.Status;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.metadata.CatalogMetadata;
import com.stratio.meta2.common.metadata.ClusterMetadata;
import com.stratio.meta2.common.metadata.ColumnMetadata;
import com.stratio.meta2.common.metadata.ColumnType;
import com.stratio.meta2.common.metadata.ConnectorAttachedMetadata;
import com.stratio.meta2.common.metadata.ConnectorMetadata;
import com.stratio.meta2.common.metadata.TableMetadata;
import com.stratio.meta2.common.statements.structures.selectors.ColumnSelector;
import com.stratio.meta2.common.statements.structures.selectors.Selector;
import com.stratio.meta2.common.statements.structures.selectors.SelectorType;
import com.stratio.meta2.core.metadata.MetadataManager;
import com.stratio.meta2.core.query.MetadataPlannedQuery;
import com.stratio.meta2.core.query.MetadataValidatedQuery;
import com.stratio.meta2.core.query.SelectPlannedQuery;
import com.stratio.meta2.core.query.SelectValidatedQuery;
import com.stratio.meta2.core.query.StoragePlannedQuery;
import com.stratio.meta2.core.query.StorageValidatedQuery;
import com.stratio.meta2.core.query.ValidatedQuery;
import com.stratio.meta2.core.statements.AttachClusterStatement;
import com.stratio.meta2.core.statements.AttachConnectorStatement;
import com.stratio.meta2.core.statements.CreateCatalogStatement;
import com.stratio.meta2.core.statements.CreateTableStatement;
import com.stratio.meta2.core.statements.InsertIntoStatement;
import com.stratio.meta2.core.statements.MetadataStatement;
import com.stratio.meta2.core.statements.SelectStatement;

/**
 * Class in charge of defining the set of {@link com.stratio.meta.common.logicalplan.LogicalStep}
 * required to execute a statement. This set of steps are ordered as a workflow on a {@link
 * com.stratio.meta.common.logicalplan.LogicalWorkflow} structure. Notice that the LogicalWorkflow
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
     * @param query A {@link com.stratio.meta2.core.query.SelectValidatedQuery}.
     * @return A {@link com.stratio.meta2.core.query.SelectPlannedQuery}.
     * @throws com.stratio.meta.common.exceptions.PlanningException If the query cannot be planned.
     */
    public SelectPlannedQuery planQuery(SelectValidatedQuery query) throws PlanningException {
        LogicalWorkflow workflow = buildWorkflow(query);
        //Plan the workflow execution into different connectors.
        ExecutionWorkflow executionWorkflow = buildExecutionWorkflow(query.getQueryId(), workflow);
        //Return the planned query.
        SelectPlannedQuery pq = new SelectPlannedQuery(query, executionWorkflow);
        return pq;
    }

    /**
     * Define an execution plan for metadata queries.
     *
     * @param query A {@link com.stratio.meta2.core.query.MetadataValidatedQuery}.
     * @return A {@link com.stratio.meta2.core.query.MetadataPlannedQuery}.
     * @throws PlanningException If the query cannot be planned.
     */
    public MetadataPlannedQuery planQuery(MetadataValidatedQuery query) throws PlanningException {
        ExecutionWorkflow executionWorkflow = buildExecutionWorkflow(query);
        return new MetadataPlannedQuery(query, executionWorkflow);
    }

    /**
     * Define an execution plan for storage queries.
     *
     * @param query A {@link com.stratio.meta2.core.query.StorageValidatedQuery}.
     * @return A {@link com.stratio.meta2.core.query.StoragePlannedQuery}.
     * @throws PlanningException If the query cannot be planned.
     */
    public StoragePlannedQuery planQuery(StorageValidatedQuery query) throws PlanningException {
        ExecutionWorkflow executionWorkflow = buildExecutionWorkflow(query);
        return new StoragePlannedQuery(query, executionWorkflow);
    }

    /**
     * Build a Logical workflow for the incoming validated query.
     *
     * @param query A valid query.
     * @return A {@link com.stratio.meta.common.logicalplan.LogicalWorkflow}
     */
    protected LogicalWorkflow buildWorkflow(ValidatedQuery query) {
        LogicalWorkflow result = null;
        if (query instanceof SelectValidatedQuery) {
            result = buildWorkflow((SelectValidatedQuery) query);
        }
        return result;
    }

    /**
     * Build a execution workflow for a query analyzing the existing logical workflow.
     *
     * @param queryId  The query identifier.
     * @param workflow The {@link com.stratio.meta.common.logicalplan.LogicalWorkflow} associated with the query.
     * @return A {@link com.stratio.meta.common.executionplan.ExecutionWorkflow}.
     * @throws PlanningException If the workflow cannot be defined.
     */
    protected ExecutionWorkflow buildExecutionWorkflow(String queryId, LogicalWorkflow workflow) throws
            PlanningException {

        //Get the list of tables accessed in this query
        List<TableName> tables = getInitialSteps(workflow.getInitialSteps());

        //Obtain the map of connector that is able to access those tables.
        Map<TableName, List<ConnectorMetadata>> candidatesConnectors = MetadataManager.MANAGER
                .getAttachedConnectors(Status.ONLINE, tables);

        List<ExecutionPath> executionPaths = new ArrayList<>();
        Map<UnionStep, Set<ExecutionPath>> unionSteps = new HashMap<>();
        //Iterate through the initial steps and build valid execution paths
        for (LogicalStep step : workflow.getInitialSteps()) {
            TableName targetTable = ((Project) step).getTableName();
            LOG.info("Table: " + targetTable);
            ExecutionPath ep = defineExecutionPath(step, candidatesConnectors.get(targetTable));
            if (UnionStep.class.isInstance(ep.getLast())) {
                Set<ExecutionPath> paths = unionSteps.get(ep.getLast());
                if (paths == null) {
                    paths = new HashSet<>();
                }
                paths.add(ep);
            }
            executionPaths.add(ep);
        }

        //Merge execution paths
        ExecutionWorkflow executionWorkflow = mergeExecutionPaths(queryId, executionPaths, unionSteps);
        return executionWorkflow;
    }

    /**
     * Merge a set of execution paths solving union dependencies along.
     *
     * @param queryId        The query identifier.
     * @param executionPaths The list of execution paths.
     * @param unionSteps     A map of union steps waiting to be merged.
     * @return A {@link com.stratio.meta.common.executionplan.ExecutionWorkflow}.
     * @throws PlanningException If the execution paths cannot be merged.
     */
    protected ExecutionWorkflow mergeExecutionPaths(String queryId,
            List<ExecutionPath> executionPaths,
            Map<UnionStep, Set<ExecutionPath>> unionSteps) throws PlanningException {

        if (unionSteps.size() == 0) {
            return toExecutionWorkflow(
                    queryId, executionPaths, executionPaths.get(0).getLast(),
                    executionPaths.get(0).getAvailableConnectors(),
                    ResultType.RESULTS);
        }

        //Find first UnionStep
        UnionStep mergeStep = null;
        ExecutionPath[] paths = null;
        for (Map.Entry<UnionStep, Set<ExecutionPath>> entry : unionSteps.entrySet()) {
            paths = entry.getValue().toArray(new ExecutionPath[entry.getValue().size()]);
            if (paths.length == 2
                    && TransformationStep.class.isInstance(paths[0].getLast())
                    && TransformationStep.class.isInstance(paths[1].getLast())) {
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
            //operation

            intermediateResults[0] = false;
            intermediateResults[1] = false;
            mergeConnectors.clear();
            toMerge.clear();
            for (int index = 0; index < paths.length; index++) {
                toRemove.clear();
                for (ConnectorMetadata connector : paths[index].getAvailableConnectors()) {
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
                    ExecutionWorkflow w = toExecutionWorkflow(
                            queryId, Arrays.asList(paths[index]),
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

                ExecutionWorkflow mergeWorkflow = extendExecutionWorkflow(
                        queryId, toMerge, next, ResultType.RESULTS);
                triggerWorkflow.put(mergeStep, mergeWorkflow);
                if (first == null) {
                    first = QueryWorkflow.class.cast(mergeWorkflow);
                }
            } else {
                Set<ExecutionPath> existingPaths = unionSteps.get(next.getLast());
                if (executionPaths == null) {
                    existingPaths = new HashSet<>();
                }else {
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
     * @return A {@link com.stratio.meta.common.executionplan.ExecutionWorkflow}.
     */
    public ExecutionWorkflow buildExecutionTree(
            QueryWorkflow first,
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
     *                       {@link com.stratio.meta.common.logicalplan.LogicalWorkflow}.
     * @param last           The last element of the workflow.
     * @param connectors     The List of available connectors.
     * @return A {@link com.stratio.meta.common.executionplan.QueryWorkflow}.
     */
    protected QueryWorkflow toExecutionWorkflow(
            String queryId, List<ExecutionPath> executionPaths,
            LogicalStep last, List<ConnectorMetadata> connectors,
            ResultType type) {

        //Define the list of initial steps.
        List<LogicalStep> initialSteps = new ArrayList<>(executionPaths.size());
        for (ExecutionPath path : executionPaths) {
            initialSteps.add(path.getInitial());
        }
        LogicalWorkflow workflow = new LogicalWorkflow(initialSteps);

        //Select an actor
        //TODO Improve actor selection based on cost analysis.
        String selectedActorUri = StringUtils.getAkkaActorRefUri(connectors.get(0).getActorRef());
        return new QueryWorkflow(queryId, selectedActorUri, ExecutionType.SELECT, type, workflow);
    }

    /**
     * Define a query worflow composed by several execution paths merging in a
     * {@link com.stratio.meta.common.executionplan.ExecutionPath} that starts with a UnionStep.
     *
     * @param queryId        The query identifier.
     * @param executionPaths The list of execution paths.
     * @param mergePath      The merge path with the union step.
     * @param type           The type of results to be returned.
     * @return A {@link com.stratio.meta.common.executionplan.QueryWorkflow}.
     */
    protected QueryWorkflow extendExecutionWorkflow(
            String queryId,
            List<ExecutionPath> executionPaths,
            ExecutionPath mergePath,
            ResultType type) {

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
     * @return An {@link com.stratio.meta.common.executionplan.ExecutionPath}.
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
            //Evaluate the connectors
            for (ConnectorMetadata connector : availableConnectors) {
                if (!connector.supports(current.getOperation())) {
                    toRemove.add(connector);
                }
            }
            //Remove invalid connectors
            if (toRemove.size() == availableConnectors.size()) {
                throw new PlanningException(
                        "Cannot determine execution path as no connector supports " + current.toString());
            } else {
                availableConnectors.removeAll(toRemove);

                if (current.getNextStep() == null
                        || UnionStep.class.isInstance(current.getNextStep())) {
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
     * @return A list of {@link com.stratio.meta2.common.data.TableName}.
     */
    protected List<TableName> getInitialSteps(List<LogicalStep> initialSteps) {
        List<TableName> tables = new ArrayList<>(initialSteps.size());
        for (LogicalStep ls : initialSteps) {
            tables.add(Project.class.cast(ls).getTableName());
        }
        return tables;
    }

    /**
     * Build a workflow with the {@link com.stratio.meta.common.logicalplan.LogicalStep} required to
     * execute a query. This method does not determine which connector will execute which part of the
     * workflow.
     *
     * @param query The query to be planned.
     * @return A Logical workflow.
     */
    protected LogicalWorkflow buildWorkflow(SelectValidatedQuery query) {
        Map<String, TableMetadata> tableMetadataMap = new HashMap<>();
        for (TableMetadata tm : query.getTableMetadata()) {
            tableMetadataMap.put(tm.getName().getQualifiedName(), tm);
        }
        //Define the list of projects
        Map<String, LogicalStep> processed = getProjects(query, tableMetadataMap);
        addProjectedColumns(processed, query);

        //TODO determine which is the correct target table if the order fails.
        String selectTable = query.getTables().get(0).getQualifiedName();

        //Add filters
        if (query.getRelationships() != null) {
            processed = addFilter(processed, tableMetadataMap, query);
        }

        SelectStatement ss = SelectStatement.class.cast(query.getStatement());
        if(ss.getWindow() != null){
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

        //Find the last element
        LogicalStep last = initial;
        while (last.getNextStep() != null) {
            last = last.getNextStep();
        }

        //Add LIMIT clause
        if (ss.isLimitInc()) {
            Limit l = new Limit(Operations.SELECT_LIMIT, ss.getLimit());
            last.setNextStep(l);
            l.setPrevious(last);
            last = l;
        }

        //Add window

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

        Set<String> managementStatements = new HashSet<>();
        managementStatements.add(AttachClusterStatement.class.toString());
        managementStatements.add(AttachConnectorStatement.class.toString());

        if (metadataStatements.contains(metadataStatement.getClass().toString())) {
            executionWorkflow = buildMetadataWorkflow(query);
        } else if (managementStatements.contains(metadataStatement.getClass().toString())) {
            executionWorkflow = buildManagementWorkflow(query);
        } else {
            throw new PlanningException("This statement can't be planned: " + metadataStatement.toString());
        }

        return executionWorkflow;
    }

    private ExecutionWorkflow buildMetadataWorkflow(MetadataValidatedQuery query) throws PlanningException {
        MetadataStatement metadataStatement = query.getStatement();
        String queryId = query.getQueryId();
        MetadataWorkflow metadataWorkflow;

        if (metadataStatement instanceof CreateCatalogStatement) {

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

        } else if (metadataStatement instanceof CreateTableStatement) {

            // Create parameters for metadata workflow
            CreateTableStatement createTableStatement = (CreateTableStatement) metadataStatement;
            String actorRefUri = null;
            ExecutionType executionType = ExecutionType.CREATE_TABLE;
            ResultType type = ResultType.RESULTS;

            if (!existsCatalogInCluster(createTableStatement.getTableName().getCatalogName(),
                    createTableStatement.getClusterName())) {
                executionType = ExecutionType.CREATE_TABLE_AND_CATALOG;

                // Recover ActorRef from ConnectorMetadata
                List<ConnectorMetadata> connectors = MetadataManager.MANAGER.getAttachedConnectors(Status.ONLINE,
                        createTableStatement.getClusterName());
                actorRefUri = connectors.iterator().next().getActorRef();

                // Create MetadataWorkFlow
                metadataWorkflow = new MetadataWorkflow(queryId, actorRefUri, executionType, type);

                // Add CatalogMetadata to the WorkFlow

                metadataWorkflow.setCatalogName(
                        createTableStatement.getTableName().getCatalogName());
                metadataWorkflow
                        .setCatalogMetadata(MetadataManager.MANAGER.getCatalog(createTableStatement.getTableName()
                                .getCatalogName()));
            } else {
                metadataWorkflow = new MetadataWorkflow(queryId, actorRefUri, executionType, type);
            }

            // Create & add TableMetadata to the MetadataWorkflow
            TableName name = createTableStatement.getTableName();
            Map<Selector, Selector> options = createTableStatement.getProperties();
            Map<ColumnName, ColumnMetadata> columnMap = new HashMap<>();
            for (Map.Entry<ColumnName, ColumnType> c : createTableStatement.getColumnsWithTypes().entrySet()) {
                ColumnName columnName = c.getKey();
                ColumnMetadata columnMetadata = new ColumnMetadata(columnName, null, c.getValue());
                columnMap.put(columnName, columnMetadata);
            }
            ClusterName clusterName = createTableStatement.getClusterName();
            List<ColumnName> partitionKey = createTableStatement.getPartitionKey();
            List<ColumnName> clusterKey = createTableStatement.getClusterKey();
            TableMetadata tableMetadata = new TableMetadata(name, options, columnMap, null,
                    clusterName, partitionKey, clusterKey);
            metadataWorkflow.setTableName(name);
            metadataWorkflow.setTableMetadata(tableMetadata);
            metadataWorkflow.setClusterName(clusterName);
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
            String actorRefUri = null;
            ExecutionType executionType = ExecutionType.ATTACH_CLUSTER;
            ResultType type = ResultType.RESULTS;

            managementWorkflow = new ManagementWorkflow(queryId, actorRefUri, executionType, type);

            // Add required information
            managementWorkflow.setClusterName(attachClusterStatement.getClusterName());
            managementWorkflow.setDatastoreName(attachClusterStatement.getDatastoreName());
            managementWorkflow.setOptions(attachClusterStatement.getOptions());

        } else if (metadataStatement instanceof AttachConnectorStatement) {

            // Create parameters for metadata workflow
            AttachConnectorStatement attachConnectorStatement = (AttachConnectorStatement) metadataStatement;
            String actorRef = null;
            ExecutionType executionType = ExecutionType.ATTACH_CONNECTOR;
            ResultType type = ResultType.RESULTS;

            managementWorkflow = new ManagementWorkflow(queryId, actorRef, executionType, type);

            // Add required information
            managementWorkflow.setConnectorName(attachConnectorStatement.getConnectorName());
            managementWorkflow.setClusterName(attachConnectorStatement.getClusterName());
            managementWorkflow.setOptions(attachConnectorStatement.getOptions());

            ConnectorMetadata connector = MetadataManager.MANAGER
                    .getConnector(attachConnectorStatement.getConnectorName());
            managementWorkflow.setActorRef(connector.getActorRef());

        } else {
            throw new PlanningException("This statement can't be planned: " + metadataStatement.toString());
        }

        return managementWorkflow;
    }

    private boolean existsCatalogInCluster(CatalogName catalogName, ClusterName clusterName) {
        CatalogMetadata catalogMetadata = MetadataManager.MANAGER.getCatalog(catalogName);
        Map<TableName, TableMetadata> tables = catalogMetadata.getTables();
        if (tables.isEmpty()) {
            return false;
        }
        for (Map.Entry<TableName, TableMetadata> t : tables.entrySet()) {
            if (t.getValue().getClusterRef().equals(clusterName)) {
                return true;
            }
        }
        return false;
    }

    protected ExecutionWorkflow buildExecutionWorkflow(StorageValidatedQuery query) throws PlanningException {

        String queryId = query.getQueryId();
        String actorRef = null;
        TableName tableName;
        Collection<Row> rows;
        if (query.getStatement() instanceof InsertIntoStatement) {
            tableName = ((InsertIntoStatement) (query.getStatement())).getTableName();
            rows = getInsertRows(((InsertIntoStatement) (query.getStatement())));
        } else {
            throw new PlanningException("Delete, Truncate and Update statements not supported yet");
        }

        TableMetadata tableMetadata = getTableMetadata(tableName);
        ClusterMetadata clusterMetadata = getClusterMetadata(tableMetadata.getClusterRef());
        Map<ConnectorName, ConnectorAttachedMetadata> connectorAttachedRefs = clusterMetadata
                .getConnectorAttachedRefs();

        Iterator it = connectorAttachedRefs.keySet().iterator();
        boolean found = false;
        while (it.hasNext() && !found) {
            ConnectorName connectorName = (ConnectorName) it.next();
            ConnectorMetadata connectorMetadata = MetadataManager.MANAGER.getConnector(connectorName);
            if (connectorMetadata.getSupportedOperations().contains(Operations.INSERT)) {
                actorRef = StringUtils.getAkkaActorRefUri(connectorMetadata.getActorRef());
                found = true;
            }
        }
        if (!found) {
            throw new PlanningException("There is not actorRef for Storage Operation");
        }

        StorageWorkflow storageWorkflow = new StorageWorkflow(queryId, actorRef, ExecutionType.INSERT,
                ResultType.RESULTS);
        storageWorkflow.setClusterName(tableMetadata.getClusterRef());
        storageWorkflow.setTableMetadata(tableMetadata);
        storageWorkflow.setRows(rows);

        return storageWorkflow;
    }

    private Collection<Row> getInsertRows(InsertIntoStatement statement) {
        Collection<Row> rows = new ArrayList<>();

        List<Selector> values = statement.getCellValues();
        List<ColumnName> ids = statement.getIds();

        for (int i = 0; i < ids.size(); i++) {
            ColumnName columnName = ids.get(i);
            Selector value = values.get(i);
            Cell cell = new Cell(value);
            Row row = new Row(columnName.getName(), cell);
            rows.add(row);
        }
        return rows;
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
     * Get the filter operation depending on the type of column and the selector.
     *
     * @param tableName The table metadata.
     * @param selector  The relationship selector.
     * @param operator  The relationship operator.
     * @return An {@link com.stratio.meta.common.connector.Operations} object.
     */
    protected Operations getFilterOperation(final TableMetadata tableName,
            final Selector selector,
            final Operator operator) {
        StringBuilder sb = new StringBuilder("FILTER_");
        if (SelectorType.FUNCTION.equals(selector.getType())) {
            sb.append("FUNCTION_");
        } else {
            ColumnSelector cs = ColumnSelector.class.cast(selector);
            if (tableName.isPK(cs.getName())) {
                sb.append("PK_");
            } else if (tableName.isIndexed(cs.getName())) {
                sb.append("INDEXED_");
            } else {
                sb.append("NON_INDEXED_");
            }
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
            Map<String, TableMetadata> tableMetadataMap,
            SelectValidatedQuery query) {
        LogicalStep previous = null;
        TableMetadata tm = null;
        Selector s = null;
        for (Relation r : query.getRelationships()) {
            s = r.getLeftTerm();
            //TODO Support left-side functions that contain columns of several tables.
            tm = tableMetadataMap.get(s.getSelectorTablesAsString());
            if (tm != null) {
                Operations op = getFilterOperation(tm, s, r.getOperator());
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
     * @param stmt The select statement.
     * @return The resulting map of logical steps.
     */
    private Map<String, LogicalStep> addWindow(Map<String, LogicalStep> lastSteps, SelectStatement stmt){
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
        j.addJoinRelations(queryJoin.getRelations());
        StringBuilder sb = new StringBuilder(targetTable)
                .append("$").append(queryJoin.getTablename().getQualifiedName());
        //Attach to input tables path
        LogicalStep t1 = stepMap.get(targetTable);
        LogicalStep t2 = stepMap.get(queryJoin.getTablename().getQualifiedName());
        t1.setNextStep(j);
        t2.setNextStep(j);
        j.addPreviousSteps(t1, t2);
        j.addSourceIdentifier(targetTable);
        j.addSourceIdentifier(queryJoin.getTablename().getQualifiedName());
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

    protected Select generateSelect(SelectStatement selectStatement, Map<String, TableMetadata> tableMetadataMap) {
        Map<ColumnName, String> aliasMap = new HashMap<>();
        Map<String, ColumnType> typeMap = new HashMap<>();
        for (Selector s : selectStatement.getSelectExpression().getSelectorList()) {
            if (s.getAlias() != null) {
                aliasMap.put(new ColumnName(selectStatement.getTableName(), s.toString()), s.getAlias());

                typeMap.put(s.toString(),
                        tableMetadataMap.get(s.getSelectorTablesAsString()).getColumns()
                                .get(ColumnSelector.class.cast(s).getName()).getColumnType()
                );
            } else {
                aliasMap.put(new ColumnName(selectStatement.getTableName(), s.toString()), s.toString());
            }
        }
        Select result = new Select(Operations.SELECT_OPERATOR, aliasMap, typeMap);
        return result;
    }

}
