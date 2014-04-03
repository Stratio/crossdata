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

import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.TableMetadata;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Using;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.structures.Option;
import com.stratio.meta.core.structures.Term;
import com.stratio.meta.core.structures.ValueCell;
import com.stratio.meta.core.utils.DeepResult;
import com.stratio.meta.core.utils.ParserUtils;
import com.stratio.meta.core.utils.Tree;
import org.apache.log4j.Logger;

import java.util.*;

public class InsertIntoStatement extends MetaStatement {

    public static final int TYPE_SELECT_CLAUSE = 1;
    public static final int TYPE_VALUES_CLAUSE = 2;
    
    private final Logger logger = Logger.getLogger(InsertIntoStatement.class);
    
    private boolean keyspaceInc = false;
    private String keyspace;
    private String tableName;
    private List<String> ids;
    private SelectStatement selectStatement;
    private List<ValueCell> cellValues;
    private boolean ifNotExists;
    private boolean optsInc;
    private List<Option> options;
    private int typeValues;

    public InsertIntoStatement(String tableName, List<String> ids,
                               SelectStatement selectStatement, 
                               List<ValueCell> cellValues, 
                               boolean ifNotExists,
                               boolean optsInc,
                               List<Option> options, 
                               int typeValues) {
        this.command = false;
        this.tableName = tableName;
        if(tableName.contains(".")){
            String[] ksAndTableName = tableName.split("\\.");
            keyspace = ksAndTableName[0];
            this.tableName = ksAndTableName[1];
            keyspaceInc = true;
        }
        this.ids = ids;
        this.selectStatement = selectStatement;
        this.cellValues = cellValues;
        this.ifNotExists = ifNotExists;
        this.optsInc = optsInc;
        this.options = options;
        this.typeValues = typeValues;
    }   

    public InsertIntoStatement(String tableName,
                               List<String> ids, 
                               SelectStatement selectStatement, 
                               boolean ifNotExists, 
                               List<Option> options) {
        this(tableName, ids, selectStatement, null, ifNotExists, true, options, 1);
    }        

    public InsertIntoStatement(String tableName,
                               List<String> ids, 
                               List<ValueCell> cellValues, 
                               boolean ifNotExists, 
                               List<Option> options) {
        this(tableName, ids, null, cellValues, ifNotExists, true, options, 2);
    }        
    
    public InsertIntoStatement(String tableName,
                               List<String> ids, 
                               SelectStatement selectStatement, 
                               boolean ifNotExists) {
        this(tableName, ids, selectStatement, null, ifNotExists, false, null, 1);
    }        

