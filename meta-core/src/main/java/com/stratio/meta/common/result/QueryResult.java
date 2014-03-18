package com.stratio.meta.common.result;

import com.datastax.driver.core.ResultSet;

public class QueryResult implements MetaResult {
    
    private ResultSet resultSet;

    public QueryResult(ResultSet resultSet) {
        this.resultSet = resultSet;
    }   
    
    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
    
    @Override
    public void print(){
        System.out.println(resultSet.toString());
    }
}
