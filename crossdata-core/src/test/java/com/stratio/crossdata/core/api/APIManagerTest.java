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

package com.stratio.crossdata.core.api;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.ask.APICommand;
import com.stratio.crossdata.common.ask.Command;
import com.stratio.crossdata.common.data.CatalogName;
import com.stratio.crossdata.common.data.ClusterName;
import com.stratio.crossdata.common.data.ConnectorName;
import com.stratio.crossdata.common.data.DataStoreName;
import com.stratio.crossdata.common.data.Name;
import com.stratio.crossdata.common.data.TableName;
import com.stratio.crossdata.common.exceptions.ManifestException;
import com.stratio.crossdata.common.manifest.BehaviorsType;
import com.stratio.crossdata.common.manifest.ConnectorType;
import com.stratio.crossdata.common.manifest.DataStoreRefsType;
import com.stratio.crossdata.common.manifest.DataStoreType;
import com.stratio.crossdata.common.manifest.PropertiesType;
import com.stratio.crossdata.common.manifest.PropertyType;
import com.stratio.crossdata.common.manifest.SupportedOperationsType;
import com.stratio.crossdata.common.result.CommandResult;
import com.stratio.crossdata.common.result.ErrorResult;
import com.stratio.crossdata.common.result.MetadataResult;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.core.MetadataManagerTestHelper;
import com.stratio.crossdata.core.metadata.MetadataManager;
import com.stratio.crossdata.core.parser.Parser;
import com.stratio.crossdata.core.planner.Planner;
import com.stratio.crossdata.core.validator.Validator;

public class APIManagerTest {

    private final Parser parser = new Parser();
    private final Validator validator = new Validator();
    private final Planner planner = new Planner("127.0.0.1");
    private static final String VERSION = "0.3.4";

    @BeforeClass
    public void setUp() throws ManifestException {
        MetadataManagerTestHelper.HELPER.initHelper();
        MetadataManagerTestHelper.HELPER.createTestEnvironment();
    }

    @AfterClass
    public void tearDown() throws Exception {
        MetadataManagerTestHelper.HELPER.closeHelper();
    }

    @Test
    public void testPersistDataStore() throws Exception {

        DataStoreType dataStoreType = new DataStoreType();

        dataStoreType.setName("dataStoreMock");

        dataStoreType.setVersion(VERSION);

        PropertiesType requiredProperties = new PropertiesType();
        List<PropertyType> property = new ArrayList<>();
        PropertyType propertyType = new PropertyType();
        propertyType.setPropertyName("RequiredProperty");
        propertyType.setDescription("Test");
        property.add(propertyType);
        requiredProperties.setProperty(property);
        dataStoreType.setRequiredProperties(requiredProperties);

        PropertiesType optionalProperties = new PropertiesType();
        property = new ArrayList<>();
        propertyType = new PropertyType();
        propertyType.setPropertyName("OptionalProperty");
        propertyType.setDescription("Test");
        property.add(propertyType);
        optionalProperties.setProperty(property);
        dataStoreType.setOptionalProperties(optionalProperties);

        BehaviorsType behaviorsType = new BehaviorsType();
        List<String> behavior = new ArrayList<>();
        behavior.add("Test");
        behaviorsType.setBehavior(behavior);
        dataStoreType.setBehaviors(behaviorsType);

        List params = new ArrayList();
        params.add(dataStoreType);

        Command cmd = new Command("QID", APICommand.ADD_MANIFEST(), params,"sessionTest");

        String expectedResult =
                "CrossdataManifest added " + System.lineSeparator() + "DATASTORE" + System.lineSeparator() +
                        "Name: dataStoreMock" + System.lineSeparator()
                        + "Version: " + VERSION + System.lineSeparator() + "Required properties: " + System.lineSeparator() +
                        "\tProperty: " + System.lineSeparator() + "\t\tPropertyName: RequiredProperty" +
                        System.lineSeparator() + "\t\tDescription: Test" + System.lineSeparator()
                        + "Optional properties: " +
                        System.lineSeparator() + "\tProperty: " + System.lineSeparator() + "\t\tPropertyName: " +
                        "OptionalProperty" + System.lineSeparator() + "\t\tDescription: Test" + System.lineSeparator() +
                        "Behaviors: " + System.lineSeparator() + "\tBehavior: Test" + System.lineSeparator();

        Result result = MetadataManagerTestHelper.HELPER.getApiManager().processRequest(cmd);

        if(result instanceof ErrorResult){
            fail(System.lineSeparator() +
                "testPersistDataStore failed." + System.lineSeparator() +
                ((ErrorResult)result).getErrorMessage());
        }

        CommandResult commandResult = (CommandResult) result;

        String str = String.valueOf(commandResult.getResult());

        assertTrue(str.equalsIgnoreCase(expectedResult), "- Expected: " + System.lineSeparator() +
                expectedResult + System.lineSeparator() + "-    Found: " + System.lineSeparator() + str);
    }

