package com.stratio.meta.streaming;

import java.text.SimpleDateFormat;
import java.util.Date;
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
  private List<String> results;

  public StreamingConsumer(String topic, String zookeeper, String groupId, List<String> results) {
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

    for(KafkaStream stream: streams){
      ConsumerIterator<byte[], byte[]> iter = stream.iterator();
      long lastIncome = System.currentTimeMillis();
      while (iter.hasNext()){
        if((System.currentTimeMillis() - lastIncome) > 1999){
          System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< "+getDate()+" >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
        String message = new String(iter.next().message());
        synchronized (results){
          results.add(message);
        }
        System.out.println("Message(" + getDate() + "): " + message);
        lastIncome = System.currentTimeMillis();
      }
      System.out.println("----------------------------------------------------------------------------------");
    }
  }

  private String getDate() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy HH:mm:ss.SSS");
    return sdf.format(new Date());
  }
}
