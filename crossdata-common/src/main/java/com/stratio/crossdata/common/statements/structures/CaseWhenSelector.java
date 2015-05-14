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
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.stratio.crossdata.common.data.TableName;

/**
 * Relation selector.
 */
public class CaseWhenSelector extends Selector {

    private static final long serialVersionUID = 3405806271945142901L;

    /**
     * Map that contains the condition and the consequence to that condition.
     */
    private List<Pair<List<AbstractRelation>, Selector>> restrictions = new ArrayList<>();

    private Selector defaultValue;

    /**
     * Class constructor.
     *
     * @param restrictions A Map of {@link com.stratio.crossdata.common.statements.structures.RelationSelector} and
     *                     {@link com.stratio.crossdata.common.statements.structures.Selector}.
     */
    public CaseWhenSelector(List<Pair<List<AbstractRelation>, Selector>> restrictions) {
        this(null, restrictions);
    }

    /**
     * Class constructor.
     *
     * @param tableName    The table name.
     * @param restrictions A {@link Relation}.
     */
    public CaseWhenSelector(TableName tableName, List<Pair<List<AbstractRelation>, Selector>> restrictions) {
        super(tableName);
        this.restrictions = restrictions;
    }

    /**
     * Class constructor.
     *
     * @param tableName    The table name.
     * @param restrictions A {@link Relation}.
     * @param defaultValue A default value in a case when clause.
     */
    public CaseWhenSelector(TableName tableName, List<Pair<List<AbstractRelation>, Selector>> restrictions,
            Selector defaultValue) {
        super(tableName);
        this.restrictions = restrictions;
        this.defaultValue = defaultValue;
    }

    public List<Pair<List<AbstractRelation>, Selector>> getRestrictions() {
        return restrictions;
    }

    /**
     * Get the default value in case when clause.
     *
     * @return A {@link com.stratio.crossdata.common.statements.structures.Selector}
     */
    public Selector getDefaultValue() {
        return defaultValue;
    }

    /**
     * Set the defaultValue in a case when clause.
     *
     * @param defaultValue The default value.
     */
    public void setDefaultValue(Selector defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public SelectorType getType() {
        return SelectorType.CASE_WHEN;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CaseWhenSelector)) {
            return false;
        }

        CaseWhenSelector that = (CaseWhenSelector) o;

        if (!restrictions.equals(that.restrictions)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return restrictions.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder("case");
        for (Pair<List<AbstractRelation>,Selector> pair:restrictions) {
            sb.append(" when ");
            List<AbstractRelation> relations = pair.getLeft();
            boolean first=true;
            for (AbstractRelation relation : relations) {
                if (!first) {
                    sb.append(" AND ");
                }
                sb.append(relation.toString());
                first=false;
            }
            sb.append(" then ").append(pair.getRight().toString());
        }
        sb.append(" else ").append(defaultValue.toString()).append(" ").append(" end");
        return sb.toString();
    }

    @Override
    public String toSQLString(boolean withAlias) {
        StringBuilder sb=new StringBuilder("CASE");
        for (Pair<List<AbstractRelation>,Selector> pair:restrictions) {
            sb.append(" WHEN ");
            List<AbstractRelation> relations = pair.getLeft();
            boolean first=true;
            for (AbstractRelation relation : relations) {
                if (!first) {
                    sb.append(" AND ");
                }
                sb.append(relation.toSQLString(withAlias));
                first=false;
            }
            sb.append(" THEN ").append(pair.getRight().toSQLString(withAlias));
        }
        sb.append(" ELSE ").append(defaultValue.toSQLString(withAlias)).append(" END");
        return sb.toString();
    }

    @Override public String getStringValue() {
        StringBuilder sb=new StringBuilder("CASE-WHEN_");
        sb.append(hashCode());
        return sb.toString();
    }

    @Override
    public Set<TableName> getSelectorTables() {
        Set<TableName> tables =  new HashSet<>();
        tables.addAll(defaultValue.getSelectorTables());
        for (Pair<List<AbstractRelation>, Selector> restriction : restrictions) {
            for (AbstractRelation abstractRelation : restriction.getLeft()) {
                tables.addAll(abstractRelation.getAbstractRelationTables());
            }
            tables.addAll(restriction.getRight().getSelectorTables());

        }
        return tables;
    }
}
