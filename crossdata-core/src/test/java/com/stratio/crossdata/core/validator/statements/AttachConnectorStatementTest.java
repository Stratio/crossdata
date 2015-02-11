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
import com.stratio.crossdata.common.data.ConnectorName;
import com.stratio.crossdata.common.exceptions.IgnoreQueryException;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.core.query.BaseQuery;
import com.stratio.crossdata.core.query.IParsedQuery;
import com.stratio.crossdata.core.query.MetadataParsedQuery;
import com.stratio.crossdata.core.statements.AttachConnectorStatement;
import com.stratio.crossdata.core.validator.BasicValidatorTest;
import com.stratio.crossdata.core.validator.Validator;

public class AttachConnectorStatementTest extends BasicValidatorTest {

    @Test
    public void attachExistingConnector() {
        String query = "ATTACH CONNECTOR CassandraConnector TO Cassandra WITH OPTIONS {'comment':'a comment'}";

        AttachConnectorStatement attachConnectorStatement = new AttachConnectorStatement(new ConnectorName
                ("CassandraConnector"),
                new ClusterName("Cassandra"), "{'comment':'a comment'}", 5, 0);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("attachConnectorID", query, new CatalogName("system"));

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, attachConnectorStatement);
        try {
            validator.validate(parsedQuery);
            Assert.fail("The CONNECTOR must not exist");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void attachConnectorUnknown() {
        String query = "ATTACH CONNECTOR unknown TO myCluster WITH OPTIONS {'comment':'a comment'}";

        AttachConnectorStatement attachConnectorStatement = new AttachConnectorStatement(new ConnectorName("unknown"),
                new ClusterName("myCluster"),
                "{'comment':'a comment'}", 5, 0);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("attachConnectorID", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, attachConnectorStatement);
        try {
            validator.validate(parsedQuery);
            Assert.fail("CONNECTOR must exists");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void attachConnectorUnknownCluster() {
        String query = "ATTACH CONNECTOR newConnector TO unknown WITH OPTIONS {'comment':'a comment'}";

        AttachConnectorStatement attachConnectorStatement = new AttachConnectorStatement(new ConnectorName
                ("CassandraConnector"),
                new ClusterName("unknown"), "{'comment':'a comment'}", 5, 0);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("attachConnectorID", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, attachConnectorStatement);
        try {
            validator.validate(parsedQuery);
            Assert.fail("CONNECTOR must exists");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void attachConnectorEmptyOptions() {
        String query = "ATTACH CONNECTOR CassandraConnector TO Cassandra WITH OPTIONS";

        AttachConnectorStatement attachConnectorStatement = new AttachConnectorStatement(new ConnectorName
                ("CassandraConnector"),
                new ClusterName("Cassandra"), "", 5, 0);
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("attachConnectorID", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, attachConnectorStatement);
        try {
            validator.validate(parsedQuery);
            Assert.fail("The options cannot be empty");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }
    }

}
