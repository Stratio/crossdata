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

package com.stratio.connector.inmemory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.stratio.connector.inmemory.datastore.InMemoryDatastore;
import com.stratio.connector.inmemory.datastore.InMemoryOperations;
import com.stratio.connector.inmemory.datastore.InMemoryRelation;
import com.stratio.connector.inmemory.datastore.selector.InMemoryColumnSelector;
import com.stratio.connector.inmemory.datastore.selector.InMemoryFunctionSelector;
import com.stratio.connector.inmemory.datastore.selector.InMemoryLiteralSelector;
import com.stratio.connector.inmemory.datastore.selector.InMemorySelector;
import com.stratio.crossdata.common.connector.IQueryEngine;
import com.stratio.crossdata.common.connector.IResultHandler;
import com.stratio.crossdata.common.data.Cell;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.ResultSet;
import com.stratio.crossdata.common.data.Row;
import com.stratio.crossdata.common.exceptions.ConnectorException;
import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.logicalplan.Filter;
import com.stratio.crossdata.common.logicalplan.Limit;
import com.stratio.crossdata.common.logicalplan.LogicalStep;
import com.stratio.crossdata.common.logicalplan.LogicalWorkflow;
import com.stratio.crossdata.common.logicalplan.OrderBy;
import com.stratio.crossdata.common.logicalplan.Project;
import com.stratio.crossdata.common.logicalplan.Select;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.result.QueryResult;
import com.stratio.crossdata.common.statements.structures.BooleanSelector;
import com.stratio.crossdata.common.statements.structures.ColumnSelector;
import com.stratio.crossdata.common.statements.structures.FloatingPointSelector;
import com.stratio.crossdata.common.statements.structures.FunctionSelector;
import com.stratio.crossdata.common.statements.structures.IntegerSelector;
import com.stratio.crossdata.common.statements.structures.Operator;
import com.stratio.crossdata.common.statements.structures.OrderByClause;
import com.stratio.crossdata.common.statements.structures.OrderDirection;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.common.statements.structures.SelectorType;
import com.stratio.crossdata.common.statements.structures.StringSelector;

/**
 * Class that implements the  {@link com.stratio.crossdata.common.connector.IQueryEngine}.
 */
public class InMemoryQueryEngine implements IQueryEngine{

    /**
     * Link to the in memory connector.
     */
    private final InMemoryConnector connector;

    /**
     * Map with the equivalences between crossdata operators and the ones supported by our datastore.
     */
    private static final Map<Operator, InMemoryOperations> operationsTransformations = new HashMap<>();

    static {
        operationsTransformations.put(Operator.EQ, InMemoryOperations.EQ);
        operationsTransformations.put(Operator.GT, InMemoryOperations.GT);
        operationsTransformations.put(Operator.LT, InMemoryOperations.LT);
        operationsTransformations.put(Operator.GET, InMemoryOperations.GET);
        operationsTransformations.put(Operator.LET, InMemoryOperations.LET);
    }

    /**
     * Class constructor.
     * @param connector The linked {@link com.stratio.connector.inmemory.InMemoryConnector}.
     */
    public InMemoryQueryEngine(InMemoryConnector connector){
        this.connector = connector;
    }

    @Override
    public QueryResult execute(LogicalWorkflow workflow) throws ConnectorException {

        List<Object[]> results;

        Project projectStep;
        OrderBy orderByStep = null;
        Select selectStep;

        //Get the project and select steps.
        try {
            projectStep = Project.class.cast(workflow.getInitialSteps().get(0));

            LogicalStep currentStep = projectStep;
            while(currentStep != null){
                if(currentStep instanceof OrderBy){
                    orderByStep = OrderBy.class.cast(currentStep);
                    break;
                }
                currentStep = currentStep.getNextStep();
            }

            selectStep = Select.class.cast(workflow.getLastStep());
        } catch(ClassCastException e) {
            throw new ExecutionException("Invalid workflow received", e);
        }

        List<InMemoryRelation> relations = getInMemoryRelations(projectStep.getNextStep());
        int limit = getLimit(projectStep.getNextStep());
        String catalogName = projectStep.getCatalogName();
        String tableName = projectStep.getTableName().getName();
        List<InMemorySelector> outputColumns = transformIntoSelectors(selectStep.getColumnMap().keySet());
        InMemoryDatastore datastore = connector.getDatastore(projectStep.getClusterName());
        if(datastore != null){
            try {
                results = datastore.search(catalogName, tableName, relations, outputColumns);
            } catch (Exception e) {
                throw new ExecutionException("Cannot perform execute operation: " + e.getMessage(), e);
            }
        } else {
            throw new ExecutionException("No datastore connected to " + projectStep.getClusterName());
        }

        if(orderByStep != null){
            results = orderResult(results, outputColumns, orderByStep);
        }

        return toCrossdataResults(selectStep, limit, results);
    }

