package com.stratio.meta.structures;

public abstract class ValueProperty {
    
    public static final int TYPE_IDENT = 1;
    public static final int TYPE_CONST = 2;
    public static final int TYPE_MAPLT = 3;
    public static final int TYPE_FLOAT = 4;
    public static final int TYPE_BOOLEAN = 5;
    public static final int TYPE_LITERAL = 6;
    
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
