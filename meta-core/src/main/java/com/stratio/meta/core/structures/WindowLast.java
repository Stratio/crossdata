package com.stratio.meta.core.structures;

public class WindowLast extends WindowSelect {

    public WindowLast() {
        this.type = TYPE_LAST;
    }        

    @Override
    public String toString() {
        return "LAST";
    }
    
}
