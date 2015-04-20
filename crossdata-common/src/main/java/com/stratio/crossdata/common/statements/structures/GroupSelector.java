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

/**
 * Integer value selector.
 */
public class GroupSelector extends Selector {

    private static final long serialVersionUID = -7194192628611176140L;
    /**
     * The first selector of the group.
     */
    private Selector firstValue;

    /**
     * The last selector of the group.
     */
    private Selector lastValue;

    /**
     * Class constructor.
     *
     * @param value1 The integer/long value.
     * @param value2 The integer/long value.
     */
    public GroupSelector(Selector value1, Selector value2) {
        this(null,value1,value2);
    }

    /**
     * Class constructor.
     * @param tableName A Table Name.
     * @param value1 The integer/long value.
     * @param value2 The integer/long value.
     */
    public GroupSelector(TableName tableName, Selector value1, Selector value2) {
        super(tableName);
        firstValue=value1;
        lastValue=value2;
    }

    /**
     * Get the long value.
     *
     * @return A  value.
     */
    public Selector getFirstValue() {
        return firstValue;
    }

    /**
     * Get the long value.
     *
     * @return A value.
     */
    public Selector getLastValue() {
        return lastValue;
    }
    @Override
    public SelectorType getType() {
        return SelectorType.GROUP;
    }

    @Override
    public String toString() {
        return (firstValue.toString()) + " AND " + (lastValue.toString());
    }

    @Override
    public String toSQLString(boolean withAlias) {
        return firstValue.toSQLString(withAlias) + " AND " + lastValue.toSQLString(withAlias);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GroupSelector that = (GroupSelector) o;

        if (firstValue != that.firstValue) {
            return false;
        }
        if (lastValue != that.lastValue) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        if (alias != null) {
            result = alias.hashCode();
        }
        result+=firstValue.hashCode() + lastValue.hashCode();
        return result;
    }
}
