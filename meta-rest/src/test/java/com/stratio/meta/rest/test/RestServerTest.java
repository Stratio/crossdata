/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta.rest.test;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.stratio.meta.common.data.Row;
import com.stratio.meta.common.metadata.structures.ColumnMetadata;
import com.stratio.meta.rest.Main;
import com.stratio.meta.rest.models.ResultSet;
import com.stratio.meta.rest.utils.DriverHelper;
import com.stratio.meta.rest.utils.RestServerTestUtils;
import com.stratio.meta2.common.metadata.ColumnType;

public class RestServerTest {

    DriverHelper driver = DriverHelper.getInstance();
    ArrayList<ColumnMetadata> expectedColumnMetadata;
    ArrayList<Row> expectedRowList;
    private HttpServer server;
    private WebTarget target;
    private Client c;
    private ResultSet expectedRs;

    @Before
    public void setUp() throws Exception {
        // start the server
        server = Main.startServer();
        // create the client
        c = ClientBuilder.newClient();
        // uncomment the following line if you want to enable
        // support for JSON in the client (you also have to uncomment
        // dependency on jersey-media-json module in pom.xml and Main.startServer())
        // --
        driver.connect();
        target = c.target(Main.BASE_URI);

        //cassandra = new CassandraHandler(CassandraHandler.MY_HOST_IP);
        //
        // // CREATE KEYSPACE, TABLE AND INDEX in cassandra
        //cassandra.loadTestData("selectdemo", "/restServer.cql");

        expectedRowList = new ArrayList<>();
        expectedColumnMetadata = new ArrayList<>();

        ColumnMetadata cm1 = new ColumnMetadata("users", "name", ColumnType.VARCHAR);
        ColumnMetadata cm2 = new ColumnMetadata("users", "gender", ColumnType.VARCHAR);
        ColumnMetadata cm3 = new ColumnMetadata("users", "email", ColumnType.VARCHAR);
        ColumnMetadata cm4 = new ColumnMetadata("users", "age", ColumnType.INT);
        ColumnMetadata cm5 = new ColumnMetadata("users", "bool", ColumnType.BOOLEAN);
        ColumnMetadata cm6 = new ColumnMetadata("users", "phrase", ColumnType.VARCHAR);

        expectedColumnMetadata.add(cm1);
        expectedColumnMetadata.add(cm2);
        expectedColumnMetadata.add(cm3);
        expectedColumnMetadata.add(cm4);
        expectedColumnMetadata.add(cm5);
        expectedColumnMetadata.add(cm6);

        // HashMap<String, ColumnDefinition> columnsMap = new HashMap<String, ColumnDefinition>();
        // columnsMap.clear();
        // columnsMap.put("name", new ColumnDefinition(String.class));
        // columnsMap.put("gender", new ColumnDefinition(String.class));
        // columnsMap.put("email", new ColumnDefinition(String.class));
        // columnsMap.put("age", new ColumnDefinition(Integer.class));
        // columnsMap.put("bool", new ColumnDefinition(Boolean.class));
        // columnsMap.put("phrase", new ColumnDefinition(String.class));

        for (int i = 0; i < 10; i++) {
            expectedRowList.add(RestServerTestUtils.createDemoRow("name_" + i, "male", "name_" + i
                    + "@domain.com", 10 + i, true, ""));
        }
        //
        // expectedRs = RestServerTestUtils.toResultSet(expectedRowList, columnsMap);
    }

    @After
    public void tearDown() throws Exception {

        //cassandra.executeQuery("DROP KEYSPACE IF EXISTS selectdemo;");
        //cassandra.disconnect();
        driver.close();
        server.stop();
    }

    /**
     * Test to see that the message "META REST Server up!" is sent in the response.
     */
    @Test
    public void testServerUp() {
        String responseMsg = target.path("").request().get(String.class);
        assertEquals("META REST Server up!", responseMsg);
    }

