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
import com.stratio.crossdata.common.data.ConnectorName;
import com.stratio.crossdata.core.query.BaseQuery;
import com.stratio.crossdata.core.query.MetadataParsedQuery;
import com.stratio.crossdata.core.query.ParsedQuery;
import com.stratio.crossdata.core.statements.DetachConnectorStatement;
import com.stratio.crossdata.core.validator.BasicValidatorTest;
import com.stratio.crossdata.core.validator.Validator;

public class DetachConnectorStatementTest extends BasicValidatorTest{

    @Test
    public void detachConnector() {
        String query = "DETACH CONNECTOR CassandraConnector FROM cluster";
        DetachConnectorStatement detachConnectorStatement = new DetachConnectorStatement(
                new ConnectorName("CassandraConnector"), new ClusterName("cluster"));
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("detachConnectorId", query, new CatalogName("system"));

        ParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, detachConnectorStatement);
        try {
            validator.validate(parsedQuery);
            Assert.assertFalse(false);
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void detachUnknownConnector() {
        String query = "DETACH CONNECTOR Unknown FROM cluster";
        DetachConnectorStatement detachConnectorStatement = new DetachConnectorStatement(new ConnectorName("Unknown"),
                new ClusterName("cluster"));
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("detachConnectorId", query, new CatalogName("system"));

        ParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, detachConnectorStatement);
        try {
            validator.validate(parsedQuery);
            Assert.fail("CONNECTOR must exist");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void detachConnectorUnknownCluster() {
        String query = "DETACH CONNECTOR CassandraConnector FROM Unknown";

        DetachConnectorStatement detachConnectorStatement = new DetachConnectorStatement(
                new ConnectorName("CassandraConnector"), new ClusterName("Unknown"));
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("detachConnectorId", query, new CatalogName("system"));

        ParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, detachConnectorStatement);
        try {
            validator.validate(parsedQuery);
            Assert.fail("Datastore must exist");
        } catch (ValidationException e) {
            Assert.assertTrue(true);
        } catch (IgnoreQueryException e) {
            Assert.assertTrue(true);
        }
    }
}
