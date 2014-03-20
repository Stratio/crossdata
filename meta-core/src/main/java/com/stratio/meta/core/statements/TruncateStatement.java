package com.stratio.meta.core.statements;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;

import static com.datastax.driver.core.querybuilder.QueryBuilder.truncate;

import com.datastax.driver.core.querybuilder.Truncate;
import com.stratio.meta.core.utils.DeepResult;
import com.stratio.meta.core.utils.MetaStep;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TruncateStatement extends MetaStatement {
    
    private boolean keyspaceInc = false;
    private String keyspace;
    private String ident;
    
    public TruncateStatement(String ident){
        if(ident.contains(".")){
            String[] ksAndTablename = ident.split("\\.");
            keyspace = ksAndTablename[0];
            ident = ksAndTablename[1];
            keyspaceInc = true;
        }
        this.ident = ident;
    }

    public boolean isKeyspaceInc() {
        return keyspaceInc;
    }

    public void setKeyspaceInc(boolean keyspaceInc) {
        this.keyspaceInc = keyspaceInc;
    }

    public String getKeyspace() {
        return keyspace;
    }

    public void setKeyspace(String keyspace) {
        this.keyspace = keyspace;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TRUNCATE ");
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
    public Statement getDriverStatement() {
        Truncate truncateQuery;
        if(keyspaceInc){
            truncateQuery = truncate(keyspace, ident);
        } else {
            truncateQuery = truncate(ident);
        }
        return truncateQuery;
    }
    
//    @Override
//    public String parseResult(ResultSet resultSet) {
//        return "Executed successfully"+System.getProperty("line.separator");
//    }    
    
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
