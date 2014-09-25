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

package com.stratio.meta2.core.query;

import com.stratio.meta.common.result.QueryStatus;
import com.stratio.meta2.core.statements.MetaStatement;
import com.stratio.meta2.core.statements.MetadataStatement;

public class MetadataParsedQuery extends BaseQuery implements ParsedQuery {

    private MetaStatement statement;

    public MetadataParsedQuery(BaseQuery baseQuery,
            MetadataStatement statement) {
        super(baseQuery);
        this.statement = statement;
        setQueryStatus(QueryStatus.PARSED);
    }

    public MetadataParsedQuery(MetadataParsedQuery parsedQuery) {
        this(parsedQuery, parsedQuery.getStatement());
    }

    @Override
    public MetadataStatement getStatement() {
        return (MetadataStatement) statement;
    }

    @Override public String toString() {
        return statement.toString();
    }
}
