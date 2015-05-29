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

package com.stratio.crossdata.common.utils;


import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.statements.structures.ExtendedSqlExpression;
import com.stratio.crossdata.common.statements.structures.ISqlExpression;

import java.util.List;

public class SqlStringUtils {

    /**
     * Create a string from a list of ISqlExpression using a separator between objects.
     *
     * @param ids       The list of objects.
     * @param separator The separator.
     * @param withAlias Whether alias is included.
     * @return A String.
     */
    public static <T extends ISqlExpression> String sqlStringList ( List<T> ids, String separator, boolean withAlias) {
        StringBuilder sb = new StringBuilder();
        for (ISqlExpression value: ids) {
            sb.append(value.toSQLString(withAlias)).append(separator);
        }
        if (sb.length() > separator.length()) {
            return sb.substring(0, sb.length() - separator.length());
        } else {
            return "";
        }
    }

    /**
     * Create a string from a list of ISqlExpression using a separator between objects.
     *
     * @param ids       The list of objects.
     * @param separator The separator.
     * @param withAlias Whether alias is included.
     * @return A String.
     */
    public static <T extends ExtendedSqlExpression> String sqlStringList ( List<T> ids, String separator, boolean withAlias, TableName excludedTableName) {
        StringBuilder sb = new StringBuilder();
        for (ExtendedSqlExpression value: ids) {
            String sqlString = value.toSQLString(withAlias, excludedTableName);
            if(!sqlString.isEmpty()) {
                sb.append(sqlString).append(separator);
            }
        }
        if (sb.length() > separator.length()) {
            return sb.substring(0, sb.length() - separator.length());
        } else {
            return "";
        }
    }
}
