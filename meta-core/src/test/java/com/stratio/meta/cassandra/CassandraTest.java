package com.stratio.meta.cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.exceptions.DriverException;
import static org.junit.Assert.*;

import org.junit.Test;

import com.stratio.meta.statements.MetaStatement;
import com.stratio.meta.utils.AntlrResult;
import com.stratio.meta.utils.MetaUtils;

public class CassandraTest extends BasicCassandraTest {                	      	
                        
    public MetaStatement testRegularStatement(String inputText, String methodName) {
        AntlrResult antlrResult = MetaUtils.parseStatement(inputText);
        MetaStatement st = antlrResult.getStatement();
        assertNotNull("Cannot parse "+methodName, st);             
        assertTrue("Cannot parse "+methodName, inputText.equalsIgnoreCase(st.toString()+";"));
        return st;
    }

    public ResultSet testDriverStatement(MetaStatement st, String methodName){
        Statement driverStmt = st.getDriverStatement();
        ResultSet resultSet;
        if(driverStmt != null){
            resultSet = CassandraClient.executeQuery(driverStmt, false);
        } else {
            resultSet = CassandraClient.executeQuery(st.translateToCQL(), false);
        }            
        assertNotNull("Cannot execute "+methodName+" in Cassandra", resultSet);
        return resultSet;
    }

    public ResultSet testStatementWithCassandra(String inputText, String methodName){
        MetaStatement st = testRegularStatement(inputText, methodName);
        return testDriverStatement(st, methodName);
    }

    public void testMetaError(String inputText){
        AntlrResult antlrResult = MetaUtils.parseStatement(inputText);
        MetaStatement st = antlrResult.getStatement();
        thrown.expect(NullPointerException.class);
        System.out.println(st.toString());
    }

    public void testRecoverableError(String inputText, String methodName){
        AntlrResult antlrResult = MetaUtils.parseStatement(inputText);
        MetaStatement st = antlrResult.getStatement();
        assertTrue("No errors reported in "+methodName, antlrResult.getFoundErrors().getNumberOfErrors()>0);
    }

    public void testCassandraError(MetaStatement st){
        Statement driverStmt = st.getDriverStatement();
        thrown.expect(DriverException.class); 
        CassandraClient.executeQuery(driverStmt, false);
    }        		                	                

    // CASSANDRA TESTS
    public void createKeyspace_basic() {
        String inputText = "CREATE KEYSPACE testKS WITH replication = {class: SimpleStrategy, replication_factor: 1}"
                + " AND durable_writes = false;";		
        AntlrResult antlrResult = MetaUtils.parseStatement(inputText);
        MetaStatement st = antlrResult.getStatement();
        assertNotNull("Cannot parse createKeyspace_basic", st);

        boolean originalOK = false;
        boolean alternative1 = false;

        if(inputText.equalsIgnoreCase(st.toString()+";")){
            originalOK = true;
        }

        String alternative1Str = "CREATE KEYSPACE testKS WITH replication = {replication_factor: 1, class: SimpleStrategy}"
                + " AND durable_writes = false;";
        if(alternative1Str.equalsIgnoreCase(st.toString()+";")){
            alternative1 = true;
        }

        assertTrue("Cannot parse createKeyspace_basic", (originalOK || alternative1));    

        testDriverStatement(st, "createKeyspace_basic");
    }

    public void createTable_basic_simple() {
        String inputText = "CREATE TABLE testKS.users(name varchar, password varchar, color varchar, gender varchar,"
                + " food varchar, animal varchar, age int, code int, PRIMARY KEY ((name, gender), color, animal));";
        ResultSet rs = testStatementWithCassandra(inputText, "createTable_basic_simple");
    }

    public void insert_into_simple_1() {
        String inputText = "INSERT INTO testKS.users (name, gender, color, animal, food, password, age, code)"
                + " VALUES(pepito, male, orange, whale, plants, efg, 12, 44);";
        ResultSet rs = testStatementWithCassandra(inputText, "insert_into_simple_1");
    }

