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

package com.stratio.meta.core.statements;

import com.datastax.driver.core.Statement;
import com.stratio.meta.common.data.DeepResultSet;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.Tree;

public class DropIndexStatement extends MetaStatement {

    private boolean _dropIfExists = false;
    private String _name = null;

    public DropIndexStatement(){
        this.command = false;
    }
    
    public DropIndexStatement(String name){
        this();
        _name = name;
    }
    
    public void setDropIfExists(){
            _dropIfExists = true;
    }

    public void setName(String name){
            _name = name;
    }

    @Override
    public String toString() {
            StringBuilder sb = new StringBuilder("DROP INDEX ");
            if(_dropIfExists){
                    sb.append("IF EXISTS ");
            }
            sb.append(_name);
            return sb.toString();
    }

    /** {@inheritDoc} */
    @Override
    public Result validate(MetadataManager metadata, String targetKeyspace) {
        return null;
    }

    @Override
    public String getSuggestion() {
        return this.getClass().toString().toUpperCase()+" EXAMPLE";
    }

    @Override
    public String translateToCQL() {
        System.out.println("translatedToCQL="+toString());
        return this.toString();
    }

    
    @Override
    public Statement getDriverStatement() {
        return null;
    }
    
    @Override
    public DeepResultSet executeDeep() {
        return new DeepResultSet();
    }

    @Override
    public Tree getPlan() {
        Tree tree = new Tree();
        tree.setNode(new MetaStep(MetaPath.CASSANDRA, this));
        return tree;
    }
    
}
