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

import com.stratio.crossdata.common.data.AlterOperation;
import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.IgnoreQueryException;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.DataType;
import com.stratio.crossdata.core.query.BaseQuery;
import com.stratio.crossdata.core.query.IParsedQuery;
import com.stratio.crossdata.core.query.MetadataParsedQuery;
import com.stratio.crossdata.core.statements.AlterTableStatement;
import com.stratio.crossdata.core.validator.BasicValidatorTest;
import com.stratio.crossdata.core.validator.Validator;

public class AlterTableStatementTest extends BasicValidatorTest {

    @Test
    public void alterTableAlterColumns() {
        String query = "ALTER TABLE demo.users ALTER demo.users.age TYPE BIGINT;";

        AlterTableStatement alterTableStatement = new AlterTableStatement(new TableName("demo", "users"),
                new ColumnName("demo", "users", "age"),
                new ColumnType(DataType.BIGINT), null, AlterOperation.ALTER_COLUMN);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("alterTableId", query, new CatalogName("demo"),"sessionTest");

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, alterTableStatement);
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
    public void alterTableAddColumns() {
        String query = "ALTER TABLE demo.users ADD demo.users.new TYPE TEXT ;";

        AlterTableStatement alterTableStatement = new AlterTableStatement(new TableName("demo", "users"),
                new ColumnName("demo", "users", "new"),
                new ColumnType(DataType.VARCHAR), null, AlterOperation.ADD_COLUMN);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("alterTableId", query, new CatalogName("demo"),"sessionTest");

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, alterTableStatement);
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
    public void alterTableWithOptions() {
        String query = "ALTER TABLE demo.users WITH comment='Users table to maintain users';";
        AlterTableStatement alterTableStatement = new AlterTableStatement(new TableName("demo", "users"), null, null,
                "{'comment': 'Users table to maintain users'}", AlterOperation.ALTER_OPTIONS);

        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("alterTableId", query, new CatalogName("demo"),"sessionTest");

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, alterTableStatement);
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
    public void alterTableDropColumns() {
        String query = "ALTER TABLE demo.users DROP demo.users.age;";

        AlterTableStatement alterTableStatement = new AlterTableStatement(new TableName("demo", "users"),
                new ColumnName("demo", "users", "age"),
                new ColumnType(DataType.BIGINT), null, AlterOperation.DROP_COLUMN);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("alterTableId", query, new CatalogName("demo"),"sessionTest");

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, alterTableStatement);
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
    public void alterUnknownTableColumns() {
        String query = "ALTER TABLE unknown DROP demo.users.age;";

        AlterTableStatement alterTableStatement = new AlterTableStatement(new TableName("demo", "unknown"),
                new ColumnName("demo", "unknown", "age"),
                new ColumnType(DataType.BIGINT), null, AlterOperation.DROP_COLUMN);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("alterTableId", query, new CatalogName("demo"),"sessionTest");

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, alterTableStatement);
        try {
            validator.validate(parsedQuery);
            Assert.fail("TABLE specified must not exists");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void alterTableUnknownColumns() {
        String query = "ALTER TABLE demo.users DROP unknown;";

        AlterTableStatement alterTableStatement = new AlterTableStatement(new TableName("demo", "users"),
                new ColumnName("demo", "users", "unknown"),
                new ColumnType(DataType.BIGINT), null, AlterOperation.DROP_COLUMN);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("alterTableId", query, new CatalogName("demo"),"sessionTest");

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, alterTableStatement);
        try {
            validator.validate(parsedQuery);
            Assert.fail("Columns specified must not exists and then fail the test");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }
    }

}
