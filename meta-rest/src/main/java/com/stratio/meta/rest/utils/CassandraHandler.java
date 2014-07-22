package com.stratio.meta.rest.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class CassandraHandler {

  private static final Logger LOGGER = Logger.getLogger(CassandraHandler.class);

  // public static final String MY_HOST_IP = "172.19.0.169";
  // public static final String MY_HOST_IP = "10.200.0.5";
  public static final String MY_HOST_IP = "127.0.0.1";

  private Cluster cluster;

  private final String host;

  private Metadata metadata;

  private static Session session;

  public CassandraHandler(String host) {
    this.host = host;
    buildCluster();
    metadata = cluster.getMetadata();
    LOGGER.debug("Connected to cluster (" + host + "): " + metadata.getClusterName() + "\n");
    session = cluster.connect();

  }

  public ResultSet executeQuery(String query) {

    return session.execute(query);
  }

  public void executeQueriesList(List<String> queriesList) {

    for (String query : queriesList) {
      session.execute(query);
    }
  }

  public void reconnect() {
    metadata = cluster.getMetadata();
    LOGGER.debug("Connected to cluster (" + host + "): " + metadata.getClusterName() + "\n");
    session = cluster.connect();
  }

  public void disconnect() {
    session.close();
  }

  public Metadata getMetadata() {
    metadata = cluster.getMetadata();
    return metadata;
  }

  public void buildCluster() {
    this.cluster = Cluster.builder().addContactPoint(host).build();
    this.cluster.getConfiguration().getQueryOptions().setConsistencyLevel(ConsistencyLevel.ONE);

  }

  public Session getSession() {
    return session;
  }


  /**
   * Load a {@code keyspace} in Cassandra using the CQL sentences in the script path. The script is
   * executed if the keyspace does not exists in Cassandra.
   * 
   * @param keyspace The name of the keyspace.
   * @param path The path of the CQL script.
   */
  public void loadTestData(String keyspace, String path) {
    KeyspaceMetadata metadata = session.getCluster().getMetadata().getKeyspace(keyspace);
    if (metadata == null) {
      LOGGER.info("Creating keyspace " + keyspace + " using " + path);
      List<String> scriptLines = loadScript(path);
      LOGGER.info("Executing " + scriptLines.size() + " lines");
      for (String cql : scriptLines) {
        ResultSet result = session.execute(cql);
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("Executing: " + cql + " -> " + result.toString());
        }
      }
    }
    LOGGER.info("Using existing keyspace " + keyspace);
  }

  /**
   * Load the lines of a CQL script containing one statement per line into a list.
   * l
   * @param path The path of the CQL script.
   * @return The contents of the script.
   */
  public static List<String> loadScript(String path) {
    List<String> result = new ArrayList<String>();
    URL url = CassandraHandler.class.getResource(path);
    LOGGER.debug(url.toString());
    LOGGER.info("Loading script from: " + url);
    try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
      String line;
      while ((line = br.readLine()) != null) {
        if (line.length() > 0 && !line.startsWith("#")) {
          result.add(line);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

}
