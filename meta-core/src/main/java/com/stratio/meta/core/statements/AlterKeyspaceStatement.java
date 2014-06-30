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
import com.stratio.meta.core.structures.ValueProperty;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.ParserUtils;
import com.stratio.meta.core.utils.Tree;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that models an {@code ALTER KEYSPACE} statement from the META language.
 */
public class AlterKeyspaceStatement extends MetaStatement {

    /**
     * The map of properties of the keyspace. The different options accepted by a keyspace
     * are determined by the selected {@link com.datastax.driver.core.ReplicationStrategy}.
     */
    private Map<String, ValueProperty> properties;

    /**
     * Class constructor.
     * @param keyspace The name of the keyspace.
     * @param properties The map of properties.
     */
    public AlterKeyspaceStatement(String keyspace, Map<String, ValueProperty> properties) {
        this.command = false;
        this.keyspace = keyspace;
        this.properties = new HashMap<>();
        for(Map.Entry<String, ValueProperty> entry : properties.entrySet()){
            this.properties.put(entry.getKey().toLowerCase(), entry.getValue());
        }
    }   

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ALTER KEYSPACE ");
        sb.append(keyspace).append(" WITH ");
        sb.append(ParserUtils.stringMap(properties, " = ", " AND "));
        return sb.toString();
    }

    @Override
    public String translateToCQL(MetadataManager metadataManager) {
        return this.toString();
    }

    @Override
    public Result validate(MetadataManager metadata, EngineConfig config) {

        Result result = QueryResult.createSuccessQueryResult();

        if(keyspace!= null && keyspace.length() > 0) {
            KeyspaceMetadata ksMetadata = metadata.getKeyspaceMetadata(keyspace);
            if(ksMetadata == null){
                result= Result.createValidationErrorResult("Keyspace " + keyspace + " not found.");
            }
        } else {
            result= Result.createValidationErrorResult("Empty keyspace name found.");
        }

        if(properties.isEmpty() || (!properties.containsKey("replication")
                                    & !properties.containsKey("durable_writes"))){
            result= Result.createValidationErrorResult("At least one property must be included: 'replication' or 'durable_writes'.");
        }

        return result;
    }

    @Override
    public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
        Tree tree = new Tree();
        tree.setNode(new MetaStep(MetaPath.CASSANDRA, this));
        return tree;
    }
    
}
