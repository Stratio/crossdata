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

import com.stratio.crossdata.common.annotation.Experimental;
import com.stratio.crossdata.common.data.TableName;


/**
 * It allows exclude information related to some tables.
 */
@Deprecated
public interface ExtendedSqlExpression extends ISqlExpression{

    /**
     * Returns the string representation in sql syntax.
     * @param withAlias Whether the expression must use alias or qualified names.
     * @return          The sql string.
     */
    @Experimental
    String toSQLString(boolean withAlias, TableName toExcludeTable);
}
