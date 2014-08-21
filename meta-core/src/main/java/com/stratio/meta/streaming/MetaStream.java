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

package com.stratio.meta.streaming;

import com.stratio.deep.context.DeepSparkContext;
import com.stratio.deep.entity.Cells;
import com.stratio.meta.common.actor.ActorResultListener;
import com.stratio.meta.common.data.CassandraResultSet;
import com.stratio.meta.common.data.Cell;
import com.stratio.meta.common.data.Row;
import com.stratio.meta.common.metadata.structures.ColumnMetadata;
import com.stratio.meta.common.metadata.structures.ColumnType;
import com.stratio.meta.common.result.CommandResult;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.statements.MetaStatement;
import com.stratio.meta.core.statements.SelectStatement;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.streaming.api.IStratioStreamingAPI;
import com.stratio.streaming.commons.messages.StreamQuery;
import com.stratio.streaming.commons.streams.StratioStream;
import com.stratio.streaming.messaging.ColumnNameType;

import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class MetaStream {

  /**
   * Class logger.
   */
  private static final Logger LOG = Logger.getLogger(MetaStream.class);

  /**
   * Map of query identifiers with the associated callback actor that will send the results back
   * to the client.
   */
  //TODO: Migrate to Hazelcast
  private static Map<String, ActorResultListener> callbackActors = new HashMap<>();

  /**
   * Map of user query identifiers with their corresponding streaming query identifier.
   */
  //TODO: Migrate to Hazelcast
  private static Map<String, String> streamingQueries = new HashMap<>();

  /**
   * Map of user queries with the number of result pages sent to the user.
   */
  //TODO: Migrate to Hazelcast
  private static Map<String, Integer> resultPages = new HashMap<>();

  /**
   * Map of query identifier with the associated MetaStatement.
   */
  //TODO: Migrate to Hazelcast
  private static Map<String, MetaStatement> queryStatements = new HashMap<>();

  /**
   * Map of which ephemeral table is associated with a particular query identifier.
   */
  //TODO: Migrate to Hazelcast
  private static Map<String, String> streamingQueryEphemeralTable = new HashMap<>();

  /**
   * Create a new ephemeral table.
   * @param queryId The query identifier.
   * @param stratioStreamingAPI The Stratio Streaming API.
   * @param tableName The name of the ephemeral table.
   * @param columnList The list of columns.
   * @param config The engine config.
   * @return A {@link com.stratio.meta.common.result.Result}.
   */
  public static Result createEphemeralTable(String queryId,
                                            IStratioStreamingAPI stratioStreamingAPI,
                                            String tableName, List<ColumnNameType> columnList,
                                            EngineConfig config){
    Result result = CommandResult
        .createCommandResult("Ephemeral table '" + tableName + "' created.");
    try {
      stratioStreamingAPI.createStream(tableName, columnList);
      //Listen so it is created.
      stratioStreamingAPI.listenStream(tableName);
    } catch (Exception e) {
      result = Result.createExecutionErrorResult(tableName
                                                 + " couldn't be created"
                                                 + System.lineSeparator()
                                                 + e.getMessage());
    }
    result.setQueryId(queryId);
    return result;
  }

  /**
   * Remove an ephemeral table.
   * @param queryId The query identifier.
   * @param stratioStreamingAPI The Stratio Streaming API.
   * @param tableName The name of the ephemeral table.
   * @return A {@link com.stratio.meta.common.result.Result}
   */
  public static Result dropEphemeralTable(String queryId,
                                          IStratioStreamingAPI stratioStreamingAPI,
                                          String tableName) {
    Result result = CommandResult
        .createCommandResult("Ephemeral table " + tableName + " has been deleted.");
    try {
      stratioStreamingAPI.dropStream(tableName);
    } catch (Exception e) {
      result = Result.createExecutionErrorResult(tableName
                                                 + " cannot be deleted"
                                                 + System.lineSeparator()
                                                 + e.getMessage());
    }
    result.setQueryId(queryId);
    return result;
  }

  public static Result removeStreamingQuery(String queryId, IStratioStreamingAPI stratioStreamingAPI){
    String streamingQueryIdentifier = streamingQueries.get(queryId);
    Result result = CommandResult.createCommandResult("Query " + queryId + " removed");
    if(streamingQueryIdentifier != null){
      //remove streaming query
      try {
        stratioStreamingAPI.removeQuery(streamingQueryEphemeralTable.get(queryId), streamingQueryIdentifier);
        //clean maps
        streamingQueries.remove(queryId);
        streamingQueryEphemeralTable.remove(queryId);
        callbackActors.remove(queryId);
        resultPages.remove(queryId);
        queryStatements.remove(queryId);
      } catch (Exception e) {
        result = Result.createExecutionErrorResult("Cannot remove streaming query " + queryId
                                                   + System.lineSeparator() + e.getMessage());
        LOG.error("Cannot remove streaming query: " + queryId, e);
      }

    }else{
      result = Result.createExecutionErrorResult("Streaming query " + queryId + " not found in server.");
    }
    return result;
  }

  public static String startQuery(String queryId,
                                  IStratioStreamingAPI stratioStreamingAPI,
                                  SelectStatement ss,
                                  EngineConfig config,
                                  ActorResultListener callbackActor,
                                  DeepSparkContext dsc,
                                  boolean isRoot){
    callbackActors.put(queryId, callbackActor);

    String ks = ss.getEffectiveCatalog();
    String streamName = ks+"_"+ss.getTableName();
    try {
      String outgoing = streamName+"_"+ queryId.replace("-", "_");

      LOG.debug("Outgoing topic: "+outgoing);

      // Create topic
      String query = ss.translateToSiddhi(stratioStreamingAPI, streamName, outgoing);
      String streamingQueryId = stratioStreamingAPI.addQuery(streamName, query);
      streamingQueries.put(queryId, streamingQueryId);
      resultPages.put(queryId, 0);
      streamingQueryEphemeralTable.put(queryId, streamName);
      queryStatements.put(queryId, ss);

      LOG.info("queryId = " + queryId);
      stratioStreamingAPI.listenStream(outgoing);

      //Insert dumb element in topic while the Kafka bug is addressed.
      StreamingUtils.insertRandomData(stratioStreamingAPI, outgoing);

      LOG.debug("Consuming outgoing Kafka topic: "+outgoing);

      List<Object> results = new ArrayList<>();

      StreamingConsumer consumer = new StreamingConsumer(outgoing, config.getZookeeperServer(), config.getJobName(), results);
      consumer.start();

      StreamListener listener = new StreamListener(results, dsc, callbackActor, queryId, ks, isRoot);
      listener.start();

      //Thread.sleep(2000);
      //StreamingUtils.insertRandomData(stratioStreamingAPI, streamName, 6000, 4, 4);

      return "Streaming QID: " + queryId + " finished";
    } catch (Exception e) {
      LOG.error(e);
      return "ERROR: "+e.getMessage();
    }
  }

  public static void sendResultsToNextStep(List<Object> data, DeepSparkContext dsc, ActorResultListener callBackActor, String queryId, String ks, boolean isRoot) {

    LOG.debug("Data for the next step = " + Arrays.toString(data.toArray()));

    if(isRoot){
      sendPartialResultsToClient(data, callBackActor, queryId, ks);
    } else {
      sendToNextTreeNode(data, dsc, callBackActor, queryId, ks);
    }

  }

  private static void sendPartialResultsToClient(List<Object> data, ActorResultListener callBackActor, String queryId, String ks) {
    CassandraResultSet crs = new CassandraResultSet();
    for(Object obj: data){
      Row newRow = new Row();
      List row = (List) obj;
      for(Object columnObj: row){
        Map column = (Map) columnObj;
        String colName = (String) column.get("column");
        Object value = column.get("value");
        String colType = (String) column.get("type");
        newRow.addCell(colName, new Cell(value));
      }
      crs.add(newRow);
    }
    QueryResult queryResult = QueryResult.createSuccessQueryResult(crs, ks);
    queryResult.setQueryId(queryId);
    Integer page = resultPages.get(queryId);
    queryResult.setResultPage(page);
    resultPages.put(queryId, page + 1);
    callBackActor.processResults(queryResult);
  }

  private static void sendToNextTreeNode(List<Object> data, DeepSparkContext dsc, ActorResultListener callBackActor, String queryId, String ks) {
    JavaRDD<Cells> rdd = convertJsonToDeep(data, dsc);

    CassandraResultSet crs = new CassandraResultSet();
    crs.add(new Row("RDD", new Cell(rdd)));

    List<ColumnMetadata> columns = new ArrayList<>();
    ColumnMetadata metadata = new ColumnMetadata("RDD", "RDD");
    ColumnType type = ColumnType.VARCHAR;
    type.setDBMapping("class", JavaRDD.class);
    metadata.setType(type);
    crs.setColumnMetadata(columns);

    QueryResult queryResult = QueryResult.createSuccessQueryResult(crs, ks);
    queryResult.setQueryId(queryId);
    Integer page = resultPages.get(queryId);
    queryResult.setResultPage(page);
    resultPages.put(queryId, page + 1);
    callBackActor.processResults(queryResult);
  }

  private static JavaRDD<Cells> convertJsonToDeep(List<Object> data, DeepSparkContext dsc) {
    List<Cells> deepCells = new ArrayList<>();
    for(Object obj: data){
      Cells newRow = new Cells();
      List row = (List) obj;
      for(Object columnObj: row){
        Map column = (Map) columnObj;
        String colName = (String) column.get("column");
        //String value = (String) column.get("value");
        Object value = column.get("value");
        String colType = (String) column.get("type");

        com.stratio.deep.entity.Cell newCell =
            com.stratio.deep.entity.Cell.create(colName, value);

        newRow.add(newCell);
      }
      deepCells.add(newRow);
    }

    LOG.debug("Creating RDD from Deep Cells.");

    JavaSparkContext jsc = new JavaSparkContext(dsc.sc());

    JavaRDD<Cells> rdd = jsc.parallelize(deepCells);

    LOG.debug("RDD.count = " + rdd.count());

    return rdd;
  }

  /**
   * Send the results to the associated listener.
   * @param results The results.
   */
  public static void sendResults(Result results){
    callbackActors.get(results.getQueryId()).processResults(results);
  }

  public static Result listStreamingQueries(String queryId, IStratioStreamingAPI stratioStreamingAPI){
    Result result = Result.createExecutionErrorResult("Cannot list streaming queries.");
    Map<String, String> transformedQueriesId = new HashMap<>();
    for(Map.Entry<String, String> q : streamingQueries.entrySet()){
      transformedQueriesId.put(q.getValue(), q.getKey());
    }
    try {
      List<Row> rows = new ArrayList<>();
      for (StratioStream stream : stratioStreamingAPI.listStreams()) {
        for (StreamQuery query : stream.getQueries()) {
          String qid = transformedQueriesId.get(query.getQueryId());
          if (qid != null) {
            Row r = new Row();
            SelectStatement ss = SelectStatement.class.cast(queryStatements.get(qid));
            r.addCell("QID", new Cell(qid));
            r.addCell("Table", new Cell(ss.getKeyspace() + "." + ss.getTableName()));
            r.addCell("Type", new Cell(MetaPath.STREAMING));
            r.addCell("Query", new Cell(ss.toString()));
            rows.add(r);
          }
        }
      }
      CassandraResultSet resultSet = new CassandraResultSet();
      resultSet.setRows(rows);
      List<ColumnMetadata> columns = new ArrayList<>();
      columns.add(new ColumnMetadata("streaming", "QID", ColumnType.TEXT));
      columns.add(new ColumnMetadata("streaming", "Table", ColumnType.TEXT));
      columns.add(new ColumnMetadata("streaming", "Type", ColumnType.TEXT));
      columns.add(new ColumnMetadata("streaming", "Query", ColumnType.TEXT));
      resultSet.setColumnMetadata(columns);
      result = QueryResult.createQueryResult(resultSet);
    } catch (Exception e) {
      LOG.error("Cannot list streaming queries", e);
    }
    return result;
  }

}
