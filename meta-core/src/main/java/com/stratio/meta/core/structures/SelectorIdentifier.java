package com.stratio.meta.core.structures;

public class SelectorIdentifier extends SelectorMeta {

    private String identifier;

    public SelectorIdentifier(String identifier) {
        this.type = TYPE_IDENT;
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }        
    
    public boolean isColumnSelector(){
        return identifier.contains(".");
    }
    
    public String getTablename(){
        if(identifier.contains(".")){
            return identifier.split("\\.")[0];
        }
        return identifier;
    }
    
    public String getColumnName(){
        if(identifier.contains(".")){
            return identifier.split("\\.")[1];
        }
        return identifier;
    }
    
    @Override
    public String toString() {
        return identifier;
    }
    
}
