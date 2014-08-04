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
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.statements.SelectStatement;
import com.stratio.meta.streaming.MetaStream;
import com.stratio.streaming.api.IStratioStreamingAPI;

import org.apache.log4j.Logger;

public class StreamingExecutionThread implements Runnable {

  /**
   * Class logger.
   */
  private static final Logger LOG = Logger.getLogger(StreamingExecutionThread.class);

  private final SelectStatement statement;

  private final EngineConfig config;

  private final String queryId;

  private final IStratioStreamingAPI stratioStreamingAPI;

  private final ActorResultListener callbackActor;

  private final DeepSparkContext deepSparkContext;

  private final boolean isRoot;

  public StreamingExecutionThread(
      String queryId,
      SelectStatement ss,
      EngineConfig config,
      IStratioStreamingAPI stratioStreamingAPI,
      DeepSparkContext deepSparkContext,
      ActorResultListener callbackActor,
      boolean isRoot){
    statement = ss;
    this.config = config;
    this.queryId = queryId;
    this.stratioStreamingAPI = stratioStreamingAPI;
    this.deepSparkContext = deepSparkContext;
    this.callbackActor = callbackActor;
    this.isRoot = isRoot;
  }

  @Override
  public void run() {
    LOG.debug("Running streaming Thread for " + statement.toString());
    MetaStream.startQuery(queryId, stratioStreamingAPI, statement, config, callbackActor, deepSparkContext, isRoot);
    /*
    QueryResult r = QueryResult.createQueryResult(new CassandraResultSet());
    r.setLastResultSet();
    r.setQueryId(queryId);
    callbackActor.processResults(r);
    */
    LOG.debug("Streaming query running");
  }

}
