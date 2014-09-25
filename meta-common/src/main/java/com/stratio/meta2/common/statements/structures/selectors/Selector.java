/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta2.common.statements.structures.selectors;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.stratio.meta2.common.data.TableName;

/**
 * This class represents one of the elements requested in a SELECT statement.
 */
public abstract class Selector {

    /**
     * The alias to be applied to the current Selector.
     */
    protected String alias = null;

    /**
     * Get the column alias.
     *
     * @return The alias.
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Set the alias for this selector.
     *
     * @param alias The alias.
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * Get the selector type.
     *
     * @return A {@link com.stratio.meta2.common.statements.structures.selectors.SelectorType}.
     */
    public abstract SelectorType getType();

    /**
     * Get the tables associated with the current selector.
     *
     * @return A set of {@link com.stratio.meta2.common.data.TableName}.
     */
    public Set<TableName> getSelectorTables(){
        return new HashSet<>();
    }

    public String getSelectorTablesAsString() {
        StringBuilder sb = new StringBuilder();
        Iterator<TableName> it = getSelectorTables().iterator();
        while (it.hasNext()) {
            sb.append(it.next().getQualifiedName());
            if (it.hasNext()) {
                sb.append("-");
            }
        }
        return sb.toString();
    }

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();

}
