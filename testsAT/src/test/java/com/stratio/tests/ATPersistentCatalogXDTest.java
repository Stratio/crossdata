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

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import java.util.Date;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
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

@CucumberOptions(features = { "src/test/resources/features/Catalog/PersistentCatalogMySQL.feature",
		"src/test/resources/features/Catalog/PersistentCatalogMySQLDropTable.feature",
		"src/test/resources/features/Catalog/PersistentCatalogMySQLImportTables.feature"
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
	private String elasticSearchCluster = System.getProperty("ELASTICSEARHC_CLUSTERNAME", "elasticsearch");
	private String elasticSearchIP = System.getProperty("ELASTICSEARCH_HOST","172.17.0.3");
	private Client client;
	private Settings settings = ImmutableSettings.settingsBuilder()
			.put("cluster.name", elasticSearchCluster).build();

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
		try {
			client = new TransportClient(settings)
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(elasticSearchIP), 9300));
			BulkRequestBuilder bulkRequest = client.prepareBulk();
			try {
				//Add tabletest
				SimpleDateFormat format_es = new SimpleDateFormat("yyyy-MM-dd");
				URL url = ATElasticSearchXDTest.class.getResource("/scripts/ElasticSearchData.data");
				XContentBuilder builder = null;

				try (BufferedReader br = new BufferedReader(new InputStreamReader(
						url.openStream(), "UTF8"))) {
					String line;
					for (int i = 0; i < 10 && (line = br.readLine()) != null; i++) {
						String [] lineArray = line.split(";");
						int aux = i + 1;
						int field_1 =  Integer.parseInt(lineArray[0]);
						Date parsed = null;
						try {
							parsed = format_es.parse(lineArray[4]);

						} catch (ParseException e) {
							e.printStackTrace();
						}
						IndexRequestBuilder res = client
								.prepareIndex("databasetest", "tabletest", String.valueOf(aux))
								.setSource(jsonBuilder()
												.startObject()
												.field("ident", field_1)
												.field("name", lineArray[1])
												.field("money", Double.parseDouble(lineArray[2]))
												.field("new", new Boolean(lineArray[3]))
												.field("date", new java.sql.Date(parsed.getTime()))
												.endObject()
								);
						bulkRequest.add(res);
					}
					bulkRequest.execute();

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		client.close();
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
		try {
			client = new TransportClient(settings)
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(elasticSearchIP), 9300));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		client.close();
	}

	@Test(enabled = true)
	public void ATPersistentCatalogXDTest() throws Exception {
		new CucumberRunner(this.getClass()).runCukes();
	}

}
