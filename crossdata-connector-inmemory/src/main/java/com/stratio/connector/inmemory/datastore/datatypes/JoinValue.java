package com.stratio.connector.inmemory.datastore.datatypes;

import com.stratio.connector.inmemory.datastore.selector.InMemoryJoinSelector;

/**
 * Created by lcisneros on 9/04/15.
 */
public class JoinValue extends SimpleValue{

    private String columnName;
    private String otherColumnName;


    public JoinValue(InMemoryJoinSelector join, Object value){
        super(null, value);
        this.columnName = join.getMyTerm().getColumnName().getName();
        this.otherColumnName =  join.getOtherTerm().getColumnName().getName();
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getOtherColumnName() {
        return otherColumnName;
    }

    public void setOtherColumnName(String otherColumnName) {
        this.otherColumnName = otherColumnName;
    }

}
