/*
 * Stratio Meta
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

  private final IStratioStreamingAPI stratioStreamingAPI;

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

    // Execute plan
    metaQuery.setResult(
        plan.executeTreeDownTop(metaQuery.getQueryId(), session, stratioStreamingAPI, deepSparkContext, engineConfig, callbackActor));

    return metaQuery;
  }
}
