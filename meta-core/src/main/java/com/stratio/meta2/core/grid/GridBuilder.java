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

import org.jgroups.JChannel;
import org.jgroups.protocols.BARRIER;
import org.jgroups.protocols.CENTRAL_LOCK;
import org.jgroups.protocols.FD_ALL;
import org.jgroups.protocols.FD_SOCK;
import org.jgroups.protocols.FRAG2;
import org.jgroups.protocols.MERGE2;
import org.jgroups.protocols.MFC;
import org.jgroups.protocols.TCP;
import org.jgroups.protocols.TCPPING;
import org.jgroups.protocols.UFC;
import org.jgroups.protocols.UNICAST2;
import org.jgroups.protocols.VERIFY_SUSPECT;
import org.jgroups.protocols.pbcast.GMS;
import org.jgroups.protocols.pbcast.NAKACK;
import org.jgroups.protocols.pbcast.STABLE;
import org.jgroups.stack.IpAddress;
import org.jgroups.stack.ProtocolStack;

import java.util.ArrayList;
import java.util.List;

public class GridBuilder {

  private static final String DEFAULT_LISTEN_HOST = "localhost";
  private static final int DEFAULT_LISTEN_PORT = 7800;

  private IpAddress listenAddress;
  private List<IpAddress> contactPoints = new ArrayList<>();
  private int minInitialMembers;
  private long timeout;
  private String path;

  public GridBuilder withListenAddress(String address, int port) {
    listenAddress = ChannelServiceBuilder.ip(address, port);
    return this;
  }

  public GridBuilder withContactPoint(String address, int port) {
    contactPoints.add(ChannelServiceBuilder.ip(address, port));
    return this;
  }

  public GridBuilder withMinInitialMembers(int minInitialMembers) {
    this.minInitialMembers = minInitialMembers;
    return this;
  }

  public GridBuilder withJoinTimeoutInMs(long timeout) {
    this.timeout = timeout;
    return this;
  }

  public GridBuilder withPersistencePath(String path) {
    this.path = path;
    return this;
  }

  public Grid build() {

    if (listenAddress == null) {
      listenAddress = ChannelServiceBuilder.ip(DEFAULT_LISTEN_HOST, DEFAULT_LISTEN_PORT);
    }

    List<IpAddress> initialHosts = new ArrayList<>(contactPoints);
    if (!initialHosts.contains(listenAddress)) {
      initialHosts.add(listenAddress);
    }

    TCP tcp = new TCP();
    tcp.setBindAddress(listenAddress.getIpAddress());
    tcp.setBindPort(listenAddress.getPort());
    tcp.setThreadPoolMaxThreads(30);
    tcp.setOOBThreadPoolMaxThreads(30);

    TCPPING tcpping = new TCPPING();
    tcpping.setInitialHosts(initialHosts);
    tcpping.setPortRange(1);
    tcpping.setNumInitialMembers(minInitialMembers);
    tcpping.setTimeout(timeout);

    JChannel channel = new JChannel(false);
    ProtocolStack stack = new ProtocolStack();
    channel.setProtocolStack(stack);

    stack.addProtocol(tcp);
    stack.addProtocol(tcpping);
    stack.addProtocol(new CENTRAL_LOCK());
    // stack.addProtocol(new PING()); // Alternative to TCPPING
    stack.addProtocol(new MERGE2());
    stack.addProtocol(new FD_SOCK());
    stack.addProtocol(new FD_ALL().setValue("timeout", 12000));
    stack.addProtocol(new VERIFY_SUSPECT());
    stack.addProtocol(new BARRIER());
    stack.addProtocol(new NAKACK());
    stack.addProtocol(new UNICAST2());
    stack.addProtocol(new STABLE());
    stack.addProtocol(new GMS());
    stack.addProtocol(new UFC());
    stack.addProtocol(new MFC());
    stack.addProtocol(new FRAG2());

    try {
      stack.init();
    } catch (Exception e) {
      throw new RuntimeException("Unable to create channel", e);
    }

    JChannel lockChannel = ChannelService.build(channel, "lock");
    JChannel storeChannel = ChannelService.build(channel, "store");
    JChannel broadcastChannel = ChannelService.build(channel, "broadcast");

    StoreService storeService = new StoreService(storeChannel, "store", path);
    LockService lockService = new LockService(lockChannel);
    ChannelService channelService = new ChannelService(broadcastChannel);

    return new Grid(channelService, storeService, lockService);
  }

}
