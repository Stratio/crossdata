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

import com.datastax.driver.core.KeyspaceMetadata;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.Tree;

/**
 * Class that models a {@code DROP KEYSPACE} statement from the META language.
 */
public class DropKeyspaceStatement extends MetaStatement {

    /**
     * Whether the keyspace should be removed only if exists.
     */
    private boolean ifExists;

    /**
     * Class constructor.
     * @param keyspace The name of the keyspace.
     * @param ifExists Whether it should be removed only if exists.
     */
    public DropKeyspaceStatement(String keyspace, boolean ifExists) {
        this.command = false;
        this.catalog = keyspace;
        this.ifExists = ifExists;
    }    

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Drop catalog ");
        if(ifExists){
           sb.append("if exists ");
        } 
        sb.append(catalog);
        return sb.toString();
    }

    @Override
    public Result validate(MetadataManager metadata, EngineConfig config) {
        Result result = QueryResult.createSuccessQueryResult();
        KeyspaceMetadata ksMetadata = metadata.getKeyspaceMetadata(catalog);
        if(ksMetadata == null && !ifExists){
            result = Result.createValidationErrorResult("Keyspace " + catalog + " does not exist.");
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