    public InsertIntoStatement(String tableName,
                               List<String> ids, 
                               List<ValueCell> cellValues, 
                               boolean ifNotExists) {
        this(tableName, ids, null, cellValues, ifNotExists, false, null, 2);
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

    public boolean isOptsInc() {
        return optsInc;
    }

    public void setOptsInc(boolean optsInc) {
        this.optsInc = optsInc;
    }        
    
    public List<String> getIds() {
        return ids;
    }

    public String getId(int index) {
        return ids.get(index);
    }
    
    public void setIds(List<String> ids) {
        this.ids = ids;
    }
    
    public void addId(String id) {
        ids.add(id);
    }
    
    public void removeId(String id){
        ids.remove(id);
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        if(tableName.contains(".")){
            String[] ksAndTablename = tableName.split("\\.");
            keyspace = ksAndTablename[0];
            tableName = ksAndTablename[1];
            keyspaceInc = true;
        }
        this.tableName = tableName;
    }

    public SelectStatement getSelectStatement() {
        return selectStatement;
    }

    public void setSelectStatement(SelectStatement selectStatement) {
        this.selectStatement = selectStatement;
    }

    public List<ValueCell> getCellValues() {
        return cellValues;
    }

    public ValueCell getCellValue(int index) {
        return cellValues.get(index);
    }
    
    public void setCellValues(List<ValueCell> cellValues) {
        this.cellValues = cellValues;
    }

    public void addCellValue(ValueCell valueCell) {
        cellValues.add(valueCell);
    }
    
    public void removeCellValue(ValueCell valueCell) {
        cellValues.remove(valueCell);
    }
    
    public boolean isIfNotExists() {
        return ifNotExists;
    }

    public void setIfNotExists(boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }

    public List<Option> getOptions() {
        return options;
    }

    public Option getOption(int n){
        return options.get(n);
    }
    
    public void setOptions(List<Option> options) {
        this.options = options;
    }
    
    public void addOption(Option option) {
        options.add(option);
    }
    
    public void remove(Option option) {
        options.remove(option);
    }

    public int getTypeValues() {
        return typeValues;
    }

    public void setTypeValues(int typeValues) {
        this.typeValues = typeValues;
    }        

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        if(keyspaceInc){
            sb.append(keyspace).append(".");
        }
        sb.append(tableName).append(" (");
        sb.append(ParserUtils.stringList(ids, ", ")).append(") ");
        if(typeValues == TYPE_SELECT_CLAUSE){
           sb.append(selectStatement.toString());
        } else {
           sb.append("VALUES (");
           sb.append(ParserUtils.stringList(cellValues, ", "));
           sb.append(")");
        }        
        if(ifNotExists){
            sb.append(" IF NOT EXISTS");            
        }
        if(optsInc){
            sb.append(" USING ");
            sb.append(ParserUtils.stringList(options, " AND "));
        }
        return sb.toString();
    }

    /** {@inheritDoc} */
    @Override
    public Result validate(MetadataManager metadata, String targetKeyspace) {
        Result result = QueryResult.CreateSuccessQueryResult();
        //Check that the table exists.

        String effectiveKeyspace = targetKeyspace;
        if(keyspaceInc){
            effectiveKeyspace = keyspace;
        }

        TableMetadata tableMetadata = null;
        //Check that the keyspace exists, and that the table does not exits.
        if(effectiveKeyspace == null || effectiveKeyspace.length() == 0){
            result = QueryResult.CreateFailQueryResult("Target keyspace missing or no keyspace has been selected.");
        }else{
            KeyspaceMetadata ksMetadata = metadata.getKeyspaceMetadata(effectiveKeyspace);
            if(ksMetadata == null){
                result = QueryResult.CreateFailQueryResult("Keyspace " + effectiveKeyspace + " does not exists.");
            }else {
                tableMetadata = metadata.getTableMetadata(effectiveKeyspace, tableName);
                if (tableMetadata != null && !ifNotExists) {
                    result = QueryResult.CreateFailQueryResult("Table " + tableName + " already exists.");
                }
            }
        }

        if(tableMetadata !=  null){
            if(typeValues == TYPE_SELECT_CLAUSE){
                result = QueryResult.CreateFailQueryResult("INSERT INTO with subqueries not supported.");
            }else {
                result = validateColumns(tableMetadata);
            }
        }

        return result;
    }

