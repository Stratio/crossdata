/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.crossdata.common.statements.structures;

import com.stratio.crossdata.common.data.TableName;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Asterisk selector.
 */
public class AsteriskSelector extends Selector {

    private static final long serialVersionUID = 3370725707713353255L;

    private Set<TableName> joinedTables;

    /**
     * Basic Constructor class.
     */
    public AsteriskSelector() {
        this(null);
    }

    /**
     * Constructor class.
     * @param tableName The table name affected by the asterisk selector.
     */
    public AsteriskSelector(TableName tableName) {
        super(tableName);
        joinedTables = new HashSet<>();
    }

    /**
     * Adds joined tables affected by the asterisk selector.
     * @param tableNames tables affected by the asterisk selector.
     */
    public void addTables(Collection<? extends TableName> tableNames){
        joinedTables.addAll(tableNames);

    }

    @Override
    public Set<TableName> getSelectorTables() {
        Set<TableName> tables =  new HashSet<>(joinedTables.size()+1);
        tables.addAll(joinedTables);
        tables.add(tableName);
        return tables;
    }

    @Override
    public String toString() {
        return "*";
    }

    @Override
    public String toSQLString(boolean withAlias) {
        return toString();
    }

    @Override public SelectorType getType() {
        return SelectorType.ASTERISK;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        if (alias != null){
            result = alias.hashCode();
        }
        return result;
    }


}
