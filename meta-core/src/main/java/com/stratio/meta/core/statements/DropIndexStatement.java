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

/**
 * Class that models a {@code DROP INDEX} statement from the META language.
 */
public class DropIndexStatement extends MetaStatement {

    /**
     * Whether the index should be dropped only if exists.
     */
    private boolean _dropIfExists = false;

    /**
     * Whether the index will be dropped.
     */
    private boolean _dropIndex = false;

    /**
     * The name of the index.
     */
    private String _name = null;

    /**
     * The keyspace specified in the drop index statement.
     */
    private String _keyspace = null;

    /**
     * Whether the keyspace has been specified in the Drop index statement or it should be taken from the
     * environment.
     */
    private boolean _keyspaceInc = false;

    /**
     * Target column associated with the index.
     */
    private ColumnMetadata _targetColumn = null;

    /**
     * Class constructor.
     */
    public DropIndexStatement(){
        this.command = false;
    }

    /**
     * Class constructor.
     * @param name The name of the index. The name may contain
     *             the name of the keyspace where the index is active.
     */
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

    /**
     * Set the option to drop the index only if exists.
     */
    public void setDropIfExists(){
            _dropIfExists = true;
    }

    /**
     * Set the index name.
     * @param name The name of the index. The name may contain
     *             the name of the keyspace where the index is active.
     */
    public void setName(String name){
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

        Result result = null;
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
    public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
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
