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
import com.stratio.meta.core.structures.*;
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
 * Class that models a {@code CREATE INDEX} statement of the META language. This class recognizes the following syntax:
 * <p>
 * CREATE {@link IndexType} INDEX (IF NOT EXISTS)? {@literal <index_name>} </br>
 * ON {@literal <tablename>} ( {@literal <identifier> , ..., <identifier>}) </br>
 * ( USING {@literal <index_class>} )? ( WITH OPTIONS ( key_1=value_1 AND ... AND key_n=value_n) )?;
 */
public class CreateIndexStatement extends MetaStatement {

    /**
     * Whether the keyspace has been specified in the Create index statement or it should be taken from the
     * environment.
     */
    private boolean _keyspaceInc = false;

    /**
     * The keyspace specified in the create index statement.
     */
    private String _keyspace = null;

    /**
     * The {@link com.stratio.meta.core.structures.IndexType} to be created.
     */
    private IndexType _type = null;

    /**
     * Whether the index should be created only if not exists.
     */
    private boolean _createIfNotExists = false;

    /**
     * Determine whether the index should be created or not.
     */
    private boolean _createIndex = false;

    /**
     * The name of the index.
     */
    private String _name = null;

    /**
     * The name of the target table.
     */
    private String _tablename = null;

    /**
     * The list of columns covered by the index. Only one column is allowed for {@code DEFAULT} indexes.
     */
    private ArrayList<String> _targetColumn = null;

    /**
     * The name of the class that implements the secondary index.
     */
    private String _usingClass = null;

    /**
     * The map of options passed to the index during its creation.
     */
    private HashMap<ValueProperty, ValueProperty> _options = null;

    /**
     * Table metadata cached on the validate function.
     */
    private transient TableMetadata _metadata = null;

    /**
     * Class constructor.
     */
    public CreateIndexStatement(){
        this.command = false;
        _targetColumn = new ArrayList<>();
        _options = new HashMap<>();
    }

    /**
     * Set the type of index.
     * @param type The type from {@link com.stratio.meta.core.structures.IndexType}.
     */
    public void setIndexType(String type){
        _type = IndexType.valueOf(type);
    }

    /**
     * Set that the index should be created if not exists.
     */
    public void setCreateIfNotExists(){
        _createIfNotExists = true;
    }

    /**
     * Determine whether the keyspace have been included in the create index statement.
     * @return Whether it has been included.
     */
    public boolean isKeyspaceInc() {
        return _keyspaceInc;
    }

    /**
     * Set whether the keyspace has been specified in the create index statement.
     * @param _keyspaceInc The boolean value.
     */
    public void setKeyspaceInc(boolean _keyspaceInc) {
        this._keyspaceInc = _keyspaceInc;
    }

    /**
     * Get the keyspace specified in the create index statement.
     * @return The keyspace or null if not set.
     */
    public String getKeyspace() {
        return _keyspace;
    }

    /**
     * Set the keyspace specified in the create index statement.
     * @param _keyspace The name of the keyspace.
     */
    public void setKeyspace(String _keyspace) {
        this._keyspace = _keyspace;
    }

    /**
     * Get the type of index.
     * @return A {@link com.stratio.meta.core.structures.IndexType}.
     */
    public IndexType getType() {
        return _type;
    }

    /**
     * Set the type of index.
     * @param _type A {@link com.stratio.meta.core.structures.IndexType}.
     */
    public void setType(IndexType _type) {
        this._type = _type;
    }

    /**
     * If the IF NOT EXISTS clause has been specified.
     * @return Whether the index should be created only if not exists.
     */
    public boolean isCreateIfNotExists() {
        return _createIfNotExists;
    }

    /**
     * Set the value of the IF NOT EXISTS clause.
     * @param ifNotExists If it has been specified or not.
     */
    public void setCreateIfNotExists(boolean ifNotExists) {
        this._createIfNotExists = ifNotExists;
    }

    /**
     * Get the list of column indexed.
     * @return The list of columns.
     */
    public ArrayList<String> getTargetColumn() {
        return _targetColumn;
    }

