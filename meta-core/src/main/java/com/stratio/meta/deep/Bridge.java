/*
 * Stratio Meta
 * 
 * Copyright (c) 2014, Stratio, All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library.
 */

package com.stratio.meta.deep;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;

import scala.Tuple2;

import com.datastax.driver.core.Session;
import com.stratio.deep.config.DeepJobConfigFactory;
import com.stratio.deep.config.ICassandraDeepJobConfig;
import com.stratio.deep.context.DeepSparkContext;
import com.stratio.deep.entity.Cells;
import com.stratio.meta.common.data.CassandraResultSet;
import com.stratio.meta.common.data.Cell;
import com.stratio.meta.common.data.ResultSet;
import com.stratio.meta.common.data.Row;
import com.stratio.meta.common.metadata.structures.ColumnMetadata;
import com.stratio.meta.common.metadata.structures.ColumnType;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta2.core.statements.MetaStatement;
import com.stratio.meta.core.statements.SelectStatement;
import com.stratio.meta.core.structures.GroupBy;
import com.stratio.meta.core.structures.Ordering;
import com.stratio.meta.common.statements.structures.relationships.Relation;
import com.stratio.meta.core.structures.SelectionClause;
import com.stratio.meta.core.structures.SelectionList;
import com.stratio.meta.common.statements.structures.terms.Term;
import com.stratio.meta.deep.comparators.DeepComparator;
import com.stratio.meta.deep.functions.Between;
import com.stratio.meta.deep.functions.DeepEquals;
import com.stratio.meta.deep.functions.GreaterEqualThan;
import com.stratio.meta.deep.functions.GreaterThan;
import com.stratio.meta.deep.functions.In;
import com.stratio.meta.deep.functions.JoinCells;
import com.stratio.meta.deep.functions.LessEqualThan;
import com.stratio.meta.deep.functions.LessThan;
import com.stratio.meta.deep.functions.MapKeyForJoin;
import com.stratio.meta.deep.functions.NotEquals;
import com.stratio.meta.deep.transformation.AverageAggregatorMapping;
import com.stratio.meta.deep.transformation.GroupByAggregation;
import com.stratio.meta.deep.transformation.GroupByMapping;
import com.stratio.meta.deep.transformation.KeyRemover;
import com.stratio.meta.deep.utils.DeepUtils;

/**
 * Class that performs as a Bridge between Meta and Stratio Deep.
 */
public class Bridge {

  /**
   * Class logger.
   */
  private static final Logger LOG = Logger.getLogger(Bridge.class);

  /**
   * Default result size.
   */
  public static final int DEFAULT_RESULT_SIZE = 100000;

  /**
   * Deep Spark context.
   */
  private DeepSparkContext deepContext;

  /**
   * Datastax Java Driver session.
   */
  private Session session;

  /**
   * Global configuration.
   */
  private EngineConfig engineConfig;

  /**
   * Brigde Constructor.
   * 
   * @param session Cassandra session. {@link com.datastax.driver.core.Session}
   * @param deepSparkContext Spark context from Deep
   * @param config A {@link com.stratio.meta.core.engine.EngineConfig}, contains global
   *        configuration
   */
  public Bridge(Session session, DeepSparkContext deepSparkContext, EngineConfig config) {
    this.deepContext = deepSparkContext;
    this.session = session;
    this.engineConfig = config;
  }

