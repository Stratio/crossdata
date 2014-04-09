package com.stratio.meta.deep.statements;

import org.apache.spark.api.java.JavaPairRDD;
import scala.Tuple2;

/**
 * Created by gmunoz on 9/04/14.
 */
public class DeepSelectStatement {

    private JavaPairRDD tableLeft;
    private JavaPairRDD tableRight;
    private Tuple2<String, String> keys;

    public DeepSelectStatement(){
    }

    public DeepSelectStatement join(JavaPairRDD rdd){
        setTableLeft(rdd);
        return this;
    }

    public DeepSelectStatement on(JavaPairRDD rdd){
        setTableRight(rdd);
        return this;
    }

    public void execute(){
        tableLeft.join(tableRight);
    }

    public Tuple2<String, String> getKeys() {
        return keys;
    }

    public void setKeys(Tuple2<String, String> keys) {
        this.keys = keys;
    }

    public JavaPairRDD getTableRight() {
        return tableRight;
    }

    public void setTableRight(JavaPairRDD tableRight) {
        this.tableRight = tableRight;
    }

    public JavaPairRDD getTableLeft() {
        return tableLeft;
    }

    public void setTableLeft(JavaPairRDD tableLeft) {
        this.tableLeft = tableLeft;
    }
}
