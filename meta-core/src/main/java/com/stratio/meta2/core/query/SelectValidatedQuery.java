package com.stratio.meta2.core.query;

/**
 * Created by jjlopez on 18/09/14.
 */
public abstract class SelectValidatedQuery extends ParsedQuery {
    public SelectValidatedQuery(SelectParsedQuery parsedQuery) {
        super(parsedQuery);
    }

    SelectValidatedQuery(NormalizedQuery normalizedQuery) {
        super(normalizedQuery);
    }


}
