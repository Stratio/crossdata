package com.stratio.meta.common.result;

import com.datastax.driver.core.ResultSet;

public class QueryResult extends MetaResult {
    
    private ResultSet resultSet;
    private String message;

    public QueryResult() {
    }    
    
    public QueryResult(ResultSet resultSet) {
        this.resultSet = resultSet;
    }   

    public QueryResult(String message) {
        this.message = message;
    }        
    
    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }        
    
    @Override
    public void print(){
        System.out.println(resultSet.toString());
    }
}
