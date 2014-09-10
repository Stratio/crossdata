/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta.core.normalizer;

import com.stratio.meta.common.exceptions.ValidationException;
import com.stratio.meta.common.statements.structures.relationships.Operator;
import com.stratio.meta.common.statements.structures.relationships.Relation;
import com.stratio.meta.core.structures.InnerJoin;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.common.data.ColumnName;
import com.stratio.meta2.common.data.TableName;
import com.stratio.meta2.common.statements.structures.selectors.ColumnSelector;
import com.stratio.meta2.common.statements.structures.selectors.SelectExpression;
import com.stratio.meta2.common.statements.structures.selectors.Selector;
import com.stratio.meta2.common.statements.structures.selectors.StringSelector;
import com.stratio.meta2.core.query.BaseQuery;
import com.stratio.meta2.core.query.NormalizedQuery;
import com.stratio.meta2.core.query.SelectParsedQuery;
import com.stratio.meta2.core.statements.SelectStatement;
import com.stratio.meta2.core.structures.OrderBy;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class NormalizerTest {

  // TODO: SetUpBeforeClass to mock MetaDataManager

  public void testSelectedParserQuery(SelectParsedQuery selectParsedQuery, String expectedText, String methodName){
    Normalizer normalizer = new Normalizer();

    NormalizedQuery result = null;
    try {
      result = normalizer.normalize(selectParsedQuery);
    } catch (ValidationException e) {
      fail("Test failed: " + methodName + System.lineSeparator(), e);
    }

    assertTrue(result.toString().equalsIgnoreCase(expectedText),
               "Test failed: "+ methodName + System.lineSeparator() +
               "Result:   " + result.toString() + System.lineSeparator() +
               "Expected: " + expectedText);
  }

  @Test
  public void testNormalizeWhereOrderGroup() throws Exception {

    String methodName = "testNormalizeWhereOrderGroup";

    String inputText = "SELECT colSales, colRevenues FROM tableClients "
                       + "WHERE colCity = 'Madrid' "
                       + "ORDER BY age "
                       + "GROUP BY gender;";

    String expectedText = "SELECT myCatalog.tableClients.colSales, myCatalog.tableClients.colRevenues FROM myCatalog.tableClients "
                          + "WHERE myCatalog.tableClients.colCity = 'Madrid' "
                          + "ORDER BY myCatalog.tableClients.age "
                          + "GROUP BY myCatalog.tableClients.gender;";

    // BASE QUERY
    BaseQuery baseQuery = new BaseQuery(UUID.randomUUID().toString(), inputText, new CatalogName(""));

    // SELECTORS
    List<Selector> selectorList = new ArrayList<>();
    selectorList.add(new ColumnSelector(new ColumnName(null, "colSales")));
    selectorList.add(new ColumnSelector(new ColumnName(null, "colRevenues")));

    SelectExpression selectExpression = new SelectExpression(selectorList);

    // SELECT STATEMENT
    SelectStatement selectStatement = new SelectStatement(selectExpression, new TableName(null, "tableClients"));

    // WHERE CLAUSES
    List<Relation> where = new ArrayList<>();
    where.add(new Relation(new ColumnSelector(new ColumnName(null, "colCity")), Operator.ASSIGN, new StringSelector("Madrid")));
    selectStatement.setWhere(where);

    // ORDER BY
    List<Selector> selectorListOrder = new ArrayList<>();
    selectorListOrder.add(new ColumnSelector(new ColumnName(null, "age")));
    OrderBy orderBy = new OrderBy(selectorListOrder);
    selectStatement.setOrderBy(orderBy);

    // GROUP BY
    List<Selector> group = new ArrayList<>();
    group.add(new ColumnSelector(new ColumnName(null, "gender")));
    selectStatement.setGroup(group);

    SelectParsedQuery selectParsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

    testSelectedParserQuery(selectParsedQuery, expectedText, methodName);
  }

  @Test
  public void testNormalizeInnerJoin() throws Exception {

    String methodName = "testNormalizeInnerJoin";

    String inputText =
        "SELECT colSales, colRevenues FROM tableClients "
        + "INNER JOIN tableCostumers ON assistantId = clientId "
        + "WHERE colCity = 'Madrid' "
        + "ORDER BY age "
        + "GROUP BY gender;";

    String expectedText = "SELECT demo.january.colSales, myCatalog.tableCostumers.january.colRevenues FROM demo.january.tableClients "
                          + "INNER JOIN myCatalog.tableCostumers ON myCatalog.tableCostumers.assistantId = demo.january.clientId "
                          + "WHERE myCatalog.tableCostumers.colCity = 'Madrid' "
                          + "ORDER BY myCatalog.tableCostumers.age "
                          + "GROUP BY demo.january.gender;";


    // BASE QUERY
    BaseQuery baseQuery = new BaseQuery(UUID.randomUUID().toString(), inputText, new CatalogName(""));

    // SELECTORS
    List<Selector> selectorList = new ArrayList<>();
    selectorList.add(new ColumnSelector(new ColumnName(null, "colSales")));
    selectorList.add(new ColumnSelector(new ColumnName(null, "colRevenues")));

    SelectExpression selectExpression = new SelectExpression(selectorList);

    // SELECT STATEMENT
    SelectStatement selectStatement = new SelectStatement(selectExpression, new TableName(null, "tableClients"));

    // TODO: Inner Join
    List<Relation> joinRelations = new ArrayList<>();
    Relation relation = new Relation(
        new ColumnSelector(new ColumnName(null, "assistandId")),
        Operator.ASSIGN,
        new ColumnSelector(new ColumnName(null, "clientId")));
    joinRelations.add(relation);
    InnerJoin innerJoin = new InnerJoin(new TableName(null, "tableCostumers"), joinRelations);
    selectStatement.setJoin(innerJoin);

    // WHERE CLAUSES
    List<Relation> where = new ArrayList<>();
    where.add(new Relation(new ColumnSelector(new ColumnName(null, "colCity")), Operator.ASSIGN, new StringSelector("Madrid")));
    selectStatement.setWhere(where);

    // ORDER BY
    List<Selector> selectorListOrder = new ArrayList<>();
    selectorListOrder.add(new ColumnSelector(new ColumnName(null, "age")));
    OrderBy orderBy = new OrderBy(selectorListOrder);
    selectStatement.setOrderBy(orderBy);

    // GROUP BY
    List<Selector> group = new ArrayList<>();
    group.add(new ColumnSelector(new ColumnName(null, "gender")));
    selectStatement.setGroup(group);

    SelectParsedQuery selectParsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

    testSelectedParserQuery(selectParsedQuery, expectedText, methodName);

  }

}
