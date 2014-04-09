package com.stratio.meta.deep.statements;

import com.datastax.driver.core.Session;
import com.stratio.deep.config.DeepJobConfigFactory;
import com.stratio.deep.config.IDeepJobConfig;
import com.stratio.deep.context.DeepSparkContext;
import com.stratio.deep.entity.Cells;
import com.stratio.deep.rdd.CassandraJavaRDD;
import com.stratio.meta.deep.functions.MapKeyForJoin;
import org.apache.spark.api.java.JavaPairRDD;

/**
 * Created by gmunoz on 9/04/14.
 */
public class DeepMapStatement {
    private String tableName;
    private String key;
    private IDeepJobConfig<Cells> config;
    private DeepSparkContext context;
    private CassandraJavaRDD<Cells> rdd;

    public DeepMapStatement(DeepSparkContext context){
        this.context=context;
        config = DeepJobConfigFactory.create();
    }

    public DeepMapStatement host(String host){
        config = config.host(host);
        return this;
    }

    public DeepMapStatement keyspace(String keyspace){
        config = config.keyspace(keyspace);
        return this;
    }

    public DeepMapStatement table(String table){
        config = config.table(table);
        return this;
    }

    public DeepMapStatement intputColumns(String... columns){
        config = config.inputColumns(columns);
        return this;
    }

    public DeepMapStatement session(Session session){
        config = config.session(session);
        return this;
    }

    public DeepMapStatement rpcPort(int rpcPort){
        config = config.rpcPort(rpcPort);
        return this;
    }

    public DeepMapStatement map(){
        config = config.initialize();
        rdd = context.cassandraJavaRDD(config);
        return this;
    }

    public JavaPairRDD on(String field){
        return rdd.map(new MapKeyForJoin<Long>("id"));
    }
}
