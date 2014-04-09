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

import com.datastax.driver.core.*;
import com.stratio.meta.common.data.DeepResultSet;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.metadata.CustomIndexMetadata;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.structures.FloatProperty;
import com.stratio.meta.core.structures.IdentifierProperty;
import com.stratio.meta.core.structures.IndexType;
import com.stratio.meta.core.structures.ValueProperty;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.ParserUtils;
import com.stratio.meta.core.utils.Tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

    /**
     * Table metadata cached on the validate function.
     */
    private transient TableMetadata _metadata = null;

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
        Result result = validateKeyspaceAndTable(metadata, targetKeyspace, _keyspaceInc, _keyspace, _tablename);

        String effectiveKeyspace = targetKeyspace;
        if(_keyspaceInc){
            effectiveKeyspace = _keyspace;
        }
        TableMetadata tableMetadata = null;
        if(!result.hasError()) {
            tableMetadata = metadata.getTableMetadata(effectiveKeyspace, _tablename);
            _metadata = tableMetadata;
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
     * Generate the set of Lucene options required to create an index.
     * @return The set of options.
     */
    public HashMap<ValueProperty, ValueProperty> generateLuceneOptions(){
        HashMap<ValueProperty, ValueProperty> result = new HashMap<>();

        // CREATE CUSTOM INDEX demo_banks
        // ON  demo.banks (lucene)
        // USING 'org.apache.cassandra.db.index.stratio.RowIndex'
        // WITH OPTIONS = {
        //     'refresh_seconds':'1',
        //     'num_cached_filters':'1',
        //     'ram_buffer_mb':'32',
        //     'max_merge_mb':'5',
        //     'max_cached_mb':'30',
        //     'schema':
        //         '{default_analyzer:"org.apache.lucene.analysis.standard.StandardAnalyzer",
        //          fields:{ip: {type:"inet"}, bytes: {type:"bytes"}, ... , decimal_digits: 50}}}'
        //  };
        //TODO: Read parameters from default configuration and merge with the user specification.
        result.put(new IdentifierProperty("refresh_seconds"), new FloatProperty(1));
        result.put(new IdentifierProperty("num_cached_filters"), new FloatProperty(1));
        result.put(new IdentifierProperty("ram_buffer_mb"), new FloatProperty(32));
        result.put(new IdentifierProperty("max_merge_mb"), new FloatProperty(5));
        result.put(new IdentifierProperty("max_cached_mb"), new FloatProperty(30));
        result.put(new IdentifierProperty("schema"), new IdentifierProperty(generateLuceneSchema()));

        return result;
    }

    /**
     * Generate the Lucene options schema that corresponds with the selected column.
     * @return The Lucene Schema.
     */
    protected String generateLuceneSchema(){
        StringBuilder sb = new StringBuilder();
        sb.append("{default_analyzer:\"org.apache.lucene.analysis.standard.StandardAnalyzer\",");
        sb.append("fields:{");
        //Iterate throught the columns.

        for(String column : _targetColumn){

            DataType type = _metadata.getColumn(column).getType();
/*
            DataType.Name.BIGINT.toString();
            DataType.Name.BIGINT.name();

            switch (_metadata.getColumn(column).getType().getName().name()){
                case DataType.Name.ASCII.name():
            }*/
        }

        sb.append("}}");
        return sb.toString();
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
    public DeepResultSet executeDeep() {
        return new DeepResultSet();
    }

    @Override
    public Tree getPlan() {
        Tree result = new Tree();

        //Add CREATE INDEX as the root.
        result.addChild(new Tree(new MetaStep(MetaPath.CASSANDRA, translateToCQL())));
        //Add alter table as leaf if LUCENE index is selected.
        if(IndexType.LUCENE.equals(_type)){
            StringBuilder alterStatement = new StringBuilder("ALTER TABLE ");
            if(_keyspaceInc){
                alterStatement.append(_keyspace);
                alterStatement.append(".");
            }
            alterStatement.append("ADD stratio_lucene");
            alterStatement.append(_name);
            alterStatement.append(" TEXT;");
            System.out.println("CreateIndexStatement.getPlan: " + alterStatement);
            result.getChild(0).addChild(new Tree(new MetaStep(MetaPath.CASSANDRA, alterStatement.toString())));
        }

        return result;
    }
    
}
