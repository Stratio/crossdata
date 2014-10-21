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
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.exceptions.IgnoreQueryException;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.statements.structures.selectors.BooleanSelector;
import com.stratio.crossdata.common.statements.structures.selectors.IntegerSelector;
import com.stratio.crossdata.common.statements.structures.selectors.Selector;
import com.stratio.crossdata.common.statements.structures.selectors.StringSelector;
import com.stratio.crossdata.core.query.BaseQuery;
import com.stratio.crossdata.core.query.ParsedQuery;
import com.stratio.crossdata.core.query.StorageParsedQuery;
import com.stratio.crossdata.core.statements.InsertIntoStatement;
import com.stratio.crossdata.core.statements.StorageStatement;
import com.stratio.crossdata.core.validator.BasicValidatorTest;
import com.stratio.crossdata.core.validator.Validator;

public class InsertIntoStatementTest extends BasicValidatorTest {

    @Test
    public void validateOk() {
        String query = "Insert into demo.users(name,gender,age,bool,phrase,email) values ('pepe','male',23,true,'this is the phrase','mail@mail.com';";
        List<ColumnName> columns = new ArrayList<>();
        List<Selector> values = new ArrayList<>();
        columns.add(new ColumnName(new TableName("demo", "users"), "name"));
        columns.add(new ColumnName(new TableName("demo", "users"), "gender"));
        columns.add(new ColumnName(new TableName("demo", "users"), "age"));
        columns.add(new ColumnName(new TableName("demo", "users"), "bool"));
        columns.add(new ColumnName(new TableName("demo", "users"), "phrase"));
        columns.add(new ColumnName(new TableName("demo", "users"), "email"));

        values.add(new StringSelector("'pepe'"));
        values.add(new StringSelector("'male'"));
        values.add(new IntegerSelector(23));
        values.add(new BooleanSelector(true));
        values.add(new StringSelector("'this is the phrase'"));
        values.add(new StringSelector("'mail@mail.com'"));

        StorageStatement insertIntoStatement = new InsertIntoStatement(new TableName("demo", "users"), columns, values,
                true);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("insertId", query, new CatalogName("system"));

        ParsedQuery parsedQuery = new StorageParsedQuery(baseQuery, insertIntoStatement);
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
        columns.add(new ColumnName(new TableName("demo", "unknown"), "name"));
        columns.add(new ColumnName(new TableName("demo", "unknown"), "gender"));
        columns.add(new ColumnName(new TableName("demo", "unknown"), "age"));
        columns.add(new ColumnName(new TableName("demo", "unknown"), "bool"));
        columns.add(new ColumnName(new TableName("demo", "unknown"), "phrase"));
        columns.add(new ColumnName(new TableName("demo", "unknown"), "email"));

        values.add(new StringSelector("'pepe'"));
        values.add(new StringSelector("'male'"));
        values.add(new IntegerSelector(23));
        values.add(new BooleanSelector(true));
        values.add(new StringSelector("'this is the phrase'"));
        values.add(new StringSelector("'mail@mail.com'"));

        StorageStatement insertIntoStatement = new InsertIntoStatement(new TableName("demo", "unknown"), columns,
                values, true);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("insertId", query, new CatalogName("system"));

        ParsedQuery parsedQuery = new StorageParsedQuery(baseQuery, insertIntoStatement);
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
        columns.add(new ColumnName(new TableName("unknown", "users"), "name"));
        columns.add(new ColumnName(new TableName("unknown", "users"), "gender"));
        columns.add(new ColumnName(new TableName("unknown", "users"), "age"));
        columns.add(new ColumnName(new TableName("unknown", "users"), "bool"));
        columns.add(new ColumnName(new TableName("unknown", "users"), "phrase"));
        columns.add(new ColumnName(new TableName("unknown", "users"), "email"));

        values.add(new StringSelector("'pepe'"));
        values.add(new StringSelector("'male'"));
        values.add(new IntegerSelector(23));
        values.add(new BooleanSelector(true));
        values.add(new StringSelector("'this is the phrase'"));
        values.add(new StringSelector("'mail@mail.com'"));

        StorageStatement insertIntoStatement =
                new InsertIntoStatement(new TableName("unknown", "users"), columns, values, true);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("insertId", query, new CatalogName("system"));

        ParsedQuery parsedQuery = new StorageParsedQuery(baseQuery, insertIntoStatement);
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
        columns.add(new ColumnName(new TableName("demo", "users"), "name"));
        columns.add(new ColumnName(new TableName("demo", "users"), "gender"));
        columns.add(new ColumnName(new TableName("demo", "users"), "age"));
        columns.add(new ColumnName(new TableName("demo", "users"), "bool"));
        columns.add(new ColumnName(new TableName("demo", "users"), "phrase"));
        columns.add(new ColumnName(new TableName("demo", "users"), "email"));

        //ERROR TYPE
        values.add(new IntegerSelector(15));
        values.add(new StringSelector("'male'"));
        values.add(new IntegerSelector(23));
        values.add(new BooleanSelector(true));
        values.add(new StringSelector("'this is the phrase'"));
        values.add(new StringSelector("'mail@mail.com'"));

        StorageStatement insertIntoStatement = new InsertIntoStatement(new TableName("demo", "users"), columns, values,
                true);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("insertId", query, new CatalogName("system"));

        ParsedQuery parsedQuery = new StorageParsedQuery(baseQuery, insertIntoStatement);
        try {
            validator.validate(parsedQuery);
            Assert.fail("There is an error in the types");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.fail(e.getMessage());
        }
    }


}
