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

package com.stratio.crossdata.core.query;

import com.stratio.crossdata.common.result.QueryStatus;
import com.stratio.crossdata.core.statements.CrossdataStatement;
import com.stratio.crossdata.core.statements.sql.SqlStatement;

public class SqlParsedQuery extends BaseQuery implements IParsedQuery {

    /**
     * The crossdata Statement.
     */
    private CrossdataStatement statement;

    /**
     * Constructor class base in a BaseQuery and the statement.
     * @param baseQuery The base query.
     * @param statement The statement.
     */
    public SqlParsedQuery(BaseQuery baseQuery,
            SqlStatement statement) {
        super(baseQuery);
        this.statement = statement;
        setQueryStatus(QueryStatus.PARSED);
    }

    /**
     * Constructor class based in a parsedQuery.
     * @param parsedQuery A parsed query.
     */
    public SqlParsedQuery(SqlParsedQuery parsedQuery) {
        this(parsedQuery, parsedQuery.getStatement());
    }

    @Override
    public SqlStatement getStatement() {
        return (SqlStatement) statement;
    }

    @Override public String toString() {
        return statement.toString();
    }

}
