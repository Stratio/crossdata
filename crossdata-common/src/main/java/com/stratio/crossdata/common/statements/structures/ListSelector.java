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

import java.util.Iterator;
import java.util.List;

import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.utils.StringUtils;

public class ListSelector extends Selector {

    private static final long serialVersionUID = 6144378661087640561L;

    private final List<Selector> selectorsList;

    /**
     * Class constructor.
     *
     * @param tableName The associated {@link com.stratio.crossdata.common.data.TableName}.
     */
    public ListSelector(TableName tableName, List<Selector> selectorsList) {
        super(tableName);
        this.selectorsList = selectorsList;
    }

    public List<Selector> getSelectorsList() {
        return selectorsList;
    }

    /**
     * Get the selector type.
     *
     * @return A {@link SelectorType}.
     */
    @Override
    public SelectorType getType() {
        return SelectorType.LIST;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        Iterator<Selector> iter = selectorsList.iterator();
        while(iter.hasNext()){
            Selector s = iter.next();
            sb.append(s);
            if(iter.hasNext()){
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String toSQLString(boolean withAlias) {
        StringBuilder sb = new StringBuilder("(");
        sb.append(StringUtils.sqlStringList(selectorsList, ", ", withAlias));
        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ListSelector that = (ListSelector) o;

        if (selectorsList != null ? !selectorsList.equals(that.selectorsList) : that.selectorsList != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return selectorsList != null ? selectorsList.hashCode() : 0;
    }
}
