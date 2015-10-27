package com.stratio.tests;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.stratio.cucumber.testng.CucumberRunner;
import com.stratio.exceptions.DBException;
import com.stratio.tests.utils.BaseTest;
import com.stratio.tests.utils.CassandraUtils;
import com.stratio.tests.utils.ThreadProperty;

import cucumber.api.CucumberOptions;

//Indicar feature
@CucumberOptions(features = { "src/test/resources/features/Cassandra/CassandraSelectSimple.feature",
		"src/test/resources/features/Cassandra/CassandraSelectLimit.feature",
		"src/test/resources/features/Cassandra/CassandraSelectEqualsFilter.feature" })
public class ATCassandraXDTest extends BaseTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass()
			.getCanonicalName());
	CassandraUtils cassandra = new CassandraUtils();
	private String catalog = "databasetest";
	// Global for C*
	private String cluster = System.getProperty("CASSANDRA_CLUSTER",
			"Test Cluster");
	private String host = System.getProperty("CASSANDRA_HOST", "127.0.0.1");
	private String sourceProvider = System.getProperty("SOURCE_PROVIDER",
			"com.stratio.crossdata.sql.sources.cassandra");

	public ATCassandraXDTest() {
	}

	@BeforeClass
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
		//Compartimos las variablescd
		String connector = "Cassandra";
		ThreadProperty.set("Cluster", cluster);
		ThreadProperty.set("Catalog", catalog);
		ThreadProperty.set("Tables", tables.toString());
		ThreadProperty.set("Host", host);
		ThreadProperty.set("SourceProvider", sourceProvider);
		ThreadProperty.set("Connector", connector);

	}

	@AfterClass
	public void cleanUp() {
		cassandra.dropKeyspace(catalog);
		try {
			cassandra.disconnect();
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test(enabled = true)
	public void ATCassandraXD() throws Exception {
		new CucumberRunner(this.getClass()).runCukes();
	}

}
