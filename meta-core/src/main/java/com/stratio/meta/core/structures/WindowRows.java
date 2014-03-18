package com.stratio.meta.core.structures;

public class WindowRows extends WindowSelect {

    private int num;

    public WindowRows(int num) {
        this.type = TYPE_ROWS;
        this.num = num;
    }   
    
    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }        
    
    @Override
    public String toString() {
        return num+" ROWS";
    }
    
}
