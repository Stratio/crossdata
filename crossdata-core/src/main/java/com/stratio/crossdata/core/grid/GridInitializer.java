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

package com.stratio.crossdata.core.grid;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jgroups.JChannel;

/**
 * Builder for creating a new {@link Grid}.
 */
public class GridInitializer {

    private static final String DEFAULT_LISTEN_HOST = "localhost";
    private String listenAddress = DEFAULT_LISTEN_HOST;
    private static final int DEFAULT_PORT = 7800;
    private int port = DEFAULT_PORT;
    private List<String> contactPoints = new ArrayList<>();
    private int minInitialMembers;
    private long timeout;
    private String path;
    private static final Logger LOG = Logger.getLogger(GridInitializer.class);

    /**
     * Package constructor.
     */
    GridInitializer() {
        //TODO
        LOG.info(" ---> CREATING GRID INITIALIZER");
    }

    public GridInitializer withPort(int port) {
        this.port = port;
        return this;
    }

    /**
     * Returns a {@link GridInitializer} using the specified local listen address for the service.
     *
     * @param address the listen address host
     * @return a {@link GridInitializer} using the specified local listen address for the service
     */
    public GridInitializer withListenAddress(String address) {
        listenAddress = address;
        return this;
    }

    /**
     * Returns a {@link GridInitializer} using the specified contact point in addition  to the
     * existing ones.
     *
     * @param address the contact point host
     * @return a {@link GridInitializer} using the specified contact point in addition  to the
     * existing ones
     */
    public GridInitializer withContactPoint(String address) {
        contactPoints.add(address);
        return this;
    }

    /**
     * Returns a {@link GridInitializer} using the specified minimum number of initial members.
     *
     * @param minInitialMembers the minimum number of initial members
     * @return a {@link GridInitializer} using the specified minimum number of initial members
     */
    public GridInitializer withMinInitialMembers(int minInitialMembers) {
        this.minInitialMembers = minInitialMembers;
        return this;
    }

    /**
     * Returns a {@link GridInitializer} using the specified cluster join timeout.
     *
     * @param timeout the specified cluster join timeout in milliseconds
     * @return a {@link GridInitializer} using the specified cluster join timeout
     */
    public GridInitializer withJoinTimeoutInMs(long timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * Returns a {@link GridInitializer} using the specified files persistence path.
     *
     * @param path the files persistence path
     * @return a {@link GridInitializer} using the specified files persistence path
     */
    public GridInitializer withPersistencePath(String path) {
        this.path = path;
        return this;
    }

    /**
     * Returns the new {@link Grid} defined by this.
     *
     * @return the new {@link Grid} defined by this.
     */
    public void init() {

        ChannelService channelService = new ChannelServiceBuilder()
                .withPort(port)
                .withListenAddress(listenAddress)
                .withContactPoints(contactPoints)
                .withMinInitialMembers(minInitialMembers)
                .withJoinTimeoutInMs(timeout)
                .build();

        JChannel storeChannel = channelService.build("store");
        JChannel lockChannel = channelService.build("lock");

        StoreService storeService = new StoreService(storeChannel, "store", path);
        LockService lockService = new LockService(lockChannel);

        Grid.INSTANCE.init(channelService, lockService, storeService);
    }

}
