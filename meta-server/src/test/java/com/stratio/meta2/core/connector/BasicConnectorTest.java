/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.meta2.core.connector;

import com.datastax.driver.core.Session;
import com.stratio.meta.common.result.ErrorResult;
import com.stratio.meta.common.result.Result;
import com.stratio.meta.test.CCMHandler;
import com.stratio.meta2.core.parser.Parser;

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


public class BasicConnectorTest {

  /**
   * Default Connector HOST using 127.0.0.1.
   */
  private static final String DEFAULT_HOST = "127.0.0.1";

  /**
   * Parsed used to interpret the testing queries.
   */
  protected final Parser parser = new Parser();

  /**
   * Class logger.
   */
  private static final Logger logger = Logger.getLogger(BasicConnectorTest.class);

  @BeforeClass
  public static void setUpBeforeClass() {
	/*
    CCMHandler.startCCM();
    initConnectorConnection();
    dropKeyspaceIfExists("testKS");
    */
  }

  @AfterClass
  public static void tearDownAfterClass() {
	/*
    dropKeyspaceIfExists("testKs");
    closeConnectorConnection();
    */
  }

  /**
   * Establish the connection with Connector in order to be able to retrieve metadata from the
   * system columns.
   * 
   * @param host The target host.
   * @return Whether the connection has been established or not.
   */
  protected static boolean connect(String host) {
	/*
    boolean result = false;
    Cluster c = Cluster.builder().addContactPoint(host).build();
    _session = c.connect();
    result = null == _session.getLoggedKeyspace();
    return result;
    */
	return true;
  }

  private static String getHost() {
    return System.getProperty("ConnectorTestHost", DEFAULT_HOST);
  }

  /**
   * Initialize the connection to Connector using the host specified by {@code DEFAULT_HOST}.
   */
  public static void initConnectorConnection() {
    assertTrue(connect(getHost()), "Cannot connect to Connector");
  }

  /**
   * Close the Connector session.
   */
  public static void closeConnectorConnection() {
    //_session.close();
  }

  /**
   * Drop a keyspace if it exists in the database.
   * 
   * @param targetKeyspace The target keyspace.
   */
  public static void dropKeyspaceIfExists(String targetKeyspace) {
	/*
    String query = "USE " + targetKeyspace;
    boolean ksExists = true;
    try {
      _session.execute(query);
    } catch (InvalidQueryException iqe) {
      ksExists = false;
    }

    if (ksExists) {
      String q = "DROP KEYSPACE " + targetKeyspace;
      try {
        _session.execute(q);
      } catch (Exception e) {
        logger.error("Cannot drop catalog: " + targetKeyspace, e);
      }
    }
    */
  }

  /**
   * Load a {@code keyspace} in Connector using the CQL sentences in the script path. The script is
   * executed if the keyspace does not exist in Connector.
   * 
   * @param keyspace The name of the keyspace.
   * @param path The path of the CQL script.
   */
  protected static void loadTestData(String keyspace, String path) {
	/*
    KeyspaceMetadata metadata = _session.getCluster().getMetadata().getKeyspace(keyspace);
    if (metadata == null) {
      logger.info("Creating catalog " + keyspace + " using " + path);
      List<String> scriptLines = loadScript(path);
      logger.info("Executing " + scriptLines.size() + " lines");
      for (String cql : scriptLines) {
        ResultSet result = _session.execute(cql);
        if (logger.isDebugEnabled()) {
          logger.debug("Executing: " + cql + " -> " + result.toString());
        }
      }
    }
    logger.info("Using existing catalog " + keyspace);
    */
  }

  /**
   * Load the lines of a CQL script containing one statement per line into a list.
   * 
   * @param path The path of the CQL script.
   * @return The contents of the script.
   */
  public static List<String> loadScript(String path) {
    List<String> result = new ArrayList<>();
    URL url = BasicConnectorTest.class.getResource(path);
    logger.info("Loading script from: " + url);
    try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
      String line;
      while ((line = br.readLine()) != null) {
        if (line.length() > 0 && !line.startsWith("#")) {
          result.add(line);
        }
      }
    } catch (IOException e) {
      logger.error(e.getStackTrace());
    }
    return result;
  }

  public static String getErrorMessage(Result metaResult){
    String result = "Invalid class: " + metaResult.getClass();
    if(ErrorResult.class.isInstance(metaResult)){
      result = ErrorResult.class.cast(metaResult).getErrorMessage();
    }
    return result;
  }

}
