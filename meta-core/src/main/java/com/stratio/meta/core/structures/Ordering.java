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

public class Ordering {
    
    private String identifier;
    private boolean dirInc;
    private OrderDirection orderDir;

    public Ordering(String identifier, boolean dirInc, OrderDirection orderDir) {
        this.identifier = identifier;
        this.dirInc = dirInc;
        this.orderDir = orderDir;
    }
    
    public Ordering(String identifier) {
        this(identifier, false, null);
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public boolean isDirInc() {
        return dirInc;
    }

    public void setDirInc(boolean dirInc) {
        this.dirInc = dirInc;
    }

    public OrderDirection getOrderDir() {
        return orderDir;
    }        

    public void setOrderDir(OrderDirection orderDir) {
        this.dirInc = true;
        this.orderDir = orderDir;
    }        
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder(identifier);
        if(dirInc){
            sb.append(" ").append(orderDir);
        }
        return sb.toString();
    }
    
}
