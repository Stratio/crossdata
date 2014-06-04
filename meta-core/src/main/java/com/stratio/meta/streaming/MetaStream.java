package com.stratio.meta.streaming;

import com.stratio.deep.context.DeepSparkContext;
import com.stratio.meta.common.result.CommandResult;
import com.stratio.meta.common.result.Result;
import com.stratio.streaming.api.IStratioStreamingAPI;
import com.stratio.streaming.api.StratioStreamingAPIFactory;
import com.stratio.streaming.commons.exceptions.StratioEngineStatusException;
import com.stratio.streaming.commons.messages.ColumnNameTypeValue;
import com.stratio.streaming.commons.messages.StratioStreamingMessage;
import com.stratio.streaming.commons.streams.StratioStream;
import com.stratio.streaming.messaging.ColumnNameType;
import com.stratio.streaming.messaging.ColumnNameValue;

import org.apache.log4j.Logger;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;

public class MetaStream {

  /**
   * Class logger.
   */
  private static final Logger LOG = Logger.getLogger(MetaStream.class);

  private static IStratioStreamingAPI stratioStreamingAPI = null;

  private static JavaStreamingContext jssc = null;

  static {
    try {
      stratioStreamingAPI = StratioStreamingAPIFactory.create().initialize();
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  public static void setDeepContext(DeepSparkContext deepContext) {
    if(jssc == null){
      jssc = new JavaStreamingContext(deepContext, new Duration(1000));
    }
  }

  public static IStratioStreamingAPI getStratioStreamingAPI() {
    return stratioStreamingAPI;
  }

  public static List<StratioStream> listStreams ()  {
    List<StratioStream> streamsList = null;
    try {
      streamsList = stratioStreamingAPI.listStreams();
    } catch (StratioEngineStatusException e) {
      e.printStackTrace();
    }
    return streamsList;
    }

  public static boolean checkstream(String ephimeralTable){
      for (StratioStream stream: listStreams()) {
          if (stream.getStreamName().equalsIgnoreCase(ephimeralTable)){
              return true;
          }
      }
      return false;
  }

  public static Result createStream(String streamName, List<ColumnNameType> columnList){
    CommandResult result = CommandResult.createSuccessCommandResult("Ephemeral table '" + streamName + "' created.");
    try {
      stratioStreamingAPI.createStream(streamName, columnList);
    } catch (Throwable t) {
      result = CommandResult.createFailCommandResult(streamName + " couldn't be created");
    }
    return result;
  }

  public static void dropStream(String streamName) {
    try {
      stratioStreamingAPI.dropStream(streamName);
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  public static String listenStream(String streamName, int seconds){
    try {
      //////////////////////////////////////////////////////////////
      String query = "from "+streamName+" select name, age, rating insert into pof";
      String queryId = stratioStreamingAPI.addQuery(streamName, query);
      System.out.println("queryId = "+queryId);
      stratioStreamingAPI.listenStream("pof");
      //////////////////////////////////////////////////////////////
      long start = System.currentTimeMillis();
      insertRandomData(streamName);
      KafkaStream<String, StratioStreamingMessage> streams = stratioStreamingAPI.listenStream(streamName);
      StringBuilder sb = new StringBuilder(System.lineSeparator());
      insertRandomData(streamName);
      for (MessageAndMetadata stream: streams) {
        long elapsed_time = (System.currentTimeMillis() - start);
        long limit_time = (seconds * 1000);
        if(elapsed_time > limit_time){
          stopListenStream(streamName);
          break;
        }
        StratioStreamingMessage theMessage = (StratioStreamingMessage)stream.message();
        sb.append("----------------------------------------------------------").append(System.lineSeparator());
        for (ColumnNameTypeValue column: theMessage.getColumns()) {
          sb.append("Column: " + column.getColumn());
          sb.append(" | Value: " + column.getValue());
          sb.append(" | Type: " + column.getType());
          sb.append(System.lineSeparator());
        }
        sb.append("----------------------------------------------------------").append(System.lineSeparator());
        insertRandomData(streamName);
      }
      /*
      SimpleConsumer
          simpleConsumer = new SimpleConsumer("ingestion.stratio.com", 9092, 100000, 64 * 1024, "stratio");

      kafka.api.FetchRequest req = new FetchRequestBuilder()
          .clientId("stratio")
          .addFetch("pof", 0, 0, 100)
          .build();
      kafka.javaapi.FetchResponse fetchResponse = simpleConsumer.fetch(req);

      ByteBufferMessageSet response = fetchResponse.messageSet("pof", 0);

      System.out.println("TRACE: "+new String(response.getBuffer().array(), "UTF-8"));
      */
      ////////////////////////////////////////////////////////////////////////////////////
      /*
      Properties props = new Properties();
      props.put("zookeeper.connect", "ingestion.stratio.com");
      props.put("group.id", "stratio");
      props.put("zookeeper.session.timeout.ms", "400");
      props.put("zookeeper.sync.time.ms", "200");
      props.put("auto.commit.interval.ms", "1000");

      ConsumerConnector consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));

      //consumer.createMessageStreams(new HashMap<String, Integer>());
      List<KafkaStream<byte[], byte[]>>
          result =
          consumer.createMessageStreamsByFilter(new Whitelist("pof"));
      System.out.println("result.size = "+result.size());
      for(KafkaStream<byte[], byte[]> kafkaStream: result){
        ConsumerIterator<byte[], byte[]> iter = kafkaStream.iterator();
        while(iter.hasNext()){
          MessageAndMetadata<byte[], byte[]> row = iter.next();
          System.out.println("TRACE: "+new String(row.message()));
        }

      }*/

      /**
       * Create an input stream that pulls messages form a Kafka Broker.
       * Storage level of the data will be the default StorageLevel.MEMORY_AND_DISK_SER_2.
       * param jssc      JavaStreamingContext object
       * param zkQuorum  Zookeeper quorum (hostname:port,hostname:port,..)
       * param groupId   The group id for this consumer
       * param topics    Map of (topic_name -> numPartitions) to consume. Each partition is consumed
       *                  in its own thread
       *
      def createStream(
          jssc: JavaStreamingContext,
          zkQuorum: String,
          groupId: String,
          topics: JMap[String, JInt]
      ): JavaPairDStream[String, String] = {
        implicit val cmt: ClassTag[String] =
            implicitly[ClassTag[AnyRef]].asInstanceOf[ClassTag[String]]
        createStream(jssc.ssc, zkQuorum, groupId, Map(topics.mapValues(_.intValue()).toSeq: _*))
      }
      */
      Map<String, Integer> topics = new HashMap<>();
      topics.put("pof", 100);
      if(jssc == null){
        System.out.println("TRACE: jssc is NULL");
      }
      if(topics == null){
        System.out.println("TRACE: topics is NULL");
      }
      JavaPairDStream<String, String>
          dstream =
          KafkaUtils.createStream(jssc, "ingestion.stratio.com", "stratio", topics);
      System.out.println("TRACE: dstream.class="+dstream.getClass());
      ///////////////////////////////////////////////////////////////////////////////////
      return sb.toString();
    } catch (Throwable t) {
      t.printStackTrace();
      return "ERROR";
    }
  }

  private static void insertRandomData(String streamName) {
    double randomDouble = Math.random()*100;
    int randomInt = (int) (randomDouble*Math.random()*2);
    StringBuilder sb = new StringBuilder(String.valueOf(randomDouble));
    sb.append(randomInt);
    String str = sb.toString().replace('0', 'o').replace('1', 'i').replace('2', 'u').replace('3', 'e').replace('4', 'a').
        replace('5', 'b').replace('6', 'c').replace('7', 'd').replace('8', 'f').replace('9', 'g').replace('.', 'm');
    ColumnNameValue firstColumnValue = new ColumnNameValue("name", str);
    ColumnNameValue secondColumnValue = new ColumnNameValue("age", new Integer(randomInt));
    ColumnNameValue thirdColumnValue = new ColumnNameValue("rating", new Double(randomDouble));
    ColumnNameValue fourthColumnValue = new ColumnNameValue("member", new Boolean((randomInt % 2) == 0));
    List<ColumnNameValue> streamData = Arrays
        .asList(firstColumnValue, secondColumnValue, thirdColumnValue, fourthColumnValue);
    try {
      stratioStreamingAPI.insertData(streamName, streamData);
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  public static void stopListenStream(String streamName){
    try {
      stratioStreamingAPI.stopListenStream(streamName);
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  public static List<String> getColumnNames(String tablename) {
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