    @Test
    public void testPersistDataStoreFail() throws Exception {
        APIManager ApiManager = new APIManager(parser, validator, planner);

        DataStoreType dataStoreType = new DataStoreType();

        dataStoreType.setVersion(VERSION);

        PropertiesType requiredProperties = new PropertiesType();
        List<PropertyType> property = new ArrayList<>();
        PropertyType propertyType = new PropertyType();
        propertyType.setPropertyName("RequiredProperty");
        propertyType.setDescription("Test");
        property.add(propertyType);
        requiredProperties.setProperty(property);
        dataStoreType.setRequiredProperties(requiredProperties);

        PropertiesType optionalProperties = new PropertiesType();
        property = new ArrayList<>();
        propertyType = new PropertyType();
        propertyType.setPropertyName("OptionalProperty");
        propertyType.setDescription("Test");
        property.add(propertyType);
        optionalProperties.setProperty(property);
        dataStoreType.setOptionalProperties(optionalProperties);

        BehaviorsType behaviorsType = new BehaviorsType();
        List<String> behavior = new ArrayList<>();
        behavior.add("Test");
        behaviorsType.setBehavior(behavior);
        dataStoreType.setBehaviors(behaviorsType);

        List params = new ArrayList();
        params.add(dataStoreType);

        Command cmd = new Command("QID", APICommand.ADD_MANIFEST(), params,"sessionTest");

        String expectedResult =
                "CrossdataManifest added " + System.lineSeparator() + "DATASTORE" + System.lineSeparator() +
                        "Name: dataStoreTest" + System.lineSeparator()
                        + "Version: " + VERSION + System.lineSeparator() + "Required properties: " + System.lineSeparator() +
                        "\tProperty: " + System.lineSeparator() + "\t\tPropertyName: RequiredProperty" +
                        System.lineSeparator() + "\t\tDescription: Test" + System.lineSeparator()
                        + "Optional properties: " +
                        System.lineSeparator() + "\tProperty: " + System.lineSeparator() + "\t\tPropertyName: " +
                        "OptionalProperty" + System.lineSeparator() + "\t\tDescription: Test" + System.lineSeparator() +
                        "Behaviors: " + System.lineSeparator() + "\tBehavior: Test" + System.lineSeparator();

        Result result = ApiManager.processRequest(cmd);

        assertTrue(result instanceof ErrorResult,
                "ErrorResult expected." + System.lineSeparator() +
                "Found: " + result.getClass().getCanonicalName());
    }

