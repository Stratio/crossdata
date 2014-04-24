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

import com.stratio.meta.core.metadata.MetadataManager;
import com.stratio.meta.core.structures.ValueProperty;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.MetaStep;
import com.stratio.meta.core.utils.Tree;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Class that models an {@code ALTER TABLE} statement from the META language.
 */
public class AlterTableStatement extends MetaStatement{

    /**
     * Whether the keyspace has been specified in the Select statement or it should be taken from the
     * environment.
     */
    private boolean keyspaceInc = false;

    /**
     * The keyspace specified in the select statement.
     */
    private String keyspace;

    /**
     * The name of the target table.
     */
    private String tableName;

    /**
     * Type of alter. Accepted values are:
     * <ul>
     *     <li>1: Alter a column data type using {@code ALTER}.</li>
     *     <li>2: Add a new column using {@code ADD}.</li>
     *     <li>3: Drop a column using {@code DROP}.</li>
     *     <li>4: Establish a set of options using {@code WITH}.</li>
     * </ul>
     */
    private int prop;

    /**
     * Target column name.
     */
    private String column;

    /**
     * Target column datatype used with {@code ALTER} or {@code ADD}.
     */
    private String type;

    /**
     * The map of properties.
     */
    private Map<String, ValueProperty> option;

    /**
     * Class constructor.
     * @param tableName The name of the table.
     * @param column The name of the column.
     * @param type The data type of the column.
     * @param option The map of options.
     * @param prop The type of modification.
     */
    public AlterTableStatement(String tableName, String column, String type, Map<String, ValueProperty> option, int prop) {
        this.command = false;
        if(tableName.contains(".")){
            String[] ksAndTableName = tableName.split("\\.");
            keyspace = ksAndTableName[0];
            tableName = ksAndTableName[1];
            keyspaceInc = true;
        }
        this.tableName = tableName;
        this.column = column;
        this.type = type;
        this.option = option;
        this.prop = prop;          
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Alter table ");
        if(keyspaceInc){
            sb.append(keyspace).append(".");
        }
        sb.append(tableName);
        switch(prop){
            case 1: {
                sb.append(" alter ");
                sb.append(column);
                sb.append(" type ");
                sb.append(type);
            }break;
            case 2: {
                sb.append(" add ");
                sb.append(column).append(" ");
                sb.append(type);
            }break;
            case 3: {
                sb.append(" drop ");
                sb.append(column);
            }break;
            case 4: {
                Set<String> keySet = option.keySet();
                sb.append(" with");
                for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
                    String key = it.next();
                    ValueProperty vp = option.get(key);
                    sb.append(" ").append(key).append("=").append(String.valueOf(vp));
                    if(it.hasNext()) {
                        sb.append(" AND");
                    }
                }
            }break;
            default:{
                sb.append("bad option");
            }break;
        }        
        return sb.toString();
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
    public Tree getPlan(MetadataManager metadataManager, String targetKeyspace) {
        Tree tree = new Tree();
        tree.setNode(new MetaStep(MetaPath.CASSANDRA, this));
        return tree;
    }
    
}
