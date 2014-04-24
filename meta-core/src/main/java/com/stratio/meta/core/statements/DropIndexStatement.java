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
    private boolean dropIfExists = false;

    /**
     * Whether the index will be dropped.
     */
    private boolean dropIndex = false;

    /**
     * The name of the index.
     */
    private String name = null;

    /**
     * The keyspace specified in the drop index statement.
     */
    private String keyspace = null;

    /**
     * Whether the keyspace has been specified in the Drop index statement or it should be taken from the
     * environment.
     */
    private boolean keyspaceInc = false;

    /**
     * Target column associated with the index.
     */
    private ColumnMetadata targetColumn = null;

    /**
     * Class constructor.
     */
    public DropIndexStatement(){
        this.command = false;
    }

    /**
     * Set the option to drop the index only if exists.
     */
    public void setDropIfExists(){
            dropIfExists = true;
    }

    /**
     * Set the index name.
     * @param name The name of the index. The name may contain
     *             the name of the keyspace where the index is active.
     */
    public void setName(String name){
            this.name = name;
        if(name.contains(".")){
            String[] ksAndName = name.split("\\.");
            keyspace = ksAndName[0];
            this.name = ksAndName[1];
            keyspaceInc = true;
        }
    }

    @Override
    public String toString() {
            StringBuilder sb = new StringBuilder("DROP INDEX ");
            if(dropIfExists){
                    sb.append("IF EXISTS ");
            }
        if(keyspaceInc){
            sb.append(keyspace).append(".");
        }
            sb.append(name);
            return sb.toString();
    }

    @Override
    public Result validate(MetadataManager metadata, String targetKeyspace) {

        Result result = null;
        //Get the effective keyspace based on the user specification during the create
        //sentence, or taking the keyspace in use in the user session.
        String effectiveKeyspace = targetKeyspace;
        if(keyspaceInc){
            effectiveKeyspace = keyspace;
        }

        //Check that the keyspace and table exists.
        if(effectiveKeyspace == null || effectiveKeyspace.length() == 0){
            result= QueryResult.createFailQueryResult("Target keyspace missing or no keyspace has been selected.");
        }else{
            KeyspaceMetadata ksMetadata = metadata.getKeyspaceMetadata(effectiveKeyspace);
            if(ksMetadata == null){
                result= QueryResult.createFailQueryResult("Keyspace " + effectiveKeyspace + " does not exists.");
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
        Result result = QueryResult.createSuccessQueryResult();
        boolean found = false;
        Iterator<TableMetadata> tables = ksMetadata.getTables().iterator();

        while(tables.hasNext() && !found){
            TableMetadata tableMetadata = tables.next();
            Iterator<ColumnMetadata> columns = tableMetadata.getColumns().iterator();
            while(columns.hasNext() && !found){
                ColumnMetadata column = columns.next();
                if(column.getIndex() != null
                        && (column.getIndex().getName().equals(name)
                        || column.getIndex().getName().equals("stratio_lucene_"+ name))){
                    found = true;
                    targetColumn = column;
                }
            }
        }

        if(!dropIfExists && !found){
            result = QueryResult.createFailQueryResult("Index " + name + " not found in keyspace " + ksMetadata.getName());
        }else{
            dropIndex = true;
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

    @Override
    public DeepResultSet executeDeep() {
        return new DeepResultSet();
    }

    @Override
    public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
        Tree result = new Tree();
        if(dropIndex) {
            //Add CREATE INDEX as the root.
            StringBuilder sb = new StringBuilder("DROP INDEX ");
            if(keyspaceInc) {
                sb.append(keyspace).append(".");
            }
            sb.append(targetColumn.getIndex().getName());

            if (targetColumn.getIndex().getName().startsWith("stratio")) {
                //Remove associated column.
                StringBuilder sb2 = new StringBuilder("ALTER TABLE ");
                if(keyspaceInc) {
                    sb2.append(keyspace).append(".");
                }
                sb2.append(targetColumn.getTable().getName());
                sb2.append(" DROP ").append("stratio_lucene_").append(name);

                result.setNode(new MetaStep(MetaPath.CASSANDRA, sb2.toString()));
                result.addChild(new Tree(new MetaStep(MetaPath.CASSANDRA, sb.toString())));
            }else{
                result.setNode(new MetaStep(MetaPath.CASSANDRA, sb.toString()));
            }

        }
        return result;
    }
    
}
