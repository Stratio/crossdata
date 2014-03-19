package com.stratio.meta.core.utils;

import com.stratio.meta.common.result.MetaResult;
import com.stratio.meta.core.statements.MetaStatement;
import com.stratio.meta.core.planner.Planner;

public class MetaQuery {
    
    private String query;
    private boolean hasError;
    private MetaStatement statement;
    private Planner plan;
    private MetaResult result;
    
    public MetaQuery() {
        this.hasError = false;
        result = new MetaResult();
    }
    
    public MetaQuery(String query) {
        this.hasError = false;
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
    
    public Planner getPlan() {
        return plan;
    }

    public void setPlan(Planner plan) {
        this.plan = plan;
    }
    
    public void setResult(MetaResult result) {
        this.result = result;
    }

    public MetaResult getResult() {
        return result;
    }
    
}
