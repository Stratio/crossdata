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

public class CreateTableStatement extends MetaStatement{
    
    private boolean keyspaceInc = false;
    private String keyspace;
    private String tablename;
    private LinkedHashMap<String, String> columns;    
    private List<String> primaryKey;    
    private List<String> clusterKey;
    private List<Property> properties;
    private int Type_Primary_Key;
    private boolean ifNotExists;
    private boolean withClusterKey;
    private int columnNumberPK;
    private boolean withProperties;

    public CreateTableStatement(String name_table, 
                                LinkedHashMap<String, String> columns, 
                                List<String> primaryKey, 
                                List<String> clusterKey, 
                                List<Property> properties,
                                int Type_Primary_Key, 
                                boolean ifNotExists, 
                                boolean withClusterKey, 
                                int columnNumberPK, 
                                boolean withProperties) {
        this.command = false;
        if(name_table.contains(".")){
            String[] ksAndTablename = name_table.split("\\.");
            keyspace = ksAndTablename[0];
            name_table = ksAndTablename[1];
            keyspaceInc = true;
        }
        this.tablename = name_table;
        this.columns = columns;
        this.primaryKey = primaryKey;
        this.clusterKey = clusterKey;
        this.properties = properties;
        this.Type_Primary_Key = Type_Primary_Key;
        this.ifNotExists = ifNotExists;
        this.withClusterKey = withClusterKey;
        this.columnNumberPK = columnNumberPK;
        this.withProperties=withProperties;
    }

    public String getKeyspace() {
        return keyspace;
    }

