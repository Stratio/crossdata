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
import java.util.Objects;

import com.stratio.connector.inmemory.datastore.InMemoryDatastore;
import com.stratio.connector.inmemory.datastore.InMemoryOperations;
import com.stratio.connector.inmemory.datastore.InMemoryRelation;
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
import com.stratio.crossdata.common.logicalplan.Project;
import com.stratio.crossdata.common.logicalplan.Select;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.result.QueryResult;
import com.stratio.crossdata.common.statements.structures.BooleanSelector;
import com.stratio.crossdata.common.statements.structures.ColumnSelector;
import com.stratio.crossdata.common.statements.structures.FloatingPointSelector;
import com.stratio.crossdata.common.statements.structures.IntegerSelector;
import com.stratio.crossdata.common.statements.structures.Operator;
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

        List<Object[]> results = null;

        Project projectStep = null;
        Select selectStep = null;

        //Get the project and select steps.
        try {
            projectStep = Project.class.cast(workflow.getInitialSteps().get(0));
            selectStep = Select.class.cast(workflow.getLastStep());
        }catch(ClassCastException e){
            throw new ExecutionException("Invalid workflow received", e);
        }

        List<InMemoryRelation> relations = getInMemoryRelations(projectStep.getNextStep());
        int limit = getLimit(projectStep.getNextStep());
        String catalogName = projectStep.getCatalogName();
        String tableName = projectStep.getTableName().getName();

        InMemoryDatastore datastore = connector.getDatastore(projectStep.getClusterName());
        if(datastore != null){
            List<String> outputColumns = new ArrayList<>();
            for(ColumnName name : selectStep.getColumnOrder()){
                outputColumns.add(name.getName());
            }
            try {
                results = datastore.search(catalogName, tableName, relations, outputColumns);
            } catch (Exception e) {
                throw new ExecutionException("Cannot perform execute operation: " + e.getMessage(), e);
            }
        }else{
            throw new ExecutionException("No datastore connected to " + projectStep.getClusterName());
        }
        return toCrossdataResults(selectStep, limit, results);
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
        final List<ColumnName> outputColumns = selectStep.getColumnOrder();

        List<ColumnMetadata> columnMetadataList = new ArrayList<>();

        for(ColumnName columnName : outputColumns){
            columnAlias.add(selectStep.getColumnMap().get(columnName));
            columnName.setAlias(selectStep.getColumnMap().get(columnName));
            ColumnMetadata metadata = new ColumnMetadata(
                    columnName, null, selectStep.getTypeMapFromColumnName().get(columnName));
            columnMetadataList.add(metadata);
        }

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
        return QueryResult.createQueryResult(crossdataResults);
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
        InMemoryOperations relation = null;

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
