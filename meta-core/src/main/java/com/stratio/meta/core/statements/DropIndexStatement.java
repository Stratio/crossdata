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
import com.stratio.meta.common.result.MetaResult;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.DeepResult;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.Tree;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public MetaResult validate(MetadataManager metadata, String targetKeyspace) {
        return null;
    }

    @Override
    public String getSuggestion() {
        return this.getClass().toString().toUpperCase()+" EXAMPLE";
    }

    @Override
    public String translateToCQL() {
        return this.toString();
    }

//    @Override
//    public String parseResult(ResultSet resultSet) {
//        return "\t"+resultSet.toString();
//    }
    
    @Override
    public Statement getDriverStatement() {
        Statement statement = null;
        return statement;
    }
    
    @Override
    public DeepResult executeDeep() {
        return new DeepResult("", new ArrayList<>(Arrays.asList("Not supported yet")));
    }
    
    @Override
    public Tree getPlan() {
        return new Tree();
    }
    
}
