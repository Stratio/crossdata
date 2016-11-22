/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.crossdata.testsAT.specs;


import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static com.stratio.tests.utils.DataFrameAssert.asserThat;
import static com.stratio.tests.utils.SQLResultAssert.asserThat;
import static com.stratio.tests.utils.FlattenerMetadataAssert.asserThat;
import static org.hamcrest.Matchers.equalTo;
import com.stratio.exceptions.DBException;
import com.stratio.tests.utils.ThreadProperty;

/**
 * Created by hdominguez on 13/10/15.
 */
public class CrossdataSpecs extends BaseSpec {

    public CrossdataSpecs(Common spec) {
        this.commonspec = spec;
    }

    @When(value = "^I execute '(.*?)'$")
    public void execute(String query) {
        commonspec.getExceptions().clear();
        commonspec.getLogger().info("Executing native query: " + query);
        if (ThreadProperty.get("Driver").equals("context")) {
            try {
                commonspec.getXdContext().executeQuery(query);
            } catch (Exception e) {
                commonspec.getLogger().error("XD CONTEXT : " + e.toString());
                commonspec.getExceptions().add(e);
            }
            commonspec.getLogger().info("Query executed");
        } else {
            commonspec.getLogger().info("Executing native query in driver: " + query);
            try {
                commonspec.getXdDriver().executeSyncQuery(query);
            } catch (Exception e) {
                commonspec.getLogger().error(e.toString());
                commonspec.getExceptions().add(e);
            }
            commonspec.getLogger().info("Query executed");

        }
    }

    @Then(value = "The result has to have '(.*?)' rows:$")
    public void assertResult(String rows, DataTable table) {
        if (ThreadProperty.get("Driver").equals("context")) {
            commonspec.getLogger().info("The result obtained is: ");
            commonspec.getXdContext().showDataframe();
            asserThat(commonspec.getXdContext().getXDDataFrame()).hasLength(Integer.parseInt(rows));
            asserThat(commonspec.getXdContext().getXDDataFrame()).equalsMetadata(table.raw().get(0));
            asserThat(commonspec.getXdContext().getXDDataFrame()).equalsResultsNative(table.raw());
            commonspec.getXdContext().clearXDF();
        } else {
            asserThat(commonspec.getXdDriver().getResult()).hasLength(Integer.parseInt(rows));
            asserThat(commonspec.getXdDriver().getResult()).assertSuccesfulMetadataResult(table.raw().get(0));
            asserThat(commonspec.getXdDriver().getResult()).equalsSQLResults(table.raw());
        }
    }

    @Then(value = "The flattened result has to have '(.*?)' rows:$")
    public void assertResultSpark(String rows, DataTable table) {
        if (ThreadProperty.get("Driver").equals("context")) {
        asserThat(commonspec.getXdContext().getXDDataFrame()).hasFlattenedLength(Integer.parseInt(rows));
        asserThat(commonspec.getXdContext().getXDDataFrame()).equalsFlattenedMetadata(table.raw().get(0));
        asserThat(commonspec.getXdContext().getXDDataFrame()).equalsFlattenedResult(table.raw());
            commonspec.getXdContext().clearXDF();
        }
    }

    @Then(value = "The spark result has to have '(.*?)' rows:$")
    public void assertResultflattened(String rows, DataTable table) {
        if (ThreadProperty.get("Driver").equals("context")) {
            commonspec.getLogger().info("The result obtained is: ");
            commonspec.getXdContext().showDataframe();
            asserThat(commonspec.getXdContext().getXDDataFrame()).hasLength(Integer.parseInt(rows));
            asserThat(commonspec.getXdContext().getXDDataFrame()).equalsMetadata(table.raw().get(0));
            asserThat(commonspec.getXdContext().getXDDataFrame()).equalsResultsSpark(table.raw());
            commonspec.getXdContext().clearXDF();
        } else {
            asserThat(commonspec.getXdDriver().getResult()).hasLength(Integer.parseInt(rows));
            asserThat(commonspec.getXdDriver().getResult()).assertSuccesfulMetadataResult(table.raw().get(0));
            asserThat(commonspec.getXdDriver().getResult()).equalsSQLResults(table.raw());
        }
    }

    @Then(value = "The spark result has to have '(.*?)' rows-function:$")
    public void assertResultfGroupConcat(String rows, DataTable table) {
        if (ThreadProperty.get("Driver").equals("context")) {
            commonspec.getLogger().info("The result obtained is: ");
            commonspec.getXdContext().showDataframe();
            asserThat(commonspec.getXdContext().getXDDataFrame()).hasLength(Integer.parseInt(rows));
            asserThat(commonspec.getXdContext().getXDDataFrame()).equalsMetadata(table.raw().get(0));
            asserThat(commonspec.getXdContext().getXDDataFrame()).equalsResultsSparkIgnoringRowOrder(table.raw());
            commonspec.getXdContext().clearXDF();
        } else {
            asserThat(commonspec.getXdDriver().getResult()).hasLength(Integer.parseInt(rows));
            asserThat(commonspec.getXdDriver().getResult()).assertSuccesfulMetadataResult(table.raw().get(0));
            asserThat(commonspec.getXdDriver().getResult()).equalsSQLResults(table.raw());
        }
    }