    private List<Object[]> orderResult(
            List<Object[]> results,
            List<InMemorySelector> outputColumns,
            OrderBy orderByStep) throws ExecutionException {
        List<Object[]> orderedResult = new ArrayList<>();
        if((results != null) && (!results.isEmpty())){
            for(Object[] row: results){
                if(orderedResult.isEmpty()){
                    orderedResult.add(row);
                } else {
                    int order = 0;
                    for(Object[] orderedRow: orderedResult){
                        if(compareRows(row, orderedRow, outputColumns, orderByStep)){
                            break;
                        }
                        order++;
                    }
                    orderedResult.add(order, row);
                }
            }
        }
        return orderedResult;
    }

    private boolean compareRows(
            Object[] candidateRow,
            Object[] orderedRow,
            List<InMemorySelector> outputColumns,
            OrderBy orderByStep) {
        boolean result = false;

        List<String> columnNames = new ArrayList<>();
        for(InMemorySelector selector : outputColumns){
            columnNames.add(selector.getName());
        }

        for(OrderByClause clause: orderByStep.getIds()){
            int index = columnNames.indexOf(clause.getSelector().getColumnName().getName());
            int comparison = compareCells(candidateRow[index], orderedRow[index], clause.getDirection());
            if(comparison != 0){
                result = (comparison > 0);
                break;
            }
        }
        return result;
    }

    private int compareCells(Object toBeOrdered, Object alreadyOrdered, OrderDirection direction) {
        int result = -1;
        InMemoryOperations.GT.compare(toBeOrdered, alreadyOrdered);
        if(InMemoryOperations.EQ.compare(toBeOrdered, alreadyOrdered)){
            result = 0;
        } else if(direction == OrderDirection.ASC){
            if(InMemoryOperations.LT.compare(toBeOrdered, alreadyOrdered)){
                result = 1;
            }
        } else if(direction == OrderDirection.DESC){
            if(InMemoryOperations.GT.compare(toBeOrdered, alreadyOrdered)){
                result = 1;
            }
        }
        return result;
    }

    /**
     * Transform a set of crossdata selectors into in-memory ones.
     * @param selectors The set of crossdata selectors.
     * @return A list of in-memory selectors.
     */
    private List<InMemorySelector> transformIntoSelectors(Set<Selector> selectors) {
        List<InMemorySelector> result = new ArrayList<>();
        for(Selector s: selectors){
            result.add(transformCrossdataSelector(s));
        }
        return result;
    }

    /**
     * Transform a Crossdata selector into an InMemory one.
     * @param selector The Crossdata selector.
     * @return The equivalent InMemory selector.
     */
    private InMemorySelector transformCrossdataSelector(Selector selector){
        InMemorySelector result = null;
        if(FunctionSelector.class.isInstance(selector)){
            FunctionSelector xdFunction = FunctionSelector.class.cast(selector);
            String name = xdFunction.getFunctionName();
            List<InMemorySelector> arguments = new ArrayList<>();
            for(Selector arg : xdFunction.getFunctionColumns()){
                arguments.add(transformCrossdataSelector(arg));
            }
            result = new InMemoryFunctionSelector(name, arguments);
        }else if(ColumnSelector.class.isInstance(selector)){
            ColumnSelector cs = ColumnSelector.class.cast(selector);
            result = new InMemoryColumnSelector(cs.getName().getName());
        }else{
            result = new InMemoryLiteralSelector(selector.getStringValue());
        }
        return result;
    }