    /**
     * Set the list of columns to be indexed.
     * @param targetColumn The list of columns.
     */
    public void setTargetColumn(ArrayList<String> targetColumn) {
        this._targetColumn = targetColumn;
    }

    /**
     * Set the name of the index.
     * @param name The name.
     */
    public void setName(String name){
        if(name.contains(".")){
            String[] ksAndTablename = name.split("\\.");
            _keyspace = ksAndTablename[0];
            name = ksAndTablename[1];
            _keyspaceInc = true;
        }
        _name = name;
    }

    /**
     * Get the index name.
     * @return The name.
     */
    public String getName(){
            return _name;
    }

    /**
     * Set the name of the target table.
     * @param tablename The name.
     */
    public void setTablename(String tablename){
        if(tablename.contains(".")){
            String[] ksAndTablename = tablename.split("\\.");
            _keyspace = ksAndTablename[0];
            tablename = ksAndTablename[1];
            _keyspaceInc = true;
        }
        _tablename = tablename;

    }

    /**
     * Add a column to the list of indexed columns.
     * @param column The name of the column.
     */
    public void addColumn(String column){
        _targetColumn.add(column);
    }

    /**
     * Set a USING class that implements the custom index.
     * @param using The qualified name of the class.
     */
    public void setUsingClass(String using){
        _usingClass = using;
    }

    /**
     * Add an options to the index.
     * @param key
     * @param value
     */
    public void addOption(ValueProperty key, ValueProperty value){
        _options.put(key, value);
    }

    /**
     * Get the map of options.
     * @return The map of options.
     */
    public HashMap<ValueProperty, ValueProperty> getOptions(){
        return _options;
    }

