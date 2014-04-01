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
import com.stratio.meta.common.result.MetaResult;
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
    public MetaResult validate(MetadataManager metadata, String targetKeyspace) {

        //Validate target table
        MetaResult result = validateKeyspaceAndTable(metadata, targetKeyspace);

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
                result.setErrorMessage("Internal namespace stratio cannot be use on index name " + _name);
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
     * @return A {@link com.stratio.meta.common.result.MetaResult} with the validation result.
     */
    private MetaResult validateSelectionColumns(TableMetadata tableMetadata) {
        MetaResult result = new MetaResult();

        for(String c : _targetColumn){
            if(c.toLowerCase().startsWith("stratio")){
                result.setErrorMessage("Internal column " + c + " cannot be part of the WHERE clause.");
            }else if(tableMetadata.getColumn(c) == null){
                result.setErrorMessage("Column " + c + " does not exists in table " + tableMetadata.getName());
            }
        }

        return result;
    }

    private MetaResult validateIndexName(MetadataManager metadata, TableMetadata tableMetadata){
        MetaResult result = new MetaResult();
        String index_name = _name;
        if(IndexType.LUCENE.equals(_type)){
            index_name = "stratio_lucene_" + _name;
        }
        List<CustomIndexMetadata> allIndex = new ArrayList<>();
        for(List<CustomIndexMetadata> l : metadata.getColumnIndexes(tableMetadata).values()){
            allIndex.addAll(l);
        }
        boolean found = false;
        for(int index = 0; index < allIndex.size() && !found; index++){
            if(allIndex.get(index).getIndexName().equalsIgnoreCase(index_name)){
                found = true;
            }
        }
        if(found && !_createIfNotExists){
            result.setErrorMessage("Index " + _name + " already exists in table " + _tablename);
        }
        return result;
    }

    private MetaResult validateOptions(String effectiveKeyspace, TableMetadata metadata) {
        MetaResult result = new MetaResult();
        if(_options.size() > 0){
            result.setErrorMessage("WITH OPTIONS clause not supported in index creation.");
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
                result.setErrorMessage("Cannot create index: A Lucene index already exists on table " + effectiveKeyspace + "."
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
     * @return A {@link com.stratio.meta.common.result.MetaResult} with the validation result.
     */
    private MetaResult validateKeyspaceAndTable(MetadataManager metadata, String targetKeyspace){
        MetaResult result = new MetaResult();
        //Get the effective keyspace based on the user specification during the create
        //sentence, or taking the keyspace in use in the user session.
        String effectiveKeyspace = targetKeyspace;
        if(_keyspaceInc){
            effectiveKeyspace = _keyspace;
        }

        //Check that the keyspace and table exists.
        if(effectiveKeyspace == null || effectiveKeyspace.length() == 0){
            result.setErrorMessage("Target keyspace missing or no keyspace has been selected.");
        }else{
            KeyspaceMetadata ksMetadata = metadata.getKeyspaceMetadata(effectiveKeyspace);
            if(ksMetadata == null){
                result.setErrorMessage("Keyspace " + effectiveKeyspace + " does not exists.");
            }else {
                TableMetadata tableMetadata = metadata.getTableMetadata(effectiveKeyspace, _tablename);
                if (tableMetadata == null) {
                    result.setErrorMessage("Table " + _tablename + " does not exists.");
                }
            }

        }
        return result;
    }

/*
    public void validate(MetadataManager mm, String keyspace) {

        boolean result = true;
        TableMetadata tm = null;


        if(result){
            //Check that target columns appear on the table
            for(String column : _targetColumn){
                if(tm.getColumn(column)==null){
                    //TODO Report Column does not exists
                    result = false;
                }
            }
        }

        if(result){
            if(IndexType.LUCENE.equals(_type)){
                //Parse index options.
                //Check that the index mapping types are compatible with the specified C* types.

            }else if(IndexType.DEFAULT.equals(_type)){
                //Check that only one column is specified

            }
        }
    }*/

    @Override
    public String getSuggestion() {
        return this.getClass().toString().toUpperCase()+" EXAMPLE";
    }

    @Override
    public String translateToCQL() {
        // EXAMPLE:
        // META: CREATE LUCENE INDEX demo_banks ON demo.banks(lucene) USING org.apache.cassandra.db.index.stratio.RowIndex WITH OPTIONS schema = '{default_analyzer:"org.apache.lucene.analysis.standard.StandardAnalyzer", fields: {day: {type: "date", pattern: "yyyy-MM-dd"}, key: {type:"uuid"}}}';
        // CQL: CREATE CUSTOM INDEX demo_banks ON demo.banks (lucene) USING 'org.apache.cassandra.db.index.stratio.RowIndex' WITH OPTIONS = {'schema' : '{default_analyzer:"org.apache.lucene.analysis.standard.StandardAnalyzer", fields: {day: {type: "date", pattern: "yyyy-MM-dd"}, key: {type:"uuid"}}}'}
        
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
            //String cqlOptions = cqlString.substring(cqlString.indexOf("{"), cqlString.lastIndexOf("}")+1);
            //cqlString = cqlString.substring(0, cqlString.indexOf("{")+1).concat(cqlString.substring(cqlString.lastIndexOf("}")));           
            /*
            String[] opts = cqlOptions.split("=");
            cqlOptions = new String();
            for(int i=0; i<opts.length; i++){
                cqlOptions = cqlOptions.concat("\'").concat(opts[i]).concat("\'");
                if(i % 2 == 0){
                    cqlOptions = cqlOptions.concat(": ");
                } else {
                    if(i<(opts.length-1)){
                        cqlOptions = cqlOptions.concat(" AND ");
                    }
                }
            }
            cqlString = cqlString.replace("OPTIONS = {", "OPTIONS = {"+cqlOptions);
            */            
                    
            
            //cqlString = cqlString.replace("OPTIONS = {", "OPTIONS = {"+ParserUtils.addSingleQuotesToStringList(cqlOptions));
        }
        return cqlString;
    }
        
//    @Override
//    public String parseResult(ResultSet resultSet) {
//        return "\t"+resultSet.toString();
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
        return new Tree();
    }
    
}
