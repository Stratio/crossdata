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
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.utils.MetaQuery;
import com.stratio.streaming.api.IStratioStreamingAPI;

/**
 * Runnable class that triggers the computation of a Plan each time results in the root are
 * received.
 */
public class StreamingPlanTrigger implements Runnable, ActorResultListener{

  private final MetaQuery metaQuery;
  private final Session session;
  private final IStratioStreamingAPI stratioStreamingAPI;
  private final DeepSparkContext deepSparkContext;
  private final EngineConfig engineConfig;
  private final ActorResultListener callback;

  public StreamingPlanTrigger(MetaQuery metaQuery, Session session,
                              IStratioStreamingAPI stratioStreamingAPI,
                              DeepSparkContext deepSparkContext, EngineConfig engineConfig,
                              ActorResultListener callbackActor) {

    this.metaQuery = metaQuery;
    this.session = session;
    this.stratioStreamingAPI = stratioStreamingAPI;
    this.deepSparkContext = deepSparkContext;
    this.engineConfig = engineConfig;
    this.callback = callbackActor;
  }

  @Override
  public void run() {
    System.out.println();
    System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
    System.out.println();
    System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
    System.out.println();
    System.out.println();
    //Register this thread as the callback for the streaming results.
    metaQuery.setResult(
        metaQuery.getPlan().executeTreeDownTop(metaQuery.getQueryId(), session, stratioStreamingAPI,
                                deepSparkContext, engineConfig, this));

  }

  @Override
  public void processResults(Result result) {
    //Each time new results are received, trigger the execution of the childs.
    System.out.println("Results returned, triggering batch execution.");
  }
}
