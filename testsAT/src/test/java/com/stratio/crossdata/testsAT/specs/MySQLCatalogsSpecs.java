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
package com.stratio.crossdata.testsAT.specs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    @When(value = "I execute a jdbc select '(.*?)'$")
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