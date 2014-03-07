package com.stratio.meta.structures;

public abstract class Selection {
    
    public static final int TYPE_SELECTOR = 1;
    public static final int TYPE_ASTERISK = 2;
 
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
