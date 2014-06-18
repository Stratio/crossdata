package com.stratio.meta.streaming;

import com.stratio.meta.common.actor.ActorResultListener;
import com.stratio.meta.common.data.CassandraResultSet;
import com.stratio.meta.common.data.Cell;
import com.stratio.meta.common.data.Row;
import com.stratio.meta.common.metadata.structures.ColumnMetadata;
import com.stratio.meta.common.result.CommandResult;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.executor.StreamExecutor;
import com.stratio.meta.core.statements.SelectStatement;
import com.stratio.streaming.api.IStratioStreamingAPI;
import com.stratio.streaming.commons.constants.StreamAction;
import com.stratio.streaming.commons.exceptions.StratioAPIGenericException;
import com.stratio.streaming.commons.exceptions.StratioEngineStatusException;
import com.stratio.streaming.commons.streams.StratioStream;
import com.stratio.streaming.messaging.ColumnNameType;

import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
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

import akka.actor.ActorRef;

public class MetaStream {

  /**
   * Class logger.
   */
  private static final Logger LOG = Logger.getLogger(MetaStream.class);

  private static Map<String, ActorResultListener> callbackActors = new HashMap<>();

  private static int numberPages = 0;


  public static List<StratioStream> listStreams(IStratioStreamingAPI stratioStreamingAPI)  {
    List<StratioStream> streamsList = null;
    try {
      streamsList = stratioStreamingAPI.listStreams();
    } catch (Throwable t) {
      t.printStackTrace();
    }
    return streamsList;
  }

  public static boolean checkstream(IStratioStreamingAPI stratioStreamingAPI, String ephemeralTable){
    for (StratioStream stream: listStreams(stratioStreamingAPI)) {
      if (stream.getStreamName().equalsIgnoreCase(ephemeralTable)){
        return true;
      }
    }
    return false;
  }

  public static Result createStream(String queryId, IStratioStreamingAPI stratioStreamingAPI, String streamName, List<ColumnNameType> columnList, EngineConfig config){
    Result
        result = CommandResult.createCommandResult("Ephemeral table '" + streamName + "' created.");
    try {
      stratioStreamingAPI.createStream(streamName, columnList);
      //Listen so it is created.
      stratioStreamingAPI.listenStream(streamName);

    } catch (Throwable t) {
      result = Result.createExecutionErrorResult(streamName + " couldn't be created"+System.lineSeparator()+t.getMessage());
    }
    result.setQueryId(queryId);
    return result;
  }

  public static void dropStream(IStratioStreamingAPI stratioStreamingAPI, String streamName) {
    try {
      stratioStreamingAPI.dropStream(streamName);
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  public static JavaStreamingContext createSparkStreamingContext(EngineConfig config){
    JavaSparkContext sparkContext = new JavaSparkContext(config.getSparkMaster(), "MetaStreaming");
    LOG.info("Creating new JavaStreamingContext.");
    JavaStreamingContext jssc = null;
    while(jssc == null){
      try {
        jssc = new JavaStreamingContext(
            sparkContext.getConf()
                            .set("spark.driver.port", String.valueOf(StreamingUtils.findFreePort()))
                            .set("spark.ui.port", String.valueOf(StreamingUtils.findFreePort())),
            new Duration(config.getStreamingDuration()));
      } catch (Throwable t){
        jssc = null;
        LOG.debug("Cannot create Streaming Context. Trying it again.", t);
      }
    }
    return jssc;
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
      final String outgoing = streamName+"_"+ UUID.randomUUID().toString().replace("-", "_");

      // Create topic
      String query = ss.translateToSiddhi(stratioStreamingAPI, streamName, outgoing);
      final String streamingQueryId = stratioStreamingAPI.addQuery(streamName, query);
      StreamExecutor.addContext(streamingQueryId, jssc);
      LOG.info("queryId = " + streamingQueryId);
      stratioStreamingAPI.listenStream(outgoing);

      //Insert dumb element in topic
      StreamingUtils.insertRandomData(stratioStreamingAPI, outgoing);

      boolean outgoingStreamCreated = false;
      while(!outgoingStreamCreated){
        Thread.sleep(1000);
        List<StratioStream> createdStreams = stratioStreamingAPI.listStreams();
        for(StratioStream stratioStream: createdStreams){
          for(StreamAction streamAction: stratioStream.getActiveActions()){
            LOG.debug("streamAction(" + stratioStream.getStreamName() + ") = " + streamAction.toString());
          }
          if(stratioStream.getStreamName().equalsIgnoreCase(outgoing)){
            LOG.debug(stratioStream.getStreamName()+" found.");
            outgoingStreamCreated = true;
          }
        }
      }

      // Create stream reading outgoing Kafka topic
      Map<String, Integer> topics = new HashMap<>();
      //Map of (topic_name -> numPartitions) to consume. Each partition is consumed in its own thread
      topics.put(outgoing, 1);
      // jssc: JavaStreamingContext, zkQuorum: String, groupId: String, topics: Map<String, integer>
      final JavaPairDStream<String, String>
          dstream =
          KafkaUtils.createStream(jssc, config.getZookeeperServer(), config.getStreamingGroupId(), topics);

      final long duration = ss.getWindow().getDurationInMilliseconds();

      //StreamingUtils.insertRandomData(stratioStreamingAPI, streamName, duration, 4);
      StreamingUtils.insertRandomData(stratioStreamingAPI, streamName, duration, 10, 4);

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
                  queryResult.setResultPage(numberPages);
                  numberPages++;
                  sendResults(queryResult);
                }
              }
            });
            //Send resultset to client
          }
          return null;
        }
      });

      LOG.info("Starting the streaming context.");
      jssc.start();
      //jssc.awaitTermination((long) (duration*1.5));
      jssc.awaitTermination((long) (duration*5));
      jssc.stop();

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

  public static void stopListenStream(IStratioStreamingAPI stratioStreamingAPI, String streamName){
    try {
      stratioStreamingAPI.stopListenStream(streamName);
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

}
