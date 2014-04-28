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
    private String jobName = "stratioDeepWithMeta";
    private String [] cassandraHosts;
    private int cassandraPort;
    private String sparkMaster;
    private List<String> jars;
    private static String [] forbiddenJars = {"akka"};

    private static final Logger LOG = Logger.getLogger(EngineConfig.class.getName());

    public String[] getCassandraHosts() {
        return cassandraHosts;
    }

    public void setCassandraHosts(String[] cassandraHosts) {
        this.cassandraHosts = Arrays.copyOf(cassandraHosts, cassandraHosts.length);
    }

    public int getCassandraPort() {
        return cassandraPort;
    }

    public void setCassandraPort(int cassandraPort) {
        this.cassandraPort = cassandraPort;
    }

    public String getSparkMaster(){
        return sparkMaster;
    }

    public void setSparkMaster(String sparkMaster){
        this.sparkMaster=sparkMaster;
    }

    public String getJobName(){
        return jobName;
    }

    public void setJobName(String jobName){
        this.jobName=jobName;
    }

    public String getRandomCassandraHost(){
        Random rand = new Random();
        return cassandraHosts[rand.nextInt(cassandraHosts.length)];
    }


    public List<String> getJars(){
        return jars;
    }

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

    private boolean filterJars(String jar){
        for (String forbiddenJar : forbiddenJars) {
            if (jar.startsWith(forbiddenJar))
                return false;
        }
        return true;
    }
}
