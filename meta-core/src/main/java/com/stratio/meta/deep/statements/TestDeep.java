package com.stratio.meta.deep.statements;

import com.stratio.deep.config.DeepJobConfigFactory;
import com.stratio.deep.config.IDeepJobConfig;
import com.stratio.deep.context.DeepSparkContext;
import com.stratio.deep.entity.Cell;
import com.stratio.deep.entity.Cells;
import com.stratio.deep.rdd.CassandraJavaRDD;
import com.stratio.meta.deep.functions.MapKeyForJoin;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmunoz on 8/04/14.
 */
public class TestDeep {
    protected DeepSparkContext context;
    protected CassandraJavaRDD<Cells> tweets;

    public static void main(String args[]){
        DeepSparkContext context = new DeepSparkContext("spark://gmunoz-Vostro-3550:7077", "Deep Test Take");
        context.addJar("/home/gmunoz/workspace/stratio-meta/meta-deep/target/meta-deep-1.0.0-SNAPSHOT-jar-with-dependencies.jar");

        //El método session nos permite asignar una sessión ya creada
        IDeepJobConfig<Cells> configT1 = DeepJobConfigFactory.create().host("localhost")
                .keyspace("twitter").table("tweets").inputColumns("id", "username").initialize();


        IDeepJobConfig<Cells> configT2 = DeepJobConfigFactory.create().host("localhost")
                .keyspace("twitter").table("tweetstest").inputColumns("id").initialize();



        CassandraJavaRDD<Cells> rdd = context.cassandraJavaRDD(configT1);

        JavaPairRDD<Long, Cells> tweetsRDD = rdd.map(new MapKeyForJoin<Long>("id"));

        CassandraJavaRDD<Cells> rdd2 = context.cassandraJavaRDD(configT2);

        JavaPairRDD<Long, Cells> testTweetsRDD = rdd2.map(new MapKeyForJoin<Long>("id"));

        JavaPairRDD<Long, Tuple2<Cells, Cells> > myjoin = tweetsRDD.join(testTweetsRDD);


        JavaRDD<Cells> result = myjoin.map(new Function<Tuple2<Long, Tuple2<Cells, Cells>>, Cells>() {
            @Override
            public Cells call(Tuple2<Long, Tuple2<Cells, Cells>> t) throws Exception {
                Cells cells1 = t._2()._1();
                Cells cells2 = t._2()._2();

                return new Cells(cells1.getCellByName("id"), cells1.getCellByName("username"));
            }
        });


        for(int i=0;i<10000;i=i+1000){
            int limitSup = i+1000;
            List<Cells> mycells = result.dropTake(i, limitSup);
        }

        System.exit(0);
    }

    void printCells(Cells cells){
        List<Cell> list = (ArrayList)cells.getCells();
        for(Cell cell : cells){
            System.out.println("Name: " + cell.getCellName() + ", value: " + cell.getCellValue());
        }
    }
}