    /**
     * Get the name of the index. If a LUCENE index is to be created, the name of the index
     * is prepended with {@code stratio_lucene_}. If a name for the index is not specified, the index
     * will be named using the concatenation of the target column names.
     * @return The name of the index.
     */
    public String getIndexName(){
        String result = _name;
        if(_name == null){
            StringBuilder sb = new StringBuilder();
            if(IndexType.LUCENE.equals(_type)){
                sb.append("stratio_lucene_");
                sb.append(_tablename);
            }else {
                sb.append(_tablename);
                for (String c : _targetColumn) {
                    sb.append("_");
                    sb.append(c);
                }
                sb.append("_idx");
            }
            result = sb.toString();
        }else{
            result = _name;
            if(IndexType.LUCENE.equals(_type)){
                result = "stratio_lucene_" + _name;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CREATE ");
        sb.append(_type);
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
            sb.append(" WITH OPTIONS = {");
            Iterator<Entry<ValueProperty, ValueProperty>> entryIt = _options.entrySet().iterator();
            Entry<ValueProperty, ValueProperty> e;
            while(entryIt.hasNext()){
                    e = entryIt.next();
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

        //If the syntax is valid and we are dealing with a Lucene index, complete the missing fields.
        if(!result.hasError()
                && IndexType.LUCENE.equals(_type)
                && (_options.size()==0 || _usingClass == null)){
            _options.clear();
            _options.putAll(generateLuceneOptions());
            _usingClass = "org.apache.cassandra.db.index.stratio.RowIndex";

            System.out.println("Set Lucene options: " + this.toString());
        }
        if(result.hasError()) {
            System.out.println("validation: " + result.hasError() + " type: " + _type + " error: " + result.getErrorMessage());
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

    /**
     * Validate that the index name is valid an has not been previously used.
     * @param metadata The associated {@link com.stratio.meta.core.metadata.MetadataManager}.
     * @param tableMetadata The associated {@link com.datastax.driver.core.TableMetadata}.
     * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
     */
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
        }else{
            _createIndex = true;
        }
        return result;
    }

    /**
     * Validate the index options.
     * @param effectiveKeyspace The effective keyspace used in the validation process.
     * @param metadata The associated {@link com.datastax.driver.core.TableMetadata}.
     * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
     */
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
        result.put(new IdentifierProperty("'refresh_seconds'"), new IdentifierProperty("'1'"));
        result.put(new IdentifierProperty("'num_cached_filters'"), new IdentifierProperty("'0'"));
        result.put(new IdentifierProperty("'ram_buffer_mb'"), new IdentifierProperty("'32'"));
        result.put(new IdentifierProperty("'max_merge_mb'"), new IdentifierProperty("'5'"));
        result.put(new IdentifierProperty("'max_cached_mb'"), new IdentifierProperty("'30'"));
        result.put(new IdentifierProperty("'schema'"), new IdentifierProperty("'" + generateLuceneSchema() + "'"));

        return result;
    }

    /**
     * Generate the Lucene options schema that corresponds with the selected column.
     * @return The JSON representation of the Lucene schema.
     */
    protected String generateLuceneSchema(){
        StringBuilder sb = new StringBuilder();
        sb.append("{default_analyzer:\"org.apache.lucene.analysis.standard.StandardAnalyzer\",");
        sb.append("fields:{");

        //Iterate throught the columns.
        for(String column : _targetColumn){
            sb.append(column);
            sb.append(":");
            sb.append(getLuceneType(_metadata.getColumn(column).getType()));
            sb.append(",");
        }

        sb.append("}}");
        return sb.toString().replace(",}}", "}}");
    }

    /**
     * Get the Lucene type equivalent to a Cassandra data type.
     * @param type The {@link com.datastax.driver.core.DataType} to be converted.
     * @return The result or null if the conversion is not supported.
     */
    protected String getLuceneType(DataType type){
        String result = null;

        if(DataType.text().equals(type)
                || DataType.varchar().equals(type)
                || DataType.inet().equals(type)
                || DataType.ascii().equals(type)){
            result = "{type:\"string\"}";
        }else if(DataType.bigint().equals(type)
                || DataType.counter().equals(type)){
            result = "{type:\"long\"}";
        }else if(DataType.cboolean().equals(type)){
            result = "{type:\"boolean\"}";
        }else if(DataType.cdouble().equals(type)){
            result = "{type:\"double\"}";
        }else if(DataType.cfloat().equals(type)){
            result = "{type:\"float\"}";
        }else if(DataType.cint().equals(type)){
            result = "{type:\"integer\"}";
        }else if(DataType.uuid().equals(type)){
            result = "{type:\"uuid\"}";
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

        if(IndexType.LUCENE.equals(_type)){
            _targetColumn.clear();
            _targetColumn.add(getIndexName());
        }

        String cqlString = this.toString().replace(" DEFAULT ", " ");
        if(cqlString.contains(" LUCENE ")){
            cqlString = this.toString().replace("CREATE LUCENE ", "CREATE CUSTOM ");
        }

        if(_name == null){
            cqlString = cqlString.replace("INDEX ON", "INDEX " + getIndexName() + " ON");
        }

        if(cqlString.contains("USING")){
            cqlString = cqlString.replace("USING ", "USING '");
            if(cqlString.contains("WITH ")){
                cqlString = cqlString.replace(" WITH OPTIONS", "' WITH OPTIONS");
            }
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
    public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
        Tree result = new Tree();

        if(_createIndex) {
            //Add CREATE INDEX as the root.
            result.setNode(new MetaStep(MetaPath.CASSANDRA, translateToCQL()));
            //Add alter table as leaf if LUCENE index is selected.
            if (IndexType.LUCENE.equals(_type)) {
                StringBuilder alterStatement = new StringBuilder("ALTER TABLE ");
                if (_keyspaceInc) {
                    alterStatement.append(_keyspace);
                    alterStatement.append(".");
                }
                alterStatement.append(_tablename);
                alterStatement.append(" ADD ");
                alterStatement.append(getIndexName());
                alterStatement.append(" TEXT;");
                System.out.println("CreateIndexStatement.getPlan: " + alterStatement);
                result.addChild(new Tree(new MetaStep(MetaPath.CASSANDRA, alterStatement.toString())));
            }
        }

        return result;
    }
    
}
