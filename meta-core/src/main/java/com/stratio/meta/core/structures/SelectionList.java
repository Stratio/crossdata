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

public class SelectionList extends SelectionClause {

    private boolean distinct;
    private Selection selection;

    public SelectionList(boolean distinct, Selection selection) {
        this.type = TYPE_SELECTION;
        this.distinct = distinct;
        this.selection = selection;
    }   
    
    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }        

    public Selection getSelection() {
        return selection;
    }

    public void setSelection(Selection selection) {
        this.selection = selection;
    }        
    
    public int getTypeSelection(){
        return selection.getType();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(distinct){
            sb.append("DISTINCT ");
        }
        sb.append(selection.toString());
        return sb.toString();
    }
    
}