    @Test
    public void testPersistConnector() throws Exception {
        APIManager ApiManager = new APIManager(parser, validator, planner);

        ConnectorType connectorType = new ConnectorType();

        connectorType.setConnectorName("connectorTest");

        connectorType.setVersion(VERSION);

        connectorType.setDataStores(new DataStoreRefsType());

        PropertiesType requiredProperties = new PropertiesType();
        List<PropertyType> property = new ArrayList<>();
        PropertyType propertyType = new PropertyType();
        propertyType.setPropertyName("RequiredProperty");
        propertyType.setDescription("Test");
        property.add(propertyType);
        requiredProperties.setProperty(property);
        connectorType.setRequiredProperties(requiredProperties);

        PropertiesType optionalProperties = new PropertiesType();
        property = new ArrayList<>();
        propertyType = new PropertyType();
        propertyType.setPropertyName("OptionalProperty");
        propertyType.setDescription("Test");
        property.add(propertyType);
        optionalProperties.setProperty(property);
        connectorType.setOptionalProperties(optionalProperties);

        SupportedOperationsType supportedOperationsType = new SupportedOperationsType();
        List<String> operation = new ArrayList<>();
        operation.add("PROJECT");
        supportedOperationsType.setOperation(operation);
        connectorType.setSupportedOperations(supportedOperationsType);

        List params = new ArrayList();
        params.add(connectorType);

        Command cmd = new Command("QID", APICommand.ADD_MANIFEST(), params,"sessionTest");

        String expectedResult = "CrossdataManifest added " + System.lineSeparator() + "CONNECTOR" +
                System.lineSeparator() + "ConnectorName: connectorTest" + System.lineSeparator()
                + "DataStores: " + System.lineSeparator()
                + "Version: " + VERSION + System.lineSeparator() + "Required properties: " + System.lineSeparator() +
                "\tProperty: " + System.lineSeparator() + "\t\tPropertyName: RequiredProperty" +
                System.lineSeparator() + "\t\tDescription: Test" + System.lineSeparator() + "Optional properties: " +
                System.lineSeparator() + "\tProperty: " + System.lineSeparator() + "\t\tPropertyName: " +
                "OptionalProperty" + System.lineSeparator() + "\t\tDescription: Test" + System.lineSeparator() +
                "Supported operations: " + System.lineSeparator() + "\tOperation: PROJECT" + System.lineSeparator();

        CommandResult result = (CommandResult) ApiManager.processRequest(cmd);

        String str = String.valueOf(result.getResult());

        assertTrue(str.equalsIgnoreCase(expectedResult), "- Expected: " + System.lineSeparator() +
                expectedResult + System.lineSeparator() + "-    Found: " + System.lineSeparator() + str);
    }

    @Test
    public void testPersistConnectorFail() throws Exception {
        APIManager ApiManager = new APIManager(parser, validator, planner);

        ConnectorType connectorType = new ConnectorType();

        connectorType.setVersion(VERSION);

        connectorType.setDataStores(new DataStoreRefsType());

        PropertiesType requiredProperties = new PropertiesType();
        List<PropertyType> property = new ArrayList<>();
        PropertyType propertyType = new PropertyType();
        propertyType.setPropertyName("RequiredProperty");
        propertyType.setDescription("Test");
        property.add(propertyType);
        requiredProperties.setProperty(property);
        connectorType.setRequiredProperties(requiredProperties);

        PropertiesType optionalProperties = new PropertiesType();
        property = new ArrayList<>();
        propertyType = new PropertyType();
        propertyType.setPropertyName("OptionalProperty");
        propertyType.setDescription("Test");
        property.add(propertyType);
        optionalProperties.setProperty(property);
        connectorType.setOptionalProperties(optionalProperties);

        SupportedOperationsType supportedOperationsType = new SupportedOperationsType();
        List<String> operation = new ArrayList<>();
        operation.add("PROJECT");
        supportedOperationsType.setOperation(operation);
        connectorType.setSupportedOperations(supportedOperationsType);

        List params = new ArrayList();
        params.add(connectorType);

        Command cmd = new Command("QID", APICommand.ADD_MANIFEST(), params,"sessionTest");

        String expectedResult = "CrossdataManifest added " + System.lineSeparator() + "CONNECTOR" +
                System.lineSeparator() + "ConnectorName: connectorTest" + System.lineSeparator()
                + "DataStores: " + System.lineSeparator()
                + "Version: " + VERSION + System.lineSeparator() + "Required properties: " + System.lineSeparator() +
                "\tProperty: " + System.lineSeparator() + "\t\tPropertyName: RequiredProperty" +
                System.lineSeparator() + "\t\tDescription: Test" + System.lineSeparator() + "Optional properties: " +
                System.lineSeparator() + "\tProperty: " + System.lineSeparator() + "\t\tPropertyName: " +
                "OptionalProperty" + System.lineSeparator() + "\t\tDescription: Test" + System.lineSeparator() +
                "Supported operations: " + System.lineSeparator() + "\tOperation: PROJECT" + System.lineSeparator();

        Result result = ApiManager.processRequest(cmd);

        assertTrue(result instanceof ErrorResult,
                "ErrorResult expected." + System.lineSeparator() +
                "Found: " + result.getClass().getCanonicalName());
    }

