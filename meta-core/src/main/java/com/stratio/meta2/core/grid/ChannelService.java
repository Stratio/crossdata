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
import org.jgroups.fork.ForkChannel;
import org.jgroups.protocols.BARRIER;
import org.jgroups.protocols.CENTRAL_LOCK;
import org.jgroups.protocols.FD_ALL;
import org.jgroups.protocols.FD_SOCK;
import org.jgroups.protocols.FRAG2;
import org.jgroups.protocols.MERGE2;
import org.jgroups.protocols.MFC;
import org.jgroups.protocols.TCP;
import org.jgroups.protocols.TCPPING;
import org.jgroups.protocols.UNICAST2;
import org.jgroups.protocols.VERIFY_SUSPECT;
import org.jgroups.protocols.pbcast.GMS;
import org.jgroups.protocols.pbcast.NAKACK;
import org.jgroups.protocols.pbcast.STABLE;
import org.jgroups.stack.IpAddress;
import org.jgroups.stack.ProtocolStack;

import java.io.Closeable;
import java.util.List;

/**
 * A distributed JGroups {@link org.jgroups.JChannel} factory/manager. The provided channels are
 * useful for broadcast communications between cluster members.
 *
 * It must be closed ({@link #close()}) when its created {@link com.stratio.meta2.core.grid.StoreService}s are not needed anymore.
 */
public class ChannelService implements Closeable {

  private final JChannel channel;

  /**
   * Builds a new {@link com.stratio.meta2.core.grid.ChannelService} based on the specified JGroups
   * channel. All the created channels will be forked from the specified base channel.
   */
  public ChannelService(JChannel channel) {
    this.channel = channel;
  }

  /**
   * Builds a new {@link com.stratio.meta2.core.grid.ChannelService} using the JGroups channel
   * defined by the specified properties.  All the created channels will be forked from the
   * specified base channel.
   *
   * @param listenAddress     The listen address.
   * @param initialHosts      The other nodes contact addresses.
   * @param minInitialMembers The minimum of required nodes at startup.
   * @param timeout           The base channel timeout in milliseconds.
   */
  public ChannelService(IpAddress listenAddress, List<IpAddress> initialHosts,
                        int minInitialMembers, long timeout) {

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

    channel = new JChannel(false);
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
//    stack.addProtocol(new UFC());
    stack.addProtocol(new MFC());
    stack.addProtocol(new FRAG2());

    try {
      stack.init();
    } catch (Exception e) {
      throw new RuntimeException("Unable to create channel", e);
    }
  }

  /**
   * Returns the base channel.
   *
   * @return the base channel
   */
  public JChannel getBaseChannel() {
    return channel;
  }

  /**
   * Returns a new forked {@link org.jgroups.JChannel} with the specified name.
   *
   * @param name The channel's name.
   * @return A new forked {@link org.jgroups.JChannel} with the specified name.
   */
  public JChannel build(String name) {
    return build(channel, name);
  }

  /**
   * Builds a new {@link org.jgroups.JChannel} forked from the specified JGroups channel and using
   * the specified name.
   *
   * @param channel The base {@link org.jgroups.JChannel}.
   * @param name    The forked channel's name.
   * @return a new {@link org.jgroups.JChannel} forked from {@code channel}.
   */
  static JChannel build(JChannel channel, String name) {
    try {
      if (!channel.isConnected()) {
        channel.connect(name);
      }
      return new ForkChannel(channel, name, name, true, ProtocolStack.ABOVE, FRAG2.class);
    } catch (Exception e) {
      throw new RuntimeException("Unable to fork channel");
    }
  }

  /**
   * Closes this and all this created channels.
   */
  @Override
  public void close() {
    channel.disconnect();
    channel.close();
  }

}