  /**
   * Execute a Leaf node in the current plan.
   * 
   * @param stmt Statement which corresponds to this node.
   * @param isRoot Indicates if this node is root in this plan
   * @return a {@link com.stratio.meta.common.data.ResultSet}
   */
  public ResultSet executeLeafNode(MetaStatement stmt, boolean isRoot) {
    SelectStatement ss = (SelectStatement) stmt;

    ss.addTablenameToIds();

    // LEAF
    String[] columnsSet = {};
    if (ss.getSelectionClause().getType() == SelectionClause.TYPE_SELECTION) {
      columnsSet = DeepUtils.retrieveSelectorFields(ss);
    }
    ICassandraDeepJobConfig<Cells> config =
        DeepJobConfigFactory.create().session(session).host(engineConfig.getRandomCassandraHost())
            .rpcPort(engineConfig.getCassandraPort()).keyspace(ss.getEffectiveCatalog())
            .table(ss.getTableName());

    config =
        (columnsSet.length == 0) ? config.initialize() : config.inputColumns(columnsSet)
            .initialize();

    JavaRDD<Cells> rdd = deepContext.cassandraJavaRDD(config);

    // If where
    if (ss.isWhereInc()) {
      List<Relation> where = ss.getWhere();
      for (Relation rel : where) {
        rdd = doWhere(rdd, rel);
      }
    }

    List<String> cols =
        DeepUtils.retrieveSelectors(((SelectionList) ss.getSelectionClause()).getSelection());

    // Group by clause
    if (ss.isGroupInc()) {
      rdd = doGroupBy(rdd, ss.getGroup(), (SelectionList) ss.getSelectionClause());
    } else if (ss.getSelectionClause().containsFunctions()) {
      rdd = doGroupBy(rdd, null, (SelectionList) ss.getSelectionClause());
    }

    CassandraResultSet resultSet =
        (CassandraResultSet) returnResult(rdd, isRoot,
            ss.getSelectionClause().getType() == SelectionClause.TYPE_COUNT, cols, ss.getLimit(),
            ss.getOrder());

    return replaceWithAliases(ss.getFieldsAliasesMap(), resultSet);
  }

  /**
   * Executes a root node statement.
   * 
   * @param stmt Statement which corresponds to this node.
   * @param resultsFromChildren List of results from node children
   * @return a {@link com.stratio.meta.common.data.ResultSet}
   */
  public ResultSet executeRootNode(MetaStatement stmt, List<Result> resultsFromChildren) {
    SelectStatement ss = (SelectStatement) stmt;

    ss.addTablenameToIds();

    // Retrieve RDDs and selected columns from children
    List<JavaRDD<Cells>> children = new ArrayList<>();
    List<String> selectedCols = new ArrayList<>();
    for (Result child : resultsFromChildren) {
      QueryResult qResult = (QueryResult) child;
      CassandraResultSet crset = (CassandraResultSet) qResult.getResultSet();

      Map<String, Cell> cells = crset.getRows().get(0).getCells();
      // RDD from child
      Cell cell = cells.get("RDD");
      JavaRDD<Cells> rdd = (JavaRDD<Cells>) cell.getValue();
      children.add(rdd);
    }

    // Retrieve selected columns without tablename
    for (String id : ss.getSelectionClause().getIds()) {
      if (id.contains(".")) {
        selectedCols.add(id.split("\\.")[1]);
      } else {
        selectedCols.add(id);
      }
    }

    // JOIN
    String keyTableLeft = ss.getJoin().getLeftField().getField();
    String keyTableRight = ss.getJoin().getRightField().getField();

    LOG.debug("INNER JOIN on: " + keyTableLeft + " - " + keyTableRight);

    JavaRDD<Cells> rddTableLeft = children.get(0);
    JavaRDD<Cells> rddTableRight = children.get(1);

    JavaPairRDD<Cells, Cells> rddLeft = rddTableLeft.mapToPair(new MapKeyForJoin(keyTableLeft));
    JavaPairRDD<Cells, Cells> rddRight = rddTableRight.mapToPair(new MapKeyForJoin(keyTableRight));

    JavaPairRDD<Cells, Tuple2<Cells, Cells>> joinRDD = rddLeft.join(rddRight);

    JavaRDD<Cells> result = joinRDD.map(new JoinCells(keyTableLeft));

    // MetaResultSet
    CassandraResultSet resultSet =
        (CassandraResultSet) returnResult(result, true, false, selectedCols, ss.getLimit(),
            ss.getOrder());

    return replaceWithAliases(ss.getFieldsAliasesMap(), resultSet);
  }

