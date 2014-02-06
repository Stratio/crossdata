package com.stratio.sdh.meta.structures;

public class ConstantProperty extends ValueProperty {
    
    private int constant;

    public ConstantProperty(int constant) {
        this.constant = constant;
        this.type = TYPE_CONST;
    }
    
    public int getConstant() {
        return constant;
    }

    public void setConstant(int constant) {
        this.constant = constant;
    }

    @Override
    public String toString() {
        return Integer.toString(constant);
    }
    
    

}
