package com.stratio.qa.utils;

import java.util.Arrays;
import java.util.List;

import com.stratio.crossdata.common.result.SQLResult;
import com.stratio.crossdata.driver.JavaDriver;
import com.stratio.crossdata.driver.config.DriverConf;
import com.stratio.crossdata.driver.metadata.FieldMetadata;
import com.stratio.crossdata.driver.metadata.JavaTableName;


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

 }
