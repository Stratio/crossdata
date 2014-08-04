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
 */package com.stratio.meta.streaming;

import com.stratio.deep.context.DeepSparkContext;
import com.stratio.meta.common.actor.ActorResultListener;

import java.util.List;

public class StreamListener extends Thread {

  private List<Object> results;
  private DeepSparkContext dsc;
  private ActorResultListener callBackActor;
  private String queryId;
  private String ks;
  private boolean isRoot;

  public StreamListener(List<Object> results,
                        DeepSparkContext dsc,
                        ActorResultListener callBackActor,
                        String queryId,
                        String ks,
                        boolean isRoot) {
    this.results = results;
    this.dsc = dsc;
    this.callBackActor = callBackActor;
    this.queryId = queryId;
    this.ks = ks;
    this.isRoot = isRoot;
  }

  @Override
  public void run() {
    int lastSize = 0;
    boolean resultsEmpty = true;
    int currentSize = 0;

    while(true){
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      synchronized (results){
        resultsEmpty = results.isEmpty();
        currentSize = results.size();
      }
      if((!resultsEmpty) &&(lastSize == currentSize)){
        synchronized (results){
          MetaStream.sendResultsToNextStep(results, dsc, callBackActor, queryId, ks, isRoot);
          results.clear();
        }
      }
      lastSize = results.size();

    }
  }
}
