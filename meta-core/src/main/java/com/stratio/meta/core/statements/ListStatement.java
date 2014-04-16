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
import com.stratio.meta.core.structures.ListType;
import com.stratio.meta.core.utils.Tree;

/**
 * Class that models a {@code LIST} statement from the META language.
 */
public class ListStatement extends MetaStatement {

    /**
     * The {@link com.stratio.meta.core.structures.ListType} to be executed.
     */
    private ListType _type = null;

    /**
     * Class constructor.
     * @param type The {@link com.stratio.meta.core.structures.ListType} to be executed.
     */
    public ListStatement(String type){
        this.command = true;
        _type = ListType.valueOf(type.toUpperCase());
    }

    @Override
    public String toString() {
            return "LIST " + _type;
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
    public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
        return new Tree();
    }
    
}
