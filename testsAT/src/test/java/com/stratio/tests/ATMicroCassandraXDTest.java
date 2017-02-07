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
package com.stratio.tests;

import com.stratio.qa.cucumber.testng.CucumberRunner;
import com.stratio.qa.exceptions.DBException;
import com.stratio.tests.utils.BaseTest;
import com.stratio.qa.utils.CassandraUtils;
import com.stratio.qa.utils.ThreadProperty;
import cucumber.api.CucumberOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

//Indicar feature
@CucumberOptions(features = {
		"src/test/resources/features/MicroStrategy/MicroStrategy.feature",
})
public class ATMicroCassandraXDTest extends BaseTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass()
			.getCanonicalName());
	CassandraUtils cassandra = new CassandraUtils();
	private String catalog = "KUYA";
	// Global for C*
	private String cluster = System.getProperty("CASSANDRA_CLUSTER",
			"Test Cluster");
	private String host = System.getProperty("CASSANDRA_HOST", "127.0.0.1");
	private String sourceProvider = System.getProperty("SOURCE_PROVIDER",
			"com.stratio.crossdata.sql.sources.cassandra");

	public ATMicroCassandraXDTest() {
	}

	@BeforeClass(groups = {"micro-cassandra"})
	public void setUp() {
		logger.info("Connecting to Cassandra Cluster");
		cassandra.connect();
		logger.info("Checking if the catalog exists");
		if (cassandra.existsKeyspace(catalog, false) || cassandra.existsKeyspace("databasetest", false) ) {
			logger.info("The catalog exists");
			cassandra.executeQuery("DROP KEYSPACE \"KUYA\"");
			cassandra.executeQuery("DROP KEYSPACE databasetest");
			logger.info("The catalog has benn dropped");
		}

		cassandra.loadTestData(catalog, "/scripts/CassandraMicroScript.cql");
		List<String> tables = cassandra.getTables(catalog);
		String connector = "Cassandra-Micro";
		ThreadProperty.set("Cluster", cluster);
		ThreadProperty.set("Catalog", catalog);
		ThreadProperty.set("Tables", tables.toString());
		ThreadProperty.set("Host", host);
		ThreadProperty.set("SourceProvider", sourceProvider);
		ThreadProperty.set("Connector", connector);
		ThreadProperty.set("Driver", "context");
		try {
			cassandra.disconnect();
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@AfterClass(groups = {"micro-cassandra"})
	public void cleanUp() {

	}

	@Test(enabled = true, groups = {"micro-cassandra"})
	public void ATCassandraXDTest() throws Exception {
		new CucumberRunner(this.getClass()).runCukes();
	}

}

