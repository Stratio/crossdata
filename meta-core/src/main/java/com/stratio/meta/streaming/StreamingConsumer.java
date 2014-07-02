package com.stratio.meta.streaming;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

public class StreamingConsumer extends Thread {

  private ConsumerConnector consumer;
  private String topic;
  private List<KafkaStream<byte[], byte[]>> streams;
  private List<Object> results;

  public StreamingConsumer(String topic, String zookeeper, String groupId, List<Object> results) {
    this.results = results;
    Properties props = new Properties();
    props.put("zookeeper.connect", zookeeper);
    props.put("group.id", groupId);
    props.put("zookeeper.session.timeout.ms", "400");
    props.put("zookeeper.sync.time.ms", "200");
    props.put("auto.commit.interval.ms", "1000");
    consumer = kafka.consumer.Consumer.createJavaConsumerConnector(
        new ConsumerConfig(props));
    this.topic = topic;
    Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
    topicCountMap.put(topic, new Integer(1));
    Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
    streams = consumerMap.get(topic);
  }

  public void run() {

    ObjectMapper objectMapper = new ObjectMapper();

    for(KafkaStream stream: streams){
      ConsumerIterator<byte[], byte[]> iter = stream.iterator();
      while (iter.hasNext()){
        String message = new String(iter.next().message());

        // Get columns fields from Json
        Map<String, Object> myMap = null;
        try {
          myMap = objectMapper.readValue(message, HashMap.class);
        } catch (IOException e) {
          e.printStackTrace();
        }
        ArrayList columns = (ArrayList) myMap.get("columns");

        synchronized (results){
          results.add(columns);
        }
      }

    }
  }
}
