/*
 *
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
 *
 *
 */

package com.stratio.crossdata.client.examples;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

import com.stratio.crossdata.client.examples.utils.DriverResultHandler;
import com.stratio.crossdata.common.exceptions.ConnectionException;
import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.common.result.Result;
import com.stratio.crossdata.driver.BasicDriver;
import com.stratio.crossdata.driver.DriverConnection;

public class ClientDecisionExample {
	/**
	 * Class constructor.
	 */
	private ClientDecisionExample() {
	}

	static final Logger LOG = Logger.getLogger(ClientDecisionExample.class);

	static final int NUMBER_OF_ROWS = 1000;
	static final String USER_NAME = "stratio";
	static final String PASSWORD = "stratio";

	public static void main(String[] args) {

		BasicDriver basicDriver = new BasicDriver();

		DriverConnection dc = null;
		try {
			dc = basicDriver.connect(USER_NAME, PASSWORD);
		} catch (ConnectionException ex) {
			LOG.error(ex);
		}
		assert dc != null;
		LOG.info("Connected to Crossdata Server");

		// RESET SERVER DATA
		Result result = dc.resetServerdata();
		assert result != null;
		LOG.info("Server data cleaned");

		// ATTACH CLUSTER
		result = null;
		try {
			result = dc
					.executeQuery(
							"ATTACH CLUSTER decisionprod ON DATASTORE Decision WITH OPTIONS {'KafkaServer': '[127.0.0.1]', 'KafkaPort': '[9092]', 'zooKeeperServer': '[127.0.0.1]', 'zooKeeperPort': '[2181]'};");
		} catch (ConnectionException | ValidationException | ExecutionException
				| UnsupportedException ex) {
			LOG.error(ex);
		}
		assert result != null;
		LOG.info("Cluster attached.");

		// ATTACH STRATIO DECISION CONNECTOR
		result = null;
		try {
			result = dc
					.executeQuery(
							"ATTACH CONNECTOR DecisionConnector TO decisionprod WITH OPTIONS {};");
		} catch (ConnectionException | ValidationException | ExecutionException
				| UnsupportedException ex) {
			LOG.error(ex);
		}
		assert result != null;

		LOG.info("Stratio Cassandra connector attached.");

		// CREATE CATALOG
		result = null;
		try {
			result = dc.executeQuery("CREATE CATALOG catalogTest;");
		} catch (ConnectionException | ValidationException | ExecutionException
				| UnsupportedException ex) {
			LOG.error(ex);
		}
		assert result != null;
		LOG.info("Catalog created.");

		// USE
		dc.setCurrentCatalog("catalogTest");

		// CREATE TABLE 1
		result = null;
		try {
			result = dc
					.executeQuery(
							"CREATE TABLE tableTest ON CLUSTER decisionprod "
									+ "(id int PRIMARY KEY, timestamp TIMESTAMP, Temp DOUBLE);");
		} catch (ConnectionException | ValidationException | ExecutionException
				| UnsupportedException ex) {
			LOG.error(ex);
		}
		assert result != null;
		LOG.info("Table 1 created.");
		// DRIVER RESULT HANDLER
		DriverResultHandler driverResultHandler = new DriverResultHandler();
		// USE
		dc.setCurrentCatalog("catalogTest");

		dc.executeAsyncRawQuery(
				"SELECT * FROM tableTest WITH WINDOW 5 SECS",
				driverResultHandler);

		while (driverResultHandler.getNumResults() < 10) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		dc.stopProcess(driverResultHandler.getQueryID());
		// CLOSE DRIVER
		basicDriver.close();
		LOG.info("Connection closed");
	}

}
