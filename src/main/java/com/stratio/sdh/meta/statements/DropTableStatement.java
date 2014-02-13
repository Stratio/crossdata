package com.stratio.sdh.meta.statements;

import com.stratio.sdh.meta.structures.Path;

public class DropTableStatement extends Statement {
    
    private String ident;
    private boolean ifExists;

    public DropTableStatement(String ident, boolean ifExists) {
        this.ident = ident;
        this.ifExists = ifExists;
    }
    
    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
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
        sb.append(ident);                
        return sb.toString();
    }

    @Override
    public Path estimatePath() {
        return Path.CASSANDRA;
    }
            
}
