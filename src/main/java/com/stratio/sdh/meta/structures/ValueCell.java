package com.stratio.sdh.meta.structures;

public abstract class ValueCell {

    public static final int TYPE_TERM = 1;
    public static final int TYPE_COLLECTION_LITERAL = 2;
    
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
