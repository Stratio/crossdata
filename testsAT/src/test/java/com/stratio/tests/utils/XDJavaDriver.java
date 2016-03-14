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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.UUID;

import com.stratio.crossdata.common.SQLCommand;
import com.stratio.crossdata.common.result.SQLResult;
import com.stratio.crossdata.driver.JavaDriver;
import com.stratio.crossdata.driver.config.DriverConf;
import com.stratio.crossdata.driver.metadata.FieldMetadata;
import com.stratio.crossdata.driver.metadata.JavaTableName;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigList;
import com.typesafe.config.ConfigMergeable;
import com.typesafe.config.ConfigOrigin;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueFactory;
import com.typesafe.config.ConfigValueType;

public class XDJavaDriver {

    private JavaDriver xdDriver;
    private List<FieldMetadata> descTables;
    private List<JavaTableName> tableList;
    private List<String> databases;
    private SQLResult result;
    private List<String> seedNodes = Arrays.asList(System.getProperty("CROSSDATA_HOST","127.0.0.1:13420").split(","));
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
        if(xdDriver == null) {
            xdDriver = new JavaDriver(seedNodes);
        }
        descTables = xdDriver.describeTable(tableName);
    }

    public void listTables(){
        tableList = xdDriver.listTables();
    }

    public void listTables(String dataBaseName){
        tableList = xdDriver.listTables(dataBaseName);
    }

    public void executeSyncQuery(String sql){
        if(xdDriver == null) {
            xdDriver = new JavaDriver(seedNodes);
        }
        result = xdDriver.sql(sql);
//        xdDriver.close();
//        xdDriver = null;
    }

    public void executeflattenedSyncQuery(String sql){
        if(xdDriver == null) {
            DriverConf conf = new DriverConf();
            conf.setFlattenTables(true);
            conf.setClusterContactPoint(seedNodes);
            xdDriver = new JavaDriver(conf);

        }
        result = xdDriver.sql(sql);
        //        xdDriver.close();
        //        xdDriver = null;
    }

    public void close(){

        xdDriver.close();

    }
}
