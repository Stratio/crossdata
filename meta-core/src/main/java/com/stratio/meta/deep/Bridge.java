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
import com.stratio.deep.entity.Cells;
import com.stratio.deep.rdd.CassandraJavaRDD;
import com.stratio.meta.common.data.CassandraResultSet;
import com.stratio.meta.common.data.Cell;
import com.stratio.meta.common.data.ResultSet;
import com.stratio.meta.common.data.Row;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.statements.MetaStatement;
import com.stratio.meta.core.statements.SelectStatement;
import com.stratio.meta.core.structures.*;
import com.stratio.meta.deep.context.Context;
import com.stratio.meta.deep.functions.*;
import com.stratio.meta.deep.utils.DeepUtils;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.rdd.RDD;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Bridge {

    public static final DeepSparkContext deepContext = new DeepSparkContext(Context.cluster, Context.jobName);

    public static int executeCount(String keyspaceName, String tableName, Session cassandraSession){

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

        //deepContext.stop();

        System.out.println("TRACE: Deep context stopped");

        return rddCount.intValue();

    }

    public static ResultSet execute(MetaStatement stmt, List<Result> resultsFromChildren, boolean isRoot, Session cassandraSession){

        System.out.println("TRACE: Deep.execution");

        //DeepSparkContext deepContext = new DeepSparkContext(Context.cluster, Context.jobName);

        System.out.println("TRACE: Executing deep for: "+stmt.toString());

        if(!(stmt instanceof SelectStatement)){
            List<Row> oneRow = new ArrayList<Row>();
            oneRow.add(new Row("RESULT", new Cell(String.class, "NOT supported yet")));
            return new CassandraResultSet(oneRow);
        }

        SelectStatement ss = (SelectStatement) stmt;

        if(resultsFromChildren.isEmpty()){ // LEAF

            //Retrieve selected column names
            SelectionList sList = (SelectionList) ss.getSelectionClause();
            SelectionSelectors sSelectors = (SelectionSelectors) sList.getSelection();
            String [] columnsSet = new String[sSelectors.getSelectors().size()];
            for(int i=0;i<sSelectors.getSelectors().size();++i){
                SelectionSelector sSel = sSelectors.getSelectors().get(i);
                SelectorIdentifier selId = (SelectorIdentifier) sSel.getSelector();
                columnsSet[i] = selId.getColumnName();
            }

            // Configuration and initialization
            IDeepJobConfig config = DeepJobConfigFactory.create()
                    .host(Context.cassandraHost).rpcPort(Context.cassandraPort)
                    .keyspace(ss.getKeyspace()).table(ss.getTableName()).inputColumns(columnsSet).initialize();

            JavaRDD rdd = deepContext.cassandraJavaRDD(config);

            if(ss.isWhereInc()){ // If where
                ArrayList<Relation> where = ss.getWhere();
                for(Relation rel : where){
                    rdd = doWhere(rdd, rel);
                }
            }

            // Return RDD
            return returnResult(rdd, isRoot);

        } else { // (INNER NODE) NO LEAF
            // Retrieve RDDs from children
            ArrayList<JavaRDD> children = new ArrayList<JavaRDD>();
            for (Result child: resultsFromChildren){
                QueryResult qResult = (QueryResult) child;
                CassandraResultSet crset = (CassandraResultSet) qResult.getResultSet();
                Map<String, Cell> cells = crset.getRows().get(0).getCells();
                Cell cell = cells.get(cells.keySet().iterator().next());
                JavaRDD rdd = (JavaRDD) cell.getValue();
                children.add(rdd);
            }

            //JOIN
            Map<String, String> fields = ss.getJoin().getColNames();
            Set<String> keys = fields.keySet();
            String field1 = keys.iterator().next();
            String field2 = fields.get(field1);

            JavaRDD rdd1 = children.get(0);
            JavaRDD rdd2 = children.get(1);

            JavaPairRDD rddLeft = rdd1.map(new MapKeyForJoin(field1));
            JavaPairRDD rddRight = rdd2.map(new MapKeyForJoin(field2));

            JavaPairRDD joinRDD = rddLeft.join(rddRight);

            JavaRDD result = joinRDD.map(new JoinCells(field1, field2));

            ResultSet resultSet = null;

            if(isRoot){
                resultSet = returnResult(result.dropTake(0, 10000));
            } else {
                resultSet = returnResult(result, isRoot);
            }

            return resultSet;
        }

    }

    private static ResultSet returnResult(List<Cells> cells) {
        CassandraResultSet rs = new CassandraResultSet();
        for(Cells deepRow: cells){
            Row metaRow = new Row();
            for(com.stratio.deep.entity.Cell deepCell: deepRow.getCells()){
                Cell metaCell = new Cell(deepCell.getValueType(), deepCell.getCellValue());
                metaRow.addCell(deepCell.getCellName(), metaCell);
            }
            rs.add(metaRow);
        }
        System.out.println("Deep Result:"+ rs.size()+ " rows");
        return rs;
    }

    public static void stopContext(){
        deepContext.stop();
    }

    private static ResultSet returnResult(JavaRDD rdd, boolean isRoot){
        if(isRoot){
            return DeepUtils.convertRDDtoResultSet(rdd);
        } else {
            List oneRow = new ArrayList<Row>();
            oneRow.add(new Row("RESULT", new Cell(JavaRDD.class, rdd)));
            return new CassandraResultSet(oneRow);
        }
    }

    private static JavaRDD doWhere(JavaRDD rdd, Relation rel){
        String operator = rel.getOperator();
        JavaRDD result = null;
        String cn = rel.getIdentifiers().get(0);  //Take first. Common is 1 identifier and 1 termValue
        Object termValue = rel.getTerms().get(0).getTermValue();

        switch (operator){
            case "=":
                result = rdd.filter(new Equals(cn, termValue));
                break;
            case "<>":
                result = rdd.filter(new NotEquals(cn,termValue));
                break;
            case ">":
                result = rdd.filter(new GreaterThan(cn,termValue));
                break;
            case ">=":
                result = rdd.filter(new GreaterEqualThan(cn,termValue));
                break;
            case "<":
                result = rdd.filter(new LessThan(cn,termValue));
                break;
            case "<=":
                result = rdd.filter(new LessEqualThan(cn,termValue));
                break;
//            case "like":
//                break;
//            case "in":
//                break;
            default:
                break;
        }

        return result;
    }

}
