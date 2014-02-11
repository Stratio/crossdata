package com.stratio.sdh.meta.structures;

public abstract class ValueProperty {
    
    public final int TYPE_IDENT = 1;
    public final int TYPE_CONST = 2;
    public final int TYPE_MAPLT = 3;
    public final int TYPE_FLOAT = 4;
    
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
