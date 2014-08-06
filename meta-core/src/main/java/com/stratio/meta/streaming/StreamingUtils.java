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

import com.stratio.streaming.api.IStratioStreamingAPI;
import com.stratio.streaming.commons.constants.ColumnType;
import com.stratio.streaming.messaging.ColumnNameValue;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.List;

public class StreamingUtils {

  /**
   * Class logger.
   */
  private static final Logger LOG = Logger.getLogger(StreamingUtils.class);

  private static int numItem = 0;

  public static ColumnType metaToStreamingType(String value) {
    ColumnType type = null;
    if (value.equalsIgnoreCase("varchar") || value.equalsIgnoreCase("text") || value.equalsIgnoreCase("uuid")
        || value.equalsIgnoreCase("timestamp") || value.equalsIgnoreCase("timeuuid")){
      type=ColumnType.STRING;
    } else if (value.equalsIgnoreCase("boolean")){
      type=ColumnType.BOOLEAN;
    } else if (value.equalsIgnoreCase("double")){
      type=ColumnType.DOUBLE;
    } else if (value.equalsIgnoreCase("float")){
      type=ColumnType.FLOAT;
    } else if (value.equalsIgnoreCase("integer") || value.equalsIgnoreCase("int")){
      type=ColumnType.INTEGER;
    } else if (value.equalsIgnoreCase("long") || value.equalsIgnoreCase("counter")){
      type=ColumnType.LONG;
    } else {
      type = ColumnType.valueOf(value);
    }
    return type;
  }

  public static void insertRandomData(final IStratioStreamingAPI stratioStreamingAPI,
                                      final String streamName,
                                      final long duration,
                                      final int nRows,
                                      final int numRepetitions) {
    // Insert random data
    Thread randomThread = new Thread(){
      public void run(){
        try {
          Thread.sleep((long) (duration/3));
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        for(int i=0; i<numRepetitions; i++){
          LOG.debug("Inserting data");
          for(int j=0; j<nRows; j++){
            insertRandomData(stratioStreamingAPI, streamName);
          }
          LOG.debug("Data inserted");
          try {
            Thread.sleep(duration);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    };
    randomThread.start();
  }

  public static void insertRandomData(IStratioStreamingAPI stratioStreamingAPI, String streamName) {
    double randomDouble = Math.random()*100;
    int randomInt = (int) (randomDouble*Math.random()*2);
    StringBuilder sb = new StringBuilder(String.valueOf(randomDouble));
    sb.append(randomInt);
    String str = convertRandomNumberToString(sb.toString()) + "___" + numItem;
    numItem++;
    if(numItem == 20){
      numItem = 0;
    }
    //ColumnNameValue firstColumnValue = new ColumnNameValue("name", str);
    ColumnNameValue firstColumnValue = new ColumnNameValue("name", "name_"+numItem);
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

  public static String convertRandomNumberToString(String str){
    return str.replace('0', 'o').replace('1', 'i').replace('2', 'u').replace('3', 'e').replace('4', 'a').
        replace('5', 'b').replace('6', 'c').replace('7', 'd').replace('8', 'f').replace('9', 'g').replace('.', 'm');
  }

  public static int findFreePort() {
    ServerSocket socket = null;
    try {
      socket = new ServerSocket(0);
      socket.setReuseAddress(true);
      int port = socket.getLocalPort();
      socket.close();
      return port;
    } catch (IOException e) {
      throw new IllegalStateException("Could not find a free port.");
    }
  }

  public static com.stratio.meta.common.metadata.structures.ColumnType streamingToMetaType(String type) {
    com.stratio.meta.common.metadata.structures.ColumnType metaType = com.stratio.meta.common.metadata.structures.ColumnType.VARCHAR;
    if(type.equalsIgnoreCase(ColumnType.BOOLEAN.getValue())){
      metaType = com.stratio.meta.common.metadata.structures.ColumnType.BOOLEAN;
    } else if(type.equalsIgnoreCase(ColumnType.DOUBLE.getValue())){
      metaType = com.stratio.meta.common.metadata.structures.ColumnType.DOUBLE;
    } else if(type.equalsIgnoreCase(ColumnType.FLOAT.getValue())){
      metaType = com.stratio.meta.common.metadata.structures.ColumnType.FLOAT;
    } else if(type.equalsIgnoreCase(ColumnType.INTEGER.getValue())){
      metaType = com.stratio.meta.common.metadata.structures.ColumnType.INT;
    } else if(type.equalsIgnoreCase(ColumnType.LONG.getValue())){
      metaType = com.stratio.meta.common.metadata.structures.ColumnType.BIGINT;
    }
    return metaType;
  }

  public static Object convertStreamingToJava(String value, String streamingType){
    Object result = value;
    if(streamingType.equalsIgnoreCase(ColumnType.BOOLEAN.getValue())){
      result = Boolean.valueOf(value);
    } else if(streamingType.equalsIgnoreCase(ColumnType.DOUBLE.getValue())){
      result = Double.valueOf(value);
    } else if(streamingType.equalsIgnoreCase(ColumnType.FLOAT.getValue())){
      result = Float.valueOf(value);
    } else if(streamingType.equalsIgnoreCase(ColumnType.INTEGER.getValue())){
      result = Integer.valueOf(value);
    } else if(streamingType.equalsIgnoreCase(ColumnType.LONG.getValue())){
      result = Long.valueOf(value);
    }
    return result;
  }
}
