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

import static org.testng.Assert.assertNotNull;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.exceptions.IgnoreQueryException;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.core.query.BaseQuery;
import com.stratio.crossdata.core.query.IParsedQuery;
import com.stratio.crossdata.core.query.IValidatedQuery;
import com.stratio.crossdata.core.query.MetadataParsedQuery;
import com.stratio.crossdata.core.statements.AlterCatalogStatement;
import com.stratio.crossdata.core.validator.BasicValidatorTest;
import com.stratio.crossdata.core.validator.Validator;

public class AlterCatalogStatementTest extends BasicValidatorTest {

    @Test
    public void alterCatalogWithoutOptions() {
        String query = "ALTER CATALOG demo WITH {};";
        AlterCatalogStatement alterCatalogStatement = new AlterCatalogStatement(new CatalogName("demo"), "");
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("alterCatalogid", query, new CatalogName("demo"),"sessionTest");

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, alterCatalogStatement);
        try {
            IValidatedQuery validatedQuery = validator.validate(parsedQuery);
            assertNotNull(validatedQuery, "Validation of Alter Catalog with empty options should have succeed.");
        } catch (ValidationException | IgnoreQueryException e) {
            Assert.fail("ALTER CATALOG should accept empty options.");
        }
    }

    @Test
    public void alterCatalogNotFound() {
        String query = "ALTER CATALOG unknown WITH {};";
        AlterCatalogStatement alterCatalogStatement = new AlterCatalogStatement(new CatalogName("unknown"), "");
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("alterCatalogid", query, new CatalogName("unknown"),"sessionTest");

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, alterCatalogStatement);
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
    public void alterCatalogValid() {
        String query = "ALTER CATALOG key_space1 WITH {\"comment\":\"This is a comment\"};";
        AlterCatalogStatement alterCatalogStatement = new AlterCatalogStatement(new CatalogName("demo"),
                "{\"comment\":\"This is a comment\"}");
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("alterCatalogid", query, new CatalogName("demo"),"sessionTest");

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, alterCatalogStatement);
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
