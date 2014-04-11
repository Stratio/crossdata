package com.stratio.meta.deep.functions;

import com.stratio.deep.entity.Cells;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.io.Serializable;

/**
 * Created by gmunoz on 9/04/14.
 */
public class MapKeyForJoin<T> extends PairFunction<Cells, T, Cells> implements Serializable{
    private static final long serialVersionUID = -6677647619149716567L;

    private String key;

    public MapKeyForJoin(String key){
        this.key=key;
    }

    @Override
    public Tuple2<T, Cells> call(Cells cells) throws Exception {
        return new Tuple2<T, Cells>((T)cells.getCellByName(key).getCellValue(),cells);
    }
}
