package com.stratio.meta.deep.functions;

import com.stratio.deep.entity.Cells;
import org.apache.spark.api.java.function.Function;

import java.io.Serializable;

public class GreaterThan extends Function<Cells, Boolean> implements Serializable {
    private static final long serialVersionUID = 2675616112608139116L;

    private Object value;
    private String field;

    public GreaterThan(String field, Object value){
        this.value=value;
        this.field=field;
    }

    @Override
    public Boolean call(Cells cells) throws Exception {
        Object obj = cells.getCellByName(field).getCellValue();
        return ((Comparable)obj).compareTo(value) == 1;
    }
}
