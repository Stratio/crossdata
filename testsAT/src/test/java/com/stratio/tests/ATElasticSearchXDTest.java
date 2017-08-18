/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
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

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.stratio.qa.cucumber.testng.CucumberRunner;
import com.stratio.qa.utils.BaseTest;
import com.stratio.qa.utils.ThreadProperty;

import cucumber.api.CucumberOptions;

//Indicar feature
@CucumberOptions(features = {
       "src/test/resources/features/Elasticsearch/ElasticSearchSelectSimple.feature",
       "src/test/resources/features/Elasticsearch/ElasticSearchelectAnd.feature",
       "src/test/resources/features/Elasticsearch/ElasticSearchSelectINFilter.feature",
       "src/test/resources/features/Elasticsearch/ElasticSearchSelectEqualsFilter.feature",
       "src/test/resources/features/Elasticsearch/ElasticSearchSelectGreaterFilter.feature",
       "src/test/resources/features/Elasticsearch/ElasticSearchSelectGreaterEqualsFilter.feature",
       "src/test/resources/features/Elasticsearch/ElasticSearchSelectLessFilter.feature",
       "src/test/resources/features/Elasticsearch/ElasticSearchSelectLessEqualsFilter.feature",
       "src/test/resources/features/Udaf/Group_concat.feature",
       "src/test/resources/features/Elasticsearch/TemporaryViews.feature",
        "src/test/resources/features/Elasticsearch/Views.feature",
       "src/test/resources/features/Elasticsearch/DropViews.feature"
       //"src/test/resources/features/Elasticsearch/ElasticSearchInsertInto.feature"
})
public class ATElasticSearchXDTest extends BaseTest {
    private String elasticSearchCluster = System.getProperty("ES_CLUSTER", "elasticsearch");
    private String elasticSearchIP = System.getProperty("ES_NODE","127.0.0.1");
    Client client;
    private Settings settings = Settings.settingsBuilder()
            .put("cluster.name", elasticSearchCluster).build();
    public ATElasticSearchXDTest() {
	}

	@BeforeClass(groups = {"elasticsearch"})
	public void setUp() {
        String connector = "ElasticSearch";
        ThreadProperty.set("Connector", connector);
        try {
            client = TransportClient.builder().settings(settings).build()
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
        ThreadProperty.set("Driver", "context");
    }

	@AfterClass(groups = {"elasticsearch"})
	public void cleanUp() {
        try {
            client = TransportClient.builder().settings(settings).build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(elasticSearchIP), 9300));
          //  DeleteIndexResponse delete = client.admin().indices().delete(new DeleteIndexRequest("databasetest"))
          //          .actionGet();
           // System.out.println(response.toString());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        client.close();
	}

    @Test(enabled = true, groups = {"elasticsearch"})
    public void ATElasticSearchXDTest() throws Exception {
		new CucumberRunner(this.getClass()).runCukes();
	}

}
