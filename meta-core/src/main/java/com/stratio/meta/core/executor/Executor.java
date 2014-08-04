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

import com.datastax.driver.core.Session;
import com.stratio.deep.context.DeepSparkContext;
import com.stratio.meta.common.actor.ActorResultListener;
import com.stratio.meta.common.result.QueryStatus;
import com.stratio.meta.core.engine.Engine;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.utils.MetaQuery;
import com.stratio.meta.core.utils.Tree;
import com.stratio.streaming.api.IStratioStreamingAPI;

import org.apache.log4j.Logger;
import org.apache.spark.SparkEnv;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Executor {

  /**
   * Class logger.
   */
  private static final Logger LOG = Logger.getLogger(Executor.class.getName());

  /**
   * Cassandra datastax java driver session.
   */
  private final Session session;

  /**
   * Deep Spark context.
   */
  private final DeepSparkContext deepSparkContext;

  /**
   * Global configuration.
   */
  private final EngineConfig engineConfig;

  /**
   * Stratio Streaming API.
   */
  private final IStratioStreamingAPI stratioStreamingAPI;

  private final ExecutorService executorService;

  /**
   * Executor constructor.
   * @param session Cassandra datastax java driver session.
   * @param deepSparkContext Spark context.
   * @param engineConfig a {@link com.stratio.meta.core.engine.EngineConfig}
   */
  public Executor(Session session, IStratioStreamingAPI stratioStreamingAPI, DeepSparkContext deepSparkContext, EngineConfig engineConfig) {
    this.session = session;
    this.deepSparkContext = deepSparkContext;
    this.engineConfig = engineConfig;
    this.stratioStreamingAPI = stratioStreamingAPI;
    this.executorService = Executors.newFixedThreadPool(3);
  }

  /**
   * Executes a query.
   * @param metaQuery Query to execute embedded in a {@link com.stratio.meta.core.utils.MetaQuery}.
   * @return query executed.
   */
  public MetaQuery executeQuery(MetaQuery metaQuery, ActorResultListener callbackActor) {

    metaQuery.setStatus(QueryStatus.EXECUTED);

    // Get plan
    Tree plan = metaQuery.getPlan();

    LOG.debug("Execution plan: " + System.lineSeparator() + plan.toStringDownTop());

    if(plan.involvesStreaming() && plan.getChildren().size() > 0){
      LOG.info("Streaming with intermediate callback");

      // If the task involves streaming and it is a non-single statement (e.g., SELECT * FROM t WITH
      // WINDOW 2 s), create an execution trigger handler in such a way that the remainder of the
      // plan is executed each time new streaming data arrives.
      StreamingPlanTrigger st = new StreamingPlanTrigger(metaQuery, session, stratioStreamingAPI, deepSparkContext, engineConfig, callbackActor);
      executorService.execute(st);

    }else {

      // Execute plan
      metaQuery.setResult(
          plan.executeTreeDownTop(metaQuery.getQueryId(), session, stratioStreamingAPI,
                                  deepSparkContext, engineConfig, callbackActor));
    }

    return metaQuery;
  }
}
