package com.stratio.meta.deep.functions;

import com.stratio.deep.entity.Cells;
import org.apache.spark.api.java.function.Function;

import java.io.Serializable;


public class NotEquals extends Function<Cells, Boolean> implements Serializable {
    private static final long serialVersionUID = 927384912608139416L;

    private Object value;
    private String field;

    public NotEquals(String field, Object value){
        this.value=value;
        this.field=field;
    }

    @Override
    public Boolean call(Cells cells) throws Exception {
        Object currentValue = cells.getCellByName(field).getCellValue();
        if (currentValue == null){
            if(value == null){
                return true;
            } else {
                return false;
            }
        }
        return !currentValue.equals(value);
    }
}
