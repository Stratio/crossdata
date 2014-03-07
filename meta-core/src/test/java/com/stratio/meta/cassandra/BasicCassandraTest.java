package com.stratio.meta.cassandra;

import com.datastax.driver.core.exceptions.DriverException;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class BasicCassandraTest {
    /**
     * Class logger.
     */
    private static final Logger logger = Logger.getLogger(CassandraTest.class);
    
    public static void initCassandraConnection(){
        try {
            CassandraClient.connect();
            logger.info("Connected to Cassandra");
        } catch(NoHostAvailableException ex){
            logger.error("\033[31mCannot connect with Cassandra\033[0m", ex);  
            System.exit(-1);
        }
    }
    
    public static void checkKeyspaces(){
        try {
            CassandraClient.executeQuery("USE testKS", false);
            logger.error("\033[31mKeyspace \'testKs\' already exists\033[0m");  
            System.exit(-1);
        } catch(DriverException ex){
            logger.info("Creating keyspace \'testKS\' in Cassandra for testing purposes");
        }
    }

    @BeforeClass
    public static void init(){
        initCassandraConnection();
        checkKeyspaces();
    }

    public static void dropKeyspaces(){
        CassandraClient.executeQuery("DROP KEYSPACE IF EXISTS testKS;", false);
    }

    public static void closeCassandraConnection(){
        CassandraClient.close();
    }

    @AfterClass
    public static void exit(){
        dropKeyspaces();
        closeCassandraConnection();
    }  
        
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
}
