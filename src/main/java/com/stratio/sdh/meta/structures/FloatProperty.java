/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stratio.sdh.meta.structures;

/**
 *
 * @author aalcocer
 */
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
        System.out.println("el numero float es" + number);
    }

    
    
    @Override
    public String toString() {
            return Float.toString(number);
    }
    
}
