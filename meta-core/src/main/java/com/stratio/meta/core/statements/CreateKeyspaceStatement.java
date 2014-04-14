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
import com.datastax.driver.core.Statement;
import com.stratio.meta.common.data.DeepResultSet;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.structures.ValueProperty;
import com.stratio.meta.core.utils.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that models a {@code CREATE KEYSPACE} statement from the META language.
 */
public class CreateKeyspaceStatement extends MetaStatement {

    /**
     * The name of the keyspace.
     */
    private String name;

    /**
     * Whether the keyspace should be created only if it not exists.
     */
    private boolean ifNotExists;

    /**
     * The map of properties of the keyspace. The different options accepted by a keyspace
     * are determined by the selected {@link com.datastax.driver.core.ReplicationStrategy}.
     */
    private HashMap<String, ValueProperty> properties;

    /**
     * Class constructor.
     * @param name The name of the keyspace.
     * @param ifNotExists Whether it should be created only if it not exists.
     * @param properties The map of properties.
     */
    public CreateKeyspaceStatement(String name, boolean ifNotExists, Map<String, ValueProperty> properties) {
        this.name = name;
        this.command = false;
        this.ifNotExists = ifNotExists;
        this.properties = new HashMap<>();
        this.properties.putAll(properties);
    }

    /**
     * Get the name of the keyspace.
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the keyspace.
     * @param name The name of the keyspace.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * If the IF NOT EXISTS clause has been specified.
     * @return Whether the keyspace should be created only if not exists.
     */
    public boolean isIfNotExists() {
        return ifNotExists;
    }

    /**
     * Set the value of the IF NOT EXISTS clause.
     * @param ifNotExists If it has been specified or not.
     */
    public void setIfNotExists(boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }

    /**
     * Get the map of properties.
     * @return The map.
     */
    public HashMap<String, ValueProperty> getProperties() {
        return properties;
    }

    /**
     * Set the map of properties used during the keyspace creation.
     * @param properties The map of properties.
     */
    public void setProperties(HashMap<String, ValueProperty> properties) {
        this.properties = properties;
    }        

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CREATE KEYSPACE ");
        if(ifNotExists){
            sb.append("IF NOT EXISTS ");
        }
        sb.append(name);
        sb.append(" WITH ");
        sb.append(ParserUtils.stringMap(properties, " = ", " AND "));
        return sb.toString();
    }

    @Override
    public Result validate(MetadataManager metadata, String targetKeyspace) {
        Result result = QueryResult.CreateSuccessQueryResult();
        if(name!= null && name.length() > 0) {
            KeyspaceMetadata ksMetadata = metadata.getKeyspaceMetadata(name);
            if(ksMetadata != null && !ifNotExists){
                result= QueryResult.CreateFailQueryResult("Keyspace " + name + " already exists.");
            }
        }else{
            result= QueryResult.CreateFailQueryResult("Empty keyspace name found.");
        }

        if(properties.size() == 0 || !properties.containsKey("replication")){
            result= QueryResult.CreateFailQueryResult("Missing mandatory replication property.");
        }

        return result;
    }

    @Override
    public String getSuggestion() {
        return this.getClass().toString().toUpperCase()+" EXAMPLE";
    }

    @Override
    public String translateToCQL() {
        String metaStr = this.toString();
        if(metaStr.contains("{")){
            return ParserUtils.translateLiteralsToCQL(metaStr);
        } else {
            return metaStr;
        }        
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
        Tree tree = new Tree();
        tree.setNode(new MetaStep(MetaPath.CASSANDRA, this));
        return tree;
    }
    
}
