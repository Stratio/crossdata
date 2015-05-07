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

import com.codahale.metrics.Timer;
import com.stratio.connector.inmemory.datastore.InMemoryDatastore;
import com.stratio.connector.inmemory.datastore.InMemoryOperations;
import com.stratio.connector.inmemory.datastore.InMemoryQuery;
import com.stratio.connector.inmemory.datastore.datatypes.SimpleValue;
import com.stratio.connector.inmemory.datastore.structures.InMemoryColumnSelector;
import com.stratio.connector.inmemory.datastore.structures.InMemoryFunctionSelector;
import com.stratio.connector.inmemory.datastore.structures.InMemoryLiteralSelector;
import com.stratio.connector.inmemory.datastore.structures.InMemorySelector;
import com.stratio.crossdata.common.connector.IQueryEngine;
import com.stratio.crossdata.common.connector.IResultHandler;
import com.stratio.crossdata.common.data.Cell;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.ResultSet;
import com.stratio.crossdata.common.data.Row;
import com.stratio.crossdata.common.exceptions.ConnectorException;
import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.logicalplan.*;
import com.stratio.crossdata.common.metadata.ColumnMetadata;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.result.QueryResult;
import com.stratio.crossdata.common.statements.structures.*;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.codahale.metrics.MetricRegistry.name;

/**
 * Class that implements the  {@link com.stratio.crossdata.common.connector.IQueryEngine}.
 */
public class InMemoryQueryEngine implements IQueryEngine {

    /**
     * Class logger.
     */
    private static final Logger LOG = Logger.getLogger(InMemoryQueryEngine.class);

    /**
     * Link to the in memory connector.
     */
    private final InMemoryConnector connector;

    private final Timer executeTimer;

    /**
     * Class constructor.
     * @param connector The linked {@link com.stratio.connector.inmemory.InMemoryConnector}.
     */
    public InMemoryQueryEngine(InMemoryConnector connector){
        this.connector = connector;
        executeTimer = new Timer();
        String timerName = name(InMemoryQueryEngine.class, "execute");
        connector.registerMetric(timerName, executeTimer);
    }

    @Override
    public QueryResult execute(LogicalWorkflow workflow) throws ConnectorException {
        //Init Metric
        Timer.Context executeTimerContext = executeTimer.time();

        Project projectOne =  (Project)workflow.getInitialSteps().get(0);
        InMemoryDatastore datastore = connector.getDatastore(projectOne.getClusterName());

        if(datastore == null){
            throw new ExecutionException("No datastore connected to " + projectOne.getClusterName());
        }

        List<List<SimpleValue[]>> tableResults = new ArrayList<>();

        for (LogicalStep project:workflow.getInitialSteps()){
            InMemoryQuery query = null;

            if (tableResults.size()<1){
                query = InMemoryQueryBuilder.instance().build((Project) project);
            }else{
                query = InMemoryQueryBuilder.instance().build((Project)project, tableResults.get(0));
            }

            List<SimpleValue[]> results = null;
            try {
                results = datastore.search(query.getCatalogName(), query);
            } catch (Exception e) {
                throw new ExecutionException("Cannot perform execute operation: " + e.getMessage(), e);
            }

            if (results == null || results.size()== 0){
                break;
            }

            tableResults.add(results);
        }

        List<SimpleValue[]> joinResult = datastore.joinResults(tableResults);

        joinResult = orderResult(joinResult, workflow);

        QueryResult finalResult = toCrossdataResults((Select) workflow.getLastStep(), getFinalLimit(workflow), joinResult);

        //End Metric
        long millis = executeTimerContext.stop();
        LOG.info("Query took " + millis + " nanoseconds");

        return finalResult;
    }

