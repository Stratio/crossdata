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
import com.stratio.meta.common.data.CassandraResultSet;
import com.stratio.meta.common.data.Cell;
import com.stratio.meta.common.data.ResultSet;
import com.stratio.meta.common.data.Row;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.statements.MetaStatement;
import com.stratio.meta.core.statements.SelectStatement;
import com.stratio.meta.core.structures.Relation;
import com.stratio.meta.deep.functions.*;
import com.stratio.meta.deep.utils.DeepUtils;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class that performs as a Bridge between Meta and Stratio Deep
 */
public class Bridge {

    private static final Logger LOG = Logger.getLogger(Bridge.class);
    public static final int DEFAULT_RESULT_SIZE = 100000;

    private static DeepSparkContext deepContext;
    private Session session;
    private EngineConfig engineConfig;

    public Bridge(Session session, DeepSparkContext deepSparkContext, EngineConfig config) {
        this.deepContext = deepSparkContext;
        this.session = session;
        this.engineConfig = config;
    }

    public ResultSet execute(MetaStatement stmt, List<Result> resultsFromChildren, boolean isRoot){

        LOG.info("Executing deep for: " + stmt.toString());

        if(!(stmt instanceof SelectStatement)){
            List<Row> oneRow = new ArrayList<Row>();
            oneRow.add(new Row("RESULT", new Cell(String.class, "NOT supported yet")));
            return new CassandraResultSet(oneRow);
        }

        SelectStatement ss = (SelectStatement) stmt;
        if(resultsFromChildren.isEmpty()){
            // LEAF
            String[] columnsSet = DeepUtils.retrieveSelectorFields(ss);
            IDeepJobConfig config = DeepJobConfigFactory.create().session(session)
                    .host(engineConfig.getRandomCassandraHost()).rpcPort(engineConfig.getCassandraPort())
                    .keyspace(ss.getKeyspace()).table(ss.getTableName());

            config = (null==columnsSet)? config.initialize() : config.inputColumns(columnsSet).initialize() ;
            
            JavaRDD rdd = deepContext.cassandraJavaRDD(config);
            //If where
            if(ss.isWhereInc()){
                List<Relation> where = ss.getWhere();
                for(Relation rel : where){
                    rdd = doWhere(rdd, rel);
                }
            }

            return returnResult(rdd, isRoot);

        } else {
            // (INNER NODE) NO LEAF
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

            LOG.info("INNER JOIN on: " + field1 + " - " + field2);

            JavaRDD result = null;

            JavaRDD rdd1 = children.get(0);
            JavaRDD rdd2 = children.get(1);

            JavaPairRDD rddLeft = rdd1.map(new MapKeyForJoin(field1));
            JavaPairRDD rddRight = rdd2.map(new MapKeyForJoin(field2));

            JavaPairRDD joinRDD = rddLeft.join(rddRight);

            result = joinRDD.map(new JoinCells(field1));

            // Return MetaResultSet
            return returnResult(result, isRoot);
        }
    }

    private ResultSet returnResult(JavaRDD rdd, boolean isRoot){
        if(isRoot){
            return DeepUtils.buildResultSet(rdd.dropTake(0, DEFAULT_RESULT_SIZE));
        } else {
            List oneRow = new ArrayList<Row>();
            oneRow.add(new Row("RESULT", new Cell(JavaRDD.class, rdd)));
            LOG.info("LEAF: rdd.count=" + ((int) rdd.count()));
            return new CassandraResultSet(oneRow);
        }
    }

    public void stopContext(){
        deepContext.stop();
    }

    private JavaRDD doWhere(JavaRDD rdd, Relation rel){
        String operator = rel.getOperator();
        JavaRDD result = null;
        String cn = rel.getIdentifiers().get(0);
        Object termValue = rel.getTerms().get(0).getTermValue();

        LOG.info("Rdd input size: " + rdd.count());
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
        }
        return result;
    }
}
