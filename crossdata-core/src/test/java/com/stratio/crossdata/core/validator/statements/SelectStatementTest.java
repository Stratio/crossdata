/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.crossdata.core.validator.statements;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.IgnoreQueryException;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.common.exceptions.validation.AmbiguousNameException;
import com.stratio.crossdata.common.statements.structures.AsteriskSelector;
import com.stratio.crossdata.common.statements.structures.BooleanSelector;
import com.stratio.crossdata.common.statements.structures.ColumnSelector;
import com.stratio.crossdata.common.statements.structures.FunctionSelector;
import com.stratio.crossdata.common.statements.structures.IntegerSelector;
import com.stratio.crossdata.common.statements.structures.Operator;
import com.stratio.crossdata.common.statements.structures.OrderByClause;
import com.stratio.crossdata.common.statements.structures.OrderDirection;
import com.stratio.crossdata.common.statements.structures.Relation;
import com.stratio.crossdata.common.statements.structures.SelectExpression;
import com.stratio.crossdata.common.statements.structures.Selector;
import com.stratio.crossdata.common.statements.structures.StringSelector;
import com.stratio.crossdata.core.parser.Parser;
import com.stratio.crossdata.common.utils.Constants;
import com.stratio.crossdata.core.query.BaseQuery;
import com.stratio.crossdata.core.query.IParsedQuery;
import com.stratio.crossdata.core.query.IValidatedQuery;
import com.stratio.crossdata.core.query.SelectParsedQuery;
import com.stratio.crossdata.core.query.SelectValidatedQuery;
import com.stratio.crossdata.core.statements.SelectStatement;
import com.stratio.crossdata.core.structures.InnerJoin;
import com.stratio.crossdata.core.validator.BasicValidatorTest;
import com.stratio.crossdata.core.validator.Validator;

public class SelectStatementTest extends BasicValidatorTest {

