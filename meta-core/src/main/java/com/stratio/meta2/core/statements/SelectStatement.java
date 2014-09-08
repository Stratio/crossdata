/*
 * Licensed to STRATIO (C) under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright ownership. The STRATIO
 * (C) licenses this file to you under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.stratio.meta2.core.statements;

import com.stratio.meta.common.statements.structures.relationships.Relation;
import com.stratio.meta.common.statements.structures.window.Window;
import com.stratio.meta.common.utils.StringUtils;
import com.stratio.meta.core.structures.GroupBy;
import com.stratio.meta.core.structures.InnerJoin;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.statements.structures.selectors.SelectExpression;
import com.stratio.meta2.core.structures.OrderBy;
import com.stratio.meta2.core.structures.Ordering;
import com.stratio.meta2.core.validator.ValidationRequirements;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Class that models a {@code SELECT} statement from the META language.
 */
public class SelectStatement extends MetaStatement {

  /**
   * Maximum limit of rows to be retreived in a query.
   */
  private static final int MAX_LIMIT = 10000;

  /**
   * The list of selectors to be retreived.
   */
  private SelectExpression selectExpression = null;

  /**
   * The name of the target table.
   */
  private final TableName tableName;

  /**
   * Whether a time window has been specified in the Select statement.
   */
  private boolean windowInc = false;

  /**
   * The {@link com.stratio.meta.common.statements.structures.window.Window} specified in the Select
   * statement for streaming queries.
   */
  private Window window = null;

  /**
   * Whether a JOIN clause has been specified.
   */
  private boolean joinInc = false;

  /**
   * The {@link com.stratio.meta.core.structures.InnerJoin} clause.
   */
  private InnerJoin join = null;

  /**
   * Whether the Select contains a WHERE clause.
   */
  private boolean whereInc = false;

  /**
   * The list of {@link com.stratio.meta.common.statements.structures.relationships.Relation} found
   * in the WHERE clause.
   */
  private List<Relation> where = null;

  /**
   * Whether an ORDER BY clause has been specified.
   */
  private boolean orderInc = false;

  /**
   * The list of {@link com.stratio.meta2.core.structures.Ordering} clauses.
   */
  @Deprecated
  private List<Ordering> order = null;

  /**
   * Whether a GROUP BY clause has been specified.
   */
  private boolean groupInc = false;

  /**
   * The {@link com.stratio.meta.core.structures.GroupBy} clause.
   */
  private GroupBy group = null;

  private OrderBy orderBy=null;


  /**
   * Whether a LIMIT clause has been specified.
   */
  private boolean limitInc = false;

  /**
   * The LIMIT in terms of the number of rows to be retrieved in the result of the SELECT statement.
   */
  private int limit = 0;


  /**
   * Class logger.
   */
  private static final Logger LOG = Logger.getLogger(SelectStatement.class);

  private Map<String, String> fieldsAliasesMap;

  private Map<String, String> tablesAliasesMap;

  /**
   * Class constructor.
   * 
   * @param tableName The name of the target table.
   */
  public SelectStatement(TableName tableName) {
    this.command = false;
    this.tableName = tableName;
  }

  /**
   * Class constructor.
   * 
   * @param selectExpression The
   *        {@link com.stratio.meta2.common.statements.structures.selectors.SelectExpression} of the
   *        Select statement.
   * @param tableName The name of the target table.
   */
  public SelectStatement(SelectExpression selectExpression, TableName tableName) {
    this(tableName);
    this.selectExpression = selectExpression;
  }

  /**
   * Get the catalog specified in the select statement.
   * 
   * @return The catalog or null if not specified.
   */
  public String getCatalog() {
    return catalog;
  }

  /**
   * Set the catalog specified in the select statement.
   * 
   * @param catalog The name of the catalog.
   */
  public void setCatalog(String catalog) {
    this.catalogInc = true;
    this.catalog = catalog;
  }

  /**
   * Get the name of the target table.
   * 
   * @return The table name.
   */
  public TableName getTableName() {
    return tableName;
  }

  public SelectExpression getSelectExpression() {
    return selectExpression;
  }

  public void setSelectExpression(SelectExpression selectExpression) {
    this.selectExpression = selectExpression;
  }

  /**
   * Set the {@link com.stratio.meta.common.statements.structures.window.Window} for streaming
   * queries.
   * 
   * @param window The window.
   */
  public void setWindow(Window window) {
    this.windowInc = true;
    this.window = window;
  }

