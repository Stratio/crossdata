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

import org.testng.Assert;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.exceptions.IgnoreQueryException;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.core.query.BaseQuery;
import com.stratio.crossdata.core.query.IParsedQuery;
import com.stratio.crossdata.core.query.MetadataParsedQuery;
import com.stratio.crossdata.core.statements.CreateIndexStatement;
import com.stratio.crossdata.core.validator.BasicValidatorTest;
import com.stratio.crossdata.core.validator.Validator;

public class CreateIndexStatementTest extends BasicValidatorTest {

    @Test
    public void createIndex() {
        String query = "CREATE INDEX bool_idx ON users (bool); ";

        CreateIndexStatement createIndexStatement = new CreateIndexStatement();
        createIndexStatement.setIndexType("DEFAULT");
        createIndexStatement.setName(new ColumnName("demo", "users", "bool"));
        createIndexStatement.addColumn(new ColumnName("demo", "users", "bool"));

        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("CreateTableId", query, new CatalogName("demo"),"sessionTest");

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, createIndexStatement);
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
    public void createIndexUnknownTable() {
        String query = "CREATE INDEX gender_idx ON unknown (gender); ";

        CreateIndexStatement createIndexStatement = new CreateIndexStatement();
        createIndexStatement.setIndexType("DEFAULT");
        createIndexStatement.addColumn(new ColumnName("demo", "unknown", "gender"));

        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("CreateTableId", query, new CatalogName("demo"),"sessionTest");

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, createIndexStatement);
        try {
            validator.validate(parsedQuery);
            Assert.fail("TABLE must exists");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void createIndexUnknownColumns() {
        String query = "CREATE INDEX gender_idx ON users (unknown); ";

        CreateIndexStatement createIndexStatement = new CreateIndexStatement();
        createIndexStatement.setIndexType("DEFAULT");
        createIndexStatement.addColumn(new ColumnName("demo", "users", "unknown"));

        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("CreateTableId", query, new CatalogName("demo"),"sessionTest");

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, createIndexStatement);
        try {
            validator.validate(parsedQuery);
            Assert.fail("COLUMN must exists");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }
    }

}
