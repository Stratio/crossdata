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
import com.stratio.meta.common.ask.Query;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.metadata.CustomIndexMetadata;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.structures.*;
import com.stratio.meta.core.utils.DeepResult;
import com.stratio.meta.core.utils.ParserUtils;
import com.stratio.meta.core.utils.Tree;

import java.util.*;
import java.util.Map.Entry;

/**
 * Create index statement of the META language. This class recognizes the following syntax:
 * <p>
 * CREATE {@link IndexType} INDEX (IF NOT EXISTS)? {@literal <index_name>}
 * ON {@literal <tablename>} ( {@literal <identifier> , ..., <identifier>})
 * ( USING {@literal <index_class>} )? ( WITH OPTIONS ( key_1=value_1 AND ... AND key_n=value_n) )?;
 */
public class CreateIndexStatement extends MetaStatement {	
	
    private boolean _keyspaceInc = false;
    private String _keyspace = null;
    private IndexType _type = null;
    private boolean _createIfNotExists = false;
    private String _name = null;
    private String _tablename = null;
    private ArrayList<String> _targetColumn = null;
    private String _usingClass = null;
    private HashMap<ValueProperty, ValueProperty> _options = null;

    public CreateIndexStatement(){
        this.command = false;
        _targetColumn = new ArrayList<>();
        _options = new HashMap<>();
    }

    public void setIndexType(String type){
        _type = IndexType.valueOf(type);
    }

    public void setCreateIfNotExists(){
        _createIfNotExists = true;
    }

    public boolean isKeyspaceInc() {
        return _keyspaceInc;
    }

    public void setKeyspaceInc(boolean _keyspaceInc) {
        this._keyspaceInc = _keyspaceInc;
    }

    public String getKeyspace() {
        return _keyspace;
    }

    public void setKeyspace(String _keyspace) {
        this._keyspace = _keyspace;
    }

    public IndexType getType() {
        return _type;
    }

    public void setType(IndexType _type) {
        this._type = _type;
    }

    public boolean isCreateIfNotExists() {
        return _createIfNotExists;
    }

    public void setCreateIfNotExists(boolean _createIfNotExists) {
        this._createIfNotExists = _createIfNotExists;
    }

    public ArrayList<String> getTargetColumn() {
        return _targetColumn;
    }

    public void setTargetColumn(ArrayList<String> _targetColumn) {
        this._targetColumn = _targetColumn;
    }	        
        
    public void setName(String name){
        if(name.contains(".")){
            String[] ksAndTablename = name.split("\\.");
            _keyspace = ksAndTablename[0];
            name = ksAndTablename[1];
            _keyspaceInc = true;
        }
        _name = name;
    }

    public String getName(){
            return _name;
    }

    public void setTablename(String tablename){
        if(tablename.contains(".")){
            String[] ksAndTablename = tablename.split("\\.");
            _keyspace = ksAndTablename[0];
            tablename = ksAndTablename[1];
            _keyspaceInc = true;
        }
        _tablename = tablename;

    }

    public void addColumn(String column){
        _targetColumn.add(column);
    }

    public void setUsingClass(String using){
        _usingClass = using;
    }

    public void addOption(ValueProperty key, ValueProperty value){
        _options.put(key, value);
    }

    public HashMap<ValueProperty, ValueProperty> getOptions(){
        return _options;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CREATE ");
        //if(_type == IndexType.HASH){
        //    sb.append("CUSTOM");
        //} else {
            sb.append(_type);
        //}
        sb.append(" INDEX ");
        if(_createIfNotExists){
                sb.append("IF NOT EXISTS ");
        }

        if(_name != null){
            sb.append(_name).append(" ");
        }
        sb.append("ON ");
        if(_keyspaceInc){
            sb.append(_keyspace).append(".");
        }
        sb.append(_tablename);
        sb.append(" (").append(ParserUtils.stringList(_targetColumn, ", ")).append(")");
        if(_usingClass != null){
                sb.append(" USING ");
                sb.append(_usingClass);
        }
        if(_options.size() > 0){
            //sb.append(" WITH OPTIONS ");
            sb.append(" WITH OPTIONS = {");
            Iterator<Entry<ValueProperty, ValueProperty>> entryIt = _options.entrySet().iterator();
            Entry<ValueProperty, ValueProperty> e;
            while(entryIt.hasNext()){
                    e = entryIt.next();
                    //sb.append(e.getKey()).append(" : ").append("'").append(e.getValue()).append("'");
                    sb.append(e.getKey()).append(": ").append(e.getValue());
                    if(entryIt.hasNext()){
                            sb.append(", ");
                    }
            }
            sb.append("}");
        }
        
        return sb.toString();
    }

    /** {@inheritDoc} */
    @Override
    public Result validate(MetadataManager metadata, String targetKeyspace) {

        //Validate target table
        Result result = validateKeyspaceAndTable(metadata, targetKeyspace);

        String effectiveKeyspace = targetKeyspace;
        if(_keyspaceInc){
            effectiveKeyspace = _keyspace;
        }
        TableMetadata tableMetadata = null;
        if(!result.hasError()) {
            tableMetadata = metadata.getTableMetadata(effectiveKeyspace, _tablename);
            result = validateOptions(effectiveKeyspace, tableMetadata);
        }

        //Validate index name if not exists
        if(!result.hasError()){
            if(_name != null && _name.toLowerCase().startsWith("stratio")){
                result= QueryResult.CreateFailQueryResult("Internal namespace stratio cannot be use on index name " + _name);
            }else {
                result = validateIndexName(metadata, tableMetadata);
            }
        }

        //Validate target columns
        if(!result.hasError()){
            result = validateSelectionColumns(tableMetadata);
        }

        return result;
    }

