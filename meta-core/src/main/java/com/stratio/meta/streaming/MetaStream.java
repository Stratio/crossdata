package com.stratio.meta.streaming;

import com.stratio.meta.common.data.CassandraResultSet;
import com.stratio.meta.common.data.Cell;
import com.stratio.meta.common.data.ColumnDefinition;
import com.stratio.meta.common.data.Row;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.streaming.api.IStratioStreamingAPI;
import com.stratio.streaming.api.StratioStreamingAPIFactory;
import com.stratio.streaming.commons.exceptions.StratioEngineStatusException;
import com.stratio.streaming.commons.streams.StratioStream;
import com.stratio.streaming.messaging.ColumnNameType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    CassandraResultSet rs = new CassandraResultSet();

    Row metaRow = new Row();

    Cell metaCell = new Cell("Ephemeral table '"+streamName+"' created.");

    Map colDefs = new HashMap<String, ColumnDefinition>();
    colDefs.put("RESULT", new ColumnDefinition(String.class));

    rs.setColumnDefinitions(colDefs);

    metaRow.addCell("RESULT", metaCell);
    rs.add(metaRow);
    QueryResult result = QueryResult.createSuccessQueryResult(rs);
    try {
      stratioStreamingAPI.createStream(streamName, columnList);
    } catch (Throwable t) {
      result = QueryResult.createFailQueryResult(streamName + " couldn't be created");
    }
    return result;
  }
}


