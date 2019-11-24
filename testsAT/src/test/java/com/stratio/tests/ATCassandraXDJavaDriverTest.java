/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.tests;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.stratio.qa.cucumber.testng.CucumberRunner;
import com.stratio.qa.exceptions.DBException;
import com.stratio.qa.utils.BaseTest;
import com.stratio.qa.utils.CassandraUtils;
import com.stratio.qa.utils.ThreadProperty;

import cucumber.api.CucumberOptions;

//Indicar feature
@CucumberOptions(features = {
		"src/test/resources/features/Cassandra/CassandraSelectSimple.feature",
		"src/test/resources/features/Cassandra/CassandraSelectLimit.feature",
		"src/test/resources/features/Cassandra/CassandraSelectEqualsFilter.feature",
		"src/test/resources/features/Cassandra/CassandraSelectUDF.feature",
		"src/test/resources/features/Cassandra/CassandraPureNativeAggregation.feature",
		"src/test/resources/features/Udaf/Group_concat.feature",
		"src/test/resources/features/Views/TemporaryViews.feature",
		"src/test/resources/features/Views/Views.feature",
		"src/test/resources/features/Views/DropViews.feature"

		})
public class ATCassandraXDJavaDriverTest extends BaseTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass()
			.getCanonicalName());
	CassandraUtils cassandra = new CassandraUtils();
	private String catalog = "databasetest";
	// Global for C*
	private String host = System.getProperty("CASSANDRA_HOST", "127.0.0.1");
	private String sourceProvider = System.getProperty("SOURCE_PROVIDER",
			"com.stratio.crossdata.sql.sources.cassandra");

	public ATCassandraXDJavaDriverTest() {
	}

	@BeforeClass(groups = {"cassandra"})
	public void setUp() {
		logger.info("Connecting to Cassandra Cluster");
		cassandra.connect();
		logger.info("Checking if the catalog exists");
		if (cassandra.existsKeyspace(catalog, false)) {
			logger.info("The catalog exists");
			cassandra.dropKeyspace(catalog);
			logger.info("The catalog has benn dropped");
		}
		cassandra.createKeyspace(catalog);
		cassandra.loadTestData(catalog, "/scripts/CassandraScript.cql");
		List<String> tables = cassandra.getTables(catalog);
		String connector = "Cassandra";
		ThreadProperty.set("Connector", connector);
		ThreadProperty.set("Driver", "javaDriver");

	}

	@AfterClass(groups = {"cassandra"})
	public void cleanUp() {
		cassandra.dropKeyspace(catalog);
		try {
			cassandra.disconnect();
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test(enabled = false, groups = {"cassandra"})
	public void ATCassandraXDJavaDriverTest() throws Exception {
		new CucumberRunner(this.getClass()).runCukes();
	}

}

