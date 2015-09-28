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
import com.stratio.crossdata.core.statements.SelectStatement;

/**
 * Class that implements the logic of a parsed select statement.
 */
public class SelectParsedQuery extends BaseQuery implements IParsedQuery {

    /**
     * The select statement.
     */
    private SelectStatement statement;

    /**
     * The select parsed query of the nested subquery.
     */
    private SelectParsedQuery childSelectParsedQuery;

    /**
     * Constructor class based in a Base Query and a Statement.
     * @param baseQuery The query previous to be parsed.
     * @param statement The statement with the query.
     */
    public SelectParsedQuery(BaseQuery baseQuery,
            SelectStatement statement) {
        super(baseQuery);
        this.statement = statement;
        SelectStatement subquery = statement.getSubquery();
        if(subquery != null){
            childSelectParsedQuery = new SelectParsedQuery(baseQuery,subquery);
        }
        setQueryStatus(QueryStatus.PARSED);

    }

    /**
     * Constructor class based in a parsed query.
     * @param parsedQuery The parsed query.
     */
    public SelectParsedQuery(SelectParsedQuery parsedQuery) {
        this(parsedQuery, parsedQuery.getStatement());
    }

    @Override
    public SelectStatement getStatement() {
        return statement;
    }

    /**
     * Get the inner parsed query.
     * @return The parsed query of the subquery.
     */
    public SelectParsedQuery getChildParsedQuery() {
        return childSelectParsedQuery;
    }

    @Override
    public String toString() {
        return statement.toString();
    }

}
