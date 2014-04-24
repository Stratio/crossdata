package com.stratio.meta.deep.functions;

import com.stratio.deep.entity.Cells;
import org.apache.spark.api.java.function.Function;

import java.io.Serializable;

public class GreaterEqualThan extends Function<Cells, Boolean> implements Serializable {

    private static final long serialVersionUID = 2927596112428729111L;

    private Object value;
    private String field;

    public GreaterEqualThan(String field, Object value){
        this.value=value;
        this.field=field;
    }

    @Override
    public Boolean call(Cells cells){
        Object currentValue = cells.getCellByName(field).getCellValue();
        if (currentValue == null){
            return value == null;
        }
        return ((Comparable) currentValue).compareTo(value) >= 0;
    }
}
