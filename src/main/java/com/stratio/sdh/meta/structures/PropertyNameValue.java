package com.stratio.sdh.meta.structures;

public class PropertyNameValue extends MetaProperty{

    private String name;
    private ValueProperty vp;
    
    public PropertyNameValue() {
        super(TYPE_NAME_VALUE);
    }

    public PropertyNameValue(String name, ValueProperty vp) {
        super(TYPE_NAME_VALUE);
        this.name = name;
        this.vp = vp;
    }        

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ValueProperty getVp() {
        return vp;
    }

    public void setVp(ValueProperty vp) {
        this.vp = vp;
    }        
    
}
