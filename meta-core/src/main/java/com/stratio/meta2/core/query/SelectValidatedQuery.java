package com.stratio.meta2.core.query;

import com.stratio.meta.common.result.QueryStatus;
import com.stratio.meta.common.statements.structures.relationships.Relation;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.metadata.TableMetadata;
import com.stratio.meta2.core.statements.SelectStatement;

import java.util.List;

/**
 * Created by jjlopez on 18/09/14.
 */
public  class SelectValidatedQuery extends ValidatedQuery {
    public SelectValidatedQuery(ParsedQuery parsedQuery) {
        super(parsedQuery);
    }

    SelectValidatedQuery(NormalizedQuery normalizedQuery) {
        super(normalizedQuery);
    }


    public QueryStatus getStatus() {
        return QueryStatus.VALIDATED;
    }

    //IStatement Methods move to MetaStatement
    public List<CatalogName> getCatalogs(){
        throw new UnsupportedOperationException();
    }


    public List<TableName> getTables(){
        throw new UnsupportedOperationException();
    }


    public List<ColumnName> getColumns(){
        throw new UnsupportedOperationException();
    }


    public List<Relation> getAssignations(){
        throw new UnsupportedOperationException();
    }

    public List<Relation> getRelationships(){
        throw new UnsupportedOperationException();
    }

    public List<TableMetadata> getTableMetadata(){
        throw new UnsupportedOperationException();
    }

    @Override
    public SelectStatement getStatement() {
        return (SelectStatement)statement;
    }
}