    @Test
    public void validateBasicColumnOk() {
        String query = "SELECT users.name FROM demo.users;";
        List<Selector> selectorList = new ArrayList<>();
        Selector selector = new ColumnSelector(new ColumnName("demo", "users", "name"));
        selectorList.add(selector);
        SelectExpression selectExpression = new SelectExpression(selectorList);
        TableName tablename = new TableName("demo", "users");

        SelectStatement selectStatement = new SelectStatement(selectExpression, tablename);

        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("SelectId", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        try {
            validator.validate(parsedQuery);
            Assert.assertTrue(true);
        } catch (ValidationException e) {
            fail(e.getMessage());
        } catch (IgnoreQueryException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void validateSelectWithSubquery() {
        String query = "SELECT age, email FROM (SELECT * FROM test.table1);";

        BaseQuery baseQuery = new BaseQuery("validateSelectWithSubquery", query, new CatalogName("demo"));

        Parser parser = new Parser();
        SelectParsedQuery parsedQuery = (SelectParsedQuery) parser.parse(baseQuery);

        Validator validator = new Validator();

        try {
            validator.validate(parsedQuery);
            Assert.assertTrue(true);
        } catch (ValidationException e) {
            Assert.fail(e.getMessage());
        } catch (IgnoreQueryException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void validateSelectsInWhereClauses() {
        String query = "SELECT age, email FROM demo.users " +
                "WHERE age = 25 * 3 + (SELECT * FROM test.table1) - phrase + (SELECT rating FROM test.table2)" +
                " AND email = 25*(SELECT member FROM table3)+(SELECT name, address FROM table3" +
                " WHERE address = (SELECT * FROM sales.customers));";

        BaseQuery baseQuery = new BaseQuery("validateSelectsInWhereClauses", query, new CatalogName("demo"));

        Parser parser = new Parser();
        SelectParsedQuery parsedQuery = (SelectParsedQuery) parser.parse(baseQuery);

        Validator validator = new Validator();

        try {
            SelectValidatedQuery normalizedQuery = (SelectValidatedQuery) validator.validate(parsedQuery);
            assertNotNull(normalizedQuery, "normalizedQuery is null");
            Assert.assertTrue(true, "Test failed.");
        } catch (ValidationException e) {
            Assert.fail(e.getMessage());
        } catch (IgnoreQueryException e) {
            Assert.fail(e.getMessage());
        }

    }

    /*
    @Test
    public void validateBasicCountOk() {
        String query = "SELECT count(*) FROM demo.users;";
        List<Selector> selectorList = new ArrayList<>();
        Selector selector =
        selectorList.add(selector);
        SelectExpression selectExpresion = new SelectExpression(selectorList);
        TableName tablename = new TableName("demo", "users");

        SelectStatement selectStatement = new SelectStatement(selectExpresion, tablename);

        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("SelectId", query, new CatalogName("demo"));

        ParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        try {
            validator.validate(parsedQuery);
            Assert.assertTrue(true);
        } catch (ValidationException e) {
            Assert.fail(e.getMessage());
        } catch (IgnoreQueryException e) {
            Assert.fail(e.getMessage());
        }

    }*/

    @Test
    public void validateBasicSeveralColumnsOk() {
        String query = "SELECT users.name, users.age FROM demo.users;";
        List<Selector> selectorList = new ArrayList<>();
        Selector selector = new ColumnSelector(new ColumnName("demo", "users", "name"));
        Selector selector2 = new ColumnSelector(new ColumnName("demo", "users", "age"));
        selectorList.add(selector);
        selectorList.add(selector2);
        SelectExpression selectExpression = new SelectExpression(selectorList);
        TableName tablename = new TableName("demo", "users");

        SelectStatement selectStatement = new SelectStatement(selectExpression, tablename);

        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("SelectId", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        try {
            validator.validate(parsedQuery);
            Assert.assertTrue(true);
        } catch (ValidationException e) {
            fail(e.getMessage());
        } catch (IgnoreQueryException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void validateColumnUnknown() {
        String query = "SELECT users.name, users.unknown FROM demo.users;";
        List<Selector> selectorList = new ArrayList<>();

        Selector selector = new ColumnSelector(new ColumnName("demo", "users", "name"));
        Selector selector2 = new ColumnSelector(new ColumnName("demo", "users", "unknown"));

        selectorList.add(selector);
        selectorList.add(selector2);
        SelectExpression selectExpression = new SelectExpression(selectorList);
        TableName tablename = new TableName("demo", "users");

        SelectStatement selectStatement = new SelectStatement(selectExpression, tablename);

        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("SelectId", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        try {
            validator.validate(parsedQuery);
            fail("users.unknown is not a valid column name");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void validateBasicWhereOk() {
        String query = "SELECT users.name, users.age FROM demo.users WHERE users.name = 'name_5';";
        List<Selector> selectorList = new ArrayList<>();

        TableName tablename = new TableName("demo", "users");

        Selector selector = new StringSelector(tablename, "name");
        Selector selector2 = new StringSelector(tablename, "age");
        selectorList.add(selector);
        selectorList.add(selector2);
        SelectExpression selectExpression = new SelectExpression(selectorList);

        SelectStatement selectStatement = new SelectStatement(selectExpression, tablename);

        List<Relation> where = new ArrayList<>();
        Selector left = new ColumnSelector(new ColumnName("demo", "users", "name"));
        Selector right = new StringSelector(tablename, "name_5");
        Relation relation = new Relation(left, Operator.EQ, right);
        where.add(relation);

        selectStatement.setWhere(where);

        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("SelectId", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        try {
            validator.validate(parsedQuery);
            Assert.assertTrue(true);
        } catch (ValidationException e) {
            fail(e.getMessage());
        } catch (IgnoreQueryException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void validateWhere2columnsOk() {
        String query =
                "SELECT users.name, users.age FROM demo.users WHERE users.name = 'name_5' AND users.age = 15;";
        List<Selector> selectorList = new ArrayList<>();

        TableName tablename = new TableName("demo", "users");

        Selector selector = new StringSelector(tablename, "name");
        Selector selector2 = new StringSelector(tablename, "age");
        selectorList.add(selector);
        selectorList.add(selector2);
        SelectExpression selectExpression = new SelectExpression(selectorList);

        SelectStatement selectStatement = new SelectStatement(selectExpression, tablename);

        List<Relation> where = new ArrayList<>();
        Selector left = new ColumnSelector(new ColumnName(tablename, "name"));
        Selector right = new StringSelector(tablename, "name_5");
        Relation relation = new Relation(left, Operator.EQ, right);

        Selector left2 = new ColumnSelector(new ColumnName(tablename, "age"));
        Selector right2 = new IntegerSelector(tablename, "15");
        Relation relation2 = new Relation(left2, Operator.EQ, right2);

        where.add(relation);
        where.add(relation2);

        selectStatement.setWhere(where);

        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("SelectId", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        try {
            validator.validate(parsedQuery);
            Assert.assertTrue(true);
        } catch (ValidationException e) {
            fail(e.getMessage());
        } catch (IgnoreQueryException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void validateWhereColumnUnknown() {
        String query =
                "SELECT users.name, users.age FROM demo.users WHERE users.unknown = 'name_5' AND users.age = 15;";
        List<Selector> selectorList = new ArrayList<>();

        TableName tablename = new TableName("demo", "users");

        Selector selector = new StringSelector(tablename, "name");
        Selector selector2 = new StringSelector(tablename, "age");
        selectorList.add(selector);
        selectorList.add(selector2);
        SelectExpression selectExpression = new SelectExpression(selectorList);

        SelectStatement selectStatement = new SelectStatement(selectExpression, tablename);

        List<Relation> where = new ArrayList<>();
        Selector left = new ColumnSelector(new ColumnName(tablename, "unknown"));
        Selector right = new StringSelector(tablename, "name_5");
        Relation relation = new Relation(left, Operator.EQ, right);

        Selector left2 = new ColumnSelector(new ColumnName(tablename, "age"));
        Selector right2 = new IntegerSelector(tablename, "15");
        Relation relation2 = new Relation(left2, Operator.EQ, right2);

        where.add(relation);
        where.add(relation2);

        selectStatement.setWhere(where);

        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("SelectId", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        try {
            validator.validate(parsedQuery);
            fail("Invalid columnName in where not checked");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void validateWhereIntegerFail() {
        String query =
                "SELECT users.name, users.age FROM demo.users WHERE users.name = 'name_5' AND users.age = '15';";
        List<Selector> selectorList = new ArrayList<>();

        TableName tablename = new TableName("demo", "users");

        Selector selector = new StringSelector(tablename, "name");
        Selector selector2 = new StringSelector(tablename, "age");
        selectorList.add(selector);
        selectorList.add(selector2);
        SelectExpression selectExpression = new SelectExpression(selectorList);

        SelectStatement selectStatement = new SelectStatement(selectExpression, tablename);

        List<Relation> where = new ArrayList<>();
        Selector left = new ColumnSelector(new ColumnName(tablename, "unknown"));
        Selector right = new StringSelector(tablename, "name_5");
        Relation relation = new Relation(left, Operator.EQ, right);

        Selector left2 = new ColumnSelector(new ColumnName(tablename, "age"));
        Selector right2 = new StringSelector(tablename, "15");
        Relation relation2 = new Relation(left2, Operator.EQ, right2);

        where.add(relation);
        where.add(relation2);

        selectStatement.setWhere(where);

        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("SelectId", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        try {
            validator.validate(parsedQuery);
            fail("Invalid columnName in where not checked");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void validateWhereStringFail() {
        String query =
                "SELECT users.name, users.age FROM demo.users WHERE users.name = 15 AND users.age = 15;";

        List<Selector> selectorList = new ArrayList<>();

        TableName tablename = new TableName("demo", "users");

        Selector selector = new StringSelector(tablename, "name");
        Selector selector2 = new StringSelector(tablename, "age");
        selectorList.add(selector);
        selectorList.add(selector2);
        SelectExpression selectExpression = new SelectExpression(selectorList);

        SelectStatement selectStatement = new SelectStatement(selectExpression, tablename);

        List<Relation> where = new ArrayList<>();
        Selector left = new ColumnSelector(new ColumnName(tablename, "name"));
        Selector right = new IntegerSelector(tablename, 15);
        Relation relation = new Relation(left, Operator.EQ, right);

        Selector left2 = new ColumnSelector(new ColumnName(tablename, "age"));
        Selector right2 = new IntegerSelector(tablename, "15");
        Relation relation2 = new Relation(left2, Operator.EQ, right2);

        where.add(relation);
        where.add(relation2);

        selectStatement.setWhere(where);

        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("SelectId", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        try {
            validator.validate(parsedQuery);
            fail("Invalid columnName in where not checked");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void validateOperatorString() {
        String[] operators = { ">", "<", ">=", "<=" };

        List<Selector> selectorList = new ArrayList<>();

        TableName tablename = new TableName("demo", "users");

        Selector selector = new StringSelector(tablename, "name");
        Selector selector2 = new StringSelector(tablename, "age");
        selectorList.add(selector);
        selectorList.add(selector2);
        SelectExpression selectExpression = new SelectExpression(selectorList);

        SelectStatement selectStatement = new SelectStatement(selectExpression, tablename);

        List<Relation> where = new ArrayList<>();
        Selector left = new ColumnSelector(new ColumnName(tablename, "name"));
        Selector right = new StringSelector(tablename, "name_5");
        Relation relation = null;

        for (String operator : operators) {
            String query = "SELECT users.name, users.age FROM demo.users WHERE users.name " + operator
                    + " 'name_5';";

            switch (operator) {
            case ">":
                relation = new Relation(left, Operator.GT, right);
                break;
            case "<":
                relation = new Relation(left, Operator.LT, right);
                break;
            case "<=":
                relation = new Relation(left, Operator.LET, right);
                break;
            case ">=":
                relation = new Relation(left, Operator.GET, right);
                break;
            default:
                break;
            }

            where.add(relation);

            selectStatement.setWhere(where);

            Validator validator = new Validator();

            BaseQuery baseQuery = new BaseQuery("SelectId", query, new CatalogName("demo"));

            IParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

            try {
                IValidatedQuery validationResult = validator.validate(parsedQuery);
                assertNotNull(validationResult, "Validation Result shouldn't be null");
            } catch (ValidationException e) {
                fail();
            } catch (IgnoreQueryException e) {
                fail(e.getMessage());
            }
        }
    }

    @Test
    public void validateOperatorBooleanFail() {
        String[] operators = { ">", "<", ">=", "<=" };

        List<Selector> selectorList = new ArrayList<>();

        TableName tablename = new TableName("demo", "users");

        Selector selector = new StringSelector(tablename, "name");
        Selector selector2 = new StringSelector(tablename, "age");
        selectorList.add(selector);
        selectorList.add(selector2);
        SelectExpression selectExpression = new SelectExpression(selectorList);

        SelectStatement selectStatement = new SelectStatement(selectExpression, tablename);

        List<Relation> where = new ArrayList<>();
        Selector left = new ColumnSelector(new ColumnName(tablename, "bool"));
        Selector right = new BooleanSelector(tablename, true);
        Relation relation = null;
        for (String operator : operators) {
            String query =
                    "SELECT users.bool FROM demo.users WHERE users.bool " + operator + " true;";
            switch (operator) {
            case ">":
                relation = new Relation(left, Operator.GT, right);
                break;
            case "<":
                relation = new Relation(left, Operator.LT, right);
                break;
            case "<=":
                relation = new Relation(left, Operator.LET, right);
                break;
            case ">=":
                relation = new Relation(left, Operator.GET, right);
                break;
            default:
                break;
            }

            where.add(relation);

            selectStatement.setWhere(where);

            Validator validator = new Validator();

            BaseQuery baseQuery = new BaseQuery("SelectId", query, new CatalogName("demo"));

            IParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

            try {
                validator.validate(parsedQuery);
                fail("Strings operator must be only '=' ");
            } catch (ValidationException e) {
                Assert.assertTrue(true);
            } catch (IgnoreQueryException e) {
                fail(e.getMessage());
            }

        }
    }

    @Test
    public void testValidateNotEqualOk() {

        String query =
                "SELECT users.name, users.age FROM demo.users WHERE users.email <> 'name_1@domain.com';";

        List<Selector> selectorList = new ArrayList<>();

        TableName tablename = new TableName("demo", "users");

        Selector selector = new StringSelector(tablename, "name");
        Selector selector2 = new StringSelector(tablename, "age");
        selectorList.add(selector);
        selectorList.add(selector2);
        SelectExpression selectExpression = new SelectExpression(selectorList);

        SelectStatement selectStatement = new SelectStatement(selectExpression, tablename);

        List<Relation> where = new ArrayList<>();
        Selector left = new ColumnSelector(new ColumnName("demo", "users", "email"));
        Selector right = new StringSelector(tablename, "name_1@domain.com");
        Relation relation = new Relation(left, Operator.DISTINCT, right);
        where.add(relation);

        selectStatement.setWhere(where);

        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("SelectId", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        try {
            validator.validate(parsedQuery);
            Assert.assertTrue(true);
        } catch (ValidationException e) {
            fail(e.getMessage());
        } catch (IgnoreQueryException e) {
            fail(e.getMessage());
        }
    }

    //
    // Tests with inner joins
    //
    @Test
    public void validateInnerJoinBasicOk() {
        String query =
                "SELECT users.name, users.age, users.email FROM demo.users "
                        + "INNER JOIN demo.users ON users.name=users.name;";

        List<Selector> selectorList = new ArrayList<>();
        Selector selector = new ColumnSelector(new ColumnName("demo", "users", "name"));
        Selector selector2 = new ColumnSelector(new ColumnName("demo", "users", "age"));
        Selector selector3 = new ColumnSelector(new ColumnName("demo", "users", "email"));
        selectorList.add(selector);
        selectorList.add(selector2);
        selectorList.add(selector3);
        SelectExpression selectExpression = new SelectExpression(selectorList);
        TableName tablename = new TableName("demo", "users");

        SelectStatement selectStatement = new SelectStatement(selectExpression, tablename);
        List<Relation> joinRelations = new ArrayList<>();

        Selector left = new ColumnSelector(new ColumnName("demo", "users", "name"));
        Selector right = new ColumnSelector(new ColumnName("demo", "users", "name"));

        Relation relation = new Relation(left, Operator.EQ, right);
        joinRelations.add(relation);

        InnerJoin join = new InnerJoin(new TableName("demo", "users"), joinRelations);
        selectStatement.addJoin(join);

        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("SelectId", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        try {
            validator.validate(parsedQuery);
            Assert.assertTrue(true);
        } catch (ValidationException e) {
            fail(e.getMessage());
        } catch (IgnoreQueryException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void validateUnknownKs1Fail() {
        String query =
                "SELECT users.name, users.age, users.email FROM unknown.users "
                        + "INNER JOIN demo.users ON users.name=users.name;";
        List<Selector> selectorList = new ArrayList<>();
        Selector selector = new ColumnSelector(new ColumnName("demo", "users", "name"));
        Selector selector2 = new ColumnSelector(new ColumnName("demo", "users", "age"));
        Selector selector3 = new ColumnSelector(new ColumnName("demo", "users", "email"));
        selectorList.add(selector);
        selectorList.add(selector2);
        selectorList.add(selector3);
        SelectExpression selectExpression = new SelectExpression(selectorList);
        TableName tablename = new TableName("unknown", "users");

        SelectStatement selectStatement = new SelectStatement(selectExpression, tablename);
        List<Relation> joinRelations = new ArrayList<>();

        Selector left = new ColumnSelector(new ColumnName("demo", "users", "name"));
        Selector right = new ColumnSelector(new ColumnName("demo", "users", "name"));

        Relation relation = new Relation(left, Operator.EQ, right);
        joinRelations.add(relation);

        InnerJoin join = new InnerJoin(new TableName("demo", "users"), joinRelations);
        selectStatement.addJoin(join);

        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("SelectId", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        try {
            validator.validate(parsedQuery);
            fail("Not valid catalog in FROM");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void validateUnknownKs2Fail() {
        String query =
                "SELECT users.name, users.age, users.email FROM demo.users "
                        + "INNER JOIN unknown.users ON users.name=users.name;";
        List<Selector> selectorList = new ArrayList<>();
        Selector selector = new ColumnSelector(new ColumnName("demo", "users", "name"));
        Selector selector2 = new ColumnSelector(new ColumnName("demo", "users", "age"));
        Selector selector3 = new ColumnSelector(new ColumnName("demo", "users", "email"));
        selectorList.add(selector);
        selectorList.add(selector2);
        selectorList.add(selector3);
        SelectExpression selectExpression = new SelectExpression(selectorList);
        TableName tablename = new TableName("demo", "users");

        SelectStatement selectStatement = new SelectStatement(selectExpression, tablename);
        List<Relation> joinRelations = new ArrayList<>();

        Selector left = new ColumnSelector(new ColumnName("demo", "users", "name"));
        Selector right = new ColumnSelector(new ColumnName("demo", "users", "name"));

        Relation relation = new Relation(left, Operator.EQ, right);
        joinRelations.add(relation);

        InnerJoin join = new InnerJoin(new TableName("unknown", "users"), joinRelations);
        selectStatement.addJoin(join);

        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("SelectId", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        try {
            validator.validate(parsedQuery);
            fail("Not valid catalog in INNER JOIN");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void validateUnknownTable2Fail() {
        String query =
                "SELECT users.name, users.age, users.email FROM demo.users "
                        + "INNER JOIN demo.unknown ON users.name=users.name;";
        List<Selector> selectorList = new ArrayList<>();
        Selector selector = new ColumnSelector(new ColumnName("demo", "users", "name"));
        Selector selector2 = new ColumnSelector(new ColumnName("demo", "users", "age"));
        Selector selector3 = new ColumnSelector(new ColumnName("demo", "users", "email"));
        selectorList.add(selector);
        selectorList.add(selector2);
        selectorList.add(selector3);
        SelectExpression selectExpression = new SelectExpression(selectorList);
        TableName tablename = new TableName("demo", "users");

        SelectStatement selectStatement = new SelectStatement(selectExpression, tablename);
        List<Relation> joinRelations = new ArrayList<>();

        Selector left = new ColumnSelector(new ColumnName("demo", "users", "name"));
        Selector right = new ColumnSelector(new ColumnName("demo", "users", "name"));

        Relation relation = new Relation(left, Operator.EQ, right);
        joinRelations.add(relation);

        InnerJoin join = new InnerJoin(new TableName("demo", "unknown"), joinRelations);
        selectStatement.addJoin(join);

        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("SelectId", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        try {
            validator.validate(parsedQuery);
            fail("Not valid catalog in INNER JOIN");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void validateOnUnknownKsFail() {
        String query =
                "SELECT users.name, users.age, users.email FROM demo.users "
                        + "INNER JOIN demo.users ON unknown.name=users.name;";
        List<Selector> selectorList = new ArrayList<>();
        Selector selector = new ColumnSelector(new ColumnName("demo", "users", "name"));
        Selector selector2 = new ColumnSelector(new ColumnName("demo", "users", "age"));
        Selector selector3 = new ColumnSelector(new ColumnName("demo", "users", "email"));
        selectorList.add(selector);
        selectorList.add(selector2);
        selectorList.add(selector3);
        SelectExpression selectExpression = new SelectExpression(selectorList);
        TableName tablename = new TableName("demo", "users");

        SelectStatement selectStatement = new SelectStatement(selectExpression, tablename);
        List<Relation> joinRelations = new ArrayList<>();

        Selector left = new ColumnSelector(new ColumnName("unknown", "users", "name"));
        Selector right = new ColumnSelector(new ColumnName("demo", "users", "name"));

        Relation relation = new Relation(left, Operator.EQ, right);
        joinRelations.add(relation);

        InnerJoin join = new InnerJoin(new TableName("demo", "users"), joinRelations);
        selectStatement.addJoin(join);

        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("SelectId", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        try {
            validator.validate(parsedQuery);
            fail("Not valid catalog in parameter ON of an InnerJoin ");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void validateOnUnknownKs2Fail() {
        String query =
                "SELECT users.name, users.age, users.email FROM demo.users "
                        + "INNER JOIN demo.users ON users.name=pepito.name;";
        List<Selector> selectorList = new ArrayList<>();
        Selector selector = new ColumnSelector(new ColumnName("demo", "users", "name"));
        Selector selector2 = new ColumnSelector(new ColumnName("demo", "users", "age"));
        Selector selector3 = new ColumnSelector(new ColumnName("demo", "users", "email"));
        selectorList.add(selector);
        selectorList.add(selector2);
        selectorList.add(selector3);
        SelectExpression selectExpression = new SelectExpression(selectorList);
        TableName tablename = new TableName("demo", "users");

        SelectStatement selectStatement = new SelectStatement(selectExpression, tablename);
        List<Relation> joinRelations = new ArrayList<>();

        Selector left = new ColumnSelector(new ColumnName("demo", "users", "name"));
        Selector right = new ColumnSelector(new ColumnName("demo", "pepito", "name"));

        Relation relation = new Relation(left, Operator.EQ, right);
        joinRelations.add(relation);

        InnerJoin join = new InnerJoin(new TableName("demo", "users"), joinRelations);
        selectStatement.addJoin(join);

        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("SelectId", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        try {
            validator.validate(parsedQuery);
            fail("Not valid catalog in parameter ON of an InnerJoin ");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void validateOnUnknownTableFail() {
        String query =
                "SELECT users.name, users.age, users.email FROM demo.users "
                        + "INNER JOIN demo.users ON demo.unknown=users.name;";
        List<Selector> selectorList = new ArrayList<>();
        Selector selector = new ColumnSelector(new ColumnName("demo", "users", "name"));
        Selector selector2 = new ColumnSelector(new ColumnName("demo", "users", "age"));
        Selector selector3 = new ColumnSelector(new ColumnName("demo", "users", "email"));
        selectorList.add(selector);
        selectorList.add(selector2);
        selectorList.add(selector3);
        SelectExpression selectExpression = new SelectExpression(selectorList);
        TableName tablename = new TableName("demo", "users");

        SelectStatement selectStatement = new SelectStatement(selectExpression, tablename);
        List<Relation> joinRelations = new ArrayList<>();

        Selector left = new ColumnSelector(new ColumnName("demo", "users", "unknown"));
        Selector right = new ColumnSelector(new ColumnName("demo", "users", "name"));

        Relation relation = new Relation(left, Operator.EQ, right);
        joinRelations.add(relation);

        InnerJoin join = new InnerJoin(new TableName("demo", "users"), joinRelations);
        selectStatement.addJoin(join);

        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("SelectId", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        try {
            validator.validate(parsedQuery);
            fail("Not valid columnName in parameter ON of an InnerJoin ");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void validateOnUnknownTable2Fail() {
        String query =
                "SELECT users.name, users.age, users.email FROM demo.users "
                        + "INNER JOIN demo.users ON users.name=demo.unknown;";
        List<Selector> selectorList = new ArrayList<>();
        Selector selector = new ColumnSelector(new ColumnName("demo", "users", "name"));
        Selector selector2 = new ColumnSelector(new ColumnName("demo", "users", "age"));
        Selector selector3 = new ColumnSelector(new ColumnName("demo", "users", "email"));
        selectorList.add(selector);
        selectorList.add(selector2);
        selectorList.add(selector3);
        SelectExpression selectExpression = new SelectExpression(selectorList);
        TableName tablename = new TableName("demo", "users");

        SelectStatement selectStatement = new SelectStatement(selectExpression, tablename);
        List<Relation> joinRelations = new ArrayList<>();

        Selector left = new ColumnSelector(new ColumnName("demo", "users", "name"));
        Selector right = new ColumnSelector(new ColumnName("demo", "users", "unknown"));

        Relation relation = new Relation(left, Operator.EQ, right);
        joinRelations.add(relation);

        InnerJoin join = new InnerJoin(new TableName("demo", "users"), joinRelations);
        selectStatement.addJoin(join);

        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("SelectId", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        try {
            validator.validate(parsedQuery);
            fail("Not valid columnName in parameter ON of an InnerJoin ");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void validateInnerJoinFailWhereOk() {
        String query =
                "SELECT users.name, users.age, users_info.info FROM demo.users "
                        + "INNER JOIN demo.users_info ON users.name=users_info.link_name "
                        + "WHERE users.name = 'name_3';";
        List<Selector> selectorList = new ArrayList<>();
        Selector selector = new ColumnSelector(new ColumnName("demo", "users", "name"));
        Selector selector2 = new ColumnSelector(new ColumnName("demo", "users", "age"));
        Selector selector3 = new ColumnSelector(new ColumnName("demo", "users", "email"));
        selectorList.add(selector);
        selectorList.add(selector2);
        selectorList.add(selector3);
        SelectExpression selectExpression = new SelectExpression(selectorList);
        TableName tablename = new TableName("demo", "users");

        SelectStatement selectStatement = new SelectStatement(selectExpression, tablename);
        List<Relation> joinRelations = new ArrayList<>();

        Selector left = new ColumnSelector(new ColumnName("demo", "users", "name"));
        Selector right = new ColumnSelector(new ColumnName("demo", "users", "unknown"));

        Relation relation = new Relation(left, Operator.EQ, right);
        joinRelations.add(relation);

        InnerJoin join = new InnerJoin(new TableName("demo", "users"), joinRelations);
        selectStatement.addJoin(join);

        List<Relation> where = new ArrayList<>();
        Selector leftWh = new ColumnSelector(new ColumnName("demo", "users", "name"));
        Selector rightWh = new StringSelector(new TableName("demo", "users"), "name_3");
        Relation relationWh = new Relation(leftWh, Operator.EQ, rightWh);
        where.add(relationWh);

        selectStatement.setWhere(where);

        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("SelectId", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        try {
            validator.validate(parsedQuery);
            fail("Not valid columnName in parameter ON of an InnerJoin ");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void validateInnerJoinWhereOk() {
        String query =
                "SELECT users.name, users.age, users_info.info FROM demo.users "
                        + "INNER JOIN demo.users_info ON users.name=users.name "
                        + "WHERE users.name = 'name_3';";
        List<Selector> selectorList = new ArrayList<>();
        Selector selector = new ColumnSelector(new ColumnName("demo", "users", "name"));
        Selector selector2 = new ColumnSelector(new ColumnName("demo", "users", "age"));
        Selector selector3 = new ColumnSelector(new ColumnName("demo", "users", "email"));
        selectorList.add(selector);
        selectorList.add(selector2);
        selectorList.add(selector3);
        SelectExpression selectExpression = new SelectExpression(selectorList);
        TableName tablename = new TableName("demo", "users");

        SelectStatement selectStatement = new SelectStatement(selectExpression, tablename);
        List<Relation> joinRelations = new ArrayList<>();

        Selector left = new ColumnSelector(new ColumnName("demo", "users", "name"));
        Selector right = new ColumnSelector(new ColumnName("demo", "users", "name"));

        Relation relation = new Relation(left, Operator.EQ, right);
        joinRelations.add(relation);

        InnerJoin join = new InnerJoin(new TableName("demo", "users"), joinRelations);
        selectStatement.addJoin(join);

        List<Relation> where = new ArrayList<>();
        Selector leftWh = new ColumnSelector(new ColumnName("demo", "users", "name"));
        Selector rightWh = new StringSelector(new TableName("demo", "users"), "name_3");
        Relation relationWh = new Relation(leftWh, Operator.EQ, rightWh);
        where.add(relationWh);

        selectStatement.setWhere(where);

        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("SelectId", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        try {
            validator.validate(parsedQuery);
            Assert.assertTrue(true);
        } catch (ValidationException e) {
            fail(e.getMessage());
        } catch (IgnoreQueryException e) {
            fail(e.getMessage());
        }
    }

    /*
    @Test
    public void testValidateInClauseWithMixedDataFail() {

        String query = "SELECT users.name FROM demo.users WHERE users.email IN ('name_11@domain.com', 19);";

        List<Selector> selectorList = new ArrayList<>();
        Selector selector = new StringSelector("name");
        Selector selector2 = new StringSelector("age");
        selectorList.add(selector);
        selectorList.add(selector2);
        SelectExpression selectExpression = new SelectExpression(selectorList);
        TableName tablename = new TableName("demo", "users");

        SelectStatement selectStatement = new SelectStatement(selectExpression, tablename);

        List<Relation> where = new ArrayList<>();
        Selector left = new ColumnSelector(new ColumnName("demo", "users", "email"));
        Selector right = new StringSelector("name_5");
        Relation relation = new Relation(left, Operator.IN, right);
        where.add(relation);

        selectStatement.setWhere(where);

        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("SelectId", query, new CatalogName("demo"));

        ParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        try {
            validator.validate(parsedQuery);
            Assert.assertTrue(true);
        } catch (ValidationException e) {
            Assert.fail(e.getMessage());
        } catch (IgnoreQueryException e) {
            Assert.fail(e.getMessage());
        }

    }

    @Test
    public void testValidateBasicInClauseWithStringsOk() {

        String inputText =
                "SELECT users.name FROM demo.users WHERE users.email IN ('name_11@domain.com', 'name_9@domain.com');";

        validateOk(inputText, "testValidateBasicInClauseWithStringsOk");
    }

    @Test
    public void testValidateBasicInClauseWithIntegersOk() {

        String inputText = "SELECT users.name FROM demo.users WHERE users.age IN (19, 31);";

        validateOk(inputText, "testValidateBasicInClauseWithIntegersOk");
    }

    @Test
    public void testValidateBasicBetweenClauseWithStringDataOk() {

        String inputText =
                "SELECT users.name FROM demo.users WHERE users.email BETWEEN 'aaaa_00@domain.com' AND 'zzzz_99@domain.com';";

        validateOk(inputText, "testValidateBasicBetweenClauseWithStringDataOk");

    }

    @Test
    public void testValidateBasicBetweenClauseWithoutResultsOk() {

        String inputText = "SELECT users.name FROM demo.users WHERE users.email BETWEEN 'a' AND 'b';";

        validateOk(inputText, "testValidateBasicBetweenClauseWithoutResultsOk");
    }

    @Test
    public void testValidateBasicBetweenClauseWithIntegerDataOk() {

        String inputText = "SELECT users.name FROM demo.users WHERE users.age BETWEEN 10 AND 25;";

        validateOk(inputText, "testValidateBasicBetweenClauseWithIntegerDataOk");
    }

    @Test
    public void testValidateBasicBetweenClauseWithMixedDataTypeFail() {

        String inputText = "SELECT users.name FROM demo.users WHERE users.age BETWEEN 'user_1' AND 25;";

        validateFail(inputText, "testValidateBasicBetweenClauseWithMixedDataTypeFail");
    }

    @Test
    public void testValidateGroupByClauseCountOk() {

        String inputText = "SELECT users.gender, COUNT(*) FROM demo.users GROUP BY users.gender;";

        validateOk(inputText, "testValidateGroupByClauseCountOk");
    }

    @Test
    public void testValidateGroupByClauseCountWithAliasOk() {

        String inputText = "SELECT users.gender AS g, COUNT(*) FROM demo.users GROUP BY g;";
        String expectedText =
                "SELECT users.gender AS g, COUNT(*) FROM demo.users GROUP BY users.gender;";
        validateOk(inputText, expectedText, "testValidateGroupByClauseCountWithAliasOk");
    }

    @Test
    public void testValidateGroupByClauseSumOk() {

        String inputText = "SELECT users.gender, SUM(users.age) FROM demo.users GROUP BY users.gender;";

        validateOk(inputText, "testValidateGroupByClauseSumOk");
    }

    @Test
    public void testValidateGroupMissingFieldOk() {

        String inputText = "SELECT SUM(users.age) FROM demo.users GROUP BY users.gender;";

        validateOk(inputText, "testValidateGroupMissingFieldOk");
    }

    @Test
    public void testGroupByWithMissingSelectorFieldOk() {

        String inputText = "SELECT sum(users.age) FROM demo.users GROUP BY users.gender;";
        validateOk(inputText, "testGroupByWithMissingSelectorFieldOk");
    }

    @Test
    public void testNoGroupWithAggregationFunctionNoGroupByOk() {

        String inputText = "SELECT users.gender, sum(users.age) FROM demo.users;";
        validateOk(inputText, "testNoGroupWithAggregationFunctionNoGroupByOk");
    }
*/
    @Test
    public void testValidateSimpleOrderByOk() {

        String query = "SELECT * FROM demo.users ORDER BY users.age;";
        List<Selector> selectorList = new ArrayList<>();

        TableName tablename = new TableName("demo", "users");

        Selector selector = new StringSelector(tablename, "name");
        Selector selector2 = new StringSelector(tablename, "age");
        selectorList.add(selector);
        selectorList.add(selector2);
        SelectExpression selectExpression = new SelectExpression(selectorList);

        SelectStatement selectStatement = new SelectStatement(selectExpression, tablename);

        List<OrderByClause> orderByClauseClauses = new ArrayList<>();
        Selector selectorOrderBy = new ColumnSelector(new ColumnName(tablename, "age"));
        orderByClauseClauses.add(new OrderByClause(OrderDirection.ASC, selectorOrderBy));

        selectStatement.setOrderByClauses(orderByClauseClauses);

        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("SelectId", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        try {
            validator.validate(parsedQuery);
            Assert.assertTrue(true);
        } catch (ValidationException e) {
            fail(e.getMessage());
        } catch (IgnoreQueryException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testValidateMultipleOrderByOk() {

        String query = "SELECT * FROM demo.users ORDER BY users.gender, users.age;";
        List<Selector> selectorList = new ArrayList<>();

        TableName tablename = new TableName("demo", "users");

        Selector selector = new StringSelector(tablename, "name");
        Selector selector2 = new StringSelector(tablename, "age");
        selectorList.add(selector);
        selectorList.add(selector2);
        SelectExpression selectExpression = new SelectExpression(selectorList);

        SelectStatement selectStatement = new SelectStatement(selectExpression, tablename);

        List<OrderByClause> orderByClauseClauses = new ArrayList<>();
        Selector selectorOrderBy1 = new ColumnSelector(new ColumnName(tablename, "gender"));
        Selector selectorOrderBy2 = new ColumnSelector(new ColumnName(tablename, "age"));
        orderByClauseClauses.add(new OrderByClause(OrderDirection.DESC, selectorOrderBy1));
        orderByClauseClauses.add(new OrderByClause(OrderDirection.ASC, selectorOrderBy2));

        selectStatement.setOrderByClauses(orderByClauseClauses);

        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("SelectId", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        try {
            validator.validate(parsedQuery);
            Assert.assertTrue(true);
        } catch (ValidationException e) {
            fail(e.getMessage());
        } catch (IgnoreQueryException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testValidateSimpleOrderByUnknownFieldFail() {

        String query = "SELECT * FROM demo.users ORDER BY users.unknown;";

        List<Selector> selectorList = new ArrayList<>();

        TableName tablename = new TableName("demo", "users");

        Selector selector = new StringSelector(tablename, "name");
        Selector selector2 = new StringSelector(tablename, "age");
        selectorList.add(selector);
        selectorList.add(selector2);
        SelectExpression selectExpression = new SelectExpression(selectorList);

        SelectStatement selectStatement = new SelectStatement(selectExpression, tablename);

        List<OrderByClause> orderByClauseClauses = new ArrayList<>();
        Selector selectorOrderBy = new ColumnSelector(new ColumnName(tablename, "unknown"));

        orderByClauseClauses.add(new OrderByClause(OrderDirection.ASC, selectorOrderBy));

        selectStatement.setOrderByClauses(orderByClauseClauses);

        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("SelectId", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        try {
            validator.validate(parsedQuery);
            fail("ColumnName not exist for ORDER BY");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void simpleFromAlias() {
        String inputText = "SELECT t.name, t.age FROM demo.users t";
        String expectedText = "SELECT demo.users.name, demo.users.age FROM demo.users AS t";
        Selector selector1 = new ColumnSelector(new ColumnName("demo", "t", "name"));
        Selector selector2 = new ColumnSelector(new ColumnName("demo", "t", "age"));
        List<Selector> selectorList = new ArrayList<>();
        selectorList.add(selector1);
        selectorList.add(selector2);
        SelectExpression selectExpression = new SelectExpression(selectorList);

        TableName tablename = new TableName("demo", "users");
        tablename.setAlias("t");

        SelectStatement selectStatement = new SelectStatement(selectExpression, tablename);
        Validator validator = new Validator();
        BaseQuery baseQuery = new BaseQuery("SelectId", inputText, new CatalogName("demo"));
        IParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        IValidatedQuery validatedQuery = null;
        try {
            validatedQuery = validator.validate(parsedQuery);
        } catch (ValidationException | IgnoreQueryException e) {
            fail("Cannot validate statement", e);
        }

        assertNotNull(validatedQuery, "Expecting validated query");
        assertEquals(validatedQuery.toString(), expectedText, "Invalid resolution");

    }

    @Test
    public void simpleFunction(){
        String inputText = "SELECT getYear(users.age) FROM demo.users";
        String expectedText = "SELECT getYear(demo.users.age) AS getYear FROM demo.users";

        ColumnName col1 = new ColumnName(null, "users", "age");
        Selector selector1 = new FunctionSelector(new TableName("demo", "users"), "getYear", new LinkedList<Selector>(
                Collections.singleton(new ColumnSelector(col1))));
        List<Selector> selectorList = new ArrayList<>();
        selectorList.add(selector1);
        SelectExpression selectExpression = new SelectExpression(selectorList);

        TableName tablename = new TableName("demo", "users");

        SelectStatement selectStatement = new SelectStatement(selectExpression, tablename);
        Validator validator = new Validator();
        BaseQuery baseQuery = new BaseQuery("SelectId", inputText, new CatalogName("demo"));
        IParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);

        IValidatedQuery validatedQuery = null;
        try {
            validatedQuery = validator.validate(parsedQuery);
        } catch (ValidationException | IgnoreQueryException e) {
            fail("Cannot validate valid statement", e);
        }

        assertNotNull(validatedQuery, "Expecting validated query");
        assertEquals(validatedQuery.toString(), expectedText, "Invalid resolution");

    }

    @Test
    public void simpleSelectAlias(){
        String inputText = "SELECT demo.users.name AS n, demo.users.age FROM demo.users WHERE n = 'name_1'";
        String expectedText = "SELECT demo.users.name AS n, demo.users.age FROM demo.users " +
                "WHERE demo.users.name = 'name_1'";

        ColumnName n1 = new ColumnName("demo", "users", "name");
        //n1.setAlias("n");
        Selector selector1 = new ColumnSelector(n1);
        selector1.setAlias("n");
        Selector selector2 = new ColumnSelector(new ColumnName("demo", "users", "age"));
        List<Selector> selectorList = new ArrayList<>();
        selectorList.add(selector1);
        selectorList.add(selector2);
        SelectExpression selectExpression = new SelectExpression(selectorList);

        TableName tablename = new TableName("demo", "users");

        List<Relation> where = new ArrayList<>();
        Selector leftWh = new ColumnSelector(new ColumnName("", "", "n"));
        Selector rightWh = new StringSelector(new TableName("demo", "users"), "name_1");
        Relation relationWh = new Relation(leftWh, Operator.EQ, rightWh);
        where.add(relationWh);

        SelectStatement selectStatement = new SelectStatement(selectExpression, tablename);
        selectStatement.setWhere(where);

        Validator validator = new Validator();
        BaseQuery baseQuery = new BaseQuery("SelectId", inputText, new CatalogName("demo"));
        IParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);
        IValidatedQuery validatedQuery = null;
        try {
            validatedQuery = validator.validate(parsedQuery);
        } catch (ValidationException | IgnoreQueryException e) {
            fail("Cannot validate valid statement", e);
        }

        assertNotNull(validatedQuery, "Expecting validated query");
        assertEquals(validatedQuery.toString(), expectedText, "Invalid resolution");

    }



    @Test
    public void simpleJoinAlias() {
        String inputText =
                "SELECT u.name AS n, u.age, ui.info FROM demo.users u "
                        + "INNER JOIN demo.users_info ui ON n=ui.name "
                        + "WHERE n = 'name_1'";
        String expectedText =
                "SELECT demo.users.name AS n, demo.users.age, demo.users_info.info FROM demo.users AS u "
                        + "INNER JOIN demo.users_info AS ui ON demo.users.name = demo.users_info.name "
                        + "WHERE demo.users.name = 'name_1'";

        Selector selector1 = new ColumnSelector(new ColumnName("", "u", "name"));
        selector1.setAlias("n");
        Selector selector2 = new ColumnSelector(new ColumnName("", "u", "age"));
        Selector selector3 = new ColumnSelector(new ColumnName("", "ui", "info"));
        List<Selector> selectorList = new ArrayList<>();
        selectorList.add(selector1);
        selectorList.add(selector2);
        selectorList.add(selector3);
        SelectExpression selectExpression = new SelectExpression(selectorList);

        TableName tablename = new TableName("demo", "users");
        tablename.setAlias("u");

        TableName joinTable = new TableName("demo", "users_info");
        joinTable.setAlias("ui");

        List<Relation> joinRelations = new ArrayList<>();
        Selector left = new ColumnSelector(new ColumnName("", "", "n"));
        Selector right = new ColumnSelector(new ColumnName("", "ui", "name"));

        Relation relation = new Relation(left, Operator.EQ, right);
        joinRelations.add(relation);
        InnerJoin join = new InnerJoin(joinTable, joinRelations);

        List<Relation> where = new ArrayList<>();
        Selector leftWh = new ColumnSelector(new ColumnName("", "", "n"));
        Selector rightWh = new StringSelector(new TableName("demo", "users"), "name_1");
        Relation relationWh = new Relation(leftWh, Operator.EQ, rightWh);
        where.add(relationWh);

        SelectStatement selectStatement = new SelectStatement(selectExpression, tablename);
        selectStatement.addJoin(join);
        selectStatement.setWhere(where);

        Validator validator = new Validator();
        BaseQuery baseQuery = new BaseQuery("SelectId", inputText, new CatalogName("demo"));
        IParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);
        IValidatedQuery validatedQuery = null;
        try {
            validatedQuery = validator.validate(parsedQuery);
        } catch (ValidationException | IgnoreQueryException e) {
            fail("Cannot validate valid statement", e);
        }

        assertNotNull(validatedQuery, "Expecting validated query");
        assertEquals(validatedQuery.toString(), expectedText, "Invalid resolution");

    }

    /*
    @Test
    public void testComplexQueryWithAliasesOk() {

        String inputText =
                "SELECT users.age AS edad, users.gender AS genero, sum(users.age) AS suma, min(gender) AS minimo, count(*) AS contador FROM demo.users "
                        + "WHERE edad > 13 AND genero IN ('male', 'female') ORDER BY edad DESC GROUP BY genero;";

        String expectedText =
                "SELECT users.age AS edad, users.gender AS genero, SUM(users.age) AS suma, MIN(users.gender) AS minimo, COUNT(*) AS contador FROM demo.users "
                        + "WHERE users.age > 13 AND users.gender IN ('male', 'female') ORDER BY users.age DESC GROUP BY users.gender;";

        assertEquals(inputText, expectedText, "Invalid alias result");

    }*/


    @Test
    public void simpleSubqueryTest(){

        String inputText = "SELECT * FROM ( SELECT demo.users.name AS n, demo.users.age FROM demo.users ) t WHERE n = 'name_1'";
        String expectedText = "SELECT "+Constants.VIRTUAL_CATALOG_NAME+".t.n AS n, "+Constants.VIRTUAL_CATALOG_NAME+".t.age FROM ( SELECT demo.users.name AS n, demo.users.age FROM demo.users ) AS t " +
                        "WHERE "+ Constants.VIRTUAL_CATALOG_NAME+".t.n = 'name_1'";

        ColumnName n1 = new ColumnName("demo", "users", "name");
        Selector selector1 = new ColumnSelector(n1);
        selector1.setAlias("n");
        Selector selector2 = new ColumnSelector(new ColumnName("demo", "users", "age"));
        List<Selector> selectorList = new ArrayList<>();
        selectorList.add(selector1);
        selectorList.add(selector2);
        SelectExpression selectExpression = new SelectExpression(selectorList);
        TableName tablename = new TableName("demo", "users");
        SelectStatement subquerySelectStatement = new SelectStatement(selectExpression, tablename);

        List<Selector> selectorListSuperquery = new ArrayList<>();
        selectorListSuperquery.add(new AsteriskSelector());
        TableName virtualTable = new TableName(Constants.VIRTUAL_CATALOG_NAME, "t");
        virtualTable.setAlias("t");
        SelectStatement selectStatement = new SelectStatement(new SelectExpression(selectorListSuperquery), virtualTable);
        selectStatement.setSubquery(subquerySelectStatement, "t");


        List<Relation> where = new ArrayList<>();
        Selector leftWh = new ColumnSelector(new ColumnName("", "", "n"));
        Selector rightWh = new StringSelector(new TableName("", ""), "name_1");
        Relation relationWh = new Relation(leftWh, Operator.EQ, rightWh);
        where.add(relationWh);
        selectStatement.setWhere(where);

        Validator validator = new Validator();
        BaseQuery baseQuery = new BaseQuery("SelectId", inputText, new CatalogName("demo"));
        IParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);
        IValidatedQuery validatedQuery = null;
        try {
            validatedQuery = validator.validate(parsedQuery);
        } catch (ValidationException | IgnoreQueryException e) {
            fail("Cannot validate valid statement", e);
        }

        assertNotNull(validatedQuery, "Expecting validated query");
        assertEquals(validatedQuery.toString(), expectedText, "Invalid resolution");

    }

    @Test
    public void simpleSubqueryWithAmbiguousNameTest(){

        String inputText = "SELECT * FROM ( SELECT demo.users.name , demo.users.age AS name FROM demo.users )'";

        ColumnName n1 = new ColumnName("demo", "users", "name");
        Selector selector1 = new ColumnSelector(n1);
        Selector selector2 = new ColumnSelector(new ColumnName("demo", "users", "age"));
        selector2.setAlias("name");
        List<Selector> selectorList = new ArrayList<>();
        selectorList.add(selector1);
        selectorList.add(selector2);
        SelectExpression selectExpression = new SelectExpression(selectorList);
        TableName tablename = new TableName("demo", "users");
        SelectStatement subquerySelectStatement = new SelectStatement(selectExpression, tablename);

        List<Selector> selectorListSuperquery = new ArrayList<>();
        selectorListSuperquery.add(new AsteriskSelector());
        TableName virtualTable = new TableName(Constants.VIRTUAL_CATALOG_NAME, "t");
        virtualTable.setAlias("t");
        SelectStatement selectStatement = new SelectStatement(new SelectExpression(selectorListSuperquery), virtualTable);
        selectStatement.setSubquery(subquerySelectStatement, "t");


        Validator validator = new Validator();
        BaseQuery baseQuery = new BaseQuery("SelectId", inputText, new CatalogName("demo"));
        IParsedQuery parsedQuery = new SelectParsedQuery(baseQuery, selectStatement);
        try {
            validator.validate(parsedQuery);
            fail("Duplicate names within the subquery should not be validated");
        } catch (ValidationException | IgnoreQueryException e) {
            Assert.assertTrue( e instanceof AmbiguousNameException);
        }

    }


}
