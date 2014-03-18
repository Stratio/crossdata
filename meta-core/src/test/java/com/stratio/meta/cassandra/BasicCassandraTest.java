package com.stratio.meta.cassandra;

import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.exceptions.DriverException;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
    public static void setUpBeforeClass(){
        initCassandraConnection();
        checkKeyspaces();
    }

    public static void dropKeyspaces(){
        CassandraClient.executeQuery("DROP KEYSPACE IF EXISTS testKS;", false);
    }

    public static void closeCassandraConnection(){
        CassandraClient.close();
    }

    /**
     * Load a {@code keyspace} in Cassandra using the CQL sentences in the script
     * path. The script is executed if the keyspace does not exists in Cassandra.
     * @param keyspace The name of the keyspace.
     * @param path The path of the CQL script.
     */
    public static void loadTestData(String keyspace, String path){
        KeyspaceMetadata metadata = CassandraClient.getClusterMetadata().getKeyspace(keyspace);
        if(metadata == null){
            logger.info("Creating keyspace " + keyspace + " using " + path);
            List<String> scriptLines = loadScript(path);
            logger.info("Executing " + scriptLines.size() + " lines");
            for(String cql : scriptLines){
                CassandraClient.executeQuery(cql, true);
            }
        }
        logger.info("Using existing keyspace " + keyspace);
    }

    public static List<String> loadScript(String path){
        List<String> result = new ArrayList<>();
        URL url = BasicCassandraTest.class.getResource(path);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String line = null;
            while((line = br.readLine()) != null){
                if(line.length() > 0 && !line.startsWith("#")){
                    result.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @AfterClass
    public static void exit(){
        dropKeyspaces();
        closeCassandraConnection();
    }  
        
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
}
