package com.stratio.crossdata.testsAT.automated.acceptance;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.exceptions.ApiException;
import com.stratio.crossdata.common.exceptions.ConnectionException;
import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.exceptions.ParsingException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.common.result.CommandResult;
import com.stratio.crossdata.testsAT.automated.dataProvider.DataProviderElasticSearchConnector;
import com.stratio.crossdata.driver.DriverConnection;
import com.stratio.cucumber.testng.CucumberRunner;
import com.stratio.tests.utils.BaseTest;
import com.stratio.tests.utils.Driver;
import com.stratio.tests.utils.ElasticSearchUtils;
import com.stratio.tests.utils.ThreadProperty;

public class ElasticSearchConnectorTests extends BaseTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass()
			.getCanonicalName());
	Driver crossDataDriver;
	DriverConnection crossdataConnection;
	static final int NUMBER_OF_ROWS = 1000;
	private String elasticSearchHost = System.getProperty("elasticSearchHost",
			"127.0.0.1");
	private String elasticSearchNativePort = System.getProperty(
			"elasticSearchNativePort", "9300");
	private String elasticSearchRestPort = System.getProperty(
			"elasticSearchRestPort", "9200");
	private String elasticSearchClusterName = System.getProperty(
			"elasticSearchClusterName", "esCluster");

	private String attachClusterQuery = "ATTACH CLUSTER ClusterTest ON DATASTORE elasticsearch WITH OPTIONS {'Hosts':'["
			+ elasticSearchHost
			+ "]', 'Native Ports':'["
			+ elasticSearchNativePort
			+ "]','Restful Ports':'["
			+ elasticSearchRestPort
			+ "]','Cluster Name':'"
			+ elasticSearchClusterName + "'};";

	private String attachConnectorQuery = "ATTACH CONNECTOR elasticsearchconnector TO ClusterTest WITH OPTIONS {};";

	private String detachClusterQuery = "DETACH CLUSTER ClusterTest;";
	private String detachConnectorQuery = "DETACH CONNECTOR elasticsearchconnector FROM ClusterTest;";

	@Factory(dataProviderClass = DataProviderElasticSearchConnector.class, dataProvider = "dataProvider")
	public ElasticSearchConnectorTests(String data) {
		this.setDataProvider(data);
	}

	@BeforeClass
	public void setUp() throws ConnectionException {
		ThreadProperty.set("ExecutionType", "Crossdata");
		logger.info("Deleting previous data in ES");
		ElasticSearchUtils elasticSearch = new ElasticSearchUtils();
		elasticSearch.connect();
		elasticSearch.dropIndex("catalogtest");
		crossDataDriver = new Driver();
		logger.info("Trying to connect with the crossdataServer");
		try {
			crossDataDriver.connect("PRELOAD_USER_TEST");
			logger.info("Connection successful");
		} catch (ConnectionException e) {
			logger.error("Connection fails");
		}
		// Limpiamos los restos
		CommandResult cr = crossDataDriver.resetMetadata();
		logger.info("Reset metadata result: " + cr.getResult());
		try {
			logger.info("Attaching cluster to ElasticSearchDataStore: " + System.lineSeparator() + attachClusterQuery);
			crossDataDriver.executeQuery(attachClusterQuery);
			logger.info("Cluster for ElasticSearchDataStore attached");
		} catch (ParsingException | ExecutionException | UnsupportedException
				| ValidationException e1) {
			logger.error(e1.toString());
		}
		try {
			logger.info("Attaching connector to ElasticSearchConnector: " + System.lineSeparator() + attachConnectorQuery);
			crossDataDriver.executeQuery(attachConnectorQuery);
			logger.info("Connector for ElasticSearchConnector attached.");
		} catch (ParsingException | ExecutionException | UnsupportedException
				| ValidationException e1) {
			logger.error(e1.toString());
		}

		// Procedemos a la creación del catalogo para los select y las inserciones correspondientes
		try {
			crossDataDriver.executeXqlScript("ElasticSearchConnector.xql");
			crossDataDriver
					.executeQuery(
							"CREATE TABLE IF NOT EXISTS catalogtest.paginationTable ON CLUSTER ClusterTest(id int,name text ,PRIMARY KEY(id));");
			logger.info("TABLE catalogtest.paginationTable created");
			for (int i = 1; i <= 50; i++) {
				crossDataDriver
						.executeQuery("INSERT INTO catalogtest.paginationTable(id, name) VALUES ("
								+ i + ", 'name_" + i + "');");
				logger.info("Row " + i + " inserted");
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		crossDataDriver.disconnect();
	}

	@AfterClass
	public void cleanUp() {
		// Procedemos al borrado del catalogo para los select y las inserciones
		// correspondientes
		try {
			crossDataDriver.connect("PRELOAD_USER_TEST");
		} catch (ConnectionException e1) {
			logger.error(e1.toString());
		}
		try {
			crossDataDriver.executeXqlScript("CassandraDeleteData.xql");
		} catch (Exception e) {
			logger.error(e.toString());
		}
		try {
			// Detach connector
			logger.info("Detaching cluster");
			crossDataDriver.executeQuery(detachConnectorQuery);
			logger.info("Cluster detached sucesfully");
			logger.info("Detaching connector");
			crossDataDriver.executeQuery(detachClusterQuery);
			logger.info("Connector detached sucesfully");
			crossDataDriver.disconnect();
		} catch (ParsingException | ExecutionException | UnsupportedException
				| ValidationException | ConnectionException e) {
			logger.error(e.toString());
		}
	}

	@Test(enabled = true)
	public void commonMetaTest() throws Exception {
		new CucumberRunner(this.getClass(), this.getDataProvider()).runCukes();
	}
}
