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
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;

import java.util.*;

public class Bridge {

    /**
     * Class logger.
     */
    private final Logger logger = Logger.getLogger(Bridge.class);

    private static DeepSparkContext deepContext = null;
    private Session session;

    public Bridge(Session session) {
        if(deepContext == null) {
            deepContext = new DeepSparkContext(Context.cluster, Context.jobName);
        }
        this.session = session;
    }

    public ResultSet execute(MetaStatement stmt, List<Result> resultsFromChildren, boolean isRoot){

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

            Selection selection = sList.getSelection();
            String [] columnsSet = null;
            boolean allCols = false;
            if(selection instanceof SelectionSelectors){
                SelectionSelectors sSelectors = (SelectionSelectors) selection;
                columnsSet = new String[sSelectors.getSelectors().size()];
                for(int i=0;i<sSelectors.getSelectors().size();++i){
                    SelectionSelector sSel = sSelectors.getSelectors().get(i);
                    SelectorIdentifier selId = (SelectorIdentifier) sSel.getSelector();
                    columnsSet[i] = selId.getColumnName();
                }
                System.out.println("Select columns: " + Arrays.toString(columnsSet));

            } else { // SelectionAsterisk
                allCols = true;
            }

            // Configuration and initialization
            IDeepJobConfig config = null;
            if(allCols){
                config = DeepJobConfigFactory.create().session(session)
                        .host(Context.cassandraHost).rpcPort(Context.cassandraPort)
                        .keyspace(ss.getKeyspace()).table(ss.getTableName()).initialize();
            } else {
                config = DeepJobConfigFactory.create().session(session)
                        .host(Context.cassandraHost).rpcPort(Context.cassandraPort)
                        .keyspace(ss.getKeyspace()).table(ss.getTableName())
                        .inputColumns(columnsSet).initialize();
            }

            JavaRDD rdd = deepContext.cassandraJavaRDD(config);

            if(ss.isWhereInc()){ // If where
                List<Relation> where = ss.getWhere();
                for(Relation rel : where){
                    rdd = doWhere(rdd, rel);
                }
            }

            // Return RDD
            return returnResult(rdd, isRoot);

        } else { // (INNER NODE) NO LEAF
            // Retrieve RDDs from children
            List<JavaRDD> children = new ArrayList<JavaRDD>();
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

            System.out.println("INNER JOIN on: " + field1 + " - " + field2);

            JavaRDD rdd1 = children.get(0);
            JavaRDD rdd2 = children.get(1);

            JavaPairRDD rddLeft = rdd1.map(new MapKeyForJoin(field1));
            JavaPairRDD rddRight = rdd2.map(new MapKeyForJoin(field2));

            JavaPairRDD joinRDD = rddLeft.join(rddRight);

            JavaRDD result = joinRDD.map(new JoinCells(field1, field2));

            // Return MetaResultSet
            return returnResult(result, isRoot);
        }

    }

    private ResultSet returnResult(List<Cells> cells) {
        CassandraResultSet rs = new CassandraResultSet();
        for(Cells deepRow: cells){
            Row metaRow = new Row();
            for(com.stratio.deep.entity.Cell deepCell: deepRow.getCells()){
                Cell metaCell = new Cell(deepCell.getValueType(), deepCell.getCellValue());
                metaRow.addCell(deepCell.getCellName(), metaCell);
            }
            rs.add(metaRow);
        }
        logger.info("Deep Result: " + rs.size() + " rows & " + rs.iterator().next().size() + " columns");
        return rs;
    }

    private ResultSet returnResult(JavaRDD rdd, boolean isRoot){
        if(isRoot){
            return returnResult(rdd.collect());
            //return returnResult(rdd.dropTake(0, 10000));
        } else {
            List oneRow = new ArrayList<Row>();
            oneRow.add(new Row("RESULT", new Cell(JavaRDD.class, rdd)));
            System.out.println("LEAF: rdd.count="+((int)rdd.count()));
            return new CassandraResultSet(oneRow);
        }
    }

    public void stopContext(){
        deepContext.stop();
    }

    private JavaRDD doWhere(JavaRDD rdd, Relation rel){
        String operator = rel.getOperator();
        JavaRDD result = null;
        String cn = rel.getIdentifiers().get(0);  //Take first. Common is 1 identifier and 1 termValue
        Object termValue = rel.getTerms().get(0).getTermValue();

        System.out.println("Rdd input size: " + rdd.count());
        switch (operator){
            case "=":
                result = rdd.filter(new DeepEquals(cn, termValue));
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

        System.out.println("Rdd input: " + Arrays.toString(rdd.collect().toArray()) +" filtered size: " + result.count());

        return result;
    }

}
