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
import java.util.List;
import java.util.TimeZone;

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
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.stratio.cucumber.testng.CucumberRunner;
import com.stratio.tests.utils.BaseTest;
import com.stratio.tests.utils.CassandraUtils;
import com.stratio.tests.utils.ThreadProperty;

import cucumber.api.CucumberOptions;

@CucumberOptions(features = { "src/test/resources/features/Catalog/CreateExternalTables.feature"
	})
public class ATCreateExternalTable extends BaseTest {


	public ATCreateExternalTable() {
	}

	@BeforeClass
	public void setUp() {
		ThreadProperty.set("Driver", "context");
		ThreadProperty.set("Connector", "external");
	}

	@AfterClass
	public void cleanUp() {

	}

	@Test(enabled = true, groups = {"basic"})
	public void ATCreateExternalTable() throws Exception {
		new CucumberRunner(this.getClass()).runCukes();
	}

}
