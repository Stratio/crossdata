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
import com.stratio.meta.common.result.MetaResult;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.structures.MetaProperty;
import com.stratio.meta.core.structures.PropertyNameValue;
import com.stratio.meta.core.structures.ValueProperty;
import com.stratio.meta.core.utils.ParserUtils;
import com.stratio.meta.core.utils.DeepResult;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.Tree;

import java.util.*;

public class CreateTableStatement extends MetaStatement{
    
    private boolean keyspaceInc = false;
    private String keyspace;
    private String tablename;
    private LinkedHashMap<String, String> columns;    
    private List<String> primaryKey;    
    private List<String> clusterKey;    
    //private LinkedHashMap<String, ValueProperty> properties;
    private List<MetaProperty> properties;
    private int Type_Primary_Key;
    private boolean ifNotExists;
    private boolean withClusterKey;
    private int columnNumberPK;
    private boolean withProperties;

    public CreateTableStatement(String name_table, 
                                LinkedHashMap<String, String> columns, 
                                List<String> primaryKey, 
                                List<String> clusterKey, 
                                List<MetaProperty> properties, 
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

    public boolean isKeyspaceInc() {
        return keyspaceInc;
    }

    public void setKeyspaceInc(boolean keyspaceInc) {
        this.keyspaceInc = keyspaceInc;
    }

    public String getKeyspace() {
        return keyspace;
    }

    public void setKeyspace(String keyspace) {
        this.keyspace = keyspace;
    }        
            
    public boolean isWithProperties() {
        return withProperties;
    }

    public void setWithProperties(boolean withProperties) {
        this.withProperties = withProperties;
    }
    public int getColumnNumberPK() {
        return columnNumberPK;
    }

    public void setColumnNumberPK(int columnNumberPK) {
        this.columnNumberPK = columnNumberPK;
    }
    
    public boolean isWithClusterKey() {
        return withClusterKey;
    }

    public void setWithClusterKey(boolean withClusterKey) {
        this.withClusterKey = withClusterKey;
    }
    
    public boolean isIfNotExists() {
        return ifNotExists;
    }

    public void setIfNotExists(boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }

    public int getType_Primary_Key() {
        return Type_Primary_Key;
    }

    public void setType_Primary_Key(int Type_Primary_Key) {
        this.Type_Primary_Key = Type_Primary_Key;
    }

    public List<MetaProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<MetaProperty> properties) {
        this.properties = properties;
    }

    public List<String> getClusterKey() {
        return clusterKey;
    }

    public void setClusterKey(List<String> clusterKey) {
        this.clusterKey = clusterKey;
    }

    public List<String> getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(List<String> primaryKey) {
        this.primaryKey = primaryKey;
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
                Set keySet = columns.keySet();
                int i = 0;
                sb.append(" (");
                
                for (Iterator it = keySet.iterator();it.hasNext();){
                    String key = (String) it.next();
                    String vp= columns.get(key);
                    if (i==0) sb.append(key).append(" ").append(vp).append(" PRIMARY KEY");
                    else sb.append(key).append(" ").append(vp);
                    i++;
                    if (it.hasNext()) sb.append(", ");
                    else sb.append(")");                      
                }         
            }break;
                
            case 2: {
                Set keySet = columns.keySet();
                int i = 0;
                sb.append(" (");
                for (Iterator it = keySet.iterator();it.hasNext();){
                    String key = (String) it.next();
                    String vp= columns.get(key);
                    if (i == columnNumberPK) sb.append(key).append(" ").append(vp).append(" PRIMARY KEY");
                    else sb.append(key).append(" ").append(vp);
                    i++;
                    if (it.hasNext()) sb.append(", ");
                    else sb.append(")");  
                }
            }break;
            case 3: {
                Set keySet = columns.keySet();
                int i = 0;
                sb.append(" (");
                for (Iterator it = keySet.iterator();it.hasNext();){
                    String key = (String) it.next();
                    String vp= columns.get(key);
                    sb.append(key).append(" ").append(vp).append(", ");
                    i++;
                    
                }
                
                sb.append("PRIMARY KEY (");
                int j=0;
                for (Iterator it = primaryKey.iterator();it.hasNext();){
                    String key = (String) it.next();
                    if (j== 0) sb.append(key);
                    else sb.append(", ").append(key);
                    j++;
                    if (!it.hasNext()) sb.append("))");
                }
            
            }break;
                
