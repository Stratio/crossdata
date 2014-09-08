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
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.utils.MetaQuery;
import com.stratio.meta.core.utils.Tree;
import com.stratio.streaming.api.IStratioStreamingAPI;

/**
 * Runnable class that triggers the computation of a Plan each time results in the root are
 * received.
 */
public class StreamingPlanTrigger implements Runnable, ActorResultListener{

  private final MetaQuery metaQuery;
  //private final Session session;
  private final IStratioStreamingAPI stratioStreamingAPI;
  private final DeepSparkContext deepSparkContext;
  private final EngineConfig engineConfig;
  private final ActorResultListener callback;

  public StreamingPlanTrigger(MetaQuery metaQuery,
                              IStratioStreamingAPI stratioStreamingAPI,
                              DeepSparkContext deepSparkContext, EngineConfig engineConfig,
                              ActorResultListener callbackActor) {

    this.metaQuery = metaQuery;
    this.stratioStreamingAPI = stratioStreamingAPI;
    this.deepSparkContext = deepSparkContext;
    this.engineConfig = engineConfig;
    this.callback = callbackActor;
  }

  @Override
  public void run() {
    //Register this thread as the callback for the streaming results.
    Tree t = new Tree(metaQuery.getPlan().getNode());
    t.setParent(new Tree());
    t.executeTreeDownTop(metaQuery.getQueryId(), stratioStreamingAPI,
                                                      deepSparkContext, engineConfig, this);
  }

  @Override
  public void processResults(Result result) {
    //Each time new results are received, trigger the execution of the childs.
    //Get the children -> Select from deep -> Join on Deep.
    Tree t = metaQuery.getPlan().getChildren().get(0);
    Result r = t.executeTreeDownTop(metaQuery.getQueryId(), stratioStreamingAPI,
                                    deepSparkContext, engineConfig, null, result);
    r.setQueryId(metaQuery.getQueryId());
    callback.processResults(r);
  }
}
