package com.stratio.meta.deep.functions;

import com.stratio.deep.entity.Cell;
import com.stratio.deep.entity.Cells;
import org.apache.spark.api.java.function.Function;
import scala.Tuple2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gmunoz on 9/04/14.
 */
public class JoinCells<T> extends Function<Tuple2<T, Tuple2<Cells, Cells>>, Cells> {

    private static final long serialVersionUID = 4534397129761833793L;
    private String key1;
    private String key2;
    private Map<String, Object> keys;

    public JoinCells(String key1, String key2){
        this.key1=key1;
        this.key2=key2;
        new HashMap<String,Object>();
    }

    @Override
    public Cells call(Tuple2<T, Tuple2<Cells, Cells>> result) throws Exception {
        Cells left = result._2()._1();
        Cells right = result._2()._2();
        Cells joinedCells = new Cells();

        //TODO What if two equals keys
        for(Cell cell : left.getCells()){
            joinedCells.add(cell);
        }

        for(Cell cell : right.getCells()){
            if(!cell.getCellName().equals(key1))
                joinedCells.add(cell);
        }

        return joinedCells;
    }
}
