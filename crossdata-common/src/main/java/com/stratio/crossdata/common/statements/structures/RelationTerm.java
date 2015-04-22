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

import com.google.common.collect.Table;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RelationTerm implements Serializable, ISqlExpression {

    private static final long serialVersionUID = -3346305822266813889L;

    private final List<AbstractRelation> relations = new ArrayList<>();
    private boolean withParenthesis = false;

    public RelationTerm(List<AbstractRelation> relations) {
        this.relations.addAll(relations);
    }

    public RelationTerm(List<AbstractRelation> relations, boolean withParenthesis) {
        this.relations.addAll(relations);
        this.withParenthesis = withParenthesis;
    }

    public RelationTerm(AbstractRelation relation) {
        this(relation, false);
    }

    public RelationTerm(AbstractRelation relation, boolean withParenthesis) {
        this(Collections.singletonList(relation), withParenthesis);
    }

    public List<AbstractRelation> getRelations() {
        return relations;
    }

    public boolean isWithParenthesis() {
        return withParenthesis;
    }

    public void setWithParenthesis(boolean withParenthesis) {
        this.withParenthesis = withParenthesis;
    }

    public String getSelectorTablesAsString() {
        StringBuilder sb = new StringBuilder();
        if(withParenthesis){
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
        if(withParenthesis){
            sb.append(")");
        }
        return sb.toString().replace("(", "").replace(")", "");
    }

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
        if(withParenthesis){
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
        if(withParenthesis){
            sb.append(")");
        }
        return sb.toString();
    }

    @Override
    public String toSQLString(boolean withAlias) {
        StringBuilder sb = new StringBuilder();
        if(withParenthesis){
            sb.append("(");
        }
        sb.append(StringUtils.sqlStringList(relations, " AND ", withAlias));
        if(withParenthesis){
            sb.append(")");
        }
        return sb.toString();
    }
}
