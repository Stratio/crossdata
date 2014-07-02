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

package com.stratio.meta.streaming;

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
    boolean resultsEmpty;
    int currentSize;

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
