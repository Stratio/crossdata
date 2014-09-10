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

package com.stratio.meta.core.executor;

import com.stratio.deep.context.DeepSparkContext;
import com.stratio.meta.common.actor.ActorResultListener;
import com.stratio.meta.common.result.CommandResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta2.core.engine.EngineConfig;
import com.stratio.meta.streaming.MetaStream;
import com.stratio.meta.streaming.StreamingUtils;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.core.statements.CreateTableStatement;
import com.stratio.meta2.core.statements.MetaStatement;
import com.stratio.meta2.core.statements.SelectStatement;
import com.stratio.streaming.api.IStratioStreamingAPI;
import com.stratio.streaming.commons.constants.ColumnType;
import com.stratio.streaming.messaging.ColumnNameType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StreamExecutor {

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
      String tableEphemeralName= cts.getEffectiveCatalog()+"_"+cts.getTableName() ;
      List<ColumnNameType> columnList = new ArrayList<>();
      for (Map.Entry<ColumnName, String> column : cts.getColumnsWithTypes().entrySet()) {
        ColumnType type = StreamingUtils.metaToStreamingType(column.getValue());
        ColumnNameType streamColumn = new ColumnNameType(column.getKey().getName(), type);
        columnList.add(streamColumn);
      }
      return MetaStream.createEphemeralTable(queryId, stratioStreamingAPI, tableEphemeralName,
                                             columnList, config);
    } else if (stmt instanceof SelectStatement){
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
      return r;

    } else {
      return Result.createExecutionErrorResult("Not supported yet.");
    }
  }

}
