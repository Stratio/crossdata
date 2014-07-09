/*
 * Stratio Meta
 * 
 * Copyright (c) 2014, Stratio, All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library.
 */

package com.stratio.meta.core.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.stratio.meta.common.result.ErrorResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.core.parser.Parser;
import com.stratio.meta.test.CCMHandler;
import com.stratio.streaming.api.IStratioStreamingAPI;
import com.stratio.streaming.api.StratioStreamingAPIFactory;
import com.stratio.streaming.commons.exceptions.StratioEngineConnectionException;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertTrue;


public class BasicCoreCassandraTest {

  /**
   * Default Cassandra HOST using 127.0.0.1.
   */
  private static final String DEFAULT_HOST = "127.0.0.1";

  /**
   * Parsed used to interpret the testing queries.
   */
  protected final Parser parser = new Parser();

  /**
   * Session to launch queries on C*.
   */
  protected static Session _session = null;

  protected static IStratioStreamingAPI stratioStreamingAPI = null;

  /**
   * Class logger.
   */
  private static final Logger logger = Logger.getLogger(BasicCoreCassandraTest.class);

  @BeforeClass
  public static void setUpBeforeClass() {
    CCMHandler.startCCM();
    initCassandraConnection();
    dropKeyspaceIfExists("testKS");
    try {
      stratioStreamingAPI =
          StratioStreamingAPIFactory.create()
              .initializeWithServerConfig("127.0.0.1", 9092, "127.0.0.1", 2181);
    } catch (StratioEngineConnectionException e) {
      e.printStackTrace();
    }
  }

  @AfterClass
  public static void tearDownAfterClass() {
    dropKeyspaceIfExists("testKs");
    closeCassandraConnection();
  }

  /**
   * Establish the connection with Cassandra in order to be able to retrieve metadata from the
   * system columns.
   * 
   * @param host The target host.
   * @return Whether the connection has been established or not.
   */
  protected static boolean connect(String host) {
    boolean result = false;
    Cluster c = Cluster.builder().addContactPoint(host).build();
    _session = c.connect();
    result = null == _session.getLoggedKeyspace();
    return result;
  }

  private static String getHost() {
    return System.getProperty("cassandraTestHost", DEFAULT_HOST);
  }

  /**
   * Initialize the connection to Cassandra using the host specified by {@code DEFAULT_HOST}.
   */
  public static void initCassandraConnection() {
    assertTrue(connect(getHost()), "Cannot connect to cassandra");
  }

  /**
   * Close the Cassandra session.
   */
  public static void closeCassandraConnection() {
    _session.close();
  }

  /**
   * Drop a keyspace if it exists in the database.
   * 
   * @param targetKeyspace The target keyspace.
   */
  public static void dropKeyspaceIfExists(String targetKeyspace) {
    String query = "USE " + targetKeyspace;
    boolean ksExists = true;
    try {
      _session.execute(query);
    } catch (InvalidQueryException iqe) {
      logger.info("Invalid query exception; keyspace doesn't exist: " + iqe.getMessage());
      ksExists = false;
    }

    if (ksExists) {
      String q = "DROP KEYSPACE " + targetKeyspace;
      try {
        _session.execute(q);
      } catch (Exception e) {
        logger.error("Cannot drop keyspace: " + targetKeyspace, e);
      }
    }
  }

  /**
   * Load a {@code keyspace} in Cassandra using the CQL sentences in the script path. The script is
   * executed if the keyspace does not exist in Cassandra.
   * 
   * @param keyspace The name of the keyspace.
   * @param path The path of the CQL script.
   */
  protected static void loadTestData(String keyspace, String path) {
    KeyspaceMetadata metadata = _session.getCluster().getMetadata().getKeyspace(keyspace);
    if (metadata == null) {
      logger.info("Creating keyspace " + keyspace + " using " + path);
      List<String> scriptLines = loadScript(path);
      logger.info("Executing " + scriptLines.size() + " lines");
      for (String cql : scriptLines) {
        ResultSet result = _session.execute(cql);
        if (logger.isDebugEnabled()) {
          logger.debug("Executing: " + cql + " -> " + result.toString());
        }
      }
    }
    logger.info("Using existing keyspace " + keyspace);
  }

  /**
   * Load the lines of a CQL script containing one statement per line into a list.
   * 
   * @param path The path of the CQL script.
   * @return The contents of the script.
   */
  public static List<String> loadScript(String path) {
    List<String> result = new ArrayList<>();
    URL url = BasicCoreCassandraTest.class.getResource(path);
    logger.info("Loading script from: " + url);
    try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
      String line;
      while ((line = br.readLine()) != null) {
        if (line.length() > 0 && !line.startsWith("#")) {
          result.add(line);
        }
      }
    } catch (IOException e) {
      logger.error("IOException", e);
    }
    return result;
  }

  public static String getErrorMessage(Result metaResult) {
    String result = "Invalid class: " + metaResult.getClass();
    if (ErrorResult.class.isInstance(metaResult)) {
      result = ErrorResult.class.cast(metaResult).getErrorMessage();
    }
    return result;
  }

}
