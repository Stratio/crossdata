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

import java.util.Set;

/**
 * Selector aliases appearing in GroupBy and OrderBy clauses which refer to any alias selectors in {@link com.stratio.crossdata.common.statements.structures.SelectExpression}
 * except for {@link com.stratio.crossdata.common.statements.structures.ColumnSelector}.
 */
public class AliasSelector extends Selector {

    private static final long serialVersionUID = 3765995925620545659L;

    /**
     * Selector referenced by the alias.
     */
    private Selector referencedSelector;


    /**
     * Class constructor.
     *
     * @param referencedSelector Selector referenced by the alias.
     */
    public AliasSelector(Selector referencedSelector) {
        super(null);
        this.referencedSelector = referencedSelector;
    }

    /**
     * Get the selector referenced by the alias..
     *
     * @return A {@link com.stratio.crossdata.common.statements.structures.Selector}.
     */
    public Selector getReferencedSelector() {
        return referencedSelector;
    }

    @Override
    public String getStringValue() {
        return referencedSelector != null ? referencedSelector.getAlias() : null;
    }

    @Override
    public SelectorType getType() {
        return SelectorType.ALIAS;
    }

    @Override
    public Set<TableName> getSelectorTables() {
        return referencedSelector.getSelectorTables();
    }

    @Override
    public String toString() {
        return getStringValue() + " refers to " + referencedSelector.toString();
    }

    @Override
    public String toSQLString(boolean withAlias) {
        return getStringValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof AliasSelector)){
            return false;
        }

        AliasSelector that = (AliasSelector) o;

        if (referencedSelector != null ?
                        !referencedSelector.equals(that.referencedSelector) :
                        that.referencedSelector != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return referencedSelector != null ? referencedSelector.hashCode() : 0;
    }

}