    @Test(dependsOnMethods = { "testPersistConnector" })
    public void testListConnectors() throws Exception {
        APIManager ApiManager = new APIManager(parser, validator, planner);
        Command cmd = new Command("QID", APICommand.DESCRIBE_CONNECTORS(), null,"sessionTest");
        MetadataManagerTestHelper.HELPER.createTestConnector("connectorTest", new DataStoreName("datastoreTest"), "akkaActorRef");
        CommandResult result = (CommandResult) ApiManager.processRequest(cmd);
        String str = String.valueOf(result.getResult());
        String[] connectors = str.split(System.lineSeparator());

        int expectedSize = 1;

        for(int i=0; i < connectors.length; i++){
            String connector = connectors[i];
            if(connector.contains("InMemoryConnector")){
                expectedSize++;
            }
        }

        assertEquals((connectors.length-1), expectedSize,
                System.lineSeparator() +
                "testListConnectors failed." + System.lineSeparator() +
                "Expected number of connectors: " + expectedSize + System.lineSeparator() +
                "Number of connectors found:    " + (connectors.length-1));
    }

    @Test(dependsOnMethods = { "testListConnectors" })
    public void testResetMetadata() throws Exception {
        APIManager ApiManager = new APIManager(parser, validator, planner);
        MetadataManagerTestHelper.HELPER.createTestConnector("connectorTest2", new DataStoreName("datastoreTest"), "akkaActorRef");
        Command cmd = new Command("QID", APICommand.RESET_SERVERDATA(), null,"sessionTest");
        CommandResult result = (CommandResult) ApiManager.processRequest(cmd);

        String str = String.valueOf(result.getResult());
        String expectedAnswer = "Crossdata server reset.";
        assertTrue(str.equals(expectedAnswer), System.lineSeparator() + "Expected: " + expectedAnswer +
                System.lineSeparator() + "   Found: " + str);

        assertTrue(MetadataManager.MANAGER.getCatalogs().isEmpty(), "Catalogs should be empty.");
        assertTrue(MetadataManager.MANAGER.getClusters().isEmpty(), "Clusters should be empty.");
        assertTrue(MetadataManager.MANAGER.getColumns().isEmpty(), "Columns should be empty");
        assertTrue(MetadataManager.MANAGER.getDatastores().isEmpty(), "Datastores should be empty");
        assertTrue(MetadataManager.MANAGER.getNodes().isEmpty(), "Nodes should be empty");
        assertTrue(MetadataManager.MANAGER.getTables().isEmpty(), "Tables should be empty");
        assertTrue(MetadataManager.MANAGER.getIndexes().isEmpty(), "Indexes should be empty");

        Name n = new ConnectorName("connectorTest2");
        assertTrue(MetadataManager.MANAGER.exists(n), "MetadataManager should maintain the connector basic info");
    }

    @Test
    public void testConstructor() throws Exception {
        APIManager ApiManager = new APIManager(parser, validator, planner);
        assertNotNull(ApiManager, "ApiManager shouldn't be null");
    }

    @Test
    public void testAddDataStore() throws Exception {
        APIManager ApiManager = new APIManager(parser, validator, planner);
        List params = new ArrayList<DataStoreType>();
        DataStoreType dataStoreType = new DataStoreType();
        dataStoreType.setName("CassandraDataStore");
        dataStoreType.setVersion("1.0");

        PropertiesType propertiesType = new PropertiesType();
        PropertyType prop = new PropertyType();
        prop.setPropertyName("DefaultLimit");
        prop.setDescription("Description");
        List<PropertyType> list = new ArrayList<>();
        list.add(prop);
        propertiesType.setProperty(list);
        dataStoreType.setRequiredProperties(propertiesType);

        params.add(dataStoreType);
        Command cmd = new Command("QID", APICommand.ADD_MANIFEST(), params,"sessionTest");
        Result result = ApiManager.processRequest(cmd);
        assertTrue(result instanceof CommandResult, "testProcessRequest should return a CommandResult");
        CommandResult cmdR = (CommandResult) result;
        String resultStr = (String) cmdR.getResult();

        assertTrue(resultStr.startsWith("CrossdataManifest added"),
                "Expected: " + "CrossdataManifest added" + System.lineSeparator() +
                "Found:    " + resultStr);
    }


