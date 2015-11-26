/**
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

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import java.util.Date;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.mongodb.BasicDBObjectBuilder;
import com.stratio.cucumber.testng.CucumberRunner;
import com.stratio.tests.utils.BaseTest;
import com.stratio.tests.utils.CassandraUtils;
import com.stratio.tests.utils.ThreadProperty;
import java.text.ParseException;
import cucumber.api.CucumberOptions;

//Indicar feature
<<<<<<< HEAD
@CucumberOptions(features = { //"src/test/resources/features/Catalog/PersistentCatalogMySQL.feature",
		"src/test/resources/features/Catalog/PersistentCatalogMySQLDropTable.feature"
=======
@CucumberOptions(features = {// "src/test/resources/features/Catalog/PersistentCatalogMySQL.feature",
		"src/test/resources/features/Catalog/PersistentCatalogMySQLImportTables.feature"
>>>>>>> upstream/master
	})
public class ATPersistentCatalogXDTest extends BaseTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass()
			.getCanonicalName());
	//Defaul mongoHost property
	private String mongoHost = System.getProperty("MONGO_HOST", "127.0.0.1");
	//Default mongoPort property
	private String mongoPortString = System.getProperty("MONGO_PORT", "27017");
	private int mongoPort = Integer.parseInt(mongoPortString);
	private String dataBase = "databasetest";
	CassandraUtils cassandra = new CassandraUtils();
	private String catalog = "databasetest";
	// Global for C*
	private String cluster = System.getProperty("CASSANDRA_CLUSTER",
			"Test Cluster");
	private String host = System.getProperty("CASSANDRA_HOST", "127.0.0.1");
	private String sourceProvider = System.getProperty("SOURCE_PROVIDER",
			"com.stratio.crossdata.sql.sources.cassandra");
	public ATPersistentCatalogXDTest() {
	}

	@BeforeClass
	public void setUp() {
		ThreadProperty.set("Connector", "Persistence MYSQL");
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
		MongoClient mongoClient = null;
		try {
			mongoClient = new MongoClient(mongoHost, mongoPort);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		mongoClient.dropDatabase(dataBase);
		DB db = mongoClient.getDB(dataBase);
		DBCollection tabletest  = db.getCollection("tabletest");
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		format.setTimeZone(TimeZone.getTimeZone("CET"));
		for(int i = 0; i < 10; i++){
			Date parsedDate = null;
			String fecha = i + "/" + i + "/200" + i;
			try {
				parsedDate = format.parse(fecha);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			BasicDBObjectBuilder documentBuilder = BasicDBObjectBuilder.start()
					.add("ident", i)
					.add("name", "name_" + i)
					.add("money", 10.2 + i)
					.add("new", true)
					.add("date", new java.sql.Date(parsedDate.getTime()));
			tabletest.insert(documentBuilder.get());
		}
		mongoClient.close();
	}

	@AfterClass
	public void cleanUp() {
        MongoClient mongoClient = null;
        try {
            mongoClient = new MongoClient(mongoHost, mongoPort);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        mongoClient.dropDatabase(dataBase);
	}

	@Test(enabled = true)
	public void ATPersistentCatalogXDTest() throws Exception {
		new CucumberRunner(this.getClass()).runCukes();
	}

}
