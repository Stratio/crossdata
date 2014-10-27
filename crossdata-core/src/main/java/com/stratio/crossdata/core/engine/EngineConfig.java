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

package com.stratio.crossdata.core.engine;

import java.util.Arrays;

public class EngineConfig {

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
     * returns the address where infinispan is listening.
     * @return A String representation of the address.
     */
    public String getGridListenAddress() {
        return gridListenAddress;
    }

    /**
     * sets the address where infinispan is listening.
     * @param gridListenAddress The address.
     */
    public void setGridListenAddress(String gridListenAddress) {
        this.gridListenAddress = gridListenAddress;
    }

    /**
     * returns the hosts where infinispan is working.
     * @return An array of host addresses.
     */
    public String[] getGridContactHosts() {
        return gridContactHosts.clone();
    }

    /**
     * sets the hosts where infinispan is working.
     * @param gridContactHosts The array of contact hosts.
     */
    public void setGridContactHosts(String[] gridContactHosts) {
        this.gridContactHosts = Arrays.copyOf(gridContactHosts, gridContactHosts.length);
    }

    /**
     * returns the port infinispan is listening to.
     * @return The port.
     */
    public int getGridPort() {
        return gridPort;
    }

    /**
     * returns the port infinispan is listening to.
     * @param gridPort The port.
     */
    public void setGridPort(int gridPort) {
        this.gridPort = gridPort;
    }

    /**
     * returns server-application.conf's crossdata-server.config.grid.min-initial-members value.
     * @return The minimum number of initial members.
     */
    public int getGridMinInitialMembers() {
        return gridMinInitialMembers;
    }

    /**
     * overrides server-application.conf's crossdata-server.config.grid.min-initial-members value.
     * @param gridMinInitialMembers The minimum number of initial members.
     */
    public void setGridMinInitialMembers(int gridMinInitialMembers) {
        this.gridMinInitialMembers = gridMinInitialMembers;
    }

    /**
     * returns server-application.conf's crossdata-server.config.grid.join-timeout value.
     * @return The timeout.
     */
    public long getGridJoinTimeout() {
        return gridJoinTimeout;
    }

    /**
     * Overrides server-application.conf's crossdata-server.config.grid.join-timeout value.
     */
    public void setGridJoinTimeout(long gridJoinTimeout) {
        this.gridJoinTimeout = gridJoinTimeout;
    }

    /**
     * Returns the path where infinispan stores its data.
     * @return The persistence path.
     */
    public String getGridPersistencePath() {
        return gridPersistencePath;
    }

    /**
     * Sets the path where infinispan stores its data.
     * @param gridPersistencePath The persistence path.
     */
    public void setGridPersistencePath(String gridPersistencePath) {
        this.gridPersistencePath = gridPersistencePath;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EngineConfig{");
        sb.append(", gridListenAddress='").append(gridListenAddress).append('\'');
        sb.append(", gridContactHosts=").append(Arrays.toString(gridContactHosts));
        sb.append(", gridPort=").append(gridPort);
        sb.append(", gridMinInitialMembers=").append(gridMinInitialMembers);
        sb.append(", gridJoinTimeout=").append(gridJoinTimeout);
        sb.append(", gridPersistencePath='").append(gridPersistencePath).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