    @Test
    public void cleanMetadataTest(){
        APIManager ApiManager = new APIManager(parser, validator, planner);
        Command cmd = new Command("CLEAN", APICommand.CLEAN_METADATA(), new ArrayList<>(), "sessionTest");
        Result result = ApiManager.processRequest(cmd);
        assertTrue(result instanceof CommandResult, "testProcessRequest should return a CommandResult");
        CommandResult cmdR = (CommandResult) result;
        String resultStr = (String) cmdR.getResult();

        assertTrue(resultStr.startsWith("Metadata cleaned."),
                "Expected: " + "    Metadata cleaned. " + System.lineSeparator() +
                        "Found:    " + resultStr);

        try {
            MetadataManagerTestHelper.HELPER.createTestEnvironment();
        } catch (ManifestException e) {
            fail("Error when try to restore test.");
        }
    }

    @Test
    public void describeCatalogTest(){
        APIManager ApiManager = new APIManager(parser, validator, planner);
        List<Object> params=new ArrayList<>();
        params.add(new CatalogName("testCatalog"));
        Command cmd = new Command("DescribeCatalog", APICommand.DESCRIBE_CATALOG(), params, "sessionTest");
        Result result = ApiManager.processRequest(cmd);
        assertTrue(result instanceof MetadataResult, "testProcessRequest should return a MetadataResult");
        MetadataResult res = (MetadataResult) result;
        Assert.assertTrue(res.getCatalogMetadataList().size() > 0, "The test should return almost one catalog");
    }

    @Test
    public void describeClusterTest(){
        APIManager ApiManager = new APIManager(parser, validator, planner);
        List<Object> params=new ArrayList<>();
        params.add(new ClusterName("production"));
        Command cmd = new Command("DescribeCluster", APICommand.DESCRIBE_CLUSTER(), params, "sessionTest");
        Result result = ApiManager.processRequest(cmd);
        assertTrue(result instanceof CommandResult, "testProcessRequest should return a CommandResult");
        CommandResult cmdR = (CommandResult) result;
        String resultStr = (String) cmdR.getResult();

        assertTrue(resultStr.contains("cluster.production"),
                "Expected: " + "    cluster.production  " + System.lineSeparator() +
                        "Found:    " + resultStr);
    }


    @Test
    public void describeClustersTest(){
        APIManager ApiManager = new APIManager(parser, validator, planner);
        List<Object> params=new ArrayList<>();

        Command cmd = new Command("DescribeCluster", APICommand.DESCRIBE_CLUSTERS(), params, "sessionTest");
        Result result = ApiManager.processRequest(cmd);
        assertTrue(result instanceof CommandResult, "testProcessRequest should return a CommandResult");
        CommandResult cmdR = (CommandResult) result;
        String resultStr = (String) cmdR.getResult();

        assertTrue(resultStr.contains("cluster.production"),
                "Expected: " + "    cluster.production  " + System.lineSeparator() +
                        "Found:    " + resultStr);
    }

    @Test
    public void describeConnectorTest(){
        APIManager ApiManager = new APIManager(parser, validator, planner);
        List<Object> params=new ArrayList<>();
        params.add(new ConnectorName("connector1"));
        Command cmd = new Command("DescribeConnector", APICommand.DESCRIBE_CONNECTOR(), params, "sessionTest");
        Result result = ApiManager.processRequest(cmd);
        assertTrue(result instanceof CommandResult, "testProcessRequest should return a CommandResult");
        CommandResult cmdR = (CommandResult) result;
        String resultStr = (String) cmdR.getResult();

        assertTrue(resultStr.contains("connector1"),
                "Expected: " + "    connector1  " + System.lineSeparator() +
                        "Found:    " + resultStr);
    }

