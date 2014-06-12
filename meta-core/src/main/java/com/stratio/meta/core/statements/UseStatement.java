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
     * Class constructor.
     * @param keyspace The name of the target keyspace.
     */
    public UseStatement(String keyspace) {
        super.keyspace = keyspace;
        if(!keyspace.contains("'")){
            super.keyspace = keyspace.toLowerCase();
        }
        this.command = false;
    }

    /**
     * Get the name of the keyspace to be used.
     * @return The name.
     */
    public String getKeyspaceName() {
        return keyspace;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("USE ");
        sb.append(keyspace);
        return sb.toString();
    }

    @Override
    public Result validate(MetadataManager metadata) {
        Result result = QueryResult.createSuccessQueryResult();
        if(keyspace != null && keyspace.length() > 0){
            if(!metadata.getKeyspacesNames().contains(keyspace.toLowerCase())){
                result= Result.createValidationErrorResult("Keyspace " + keyspace + " does not exist.");
            }
        }else{
            result= Result.createValidationErrorResult("Missing keyspace name.");
        }
        return result;
    }

    @Override
    public String translateToCQL() {
        return this.toString();
    }

    @Override
    public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
        Tree tree = new Tree();
        tree.setNode(new MetaStep(MetaPath.CASSANDRA, this));
        return tree;
    }
    
}
