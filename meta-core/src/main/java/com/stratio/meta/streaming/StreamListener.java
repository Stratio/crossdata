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

import java.util.List;

public class StreamListener extends Thread {

  private List<Object> results;
  private DeepSparkContext dsc;

  public StreamListener(List<Object> results, DeepSparkContext dsc) {
    this.results = results;
    this.dsc = dsc;
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
      if((!resultsEmpty) &&(lastSize == currentSize)){
        MetaStream.sendResultsToNextStep(results, dsc);
        synchronized (results){
          results.clear();
        }
      }
      lastSize = results.size();
      synchronized (results){
        resultsEmpty = results.isEmpty();
        currentSize = results.size();
      }
    }
  }
}
