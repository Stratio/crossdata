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
 * String selector. This type of Selector will be used for quoted strings.
 */
public class StringSelector extends Selector {

    private static final long serialVersionUID = 1606966682293837040L;
    /**
     * The string value.
     */
    private final String value;

    /**
     * Class constructor.
     *
     * @param value The string value.
     */
    public StringSelector(String value) {
        this(null, value);
    }

    /**
     * Class constructor.
     *
     * @param tableName The table name.
     * @param value The string value.
     */
    public StringSelector(TableName tableName, String value) {
        super(tableName);
        this.value = value;
    }

    /**
     * Get the value.
     *
     * @return The string value.
     */
    public String getValue() {
        return value;
    }

    @Override
    public SelectorType getType() {
        return SelectorType.STRING;
    }

    @Override
    public String toString() {
        return toSQLString(false);
    }

    @Override
    public String toSQLString(boolean withAlias) {
        StringBuilder sb=new StringBuilder();
        sb.append("'").append(value).append("'");
        return sb.toString();
    }

    @Override
    public String getStringValue() {
        return getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StringSelector that = (StringSelector) o;

        if (!value.equals(that.value)) {
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
        result = 31 * result + value.hashCode();
        return result;
    }
}
