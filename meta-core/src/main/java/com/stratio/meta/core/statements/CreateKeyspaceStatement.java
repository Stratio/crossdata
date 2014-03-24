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
import com.stratio.meta.common.result.MetaResult;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.structures.ValueProperty;
import com.stratio.meta.core.utils.ParserUtils;
import com.stratio.meta.core.utils.DeepResult;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.ValidationException;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateKeyspaceStatement extends MetaStatement {
    
    private String name;
    private boolean ifNotExists;
    private HashMap<String, ValueProperty> properties;

    public CreateKeyspaceStatement(String name, boolean ifNotExists, Map<String, ValueProperty> properties) {
        this.name = name;
        this.ifNotExists = ifNotExists;
        this.properties = new HashMap<>();
        this.properties.putAll(properties);
    }   
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIfNotExists() {
        return ifNotExists;
    }

    public void setIfNotExists(boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }

    public HashMap<String, ValueProperty> getProperties() {
        return properties;
    }

    public void setProperties(HashMap properties) {
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
    public MetaResult validate(MetadataManager metadata, String targetKeyspace) {
        MetaResult result = new MetaResult();
        if(name!= null && name.length() > 0) {
            KeyspaceMetadata ksMetadata = metadata.getKeyspaceMetadata(name);
            if(ksMetadata != null && !ifNotExists){
                result.setHasError();
                result.setErrorMessage("Keyspace " + name + " already exists.");
            }
        }else{
            result.setHasError();
            result.setErrorMessage("Empty keyspace name found.");
        }

        if(properties.size() == 0 || !properties.containsKey("replication")){
            result.setHasError();
            result.setErrorMessage("Missing mandatory replication property.");
        }

        return result;
    }

    //TODO Remove
    public void validate_remove() {
        /*
        if(properties.isEmpty()){
            throw new ValidationException("CREATE KEYSPACE must include at least property 'replication'");
        }
        // Check if there are innapropiate properties
        for(String key: properties.keySet()){
            if(!key.equalsIgnoreCase("replication") && !key.equalsIgnoreCase("durable_writes")){
                throw new ValidationException("CREATE KEYSPACE can only include properties 'replication' and 'durable_writes'");
            }
        }
        // Check if replication is present and it's built properly
        if(!properties.containsKey("replication")){
            throw new ValidationException("CREATE KEYSPACE must include property 'replication'");
        }
        if(properties.get("replication").getType() != ValueProperty.TYPE_MAPLT){
            throw new ValidationException("'replication' property must be a map");
        }
        MapLiteralProperty mlp = (MapLiteralProperty) properties.get("replication");
        if(mlp.isEmpty()){
            throw new ValidationException("'replication' property cannot be empty");
        }*/   
        // if durable_writes is present then it must be a boolean type
        if(properties.containsKey("durable_writes")){
            if(properties.get("durable_writes").getType() != ValueProperty.TYPE_BOOLEAN){
                throw new ValidationException("Property 'replication' must be a boolean");
            }
        }
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
    
//    @Override
//    public String parseResult(ResultSet resultSet) {
//        return "Executed successfully"+System.getProperty("line.separator");
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
    public List<MetaStep> getPlan() {
        ArrayList<MetaStep> steps = new ArrayList<>();
        return steps;
    }
    
}
