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
import com.stratio.meta.common.data.DeepResultSet;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.Tree;

import java.util.Iterator;

public class DropIndexStatement extends MetaStatement {

    private boolean _dropIfExists = false;
    private boolean _dropIndex = false;
    private String _name = null;
    private String _keyspace = null;
    private boolean _keyspaceInc = false;
    private ColumnMetadata _targetColumn = null;


    public DropIndexStatement(){
        this.command = false;
    }
    
    public DropIndexStatement(String name){
        this();
        _name = name;
        if(name.contains(".")){
            String[] ksAndName = name.split("\\.");
            _keyspace = ksAndName[0];
            _name = ksAndName[1];
            _keyspaceInc = true;
        }
    }
    
    public void setDropIfExists(){
            _dropIfExists = true;
    }

    public void setName(String name){
        System.out.println("name: " + name);
            _name = name;
        if(name.contains(".")){
            String[] ksAndName = name.split("\\.");
            _keyspace = ksAndName[0];
            _name = ksAndName[1];
            _keyspaceInc = true;
        }
    }

    @Override
    public String toString() {
            StringBuilder sb = new StringBuilder("DROP INDEX ");
            if(_dropIfExists){
                    sb.append("IF EXISTS ");
            }
        if(_keyspaceInc){
            sb.append(_keyspace).append(".");
        }
            sb.append(_name);
            return sb.toString();
    }

    /** {@inheritDoc} */
    @Override
    public Result validate(MetadataManager metadata, String targetKeyspace) {

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
            }else{
                result = validateIndexName(ksMetadata);
            }
        }
        return result;
    }

    /**
     * Validate the existence of the index in the selected keyspace.
     * @param ksMetadata The keyspace metadata.
     * @return A {@link com.stratio.meta.common.result.Result} with the validation result.
     */
    public Result validateIndexName(KeyspaceMetadata ksMetadata){
        Result result = QueryResult.CreateSuccessQueryResult();
        boolean found = false;
        System.out.println("ks: " + _keyspace + " name: " + _name);
        Iterator<TableMetadata> tables = ksMetadata.getTables().iterator();

        while(tables.hasNext() && !found){
            TableMetadata tableMetadata = tables.next();
            Iterator<ColumnMetadata> columns = tableMetadata.getColumns().iterator();
            while(columns.hasNext() && !found){
                ColumnMetadata column = columns.next();
                if(column.getIndex() != null
                        && (column.getIndex().getName().equals(_name)
                        || column.getIndex().getName().equals("stratio_lucene_"+_name))){
                    found = true;
                    _targetColumn = column;
                }
            }
        }

        if(!_dropIfExists && !found){
            result = QueryResult.CreateFailQueryResult("Index " + _name + " not found in keyspace " + ksMetadata.getName());
        }else{
            _dropIndex = true;
        }

        return result;
    }

    @Override
    public String getSuggestion() {
        return this.getClass().toString().toUpperCase()+" EXAMPLE";
    }

    @Override
    public String translateToCQL() {
        //System.out.println("translatedToCQL="+toString());
        return this.toString();
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
        if(_dropIndex) {
            //Add CREATE INDEX as the root.
            StringBuilder sb = new StringBuilder("DROP INDEX ");
            if(_keyspaceInc) {
                sb.append(_keyspace).append(".");
            }
            sb.append(_targetColumn.getIndex().getName());
            System.out.println("sb: " + sb.toString());

            if (_targetColumn.getIndex().getName().startsWith("stratio")) {
                //Remove associated column.
                StringBuilder sb2 = new StringBuilder("ALTER TABLE ");
                if(_keyspaceInc) {
                    sb2.append(_keyspace).append(".");
                }
                sb2.append(_targetColumn.getTable().getName());
                sb2.append(" DROP ").append("stratio_lucene_").append(_name);
                System.out.println("sb2: " + sb2.toString());

                result.setNode(new MetaStep(MetaPath.CASSANDRA, sb2.toString()));
                result.addChild(new Tree(new MetaStep(MetaPath.CASSANDRA, sb.toString())));
            }else{
                result.setNode(new MetaStep(MetaPath.CASSANDRA, sb.toString()));
            }



        }
        return result;
    }
    
}
