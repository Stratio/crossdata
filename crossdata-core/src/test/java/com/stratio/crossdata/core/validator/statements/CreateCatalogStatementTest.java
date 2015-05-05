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
import com.stratio.crossdata.common.exceptions.IgnoreQueryException;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.core.query.BaseQuery;
import com.stratio.crossdata.core.query.IParsedQuery;
import com.stratio.crossdata.core.query.MetadataParsedQuery;
import com.stratio.crossdata.core.statements.CreateCatalogStatement;
import com.stratio.crossdata.core.validator.BasicValidatorTest;
import com.stratio.crossdata.core.validator.Validator;

public class CreateCatalogStatementTest extends BasicValidatorTest {

    @Test
    public void createCatalogIfNotExists() {
        String query = "CREATE CATALOG IF NOT EXISTS new_catalog;";
        CreateCatalogStatement alterCatalogStatement = new CreateCatalogStatement(new CatalogName("demo2"), true, "");
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("createCatalogid", query, new CatalogName("demo2"),"sessionTest");

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, alterCatalogStatement);
        try {
            validator.validate(parsedQuery);
            Assert.assertTrue(true);
        } catch (ValidationException e) {
            Assert.fail(e.getMessage());
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }

    }

    @Test
    public void createCatalogIfNotExistsWithExistingCatalog() {
        String query = "CREATE CATALOG IF NOT EXISTS demo;";
        CreateCatalogStatement createCatalogStatement = new CreateCatalogStatement(new CatalogName("demo"), true, "");
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("createCatalogid", query, new CatalogName("demo"),"sessionTest");

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, createCatalogStatement);
        try {
            validator.validate(parsedQuery);
            Assert.assertTrue(true);
        } catch (ValidationException e) {
            Assert.fail(e.getMessage());
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }

    }

    @Test
    public void createCatalogWithExistingCatalog() {
        String query = "CREATE CATALOG demo;";
        CreateCatalogStatement alterCatalogStatement = new CreateCatalogStatement(new CatalogName("demo"), false, "");
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("createCatalogid", query, new CatalogName("demo"),"sessionTest");

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, alterCatalogStatement);
        try {
            validator.validate(parsedQuery);
            Assert.fail("The catalog exists yet");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }

    }

    @Test
    public void createCatalogWithOptions() {
        String query = "CREATE CATALOG new_catalog WITH {\"comment\":\"This is a comment\"};";
        CreateCatalogStatement alterCatalogStatement = new CreateCatalogStatement(new CatalogName("new_catalog"),
                false, "");
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("createCatalogid", query, new CatalogName("demo"),"sessionTest");

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, alterCatalogStatement);
        try {
            validator.validate(parsedQuery);
            Assert.assertTrue(true);
        } catch (ValidationException e) {
            Assert.fail(e.getMessage());
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }

    }

}
