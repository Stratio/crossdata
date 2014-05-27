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

public class SelectionSelector {
    
    SelectorMeta selector;
    boolean aliasInc;
    String alias;

    public SelectionSelector(SelectorMeta selector, boolean aliasInc, String alias) {
        this.selector = selector;
        this.aliasInc = aliasInc;
        this.alias = alias;
    }
    
    public SelectionSelector(SelectorMeta selector) {
        this(selector, false, null);
    }
    
    public SelectorMeta getSelector() {
        return selector;
    }

    public void setSelector(SelectorMeta selector) {
        this.selector = selector;
    }

    public boolean isAliasInc() {
        return aliasInc;
    }

    public void setAliasInc(boolean aliasInc) {
        this.aliasInc = aliasInc;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.aliasInc = true;
        this.alias = alias;
    }        
        
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder(selector.toString());
        if(aliasInc){
            sb.append(" AS ").append(alias);
        }
        return sb.toString();
    }
    
}
