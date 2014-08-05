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

package com.stratio.meta.core.structures;

public abstract class WindowSelect {

  public static final int TYPE_LAST = 1;
  public static final int TYPE_ROWS = 2;
  public static final int TYPE_TIME = 3;

  protected int type;

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  @Override
  public abstract String toString();

  public long getDurationInMilliseconds(){
    long millis = 0;
    if(this instanceof WindowTime){
      WindowTime windowTime = ((WindowTime) this);

      int factor = 1;
      TimeUnit timeUnit = windowTime.getUnit();
      if(timeUnit.equals(TimeUnit.SECONDS)){
        factor = factor * 1000;
      } else if(timeUnit.equals(TimeUnit.MINUTES)){
        factor = factor * 1000 * 60;
      } else if(timeUnit.equals(TimeUnit.HOURS)){
        factor = factor * 1000 * 60 * 60;
      } else if(timeUnit.equals(TimeUnit.DAYS)){
        factor = factor * 1000 * 60 * 60 * 24;
      }
      millis = windowTime.getNum()*factor;
    }
    return millis;
  }

}
