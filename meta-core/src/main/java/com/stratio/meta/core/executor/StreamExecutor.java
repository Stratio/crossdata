/*
 * Stratio Meta
<<<<<<< HEAD
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.core.executor;

import com.stratio.meta.common.result.CommandResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.statements.CreateTableStatement;
import com.stratio.meta.core.statements.MetaStatement;
import com.stratio.meta.core.statements.SelectStatement;
import com.stratio.meta.streaming.MetaStream;
import com.stratio.meta.streaming.StreamingUtils;
import com.stratio.streaming.api.IStratioStreamingAPI;
import com.stratio.streaming.commons.constants.ColumnType;
import com.stratio.streaming.messaging.ColumnNameType;

import org.apache.spark.streaming.api.java.JavaStreamingContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StreamExecutor {

  private static HashMap<String, JavaStreamingContext> streamContexts = new HashMap<>();

  public StreamExecutor() {

  }

  public static Result execute(MetaStatement stmt, IStratioStreamingAPI stratioStreamingAPI, EngineConfig config) {
    if (stmt instanceof CreateTableStatement) {
      CreateTableStatement cts= (CreateTableStatement) stmt;
      String tableEphemeralName= cts.getEffectiveKeyspace()+"_"+cts.getTableName() ;
      List<ColumnNameType> columnList = new ArrayList<>();
      for (Map.Entry<String, String> column : cts.getColumns().entrySet()) {
        ColumnType type = StreamingUtils.metaToStreamingType(column.getValue());
        ColumnNameType streamColumn = new ColumnNameType(column.getKey(), type);
        columnList.add(streamColumn);
      }
      return MetaStream.createStream(stratioStreamingAPI, tableEphemeralName, columnList, config);
    } else if (stmt instanceof SelectStatement){
      SelectStatement ss = (SelectStatement) stmt;
      JavaStreamingContext newContext = MetaStream.createSparkStreamingContext(config);
      String resultStream = MetaStream.listenStream(stratioStreamingAPI, ss, config, newContext);
      return CommandResult.createCommandResult(resultStream);
    } else {
      return Result.createExecutionErrorResult("Not supported yet.");
    }
  }

  public static void stopContext(String queryId){
    streamContexts.get(queryId).stop(false);
    streamContexts.remove(queryId);
  }

  public static void addContext(String queryId, JavaStreamingContext jssc){
    streamContexts.put(queryId, jssc);
  }

}