    @Then(value = "The result has to have '(.*?)' rows$")
    public void assertResultLengh(String rows, DataTable table) {
        if (ThreadProperty.get("Driver").equals("context")) {
            commonspec.getLogger().info("The result obtained is: ");
            commonspec.getXdContext().showDataframe();
            asserThat(commonspec.getXdContext().getXDDataFrame()).hasLength(Integer.parseInt(rows));
            asserThat(commonspec.getXdContext().getXDDataFrame()).equalsMetadata(table.raw().get(0));
            commonspec.getXdContext().clearXDF();
        } else {
            asserThat(commonspec.getXdDriver().getResult()).hasLength(Integer.parseInt(rows));
            asserThat(commonspec.getXdDriver().getResult()).assertSuccesfulMetadataResult(table.raw().get(0));
        }
    }

    @Then(value = "The result has to have '(.*?)' rows native$")
    public void assertResultLenghNative(String rows, DataTable table) {
        if (ThreadProperty.get("Driver").equals("context")) {
            commonspec.getLogger().info("The result obtained is: ");
            commonspec.getXdContext().showDataframe();
            asserThat(commonspec.getXdContext().getXDDataFrame()).hasLengthNative(Integer.parseInt(rows));
            asserThat(commonspec.getXdContext().getXDDataFrame()).equalsMetadata(table.raw().get(0));
            commonspec.getXdContext().clearXDF();
        } else {
            asserThat(commonspec.getXdDriver().getResult()).hasLength(Integer.parseInt(rows));
            asserThat(commonspec.getXdDriver().getResult()).assertSuccesfulMetadataResult(table.raw().get(0));
        }
    }

    @Then(value = "The result has to have '(.*?)' rows ignoring the order:$")
    public void assertResultLenghIgnoringOrder(String rows, DataTable table) {
        commonspec.getLogger().info("The result obtained is: ");
        if (ThreadProperty.get("Driver").equals("context")) {
            commonspec.getXdContext().showDataframe();
            asserThat(commonspec.getXdContext().getXDDataFrame()).hasLength(Integer.parseInt(rows));
            asserThat(commonspec.getXdContext().getXDDataFrame()).equalsMetadata(table.raw().get(0));
            asserThat(commonspec.getXdContext().getXDDataFrame()).equalsResultsIgnoringOrderNative(table.raw());
            commonspec.getXdContext().clearXDF();
        } else {
            asserThat(commonspec.getXdDriver().getResult()).hasLength(Integer.parseInt(rows));
            asserThat(commonspec.getXdDriver().getResult()).assertSuccesfulMetadataResult(table.raw().get(0));
            asserThat(commonspec.getXdDriver().getResult()).equalsResultsIgnoringOrderNative(table.raw());
        }
    }

    @Then(value = "Drop the spark tables$")
    public void dropTables() {
        commonspec.getLogger().info("The result obtained is: ");
        commonspec.getXdContext().dropTables();
    }

    @When(value = "I describe table '(.*?)'")
    public void describeTable(String tableName) {
        commonspec.getExceptions().clear();
        try {
            commonspec.getXdDriver().describeTables(tableName);
            commonspec.getLogger().info("Describe Tables executed correctly");

        } catch (Exception e) {
            commonspec.getLogger().info(e.toString());
            commonspec.getExceptions().add(e);
        }
    }

    @Then(value = "The table has to have '(.*?)' columns")
    public void assertDescribeTables(String num_columns, DataTable metadata) {
        asserThat(commonspec.getXdDriver().getTablesDescription()).hasLength(Integer.parseInt(num_columns));
        asserThat(commonspec.getXdDriver().getTablesDescription()).checkMetadata(metadata);
    }

    @Given(value = "I drop cassandra keyspace '(.*?)'")
    public void dropCassandraKeyspace(String keyspace){
        commonspec.getExceptions().clear();
        commonspec.getLogger().info("Connecting to Cassandra");
        commonspec.getCassandraClient().connect();
        commonspec.getLogger().info("Drop cassandra keyspace " + keyspace);
        commonspec.getLogger().info("Checking if the catalog exists");
        if (commonspec.getCassandraClient().existsKeyspace(keyspace, false)) {
            commonspec.getLogger().info("The catalog exists");
            commonspec.getCassandraClient().dropKeyspace(keyspace);
            commonspec.getLogger().info("The catalog has benn dropped");
        }
        try {
            commonspec.getCassandraClient().disconnect();
        }catch(Exception e){
            commonspec.getExceptions().add(e);
        }
    }

