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
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.MetaQuery;
import com.stratio.meta.core.utils.Tree;
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
  private final MetadataManager metadataManager;

  public StreamingPlanTrigger(MetaQuery metaQuery, Session session,
                              IStratioStreamingAPI stratioStreamingAPI,
                              DeepSparkContext deepSparkContext, MetadataManager metadataManager, EngineConfig engineConfig,
                              ActorResultListener callbackActor) {

    this.metaQuery = metaQuery;
    this.session = session;
    this.stratioStreamingAPI = stratioStreamingAPI;
    this.deepSparkContext = deepSparkContext;
    this.engineConfig = engineConfig;
    this.callback = callbackActor;
    this.metadataManager = metadataManager;
  }

  @Override
  public void run() {
    //Register this thread as the callback for the streaming results.
    Tree t = new Tree(metaQuery.getPlan().getNode());
    t.setParent(new Tree());
    t.executeTreeDownTop(metaQuery.getQueryId(), session, stratioStreamingAPI,
                                                      deepSparkContext, metadataManager, engineConfig, this);
  }

  @Override
  public void processResults(Result result) {
    //Each time new results are received, trigger the execution of the childs.
    //Get the children -> Select from deep -> Join on Deep.
    Tree t = metaQuery.getPlan().getChildren().get(0);
    Result r = t.executeTreeDownTop(metaQuery.getQueryId(), session, stratioStreamingAPI,
                                    deepSparkContext, metadataManager, engineConfig, null, result);
    r.setQueryId(metaQuery.getQueryId());
    callback.processResults(r);
  }
}
