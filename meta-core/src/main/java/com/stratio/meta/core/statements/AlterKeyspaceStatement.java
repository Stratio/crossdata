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
import com.stratio.meta.core.structures.ValueProperty;
import com.stratio.meta.core.utils.ParserUtils;
import com.stratio.meta.core.utils.Tree;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that models an {@code ALTER KEYSPACE} statement from the META language.
 */
public class AlterKeyspaceStatement extends MetaStatement {

    /**
     * The name of the target keyspace.
     */
    private String keyspace;

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
        this.properties.putAll(properties);
    }   

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ALTER KEYSPACE ");
        sb.append(keyspace).append(" WITH ");
        sb.append(ParserUtils.stringMap(properties, " = ", " AND "));
        return sb.toString();
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
        return new Tree();
    }
    
}
