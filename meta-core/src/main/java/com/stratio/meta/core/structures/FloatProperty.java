package com.stratio.meta.core.structures;

public class FloatProperty extends ValueProperty {

    private float number;

    public float getNumber() {
        return number;
    }

    public void setNumber(float number) {
        this.number = number;
    }

    public FloatProperty(float number) {
        this.number = number;
        this.type = TYPE_FLOAT;
  
    }

    @Override
    public String toString() {
            return Float.toString(number);
    }
    
}
