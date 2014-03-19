package com.stratio.meta.core.statements;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.stratio.meta.core.utils.DeepResult;
import com.stratio.meta.core.utils.MetaStep;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DropTableStatement extends MetaStatement {
    
    private boolean keyspaceInc = false;
    private String keyspace;
    private String ident;
    private boolean ifExists;

    public DropTableStatement(String ident, boolean ifExists) {
        if(ident.contains(".")){
            String[] ksAndTablename = ident.split("\\.");
            keyspace = ksAndTablename[0];
            ident = ksAndTablename[1];
            keyspaceInc = true;
        }
        this.ident = ident;
        this.ifExists = ifExists;
    }
    
    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        if(ident.contains(".")){
            String[] ksAndTablename = ident.split("\\.");
            keyspace = ksAndTablename[0];
            ident = ksAndTablename[1];
            keyspaceInc = true;
        }
        this.ident = ident;
    }

    public boolean isIfExists() {
        return ifExists;
    }

    public void setIfExists(boolean ifExists) {
        this.ifExists = ifExists;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Drop table ");
        if(ifExists){
            sb.append("if exists ");
        }       
        if(keyspaceInc){
            sb.append(keyspace).append(".");
        }
        sb.append(ident);                
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
        return "Executed successfully";
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
