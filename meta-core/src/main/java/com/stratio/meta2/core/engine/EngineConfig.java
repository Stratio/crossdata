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

package com.stratio.meta2.core.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

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
    private static final String[] FORBIDDEN_JARS = { "akka" };

    /**
     * Cassandra hosts.
     */
    private String[] cassandraHosts;

    /**
     * Cassandra port.
     */
    private int cassandraPort;

    /**
     * The Grid listen address.
     */
    private String gridListenAddress;

    /**
     * Grid hosts contact addresses.
     */
    private String[] gridContactHosts;

    /**
     * Cgrid port.
     */
    private int gridPort;

    /**
     * Grid initial members.
     */
    private int gridMinInitialMembers;

    /**
     * Grid join timeout.
     */
    private long gridJoinTimeout;

    /**
     * Grid files persistence path.
     */
    private String gridPersistencePath;

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

    public String getGridListenAddress() {
        return gridListenAddress;
    }

    public void setGridListenAddress(String gridListenAddress) {
        this.gridListenAddress = gridListenAddress;
    }

    public String[] getGridContactHosts() {
        return gridContactHosts.clone();
    }

    public void setGridContactHosts(String[] gridContactHosts) {
        this.gridContactHosts = Arrays.copyOf(gridContactHosts, gridContactHosts.length);
    }

    public int getGridPort() {
        return gridPort;
    }

    public void setGridPort(int gridPort) {
        this.gridPort = gridPort;
    }

    public int getGridMinInitialMembers() {
        return gridMinInitialMembers;
    }

    public void setGridMinInitialMembers(int gridMinInitialMembers) {
        this.gridMinInitialMembers = gridMinInitialMembers;
    }

    public long getGridJoinTimeout() {
        return gridJoinTimeout;
    }

    public void setGridJoinTimeout(long gridJoinTimeout) {
        this.gridJoinTimeout = gridJoinTimeout;
    }

    public String getGridPersistencePath() {
        return gridPersistencePath;
    }

    public void setGridPersistencePath(String gridPersistencePath) {
        this.gridPersistencePath = gridPersistencePath;
    }

    /**
     * Get Spark Master URL.
     *
     * @return Spark Master URL in a String.
     */
    public String getSparkMaster() {
        return sparkMaster;
    }

    /**
     * Set Spark Master URL.
     *
     * @param sparkMaster Spark Master URL spark://HOST:PORT/
     */
    public void setSparkMaster(String sparkMaster) {
        this.sparkMaster = sparkMaster;
    }

    /**
     * Get the default Job Name in Spark.
     *
     * @return the job name.
     */
    public String getJobName() {
        return JOBNAME;
    }

    /**
     * Set path which cointains spark classpath.
     *
     * @param path Path to classpath
     */
    public void setClasspathJars(String path) {
        jars = new ArrayList<>();
        File file = new File(path);
        if (file.exists() && !sparkMaster.toLowerCase().startsWith("local")
                && file.listFiles() != null) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (filterJars(f.getName())) {
                    jars.add(path + f.getName());
                }
            }
        } else if (!sparkMaster.toLowerCase().startsWith("local")) {
            LOG.error("Spark classpath null or incorrect directory.");
        }
    }

    /**
     * Check if a .jar is forbidden or not depending on {@link EngineConfig#FORBIDDEN_JARS}.
     *
     * @param jar .jar to check
     * @return {@code true} if is not forbidden.
     */
    private boolean filterJars(String jar) {
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EngineConfig{");
        sb.append("cassandraHosts=").append(Arrays.toString(cassandraHosts));
        sb.append(", cassandraPort=").append(cassandraPort);
        sb.append(", gridListenAddress='").append(gridListenAddress).append('\'');
        sb.append(", gridContactHosts=").append(Arrays.toString(gridContactHosts));
        sb.append(", gridPort=").append(gridPort);
        sb.append(", gridMinInitialMembers=").append(gridMinInitialMembers);
        sb.append(", gridJoinTimeout=").append(gridJoinTimeout);
        sb.append(", gridPersistencePath='").append(gridPersistencePath).append('\'');
        sb.append(", sparkMaster='").append(sparkMaster).append('\'');
        sb.append(", jars=").append(jars);
        sb.append(", kafkaServer='").append(kafkaServer).append('\'');
        sb.append(", kafkaPort=").append(kafkaPort);
        sb.append(", zookeeperServer='").append(zookeeperServer).append('\'');
        sb.append(", zookeeperPort=").append(zookeeperPort);
        sb.append(", streamingDuration=").append(streamingDuration);
        sb.append(", streamingGroupId='").append(streamingGroupId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
