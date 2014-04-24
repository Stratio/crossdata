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
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.Tree;

/**
 * Class that models a {@code USE} statement from the META language.
 */
public class UseStatement extends MetaStatement {

    /**
     * The name of the keyspace to be used.
     */
    private final String keyspaceName;

    /**
     * Class constructor.
     * @param keyspaceName The name of the target keyspace.
     */
    public UseStatement(String keyspaceName) {
        this.keyspaceName = keyspaceName;
        this.command = false;
    }

    /**
     * Get the name of the keyspace to be used.
     * @return The name.
     */
    public String getKeyspaceName() {
        return keyspaceName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("USE ");
        sb.append(keyspaceName);
        return sb.toString();
    }

    @Override
    public Result validate(MetadataManager metadata, String targetKeyspace) {
        Result result = QueryResult.createSuccessQueryResult();
        if(keyspaceName != null && keyspaceName.length() > 0){
            if(!metadata.getKeyspacesNames().contains(keyspaceName)){
                result= QueryResult.createFailQueryResult("Keyspace " + keyspaceName + " does not exists.");
            }
        }else{
            result= QueryResult.createFailQueryResult("Missing keyspace name.");
        }
        return result;
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
    public DeepResultSet executeDeep() {
        return new DeepResultSet();
    }
    
    @Override
    public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
        Tree tree = new Tree();
        tree.setNode(new MetaStep(MetaPath.CASSANDRA, this));
        return tree;
    }
    
}