    public void setKeyspace(String keyspace) {
        this.keyspace = keyspace;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public LinkedHashMap<String, String> getColumns() {
        return columns;
    }

    public void setColumns(LinkedHashMap<String, String> columns) {
        this.columns = columns;
    }   
    
    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        if(tablename.contains(".")){
            String[] ksAndTablename = tablename.split("\\.");
            keyspace = ksAndTablename[0];
            tablename = ksAndTablename[1];
            keyspaceInc = true;
        }
        this.tablename = tablename;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Create table ");
        if(ifNotExists) sb.append("IF NOT EXISTS ");
        
        if(keyspaceInc){
            sb.append(keyspace).append(".");
        } 
        sb.append(tablename);
        
        switch(Type_Primary_Key){
            case 1: {
                Set<String> keySet = columns.keySet();
                int i = 0;
                sb.append(" (");
                
                for (Iterator<String> it = keySet.iterator();it.hasNext();){
                    String key = it.next();
                    String vp= columns.get(key);
                    if (i==0) sb.append(key).append(" ").append(vp).append(" PRIMARY KEY");
                    else sb.append(key).append(" ").append(vp);
                    i++;
                    if (it.hasNext()) sb.append(", ");
                    else sb.append(")");                      
                }         
            }break;
                
            case 2: {
                Set<String> keySet = columns.keySet();
                int i = 0;
                sb.append(" (");
                for (Iterator<String> it = keySet.iterator();it.hasNext();){
                    String key = it.next();
                    String vp= columns.get(key);
                    if (i == columnNumberPK) sb.append(key).append(" ").append(vp).append(" PRIMARY KEY");
                    else sb.append(key).append(" ").append(vp);
                    i++;
                    if (it.hasNext()) sb.append(", ");
                    else sb.append(")");  
                }
            }break;
            case 3: {
                Set<String> keySet = columns.keySet();
                sb.append(" (");
                for (String key : keySet) {
                    String vp = columns.get(key);
                    sb.append(key).append(" ").append(vp).append(", ");
                }
                
                sb.append("PRIMARY KEY (");
                int j=0;
                for (Iterator<String> it = primaryKey.iterator();it.hasNext();){
                    String key = it.next();
                    if (j== 0) sb.append(key);
                    else sb.append(", ").append(key);
                    j++;
                    if (!it.hasNext()) sb.append("))");
                }
            
            }break;
                
            case 4: {
                Set<String> keySet = columns.keySet();
                sb.append(" (");
                for (String key : keySet) {
                    String vp = columns.get(key);
                    sb.append(key).append(" ").append(vp).append(", ");
                }
                sb.append("PRIMARY KEY ((");
                int j=0;
                for (Iterator<String> it = primaryKey.iterator();it.hasNext();){
                    String key = it.next();
                    if (j== 0) sb.append(key);
                    else sb.append(", ").append(key);
                    j++;
                    if (!it.hasNext()) sb.append(")");
                }

                if (withClusterKey){
                    for (Iterator<String> it = clusterKey.iterator();it.hasNext();){
                        String key = it.next();
                        sb.append(", ").append(key);
                        if (!it.hasNext()) sb.append("))");
                    } 
                }
                else sb.append("))");
            }break;
                
            default:{
                sb.append("bad option");
            }break;
                
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
        if(!result.hasError()){
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
        Result result = QueryResult.CreateSuccessQueryResult();
        //Get the effective keyspace based on the user specification during the create
        //sentence, or taking the keyspace in use in the user session.
        String effectiveKeyspace = targetKeyspace;
        if(keyspaceInc){
            effectiveKeyspace = keyspace;
        }

        //Check that the keyspace exists, and that the table does not exits.
        if(effectiveKeyspace == null || effectiveKeyspace.length() == 0){
            result= QueryResult.CreateFailQueryResult("Target keyspace missing or no keyspace has been selected.");
        }else{
            KeyspaceMetadata ksMetadata = metadata.getKeyspaceMetadata(effectiveKeyspace);
            if(ksMetadata == null){
                result= QueryResult.CreateFailQueryResult("Keyspace " + effectiveKeyspace + " does not exists.");
            }else {
                TableMetadata tableMetadata = metadata.getTableMetadata(effectiveKeyspace, tablename);
                if (tableMetadata != null && !ifNotExists) {
                    result= QueryResult.CreateFailQueryResult("Table " + tablename + " already exists.");
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
        Result result = QueryResult.CreateSuccessQueryResult();
        //The columns in the primary key must be declared.
        for (String pk : primaryKey) {
            if(!columns.containsKey(pk)){
                result= QueryResult.CreateFailQueryResult("Missing declaration for Primary Key column " + pk);
            }
        }

        //The columns in the clustering key must be declared and not part of the primary key.
        for(String ck : clusterKey){
            if(!columns.containsKey(ck)){
                result= QueryResult.CreateFailQueryResult("Missing declaration for Clustering Key column " + ck);
            }
            if(primaryKey.contains(ck)){
                result= QueryResult.CreateFailQueryResult("Column " + ck + " found as part of primary and clustering key.");
            }
        }

        String [] supported = {"BIGINT", "BOOLEAN", "COUNTER", "DOUBLE", "FLOAT", "INT", "VARCHAR"};
        Set<String> supportedColumns = new HashSet<>(Arrays.asList(supported));
        for(String c : columns.keySet()){
            if(!supportedColumns.contains(columns.get(c).toUpperCase()) || c.toLowerCase().startsWith("stratio")){
                result= QueryResult.CreateFailQueryResult("Column " + c + " with datatype " + columns.get(c) + " not supported.");
            }
        }

        return result;
    }

    /**
     * Validate the semantics of the ephemeral properties.
     * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
     */
    private Result validateProperties(){
        Result result = QueryResult.CreateSuccessQueryResult();
        if(withProperties){
            for(Property property: properties){
                if(property.getType() == Property.TYPE_NAME_VALUE){
                    PropertyNameValue propertyNameValue = (PropertyNameValue) property;
                    // If property ephemeral is present, it must be a boolean type
                    if(propertyNameValue.getName().equalsIgnoreCase("ephemeral")){
                        if(propertyNameValue.getVp().getType() != ValueProperty.TYPE_BOOLEAN){
                            result= QueryResult.CreateFailQueryResult("Property 'ephemeral' must be a boolean");
                        }
                        break;
                        // If property ephemeral_tuples is present, it must be a integer type
                    } else if(propertyNameValue.getName().equalsIgnoreCase("ephemeral_tuples")){
                        if(propertyNameValue.getVp().getType() != ValueProperty.TYPE_BOOLEAN){
                            result= QueryResult.CreateFailQueryResult("Property 'ephemeral' must be a boolean");
                        }
                        break;
                        // If property ephemeral_persist_on is present, it must be a string type
                    } else if(propertyNameValue.getName().equalsIgnoreCase("ephemeral_persist_on")){
                        if(propertyNameValue.getVp().getType() != ValueProperty.TYPE_BOOLEAN){
                            result= QueryResult.CreateFailQueryResult("Property 'ephemeral_persist_on' must be a string");
                        }
                        break;
                    }
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
        for (int i = 0; i < cqlString.length(); i++) {
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
        }
        return sb.toString();
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
        //Check ifNotExists
        Tree tree = new Tree();
        tree.setNode(new MetaStep(MetaPath.CASSANDRA, this));
        return tree;
    }
    
}
