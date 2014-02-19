package com.stratio.sdh.meta.statements;

import com.stratio.sdh.meta.structures.Path;

public class StopProcessStatement extends Statement {

    private String ident;

    public StopProcessStatement(String ident) {
        this.ident = ident;
    }    
    
    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }
    
    @Override
    public Path estimatePath() {
        return Path.CASSANDRA;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Stop process ");
        sb.append(ident);
        return sb.toString();
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public String getSuggestion() {
        return this.getClass().toString().toUpperCase()+" EXAMPLE";
    }
           
}