  /**
   * Get the Join clause.
   * 
   * @return The Join or null if not set.
   */
  public InnerJoin getJoin() {
    return join;
  }

  /**
   * Set the {@link com.stratio.meta.core.structures.InnerJoin} clause.
   * 
   * @param join The join clause.
   */
  public void setJoin(InnerJoin join) {
    this.joinInc = true;
    this.join = join;
  }

  /**
   * Get the list of {@link Relation} in the where clause.
   * 
   * @return The list of relations.
   */
  public List<Relation> getWhere() {
    return where;
  }

  /**
   * Set the list of {@link Relation} in the where clause.
   * 
   * @param where The list of relations.
   */
  public void setWhere(List<Relation> where) {
    this.whereInc = true;
    this.where = where;
  }

  /**
   * Set the {@link Ordering} in the ORDER BY clause.
   * 
   * @param order The order.
   */
  @Deprecated
  public void setOrder(List<Ordering> order) {
    this.orderInc = true;
    this.order = order;
  }

  /**
   * Return ORDER BY clause.
   * 
   * @return list of {@link com.stratio.meta2.core.structures.Ordering}.
   */
  @Deprecated
  public List<Ordering> getOrder() {
    return order;
  }

  /**
   * Check if ORDER BY clause is included.
   * 
   * @return {@code true} if is included.
   */
  @Deprecated
  public boolean isOrderInc() {
    return orderInc;
  }

  /**
   * Set the {@link com.stratio.meta.core.structures.GroupBy} clause.
   * 
   * @param group The group by.
   */
  public void setGroup(GroupBy group) {
    this.groupInc = true;
    this.group = group;
  }

  /**
   * Return GROUP BY clause.
   * 
   * @return list of {@link com.stratio.meta.core.structures.GroupBy}.
   */
  public GroupBy getGroup() {
    return group;
  }

  /**
   * Check if GROUP BY clause is included.
   * 
   * @return {@code true} if is included.
   */
  public boolean isGroupInc() {
    return groupInc;
  }

  /**
   * Check if a WHERE clause is included.
   * 
   * @return Whether it is included.
   */
  public boolean isWhereInc() {
    return whereInc;
  }

  /**
   * Set the LIMIT of the query.
   * 
   * @param limit The maximum number of rows to be returned.
   */
  public void setLimit(int limit) {
    this.limitInc = true;
    if (limit <= MAX_LIMIT) {
      this.limit = limit;
    } else {
      this.limit = MAX_LIMIT;
    }
  }

  public int getLimit() {
    return limit;
  }

  public Window getWindow() {
    return window;
  }


  public Map<String, String> getFieldsAliasesMap() {
    return fieldsAliasesMap;
  }

  public void setFieldsAliases(Map fieldsAliasesMap) {
    this.fieldsAliasesMap = fieldsAliasesMap;
  }

  public Map<String, String> getTablesAliasesMap() {
    return tablesAliasesMap;
  }

  public void setTablesAliases(Map tablesAliasesMap) {
    this.tablesAliasesMap = tablesAliasesMap;
  }

  /**
   * Creates a String representing the Statement with META syntax.
   * 
   * @return String
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("SELECT ");
    if (selectExpression != null) {
      sb.append(selectExpression.toString());
    }
    sb.append(" FROM ");
    if (catalogInc) {
      sb.append(catalog).append(".");
    }
    sb.append(tableName);
    if (windowInc) {
      sb.append(" WITH WINDOW ").append(window.toString());
    }
    if (joinInc) {
      sb.append(" INNER JOIN ").append(join.toString());
    }
    if (whereInc) {
      sb.append(" WHERE ");
      sb.append(StringUtils.stringList(where, " AND "));
    }
    if (orderInc) {
      sb.append(" ORDER BY ").append(StringUtils.stringList(order, ", "));
    }
    //TODO: Update group by detection
    /*if (groupInc) {
      sb.append(" GROUP BY ").append(StringUtils.stringList(group, ", "));
    }*/
    if (limitInc) {
      sb.append(" LIMIT ").append(limit);
    }

    return sb.toString().replace("  ", " ");
  }

  @Override
  public ValidationRequirements getValidationRequirements() {
    return new ValidationRequirements();
  }

  public OrderBy getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(OrderBy orderBy) {
    this.orderBy = orderBy;
  }
}
