package com.stratio.sdh.meta.structures;

public class BooleanProperty extends ValueProperty {

    private boolean bool;

    public BooleanProperty(boolean bool) {
        this.type = TYPE_BOOLEAN;
        this.bool = bool;
    }   
    
    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }        
    
    @Override
    public String toString() {
        return Boolean.toString(bool);
    }
    
}
