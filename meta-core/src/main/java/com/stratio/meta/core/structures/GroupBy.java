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

import com.stratio.meta.core.utils.ParserUtils;

import java.util.List;

public class GroupBy {

    private List<String> colNames; 

    public GroupBy(List<String> colNames) {
        this.colNames = colNames;
    }    
    
    public List<String> getColNames() {
        return colNames;
    }

    public void setColNames(List<String> colNames) {
        this.colNames = colNames;
    }        
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder(" GROUP BY ");  
        sb.append(ParserUtils.stringList(colNames, ", "));
        return sb.toString();
    }
    
}
