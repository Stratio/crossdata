/*
 * Licensed to STRATIO (C) under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright ownership. The STRATIO
 * (C) licenses this file to you under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.stratio.meta2.core.planner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.stratio.meta.common.connector.Operations;
import com.stratio.meta.common.exceptions.PlanningException;
import com.stratio.meta.common.executionplan.ExecutionType;
import com.stratio.meta.common.executionplan.ExecutionWorkflow;
import com.stratio.meta.common.executionplan.MetadataWorkflow;
import com.stratio.meta.common.executionplan.QueryWorkflow;
import com.stratio.meta.common.executionplan.ResultType;
import com.stratio.meta.common.executionplan.StorageWorkflow;
import com.stratio.meta.common.logicalplan.Filter;
import com.stratio.meta.common.logicalplan.Join;
import com.stratio.meta.common.logicalplan.Limit;
import com.stratio.meta.common.logicalplan.LogicalStep;
import com.stratio.meta.common.logicalplan.LogicalWorkflow;
import com.stratio.meta.common.logicalplan.Project;
import com.stratio.meta.common.logicalplan.Select;
import com.stratio.meta.common.logicalplan.UnionStep;
import com.stratio.meta.common.statements.structures.relationships.Operator;
import com.stratio.meta.common.statements.structures.relationships.Relation;
import com.stratio.meta.core.structures.InnerJoin;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.Status;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.metadata.CatalogMetadata;
import com.stratio.meta2.common.metadata.ColumnType;
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
import com.stratio.meta2.core.statements.CreateCatalogStatement;
import com.stratio.meta2.core.statements.CreateTableStatement;
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
        ExecutionWorkflow executionWorkflow = buildExecutionWorkflow(workflow);
        //Return the planned query.
        SelectPlannedQuery pq = new SelectPlannedQuery(query, executionWorkflow);
        return pq;
    }

    public MetadataPlannedQuery planQuery(MetadataValidatedQuery query) throws PlanningException {
        ExecutionWorkflow executionWorkflow = buildExecutionWorkflow(query);
        return new MetadataPlannedQuery(query, executionWorkflow);
    }

    public StoragePlannedQuery planQuery(StorageValidatedQuery query){
        ExecutionWorkflow executionWorkflow = buildExecutionWorkflow(query);
        return new StoragePlannedQuery(query, executionWorkflow);
    }

    /**
     * Build a Logical workflow for the incoming validated query.
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

    protected ExecutionWorkflow buildExecutionWorkflow(LogicalWorkflow workflow) throws PlanningException {

        List<TableName> tables = getInitialSteps(workflow.getInitialSteps());

        Map<TableName, List<ConnectorMetadata>> candidatesConnectors = findCapableConnectors(tables,
                workflow.getInitialSteps());

        ConnectorMetadata chosenConnector = findMoreSuitableConnector(candidatesConnectors);

        // TODO: Create this object properly
        ExecutionWorkflow executionWorkflow = new ExecutionWorkflow(null, null, null, null);

        return executionWorkflow;
    }

    protected List<TableName> getInitialSteps(List<LogicalStep> initialSteps){
        List<TableName> tables = new ArrayList<>(initialSteps.size());
        for(LogicalStep ls: initialSteps){
            tables.add(Project.class.cast(ls).getTableName());
        }
        return tables;
    }


    protected void defineExecutionWorkflow(LogicalWorkflow workflow) {
        List<TableName> tables = getInitialSteps(workflow.getInitialSteps());
        //Get the list of connector attached to the clusters that contain the required tables.
        Map<TableName, List<ConnectorMetadata>> candidatesConnectors = MetadataManager.MANAGER
                .getAttachedConnectors(Status.ONLINE, tables);
        List<ExecutionWorkflow> executionWorkflows = new ArrayList<>();
        Map<UnionStep, List<String>> joinActors = new HashMap<>();

        //Refine the list of available connectors and determine which connector to be used.
        for (LogicalStep ls : workflow.getInitialSteps()) {
            updateExecutionWorkflow(executionWorkflows, joinActors,
                    ls, candidatesConnectors.get(Project.class.cast(ls).getTableName().getQualifiedName()));
        }
    }

    protected void updateExecutionWorkflow(
            List<ExecutionWorkflow> executionWorkflows,
            Map<UnionStep, List<String>> joinActors,
            LogicalStep initial,
            List<ConnectorMetadata> connectors){
        //QueryWorkflow workflow = new QueryWorkflow();
    }

    protected ConnectorMetadata findMoreSuitableConnector(Map<TableName, List<ConnectorMetadata>> candidatesConnectors)
            throws PlanningException {
        ConnectorMetadata chosenConnector;
        if(candidatesConnectors.isEmpty()){
            throw new PlanningException("No connector meets the required capabilities.");
        } else {
            // TODO: we shouldn't choose the first candidate, we should choose the best one
            chosenConnector = candidatesConnectors.values().iterator().next().get(0);
        }
        return chosenConnector;
    }

    protected Map<TableName, List<ConnectorMetadata>> findCapableConnectors(List<TableName> tables,
            List<LogicalStep> initialSteps){

        //Get the list of connector attached to the clusters that contain the required tables.
        Map<TableName, List<ConnectorMetadata>> candidatesConnectors = MetadataManager.MANAGER.getAttachedConnectors(
                Status.ONLINE, tables);

        //Refine the list of available connectors and determine which connector to be used.
        for(LogicalStep ls: initialSteps){

            TableName tableName = Project.class.cast(ls).getTableName();

            updateCandidates(tableName, ls, candidatesConnectors);

            /*
            * TODO  We go through all the path from every initial step to the final,
            * which causes double checking of the common path. This logic has to be improved.
            * */
            LogicalStep nextLogicalStep = ls.getNextStep();
            while(nextLogicalStep != null){
                updateCandidates(tableName, nextLogicalStep, candidatesConnectors);
                nextLogicalStep = nextLogicalStep.getNextStep();
            }
        }
        return candidatesConnectors;
    }

    /**
     * Filter the list of connector candidates attached to the cluster that a table belongs to,
     * according to the capabilities required by a logical step.
     * @param tableName TABLE name extracted from the first step (see {@link Project}).
     * @param ls Logical Step containing the {@link com.stratio.meta.common.connector.Operations} to be checked.
     * @param candidatesConnectors Map with the Connectors (see {@link com.stratio.meta2.common.metadata.ConnectorMetadata}) that already met the previous
     *                             Operations.
     */
    public void updateCandidates(TableName tableName, LogicalStep ls,
            Map<TableName, List<ConnectorMetadata>> candidatesConnectors){
        Operations operations = ls.getOperation();
        List<ConnectorMetadata> connectorList = candidatesConnectors.get(tableName);
        List<ConnectorMetadata> rejectedConnectors = new ArrayList<>();
        for(ConnectorMetadata connectorMetadata: connectorList){
            if(!connectorMetadata.getSupportedOperations().getOperation().contains(operations)){
                rejectedConnectors.add(connectorMetadata);
            }
        }
        connectorList.removeAll(rejectedConnectors);
        if(connectorList.isEmpty()){
            candidatesConnectors.remove(tableName);
        } else {
            candidatesConnectors.put(tableName, connectorList);
        }

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

        //Add join
        if (query.getJoin() != null) {
            processed = addJoin(processed, selectTable, query);
        }

        //Prepare the result.
        List<LogicalStep> initialSteps = new ArrayList<>();
        LogicalStep initial = null;
        for (LogicalStep ls: processed.values()) {
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
        SelectStatement ss = SelectStatement.class.cast(query.getStatement());
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
        String queryId = query.getQueryId();
        MetadataWorkflow metadataWorkflow = null;
        if (metadataStatement instanceof CreateCatalogStatement){

            // Create parameters for metadata workflow
            CreateCatalogStatement createCatalogStatement = (CreateCatalogStatement) metadataStatement;
            Serializable actorRef = null;
            ExecutionType executionType = ExecutionType.CREATE_CATALOG;
            ResultType type = ResultType.RESULTS;

            metadataWorkflow = new MetadataWorkflow(queryId, actorRef, executionType, type);

            // Create & add CatalogMetadata to the MetadataWorkflow
            CatalogName name = createCatalogStatement.getCatalogName();
            Map<Selector, Selector> options = createCatalogStatement.getOptions();
            Map<TableName, TableMetadata> tables = new HashMap<>();
            CatalogMetadata catalogMetadata = new CatalogMetadata(name, options, tables);
            metadataWorkflow.setCatalogMetadata(catalogMetadata);

            // Add CatalogMetadata to MetadataManager
            MetadataManager.MANAGER.createCatalog(catalogMetadata);

        } else if(metadataStatement instanceof CreateTableStatement){
            CreateTableStatement createTableStatement = (CreateTableStatement) metadataStatement;
        }

        return metadataWorkflow;
    }

    protected ExecutionWorkflow buildExecutionWorkflow(StorageValidatedQuery query) {
        StorageWorkflow storageWorkflow = null;
        return storageWorkflow;
    }

    /**
     * Add the columns that need to be retrieved to the initial steps map.
     *
     * @param projectSteps The map associating table names to Project steps.
     * @param query        The query to be planned.
     */
    private void addProjectedColumns(Map<String, LogicalStep> projectSteps, SelectValidatedQuery query) {
        for (ColumnName cn: query.getColumns()) {
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
        for (Selector s: selectStatement.getSelectExpression().getSelectorList()) {
            if (s.getAlias() != null) {
                aliasMap.put(new ColumnName(selectStatement.getTableName(), s.toString()), s.getAlias());

                typeMap.put(s.toString(),
                        tableMetadataMap.get(s.getSelectorTablesAsString()).getColumns()
                                .get(ColumnSelector.class.cast(s).getName()).getColumnType());
            } else {
                aliasMap.put(new ColumnName(selectStatement.getTableName(), s.toString()), s.toString());
            }
        }
        Select result = new Select(Operations.SELECT_OPERATOR, aliasMap, typeMap);
        return result;
    }

}
