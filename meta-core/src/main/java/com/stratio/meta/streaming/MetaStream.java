/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.streaming;

import com.stratio.deep.context.DeepSparkContext;
import com.stratio.meta.common.actor.ActorResultListener;
import com.stratio.meta.common.data.CassandraResultSet;
import com.stratio.meta.common.data.Cell;
import com.stratio.meta.common.data.Row;
import com.stratio.meta.common.metadata.structures.ColumnMetadata;
import com.stratio.meta.common.metadata.structures.ColumnType;
import com.stratio.meta.common.result.CommandResult;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.engine.Engine;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.executor.StreamExecutor;
import com.stratio.meta.core.statements.MetaStatement;
import com.stratio.meta.core.statements.SelectStatement;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.streaming.api.IStratioStreamingAPI;
import com.stratio.streaming.commons.messages.StreamQuery;
import com.stratio.streaming.commons.streams.StratioStream;
import com.stratio.streaming.messaging.ColumnNameType;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkEnv;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Time;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import scala.Tuple2;

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

  public static void printMaps(){
    LOG.info("");
    LOG.info("=================== Status Maps =============================");
    for(Map.Entry<String, ActorResultListener> e : callbackActors.entrySet()){
      LOG.info("QID: " + e.getKey() + " actor: " + e.getValue());
    }
    for(Map.Entry<String, String> e : streamingQueries.entrySet()){
      LOG.info("QID: " + e.getKey() + " SID: " + e.getValue());
    }
    for(Map.Entry<String, Integer> e : resultPages.entrySet()){
      LOG.info("QID: " + e.getKey() + " resultPage: " + e.getValue());
    }
    for(Map.Entry<String, MetaStatement> e : queryStatements.entrySet()){
      LOG.info("QID: " + e.getKey() + " Stmt: " + e.getValue().toString());
    }
    for(Map.Entry<String, String> e : streamingQueryEphemeralTable.entrySet()){
      LOG.info("Ephemeral: " + e.getKey() + " QID: " + e.getValue());
    }
    LOG.info("=================== End Status Maps =============================");
    LOG.info("");
  }

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

  /**
   * Create an Spark Streaming context.
   * @param config The engine config.
   * @param retries Number of times the creation would be retried in case of failure.
   * @return A {@link org.apache.spark.streaming.api.java.JavaStreamingContext}.
   */
  //TODO: Move number of retries to EngineConfig
  public static JavaStreamingContext createSparkStreamingContext(
      EngineConfig config, DeepSparkContext deepSparkContext, int retries){
    //JavaSparkContext sparkContext = new JavaSparkContext(config.getSparkMaster(), "MetaStreaming");
    LOG.info("Creating JavaStreamingContext with Spark Master: " + config.getSparkMaster());
    SparkConf sparkConf = new SparkConf().setMaster(config.getSparkMaster())
                                         .setAppName("MetaStreaming")
                                         //.set("spark.driver.port", String.valueOf(StreamingUtils.findFreePort()))
                                          .set("spark.driver.port", "0")
                                         //.set("spark.ui.port", String.valueOf(StreamingUtils.findFreePort()));
                                          .set("spark.ui.port", "0");

    JavaStreamingContext jssc = null;
    int attempt = 0;
    while(jssc == null && attempt < retries){
      attempt++;
      try {
        /*
        jssc = new JavaStreamingContext(//deepSparkContext.getConf().set("spark.cleaner.ttl", "1000"),//.getConf()
            sparkContext.getConf()
                            .set("spark.driver.port", String.valueOf(StreamingUtils.findFreePort()))
                            .set("spark.ui.port", String.valueOf(StreamingUtils.findFreePort())),
            new Duration(config.getStreamingDuration()));
        */
        jssc = new JavaStreamingContext(sparkConf, new Duration(config.getStreamingDuration()));

      } catch (Throwable t){
        jssc = null;
        LOG.error("Cannot create Streaming Context. Trying it again.", t);
      }
    }

    if(attempt >= retries){
      LOG.error("Cannot create Streaming context after " + attempt + " retries.");
      jssc = null;
    }else {
      LOG.info("MetaStreaming context created: " + jssc.toString());
    }

    return jssc;
  }

  public static Result removeStreamingQuery(String queryId, IStratioStreamingAPI stratioStreamingAPI){
    String streamingQueryIdentifier = streamingQueries.get(queryId);
    Result result = CommandResult.createCommandResult("Query " + queryId + " removed");
    if(streamingQueryIdentifier != null){
      //remove streaming query
      try {
        stratioStreamingAPI.removeQuery(streamingQueryEphemeralTable.get(queryId), streamingQueryIdentifier);
        //Stop context
        StreamExecutor.stopContext(queryId);
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

  public static String listenStream(final String queryId, IStratioStreamingAPI stratioStreamingAPI,
                                    SelectStatement ss,
                                    EngineConfig config,
                                    JavaStreamingContext jssc,
                                    final ActorResultListener callbackActor){
    callbackActors.put(queryId, callbackActor);


    final String ks = ss.getEffectiveKeyspace();
    final String streamName = ks+"_"+ss.getTableName();
    try {
      final String outgoing = streamName+"_"+ queryId.replace("-", "_");

      // Create topic
      String query = ss.translateToSiddhi(stratioStreamingAPI, streamName, outgoing);
      final String streamingQueryId = stratioStreamingAPI.addQuery(streamName, query);
      streamingQueries.put(queryId, streamingQueryId);
      resultPages.put(queryId, 0);
      streamingQueryEphemeralTable.put(queryId, streamName);
      queryStatements.put(queryId, ss);

      StreamExecutor.addContext(queryId, jssc);
      LOG.info("queryId = " + streamingQueryId);
      stratioStreamingAPI.listenStream(outgoing);

      //Insert dumb element in topic while the Kafka bug is addressed.
      StreamingUtils.insertRandomData(stratioStreamingAPI, outgoing);

      // Create stream reading outgoing Kafka topic
      Map<String, Integer> topics = new HashMap<>();
      //Map of (topic_name -> numPartitions) to consume. Each partition is consumed in its own thread
      topics.put(outgoing, 1);
      // jssc: JavaStreamingContext, zkQuorum: String, groupId: String, topics: Map<String, integer>
      final JavaPairDStream<String, String>
          dstream =
          KafkaUtils.createStream(jssc, config.getZookeeperServer(), config.getStreamingGroupId(), topics);

      final long duration = ss.getWindow().getDurationInMilliseconds();

      StreamingUtils.insertRandomData(stratioStreamingAPI, streamName, duration, 10, 2);

      Time timeWindow = new Time(duration);
      LOG.debug("Time Window = "+timeWindow.toString());

      dstream.foreachRDD(new Function<JavaPairRDD<String, String>, Void>() {

        @Override
        public Void call(JavaPairRDD<String, String> stringStringJavaPairRDD) throws Exception {
          final long totalCount = stringStringJavaPairRDD.count();
          LOG.info(streamingQueryId+": Count=" + totalCount);
          if(totalCount > 0){
            //Create resultset
            stringStringJavaPairRDD.values().foreach(new VoidFunction<String>() {
              public CassandraResultSet resultSet = new CassandraResultSet();

              @Override
              public void call(String s) throws Exception {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> myMap = objectMapper.readValue(s, HashMap.class);
                ArrayList columns = (ArrayList) myMap.get("columns");
                String cols = Arrays.toString(columns.toArray());
                LOG.debug("Columns = " + cols);

                List<ColumnMetadata> resultMetadata = new ArrayList<>();
                Row metaRow = new Row();
                for (Object column: columns) {
                  Map columnMap = (Map) column;

                  metaRow.addCell(String.valueOf(columnMap.get("column")),
                                  new Cell(columnMap.get("value")));
                  if((resultSet.getColumnMetadata() == null) || (resultSet.getColumnMetadata().size() < 1)){
                    resultMetadata.add(
                        new ColumnMetadata(outgoing,
                                           String.valueOf(columnMap.get("column")),
                                           StreamingUtils.streamingToMetaType((String) columnMap.get("type"))));
                    resultSet.setColumnMetadata(resultMetadata);
                  }
                }
                resultSet.add(metaRow);
                if(resultSet.size() >= totalCount){
                  LOG.info("resultSet.size="+resultSet.size());
                  QueryResult queryResult = QueryResult.createSuccessQueryResult(resultSet, ks);
                  queryResult.setQueryId(queryId);
                  Integer page = resultPages.get(queryId);
                  queryResult.setResultPage(page);
                  resultPages.put(queryId, page + 1);
                  sendResults(queryResult);
                }
              }
            });
          }
          return null;
        }
      });

      LOG.info("Starting the streaming context.");
      jssc.start();
      //Thread.sleep(3000);
      
      //LOG.info("Starting the streaming context. : " + jssc.ssc());
      //LOG.info("Starting the streaming context. : " + jssc.sparkContext());
      LOG.info("Starting the streaming context. : " + jssc.ssc().env());
      //LOG.info("Starting the streaming context. : " + jssc.sparkContext().env());
      //if(SparkEnv.getThreadLocal() == null || !SparkEnv.getThreadLocal().equals(jssc.ssc().env())){
      //  LOG.warn("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ Reseting spark env");
      //  SparkEnv.set(jssc.ssc().env());
      //}

      //SparkEnv.set(jssc.sc().env());
      jssc.awaitTermination();
      //jssc.awaitTermination((long) (duration*5));
      //jssc.stop();
      LOG.info("Streaming context stopped!");

      return "Streaming QID: " + queryId + " finished";
    } catch (Throwable t) {
      t.printStackTrace();
      return "ERROR: "+t.getMessage();
    }
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
