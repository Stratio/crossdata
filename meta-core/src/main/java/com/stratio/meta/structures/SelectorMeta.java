package com.stratio.meta.structures;

public abstract class SelectorMeta {
    
    public static final int TYPE_IDENT = 1;
    public static final int TYPE_FUNCTION = 2;
    
    protected int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }        
    
    @Override
    public abstract String toString();
    
}