    @Test
    public void describeConnectorsTest(){
        APIManager ApiManager = new APIManager(parser, validator, planner);
        List<Object> params=new ArrayList<>();

        Command cmd = new Command("DescribeConnectors", APICommand.DESCRIBE_CONNECTORS(), params, "sessionTest");
        Result result = ApiManager.processRequest(cmd);
        assertTrue(result instanceof CommandResult, "testProcessRequest should return a CommandResult");
        CommandResult cmdR = (CommandResult) result;
        String resultStr = (String) cmdR.getResult();

        assertTrue(resultStr.contains("connector1"),
                "Expected: " + "    connector1  " + System.lineSeparator() +
                        "Found:    " + resultStr);
    }


    @Test
    public void describeDataStoreTest(){
        APIManager ApiManager = new APIManager(parser, validator, planner);
        List<Object> params=new ArrayList<>();
        params.add(new DataStoreName("dataStoreTest"));
        Command cmd = new Command("DescribeDataStore", APICommand.DESCRIBE_DATASTORE(), params, "sessionTest");
        Result result = ApiManager.processRequest(cmd);
        assertTrue(result instanceof CommandResult, "testProcessRequest should return a CommandResult");
        CommandResult cmdR = (CommandResult) result;
        String resultStr = (String) cmdR.getResult();

        assertTrue(resultStr.contains("dataStoreTest"),
                "Expected: " + "    dataStoreTest  " + System.lineSeparator() +
                        "Found:    " + resultStr);
    }

    @Test
    public void describeDataStoresTest(){
        APIManager ApiManager = new APIManager(parser, validator, planner);
        List<Object> params=new ArrayList<>();

        Command cmd = new Command("DescribeDataStore", APICommand.DESCRIBE_DATASTORES(), params, "sessionTest");
        Result result = ApiManager.processRequest(cmd);
        assertTrue(result instanceof CommandResult, "testProcessRequest should return a CommandResult");
        CommandResult cmdR = (CommandResult) result;
        String resultStr = (String) cmdR.getResult();

        assertTrue(resultStr.contains("dataStoreTest"),
                "Expected: " + "    dataStoreTest  " + System.lineSeparator() +
                        "Found:    " + resultStr);
    }

    @Test
    public void describeSystemTest(){
        APIManager ApiManager = new APIManager(parser, validator, planner);
        List<Object> params=new ArrayList<>();

        Command cmd = new Command("DescribeSystem", APICommand.DESCRIBE_SYSTEM(), params, "sessionTest");
        Result result = ApiManager.processRequest(cmd);
        assertTrue(result instanceof CommandResult, "testProcessRequest should return a CommandResult");
        CommandResult cmdR = (CommandResult) result;
        String resultStr = (String) cmdR.getResult();

        assertTrue(resultStr.contains("\n"
                        + "Datastore datastore.dataStoreTest:\n"
                        + "\tCluster production:\n"),
                "Expected: " + "    \n"
                        + "Datastore datastore.dataStoreTest:\n"
                        + "\tCluster production:\n  " + System.lineSeparator() +
                        "Found:    " + resultStr);
    }

    @Test
    public void describeTablesTest(){
        APIManager ApiManager = new APIManager(parser, validator, planner);
        List<Object> params=new ArrayList<>();
        params.add(new CatalogName("testCatalog"));
        Command cmd = new Command("DescribeTables", APICommand.DESCRIBE_TABLES(), params, "sessionTest");
        Result result = ApiManager.processRequest(cmd);
        assertTrue(result instanceof CommandResult, "testProcessRequest should return a CommandResult");
        CommandResult cmdR = (CommandResult) result;
        String resultStr = (String) cmdR.getResult();
        assertTrue(resultStr.contains("testCatalog.testTable"),  "Expected: " + " testCatalog.testTable " + System
                .lineSeparator() +
                "Found:    " + resultStr);
    }


    @Test
    public void describeTableTest(){
        APIManager ApiManager = new APIManager(parser, validator, planner);
        List<Object> params=new ArrayList<>();
        params.add(new TableName("testCatalog","testTable"));
        Command cmd = new Command("DescribeTables", APICommand.DESCRIBE_TABLE(), params, "sessionTest");
        Result result = ApiManager.processRequest(cmd);
        assertTrue(result instanceof MetadataResult, "testProcessRequest should return a MetadataResult");
        MetadataResult res = (MetadataResult) result;
        Assert.assertTrue(res.getTableList().size() > 0, "The test should return almost one catalog");
    }


}