    @Given(value = "^I create a  cassandra keyspace '(.*?)'$")
    public void createCassandraKeyspace(String keyspace){
        commonspec.getExceptions().clear();
        commonspec.getLogger().info("Connecting to Cassandra");
        commonspec.getCassandraClient().connect();
        commonspec.getLogger().info("Drop cassandra keyspace " + keyspace);
        commonspec.getLogger().info("Checking if the catalog exists");
        if (commonspec.getCassandraClient().existsKeyspace(keyspace, false)) {
            commonspec.getLogger().info("The catalog exists");
            commonspec.getCassandraClient().dropKeyspace(keyspace);
            commonspec.getLogger().info("The catalog has benn dropped");
        }
        commonspec.getCassandraClient().createKeyspace(keyspace);
        try {
            commonspec.getCassandraClient().disconnect();
        }catch(Exception e){
            commonspec.getExceptions().add(e);
        }
    }

    @Given(value = "^I create a  cassandra table '(.*?)' over keyspace '(.*?)'$")
    public void createCassandraTable(String tableName,String keyspace){
        commonspec.getExceptions().clear();
        commonspec.getLogger().info("Connecting to Cassandra");
        commonspec.getCassandraClient().connect();
        commonspec.getLogger().info("Drop cassandra keyspace " + keyspace);
        commonspec.getLogger().info("Checking if the catalog exists");
        if (commonspec.getCassandraClient().existsKeyspace(keyspace, false)) {
            commonspec.getLogger().info("The catalog exists");
            commonspec.getCassandraClient().dropKeyspace(keyspace);
            commonspec.getLogger().info("The catalog has benn dropped");
        }
        commonspec.getCassandraClient().createKeyspace(keyspace);
        String query = "CREATE TABLE "+ keyspace + "." + tableName + "(id INT PRIMARY KEY, name TEXT);";
        commonspec.getCassandraClient().executeQuery(query);
        try {
            commonspec.getCassandraClient().disconnect();
        }catch(Exception e){
            commonspec.getExceptions().add(e);
        }
    }

    @Then(value= "The table '(.*?)' exists in cassandra keyspace '(.*?)'")
    public void existsCassandraTable(String tableName,String keyspace){
        commonspec.getLogger().info("Connecting to Cassandra");
        commonspec.getCassandraClient().connect();
        boolean res = commonspec.getCassandraClient().existsTable(keyspace, tableName, false);
        try {
            commonspec.getCassandraClient().disconnect();
        }catch(Exception e){
            commonspec.getExceptions().add(e);
        }
       // org.hamcrest.MatcherAssert.assertThat("The Cassandra keyspace does not contains the table", res,equalTo
        // (true));
    }

    @Given(value = "^I create a mongoDB database '(.*?)'$")
    public void createMongoDBDataBase(String database){
        commonspec.getExceptions().clear();
        commonspec.getLogger().info("Connecting to Mongo");
        try {
            commonspec.getMongoDBClient().connect();
            commonspec.getMongoDBClient().connectToMongoDBDataBase(database);
            commonspec.getMongoDBClient().disconnect();
        } catch (DBException e) {
            commonspec.getExceptions().add(e);
        }
    }

    @Given(value = "^I drop a mongoDB database '(.*?)'$")
    public void dropMongoDBDataBase(String database){
        commonspec.getExceptions().clear();
        commonspec.getLogger().info("Connecting to Mongo");
        try {
            commonspec.getMongoDBClient().connect();
            commonspec.getMongoDBClient().dropMongoDBDataBase(database);
            commonspec.getMongoDBClient().disconnect();
        } catch (DBException e) {
            commonspec.getExceptions().add(e);
        }
    }

    @Given(value = "^I create a mongoDB collection '(.*?)' over database '(.*?)'$")
    public void createMongoDBCollection(String collection, String database){
        commonspec.getExceptions().clear();
        commonspec.getLogger().info("Connecting to Mongo");
        try {
            commonspec.getMongoDBClient().connect();
            commonspec.getMongoDBClient().connectToMongoDBDataBase(database);
            commonspec.getMongoDBClient().createMongoDBCollection(collection);
            commonspec.getMongoDBClient().disconnect();
        } catch (DBException e) {
            commonspec.getExceptions().add(e);
        }
    }

    @Then(value= "The collection '(.*?)' exists in mongo database '(.*?)'")
    public void existsMongoCollection(String collection,String database){
        commonspec.getExceptions().clear();
        commonspec.getLogger().info("Connecting to Mongo");
        try {
            commonspec.getMongoDBClient().connect();
            commonspec.getMongoDBClient().exitsMongoDbDataBase(database);
            org.hamcrest.MatcherAssert.assertThat("The Mongo database does not exists", commonspec
                    .getMongoDBClient().exitsMongoDbDataBase(database), equalTo(true));
            commonspec.getMongoDBClient().connectToMongoDBDataBase(database);
            org.hamcrest.MatcherAssert.assertThat("The Mongo collection "+ collection +"does not exists in the "
                            + "database " +
                            database,
                    commonspec
                            .getMongoDBClient().exitsCollections(collection),equalTo(true));
            commonspec.getMongoDBClient().disconnect();
        } catch (DBException e) {
            commonspec.getExceptions().add(e);
        }
    }
}
