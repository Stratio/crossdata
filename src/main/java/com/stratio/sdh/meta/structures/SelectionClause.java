package com.stratio.sdh.meta.structures;

public abstract class SelectionClause {
    
    public static final int TYPE_SELECTION = 1;
    public static final int TYPE_COUNT = 2;
    
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
