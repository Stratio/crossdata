package com.stratio.meta.streaming;

import com.stratio.meta.common.result.CommandResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.statements.SelectStatement;
import com.stratio.streaming.api.IStratioStreamingAPI;
import com.stratio.streaming.commons.exceptions.StratioEngineStatusException;
import com.stratio.streaming.commons.messages.ColumnNameTypeValue;
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

public class MetaStream {

  /**
   * Class logger.
   */
  private static final Logger LOG = Logger.getLogger(MetaStream.class);

  private static StringBuilder sb = new StringBuilder();

  public static List<StratioStream> listStreams(IStratioStreamingAPI stratioStreamingAPI)  {
    List<StratioStream> streamsList = null;
    try {
      streamsList = stratioStreamingAPI.listStreams();
    } catch (StratioEngineStatusException e) {
      e.printStackTrace();
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

  public static Result createStream(IStratioStreamingAPI stratioStreamingAPI, String streamName, List<ColumnNameType> columnList){
    Result
        result = CommandResult.createCommandResult("Ephemeral table '" + streamName + "' created.");
    try {
      stratioStreamingAPI.createStream(streamName, columnList);
      stratioStreamingAPI.listenStream(streamName);
    } catch (Throwable t) {
      result = Result.createExecutionErrorResult(streamName + " couldn't be created"+System.lineSeparator()+t.getMessage());
    }
    return result;
  }

  public static void dropStream(IStratioStreamingAPI stratioStreamingAPI, String streamName) {
    try {
      stratioStreamingAPI.dropStream(streamName);
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  public static String listenStream(IStratioStreamingAPI stratioStreamingAPI, SelectStatement ss, EngineConfig config){
    final String streamName = ss.getEffectiveKeyspace()+"_"+ss.getTableName();
    try {
      JavaSparkContext sparkContext = new JavaSparkContext(config.getSparkMaster(), "MetaStreaming");
      LOG.info("Creating new JavaStreamingContext.");
      JavaStreamingContext jssc = null;
      while(jssc == null){
        try {
          jssc = new JavaStreamingContext(
              sparkContext.getConf().set("spark.driver.port", String.valueOf(StreamingUtils.findFreePort())),
              new Duration(config.getStreamingDuration()));
        } catch (Throwable t){
          jssc = null;
          LOG.debug("Cannot create Streaming Context. Trying it again.");
        }
      }

      String outgoing = streamName+"_"+String.valueOf(System.currentTimeMillis());
      // Create topic
      String query = ss.translateToSiddhi(stratioStreamingAPI, streamName, outgoing);
      System.out.println("TRACE: Query = "+query);
      final String queryId = stratioStreamingAPI.addQuery(streamName, query);
      LOG.info("queryId = " + queryId);
      stratioStreamingAPI.listenStream(outgoing);

      // Create stream reading outgoing Kafka topic
      Map<String, Integer> topics = new HashMap<>();
      //Map of (topic_name -> numPartitions) to consume. Each partition is consumed in its own thread
      topics.put(outgoing, 8);
      // jssc: JavaStreamingContext, zkQuorum: String, groupId: String, topics: Map<String, integer>
      final JavaPairDStream<String, String>
          dstream =
          KafkaUtils.createStream(jssc, config.getZookeeperServer(), config.getStreamingGroupId(), topics);

      final long duration = ss.getWindow().getDurationInMilliseconds();

      StreamingUtils.insertRandomData(stratioStreamingAPI, streamName, duration);

      Time timeWindow = new Time(duration);
      LOG.debug("Time = "+timeWindow.toString());
      JavaPairDStream<String, String>
          dstreamWindowed =
          dstream.window(new Duration(duration), new Duration(duration));

      dstreamWindowed.foreachRDD(new Function<JavaPairRDD<String, String>, Void>() {
        @Override
        public Void call(JavaPairRDD<String, String> stringStringJavaPairRDD) throws Exception {
          final long totalCount = stringStringJavaPairRDD.count();
          LOG.info(queryId+": Count=" + totalCount);
          if(totalCount > 0){
            sb = new StringBuilder();
            stringStringJavaPairRDD.values().foreach(new VoidFunction<String>() {
              @Override
              public void call(String s) throws Exception {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> myMap = objectMapper.readValue(s, HashMap.class);
                ArrayList columns = (ArrayList) myMap.get("columns");
                String cols = Arrays.toString(columns.toArray());
                LOG.debug("Columns = " + cols);
                sb.append(cols).append(System.lineSeparator());
              }
            });
          }
          return null;
        }
      });

      LOG.info("Starting the streaming context.");
      jssc.start();
      jssc.awaitTermination((long) (duration*1.4));

      return sb.toString();
    } catch (Throwable t) {
      t.printStackTrace();
      return "ERROR: "+t.getMessage();
    }
  }

  public static void stopListenStream(IStratioStreamingAPI stratioStreamingAPI, String streamName){
    try {
      stratioStreamingAPI.stopListenStream(streamName);
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  public static List<String> getColumnNames(IStratioStreamingAPI stratioStreamingAPI, String tablename) {
    List<String> colNames = new ArrayList<>();
    try {
      List<ColumnNameTypeValue> cols = stratioStreamingAPI.columnsFromStream(tablename);
      for(ColumnNameTypeValue ctp: cols){
        colNames.add(ctp.getColumn().toLowerCase());
      }
    } catch (Throwable t){
      t.printStackTrace();
    }
    return colNames;
  }

}
