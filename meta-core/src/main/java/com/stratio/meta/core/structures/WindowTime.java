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

public class WindowTime extends WindowSelect {

    private int num;
    private TimeUnit unit;

    public WindowTime(int num, TimeUnit unit) {
        this.type = TYPE_TIME;
        this.num = num;
        this.unit = unit;
    }   
    
    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public void setTime(TimeUnit unit) {
        this.unit = unit;
    }
            
    @Override
    public String toString() {
        return num+" "+unit;
    }
    
}
