package com.stratio.sdh.meta.statements;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.stratio.sdh.meta.structures.Path;

public abstract class MetaStatement {
    
    protected String query;

    public MetaStatement() {
    }

    public MetaStatement(String query) {
        this.query = query;
    }        
    
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }        
    
    public abstract Path estimatePath();
    
    @Override
    public abstract String toString();
    
    public abstract boolean validate();

    public abstract String getSuggestion();
    
    public abstract String translateToCQL();

    public abstract String parseResult(ResultSet resultSet);

    public abstract Statement getDriverStatement();
    
}
