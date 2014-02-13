package com.stratio.sdh.meta.structures;

public class SelectionAsterisk extends Selection {

    public SelectionAsterisk() {
        this.type = TYPE_ASTERISK;
    }   
    
    @Override
    public String toString() {
        return "*";
    }
    
}
