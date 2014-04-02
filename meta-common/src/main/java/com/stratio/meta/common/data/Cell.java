package com.stratio.meta.common.data;


import java.io.Serializable;

public class Cell implements Serializable {

    private final Class<?> datatype;
    private final Object value;

    public Cell(Class<?> datatype, Object value) {
        this.datatype = datatype;
        this.value = value;
    }

    public Class<?> getDatatype() {
        return datatype;
    }

    public Object getValue() {
        return value;
    }

}
