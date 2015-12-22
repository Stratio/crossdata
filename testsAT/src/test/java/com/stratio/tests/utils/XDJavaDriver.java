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
package com.stratio.tests.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.stratio.crossdata.common.SQLCommand;
import com.stratio.crossdata.common.SQLResult;
import com.stratio.crossdata.driver.JavaDriver;
import com.stratio.crossdata.driver.metadata.FieldMetadata;
import com.stratio.crossdata.driver.metadata.JavaTableName;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;

public class XDJavaDriver {

    private JavaDriver xdDriver;
    private List<FieldMetadata> descTables;
    private List<JavaTableName> tableList;
    private List<String> databases;
    private SQLResult result;

    public XDJavaDriver(){
   }

    public List<FieldMetadata> getTablesDescription(){
        return descTables;
    }

    public List<JavaTableName> getTablesList(){
        return tableList;
    }

    public List<String> getDatabasesList(){
        return databases;
    }

    public SQLResult getResult(){
        return result;
    }

    public void clearResults(){
        descTables.clear();
        tableList.clear();
        result = null;
        databases.clear();

    }

    public void describeTables(String dataBaseName, String tableName){
        descTables = xdDriver.describeTable(dataBaseName,tableName);
    }

    public void describeTables(String tableName){
        descTables = xdDriver.describeTable(tableName);
    }

    public void listTables(){
        tableList = xdDriver.listTables();
    }

    public void listTables(String dataBaseName){
        tableList = xdDriver.listTables(dataBaseName);
    }

    public void listDatabases(){
        databases = xdDriver.listDatabases();
    }

    public void executeSyncQuery(String sql){
        if(xdDriver == null) {
           //List<String> hosts = Arrays.asList(System.getProperty("CROSSDATA_HOST", "127.0.0.1").split(","));
           //xdDriver = new JavaDriver(hosts, false);
           xdDriver = new JavaDriver();
        }
        result = xdDriver.syncQuery(new SQLCommand(sql, UUID.randomUUID()));
    }


//    crossdata-driver.akka.remote.netty.tcp.hostname = "10.90.0.100"
//    crossdata-driver.config.cluster.hosts = ["10.90.0.101:13420","10.90.0.102:13420","10.90.0.103:13420"]
//    crossdata-driver.config.retry.times = 2
//    crossdata-driver.config.retry.duration = 40s

}
