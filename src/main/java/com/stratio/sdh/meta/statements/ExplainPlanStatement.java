package com.stratio.sdh.meta.statements;

import com.stratio.sdh.meta.structures.Path;

public class ExplainPlanStatement extends Statement {
    
    private Statement metaStatement;

    public ExplainPlanStatement(Statement metaStatement) {
        this.metaStatement = metaStatement;
    }    
    
    public Statement getMetaStatement() {
        return metaStatement;
    }

    public void setMetaStatement(Statement metaStatement) {
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
    
}
