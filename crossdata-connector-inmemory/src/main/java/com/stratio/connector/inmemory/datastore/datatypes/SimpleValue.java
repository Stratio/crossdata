package com.stratio.connector.inmemory.datastore.datatypes;

import com.stratio.connector.inmemory.datastore.selector.InMemorySelector;

/**
 * Created by lcisneros on 9/04/15.
 */
public class SimpleValue {

    private InMemorySelector column;
    private Object value;


    public SimpleValue(InMemorySelector column, Object value){
        this.column = column;
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public InMemorySelector getColumn() {
        return column;
    }

    public void setColumn(InMemorySelector column) {
        this.column = column;
    }
}
