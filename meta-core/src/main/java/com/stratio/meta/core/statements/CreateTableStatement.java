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
import com.datastax.driver.core.TableMetadata;
import com.stratio.meta.common.data.DeepResultSet;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.structures.Property;
import com.stratio.meta.core.structures.PropertyNameValue;
import com.stratio.meta.core.structures.ValueProperty;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.ParserUtils;
import com.stratio.meta.core.utils.Tree;

import java.util.*;

/**
 * Class that models a {@code CREATE TABLE} statement of the META language.
 */
public class CreateTableStatement extends MetaStatement{

    /**
     * Whether the keyspace has been specified in the Create Table statement or it should be taken from the
     * environment.
     */
    private boolean keyspaceInc = false;

    /**
     * The keyspace specified in the create table statement.
     */
    private String keyspace;

    /**
     * The name of the target table.
     */
    private String tableName;

    /**
     * A map with the name of the columns in the table and the associated data type.
     */
    private Map<String, String> columns;

    /**
     * The list of columns that are part of the primary key.
     */
    private List<String> primaryKey;

    /**
     * The list of columns that are part of the clustering key.
     */
    private List<String> clusterKey;

    /**
     * The list of {@link com.stratio.meta.core.structures.Property} of the table.
     */
    private List<Property> properties = null;

    /**
     * The type of primary key. Accepted values are:
     * <ul>
     *     <li>1: If the primary key contains a single column.</li>
     *     <li>2: If the primary key is composed of several columns but it does not
     *     contain a clustering key.</li>
     *     <li>3: If both the primary key and clustering key are specified.</li>
     * </ul>
     */
    private int primaryKeyType;

    /**
     * Whether the table should be created only if not exists.
     */
    private boolean ifNotExists;

    /**
     * Whether the table will be created.
     */
    private boolean createTable = false;

    /**
     * The number of the column associated with the primary key. This
     * value is only used if the type of primary key is {@code 1}.
     */
    private int columnNumberPK;

    /**
     * Whether the table should be created with a set of properties.
     */
    private boolean withProperties = false;

    /**
     * Class constructor.
     * @param tableName The name of the table.
     * @param columns A map with the name of the columns in the table and the associated data type.
     * @param primaryKey The list of columns that are part of the primary key.
     * @param clusterKey The list of columns that are part of the clustering key.
     * @param primaryKeyType The type of primary key.
     * @param columnNumberPK The number of the column associated with the primary key. This
     * value is only used if the type of primary key is {@code 1}.
     */
    public CreateTableStatement(String tableName,
                                Map<String, String> columns,
                                List<String> primaryKey,
                                List<String> clusterKey,
                                int primaryKeyType,
                                int columnNumberPK) {
        this.command = false;
        if(tableName.contains(".")){
            String[] ksAndTablename = tableName.split("\\.");
            keyspace = ksAndTablename[0];
            this.tableName = ksAndTablename[1];
            keyspaceInc = true;
        }else {
            this.tableName = tableName;
        }
        this.columns = columns;
        this.primaryKey = primaryKey;
        this.clusterKey = clusterKey;
        this.primaryKeyType = primaryKeyType;
        this.columnNumberPK = columnNumberPK;
    }

    /**
     * Set the keyspace specified in the create table statement.
     * @param keyspace The name of the keyspace.
     */
    public void setKeyspace(String keyspace) {
        this.keyspace = keyspace;
    }

    /**
     * Set the list of {@link com.stratio.meta.core.structures.Property}.
     * @param properties The list.
     */
    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public void setIfNotExists(boolean ifNotExists){
        this.ifNotExists = ifNotExists;
    }

    public void setWithProperties(boolean withProperties){
        this.withProperties = withProperties;
    }

    public String getSinglePKString(){
        StringBuilder sb = new StringBuilder(" (");
        Set<String> keySet = columns.keySet();
        int i = 0;
        for (Iterator<String> it = keySet.iterator();it.hasNext();){
            String key = it.next();
            String vp= columns.get(key);
            sb.append(key).append(" ").append(vp);
            if (i == columnNumberPK){
                sb.append(" PRIMARY KEY");
            }
            i++;
            if (it.hasNext()){
                sb.append(", ");
            }else{
                sb.append(")");
            }
        }
        return sb.toString();
    }

