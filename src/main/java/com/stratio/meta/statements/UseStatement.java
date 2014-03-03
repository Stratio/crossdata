package com.stratio.meta.statements;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.stratio.meta.utils.DeepResult;
import com.stratio.meta.utils.MetaStep;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UseStatement extends MetaStatement {

    private String keyspaceName;

    public UseStatement(String keyspaceName) {
        this.keyspaceName = keyspaceName;
    }   
    
    public String getKeyspaceName() {
        return keyspaceName;
    }

    public void setKeyspaceName(String keyspaceName) {
        this.keyspaceName = keyspaceName;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("USE ");
        sb.append(keyspaceName);
        return sb.toString();
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public String getSuggestion() {
        return this.getClass().toString().toUpperCase()+" EXAMPLE";
    }

    @Override
    public String translateToCQL() {
        return this.toString();
    }

    @Override
    public String parseResult(ResultSet resultSet) {
        return "\t"+resultSet.toString();
    }

    @Override
    public Statement getDriverStatement() {
        Statement statement = null;
        return statement;
    }

    @Override
    public DeepResult executeDeep() {
        return new DeepResult("", new ArrayList<>(Arrays.asList("Not supported yet")));
    }
    
    @Override
    public List<MetaStep> getPlan() {
        return null;
    }
    
}
