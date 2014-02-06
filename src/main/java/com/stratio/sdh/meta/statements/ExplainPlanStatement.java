package com.stratio.sdh.meta.statements;

import com.stratio.sdh.meta.structures.Path;

public class ExplainPlanStatement extends Statement {
    
    private String metaStatement;

    public ExplainPlanStatement(String metaStatement) {
        this.metaStatement = metaStatement;
    }    
    
    public String getMetaStatement() {
        return metaStatement;
    }

    public void setMetaStatement(String metaStatement) {
        this.metaStatement = metaStatement;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Explaing plan for: ");
        sb.append(metaStatement);
        return sb.toString();
    }   

    @Override
    public Path estimatePath() {
        return Path.CASSANDRA;
    }
    
}
