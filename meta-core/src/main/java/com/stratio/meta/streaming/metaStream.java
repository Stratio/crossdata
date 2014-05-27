package com.stratio.meta.streaming;

/**
 * Created by aalcocer on 5/27/14. To generate unit test of proxy actor
 */

import com.stratio.streaming.api.IStratioStreamingAPI;
import com.stratio.streaming.api.StratioStreamingAPIFactory;
import com.stratio.streaming.commons.exceptions.StratioEngineOperationException;
import com.stratio.streaming.commons.exceptions.StratioEngineStatusException;
import com.stratio.streaming.commons.messages.ColumnNameTypeValue;
import com.stratio.streaming.commons.streams.StratioStream;
import com.stratio.streaming.messaging.ColumnNameType;

import java.util.List;

public class metaStream {



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
      if (stream.getStreamName().equalsIgnoreCase(ephimeralTable)){
        return true;
      }
    }
    return false;
  }

  public static void createStream(String streamName,List<ColumnNameType> columnList)
      throws StratioEngineOperationException {

      stratioStreamingAPI.createStream(streamName, columnList);

  }
  }