    public String getCompositePKString(){
        StringBuilder sb = new StringBuilder("PRIMARY KEY (");
        if(primaryKeyType == 3){
            sb.append("(");
        }

        Iterator<String> pks = primaryKey.iterator();
        while(pks.hasNext()){
            sb.append(pks.next());
            if(pks.hasNext()){
                sb.append(", ");
            }
        }

        if(primaryKeyType == 3){
            sb.append(")");
            for (Iterator<String> it = clusterKey.iterator();it.hasNext();){
                String key = it.next();
                sb.append(", ").append(key);
            }
        }

        sb.append("))");
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Create table ");
        if(ifNotExists) {
            sb.append("IF NOT EXISTS ");
        }

        if(keyspaceInc){
            sb.append(keyspace).append(".");
        }
        sb.append(tableName);

        if(primaryKeyType == 1){
            sb.append(getSinglePKString());
        }else{
            Set<String> keySet = columns.keySet();
            sb.append(" (");
            for (String key : keySet) {
                String vp = columns.get(key);
                sb.append(key).append(" ").append(vp).append(", ");
            }
            sb.append(getCompositePKString());
        }

        if(withProperties){
            sb.append(" WITH ").append(ParserUtils.stringList(properties, " AND "));
        }
        return sb.toString();
    }

    /** {@inheritDoc} */
    @Override
    public Result validate(MetadataManager metadata, String targetKeyspace) {
        Result result = validateKeyspaceAndTable(metadata, targetKeyspace);
        if(!result.hasError()){
            result = validateColumns();
        }
        if(!result.hasError() && withProperties) {
                result = validateProperties();
        }
        return result;
    }

    /**
     * Validate that a valid keyspace is present, and that the table does not
     * exits unless {@code ifNotExists} has been specified.
     * @param metadata The {@link com.stratio.meta.core.metadata.MetadataManager} that provides
     *                 the required information.
     * @param targetKeyspace The target keyspace where the query will be executed.
     * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
     */
    private Result validateKeyspaceAndTable(MetadataManager metadata, String targetKeyspace){
        Result result = QueryResult.createSuccessQueryResult();
        //Get the effective keyspace based on the user specification during the create
        //sentence, or taking the keyspace in use in the user session.
        String effectiveKeyspace = targetKeyspace;
        if(keyspaceInc){
            effectiveKeyspace = keyspace;
        }

        //Check that the keyspace exists, and that the table does not exits.
        if(effectiveKeyspace == null || effectiveKeyspace.length() == 0){
            result= QueryResult.createFailQueryResult("Target keyspace missing or no keyspace has been selected.");
        }else{
            KeyspaceMetadata ksMetadata = metadata.getKeyspaceMetadata(effectiveKeyspace);
            if(ksMetadata == null){
                result= QueryResult.createFailQueryResult("Keyspace " + effectiveKeyspace + " does not exists.");
            }else {
                TableMetadata tableMetadata = metadata.getTableMetadata(effectiveKeyspace, tableName);
                if (tableMetadata != null && !ifNotExists) {
                    result= QueryResult.createFailQueryResult("Table " + tableName + " already exists.");
                }else if (tableMetadata == null){
                    createTable = true;
                }
            }

        }
        return result;
    }

