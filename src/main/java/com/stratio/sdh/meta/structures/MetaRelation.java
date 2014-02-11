package com.stratio.sdh.meta.structures;

public abstract class MetaRelation {
    
    public static final int TYPE_ONE_TERM = 1;
    public static final int TYPE_TERMS = 2;
    public static final int TYPE_INTERROGATION = 3;
    
    protected String identifier;
    protected int type;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }        
    
    @Override
    public abstract String toString();
    
}
