package com.stratio.sdh.meta.statements;

import com.stratio.sdh.meta.structures.Path;

public class SelectStatement extends Statement {

    public SelectStatement() {
    }   
    
    @Override
    public Path estimatePath() {
        return Path.CASSANDRA;
    }

    @Override
    public String toString() {
        return "SELECT ";
    }
    
}