    /**
     * Transform a set of results into a Crossdata query result.
     * @param selectStep The {@link com.stratio.crossdata.common.logicalplan.Select} step to set the alias.
     * @param limit The query limit.
     * @param results The set of results retrieved from the database.
     * @return A {@link com.stratio.crossdata.common.result.QueryResult}.
     */
    private QueryResult toCrossdataResults(Select selectStep, int limit, List<Object[]> results) {
        ResultSet crossdataResults = new ResultSet();

        final List<String> columnAlias = new ArrayList<>();
        final List<ColumnMetadata> columnMetadataList = new ArrayList<>();
        for(Selector outputSelector : selectStep.getOutputSelectorOrder()){
            //ColumnSelector selector = new ColumnSelector(outputSelector.getColumnName());
            ColumnName columnName = outputSelector.getColumnName();
            String alias = selectStep.getColumnMap().get(outputSelector);
            if(alias == null){
                alias = columnName.getName();
            }
            columnAlias.add(alias);
            columnName.setAlias(alias);
            ColumnType columnType = selectStep.getTypeMapFromColumnName().get(outputSelector);
            ColumnMetadata metadata = new ColumnMetadata(
                    columnName, null, columnType);
            columnMetadataList.add(metadata);
        }

        /*final List<ColumnName> outputColumns = selectStep.getColumnOrder();

        for(ColumnName columnName : outputColumns){
            ColumnSelector selector = new ColumnSelector(columnName);
            String alias = selectStep.getColumnMap().get(selector);
            if(alias == null){
                alias = columnName.getName();
            }
            columnAlias.add(alias);
            columnName.setAlias(alias);
            ColumnType columnType = selectStep.getTypeMapFromColumnName().get(selector);
            ColumnMetadata metadata = new ColumnMetadata(
                    columnName, null, columnType);
            columnMetadataList.add(metadata);
        }*/

        //Store the metadata information
        crossdataResults.setColumnMetadata(columnMetadataList);

        int resultToAdd = results.size();
        if(limit != -1){
            resultToAdd = Math.min(results.size(), limit);
        }

        //Store the rows.
        List<Row> crossdataRows = new ArrayList<>();
        Iterator<Object[]> rowIterator = results.iterator();
        while(rowIterator.hasNext() && resultToAdd > 0){
            crossdataRows.add(toCrossdataRow(rowIterator.next(), columnAlias));
            resultToAdd--;
        }

        crossdataResults.setRows(crossdataRows);
        return QueryResult.createQueryResult(
                UUID.randomUUID().toString(),
                crossdataResults,
                0,
                true);
    }

    /**
     * Transform the results into a crossdata row.
     * @param row The in-memory row.
     * @param columnAlias The list of column alias.
     * @return A {@link com.stratio.crossdata.common.data.Row}
     */
    private Row toCrossdataRow(Object[] row, List<String> columnAlias) {
        Row result = new Row();
        for(int index = 0; index < columnAlias.size(); index++){
            result.addCell(columnAlias.get(index), new Cell(row[index]));
        }
        return result;
    }

    /**
     * Get the list of relations (i.e., Filter operands) in the logical workflow.
     * @param step The first step of the logical workflow.
     * @return A list of {@link com.stratio.connector.inmemory.datastore.InMemoryRelation}.
     */
    private List<InMemoryRelation> getInMemoryRelations(LogicalStep step) throws ExecutionException {
        List<InMemoryRelation> result = new ArrayList<>();
        LogicalStep current = step;
        while(current != null){
            if(Filter.class.isInstance(current)){
                InMemoryRelation r = toInMemoryRelation(Filter.class.cast(current));
                result.add(r);
            }
            current = current.getNextStep();
        }
        return result;
    }

    /**
     * Get the limit on the query if exists.
     * @param step The first step of the logical workflow.
     * @return The limit or -1 if not specified.
     */
    private int getLimit(LogicalStep step){
        int result = -1;
        LogicalStep current = step;
        while(current != null){
            if(Limit.class.isInstance(current)){
                result = Limit.class.cast(current).getLimit();
            }
            current = current.getNextStep();
        }
        return result;
    }

    /**
     * Transform a crossdata relationship into an in-memory relation.
     * @param f The {@link com.stratio.crossdata.common.logicalplan.Filter} logical step.
     * @return An equivalent {@link com.stratio.connector.inmemory.datastore.InMemoryRelation}.
     * @throws ExecutionException If the relationship cannot be translated.
     */
    private InMemoryRelation toInMemoryRelation(Filter f) throws ExecutionException {
        ColumnSelector left = ColumnSelector.class.cast(f.getRelation().getLeftTerm());
        String columnName = left.getName().getName();
        InMemoryOperations relation;

        if(operationsTransformations.containsKey(f.getRelation().getOperator())){
            relation = operationsTransformations.get(f.getRelation().getOperator());
        }else{
            throw new ExecutionException("Operator " + f.getRelation().getOperator() + " not supported");
        }
        Selector rightSelector = f.getRelation().getRightTerm();
        Object rightPart = null;

        if(SelectorType.STRING.equals(rightSelector.getType())){
            rightPart = StringSelector.class.cast(rightSelector).getValue();
        }else if(SelectorType.INTEGER.equals(rightSelector.getType())){
            rightPart = IntegerSelector.class.cast(rightSelector).getValue();
        }else if(SelectorType.BOOLEAN.equals(rightSelector.getType())){
            rightPart = BooleanSelector.class.cast(rightSelector).getValue();
        }else if(SelectorType.FLOATING_POINT.equals(rightSelector.getType())){
            rightPart = FloatingPointSelector.class.cast(rightSelector).getValue();
        }

        return new InMemoryRelation(columnName, relation, rightPart);
    }


    @Override
    public void asyncExecute(String queryId, LogicalWorkflow workflow, IResultHandler resultHandler)
            throws ConnectorException {
        throw new UnsupportedException("Async query execution is not supported");
    }

    @Override
    public void stop(String queryId) throws ConnectorException {
        throw new UnsupportedException("Stopping running queries is not supported");
    }
}
