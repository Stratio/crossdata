package com.stratio.meta.deep.functions;

import com.stratio.deep.entity.Cells;
import org.apache.spark.api.java.function.Function;

import java.io.Serializable;

public class LessThan extends Function<Cells, Boolean> implements Serializable {

    private static final long serialVersionUID = 2675616112608139116L;

    private Object value;
    private String field;

    public LessThan(String field, Object value){
        this.value=value;
        this.field=field;
    }

    @Override
    public Boolean call(Cells cells){
        Object obj = cells.getCellByName(field).getCellValue();
        return ((Comparable)obj).compareTo(value) == -1;
    }
}
