/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
package com.stratio.meta.driver;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ParentCassandraTest {
    /**
     * Default Cassandra HOST using 127.0.0.1.
     */
    private static final String DEFAULT_HOST = "127.0.0.1";


    /**
     * Session to launch queries on C*.
     */
    protected static Session session = null;

    /**
     * Class logger.
     */
    private static final Logger logger = Logger.getLogger(ParentCassandraTest.class);

    @BeforeClass
    public static void setUpBeforeClass(){
        try {
            File script = new File(ParentCassandraTest.class.getResource("/com/stratio/meta/test/test.sh").getPath());
            script.setExecutable(true);
            Process p = Runtime.getRuntime().exec(script.getAbsolutePath());
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            logger.error("Cannot execute ccm script", e);
        }
        initCassandraConnection();
        dropKeyspaceIfExists("testKS");
    }

    @AfterClass
    public static void tearDownAfterClass(){
        dropKeyspaceIfExists("testKs");
        closeCassandraConnection();
    }

    /**
     * Establish the connection with Cassandra in order to be able to retrieve
     * metadata from the system columns.
     * @param host The target host.
     * @return Whether the connection has been established or not.
     */
    protected static boolean connect(String host){
        boolean result = false;
        Cluster c = Cluster.builder().addContactPoint(host).build();
        session = c.connect();
        result = null == session.getLoggedKeyspace();
        return result;
    }

    private static String getHost(){
        return System.getProperty("cassandraTestHost", DEFAULT_HOST);
    }

    /**
     * Initialize the connection to Cassandra using the
     * host specified by {@code DEFAULT_HOST}.
     */
    public static void initCassandraConnection(){
        Assert.assertTrue(connect(getHost()), "Cannot connect to cassandra");
    }

    /**
     * Close the Cassandra session.
     */
    public static void closeCassandraConnection(){
        session.close();
    }

    /**
     * Drop a keyspace if it exists in the database.
     * @param targetKeyspace The target keyspace.
     */
    public static void dropKeyspaceIfExists(String targetKeyspace){
        String query = "USE " + targetKeyspace;
        boolean ksExists = true;
        try{
            session.execute(query);
        }catch (InvalidQueryException iqe){
            ksExists = false;
        }

        if(ksExists){
            String q = "DROP KEYSPACE " + targetKeyspace;
            try{
                session.execute(q);
            }catch (Exception e){
                logger.error("Cannot drop keyspace: " + targetKeyspace, e);
            }
        }
    }

    /**
     * Load a {@code keyspace} in Cassandra using the CQL sentences in the script
     * path. The script is executed if the keyspace does not exists in Cassandra.
     * @param keyspace The name of the keyspace.
     * @param path The path of the CQL script.
     */
    public static void loadTestData(String keyspace, String path){
        KeyspaceMetadata metadata = session.getCluster().getMetadata().getKeyspace(keyspace);
        if(metadata == null){
            logger.info("Creating keyspace " + keyspace + " using " + path);
            List<String> scriptLines = loadScript(path);
            logger.info("Executing " + scriptLines.size() + " lines");
            for(String cql : scriptLines){
                ResultSet result = session.execute(cql);
                if(logger.isDebugEnabled()){
                    logger.debug("Executing: " + cql + " -> " + result.toString());
                }
            }
        }
        logger.info("Using existing keyspace " + keyspace);
    }

    /**
     * Load the lines of a CQL script containing one statement per line
     * into a list.
     * @param path The path of the CQL script.
     * @return The contents of the script.
     */
    public static List<String> loadScript(String path){
        List<String> result = new ArrayList<>();
        URL url = ParentCassandraTest.class.getResource(path);
        logger.info("Loading script from: " + url);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String line;
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
}
