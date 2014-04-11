package com.stratio.meta.deep.functions;

import com.stratio.deep.entity.Cells;
import org.apache.spark.api.java.function.Function;

import java.io.Serializable;

public class LessEqualThan extends Function<Cells, Boolean> implements Serializable{

    private static final long serialVersionUID = 2775666112428131116L;

    private Object value;
    private String field;

    public LessEqualThan(String field, Object value){
        this.value=value;
        this.field=field;
    }

    @Override
    public Boolean call(Cells cells) throws Exception {
        Object obj = cells.getCellByName(field).getCellValue();
        return ((Comparable)obj).compareTo(value) != 1;
    }
}
