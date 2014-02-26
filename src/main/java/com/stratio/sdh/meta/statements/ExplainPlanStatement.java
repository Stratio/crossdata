package com.stratio.sdh.meta.statements;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.stratio.sdh.meta.structures.Path;

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
    public Path estimatePath() {
        return Path.CASSANDRA;
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
    
}