    /**
     * Orthers the results using the orderStep.
     * @param results
     * @return
     * @throws ExecutionException
     */
    public List<SimpleValue[]> orderResult(List<SimpleValue[]> results, LogicalWorkflow workflow) throws ExecutionException {

        OrderBy orderByStep = null;
        LogicalStep current = workflow.getLastStep();
        do {
            if (current instanceof OrderBy){
                orderByStep = (OrderBy) current;
                break;
            }
            current = current.getFirstPrevious ();
        }while(current != null);


        if (orderByStep != null) {
            List<SimpleValue[]> orderedResult = new ArrayList<>();
            if ((results != null) && (!results.isEmpty())) {

                List<String> columnNames = new ArrayList<>();
                for (SimpleValue value: results.get(0)){
                    columnNames.add(value.getColumn().getName());
                }

                for (SimpleValue[] row : results) {
                    if (orderedResult.isEmpty()) {
                        orderedResult.add(row);
                    } else {
                        int order = 0;
                        for (SimpleValue[] orderedRow : orderedResult) {
                            if (compareRows(row, orderedRow, orderByStep, columnNames)) {
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

        return results;

    }

    private boolean compareRows(
            SimpleValue[] candidateRow,
            SimpleValue[] orderedRow,
            OrderBy orderByStep,
            List<String> columnNames) {
        boolean result = false;



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
        InMemorySelector result;
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



    protected int compareCells(SimpleValue toBeOrdered, SimpleValue alreadyOrdered, OrderDirection direction) {
        int result = -1;

        InMemoryOperations.GT.compare(toBeOrdered.getValue(), alreadyOrdered.getValue());

        if(InMemoryOperations.EQ.compare(toBeOrdered.getValue(), alreadyOrdered.getValue())){
            result = 0;
        } else if(direction == OrderDirection.ASC){
            if(InMemoryOperations.LT.compare(toBeOrdered.getValue(), alreadyOrdered.getValue())){
                result = 1;
            }
        } else if(direction == OrderDirection.DESC){
            if(InMemoryOperations.GT.compare(toBeOrdered.getValue(), alreadyOrdered.getValue())){
                result = 1;
            }
        }
        return result;
    }

    private Integer getFinalLimit(LogicalWorkflow workflow){

        if (workflow.getLastStep().getFirstPrevious() instanceof Limit){
            return  Limit.class.cast(workflow.getLastStep().getFirstPrevious()).getLimit();
        }

        return -1;
    }






    /**
     * Transform a set of results into a Crossdata query result.
     * @param selectStep The {@link com.stratio.crossdata.common.logicalplan.Select} step to set the alias.
     * @param limit The query limit.
     * @param results The set of results retrieved from the database.
     * @return A {@link com.stratio.crossdata.common.result.QueryResult}.
     */
    private QueryResult toCrossdataResults(Select selectStep, int limit, List<SimpleValue[]> results) {
        ResultSet crossdataResults = new ResultSet();

        final List<String> columnAlias = new ArrayList<>();
        final List<ColumnMetadata> columnMetadataList = new ArrayList<>();
        for(Selector outputSelector : selectStep.getOutputSelectorOrder()){
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

        //Store the metadata information
        crossdataResults.setColumnMetadata(columnMetadataList);

        int resultToAdd = results.size();
        if(limit != -1){
            resultToAdd = Math.min(results.size(), limit);
        }

        //Store the rows.
        List<Row> crossdataRows = new ArrayList<>();
        Iterator<SimpleValue[]> rowIterator = results.iterator();
        while(rowIterator.hasNext() && resultToAdd > 0){
            crossdataRows.add(toCrossdataRow(rowIterator.next(), columnAlias));
            resultToAdd--;
        }

        crossdataResults.setRows(crossdataRows);
        return QueryResult.createQueryResult(
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
    private Row toCrossdataRow(SimpleValue[] row, List<String> columnAlias) {
        Row result = new Row();

        for (String alias:columnAlias){
            for(SimpleValue field: row){
                if (alias.contains(field.getColumn().getName())){
                    result.addCell(alias, new Cell(field.getValue()));
                    break;
                }
            }
        }

        return result;
    }

    @Override
    public void asyncExecute(String queryId, LogicalWorkflow workflow, IResultHandler resultHandler)
            throws ConnectorException {

        QueryResult queryResult = execute(workflow);
        queryResult.setLastResultSet();
        queryResult.setQueryId(queryId);
        resultHandler.processResult(queryResult);

    }

    @Override public void pagedExecute(
            String queryId,
            LogicalWorkflow workflow,
            IResultHandler resultHandler,
            int pageSize) throws ConnectorException {
        QueryResult queryResult = execute(workflow);
        ResultSet resultSet = queryResult.getResultSet();
        List<Row> rows = resultSet.getRows();
        int counter = 0;
        int page = 0;
        List<Row> partialRows = new ArrayList<>();
        for(Row row: rows){
            if(counter >= pageSize){
                QueryResult partialQueryResult = buildPartialResult(
                        partialRows,
                        queryResult.getResultSet().getColumnMetadata(),
                        queryId,
                        page,
                        false);
                resultHandler.processResult(partialQueryResult);
                counter = 0;
                page++;
                partialRows = new ArrayList<>();
            }
            partialRows.add(row);
            counter++;
        }
        QueryResult partialQueryResult = buildPartialResult(
                partialRows,
                queryResult.getResultSet().getColumnMetadata(),
                queryId,
                page,
                true);
        resultHandler.processResult(partialQueryResult);
    }

    QueryResult buildPartialResult(List<Row> partialRows, List<ColumnMetadata> columnsMetadata, String queryId,
            int page, boolean lastResult){
        ResultSet partialResultSet = new ResultSet();
        partialResultSet.setRows(partialRows);
        partialResultSet.setColumnMetadata(columnsMetadata);
        QueryResult partialQueryResult = QueryResult.createQueryResult(partialResultSet, page, lastResult);
        partialQueryResult.setQueryId(queryId);
        return partialQueryResult;
    }

    @Override
    public void stop(String queryId) throws ConnectorException {
        throw new UnsupportedException("Stopping running queries is not supported");
    }




}
