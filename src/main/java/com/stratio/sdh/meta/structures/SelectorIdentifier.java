package com.stratio.sdh.meta.structures;

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
    
    @Override
    public String toString() {
        return identifier;
    }
    
}
