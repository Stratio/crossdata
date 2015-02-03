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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.HashSet;

import org.testng.annotations.Test;

import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.exceptions.IgnoreQueryException;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.common.manifest.PropertyType;
import com.stratio.crossdata.common.metadata.DataStoreMetadata;
import com.stratio.crossdata.core.metadata.MetadataManager;
import com.stratio.crossdata.core.query.BaseQuery;
import com.stratio.crossdata.core.query.IParsedQuery;
import com.stratio.crossdata.core.query.IValidatedQuery;
import com.stratio.crossdata.core.query.MetadataParsedQuery;
import com.stratio.crossdata.core.statements.AttachClusterStatement;
import com.stratio.crossdata.core.validator.BasicValidatorTest;
import com.stratio.crossdata.core.validator.Validator;

public class AttachClusterStatementTest extends BasicValidatorTest {

    @Test
    public void attachClusterNoOptions() {
        boolean res = true;
        String query = "ATTACH CLUSTER cluster on DATASTORE Cassandra";

        AttachClusterStatement attachClusterStatement = new AttachClusterStatement(new ClusterName("cluster"), false,
                new DataStoreName("Cassandra"),
                "");
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("CreateTableId", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, attachClusterStatement);

        try {
            validator.validate(parsedQuery);
            fail("Options are required for ATTACH CLUSTER statement");
        } catch (ValidationException e) {
            assertTrue(res, "Result should be True");
        } catch (IgnoreQueryException e) {
            assertTrue(res, "Result should be True");
        }
    }

    @Test
    public void attachClusterUnknownDatastore() {
        boolean res = true;
        String query = "ATTACH CLUSTER cluster on DATASTORE unknown";

        AttachClusterStatement attachClusterStatement = new AttachClusterStatement(new ClusterName("cluster"), false,
                new DataStoreName("unknown"),
                "");
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("CreateTableId", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, attachClusterStatement);
        try {
            validator.validate(parsedQuery);
            fail("Datastore must exists before ATTACH CLUSTER statement");
        } catch (ValidationException e) {
            assertTrue(res, "Result should be True");
        } catch (IgnoreQueryException e) {
            assertTrue(res, "Result should be True");
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
            fail("Execution should have failed because cluster already exists");
        } catch (ValidationException e) {
            String errorMsg = System.lineSeparator() +
                    "Expected: [cluster.cluster] exists already" + System.lineSeparator() +
                    "   Found: " + e.getMessage().trim();
            assertTrue(e.getMessage().trim().equalsIgnoreCase("[cluster.cluster] exists already"), errorMsg);
        } catch (IgnoreQueryException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void attachUnknownClusterWithOptionsIfExists() {
        String query = "ATTACH CLUSTER IF EXIST unknown on DATASTORE Cassandra with options " +
                "{'comment':'attach cluster'}";

        AttachClusterStatement attachClusterStatement = new AttachClusterStatement(new ClusterName("unknown"), true,
                new DataStoreName("Cassandra"),
                "{'comment':'attach cluster'}");
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("CreateTableId", query, new CatalogName("demo"));

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, attachClusterStatement);
        try {
            validator.validate(parsedQuery);
            fail("Execution should have failed because comment options is not in the manifest");
        } catch (ValidationException e) {
            String errorMsg = System.lineSeparator() +
                    "Expected: Some properties are not found in the datastore manifest" + System.lineSeparator() +
                    "   Found: " + e.getMessage().trim();
            assertTrue(e.getMessage().trim().equalsIgnoreCase("Some properties are not found in the manifest"),
                    errorMsg);
        } catch (IgnoreQueryException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void attachToADifferentDatastore() {
        // Add ElasticSearch Data Store to Metadata Manager
        HashSet<String> behaviours = new HashSet<>();
        behaviours.add("PROJECT");
        DataStoreMetadata dataStoreMetadata = new DataStoreMetadata(
                new DataStoreName("ElasticSearch"),
                "1.0",
                new HashSet<PropertyType>(),
                new HashSet<PropertyType>(),
                behaviours, null);
        MetadataManager.MANAGER.createDataStore(dataStoreMetadata);

        // Create & send query
        String query = "ATTACH CLUSTER cluster on DATASTORE ElasticSearch WITH OPTIONS {'comment':'attach cluster'};";

        AttachClusterStatement attachClusterStatement = new AttachClusterStatement(
                new ClusterName("cluster"),
                false,
                new DataStoreName("ElasticSearch"),
                "{'comment':'attach cluster'}");
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("attachToADifferentDatastore", query, new CatalogName("catalogTest"));

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, attachClusterStatement);
        try {
            validator.validate(parsedQuery);
            fail("Test should have failed.");
        } catch (ValidationException e) {
            assertEquals(e.getMessage().trim(),
                    "[cluster.cluster] exists already",
                    "Message exception not expected");
        } catch (IgnoreQueryException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void attachClusterWithProperties(){
        // Add ElasticSearch Data Store to Metadata Manager
        HashSet<String> behaviours = new HashSet<>();
        behaviours.add("PROJECT");
        behaviours.add("CREATE_TABLE");
        HashSet<PropertyType> requiredProperties = new HashSet<>();
        PropertyType pt = new PropertyType();
        pt.setPropertyName("replication");
        pt.setDescription("Data replication");
        requiredProperties.add(pt);
        DataStoreMetadata dataStoreMetadata = new DataStoreMetadata(
                new DataStoreName("randomDB"),
                "1.0",
                requiredProperties,
                new HashSet<PropertyType>(),
                behaviours, null);
        MetadataManager.MANAGER.createDataStore(dataStoreMetadata);

        // Create & send query
        String query = "ATTACH CLUSTER randomCluster on DATASTORE randomDB WITH OPTIONS {'replication':2};";

        AttachClusterStatement attachClusterStatement = new AttachClusterStatement(
                new ClusterName("randomCluster"),
                false,
                new DataStoreName("randomDB"),
                "{'replication':2}");
        Validator validator = new Validator();

        BaseQuery baseQuery = new BaseQuery("attachClusterWithProperties", query, new CatalogName("catalogTest"));

        IParsedQuery parsedQuery = new MetadataParsedQuery(baseQuery, attachClusterStatement);
        try {
            IValidatedQuery validatedQuery = validator.validate(parsedQuery);
            assertNotNull(validatedQuery, "Validation failed for: " + System.lineSeparator() + query);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
