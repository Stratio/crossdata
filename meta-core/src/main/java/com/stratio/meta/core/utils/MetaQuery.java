package com.stratio.meta.core.utils;

import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.core.statements.MetaStatement;
import com.stratio.meta.core.planner.MetaPlan;

public class MetaQuery {
    
    private String query;
    private boolean hasError;
    private MetaStatement statement;
    private MetaPlan plan;
    private QueryResult result;
    
    public MetaQuery() {
        this.hasError = false;
        result = new QueryResult();
    }
    
    public MetaQuery(String query) {
        this();
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
    
    public void setQuery(String query) {
        this.query = query;
    }
    
    public boolean hasError() {
        return hasError;
    }
    
    public void setError(){
        hasError = true;
    }
    
    public void setErrorMessage(String errorMsg) {
        hasError = true;
        result.setErrorMessage(errorMsg);
    }
    
    public void setStatement(MetaStatement statement) {
        this.statement = statement;
    }    
    
    public MetaStatement getStatement() {
        return statement;
    } 
    
    public MetaPlan getPlan() {
        return plan;
    }

    public void setPlan(MetaPlan plan) {
        this.plan = plan;
    }
    
    public void setResult(QueryResult result) {
        this.result = result;
    }

    public QueryResult getResult() {
        return result;
    }
    
}
