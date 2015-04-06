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
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.exceptions.IgnoreQueryException;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.core.query.BaseQuery;
import com.stratio.crossdata.core.query.IParsedQuery;
import com.stratio.crossdata.core.query.MetadataParsedQuery;
import com.stratio.crossdata.core.statements.AlterClusterStatement;
import com.stratio.crossdata.core.validator.BasicValidatorTest;
import com.stratio.crossdata.core.validator.Validator;

public class AlterClusterStatementTest extends BasicValidatorTest {
    @Test
    public void alterCluster() {
        String query = "ALTER CLUSTER IF EXISTS cluster WITH OPTIONS {'comment':'my coments'}";

        AlterClusterStatement alterClusterStatement = new AlterClusterStatement(
                new ClusterName("cluster"),
                true,
                "{'comment':'my coments'}");
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("alterClusterId", query, new CatalogName("system"),"sessionTest");

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, alterClusterStatement);
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
    public void alterIfExistsClusterNotExists() {
        String query = "ALTER CLUSTER IF EXISTS unknown WITH OPTIONS {'comment':'my coments'}";

        AlterClusterStatement alterClusterStatement = new AlterClusterStatement(
                new ClusterName("unknown"),
                true,
                "{'comment':'my coments'}");

        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("alterClusterId", query, new CatalogName("system"),"sessionTest");

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, alterClusterStatement);
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
    public void alterClusterEmptyOptions() {
        String query = "ALTER CLUSTER cluster WITH OPTIONS";

        AlterClusterStatement alterClusterStatement = new AlterClusterStatement(
                new ClusterName("cluster"),
                false,
                "");
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("alterClusterId", query, new CatalogName("system"),"sessionTest");

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, alterClusterStatement);
        try {
            validator.validate(parsedQuery);
            Assert.fail("CLUSTER options must exists");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void alterNotExistCluster() {
        String query = "ALTER CLUSTER unknown";

        AlterClusterStatement alterClusterStatement = new AlterClusterStatement(
                new ClusterName("unknown"),
                false,
                "{}");
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("alterClusterId", query, new CatalogName("system"),"sessionTest");

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, alterClusterStatement);
        try {
            validator.validate(parsedQuery);
            Assert.fail("CLUSTER must exists");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }
    }

}
