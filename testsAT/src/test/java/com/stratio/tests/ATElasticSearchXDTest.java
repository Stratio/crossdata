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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.stratio.cucumber.testng.CucumberRunner;
import com.stratio.tests.utils.BaseTest;
import com.stratio.tests.utils.ThreadProperty;

import cucumber.api.CucumberOptions;

//Indicar feature
@CucumberOptions(features = { //"src/test/resources/features/Elasticsearch/ElasticSearchSelectSimple.feature",
//        "src/test/resources/features/Elasticsearch/ElasticSearchelectAnd.feature",
       // "src/test/resources/features/Elasticsearch/ElasticSearchSelectINFilter.feature"
         "src/test/resources/features/Elasticsearch/ElasticSearchSelectEqualsFilter.feature"})
public class ATElasticSearchXDTest extends BaseTest {
	private String elasticSearchCluster = System.getProperty("ELASTICSEARHC_CLUSTERNAME", "elasticsearch");
    private String elasticSearchIP = System.getProperty("ELASTICSEARCH_HOST","172.17.0.2");
    Client client;
    private Settings settings = ImmutableSettings.settingsBuilder()
            .put("cluster.name", elasticSearchCluster).build();
    public ATElasticSearchXDTest() {
	}

	@BeforeClass
	public void setUp() {
        String connector = "ElasticSearch";
        ThreadProperty.set("Connector", connector);
        try {
            client = new TransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(elasticSearchIP), 9300));
            BulkRequestBuilder bulkRequest = client.prepareBulk();
            try {
                //Add tabletest
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
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
                                parsed = format.parse(lineArray[4]);

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
                                                    .field("date", new java.sql.Timestamp(parsed.getTime()))
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
        try {
            client = new TransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(elasticSearchIP), 9300));
            DeleteIndexResponse delete = client.admin().indices().delete(new DeleteIndexRequest("databasetest"))
                    .actionGet();

           // System.out.println(response.toString());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        client.close();
	}

	@Test(enabled = true)
	public void ATElasticSearchXDTest() throws Exception {
		new CucumberRunner(this.getClass()).runCukes();
	}

}
