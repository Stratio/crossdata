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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.utils.SqlStringUtils;
import com.stratio.crossdata.common.utils.StringUtils;

/**
 * Models OR relationships that can be found on a WHERE clause.
 */
public class RelationDisjunction extends AbstractRelation {

    private static final long serialVersionUID = 2085700590246602145L;

    private final List<RelationTerm> terms = new ArrayList<>();


    /**
     * Class constructor.
     *
     */
    public RelationDisjunction() {
    }

    /**
     * Class constructor.
     *
     * @param terms     Inner relations list.
     */
    public RelationDisjunction(RelationTerm... terms) {
        for(int i = 0; i<terms.length; i++){
            this.terms.add(terms[i]);
        }
    }

    /**
     * Gets the inner relations.
     *
     * @return  The list of inner relations.
     */
    public List<RelationTerm> getTerms() {
        return terms;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(isParenthesis()){
            sb.append("(");
        }

        Iterator<RelationTerm> iter = terms.iterator();
        while(iter.hasNext()){
            RelationTerm rt = iter.next();
            sb.append(rt);
            if(iter.hasNext()){
                sb.append(" OR ");
            }
        }

        if(isParenthesis()){
            sb.append(")");
        }
        return sb.toString();
    }

    /**
     * Returns the string representation in sql syntax.
     * @param withAlias Whether the expression must use alias or qualified names.
     * @return          The sql string.
     */
    public String toSQLString(boolean withAlias) {

        StringBuilder sb = new StringBuilder();
        if(isParenthesis()){
            sb.append("(");
        }
        sb.append(SqlStringUtils.sqlStringList(terms, " OR ", withAlias));
        if(isParenthesis()){
            sb.append(")");
        }
        return sb.toString();
    }


    /**
     * Get a string representation of the tables associated with the selector.
     *
     * @return A string with the table qualified names separated by -.
     */
    public String getSelectorTablesAsString() {
        Set<String> allTables = new LinkedHashSet<>();
        // Inner relations
        for(RelationTerm rt: terms){
            allTables.add(getSelectorTablesAsString(rt));
        }
        // Create final String without duplicates
        StringBuilder sb = new StringBuilder();
        Iterator<String> iter = allTables.iterator();
        while(iter.hasNext()){
            String name = iter.next();
            sb.append(name);
            if(iter.hasNext()){
                sb.append("-");
            }
        }
        return sb.toString().replace("(", "").replace(")", "");
    }

    /**
     * Get a string representation of the tables associated with the first selector.
     *
     * @return A string with the table qualified names separated by -.
     */
    public String getFirstSelectorTablesAsString() {
        for(RelationTerm rt: terms){
            Iterator<AbstractRelation> iter = rt.getRelations().iterator();
            while(iter.hasNext()){
                AbstractRelation ab = iter.next();
                if(ab instanceof RelationDisjunction){
                    RelationDisjunction rd = (RelationDisjunction) ab;
                    iter = rd.getTerms().get(0).getRelations().iterator();
                } else {
                    Relation r = (Relation) ab;
                    return r.getLeftTerm().getTableName().toString().split(" AS ")[0];
                }
            }
        }
        return getSelectorTablesAsString();
    }


    /**
     * Get the tables queried on the selector.
     * @param   relationTerm
     * @return A set of {@link com.stratio.crossdata.common.data.TableName}.
     */
    public String getSelectorTablesAsString(RelationTerm relationTerm) {
        Set<String> allTables = new HashSet<>();
        allTables.add(relationTerm.getSelectorTablesAsString());
        // Create final String without duplicates
        StringBuilder sb = new StringBuilder();
        Iterator<String> iter = allTables.iterator();
        while(iter.hasNext()){
            String name = iter.next();
            sb.append(name);
            if(iter.hasNext()){
                sb.append("-");
            }
        }
        return sb.toString().replace("(", "").replace(")", "");
    }

    @Override
    public Set<TableName> getAbstractRelationTables() {
        Set<TableName> allTables = new LinkedHashSet<>();
        for(RelationTerm rt: terms){
            allTables.addAll( rt.getSelectorTables());
        }
        return allTables;
    }


    @Override
    public boolean isBasicRelation(){
        Set<TableName> tableNameSet = new HashSet<>();
        //TODO while: stop when tableName.size > 1
        for (RelationTerm term : terms) {
            for (AbstractRelation abstractRelation : term.getRelations()) {
                tableNameSet.addAll(abstractRelation.getAbstractRelationTables());
            }
        }
        return tableNameSet.size() <= 1;
    }

    @Override
    public String toSQLString(boolean withAlias, TableName toExcludeTable) {
        if(isBasicRelation() && getAbstractRelationTables().iterator().next().equals(toExcludeTable)){
            return "";
        }else {
            return toSQLString(withAlias);
        }
    }
}
