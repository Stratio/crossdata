package com.stratio.crossdata.testsAT.automated.acceptance;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.exceptions.ApiException;
import com.stratio.crossdata.common.exceptions.ConnectionException;
import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.exceptions.ManifestException;
import com.stratio.crossdata.common.exceptions.ParsingException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.common.exceptions.validation.ExistNameException;
import com.stratio.crossdata.dataProvider.DataProviderCassandraConnector;
import com.stratio.cucumber.testng.CucumberRunner;
import com.stratio.tests.utils.BaseTest;
import com.stratio.tests.utils.CassandraUtils;
import com.stratio.tests.utils.Driver;
import com.stratio.tests.utils.ThreadProperty;

public class CassandraConnectorTests extends BaseTest {
	Driver crossDataDriver;
	private String cassandraHost = System.getProperty("cassandraHost",
			"127.0.0.1");
	// "10.200.0.114");
	private String cassandraPort = System.getProperty("cassandraPort", "9042");
	private final Logger logger = LoggerFactory.getLogger(this.getClass()
			.getCanonicalName());
	private String attachClusterQuery = "ATTACH CLUSTER ClusterTest ON DATASTORE Cassandra WITH OPTIONS {'Hosts':'["
			+ cassandraHost + "]', 'Port':" + cassandraPort + "};";
	private String attachConnectorQuery = "ATTACH CONNECTOR CassandraConnector TO ClusterTest WITH OPTIONS {'DefaultLimit': '1000'} AND PAGINATION = 40;";
																								
	private String detachClusterQuery = "DETACH CLUSTER ClusterTest;";
	private String detachConnectorQuery = "DETACH CONNECTOR CassandraConnector FROM ClusterTest;";
	Process process;
	URL url_dat = CassandraConnectorTests.class.getResource("/manifest/"
			+ "CassandraDataStore.xml");
	URL url_con = CassandraConnectorTests.class.getResource("/manifest/"
			+ "CassandraConnector.xml");

	@Factory(dataProviderClass = DataProviderCassandraConnector.class, dataProvider = "dataProvider")
	public CassandraConnectorTests(String data) {
		this.setDataProvider(data);
	}

	@BeforeClass
	public void setUp() throws ConnectionException {
		ThreadProperty.set("ExecutionType", "Crossdata");
		logger.info("Deleting previous data in C*");
		CassandraUtils cassandra = new CassandraUtils();
		cassandra.connect();
		if (cassandra.existsKeyspace("catalogTest", false)) {
			logger.info("The catalog exists");
			cassandra.executeQuery("DROP KEYSPACE \"catalogTest\";");
			logger.info("The catalog has benn dropped");
		}
		try {
			cassandra.disconnect();
		} catch (Exception e) {
			logger.error(e.toString());
		}
		crossDataDriver = new Driver();
		logger.info("Trying to connect with the crossDataServer");
		try {
			crossDataDriver.connect("PRELOAD_USER_TEST");
			logger.info("Connection succesfull");
		} catch (ConnectionException e) {
			logger.error("Connection fails");
		}
		// Limpiamos los restos
		crossDataDriver.resetMetadata();
		logger.info("Adding new datastore xml file : "
				+ "CassandraDataStore.xml");
		try {
			crossDataDriver.addDatastore(url_dat.getPath());
			crossDataDriver.addConnector(url_con.getPath());
			logger.info("Datastore added succesfully");
		} catch (FileNotFoundException | JAXBException | ExistNameException | ApiException e) {
			logger.error(e.toString());
		}
		try {
			logger.info("Attaching cluster");
			crossDataDriver.executeQuery(attachClusterQuery);
			logger.info("Cluster attached sucesfully");
			logger.info("Attaching connector");
			crossDataDriver.executeQuery(attachConnectorQuery);
			logger.info("Connector attached sucesfully");
		} catch (ConnectionException | ParsingException | ExecutionException
				| UnsupportedException | ValidationException e) {
			logger.error(e.toString());
		}
		// Procedemos la creacion del catalogo para los select y las inserciones
		// correspondientes
		try {
			crossDataDriver.executeXqlScript("CassandraData.xql");
			crossDataDriver
					.executeQuery("CREATE TABLE IF NOT EXISTS catalogTest.paginationTable ON CLUSTER ClusterTest(id int,name text ,PRIMARY KEY(id));");
			for (int i = 1; i <= 50; i++) {
				crossDataDriver
						.executeQuery("INSERT INTO catalogTest.paginationTable(id, name) VALUES ("
								+ i + ", 'name_" + i + "');");
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
			crossDataDriver.dropConnector("CassandraConnector");
			crossDataDriver.dropDatastore("Cassandra");
			crossDataDriver.disconnect();
		} catch (ParsingException | ExecutionException | UnsupportedException
				| ValidationException | ConnectionException | ApiException e) {
			logger.error(e.toString());
		}
	}

	@Test(enabled = true)
	public void commonMetaTest() throws Exception {
		new CucumberRunner(this.getClass(), this.getDataProvider()).runCukes();
	}

}