            case 4: {
                Set keySet = columns.keySet();
                int i = 0;
                sb.append(" (");
                for (Iterator it = keySet.iterator();it.hasNext();){
                    String key = (String) it.next();
                    String vp= columns.get(key);
                    sb.append(key).append(" ").append(vp).append(", ");
                    i++;
                    
                }
                sb.append("PRIMARY KEY ((");
                int j=0;
                for (Iterator it = primaryKey.iterator();it.hasNext();){
                    String key = (String) it.next();
                    if (j== 0) sb.append(key);
                    else sb.append(", ").append(key);
                    j++;
                    if (!it.hasNext()) sb.append(")");
                }
                //System.out.println(withClusterKey);
                if (withClusterKey){
                    for (Iterator it = clusterKey.iterator();it.hasNext();){
                        String key = (String) it.next();
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
        /*
        Set keySet = properties.keySet();
        //if (withProperties) sb.append(" with:\n\t");
        if (withProperties) sb.append(" with");
        for (Iterator it = keySet.iterator(); it.hasNext();) {
            String key = (String) it.next();
            ValueProperty vp = properties.get(key);
            //sb.append(key).append(": ").append(String.valueOf(vp)).append("\n\t");
            sb.append(" ").append(key).append("=").append(String.valueOf(vp));
            if(it.hasNext()) sb.append(" AND");
        }
        */
        return sb.toString();    
    }

    /** {@inheritDoc} */
    @Override
    public MetaResult validate(MetadataManager metadata, String targetKeyspace) {
        MetaResult result = validateKeyspaceAndTable(metadata, targetKeyspace);
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
     * @return A {@link com.stratio.meta.common.result.MetaResult} with the validation result.
     */
    private MetaResult validateKeyspaceAndTable(MetadataManager metadata, String targetKeyspace){
        MetaResult result = new MetaResult();
        //Get the effective keyspace based on the user specification during the create
        //sentence, or taking the keyspace in use in the user session.
        String effectiveKeyspace = targetKeyspace;
        if(keyspaceInc){
            effectiveKeyspace = keyspace;
        }

        //Check that the keyspace exists, and that the table does not exits.
        if(effectiveKeyspace == null || effectiveKeyspace.length() == 0){
            result.setErrorMessage("Target keyspace missing or no keyspace has been selected.");
        }else{
            KeyspaceMetadata ksMetadata = metadata.getKeyspaceMetadata(effectiveKeyspace);
            if(ksMetadata == null){
                result.setErrorMessage("Keyspace " + effectiveKeyspace + " does not exists.");
            }else {
                TableMetadata tableMetadata = metadata.getTableMetadata(effectiveKeyspace, tablename);
                if (tableMetadata != null && !ifNotExists) {
                    result.setErrorMessage("Table " + tablename + " already exists.");
                }
            }

        }
        return result;
    }

    /**
     * Validate that the primary key is created and uses a set
     * of existing columns. The same checks are applied to the clustering
     * key if it exists.
     * @return A {@link com.stratio.meta.common.result.MetaResult} with the validation result.
     */
    private MetaResult validateColumns(){
        MetaResult result = new MetaResult();
        //The columns in the primary key must be declared.
        for (String pk : primaryKey) {
            if(!columns.containsKey(pk)){
                result.setErrorMessage("Missing declaration for Primary Key column " + pk);
            }
        }

        //The columns in the clustering key must be declared and not part of the primary key.
        for(String ck : clusterKey){
            if(!columns.containsKey(ck)){
                result.setErrorMessage("Missing declaration for Clustering Key column " + ck);
            }
            if(primaryKey.contains(ck)){
               result.setErrorMessage("Column " + ck + " found as part of primary and clustering key.");
            }
        }

        String [] supported = {"BIGINT", "BOOLEAN", "COUNTER", "DOUBLE", "FLOAT", "INT", "VARCHAR"};
        Set<String> supportedColumns = new HashSet<String>(Arrays.asList(supported));
        for(String c : columns.keySet()){
            if(!supportedColumns.contains(columns.get(c).toUpperCase()) || c.toLowerCase().startsWith("stratio")){
                result.setErrorMessage("Column " + c + " with datatype " + columns.get(c) + " not supported.");
            }
        }

        return result;
    }

    /**
     * Validate the semantics of the ephemeral properties.
     * @return A {@link com.stratio.meta.common.result.MetaResult} with the validation result.
     */
    private MetaResult validateProperties(){
        MetaResult result = new MetaResult();
        if(withProperties){
            for(MetaProperty property: properties){
                if(property.getType() == MetaProperty.TYPE_NAME_VALUE){
                    PropertyNameValue propertyNameValue = (PropertyNameValue) property;
                    // If property ephemeral is present, it must be a boolean type
                    if(propertyNameValue.getName().equalsIgnoreCase("ephemeral")){
                        if(propertyNameValue.getVp().getType() != ValueProperty.TYPE_BOOLEAN){
                            result.setErrorMessage("Property 'ephemeral' must be a boolean");
                        }
                        break;
                        // If property ephemeral_tuples is present, it must be a integer type
                    } else if(propertyNameValue.getName().equalsIgnoreCase("ephemeral_tuples")){
                        if(propertyNameValue.getVp().getType() != ValueProperty.TYPE_BOOLEAN){
                            result.setErrorMessage("Property 'ephemeral' must be a boolean");
                        }
                        break;
                        // If property ephemeral_persist_on is present, it must be a string type
                    } else if(propertyNameValue.getName().equalsIgnoreCase("ephemeral_persist_on")){
                        if(propertyNameValue.getVp().getType() != ValueProperty.TYPE_BOOLEAN){
                            result.setErrorMessage("Property 'ephemeral_persist_on' must be a string");
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
        if(!cqlString.contains(" WITH ")){
            return cqlString;
        }
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<cqlString.length(); i++){
            char c = cqlString.charAt(i);
            if(c=='{'){
                sb.append("{");
                int newI = cqlString.indexOf("}", i);
                String insideBracket = cqlString.substring(i+1, newI);
                insideBracket = insideBracket.replace(":", " ");
                insideBracket = insideBracket.replace(",", " ");
                //System.out.println("|"+insideBracket+"|");
                boolean wasChanged = true;
                while(wasChanged){
                    int before = insideBracket.length();
                    insideBracket = insideBracket.replace("  ", " ");
                    int after = insideBracket.length();
                    if(before==after){
                        wasChanged = false;
                    }
                }
                //System.out.println("|"+insideBracket+"|");
                insideBracket = insideBracket.trim();
                String[] strs = insideBracket.split(" ");
                for(int j=0; j<strs.length; j++){
                    String currentStr = strs[j];
                    if(currentStr.matches("[0123456789.]+")){
                        sb.append(strs[j]);
                    } else {
                        sb.append("\'").append(strs[j]).append("\'");
                    }
                    if(j % 2 == 0){
                        sb.append(": ");
                    } else {
                        if(j<(strs.length-1)){
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
    
//    @Override
//    public String parseResult(ResultSet resultSet) {
//        //return "\t"+resultSet.toString();
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
    public Tree getPlan() {
        //Check ifNotExists
        return new Tree();
    }
    
}
