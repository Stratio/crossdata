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

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
      long lastIncome = System.currentTimeMillis();
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
        lastIncome = System.currentTimeMillis();
      }

    }
  }

  private String getDate() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy HH:mm:ss.SSS");
    return sdf.format(new Date());
  }
}
