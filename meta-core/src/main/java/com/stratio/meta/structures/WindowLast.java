package com.stratio.meta.structures;

public class WindowLast extends WindowSelect {

    public WindowLast() {
        this.type = TYPE_LAST;
    }        

    @Override
    public String toString() {
        return "LAST";
    }
    
}
