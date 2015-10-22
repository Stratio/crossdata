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

package com.stratio.crossdata.client.examples;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jfairy.Fairy;
import org.jfairy.producer.BaseProducer;
import org.jfairy.producer.person.Person;

import com.stratio.crossdata.common.data.ResultSet;
import com.stratio.crossdata.common.exceptions.ConnectionException;
import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.common.result.QueryResult;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.driver.BasicDriver;
import com.stratio.crossdata.driver.DriverConnection;
import com.stratio.crossdata.driver.querybuilder.Query;
import com.stratio.crossdata.driver.querybuilder.QueryBuilder;

public class ClientAPIExample {

    static final Logger LOG = Logger.getLogger(ClientAPIExample.class);

    static final String INMEMORY_CLUSTER = "InMemoryCluster";
    static final int NUMBER_OF_ROWS = 100;
    static final String USER_NAME = "stratio";
    static final String PASSWORD = "stratio";
    static final String CATALOG_NAME = "customers";
    static final String TABLE_NAME = "products";
    static final Map<String, String> COLUMN_TYPE = new HashMap<>(4);

    static {
        COLUMN_TYPE.put("id", "INT");
        COLUMN_TYPE.put("serial", "INT");
        COLUMN_TYPE.put("name", "TEXT");
        COLUMN_TYPE.put("rating", "DOUBLE");
        COLUMN_TYPE.put("email", "TEXT");
    }

    static final List<String> PARTITION_KEY = Arrays.asList("id");

    /**
     * Class constructor.
     */
    private ClientAPIExample() {
    }

    public static void main(String[] args) {


        BasicDriver basicDriver = new BasicDriver();

        try {
            DriverConnection xdConnection = basicDriver.connect(USER_NAME, PASSWORD);
            LOG.info("Connected to Crossdata Server");

            // RESET SERVER DATA
            xdConnection.cleanMetadata();
            xdConnection.resetServerdata();

            // ATTACH CLUSTER
            Map<String, Object> clusterOptions = new HashMap<>(1);
            clusterOptions.put("TableRowLimit",100);
            xdConnection.attachCluster(INMEMORY_CLUSTER, "InMemoryDatastore", false, clusterOptions);


            // ATTACH STRATIO INMEMORY CONNECTOR
            xdConnection.attachConnector("InMemoryConnector", INMEMORY_CLUSTER, Collections.<String, Object>emptyMap(), 0, 5);

            // CREATE CATALOG
            xdConnection.createCatalog(CATALOG_NAME, true, Collections.<String, Object>emptyMap());

            // CREATE TABLE
            xdConnection.createTable(CATALOG_NAME, TABLE_NAME, INMEMORY_CLUSTER, COLUMN_TYPE, PARTITION_KEY, Collections.<String>emptyList(), false, false, Collections.<String, Object>emptyMap());

            // INSERT RANDOM DATA
            Fairy fairy = Fairy.create();
            BaseProducer baseProducer = fairy.baseProducer();
            Map<String, Object> columnValue = new HashMap<>();
            Map<String, Object> insertOptions =Collections.emptyMap();
            for(int i = 1; i<NUMBER_OF_ROWS+1; i++){
                Person person = fairy.person();
                columnValue.put("id", generateSerial(baseProducer));
                columnValue.put("serial", generateSerial(baseProducer));
                columnValue.put("name", generateName(person));
                columnValue.put("rating", generateRating(baseProducer));
                columnValue.put("email", generateEmail(person));

                xdConnection.insert(CATALOG_NAME,TABLE_NAME, columnValue, false, insertOptions);
                LOG.info("Row for first table inserted.");
            }

            //SELECT
            Query selectLowestRated = QueryBuilder.selectAll().from(CATALOG_NAME+"."+TABLE_NAME).orderBy("rating").limit(20);
            Result queryResult = xdConnection.executeQuery(selectLowestRated);
            ResultSet resultSet = getResultSet(queryResult);
            LOG.info("ResultSet size: "+resultSet.size());

            //DROP TABLE
            xdConnection.dropTable(CATALOG_NAME,TABLE_NAME,false, false);

            //DROP CATALOG
            xdConnection.dropCatalog(CATALOG_NAME,false);

            // CLOSE CONNECTION
            basicDriver.disconnect();
            LOG.info("Connection closed");
        } catch (ConnectionException | ValidationException | ExecutionException | UnsupportedException  ex) {
            LOG.error(ex);
        }
        // CLOSE DRIVER
        basicDriver.close();
    }

    private static int generateSerial(BaseProducer baseProducer) {
        return baseProducer.randomBetween(1, Integer.MAX_VALUE-1);
    }

    private static String generateName(Person person) {
        return  person.username() ;
    }

    private static double generateRating(BaseProducer baseProducer) {
        double rating = baseProducer.randomBetween(0.0, 10.0);
        return Math.round(rating*100.0)/100.0;
    }

    private static String generateEmail(Person person) {
        return  person.email();
    }

    private static ResultSet getResultSet(Result queryResult) throws ExecutionException {
        if (queryResult.hasError()){
            throw new ExecutionException("Error executing the query");
        }else {
            return ((QueryResult)queryResult).getResultSet();
        }
    }
}
