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

import java.util.List;

public class StreamListener extends Thread {

  List<String> results;

  public StreamListener(List<String> results) {
    this.results = results;
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
      System.out.println("Checking end of window...");
      if((!resultsEmpty) &&(lastSize == currentSize)){
        System.out.println("--- End of the window ---");
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
