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

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import com.stratio.crossdata.common.exceptions.validation.NotValidColumnException;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ColumnName;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.IgnoreQueryException;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.common.metadata.ColumnType;
import com.stratio.crossdata.common.metadata.DataType;
import com.stratio.crossdata.core.query.BaseQuery;
import com.stratio.crossdata.core.query.IParsedQuery;
import com.stratio.crossdata.core.query.MetadataParsedQuery;
import com.stratio.crossdata.core.statements.CreateTableStatement;
import com.stratio.crossdata.core.validator.BasicValidatorTest;
import com.stratio.crossdata.core.validator.Validator;

public class CreateTableStatementTest extends BasicValidatorTest {

    @Test
    public void createTable() {
        String query = "CREATE TABLE demo.users2 ( name varchar, gender varchar, age int, PRIMARY KEY (name)) ";
        boolean isExternal = false;
        LinkedHashSet<ColumnName> primaryKey = new LinkedHashSet<>();
        ColumnName partitionColumn1 = new ColumnName("demo", "users2", "name");
        primaryKey.add(partitionColumn1);

        LinkedHashMap<ColumnName, ColumnType> columns = createColumnsMap();

        LinkedHashSet<ColumnName> clusterKey = new LinkedHashSet<>();
        CreateTableStatement createTableStatement = new CreateTableStatement(new TableName("demo", "users2"),
                new ClusterName("cluster"),
                columns,
                primaryKey,
                clusterKey,isExternal);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("CreateTableId", query, new CatalogName("demo"),"sessionTest");

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, createTableStatement);
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
    public void createTableWithOptions() {
        String query = "CREATE TABLE demo.users2 ( name varchar, gender varchar, age int, PRIMARY KEY (name)) WITH comment='Users2 table'";
        LinkedHashSet<ColumnName> primaryKey = new LinkedHashSet<>();
        ColumnName partitionColumn1 = new ColumnName("demo", "users2", "name");
        primaryKey.add(partitionColumn1);
        boolean isExternal = false;

        LinkedHashMap<ColumnName, ColumnType> columns = createColumnsMap();

        LinkedHashSet<ColumnName> clusterKey = new LinkedHashSet<>();
        CreateTableStatement createTableStatement = new CreateTableStatement(new TableName("demo", "users2"),
                new ClusterName("cluster"),
                columns,
                primaryKey,
                clusterKey, isExternal);

        createTableStatement.setProperties("{'comment': 'Users2 table'}");
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("CreateTableId", query, new CatalogName("demo"),"sessionTest");

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, createTableStatement);
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
    public void createTableUnknownCatalog() {
        String query = "CREATE TABLE unknown.users2 ( name varchar, gender varchar, age int, PRIMARY KEY (name))";
        LinkedHashSet<ColumnName> primaryKey = new LinkedHashSet<>();
        ColumnName partitionColumn1 = new ColumnName("unknown", "users2", "name");
        primaryKey.add(partitionColumn1);
        boolean isExternal = false;
        TableName tableName = new TableName("unknown", "users2");

        LinkedHashMap<ColumnName, ColumnType> columns = new LinkedHashMap<>();
        columns.put(new ColumnName(tableName, "name"), new ColumnType(DataType.TEXT));
        columns.put(new ColumnName(tableName, "gender"), new ColumnType(DataType.TEXT));
        columns.put(new ColumnName(tableName, "age"), new ColumnType(DataType.INT));

        CreateTableStatement createTableStatement = new CreateTableStatement(new TableName("unknown", "users2"),
                new ClusterName("cluster"), columns, primaryKey, null,isExternal);

        createTableStatement.setProperties("comment");
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("CreateTableId", query, new CatalogName("unknown"),"sessionTest");

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, createTableStatement);
        try {
            validator.validate(parsedQuery);
            Assert.fail("CATALOG must exists");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void createDuplicateTable() {
        String query = "CREATE TABLE demo.users ( name varchar, gender varchar, age int, PRIMARY KEY (name)) ";
        boolean isExternal = false;
        LinkedHashSet<ColumnName> primaryKey = new LinkedHashSet<>();
        ColumnName partitionColumn1 = new ColumnName("demo", "users", "name");
        primaryKey.add(partitionColumn1);

        LinkedHashMap<ColumnName, ColumnType> columns = new LinkedHashMap<>();
        columns.put(new ColumnName(new TableName("demo", "users"), "name"), new ColumnType(DataType.TEXT));
        columns.put(new ColumnName(new TableName("demo", "users"), "gender"), new ColumnType(DataType.TEXT));
        columns.put(new ColumnName(new TableName("demo", "users"), "age"), new ColumnType(DataType.INT));

        CreateTableStatement createTableStatement = new CreateTableStatement(new TableName("demo", "users"),
                new ClusterName("cluster"), columns, primaryKey, null, isExternal);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("CreateTableId", query, new CatalogName("demo"),"sessionTest");

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, createTableStatement);
        try {
            validator.validate(parsedQuery);
            Assert.fail("The new table must not exists");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }
    }


    @Test(expectedExceptions = NotValidColumnException.class)
    public void badPrimaryKeyName() throws ValidationException, IgnoreQueryException {
        String query = "CREATE TABLE demo.users2 ( name varchar, gender varchar, age int, PRIMARY KEY (NoExist)) ";
        boolean isExternal = false;
        LinkedHashSet<ColumnName> primaryKey = new LinkedHashSet<>();
        primaryKey.add(new ColumnName("demo", "users2", "NoExist"));

        LinkedHashMap<ColumnName, ColumnType> columns = createColumnsMap();

        LinkedHashSet<ColumnName> clusterKey = new LinkedHashSet<>();
        CreateTableStatement createTableStatement = new CreateTableStatement(new TableName("demo", "users2"),
                new ClusterName("cluster"),
                columns,
                primaryKey,
                clusterKey,isExternal);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("CreateTableId", query, new CatalogName("demo"),"sessionTest");

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, createTableStatement);

        validator.validate(parsedQuery);
    }

    @Test(expectedExceptions = NotValidColumnException.class)
    public void badMultiplePrimaryKeyName() throws ValidationException, IgnoreQueryException {
        String query = "CREATE TABLE demo.users2 ( name varchar, gender varchar, age int, PRIMARY KEY (NoExist)) ";
        boolean isExternal = false;
        LinkedHashSet<ColumnName> primaryKey = new LinkedHashSet<>();

        primaryKey.add( new ColumnName("demo", "users2", "name"));
        primaryKey.add( new ColumnName("demo", "users2", "NoExist2"));

        LinkedHashMap<ColumnName, ColumnType> columns = createColumnsMap();

        LinkedHashSet<ColumnName> clusterKey = new LinkedHashSet<>();
        CreateTableStatement createTableStatement = new CreateTableStatement(new TableName("demo", "users2"),
                new ClusterName("cluster"),
                columns,
                primaryKey,
                clusterKey,isExternal);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("CreateTableId", query, new CatalogName("demo"),"sessionTest");

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, createTableStatement);

        validator.validate(parsedQuery);
    }

    private LinkedHashMap<ColumnName, ColumnType> createColumnsMap() {
        LinkedHashMap<ColumnName, ColumnType> columns = new LinkedHashMap<>();
        columns.put(new ColumnName(new TableName("demo", "users2"), "name"), new ColumnType(DataType.TEXT));
        columns.put(new ColumnName(new TableName("demo", "users2"), "gender"), new ColumnType(DataType.TEXT));
        columns.put(new ColumnName(new TableName("demo", "users2"), "age"), new ColumnType(DataType.INT));
        return columns;
    }
}
