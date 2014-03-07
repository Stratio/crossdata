package com.stratio.meta.structures;

public abstract class WindowSelect {
    
    public static final int TYPE_LAST = 1;
    public static final int TYPE_ROWS = 2;
    public static final int TYPE_TIME = 3;
    
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
