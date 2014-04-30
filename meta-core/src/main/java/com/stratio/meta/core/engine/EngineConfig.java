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

package com.stratio.meta.core.engine;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
     *
     */
    private static String [] forbiddenJars = {"akka"};

    /**
     * Cassandra hosts.
     */
    private String [] cassandraHosts;

    /**
     * Cassandra port.
     */
    private int cassandraPort;

    /**
     * Spark Master spark://HOST:PORT/.
     */
    private String sparkMaster;

    /**
     * List of *.jar in the classpath.
     */
    private List<String> jars;

    /**
     * Get Cassandra hosts.
     * @return an array of hosts
     */
    public String[] getCassandraHosts() {
        return cassandraHosts;
    }

    /**
     * Set cassandra hosts.
     * @param cassandraHosts an array of String containing cassandra hosts.
     */
    public void setCassandraHosts(String[] cassandraHosts) {
        this.cassandraHosts = Arrays.copyOf(cassandraHosts, cassandraHosts.length);
    }

    /**
     * Get cassandra port.
     * @return current cassandra port.
     */
    public int getCassandraPort() {
        return cassandraPort;
    }

    /**
     * Set cassandra port.
     * @param cassandraPort Port of cassandra (CQL).
     */
    public void setCassandraPort(int cassandraPort) {
        this.cassandraPort = cassandraPort;
    }

    /**
     * Get Spark Master URL.
     * @return Spark Master URL in a String.
     */
    public String getSparkMaster(){
        return sparkMaster;
    }

    /**
     * Set Spark Master URL.
     * @param sparkMaster Spark Master URL spark://HOST:PORT/
     */
    public void setSparkMaster(String sparkMaster){
        this.sparkMaster=sparkMaster;
    }

    /**
     * Get the default Job Name in Spark.
     * @return the job name.
     */
    public String getJobName(){
        return JOBNAME;
    }


    /**
     * Get cassandra host randomly.
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
            LOG.error("Spark classpath null or incorrect directory");
        }
    }

    /**
     * Check if a .jar is forbidden or not depending on {@link EngineConfig#forbiddenJars}.
     *
     * @param jar .jar to check
     * @return {@code true} if is not forbidden.
     */
    private boolean filterJars(String jar){
        for (String forbiddenJar : forbiddenJars) {
            if (jar.startsWith(forbiddenJar)) {
                return false;
            }
        }
        return true;
    }
}