  private ResultSet replaceWithAliases(Map<String, String> fieldsAliasesMap,
      CassandraResultSet resultSet) {

    List<ColumnMetadata> metadata = resultSet.getColumnMetadata();

    List<ColumnMetadata> resultMetadata = null;
    if (!metadata.isEmpty() && fieldsAliasesMap != null && !fieldsAliasesMap.isEmpty()) {
      resultMetadata = new ArrayList<>();
      Iterator<ColumnMetadata> metadataIt = metadata.iterator();
      while (metadataIt.hasNext()) {
        ColumnMetadata columnMetadata = metadataIt.next();
        String table = columnMetadata.getTableName();
        String column = columnMetadata.getColumnName();

        for (Entry<String, String> entry : fieldsAliasesMap.entrySet()) {

          if (entry.getValue().equalsIgnoreCase(column)
              || entry.getValue().equalsIgnoreCase(table + "." + column)) {
            columnMetadata.setColumnAlias(entry.getKey());
          }
        }
        resultMetadata.add(columnMetadata);
      }
    }

    if (resultMetadata != null) {
      resultSet.setColumnMetadata(resultMetadata);
    }

    return resultSet;
  }

  /**
   * General execution. Depending on the type execution will divide up.
   * 
   * @param stmt Statement which corresponds to this node.
   * @param resultsFromChildren List of results from node children
   * @param isRoot Indicates if this node is root in this plan
   * @return a {@link com.stratio.meta.common.data.ResultSet}
   */
  public ResultSet execute(MetaStatement stmt, List<Result> resultsFromChildren, boolean isRoot) {

    LOG.info("Executing deep for: " + stmt.toString());

    if (!(stmt instanceof SelectStatement)) {
      CassandraResultSet crs = new CassandraResultSet();
      crs.add(new Row("RESULT", new Cell("NOT supported yet")));

      List<ColumnMetadata> columns = new ArrayList<>();
      ColumnMetadata metadata = new ColumnMetadata("result", "result");
      ColumnType type = ColumnType.VARCHAR;
      type.setDBMapping("varchar", String.class);
      metadata.setType(type);
      crs.setColumnMetadata(columns);
      return crs;
    }

    if (resultsFromChildren.isEmpty()) {
      // LEAF
      return executeLeafNode(stmt, isRoot);
    } else {
      // (INNER NODE) NO LEAF
      return executeRootNode(stmt, resultsFromChildren);
    }
  }

  /**
   * Build a ResultSet from a RDD depending the context.
   * 
   * @param rdd RDD which corresponds to Spark result.
   * @param isRoot Indicates if this node is root in this plan.
   * @param isCount Indicates if this query have a COUNT clause.
   * @param selectedCols List of columns selected in current SelectStatement.
   * @param limit Maximum number of rows to be retrieved.
   * @param orderings Sorting criteria.
   * @return ResultSet containing the result of built.
   */
  private ResultSet returnResult(JavaRDD<Cells> rdd, boolean isRoot, boolean isCount,
      List<String> selectedCols, int limit, List<Ordering> orderings) {
    if (isRoot) {
      if (isCount) {
        return DeepUtils.buildCountResult(rdd);
      }

      List<Cells> cells = null;
      if (orderings == null) {
        if (limit <= 0 || limit > DEFAULT_RESULT_SIZE) {
          cells = rdd.take(DEFAULT_RESULT_SIZE);
        } else {
          cells = rdd.take(limit);
        }
      } else {
        if (limit <= 0 || limit > DEFAULT_RESULT_SIZE) {
          cells = rdd.takeOrdered(DEFAULT_RESULT_SIZE, new DeepComparator(orderings));
        } else {
          cells = rdd.takeOrdered(limit, new DeepComparator(orderings));
        }
      }

      return DeepUtils.buildResultSet(cells, selectedCols);
    } else {
      CassandraResultSet crs = new CassandraResultSet();
      crs.add(new Row("RDD", new Cell(rdd)));

      List<ColumnMetadata> columns = new ArrayList<>();
      ColumnMetadata metadata = new ColumnMetadata("RDD", "RDD");
      ColumnType type = ColumnType.VARCHAR;
      type.setDBMapping("class", JavaRDD.class);
      metadata.setType(type);
      crs.setColumnMetadata(columns);

      LOG.info("LEAF: rdd.count=" + ((int) rdd.count()));
      return crs;
    }
  }

