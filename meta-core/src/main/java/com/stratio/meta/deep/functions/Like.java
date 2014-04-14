package com.stratio.meta.deep.functions;

import com.stratio.deep.entity.Cells;
import org.apache.spark.api.java.function.Function;

import java.io.Serializable;

public class Like extends Function<Cells, Boolean> implements Serializable{
    private static final long serialVersionUID = 5642510017426647895L;

    private String field;
    private String regexp;

    public Like(String field, String regexp){
        this.field=field;
        this.regexp=regexp;
    }

    //TODO Exception Management
    @Override
    public Boolean call(Cells cells) throws Exception {
        Object currentValue = cells.getCellByName(field).getCellValue();
        if (currentValue == null){
            if(regexp == null){
                return true;
            } else {
                return false;
            }
        }
        return String.valueOf(currentValue).matches(regexp);
    }
}
