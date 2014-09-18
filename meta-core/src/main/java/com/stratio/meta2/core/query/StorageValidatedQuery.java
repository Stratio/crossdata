package com.stratio.meta2.core.query;

import com.stratio.meta.common.result.QueryStatus;
import com.stratio.meta2.core.statements.MetaStatement;
import com.stratio.meta2.core.statements.StorageStatement;

/**
 * Created by jjlopez on 18/09/14.
 */
public class StorageValidatedQuery extends ValidatedQuery {

    public StorageValidatedQuery(BaseQuery baseQuery,
        MetaStatement statement) {
        super(baseQuery, statement);
    }

    StorageValidatedQuery(ParsedQuery parsedQuery) {
        super(parsedQuery);
    }

    public StorageStatement getStatement(){
        return (StorageStatement)statement;
    }
}