  /**
   * Take a RDD and a Relation and apply suitable filter to the RDD. Execute where clause on Deep.
   * 
   * @param rdd RDD which filter must be applied.
   * @param rel {@link com.stratio.meta.common.statements.structures.relationships.Relation} to apply
   * @return A new RDD with the result.
   */
  private JavaRDD<Cells> doWhere(JavaRDD<Cells> rdd, Relation rel) {
    String operator = rel.getOperator();
    JavaRDD<Cells> result = null;
    String cn = rel.getIdentifiers().get(0).getField();
    List<Term<?>> terms = rel.getTerms();

    LOG.info("Rdd input size: " + rdd.count());
    switch (operator.toLowerCase()) {
      case "=":
        result = rdd.filter(new DeepEquals(cn, terms.get(0)));
        break;
      case "<>":
        result = rdd.filter(new NotEquals(cn, terms.get(0)));
        break;
      case ">":
        result = rdd.filter(new GreaterThan(cn, terms.get(0)));
        break;
      case ">=":
        result = rdd.filter(new GreaterEqualThan(cn, terms.get(0)));
        break;
      case "<":
        result = rdd.filter(new LessThan(cn, terms.get(0)));
        break;
      case "<=":
        result = rdd.filter(new LessEqualThan(cn, terms.get(0)));
        break;
      case "in":
        result = rdd.filter(new In(cn, terms));
        break;
      case "between":
        result = rdd.filter(new Between(cn, terms.get(0), terms.get(1)));
        break;
      default:
        LOG.error("Operator not supported: " + operator);
        result = null;
    }
    return result;
  }

  /**
   * Take a RDD and the group by information, and apply the requested grouping. If there is any
   * aggregation function, apply it to the desired column.
   * 
   * @param rdd RDD which filter must be applied.
   * @param groupByClause {@link com.stratio.meta.core.structures.GroupBy} to retrieve the grouping
   *        columns.
   * @param selectionClause {@link com.stratio.meta.core.structures.SelectionClause} containing the
   *        aggregation functions.
   * @return A new RDD with the result.
   */
  private JavaRDD<Cells> doGroupBy(JavaRDD<Cells> rdd, List<GroupBy> groupByClause,
      SelectionList selectionClause) {

    final List<String> aggregationCols =
        DeepUtils.retrieveSelectorAggegationFunctions(selectionClause.getSelection());

    // Mapping the rdd to execute the group by clause
    JavaPairRDD<Cells, Cells> groupedRdd =
        rdd.mapToPair(new GroupByMapping(aggregationCols, groupByClause));

    JavaPairRDD<Cells, Cells> aggregatedRdd = applyGroupByAggregations(groupedRdd, aggregationCols);

    JavaRDD<Cells> map = aggregatedRdd.map(new KeyRemover());

    return map;

  }

  private JavaPairRDD<Cells, Cells> applyGroupByAggregations(JavaPairRDD<Cells, Cells> groupedRdd,
      List<String> aggregationCols) {

    JavaPairRDD<Cells, Cells> aggregatedRdd =
        groupedRdd.reduceByKey(new GroupByAggregation(aggregationCols));

    // Looking for the average aggregator to complete it
    for (String aggregation : aggregationCols) {

      if (aggregation.toLowerCase().startsWith("avg(")) {
        aggregatedRdd = aggregatedRdd.mapValues(new AverageAggregatorMapping(aggregation));
      }
    }
    return aggregatedRdd;
  }
}