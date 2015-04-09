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

import static com.codahale.metrics.MetricRegistry.name;

import java.util.*;

import com.stratio.connector.inmemory.datastore.datatypes.JoinValue;
import com.stratio.crossdata.common.logicalplan.*;
import org.apache.log4j.Logger;

import com.codahale.metrics.Timer;
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
        QueryResult finalResult = null;

        Project projectOne =  (Project)workflow.getInitialSteps().get(0);
        InMemoryDatastore datastore = connector.getDatastore(projectOne.getClusterName());

        if(datastore == null){
            throw new ExecutionException("No datastore connected to " + projectOne.getClusterName());
        }

        InMemoryQuery query = new InMemoryQuery(projectOne);

        List<Object[]> results = null;
        try {
            results = datastore.search(query.catalogName, query.tableName, query.relations, query.outputColumns);
        } catch (Exception e) {
            throw new ExecutionException("Cannot perform execute operation: " + e.getMessage(), e);
        }


        if (query.joinStep != null && results.size()>0){
            Project projectTwo =  (Project) workflow.getInitialSteps().get(1);
            InMemoryQuery queryTwo = new InMemoryQuery(projectTwo, results);

            List<Object[]> resultsTwo = null;
            try {
                resultsTwo = datastore.search(queryTwo.catalogName, queryTwo.tableName, queryTwo.relations, queryTwo.outputColumns);
            } catch (Exception e) {
                throw new ExecutionException("Cannot perform execute operation: " + e.getMessage(), e);
            }

            results = joinResults(results, resultsTwo);
        }




        results = query.orderResult(results);
        finalResult = toCrossdataResults(query.selectStep, query.limit, results);


        //End Metric
        long millis = executeTimerContext.stop();
        LOG.info("Query took " + millis + " nanoseconds");

        return finalResult;
    }

    private List<Object[]> joinResults(List<Object[]> results, List<Object[]> resultsTwo) {

        List<Object[]> finalResult = new ArrayList<Object[]>();
        if (resultsTwo.size() > 0) {
            for (Object[] row : results) {
                List<Object> rowResult = new ArrayList<>();
                for (Object field:row){
                    if (field instanceof JoinValue){

                        for(Object[] otherRow: resultsTwo){
                            for (Object otherField:otherRow) {
                                if (otherField instanceof JoinValue) {
                                    if (((JoinValue) field).getValue().equals(((JoinValue) otherField).getValue())){
                                        for (Object otherField2:otherRow) {
                                            if (!(otherField2 instanceof JoinValue)){
                                                rowResult.add(otherField2);
                                            }
                                        }
                                    }
                            }
                            }
                        }
                    }else{
                        rowResult.add(field);
                    }
                }
                finalResult.add(rowResult.toArray(new Object[]{}));
            }
        }

        return finalResult;
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








    @Override
    public void asyncExecute(String queryId, LogicalWorkflow workflow, IResultHandler resultHandler)
            throws ConnectorException {
        throw new UnsupportedException("Async query execution is not supported");
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
