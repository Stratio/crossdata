package com.stratio.meta.core.statements;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.stratio.meta.core.utils.DeepResult;
import com.stratio.meta.core.utils.MetaStep;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DropTriggerStatement extends MetaStatement{

    private String ident;

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    private String ident2;

    public String getIdent2() {
        return ident2;
    }

    public void setIdent2(String ident2) {
        this.ident2 = ident2;
    }

    public DropTriggerStatement(String ident, String ident2) {
        this.ident = ident;
        this.ident2 = ident2;
    }

    @Override
    public String toString() {
    StringBuilder sb = new StringBuilder("Drop trigger ");
        sb.append(ident);
        sb.append(" on ");
        sb.append(ident2);
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
        ArrayList<MetaStep> steps = new ArrayList<>();
        return steps;
    }
    
}
