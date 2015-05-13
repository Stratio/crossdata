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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.stratio.crossdata.common.statements.structures.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.IgnoreQueryException;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.core.query.BaseQuery;
import com.stratio.crossdata.core.query.IParsedQuery;
import com.stratio.crossdata.core.query.StorageParsedQuery;
import com.stratio.crossdata.core.statements.InsertIntoStatement;
import com.stratio.crossdata.core.statements.SelectStatement;
import com.stratio.crossdata.core.statements.StorageStatement;
import com.stratio.crossdata.core.validator.BasicValidatorTest;
import com.stratio.crossdata.core.validator.Validator;

public class InsertIntoStatementTest extends BasicValidatorTest {

    @Test
    public void validateOk() {
        String query = "Insert into demo.users(name,gender,age,bool,phrase,email) values ('pepe','male',23,true,'this is the phrase','mail@mail.com';";
        List<ColumnName> columns = new ArrayList<>();
        List<Selector> values = new ArrayList<>();

        TableName tableName = new TableName("demo", "users");

        columns.add(new ColumnName(tableName, "name"));
        columns.add(new ColumnName(tableName, "gender"));
        columns.add(new ColumnName(tableName, "age"));
        columns.add(new ColumnName(tableName, "bool"));
        columns.add(new ColumnName(tableName, "phrase"));
        columns.add(new ColumnName(tableName, "email"));

        values.add(new StringSelector(tableName, "'pepe'"));
        values.add(new StringSelector(tableName, "'male'"));
        values.add(new IntegerSelector(tableName, 23));
        values.add(new BooleanSelector(tableName, true));
        values.add(new StringSelector(tableName, "'this is the phrase'"));
        values.add(new StringSelector(tableName, "'mail@mail.com'"));

        StorageStatement insertIntoStatement = new InsertIntoStatement(new TableName("demo", "users"), columns,
                null, values, false, null, null, InsertIntoStatement.TYPE_VALUES_CLAUSE);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("insertId", query, new CatalogName("system"),"sessionTest");

        IParsedQuery parsedQuery = new StorageParsedQuery(baseQuery, insertIntoStatement);
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
    public void validateTypesOK() {
        String query = "Insert into test.table2(name,surname,rating,member) values (substring(name),'surname',23.5,true;";
        List<ColumnName> columns = new ArrayList<>();
        List<Selector> values = new ArrayList<>();

        TableName tableName = new TableName("test", "table2");

        columns.add(new ColumnName(tableName, "name"));
        columns.add(new ColumnName(tableName, "surname"));
        columns.add(new ColumnName(tableName, "rating"));
        columns.add(new ColumnName(tableName, "member"));

        values.add(new FunctionSelector(tableName, "substring", Arrays.asList( (Selector) new ColumnSelector(new ColumnName(tableName,"name")))));
        values.add(new StringSelector(tableName,"surname"));
        values.add(new FloatingPointSelector(tableName, 23.5));
        values.add(new BooleanSelector(tableName, true));

        StorageStatement insertIntoStatement = new InsertIntoStatement(new TableName("test", "table2"), columns,
                null, values, false, null, null, InsertIntoStatement.TYPE_VALUES_CLAUSE);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("insertId", query, new CatalogName("system"),"sessionTest");

        IParsedQuery parsedQuery = new StorageParsedQuery(baseQuery, insertIntoStatement);
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
    public void validateIfNotExists() {
        String query = "Insert into demo.unknown(name,gender,age,bool,phrase,email) values ('pepe','male',23,true,'this is the phrase','mail@mail.com';";
        List<ColumnName> columns = new ArrayList<>();
        List<Selector> values = new ArrayList<>();

        TableName tableName = new TableName("demo", "unknown");

        columns.add(new ColumnName(tableName, "name"));
        columns.add(new ColumnName(tableName, "gender"));
        columns.add(new ColumnName(tableName, "age"));
        columns.add(new ColumnName(tableName, "bool"));
        columns.add(new ColumnName(tableName, "phrase"));
        columns.add(new ColumnName(tableName, "email"));

        values.add(new StringSelector(tableName, "'pepe'"));
        values.add(new StringSelector(tableName, "'male'"));
        values.add(new IntegerSelector(tableName, 23));
        values.add(new BooleanSelector(tableName, true));
        values.add(new StringSelector(tableName, "'this is the phrase'"));
        values.add(new StringSelector(tableName, "'mail@mail.com'"));

        StorageStatement insertIntoStatement = new InsertIntoStatement(new TableName("demo", "unknown"), columns,
                null, values, false, null, null, InsertIntoStatement.TYPE_VALUES_CLAUSE);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("insertId", query, new CatalogName("system"),"sessionTest");

        IParsedQuery parsedQuery = new StorageParsedQuery(baseQuery, insertIntoStatement);
        try {
            validator.validate(parsedQuery);
            Assert.fail("TABLE must exist");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void validateIfNotExistsCatalog() {
        String query =
                "Insert into unknown.users(name,gender,age,bool,phrase,email) values ('pepe','male',23,true,'this is the phrase','mail@mail.com';";
        List<ColumnName> columns = new ArrayList<>();
        List<Selector> values = new ArrayList<>();

        TableName tableName = new TableName("demo", "unknown");

        columns.add(new ColumnName(tableName, "name"));
        columns.add(new ColumnName(tableName, "gender"));
        columns.add(new ColumnName(tableName, "age"));
        columns.add(new ColumnName(tableName, "bool"));
        columns.add(new ColumnName(tableName, "phrase"));
        columns.add(new ColumnName(tableName, "email"));

        values.add(new StringSelector(tableName, "'pepe'"));
        values.add(new StringSelector(tableName, "'male'"));
        values.add(new IntegerSelector(tableName, 23));
        values.add(new BooleanSelector(tableName, true));
        values.add(new StringSelector(tableName, "'this is the phrase'"));
        values.add(new StringSelector(tableName, "'mail@mail.com'"));

        StorageStatement insertIntoStatement =
                new InsertIntoStatement(new TableName("unknown", "users"), columns, null,
                        values, true, null, null, InsertIntoStatement.TYPE_VALUES_CLAUSE);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("insertId", query, new CatalogName("system"),"sessionTest");

        IParsedQuery parsedQuery = new StorageParsedQuery(baseQuery, insertIntoStatement);
        try {
            validator.validate(parsedQuery);
            Assert.fail("CATALOG must exist");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void validateErrorTypes() {
        String query = "Insert into demo.users(name,gender,age,bool,phrase,email) values ('pepe','male',23,true,'this is the phrase','mail@mail.com';";
        List<ColumnName> columns = new ArrayList<>();
        List<Selector> values = new ArrayList<>();

        TableName tableName = new TableName("demo", "users");

        columns.add(new ColumnName(tableName, "name"));
        columns.add(new ColumnName(tableName, "gender"));
        columns.add(new ColumnName(tableName, "age"));
        columns.add(new ColumnName(tableName, "bool"));
        columns.add(new ColumnName(tableName, "phrase"));
        columns.add(new ColumnName(tableName, "email"));

        //ERROR TYPE
        values.add(new IntegerSelector(tableName, 15));
        values.add(new StringSelector(tableName, "'male'"));
        values.add(new IntegerSelector(tableName, 23));
        values.add(new BooleanSelector(tableName, true));
        values.add(new StringSelector(tableName, "'this is the phrase'"));
        values.add(new StringSelector(tableName, "'mail@mail.com'"));

        StorageStatement insertIntoStatement = new InsertIntoStatement(new TableName("demo", "users"), columns,
                null, values, true, null, null, InsertIntoStatement.TYPE_VALUES_CLAUSE);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("insertId", query, new CatalogName("system"),"sessionTest");

        IParsedQuery parsedQuery = new StorageParsedQuery(baseQuery, insertIntoStatement);
        try {
            validator.validate(parsedQuery);
            Assert.fail("There is an error in the types");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void insertIntoFromSelect() {
        String query = "INSERT INTO demo.users(name, age, bool) SELECT phrase, age, bool FROM customers;";

        // CREATE INSERT PART
        List<ColumnName> columns = new ArrayList<>();
        TableName insertTable = new TableName("demo", "users");
        columns.add(new ColumnName(insertTable, "name"));
        columns.add(new ColumnName(insertTable, "age"));
        columns.add(new ColumnName(insertTable, "bool"));

        // CREATE SELECT STATEMENT
        TableName selectTable = new TableName(null, "customers");
        List<Selector> selectorList = new ArrayList<>();
        selectorList.add(new ColumnSelector(new ColumnName(selectTable, "phrase")));
        selectorList.add(new ColumnSelector(new ColumnName(selectTable, "age")));
        selectorList.add(new ColumnSelector(new ColumnName(selectTable, "bool")));
        SelectExpression selectExpression = new SelectExpression(selectorList);
        SelectStatement ss = new SelectStatement(selectExpression, selectTable);

        StorageStatement insertIntoStatement = new InsertIntoStatement(
                insertTable,
                columns,
                ss,
                null,
                false,
                null,
                null,
                InsertIntoStatement.TYPE_SELECT_CLAUSE);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("insertIntoFromSelect", query, new CatalogName("sales"),"sessionTest");

        IParsedQuery parsedQuery = new StorageParsedQuery(baseQuery, insertIntoStatement);
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
    public void insertIntoFromSelectWrongTypes() {
        String query = "INSERT INTO demo.users(name, age, bool) SELECT email, name, bool FROM customers;";

        // CREATE INSERT PART
        List<ColumnName> columns = new ArrayList<>();
        TableName insertTable = new TableName("demo", "users");
        columns.add(new ColumnName(insertTable, "name"));
        columns.add(new ColumnName(insertTable, "age"));
        columns.add(new ColumnName(insertTable, "bool"));

        // CREATE SELECT STATEMENT
        TableName selectTable = new TableName(null, "customers");
        List<Selector> selectorList = new ArrayList<>();
        selectorList.add(new ColumnSelector(new ColumnName(selectTable, "email")));
        selectorList.add(new ColumnSelector(new ColumnName(selectTable, "name")));
        selectorList.add(new ColumnSelector(new ColumnName(selectTable, "bool")));
        SelectExpression selectExpression = new SelectExpression(selectorList);
        SelectStatement ss = new SelectStatement(selectExpression, selectTable);

        StorageStatement insertIntoStatement = new InsertIntoStatement(
                insertTable,
                columns,
                ss,
                null,
                false,
                null,
                null,
                InsertIntoStatement.TYPE_SELECT_CLAUSE);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("insertIntoFromSelectWrongTypes", query, new CatalogName("sales"),"sessionTest");

        IParsedQuery parsedQuery = new StorageParsedQuery(baseQuery, insertIntoStatement);
        try {
            validator.validate(parsedQuery);
            Assert.fail("insertIntoFromSelectWrongTypes: Validation should have failed");
        } catch (ValidationException e) {
            Assert.assertTrue(true, "ValidationException expected");
        } catch (IgnoreQueryException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void insertIntoFromSelectWrongType() {
        String query = "Insert into test.table2(name,surname,rating,member) values (substring(name),'surname',23.5, fake = fake;";
        List<ColumnName> columns = new ArrayList<>();
        List<Selector> values = new ArrayList<>();

        TableName tableName = new TableName("test", "table2");

        columns.add(new ColumnName(tableName, "name"));
        columns.add(new ColumnName(tableName, "surname"));
        columns.add(new ColumnName(tableName, "rating"));
        columns.add(new ColumnName(tableName, "member"));

        values.add(new FunctionSelector(tableName, "substring", Arrays.asList( (Selector) new ColumnSelector(new ColumnName(tableName,"name")))));
        values.add(new StringSelector(tableName,"surname"));
        values.add(new FloatingPointSelector(tableName, 23.5));
        values.add(new RelationSelector(tableName, new Relation(new StringSelector("fake"), Operator.EQ, new StringSelector("fake"))));

        StorageStatement insertIntoStatement = new InsertIntoStatement(new TableName("test", "table2"), columns,
                null, values, false, null, null, InsertIntoStatement.TYPE_VALUES_CLAUSE);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("insertId", query, new CatalogName("system"),"sessionTest");

        IParsedQuery parsedQuery = new StorageParsedQuery(baseQuery, insertIntoStatement);
        try {
            validator.validate(parsedQuery);
            Assert.fail("insertIntoFromSelectWrongTypes: Validation should have failed");
        } catch (ValidationException e) {
            Assert.assertTrue(true, "ValidationException expected");
        } catch (IgnoreQueryException e) {
            Assert.fail(e.getMessage());
        }
    }






}
