package com.stratio.meta.core.statements;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.stratio.meta.core.utils.DeepResult;
import com.stratio.meta.core.utils.MetaStep;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DropKeyspaceStatement extends MetaStatement {
    
    private String keyspaceName;
    private boolean ifExists;  

    public DropKeyspaceStatement(String keyspaceName, boolean ifExists) {
        this.keyspaceName = keyspaceName;
        this.ifExists = ifExists;
    }    
    
    public String getKeyspaceName() {
        return keyspaceName;
    }

    public void setKeyspaceName(String keyspaceName) {
        this.keyspaceName = keyspaceName;
    }

    public boolean isIfExists() {
        return ifExists;
    }

    public void setIfExists(boolean ifExists) {
        this.ifExists = ifExists;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Drop keyspace ");        
        if(ifExists){
           sb.append("if exists ");
        } 
        sb.append(keyspaceName);                 
        return sb.toString();
    }

    @Override
    public void validate() {
        
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
        return "Executed successfully"+System.getProperty("line.separator");
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
        ArrayList<MetaStep> steps = new ArrayList<>();
        return steps;
    }
    
}