    /**
     * Check that the specified columns exist on the target table and that
     * the semantics of the assigned values match.
     * @param tableMetadata Table metadata associated with the target table.
     * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
     */
    private Result validateColumns(TableMetadata tableMetadata) {
        Result result = QueryResult.CreateSuccessQueryResult();

        //Validate target column names
        for(String c : ids){
            if(c.toLowerCase().startsWith("stratio")){
                result = QueryResult.CreateFailQueryResult("Cannot insert data into column " + c + " reserved for internal use.");
            }
        }
        if(!result.hasError()) {
            ColumnMetadata cm = null;
            if (cellValues.size() == ids.size()) {
                for (int index = 0; index < cellValues.size(); index++) {
                    cm = tableMetadata.getColumn(ids.get(index));
                    if (cm != null) {
                        Term t = Term.class.cast(cellValues.get(index));
                        if (!cm.getType().asJavaClass().equals(t.getTermClass())) {
                            result = QueryResult.CreateFailQueryResult("Column " + ids.get(index)
                                    + " of type " + cm.getType().asJavaClass()
                                    + " does not accept " + t.getTermClass()
                                    + " values (" + cellValues.get(index) + ")");
                        }
                    } else {
                        result = QueryResult.CreateFailQueryResult("Column " + ids.get(index) + " not found in " + tableMetadata.getName());
                    }
                }
            } else {
                result = QueryResult.CreateFailQueryResult("Number of columns and values does not match.");
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
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        if(keyspaceInc){
            sb.append(keyspace).append(".");
        }
        sb.append(tableName).append(" (");
        sb.append(ParserUtils.stringList(ids, ", "));
        sb.append(") ");        
        if(typeValues == TYPE_SELECT_CLAUSE){
           sb.append(selectStatement.toString());
        }
        if(typeValues == TYPE_VALUES_CLAUSE){
           sb.append("VALUES (");
           sb.append(ParserUtils.addSingleQuotesToString(ParserUtils.stringList(cellValues, ", "),","));           
           sb.append(")");
        }
        if(ifNotExists){
            sb.append(" IF NOT EXISTS");            
        }
        if(optsInc){
            sb.append(" USING ");
            sb.append(ParserUtils.stringList(options, " AND "));
        }
        return sb.append(";").toString();
    }

    @Override
    public Statement getDriverStatement() {
        if(this.typeValues == TYPE_SELECT_CLAUSE){
            return null;
        }
            
        Insert insertStmt;
        if(this.keyspaceInc){
            insertStmt = QueryBuilder.insertInto(this.keyspace, this.tableName);
        } else {
            insertStmt = QueryBuilder.insertInto(this.tableName);
        }
        Iterator iter = this.cellValues.iterator();
        for(String id: this.ids){
            ValueCell valueCell = (ValueCell) iter.next();
            try{
                if(valueCell.toString().matches("[0123456789.]+")){
                    insertStmt = insertStmt.value(id, Integer.parseInt(valueCell.toString()));
                } else if (valueCell.toString().contains("-")){
                        insertStmt = insertStmt.value(id, UUID.fromString(valueCell.toString()));
                } else {
                    insertStmt = insertStmt.value(id, valueCell.toString());
                }
            } catch(Exception ex){
                return null;
            }
        }
        
        if(this.ifNotExists){
            insertStmt = insertStmt.ifNotExists();
        }
        
        Insert.Options optionsStmt = null;
        if(this.optsInc){
            Using using = null;            
            for(Option option: this.options){
                if(option.getFixedOption() == Option.OPTION_PROPERTY){
                    if(option.getNameProperty().equalsIgnoreCase("ttl")){
                        if(using == null){
                            optionsStmt = insertStmt.using(QueryBuilder.ttl(Integer.parseInt(option.getProperties().toString())));
                        } else {
                            optionsStmt = optionsStmt.and(QueryBuilder.ttl(Integer.parseInt(option.getProperties().toString())));
                        }
                    } else if(option.getNameProperty().equalsIgnoreCase("timestamp")){
                        if(using == null){
                            optionsStmt = insertStmt.using(QueryBuilder.timestamp(Integer.parseInt(option.getProperties().toString())));
                        } else {
                            optionsStmt = optionsStmt.and(QueryBuilder.timestamp(Integer.parseInt(option.getProperties().toString())));
                        }
                    }
                }
            }
        }         
        if(optionsStmt==null){
            return insertStmt;
        }
        return optionsStmt;        
    }
    
    @Override
    public DeepResult executeDeep() {
        return new DeepResult("", new ArrayList<>(Arrays.asList("Not supported yet")));
    }
    
    @Override
    public Tree getPlan() {
        return new Tree();
    }
    
}