    public void insert_into_simple_2() {
        String inputText = "INSERT INTO testKS.users (name, gender, color, animal, food, password, age, code) "
                + "VALUES(pepito, male, black, whale, plants, efg, 12, 44) IF NOT EXISTS USING TTL = 86400;"; 
        ResultSet rs = testStatementWithCassandra(inputText, "insert_into_simple_2");
    }

    public void insert_into_simple_3() {
        String inputText = "INSERT INTO testKS.users (name, gender, color, animal, food, password, age, code) "
                + "VALUES(pepita, female, blue, dog, meat, sdh, 5, 33);"; 
        ResultSet rs = testStatementWithCassandra(inputText, "insert_into_simple_3");
    }

    public void insert_into_simple_4() {
        String inputText = "INSERT INTO testKS.users (name, gender, color, animal, food, password, age, code) "
                + "VALUES(pepita, female, red, cat, mice, kdf, 3, 22);";
        ResultSet rs = testStatementWithCassandra(inputText, "insert_into_simple_4");
    }

    public void insert_into_simple_5() {
        String inputText = "INSERT INTO testKS.users (name, gender, color, animal, food, password, age, code) "
                + "VALUES(pepita, female, black, fish, plankton, dfp, 3, 11);"; 
        ResultSet rs = testStatementWithCassandra(inputText, "insert_into_simple_5");
    }

    public void insert_into_simple_6() {
        String inputText = "INSERT INTO testKS.users (name, gender, color, animal, food, password, age, code) "
                + "VALUES(pepito, male, green, cat, cookies, cas, 5, 55);";
        ResultSet rs = testStatementWithCassandra(inputText, "insert_into_simple_6");
    }

    public void select_all() {
        String inputText = "SELECT * FROM testKS.users;";
        ResultSet rs = testStatementWithCassandra(inputText, "select_all");
    }

    public void select_order() {
            String inputText = "SELECT name, gender, color, animal AS rage, age, food FROM testKS.users "
                    + "WHERE name = pepita AND gender = female ORDER BY color DESC LIMIT 2;";
            ResultSet rs = testStatementWithCassandra(inputText, "select_order");
    }

    public void select_count() {
            String inputText = "SELECT COUNT(*) FROM testKS.users;";
            ResultSet rs = testStatementWithCassandra(inputText, "select_count");
    }

    public void select_distinct() {
            String inputText = "SELECT DISTINCT name, gender FROM testKS.users;";
            ResultSet rs = testStatementWithCassandra(inputText, "select_distinct");
    }

    public void truncate_tableWithKeyspace() {
            String inputText = "TRUNCATE testKS.users;";
            ResultSet rs = testStatementWithCassandra(inputText, "truncate_tableWithKeyspace");
    }

    public void dropKeyspace_simple() {
            String inputText = "drop keyspace IF EXISTS testKS;";
            ResultSet rs = testStatementWithCassandra(inputText, "dropKeyspace_simple");
    }

    @Test
    public void test_cassandra_1(){
        createKeyspace_basic();
        createTable_basic_simple();
        insert_into_simple_1();
        insert_into_simple_2();
        insert_into_simple_3();
        insert_into_simple_4(); 
        insert_into_simple_5();
        insert_into_simple_6();
        select_all();
        select_order();
        select_count();
        select_distinct();
        truncate_tableWithKeyspace();
        dropKeyspace_simple();
    }

    // TEST CASSANDRA EXCEPTIONS
    public void insert_into_wrong_data_type(){   
        String inputText = "INSERT INTO testKS.users (name, gender, color, animal, food, password, age, code)"
                + " VALUES(pepito, male, black, whale, plants, efg, twelve, 44);";
        MetaStatement st = testRegularStatement(inputText, "insert_into_wrong_data_type");
        testCassandraError(st);                       
    }

    @Test
    public void test_cassandra_wrong_1(){   
        createKeyspace_basic();
        createTable_basic_simple();
        insert_into_wrong_data_type();
        dropKeyspace_simple();
    }        
        
}
