package com.stratio.crossdata.testsAT.automated.acceptance;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import com.stratio.crossdata.common.exceptions.ConnectionException;
import com.stratio.crossdata.common.exceptions.ExecutionException;
import com.stratio.crossdata.common.exceptions.ParsingException;
import com.stratio.crossdata.common.exceptions.UnsupportedException;
import com.stratio.crossdata.common.exceptions.ValidationException;
import com.stratio.crossdata.testsAT.automated.dataProvider.DataProviderInMemoryConnector;
import com.stratio.crossdata.testsAT.specs.clusters.AddDatastoreSpec;
import com.stratio.cucumber.testng.CucumberRunner;
import com.stratio.tests.utils.BaseTest;
import com.stratio.tests.utils.Driver;
import com.stratio.tests.utils.ThreadProperty;

public class InMemoryConnectorTest extends BaseTest {
	Driver crossDataDriver;
	private final Logger logger = LoggerFactory.getLogger(this.getClass()
			.getCanonicalName());
	private String attachClusterQuery = "ATTACH CLUSTER ClusterTest ON DATASTORE InMemoryDatastore WITH OPTIONS {'TableRowLimit':'10000'};";
	private String attachConnectorQuery = "ATTACH CONNECTOR InMemoryConnector TO ClusterTest WITH OPTIONS {};";
	private String detachClusterQuery = "DETACH CLUSTER ClusterTest;";
	private String detachConnectorQuery = "DETACH CONNECTOR InMemoryConnector FROM ClusterTest;";
	Process process;
	URL url_dat = AddDatastoreSpec.class.getResource("/manifest/"
			+ "InMemoryDataStore.xml");
	URL url_con = AddDatastoreSpec.class.getResource("/manifest/"
			+ "InMemoryConnector.xml");

	@Factory(dataProviderClass = DataProviderInMemoryConnector.class, dataProvider = "dataProvider")
	public InMemoryConnectorTest(String data) {
		this.setDataProvider(data);
	}

	@BeforeClass
	public void setUp() throws ConnectionException {
		ThreadProperty.set("ExecutionType", "Crossdata");
		crossDataDriver = new Driver();
		logger.info("Trying to connect with the crossDataServer");
		try {
			crossDataDriver.connect("PRELOAD_USER_TEST");
			logger.info("Connection succesfull");
		} catch (ConnectionException e) {
			logger.error("Connection fails");
		}
		// Limpiamos los restos
		crossDataDriver.cleanMetadata();
		logger.info("Adding new datastore xml file : "
				+ "InMemoryDatastore.xml");
//		try {
//		//	crossDataDriver.addDatastore(url_dat.getPath());
//		//	crossDataDriver.addConnector(url_con.getPath());
//			logger.info("Datastore added succesfully");
//		} catch (FileNotFoundException | JAXBException | ExistNameException | ApiException e) {
//			logger.error(e.toString());
//		}
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
		try {
			crossDataDriver.executeXqlScript("InmemoryConnector.xql");
			// Introducimos los datos para la paginacion
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
//			crossDataDriver
//					.executeQuery("DROP TABLE catalogTest.paginationTable;");
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
			//crossDataDriver.dropConnector("InMemoryConnector");
			//crossDataDriver.dropDatastore("InMemoryDatastore");
			crossDataDriver.disconnect();
		} catch (ParsingException | ExecutionException | UnsupportedException
				| ValidationException | ConnectionException e) {
			logger.error(e.toString());
		}
		// process.destroy();

	}

	@Test(enabled = true)
	public void InMemoryTests() throws Exception {
		new CucumberRunner(this.getClass(), this.getDataProvider()).runCukes();
	}

}