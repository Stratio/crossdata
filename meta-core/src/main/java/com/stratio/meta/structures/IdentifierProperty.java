package com.stratio.meta.structures;

public class IdentifierProperty extends ValueProperty {
    
    private String identifier;

    public IdentifierProperty(String identifier) {
        this.identifier = identifier;
        this.type = TYPE_IDENT;
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
