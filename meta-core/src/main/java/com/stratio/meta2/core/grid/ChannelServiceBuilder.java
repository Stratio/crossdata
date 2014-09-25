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

package com.stratio.meta2.core.grid;

import java.util.ArrayList;
import java.util.List;

import org.jgroups.stack.IpAddress;

/**
 * Builder for create new {@link com.stratio.meta2.core.grid.ChannelService}s.
 */
public class ChannelServiceBuilder {

    public static final String DEFAULT_LISTEN_HOST = "localhost";
    private String listenAddress = DEFAULT_LISTEN_HOST;
    public static final int DEFAULT_PORT = 7800;
    private int port = DEFAULT_PORT;
    public static final int DEFAULT_INITIAL_HOSTS = 1;
    private int minInitialMembers = DEFAULT_INITIAL_HOSTS;
    public static final long DEFAULT_TIMEOUT = 3000;
    private long timeout = DEFAULT_TIMEOUT;
    private List<String> contactPoints = new ArrayList<>();

    /**
     * Returns a {@link ChannelServiceBuilder} using the specified listen address.
     *
     * @param address the listen address
     * @return a {@link ChannelServiceBuilder} using the specified listen address
     */
    public ChannelServiceBuilder withListenAddress(String address) {
        listenAddress = address;
        return this;
    }

    /**
     * Returns a {@link ChannelServiceBuilder} using the specified contact point added to the existing
     * ones.
     *
     * @param address the contact point address
     * @return a {@link ChannelServiceBuilder} using the specified contact point
     */
    public ChannelServiceBuilder withContactPoint(String address) {
        contactPoints.add(address);
        return this;
    }

    /**
     * Returns a {@link ChannelServiceBuilder} using the specified contact points added to the
     * existing ones.
     *
     * @param addresses the contact points
     * @return a {@link ChannelServiceBuilder} using the specified contact point
     */
    public ChannelServiceBuilder withContactPoints(List<String> addresses) {
        contactPoints.addAll(addresses);
        return this;
    }

    public ChannelServiceBuilder withPort(int port) {
        this.port = port;
        return this;
    }

    /**
     * Returns a {@link ChannelServiceBuilder} using the specified minimum number of initial members.
     *
     * @param minInitialMembers the minimum number of initial members
     * @return a {@link ChannelServiceBuilder} using the specified minimum number of initial members
     */
    public ChannelServiceBuilder withMinInitialMembers(int minInitialMembers) {
        this.minInitialMembers = minInitialMembers;
        return this;
    }

    /**
     * Returns a {@link ChannelServiceBuilder} using the specified cluster join timeout.
     *
     * @param timeout the specified cluster join timeout in milliseconds
     * @return a {@link ChannelServiceBuilder} using the specified cluster join timeout
     */
    public ChannelServiceBuilder withJoinTimeoutInMs(long timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * Returns a new {@link com.stratio.meta2.core.grid.ChannelService}.
     *
     * @return a new {@link com.stratio.meta2.core.grid.ChannelService}
     */
    public ChannelService build() {
        try {
            IpAddress listenIp = new IpAddress(listenAddress, port);
            List<IpAddress> contactIps = new ArrayList<>(contactPoints.size());
            for (String contactPoint : contactPoints) {
                contactIps.add(new IpAddress(contactPoint, port));
            }

            if (!contactIps.contains(listenIp)) {
                contactIps.add(listenIp);
            }

            return new ChannelService(listenIp, contactIps, minInitialMembers, timeout);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