    /**
     * Validate that the target columns exists in the table.
     * @param tableMetadata The associated {@link com.datastax.driver.core.TableMetadata}.
     * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
     */
    private Result validateSelectionColumns(TableMetadata tableMetadata) {
        Result result = QueryResult.CreateSuccessQueryResult();

        for(String c : _targetColumn){
            if(c.toLowerCase().startsWith("stratio")){
                result=  QueryResult.CreateFailQueryResult("Internal column " + c + " cannot be part of the WHERE clause.");
            }else if(tableMetadata.getColumn(c) == null){
                result= QueryResult.CreateFailQueryResult("Column " + c + " does not exists in table " + tableMetadata.getName());
            }
        }

        return result;
    }

    private Result validateIndexName(MetadataManager metadata, TableMetadata tableMetadata){
        Result result = QueryResult.CreateSuccessQueryResult();
        String index_name = _name;
        if(IndexType.LUCENE.equals(_type)){
            index_name = "stratio_lucene_" + _name;
        }
        List<CustomIndexMetadata> allIndex = metadata.getTableIndex(tableMetadata);

        boolean found = false;
        for(int index = 0; index < allIndex.size() && !found; index++){
            if(allIndex.get(index).getIndexName().equalsIgnoreCase(index_name)){
                found = true;
            }
        }
        if(found && !_createIfNotExists){
            result= QueryResult.CreateFailQueryResult("Index " + _name + " already exists in table " + _tablename);
        }
        return result;
    }

    private Result validateOptions(String effectiveKeyspace, TableMetadata metadata) {
        Result result = QueryResult.CreateSuccessQueryResult();
        if(_options.size() > 0){
            result= QueryResult.CreateFailQueryResult("WITH OPTIONS clause not supported in index creation.");
        }
        if(!_createIfNotExists && IndexType.LUCENE.equals(_type)) {
            Iterator<ColumnMetadata> columns = metadata.getColumns().iterator();
            boolean found = false;
            ColumnMetadata column = null;
            while (!found && columns.hasNext()) {
                column = columns.next();
                if (column.getName().startsWith("stratio_lucene")) {
                    found = true;
                }
            }
            if (found) {
                result= QueryResult.CreateFailQueryResult("Cannot create index: A Lucene index already exists on table " + effectiveKeyspace + "."
                        + metadata.getName() + ". Use DROP INDEX " + column.getName().replace("stratio_lucene_", "") + "; to remove the index.");
            }
        }
        return result;
    }

    /**
     * Validate that a valid keyspace and table is present.
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
        if(_keyspaceInc){
            effectiveKeyspace = _keyspace;
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
        // EXAMPLE:
        // META: CREATE LUCENE INDEX demo_banks ON demo.banks(lucene) USING org.apache.cassandra.db.index.stratio.RowIndex
        // WITH OPTIONS schema = '{default_analyzer:"org.apache.lucene.analysis.standard.StandardAnalyzer", fields: {day:
        // {type: "date", pattern: "yyyy-MM-dd"}, key: {type:"uuid"}}}';
        // CQL: CREATE CUSTOM INDEX demo_banks ON demo.banks (lucene) USING 'org.apache.cassandra.db.index.stratio.RowIndex'
        // WITH OPTIONS = {'schema' : '{default_analyzer:"org.apache.lucene.analysis.standard.StandardAnalyzer",
        // fields: {day: {type: "date", pattern: "yyyy-MM-dd"}, key: {type:"uuid"}}}'}
        
        String cqlString = this.toString().replace(" DEFAULT ", " ");
        if(cqlString.contains(" LUCENE ")){
            cqlString = this.toString().replace("CREATE LUCENE ", "CREATE CUSTOM ");
        }        
        if(cqlString.contains("USING")){
            cqlString = cqlString.replace("USING ", "USING '");
            if(cqlString.contains("WITH ")){
                cqlString = cqlString.replace(" WITH ", "' WITH ");
            } /*else {
                cqlString = cqlString.replace(";", "';");
            }*/
        }
        if(cqlString.contains("OPTIONS")){
            cqlString = cqlString.replace("OPTIONS", "OPTIONS = { '");
            cqlString = cqlString.concat("}");
            cqlString = cqlString.replaceAll("='", "'='");
            cqlString = cqlString.replaceAll("= '", "' = '");
            cqlString = cqlString.replaceAll("' ", "'");
            cqlString = cqlString.replaceAll(" '", "'");
            cqlString = cqlString.replaceAll("USING'", "USING '");
            cqlString = cqlString.replaceAll("'='", "' : '");
            cqlString = cqlString.replaceAll("'= '", "' : '");
            cqlString = cqlString.replaceAll("' ='", "' : '");
            cqlString = cqlString.replaceAll("' = '", "' : '");
        }
        return cqlString;
    }
        


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
