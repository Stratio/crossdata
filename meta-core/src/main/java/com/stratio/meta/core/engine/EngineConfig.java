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
 */package com.stratio.meta.core.engine;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EngineConfig {

  /**
   * Class logger.
   */
  private static final Logger LOG = Logger.getLogger(EngineConfig.class.getName());

  /**
   * Name of the Job in Spark.
   */
  private static final String JOBNAME = "stratioDeepWithMeta";

  /**
   * Jars to exclude. Prefixes.
   */
  private static final String [] FORBIDDEN_JARS = {"akka"};

  /**
   * Cassandra hosts.
   */
  private String [] cassandraHosts;

  /**
   * Cassandra port.
   */
  private int cassandraPort;

  /**
   * Hazelcast hosts addresses.
   */
  private String[] hazelcastHosts;

  /**
   * Hazelcast port.
   */
  private int hazelcastPort;

  /**
   * Hazelcast map name.
   */
  private String hazelcastMapName;

  /**
   * Hazelcast map number of backups.
   */
  private int hazelcastMapBackups;

  /**
   * Hazelcast map max number of entities per node.
   */
  private int hazelcastMapSize;

  /**
   * Spark Master spark://HOST:PORT/.
   */
  private String sparkMaster;

  /**
   * List of *.jar in the classpath.
   */
  private List<String> jars;

  private String kafkaServer;
  private int kafkaPort;
  private String zookeeperServer;
  private int zookeeperPort;

  private int streamingDuration;
  private String streamingGroupId;


  /**
   * Get Cassandra hosts.
   *
   * @return an array of hosts
   */
  public String[] getCassandraHosts() {
    return cassandraHosts.clone();
  }

  /**
   * Set cassandra hosts.
   *
   * @param cassandraHosts an array of String containing cassandra hosts.
   */
  public void setCassandraHosts(String[] cassandraHosts) {
    this.cassandraHosts = Arrays.copyOf(cassandraHosts, cassandraHosts.length);
  }

  /**
   * Get cassandra port.
   *
   * @return current cassandra port.
   */
  public int getCassandraPort() {
    return cassandraPort;
  }

  /**
   * Set cassandra port.
   *
   * @param cassandraPort Port of cassandra (CQL).
   */
  public void setCassandraPort(int cassandraPort) {
    this.cassandraPort = cassandraPort;
  }

  public String[] getHazelcastHosts() {
    return hazelcastHosts.clone();
  }

  public void setHazelcastHosts(String[] hazelcastHosts) {
    this.hazelcastHosts = Arrays.copyOf(hazelcastHosts, hazelcastHosts.length);;
  }

  public int getHazelcastPort() {
    return hazelcastPort;
  }

  public void setHazelcastPort(int hazelcastPort) {
    this.hazelcastPort = hazelcastPort;
  }

  public String getHazelcastMapName() {
    return hazelcastMapName;
  }

  public void setHazelcastMapName(String hazelcastMapName) {
    this.hazelcastMapName = hazelcastMapName;
  }

  public int getHazelcastMapBackups() {
    return hazelcastMapBackups;
  }

  public void setHazelcastMapBackups(int hazelcastMapBackups) {
    this.hazelcastMapBackups = hazelcastMapBackups;
  }

  public int getHazelcastMapSize() {
    return hazelcastMapSize;
  }

  public void setHazelcastMapSize(int hazelcastMapSize) {
    this.hazelcastMapSize = hazelcastMapSize;
  }

  /**
   * Get Spark Master URL.
   *
   * @return Spark Master URL in a String.
   */
  public String getSparkMaster(){
    return sparkMaster;
  }

  /**
   * Set Spark Master URL.
   *
   * @param sparkMaster Spark Master URL spark://HOST:PORT/
   */
  public void setSparkMaster(String sparkMaster){
    this.sparkMaster=sparkMaster;
  }

  /**
   * Get the default Job Name in Spark.
   *
   * @return the job name.
   */
  public String getJobName(){
    return JOBNAME;
  }


  /**
   * Get cassandra host randomly.
   *
   * @return random cassandra host.
   */
  public String getRandomCassandraHost(){
    Random rand = new Random();
    return cassandraHosts[rand.nextInt(cassandraHosts.length)];
  }

  /**
   * Set List of paths to jars.
   *
   * @param jars List of paths.
   */
  public void setJars(List<String> jars){
    this.jars = jars;
  }

  /**
   * Get List of jars.
   *
   * @return list of paths, each point to one jar
   */
  public List<String> getJars(){
    return jars;
  }

  /**
   * Set path which cointains spark classpath.
   *
   * @param path Path to classpath
   */
  public void setClasspathJars(String path){
    jars = new ArrayList<>();
    File file = new File(path);
    if(file.exists() && !sparkMaster.toLowerCase().startsWith("local") && file.listFiles() != null) {
      File[] files = file.listFiles();
      for (File f : files) {
        if (filterJars(f.getName())) {
          jars.add(path + f.getName());
        }
      }
    }else if(!sparkMaster.toLowerCase().startsWith("local")){
      LOG.error("Spark classpath null or incorrect directory.");
    }
  }

  /**
   * Check if a .jar is forbidden or not depending on {@link EngineConfig#FORBIDDEN_JARS}.
   *
   * @param jar .jar to check
   * @return {@code true} if is not forbidden.
   */
  private boolean filterJars(String jar){
    for (String forbiddenJar : FORBIDDEN_JARS) {
      if (jar.startsWith(forbiddenJar)) {
        return false;
      }
    }
    return true;
  }

  public String getKafkaServer() {
    return kafkaServer;
  }

  public void setKafkaServer(String kafkaServer) {
    this.kafkaServer = kafkaServer;
  }

  public int getKafkaPort() {
    return kafkaPort;
  }

  public void setKafkaPort(int kafkaPort) {
    this.kafkaPort = kafkaPort;
  }

  public String getZookeeperServer() {
    return zookeeperServer;
  }

  public void setZookeeperServer(String zookeeperServer) {
    this.zookeeperServer = zookeeperServer;
  }

  public int getZookeeperPort() {
    return zookeeperPort;
  }

  public void setZookeeperPort(int zookeeperPort) {
    this.zookeeperPort = zookeeperPort;
  }

  public int getStreamingDuration() {
    return streamingDuration;
  }

  public void setStreamingDuration(int streamingDuration) {
    this.streamingDuration = streamingDuration;
  }

  public String getStreamingGroupId() {
    return streamingGroupId;
  }

  public void setStreamingGroupId(String streamingGroupId) {
    this.streamingGroupId = streamingGroupId;
  }
}
