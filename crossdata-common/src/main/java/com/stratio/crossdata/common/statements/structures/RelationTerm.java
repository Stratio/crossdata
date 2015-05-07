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
import com.stratio.crossdata.common.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Class that contains the relations that can be found on a {@link com.stratio.crossdata.common.statements.structures.RelationDisjunction}.
 */
public class RelationTerm implements Serializable, ISqlExpression {

    private static final long serialVersionUID = -3346305822266813889L;

    private final List<AbstractRelation> relations = new ArrayList<>();
    private boolean withParentheses = false;

    /**
     * Class constructor.
     *
     * @param relations List of {@link com.stratio.crossdata.common.statements.structures.AbstractRelation}.
     */
    public RelationTerm(List<AbstractRelation> relations) {
        this(relations, false);
    }

    /**
     * Class constructor.
     *
     * @param relations         List of {@link com.stratio.crossdata.common.statements.structures.AbstractRelation}.
     * @param withParentheses   Whether the list of relations is surrounded by parentheses or not.
     */
    public RelationTerm(List<AbstractRelation> relations, boolean withParentheses) {
        this.relations.addAll(relations);
        this.withParentheses = withParentheses;
    }

    /**
     * Class constructor.
     *
     * @param relation An {@link com.stratio.crossdata.common.statements.structures.AbstractRelation}.
     */
    public RelationTerm(AbstractRelation relation) {
        this(relation, false);
    }

    /**
     * Class constructor.
     *
     * @param relation          An {@link com.stratio.crossdata.common.statements.structures.AbstractRelation}.
     * @param withParenthesis   Whether the list of relations is surrounded by parenthesis or not.
     */
    public RelationTerm(AbstractRelation relation, boolean withParenthesis) {
        this(Collections.singletonList(relation), withParenthesis);
    }

    /**
     * Gets the list of {@link com.stratio.crossdata.common.statements.structures.AbstractRelation}.
     * @return  The list of {@link com.stratio.crossdata.common.statements.structures.AbstractRelation}.
     */
    public List<AbstractRelation> getRelations() {
        return relations;
    }

    /**
     * Returns whether the list of relations is enclosed by parenthesis or not.
     *
     * @return true if the relations is enclosed by parenteshes; false otherwise.
     */
    public boolean isWithParentheses() {
        return withParentheses;
    }

    /**
     * Adds or removes parentheses to the {@link com.stratio.crossdata.common.statements.structures.RelationTerm}.
     *
     * @param  withParentheses whether the list of relations is enclosed by parentheses or not.
     */
    public void setWithParentheses(boolean withParentheses) {
        this.withParentheses = withParentheses;
    }

    /**
     * Get a string representation of the tables associated with the selector.
     *
     * @return A string with the table qualified names separated by -.
     */
    public String getSelectorTablesAsString() {
        StringBuilder sb = new StringBuilder();
        if(withParentheses){
            sb.append("(");
        }
        Iterator<AbstractRelation> iter = relations.iterator();
        while(iter.hasNext()){
            AbstractRelation ab = iter.next();
            sb.append(ab);
            if(iter.hasNext()){
                sb.append("-");
            }
        }
        if(withParentheses){
            sb.append(")");
        }
        return sb.toString().replace("(", "").replace(")", "");
    }

    /**
     * Get the tables associated with the selector.
     *
     * @return A set of table.
     */
    public Set<TableName> getSelectorTables() {
       Set<TableName> tableNameSet = new HashSet<>();
        for (AbstractRelation relation : relations) {
            tableNameSet.addAll(relation.getAbstractRelationTables());
        }
        return tableNameSet;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(withParentheses){
            sb.append("(");
        }
        Iterator<AbstractRelation> iter = relations.iterator();
        while(iter.hasNext()){
            AbstractRelation ab = iter.next();
            sb.append(ab);
            if(iter.hasNext()){
                sb.append(" AND ");
            }
        }
        if(withParentheses){
            sb.append(")");
        }
        return sb.toString();
    }

    @Override
    public String toSQLString(boolean withAlias) {
        StringBuilder sb = new StringBuilder();
        if(withParentheses){
            sb.append("(");
        }
        sb.append(StringUtils.sqlStringList(relations, " AND ", withAlias));
        if(withParentheses){
            sb.append(")");
        }
        return sb.toString();
    }
}
