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
