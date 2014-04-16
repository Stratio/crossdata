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

import java.util.ArrayList;

public class SelectionSelectors extends Selection {

    private ArrayList<SelectionSelector> selectors;

    public SelectionSelectors() {
        this.type = TYPE_SELECTOR;
        selectors = new ArrayList<>();
    }    
    
    public SelectionSelectors(ArrayList<SelectionSelector> selectors) {
        this();
        this.selectors = selectors;
    }   
    
    public ArrayList<SelectionSelector> getSelectors() {
        return selectors;
    }

    public void setSelectors(ArrayList<SelectionSelector> selectors) {
        this.selectors = selectors;
    }   
    
    public void addSelectionSelector(SelectionSelector ss){
        selectors.add(ss);
    }
    
    public SelectionSelector getSelectionSelector(int index){
        return selectors.get(index);
    }
    
    public void removeSelectionSelector(int index){
        selectors.remove(index);
    }
    
    public int numberOfSelectors(){
        return selectors.size();
    }
    
    @Override
    public String toString() {
        return ParserUtils.stringList(selectors, ", ");
    }
    
}
