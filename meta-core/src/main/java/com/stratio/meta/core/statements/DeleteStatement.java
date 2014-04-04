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

import java.util.ArrayList;
import java.util.List;

import com.datastax.driver.core.TableMetadata;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.structures.*;
import com.stratio.meta.core.utils.ParserUtils;
import com.stratio.meta.core.utils.DeepResult;
import com.stratio.meta.core.utils.Tree;
import java.util.Arrays;

/**
 * Delete a set of rows. This class recognizes the following syntax:
 * <p>
 * DELETE ( {@literal <column>}, ( ',' {@literal <column>} )*)? FROM {@literal <tablename>}
 * WHERE {@literal <where_clause>};
 */
public class DeleteStatement extends MetaStatement {
	
    private ArrayList<String> _targetColumn = null;
    private boolean keyspaceInc = false;
    private String keyspace;
    private String _tablename = null;
    private List<Relation> _whereClauses;

    public DeleteStatement(){
        this.command = false;
        _targetColumn = new ArrayList<>();
        _whereClauses = new ArrayList<>();
    }

    public ArrayList<String> getTargetColumn() {
        return _targetColumn;
    }

    public void setTargetColumn(ArrayList<String> _targetColumn) {
        this._targetColumn = _targetColumn;
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

    public List<Relation> getWhereClauses() {
        return _whereClauses;
    }

    public void setWhereClauses(List<Relation> _whereClauses) {
        this._whereClauses = _whereClauses;
    }        

    public void addColumn(String column){
            _targetColumn.add(column);
    }

    public void setTablename(String tablename){
        if(tablename.contains(".")){
            String[] ksAndTablename = tablename.split("\\.");
            keyspace = ksAndTablename[0];
            tablename = ksAndTablename[1];
            keyspaceInc = true;
        }
        _tablename = tablename;
    }

    public void addRelation(Relation relation){
            _whereClauses.add(relation);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("DELETE ");
        if(_targetColumn.size() > 0){
            sb.append("(").append(ParserUtils.stringList(_targetColumn, ", ")).append(") ");
        }
        sb.append("FROM ");
        if(keyspaceInc){
            sb.append(keyspace).append(".");
        } 
        sb.append(_tablename);
        if(_whereClauses.size() > 0){
        	sb.append(" WHERE ");
        	sb.append(ParserUtils.stringList(_whereClauses, " AND "));
        }
        return sb.toString();
    }

    /** {@inheritDoc} */
    @Override
    public Result validate(MetadataManager metadata, String targetKeyspace) {
        Result result = validateKeyspaceAndTable(metadata, targetKeyspace);

        String effectiveKeyspace = targetKeyspace;
        if(keyspaceInc){
            effectiveKeyspace = keyspace;
        }
        TableMetadata tableMetadata = null;

        if(!result.hasError()){
            tableMetadata = metadata.getTableMetadata(effectiveKeyspace, _tablename);
            result = validateSelectionColumns(tableMetadata);
        }
        if(!result.hasError()){
            result = validateWhereClause(tableMetadata);
        }

        return result;

    }

    /**
     * Validate that the columns specified in the select are valid by checking
     * that the selection columns exists in the table.
     * @param tableMetadata The associated {@link com.datastax.driver.core.TableMetadata}.
     * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
     */
    private Result validateWhereClause(TableMetadata tableMetadata){
        Result result = QueryResult.CreateSuccessQueryResult();
        for(Relation relation : _whereClauses){
            if(Relation.TYPE_COMPARE == relation.getType()) {
                //Check comparison, =, >, <, etc.
                RelationCompare rc = RelationCompare.class.cast(relation);
                String column = rc.getIdentifiers().get(0);
                //System.out.println("column: " + column);
                if (tableMetadata.getColumn(column) == null) {
                    result= QueryResult.CreateFailQueryResult("Column " + column + " does not exists in table " + tableMetadata.getName());
                }

                Term t = Term.class.cast(rc.getTerms().get(0));
                ColumnMetadata cm = tableMetadata.getColumn(column);
                if (cm != null){
                    if (!tableMetadata.getColumn(column)
                            .getType().asJavaClass().equals(t.getTermClass())) {
                        result= QueryResult.CreateFailQueryResult("Column " + column
                                + " of type " + tableMetadata.getColumn(rc.getIdentifiers().get(0))
                                .getType().asJavaClass()
                                + " does not accept " + t.getTermClass()
                                + " values (" + t.toString() + ")");
                    }

                    if (Boolean.class.equals(tableMetadata.getColumn(column)
                            .getType().asJavaClass())) {
                        boolean supported = true;
                        switch (rc.getOperator()) {
                            case ">":
                                supported = false;
                                break;
                            case "<":
                                supported = false;
                                break;
                            case ">=":
                                supported = false;
                                break;
                            case "<=":
                                supported = false;
                                break;
                        }
                        if (!supported) {
                            result= QueryResult.CreateFailQueryResult("Operand " + rc.getOperator() + " not supported" +
                                    " for column " + column + ".");
                        }
                    }
                }else {
                    result= QueryResult.CreateFailQueryResult("Column " + column + " not found in table " + _tablename);
                }

            }else if(Relation.TYPE_IN == relation.getType()){
                //TODO: Check IN relation
                result= QueryResult.CreateFailQueryResult("IN clause not supported.");
            }else if(Relation.TYPE_TOKEN == relation.getType()){
                //TODO: Check IN relation
                result= QueryResult.CreateFailQueryResult("TOKEN function not supported.");
            }else if(Relation.TYPE_BETWEEN == relation.getType()){
                //TODO: Check IN relation
                result= QueryResult.CreateFailQueryResult("BETWEEN clause not supported.");
            }
        }

        return result;
    }

    /**
     * Validate that the columns specified in the select are valid by checking
     * that the selection columns exists in the table.
     * @param tableMetadata The associated {@link com.datastax.driver.core.TableMetadata}.
     * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
     */
    private Result validateSelectionColumns(TableMetadata tableMetadata) {
        Result result = QueryResult.CreateSuccessQueryResult();

        for(String c : _targetColumn){
            if(c.toLowerCase().startsWith("stratio")){
                result= QueryResult.CreateFailQueryResult("Internal column " + c + " cannot be part of the WHERE " +
                        "clause.");
            }else if(tableMetadata.getColumn(c) == null){
                result= QueryResult.CreateFailQueryResult("Column " + c + " does not exists in table " +
                        tableMetadata.getName());
            }
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
        Result result =QueryResult.CreateSuccessQueryResult();
        //Get the effective keyspace based on the user specification during the create
        //sentence, or taking the keyspace in use in the user session.
        String effectiveKeyspace = targetKeyspace;
        if(keyspaceInc){
            effectiveKeyspace = keyspace;
        }

        //Check that the keyspace and table exists.
        if(effectiveKeyspace == null || effectiveKeyspace.length() == 0){
            result= QueryResult.CreateFailQueryResult("Target keyspace missing or no keyspace has been selected.");
        }else{
            KeyspaceMetadata ksMetadata = metadata.getKeyspaceMetadata(effectiveKeyspace);
            if(ksMetadata == null){
                result= QueryResult.CreateFailQueryResult("Keyspace " + effectiveKeyspace + " does not exists.");
            }else {
                TableMetadata tableMetadata = metadata.getTableMetadata(effectiveKeyspace, _tablename);
                if (tableMetadata == null) {
                    result= QueryResult.CreateFailQueryResult("Table " + _tablename + " does not exists.");
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
        return this.toString();
    }
    
//    @Override
//    public String parseResult(ResultSet resultSet) {
//        return "\t"+resultSet.toString();
//    }
    
    @Override
    public Statement getDriverStatement() {
        return null;
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
