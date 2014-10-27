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

import com.stratio.crossdata.common.exceptions.IgnoreQueryException;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.core.query.BaseQuery;
import com.stratio.crossdata.core.query.IParsedQuery;
import com.stratio.crossdata.core.query.MetadataParsedQuery;
import com.stratio.crossdata.core.statements.AttachClusterStatement;
import com.stratio.crossdata.core.validator.BasicValidatorTest;
import com.stratio.crossdata.core.validator.Validator;

public class AttachClusterStatementTest extends BasicValidatorTest {

    @Test
    public void attachClusterNoOptions() {
        String query = "ATTACH CLUSTER cluster on DATASTORE Cassandra";

        AttachClusterStatement attachClusterStatement = new AttachClusterStatement(new ClusterName("cluster"), false,
                new DataStoreName("Cassandra"),
                "");
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("CreateTableId", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, attachClusterStatement);

        try {
            validator.validate(parsedQuery);
            Assert.fail("Options are required for ATTACH CLUSTER statement");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void attachClusterUnknownDatastore() {
        String query = "ATTACH CLUSTER cluster on DATASTORE unknown";

        AttachClusterStatement attachClusterStatement = new AttachClusterStatement(new ClusterName("cluster"), false,
                new DataStoreName("unknown"),
                "");
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("CreateTableId", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, attachClusterStatement);
        try {
            validator.validate(parsedQuery);
            Assert.fail("Datastore must exists before ATTACH CLUSTER statement");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void attachClusterWithOptions() {
        String query = "ATTACH CLUSTER cluster on DATASTORE Cassandra with options {'comment':'attach cluster'}";

        AttachClusterStatement attachClusterStatement = new AttachClusterStatement(new ClusterName("cluster"), false,
                new DataStoreName("Cassandra"),
                "{'comment':'attach cluster'}");
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("CreateTableId", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, attachClusterStatement);
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
    public void attachUnknownClusterWithOptionsIfexists() {
        String query = "ATTACH CLUSTER IF EXIST unknown on DATASTORE Cassandra with options {'comment':'attach " +
                "cluster'}";

        AttachClusterStatement attachClusterStatement = new AttachClusterStatement(new ClusterName("unknown"), true,
                new DataStoreName("Cassandra"),
                "{'comment':'attach cluster'}");
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("CreateTableId", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, attachClusterStatement);
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
