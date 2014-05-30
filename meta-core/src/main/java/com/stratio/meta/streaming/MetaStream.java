package com.stratio.meta.streaming;

import com.stratio.meta.common.result.CommandResult;
import com.stratio.meta.common.result.Result;
import com.stratio.streaming.api.IStratioStreamingAPI;
import com.stratio.streaming.api.StratioStreamingAPIFactory;
import com.stratio.streaming.commons.exceptions.StratioEngineStatusException;
import com.stratio.streaming.commons.messages.ColumnNameTypeValue;
import com.stratio.streaming.commons.messages.StratioStreamingMessage;
import com.stratio.streaming.commons.streams.StratioStream;
import com.stratio.streaming.messaging.ColumnNameType;

import java.util.List;

import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;

public class MetaStream {

  private static final IStratioStreamingAPI stratioStreamingAPI = StratioStreamingAPIFactory.create().initialize();

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
          System.out.println("Checking stream: "+stream.getStreamName());
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
      long start = System.currentTimeMillis();
      KafkaStream<String, StratioStreamingMessage> streams = stratioStreamingAPI.listenStream(streamName);
      StringBuilder sb = new StringBuilder();
      for (MessageAndMetadata stream: streams) {
        if((System.currentTimeMillis()-start) > (seconds*1000)){
          stopListenStream(streamName);
          return sb.toString();
        }
        StratioStreamingMessage theMessage = (StratioStreamingMessage)stream.message();
        for (ColumnNameTypeValue column: theMessage.getColumns()) {
          sb.append("Column: " + column.getColumn());
          sb.append(". Value: " + column.getValue());
          sb.append(". Type: " + column.getType());
          sb.append(System.lineSeparator());
        }
      }
      return sb.toString();
    } catch (Throwable t) {
      t.printStackTrace();
      return "ERROR";
    }
  }

  public static void stopListenStream(String streamName){
    try {
      stratioStreamingAPI.stopListenStream(streamName);
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

}


