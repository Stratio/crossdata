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

import com.stratio.deep.context.DeepSparkContext;
import com.stratio.meta.common.actor.ActorResultListener;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StreamExecutor {

  private static HashMap<String, JavaStreamingContext> streamContexts = new HashMap<>();

  /**
   * Size of the streaming thread pool.
   */
  private final static int STREAMING_POOL_SIZE = 5;

  /**
   * Executor service for streaming queries.
   */
  private final static ExecutorService pool = Executors.newFixedThreadPool(STREAMING_POOL_SIZE);

  private final static Map<String, StreamingExecutionThread> streamingThreads = new HashMap<>();


  public StreamExecutor() {

  }

  public static Result execute(
      String queryId,
      MetaStatement stmt,
      IStratioStreamingAPI stratioStreamingAPI,
      DeepSparkContext deepSparkContext,
      EngineConfig config,
      ActorResultListener callbackActor,
      boolean isRoot) {
    if (stmt instanceof CreateTableStatement) {
      CreateTableStatement cts= (CreateTableStatement) stmt;
      String tableEphemeralName= cts.getEffectiveKeyspace()+"_"+cts.getTableName() ;
      List<ColumnNameType> columnList = new ArrayList<>();
      for (Map.Entry<String, String> column : cts.getColumns().entrySet()) {
        ColumnType type = StreamingUtils.metaToStreamingType(column.getValue());
        ColumnNameType streamColumn = new ColumnNameType(column.getKey(), type);
        columnList.add(streamColumn);
      }
      return MetaStream.createEphemeralTable(queryId, stratioStreamingAPI, tableEphemeralName,
                                             columnList, config);
    } else if (stmt instanceof SelectStatement){
      /*
      SelectStatement ss = (SelectStatement) stmt;
      JavaStreamingContext newContext = MetaStream.createSparkStreamingContext(config, 3);
      MetaStream.listenStream(queryId, stratioStreamingAPI, ss, config, newContext, callbackActor);
      QueryResult r = QueryResult.createQueryResult(new CassandraResultSet());
      r.setLastResultSet();
      return r;
      */
      StreamingExecutionThread set = new StreamingExecutionThread(queryId,
                                                                  SelectStatement.class.cast(stmt),
                                                                  config,
                                                                  stratioStreamingAPI,
                                                                  deepSparkContext,
                                                                  callbackActor,
                                                                  isRoot);
      streamingThreads.put(queryId, set);
      pool.execute(set);
      Result r = CommandResult.createCommandResult("Streaming query " + queryId + " running!");
      r.setQueryId(queryId);
      //callbackActor.processResults(r);
      return r;

    } else {
      return Result.createExecutionErrorResult("Not supported yet.");
    }
  }


  /**
   * Stop a streaming context removing it from the streamContexts map.
   * @param queryId The query identifier.
   */
  public static void stopContext(String queryId){
    streamContexts.get(queryId).stop();
    streamContexts.remove(queryId);
  }

  public static void addContext(String queryId, JavaStreamingContext jssc){
    streamContexts.put(queryId, jssc);
  }

}
