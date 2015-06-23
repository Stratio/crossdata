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

import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.TableName;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Class that models a Funtion that can be found on a WHERE clause.
 */
public class FunctionRelation extends AbstractRelation {

    /**
     * Name of the Function.
     */
    private final String functionName;

    /**
     * List of function parameters, represented by Selectors.
     */
    private final List<Selector> functionSelectors;

    /**
     * List of tables of this relation
     */
    private Set<TableName> abstractRelationTables = new LinkedHashSet();

    /**
     * Class constructor.
     *
     * @param functionName Name of the includes.
     * @param functionSelectors A list of selectors with the columns affected.
     */
    public FunctionRelation(String functionName, List<Selector> functionSelectors, TableName tableName) {
        this.functionName = functionName;
        this.functionSelectors = functionSelectors;
        abstractRelationTables.add(tableName);
    }

    /**
     * The functions does not acepts joins.
     * @return
     */
    @Override
    public boolean isBasicRelation() {
        return true;
    }

    @Override
    public Set<TableName> getAbstractRelationTables() {
        return abstractRelationTables;
    }

    @Override
    public String toSQLString(boolean withAlias, TableName toExcludeTable) {
        return toSQLString(withAlias);
    }

    @Override
    public String toSQLString(boolean withAlias) {
        StringBuilder sb = new StringBuilder();
        if(isParenthesis()){
            sb.append("(");
        }
        sb.append((functionName)).append("(");
        String comma = "";
        for (Selector selector :functionSelectors){

            sb.append(comma);
            sb.append(selector.toSQLString(withAlias));
            comma = ", ";
        }

        sb.append(")");
        return sb.toString();
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();


        if(isParenthesis()){
            sb.append("(");
        }
        sb.append(functionName).append("(");
        String comma = "";
        for (Selector selector :functionSelectors){
            sb.append(comma);
            sb.append(selector.toString());
            comma = ", ";
        }
        sb.append(")");
        if(isParenthesis()){
            sb.append(")");
        }
        return sb.toString();
    }

    public List<Selector> getFunctionSelectors() {
        return functionSelectors;
    }

    public String getFunctionName() {
        return functionName;
    }

    public String getTableName(){
        return  functionSelectors.iterator().next().getTableName().getQualifiedName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FunctionRelation that = (FunctionRelation) o;

        if (functionName != null ? !functionName.equals(that.functionName) : that.functionName != null) return false;
        if (functionSelectors != null ? !functionSelectors.equals(that.functionSelectors) : that.functionSelectors != null)
            return false;
        if (abstractRelationTables != null ? !abstractRelationTables.equals(that.abstractRelationTables) : that.abstractRelationTables != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = functionName != null ? functionName.hashCode() : 0;
        result = 31 * result + (functionSelectors != null ? functionSelectors.hashCode() : 0);
        result = 31 * result + (abstractRelationTables != null ? abstractRelationTables.hashCode() : 0);
        return result;
    }
}
