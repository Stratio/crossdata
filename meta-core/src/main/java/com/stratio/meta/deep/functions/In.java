package com.stratio.meta.deep.functions;

import com.stratio.deep.entity.Cells;
import org.apache.spark.api.java.function.Function;

import java.io.Serializable;
import java.util.List;


public class In extends Function<Cells, Boolean> implements Serializable{

    private static final long serialVersionUID = -6637139616271541577L;

    private String field;
    private List<String> inIDs;

    public In(String field, List<String> inIDs){
        this.field=field;
        this.inIDs=inIDs;
    }

    @Override
    public Boolean call(Cells cells) throws Exception {
        Object currentValue = cells.getCellByName(field).getCellValue();
        if (currentValue == null){
            if(inIDs == null){
                return true;
            } else {
                return false;
            }
        }
        return inIDs.contains(String.valueOf(currentValue));
    }
}
