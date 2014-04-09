/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.deep;

import com.datastax.driver.core.Session;
import com.stratio.deep.config.DeepJobConfigFactory;
import com.stratio.deep.config.IDeepJobConfig;
import com.stratio.deep.context.DeepSparkContext;
import com.stratio.deep.rdd.CassandraJavaRDD;
import com.stratio.meta.common.data.CassandraResultSet;
import com.stratio.meta.common.data.Cell;
import com.stratio.meta.common.data.ResultSet;
import com.stratio.meta.common.data.Row;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.statements.MetaStatement;
import com.stratio.meta.core.statements.SelectStatement;
import com.stratio.meta.deep.context.Context;
import com.stratio.meta.deep.utils.DeepUtils;
import org.apache.spark.api.java.JavaRDD;

import java.util.ArrayList;
import java.util.List;

public class Bridge {

//    public static final DeepSparkContext deepContext = new DeepSparkContext(Context.cluster, Context.jobName);

    public static int executeCount(String keyspaceName, String tableName, Session cassandraSession){

        System.out.println("TRACE: Deep.executionCount");

        DeepSparkContext deepContext = new DeepSparkContext(Context.cluster, Context.jobName);

        System.out.println("TRACE: Executing deep for: "+keyspaceName+"."+tableName);

        // Configuration and initialization
        IDeepJobConfig config = DeepJobConfigFactory.create().session(cassandraSession)
                .host(Context.cassandraHost).rpcPort(Context.cassandraPort)
                .keyspace(keyspaceName).table(tableName).initialize();

        System.out.println("TRACE: table = " + cassandraSession.getCluster().getMetadata().getKeyspace(keyspaceName).getTable(tableName).asCQLQuery());

        System.out.println("TRACE: Cluster in Deep: " + config.getSession().getCluster().getClusterName());
        System.out.println("TRACE: Keyspace in Deep: " + config.getKeyspace());
        System.out.println("TRACE: Table in Deep: "+config.getTable());
        System.out.println("TRACE: Session in Deep: " + config.getSession().toString());
        System.out.println("TRACE: columnDefinitions in Deep: " + config.columnDefinitions().toString());
        System.out.println("TRACE: Number of columns: "+config.columnDefinitions().size());
        // Creating the RDD
        CassandraJavaRDD rdd = deepContext.cassandraJavaRDD(config);

        System.out.println("TRACE: CassandraJavaRDD created");

        Long rddCount = rdd.count();

        System.out.println("TRACE: Rows in the RDD (JavaClass): " + rddCount.toString());

        deepContext.stop();

        System.out.println("TRACE: Deep context stopped");

        return rddCount.intValue();

    }

    public static ResultSet execute(MetaStatement stmt, List<Result> resultsFromChildren, boolean isRoot, Session cassandraSession){

        System.out.println("TRACE: Deep.execution");

        DeepSparkContext deepContext = new DeepSparkContext(Context.cluster, Context.jobName);

        System.out.println("TRACE: Executing deep for: "+stmt.toString());

        if(!(stmt instanceof SelectStatement)){
            List oneRow = new ArrayList<Row>();
            oneRow.add(new Row("RESULT", new Cell(String.class, "NOT supported yet")));
            return new CassandraResultSet(oneRow);
        }

        SelectStatement ss = (SelectStatement) stmt;

        if(resultsFromChildren.isEmpty()){ // LEAF
            // Configuration and initialization
            IDeepJobConfig config = DeepJobConfigFactory.create().session(cassandraSession)
                    .host(Context.cassandraHost).rpcPort(Context.cassandraPort)
                    .keyspace(ss.getKeyspace()).table(ss.getTableName()).initialize();

            // Creating the RDD
            CassandraJavaRDD rdd = deepContext.cassandraJavaRDD(config);

            // Filter columns

            // Return RDD
            return returnResult(rdd, isRoot);

        } else {
            // Retrieve RDDs from children

            // Perform operation
            JavaRDD rdd = null;

            // Create and return RDD.toResultSet
            deepContext.stop();

            return returnResult(rdd, isRoot);
        }

    }

    /*public static void stopContext(){
        deepContext.stop();
    }*/

    private static ResultSet returnResult(JavaRDD rdd, boolean isRoot){
        if(isRoot){
            return DeepUtils.convertRDDtoResultSet(rdd);
        } else {
            List oneRow = new ArrayList<Row>();
            oneRow.add(new Row("RESULT", new Cell(CassandraJavaRDD.class, rdd)));
            return new CassandraResultSet(oneRow);
        }
    }


}
