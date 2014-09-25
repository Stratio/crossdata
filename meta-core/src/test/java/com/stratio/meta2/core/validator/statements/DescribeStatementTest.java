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

package com.stratio.meta2.core.validator.statements;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.stratio.meta.common.exceptions.IgnoreQueryException;
import com.stratio.meta.common.exceptions.ValidationException;
import com.stratio.meta.core.structures.DescribeType;
import com.stratio.meta2.common.data.CatalogName;
import com.stratio.meta2.core.query.BaseQuery;
import com.stratio.meta2.core.query.MetadataParsedQuery;
import com.stratio.meta2.core.query.ParsedQuery;
import com.stratio.meta2.core.statements.DescribeStatement;
import com.stratio.meta2.core.validator.BasicValidatorTest;
import com.stratio.meta2.core.validator.Validator;

public class DescribeStatementTest extends BasicValidatorTest {

    @Test
    public void describeCatalogIfNotExists() {
        String query = "DESCRIBE CATALOG demo;";
        DescribeStatement describeStatement = new DescribeStatement(DescribeType.CATALOG);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("describeid", query, new CatalogName("demo"));

        ParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, describeStatement);
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
    public void describeNotExistingCatalog() {
        String query = "DECRIBE CATALOG myCatalog;";
        DescribeStatement describeStatement = new DescribeStatement(DescribeType.CATALOG);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("describeid", query, new CatalogName("myCatalog"));

        ParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, describeStatement);
        try {
            validator.validate(parsedQuery);
            Assert.fail("Catalog must exist");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }

    }

    @Test
    public void describeTable() {
        String query = "Describe Table demo.users;";
        DescribeStatement describeStatement = new DescribeStatement(DescribeType.TABLE);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("describeid", query, new CatalogName("demo"));

        ParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, describeStatement);
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
    public void describeNotExistingTable() {
        String query = "Describe table myCatalog;";
        DescribeStatement describeStatement = new DescribeStatement(DescribeType.TABLE);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("describeid", query, new CatalogName("demo"));

        ParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, describeStatement);
        try {
            validator.validate(parsedQuery);
            Assert.fail("Table must exist");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }

    }

    @Test
    public void describeTables() {
        String query = "DESCRIBE TABLES;";
        DescribeStatement describeStatement = new DescribeStatement(DescribeType.TABLES);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("createCatalogid", query, new CatalogName("demo"));

        ParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, describeStatement);
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
    public void describeCatalogs() {
        String query = "DESCRIBE Catalogs;";
        DescribeStatement describeStatement = new DescribeStatement(DescribeType.CATALOGS);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("createCatalogid", query, new CatalogName("system"));

        ParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, describeStatement);
        try {
            validator.validate(parsedQuery);
            Assert.assertTrue(true);
        } catch (ValidationException e) {
            Assert.fail(e.getMessage());
        } catch (IgnoreQueryException e) {
            Assert.fail(e.getMessage());
        }

    }

}
