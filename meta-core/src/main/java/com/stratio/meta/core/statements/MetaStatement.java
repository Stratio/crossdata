package com.stratio.meta.core.statements;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.stratio.meta.core.utils.MetaPath;
import com.stratio.meta.core.utils.DeepResult;
import com.stratio.meta.core.utils.MetaStep;
import java.util.List;

public abstract class MetaStatement {
    
    protected String query;
    protected MetaPath path;

    public MetaStatement() {
    }

    public MetaStatement(String query, MetaPath path) {
        this.query = query;
        this.path = path;
    }        
    
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }        

    public MetaPath getPath() {
        return path;
    }

    public void setPath(MetaPath path) {
        this.path = path;
    }        
    
    @Override
    public abstract String toString();
    
    public abstract void validate();

    public abstract String getSuggestion();
    
    public abstract String translateToCQL();

    public abstract String parseResult(ResultSet resultSet);

    public abstract Statement getDriverStatement();

    public abstract DeepResult executeDeep();
    
    public abstract List<MetaStep> getPlan();
    
}