    /**
     * Validate that the primary key is created and uses a set
     * of existing columns. The same checks are applied to the clustering
     * key if it exists.
     * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
     */
    private Result validateColumns(){
        Result result = QueryResult.createSuccessQueryResult();
        //The columns in the primary key must be declared.
        for (String pk : primaryKey) {
            if(!columns.containsKey(pk)){
                result= QueryResult.createFailQueryResult("Missing declaration for Primary Key column " + pk);
            }
        }

        //The columns in the clustering key must be declared and not part of the primary key.
        for(String ck : clusterKey){
            if(!columns.containsKey(ck)){
                result= QueryResult.createFailQueryResult("Missing declaration for Clustering Key column " + ck);
            }
            if(primaryKey.contains(ck)){
                result= QueryResult.createFailQueryResult("Column " + ck + " found as part of primary and clustering key.");
            }
        }

        String [] supported = {"BIGINT", "BOOLEAN", "COUNTER", "DOUBLE", "FLOAT", "INT", "VARCHAR"};
        Set<String> supportedColumns = new HashSet<>(Arrays.asList(supported));
        for(String c : columns.keySet()){
            if(!supportedColumns.contains(columns.get(c).toUpperCase()) || c.toLowerCase().startsWith("stratio")){
                result= QueryResult.createFailQueryResult("Column " + c + " with datatype " + columns.get(c) + " not supported.");
            }
        }

        return result;
    }

    /**
     * Validate the semantics of the ephemeral properties.
     * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
     */
    private Result validateProperties(){
        Result result = QueryResult.createSuccessQueryResult();
        Iterator<Property> props = properties.iterator();
        boolean exit = false;
        while(!exit && props.hasNext()){
            Property property = props.next();
            if(property.getType() == Property.TYPE_NAME_VALUE){
                PropertyNameValue propertyNameValue = (PropertyNameValue) property;
                if(propertyNameValue.getName().equalsIgnoreCase("ephemeral")
                        && propertyNameValue.getVp().getType() != ValueProperty.TYPE_BOOLEAN){
                    // If property ephemeral is present, it must be a boolean type
                    result = QueryResult.createFailQueryResult("Property 'ephemeral' must be a boolean");
                    exit = true;
                } else if(propertyNameValue.getName().equalsIgnoreCase("ephemeral_tuples")
                        && propertyNameValue.getVp().getType() != ValueProperty.TYPE_BOOLEAN){
                    // If property ephemeral_tuples is present, it must be a integer type
                    result= QueryResult.createFailQueryResult("Property 'ephemeral' must be a boolean");
                    exit = true;
                } else if(propertyNameValue.getName().equalsIgnoreCase("ephemeral_persist_on")
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
    public String getSuggestion() {
        return this.getClass().toString().toUpperCase()+" EXAMPLE";
    }

    @Override
    public String translateToCQL() {
        String cqlString = this.toString();
        if (!cqlString.contains(" WITH ")) {
            return cqlString;
        }
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < cqlString.length()) {
            char c = cqlString.charAt(i);
            if (c == '{') {
                sb.append("{");
                int newI = cqlString.indexOf("}", i);
                String insideBracket = cqlString.substring(i + 1, newI);
                insideBracket = insideBracket.replace(":", " ");
                insideBracket = insideBracket.replace(",", " ");

                boolean wasChanged = true;
                while (wasChanged) {
                    int before = insideBracket.length();
                    insideBracket = insideBracket.replace("  ", " ");
                    int after = insideBracket.length();
                    if (before == after) {
                        wasChanged = false;
                    }
                }

                insideBracket = insideBracket.trim();
                String[] strs = insideBracket.split(" ");
                for (int j = 0; j < strs.length; j++) {
                    String currentStr = strs[j];
                    if (currentStr.matches("[0123456789.]+")) {
                        sb.append(strs[j]);
                    } else {
                        sb.append("\'").append(strs[j]).append("\'");
                    }
                    if (j % 2 == 0) {
                        sb.append(": ");
                    } else {
                        if (j < (strs.length - 1)) {
                            sb.append(", ");
                        }
                    }
                }
                sb.append("}");
                i = newI;
            } else {
                sb.append(c);
            }
            i++;
        }
        return sb.toString();
    }

    @Override
    public DeepResultSet executeDeep() {
        return new DeepResultSet();
    }

    @Override
    public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
        Tree tree = new Tree();
        if(createTable) {
            tree.setNode(new MetaStep(MetaPath.CASSANDRA, this));
        }
        return tree;
    }

}
