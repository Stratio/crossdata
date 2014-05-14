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

import com.datastax.driver.core.TableMetadata;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.structures.Property;
import com.stratio.meta.core.structures.PropertyNameValue;
import com.stratio.meta.core.structures.ValueProperty;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.Tree;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Class that models an {@code ALTER TABLE} statement from the META language.
 */
public class AlterTableStatement extends MetaStatement{

    /**
     * The name of the target table.
     */
    private String tableName;

    /**
     * Type of alter. Accepted values are:
     * <ul>
     *     <li>1: Alter a column data type using {@code ALTER}.</li>
     *     <li>2: Add a new column using {@code ADD}.</li>
     *     <li>3: Drop a column using {@code DROP}.</li>
     *     <li>4: Establish a set of options using {@code WITH}.</li>
     * </ul>
     */
    private int prop;

    /**
     * Target column name.
     */
    private String column;

    /**
     * Target column datatype used with {@code ALTER} or {@code ADD}.
     */
    private String type;

    /**
     * The map of properties.
     */
    private Map<String, ValueProperty> option;

    /**
     * Class constructor.
     * @param tableName The name of the table.
     * @param column The name of the column.
     * @param type The data type of the column.
     * @param option The map of options.
     * @param prop The type of modification.
     */
    public AlterTableStatement(String tableName, String column, String type, Map<String, ValueProperty> option, int prop) {
        this.command = false;
        if(tableName.contains(".")){
            String[] ksAndTableName = tableName.split("\\.");
            keyspace = ksAndTableName[0];
            this.tableName = ksAndTableName[1];
            keyspaceInc = true;
        }else {
            this.tableName = tableName;
        }
        this.column = column;
        this.type = type;
        this.option = option;
        this.prop = prop;          
    }

    private String getOptionString(){
        StringBuilder sb = new StringBuilder();
        Set<String> keySet = option.keySet();
        sb.append(" with");
        for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
            String key = it.next();
            ValueProperty vp = option.get(key);
            sb.append(" ").append(key).append("=").append(String.valueOf(vp));
            if(it.hasNext()) {
                sb.append(" AND");
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Alter table ");
        if(keyspaceInc){
            sb.append(keyspace).append(".");
        }
        sb.append(tableName);
        switch(prop){
            case 1:
                sb.append(" alter ").append(column);
                sb.append(" type ").append(type);
                break;
            case 2:
                sb.append(" add ");
                sb.append(column).append(" ");
                sb.append(type);
                break;
            case 3:
                sb.append(" drop ");
                sb.append(column);
                break;
            case 4:
                sb.append(getOptionString());
                break;
            default:
                sb.append("bad option");
                break;
        }        
        return sb.toString();
    }

    @Override
    public String translateToCQL() {
        return this.toString();
    }

    /**
     * Validate the semantics of the current statement. This method checks the
     * existing metadata to determine that all referenced entities exists in the
     * {@code targetKeyspace} and the types are compatible with the assignations
     * or comparisons.
     *
     * @param metadata The {@link com.stratio.meta.core.metadata.MetadataManager} that provides
     *                 the required information.
     * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
     */
    @Override
    public Result validate(MetadataManager metadata) {
        Result result = validateKeyspaceAndTable(metadata, sessionKeyspace, keyspaceInc, keyspace, tableName);
        if(!result.hasError()) {
            String effectiveKeyspace = getEffectiveKeyspace();

            TableMetadata tableMetadata = metadata.getTableMetadata(effectiveKeyspace, tableName);

            switch(prop){
                case 1:
                    result = validateAlter(tableMetadata);
                    break;
                case 2:
                    result = validateAdd(tableMetadata);
                    break;
                case 3:
                    result = validateDrop(tableMetadata);
                    break;
                case 4:
                    result = validateProperties(tableMetadata);
                    break;
                default:
            }
        }
        return result;
    }

    private Result validateAlter(TableMetadata tableMetadata) {
        Result result = QueryResult.createSuccessQueryResult();
        //Validate target column name

        //Validate type

        return result;
    }

    private Result validateAdd(TableMetadata tableMetadata) {
        Result result = QueryResult.createSuccessQueryResult();
        //Validate target column name

        //Validate type

        return result;
    }

    private Result validateDrop(TableMetadata tableMetadata) {
        Result result = QueryResult.createSuccessQueryResult();
        //Validate target column name
        return result;
    }

    private Result validateProperties(TableMetadata tableMetadata) {
        Result result = QueryResult.createSuccessQueryResult();
        Iterator<Property> props = option.iterator();
        boolean exit = false;
        while(!exit && props.hasNext()){
            Property property = props.next();
            if(property.getType() == Property.TYPE_NAME_VALUE){
                PropertyNameValue propertyNameValue = (PropertyNameValue) property;
                if("ephemeral".equalsIgnoreCase(propertyNameValue.getName())
                        && propertyNameValue.getVp().getType() != ValueProperty.TYPE_BOOLEAN){
                    // If property ephemeral is present, it must be a boolean type
                    result = QueryResult.createFailQueryResult("Property 'ephemeral' must be a boolean");
                    exit = true;
                } else if("ephemeral_tuples".equalsIgnoreCase(propertyNameValue.getName())
                        && propertyNameValue.getVp().getType() != ValueProperty.TYPE_BOOLEAN){
                    // If property ephemeral_tuples is present, it must be a integer type
                    result= QueryResult.createFailQueryResult("Property 'ephemeral' must be a boolean");
                    exit = true;
                } else if("ephemeral_persist_on".equalsIgnoreCase(propertyNameValue.getName())
                        && propertyNameValue.getVp().getType() != ValueProperty.TYPE_BOOLEAN){
                    // If property ephemeral_persist_on is present, it must be a string type
                    result= QueryResult.createFailQueryResult("Property 'ephemeral_persist_on' must be a string");
                    exit = true;
                }
            }
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
