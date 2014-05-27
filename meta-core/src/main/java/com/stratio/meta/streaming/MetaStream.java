package com.stratio.meta.streaming;

import com.stratio.meta.common.result.QueryResult;
import com.stratio.streaming.api.IStratioStreamingAPI;
import com.stratio.streaming.api.StratioStreamingAPIFactory;
import com.stratio.streaming.commons.exceptions.StratioEngineStatusException;
import com.stratio.streaming.commons.streams.StratioStream;
import com.stratio.streaming.messaging.ColumnNameType;

import java.util.List;

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

  public static QueryResult createStream(String streamName, List<ColumnNameType> columnList){
      QueryResult result = QueryResult.createSuccessQueryResult();
      try {
          stratioStreamingAPI.createStream(streamName, columnList);
      } catch (Throwable t) {
          result = QueryResult.createFailQueryResult(streamName + " couldn't be created");
      }
      return result;
  }
}


