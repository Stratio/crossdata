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

import org.jgroups.stack.IpAddress;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder for create new {@link com.stratio.meta2.core.grid.ChannelService}s.
 */
public class ChannelServiceBuilder {

  public static final String DEFAULT_LISTEN_HOST = "localhost";
  public static final int DEFAULT_LISTEN_PORT = 7800;
  public static final int DEFAULT_INITIAL_HOSTS = 1;
  public static final long DEFAULT_TIMEOUT = 3000;

  private IpAddress listenAddress;
  private List<IpAddress> contactPoints = new ArrayList<>();
  private int minInitialMembers = DEFAULT_INITIAL_HOSTS;
  private long timeout = DEFAULT_TIMEOUT;

  /**
   * Returns a {@link ChannelServiceBuilder} using the specified listen address.
   *
   * @param address the listen address
   * @param port    the listen port
   * @return a {@link ChannelServiceBuilder} using the specified listen address
   */
  public ChannelServiceBuilder withListenAddress(String address, int port) {
    listenAddress = ip(address, port);
    return this;
  }

  /**
   * Returns a {@link ChannelServiceBuilder} using the specified contact point added to the existing
   * ones.
   *
   * @param address the contact point address
   * @param port    the contact point port
   * @return a {@link ChannelServiceBuilder} using the specified contact point
   */
  public ChannelServiceBuilder withContactPoint(String address, int port) {
    contactPoints.add(ip(address, port));
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
   *  Returns a {@link ChannelServiceBuilder} using the specified cluster join timeout.
   * @param timeout the specified cluster join timeout in milliseconds
   * @return a {@link ChannelServiceBuilder} using the specified cluster join timeout
   */
  public ChannelServiceBuilder withJoinTimeoutInMs(long timeout) {
    this.timeout = timeout;
    return this;
  }

  /**
   * Returns a new {@link com.stratio.meta2.core.grid.ChannelService}.
   * @return a new {@link com.stratio.meta2.core.grid.ChannelService}
   */
  public ChannelService build() {
    try {
      if (listenAddress == null) {
        listenAddress = new IpAddress(DEFAULT_LISTEN_HOST, DEFAULT_LISTEN_PORT);
      }

      List<IpAddress> initialHosts = new ArrayList<>(contactPoints);
      if (!initialHosts.contains(listenAddress)) {
        initialHosts.add(listenAddress);
      }

      return new ChannelService(listenAddress, initialHosts, minInitialMembers, timeout);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Returns a new {@link org.jgroups.stack.IpAddress} composed by the specified host name and port.
   * @param host the host name
   * @param port the host port
   * @return a new {@link org.jgroups.stack.IpAddress} composed by {@code host} and {@code port}
   */
  static IpAddress ip(String host, int port) {
    try {
      return new IpAddress(host, port);
    } catch (UnknownHostException e) {
      String msg = String.format("Unable to build IpAddress from %s-%d", host, port);
      throw new GridException(msg, e);
    }
  }

}
