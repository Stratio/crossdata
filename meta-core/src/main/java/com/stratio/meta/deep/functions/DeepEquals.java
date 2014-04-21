package com.stratio.meta.deep.functions;

import com.stratio.deep.entity.Cells;
import org.apache.spark.api.java.function.Function;

import java.io.Serializable;


public class DeepEquals extends Function<Cells, Boolean> implements Serializable{

    private static final long serialVersionUID = -6143471452730703044L;

    private Object value;
    private String field;

    public DeepEquals(String field, Object value){
        this.value=value;
        this.field=field;
    }

    @Override
    public Boolean call(Cells cells) throws Exception {
        Object currentValue = cells.getCellByName(field).getCellValue();
        if (currentValue == null){
            return value == null;
        }
        return currentValue.equals(value);
    }
}
