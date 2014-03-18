package com.stratio.meta.core.structures;

public class WindowTime extends WindowSelect {

    private int num;
    private TimeUnit unit;

    public WindowTime(int num, TimeUnit unit) {
        this.type = TYPE_TIME;
        this.num = num;
        this.unit = unit;
    }   
    
    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public void setTime(TimeUnit unit) {
        this.unit = unit;
    }
            
    @Override
    public String toString() {
        return num+" "+unit;
    }
    
}
