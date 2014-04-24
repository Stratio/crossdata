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

import java.util.Arrays;
import java.util.Random;

public class EngineConfig {
    private String jobName = "stratioDeepWithMeta";
    private String [] cassandraHosts;
    private int cassandraPort;
    private String sparkMaster;

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
}
