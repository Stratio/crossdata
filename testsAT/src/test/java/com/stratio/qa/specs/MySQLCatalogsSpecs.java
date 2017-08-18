/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.qa.specs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by hdominguez on 13/10/15.
 */
public class MySQLCatalogsSpecs extends BaseSpec  {
    private static final String JDBC_DRIVER = System.getProperty("JDBC_MYSQL_DRIVER","org.mariadb.jdbc.Driver");
    private static  String PERSISTENCE_IP = System.getProperty("PERSISTENCE_IP","172.17.0.2");
    private static String PERSISTENCE_PORT = System.getProperty("PERSISTENCE_PORT","3306");

    private static final String DB_URL = "jdbc:mysql://"+PERSISTENCE_IP +":"+ PERSISTENCE_PORT +"/crossdata";
    private static final String USER = System.getProperty("USER", "root");
    private static final String PASS = System.getProperty("PASS", "stratio");
    private Connection con = null;
    private Statement statement = null;
    private ResultSet resultSet = null;
    private int resultObtained = 0;
    public MySQLCatalogsSpecs(Common spec) {
        this.commonspec = spec;
    }


    @When(value= "^I import tables using api for '(.*?)'")
    public void importTablesUsingApi(String datasource, DataTable table){
        commonspec.getExceptions().clear();
        commonspec.getLogger().info("Import tables for " + datasource);
        HashMap<String,String> options = new HashMap<String, String>();
        for(int i = 0; i < table.raw().size(); i++){
            options.put(table.raw().get(i).get(0), table.raw().get(i).get(1));
        }
        try {
            commonspec.getXdContext().importTables(datasource, options);
        }catch (Exception e) {
            commonspec.getLogger().info(e.toString());
            commonspec.getExceptions().add(e);
        }
        commonspec.getLogger().info("Tables imported succesfully for " + datasource);
    }


    @When(value = "^I execute a jdbc select '(.*?)'$")
    public void executeJDBCMYSQL(String sqlJDBC) {
        commonspec.getExceptions().clear();
        commonspec.getLogger().info("Execute over mysql this query: " + sqlJDBC);
        try {
            con = DriverManager.getConnection(DB_URL, USER,PASS);
            statement = con.createStatement();
            resultSet = statement.executeQuery(sqlJDBC);
            while (resultSet.next()) {
                resultObtained = resultSet.getInt(1);
            }
        }catch(SQLException e){
            commonspec.getExceptions().add(e);
        }
        finally {
            close();
        }
        commonspec.getLogger().info("Query Executed correctly");

     }

    @Then(value = "^The result has to be '(.*?)'$")
    public void checkResult(String resultExpected){
        assertThat(Integer.parseInt(resultExpected)).isEqualTo(resultObtained);
    }
    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (con != null) {
                con.close();
            }
        } catch (Exception e) {

        }
    }

}