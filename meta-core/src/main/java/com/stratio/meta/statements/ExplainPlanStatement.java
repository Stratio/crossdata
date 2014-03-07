package com.stratio.meta.statements;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.stratio.meta.utils.DeepResult;
import com.stratio.meta.utils.MetaStep;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExplainPlanStatement extends MetaStatement {
    
    private MetaStatement metaStatement;

    public ExplainPlanStatement(MetaStatement metaStatement) {
        this.metaStatement = metaStatement;
    }    
    
    public MetaStatement getMetaStatement() {
        return metaStatement;
    }

    public void setMetaStatement(MetaStatement metaStatement) {
        this.metaStatement = metaStatement;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Explain plan for ");
        sb.append(metaStatement.toString());
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
