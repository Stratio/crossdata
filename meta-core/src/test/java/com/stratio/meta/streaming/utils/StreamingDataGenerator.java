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

package com.stratio.meta.streaming.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.stratio.streaming.commons.constants.BUS;
import com.stratio.streaming.commons.constants.STREAM_OPERATIONS;
import com.stratio.streaming.commons.messages.ColumnNameTypeValue;
import com.stratio.streaming.commons.messages.StratioStreamingMessage;

public class StreamingDataGenerator {

  private static boolean unlimited = false;

  private static int rowsLimit;

  private static String sensorDataStream = "demo_temporal";

  public static void main(String[] args) throws InterruptedException {

    if (args.length > 2) {
      sensorDataStream = String.valueOf(args[2]);
      rowsLimit = Integer.valueOf(args[1]);
    } else if (args.length > 1) {
      rowsLimit = Integer.valueOf(args[1]);
    } else {
      unlimited = true;
    }

    int nameCounter = 0;

    while (unlimited || nameCounter < rowsLimit) {
      nameCounter++;

      Producer<String, String> producer = null;
      if (args != null && args.length > 0) {
        producer = new Producer<String, String>(createProducerConfig(args[0]));
      } else {
        throw new RuntimeException("Parameters are incorrect!");
      }

      int ageValue = (int) (Math.random() * 80);

      ExecutorService es = Executors.newFixedThreadPool(10);
      es.execute(new DataSender(producer, "name_" + nameCounter, ageValue));

      es.shutdown();

      if (nameCounter % 10 == 0 && nameCounter != rowsLimit) {
        System.out.println("Sleeping for 5 seconds...");
        Thread.sleep(5000);
      }
    }
  }

  private static ProducerConfig createProducerConfig(String brokerList) {
    Properties properties = new Properties();
    properties.put("serializer.class", "kafka.serializer.StringEncoder");
    properties.put("metadata.broker.list", brokerList);
    return new ProducerConfig(properties);
  }

  private static class DataSender implements Runnable {

    private final Producer<String, String> producer;
    private final String name;
    private final int age;

    public DataSender(Producer<String, String> producer, String name, int value) {
      super();
      this.producer = producer;
      this.name = name;
      this.age = value;
    }

    @Override
    public void run() {
      Gson gson = new Gson();

      for (StratioStreamingMessage message : generateStratioStreamingMessages(name, age)) {
        KeyedMessage<String, String> busMessage =
            new KeyedMessage<String, String>(BUS.TOPICS, STREAM_OPERATIONS.MANIPULATION.INSERT,
                gson.toJson(message));
        producer.send(busMessage);
      }

    }

    private List<StratioStreamingMessage> generateStratioStreamingMessages(String name, int value) {
      List<StratioStreamingMessage> result = new ArrayList<StratioStreamingMessage>();

      StratioStreamingMessage message = new StratioStreamingMessage();

      message.setOperation(STREAM_OPERATIONS.MANIPULATION.INSERT);
      message.setStreamName(sensorDataStream);
      message.setTimestamp(System.currentTimeMillis());
      message.setSession_id(String.valueOf(System.currentTimeMillis()));
      message.setRequest_id(String.valueOf(System.currentTimeMillis()));
      message.setRequest("dummy request");

      List<ColumnNameTypeValue> sensorData = Lists.newArrayList();
      sensorData.add(new ColumnNameTypeValue("name", null, name));
      sensorData.add(new ColumnNameTypeValue("age", null, value));
      // sensorData.add(new ColumnNameTypeValue("data", null, value));

      message.setColumns(sensorData);

      result.add(message);

      return result;
    }
  }
}