    @Test
    public void testGetAndPostCommandResult() throws IOException {
        Form formData = new Form();
        formData.param("query", "DESCRIBE KEYSPACES;");
        formData.param("catalog", "selectdemo");
        Response response =
                target.path("api").request()
                        .post(Entity.entity(formData, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
        assertEquals(200, response.getStatus());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        InputStreamReader is = new InputStreamReader((InputStream) response.getEntity());
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(is);
        String read = br.readLine();

        while (read != null) {
            sb.append(read);
            read = br.readLine();

        }

        String result = sb.toString();
        System.out.println(result);

        String responseGet = target.path("api/query/" + result).request().get(String.class);
        assertEquals("{\"result\":\"[selectdemo, system_traces, system]\",\"queryId\":\"" + result
                + "\"}", responseGet);

    }

    @Test
    public void testGetAndPostQueryResult() throws IOException {
        Form formData = new Form();
        formData.param("query", "SELECT * FROM selectdemo.users;");
        formData.param("catalog", "selectdemo");
        Response response =
                target.path("api").request()
                        .post(Entity.entity(formData, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
        assertEquals(200, response.getStatus());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        InputStreamReader is = new InputStreamReader((InputStream) response.getEntity());
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(is);
        String read = br.readLine();

        while (read != null) {
            sb.append(read);
            read = br.readLine();

        }

        String result = sb.toString();
        System.out.println(result);
        String responseGet = target.path("api/query/" + result).request().get(String.class).trim();

        //Gson gson = new Gson();
        //Result rs = gson.fromJson(responseGet, Result.class);
        // System.out.println("GSON RS");
        // System.out.println(rs.getResultSet().toStringCustom());
        // QueryResult qr;

        // String expected =
        // "{\"catalogChanged\":false,\"currentCatalog\":null,\"lastResultSet\":false,\"resultSet\":{\"rows\":[{\"cells\":{\"name\":{\"value\":\"name_1\"},\"gender\":{\"value\":\"male\"},\"email\":{\"value\":\"name_1@domain.com\"},\"age\":{\"value\":11},\"bool\":{\"value\":true},\"phrase\":{\"value\":\"\"}},\"cellList\":[{\"value\":\"name_1\"},{\"value\":\"male\"},{\"value\":\"name_1@domain.com\"},{\"value\":11},{\"value\":true},{\"value\":\"\"}]},{\"cells\":{\"name\":{\"value\":\"name_0\"},\"gender\":{\"value\":\"male\"},\"email\":{\"value\":\"name_0@domain.com\"},\"age\":{\"value\":10},\"bool\":{\"value\":true},\"phrase\":{\"value\":\"\"}},\"cellList\":[{\"value\":\"name_0\"},{\"value\":\"male\"},{\"value\":\"name_0@domain.com\"},{\"value\":10},{\"value\":true},{\"value\":\"\"}]}],\"columnMetadata\":[{\"columnNameToShow\":\"name\",\"columnAlias\":null,\"tableName\":\"users\",\"columnName\":\"name\",\"type\":\"VARCHAR\"},{\"columnNameToShow\":\"gender\",\"columnAlias\":null,\"tableName\":\"users\",\"columnName\":\"gender\",\"type\":\"VARCHAR\"},{\"columnNameToShow\":\"email\",\"columnAlias\":null,\"tableName\":\"users\",\"columnName\":\"email\",\"type\":\"VARCHAR\"},{\"columnNameToShow\":\"age\",\"columnAlias\":null,\"tableName\":\"users\",\"columnName\":\"age\",\"type\":\"INT\"},{\"columnNameToShow\":\"bool\",\"columnAlias\":null,\"tableName\":\"users\",\"columnName\":\"bool\",\"type\":\"BOOLEAN\"},{\"columnNameToShow\":\"phrase\",\"columnAlias\":null,\"tableName\":\"users\",\"columnName\":\"phrase\",\"type\":\"VARCHAR\"}],\"empty\":false},\"resultPage\":0,\"queryId\":\""
        // + result + "\"}";

        // assertTrue(RestServerTestUtils.assertEqualsResultSets(rs, expectedRs));
        //assertEquals(3676, responseGet.length());
        //assertEquals(expectedColumnMetadata.toString(), rs.getResultSet().getColumnMetadata()
        //    .toString());
        //assertEquals(expectedRowList.size(), rs.getResultSet().getRows().size());
        // System.out.println("GSON RS");
        // System.out.println(expectedRs.toStringCustom());
        // assertEquals(expectedRs.getColumnMetadata(), rs.getResultSet().getColumnMetadata());
        // assertTrue(RestServerTestUtils.assertEqualsResultSets(expectedRs, rs.getResultSet()));
    }

}
