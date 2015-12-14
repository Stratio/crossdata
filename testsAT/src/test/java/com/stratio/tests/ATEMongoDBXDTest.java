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
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import org.testng.annotations.Test;
import cucumber.api.CucumberOptions;
import com.mongodb.BasicDBObjectBuilder;
import com.stratio.tests.utils.BaseTest;
import com.stratio.cucumber.testng.CucumberRunner;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.stratio.tests.utils.ThreadProperty;


@CucumberOptions(features = { "src/test/resources/features/Mongo/MongoSelectSimple.feature",
        "src/test/resources/features/Mongo/MongoSelectLimit.feature",
        "src/test/resources/features/Mongo/MongoSelectEqualsFilter.feature",
        "src/test/resources/features/Mongo/MongoSelectLessFilter.feature",
        "src/test/resources/features/Mongo/MongoSelectLessEqualsFilter.feature",
        "src/test/resources/features/Mongo/MongoSelectGreaterFilter.feature",
        "src/test/resources/features/Mongo/MongoSelectGreaterEqualsFilter.feature",
        "src/test/resources/features/Mongo/MongoSelectINFilter.feature",
        "src/test/resources/features/Mongo/MongoSelectAnd.feature",
        "src/test/resources/features/Mongo/MongoSelectNOTBetween.feature",
          "src/test/resources/features/Udaf/Group_concat.feature"
})

public class ATEMongoDBXDTest extends BaseTest{

    //Defaul mongoHost property
    private String mongoHost = System.getProperty("MONGO_HOST", "127.0.0.1");
    //Default mongoPort property
    private String mongoPortString = System.getProperty("MONGO_PORT", "27017");
    private int mongoPort = Integer.parseInt(mongoPortString);
    private String dataBase = "databasetest";
    @BeforeClass
    public void setUp() throws UnknownHostException{
        MongoClient mongoClient = new MongoClient(mongoHost, mongoPort);
        mongoClient.dropDatabase(dataBase);
        DB db = mongoClient.getDB(dataBase);
        DBCollection tabletest  = db.getCollection("tabletest");
       // DBCollection tabletest = db.createCollection("tabletest");
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
        String connector = "Mongo";
        ThreadProperty.set("Host", "127.0.0.1");
        ThreadProperty.set("Connector", connector);

    }
    @AfterClass
    public void cleanUp() throws UnknownHostException{
        MongoClient mongoClient = new MongoClient(mongoHost, mongoPort);
        mongoClient.dropDatabase(dataBase);
    }

    @Test(enabled = true)
    public void ATMongoDBXDTest() throws Exception{
        new CucumberRunner(this.getClass()).runCukes();
    }
}
