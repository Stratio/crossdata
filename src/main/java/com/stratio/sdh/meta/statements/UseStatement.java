package com.stratio.sdh.meta.statements;

import com.stratio.sdh.meta.structures.Path;

public class UseStatement extends Statement {

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
        StringBuilder sb = new StringBuilder("Using keyspace ");
        sb.append(keyspaceName);
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
    
}
