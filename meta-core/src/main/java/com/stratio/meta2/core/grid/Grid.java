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

import org.jgroups.Channel;

import java.io.Closeable;
import java.util.concurrent.locks.Lock;

/**
 * Class providing several in-memory data grid artifacts, as distributed data stores, locks and
 * broadcast channels.
 */
public class Grid implements Closeable {

  private final ChannelService channelService;
  private final StoreService storeService;
  private final LockService lockService;

  /**
   * Builds a new {@link com.stratio.meta2.core.grid.Grid} using the provided {@link
   * com.stratio.meta2.core.grid.ChannelService}, {@link com.stratio.meta2.core.grid.StoreService}
   * and {@link com.stratio.meta2.core.grid.LockService}.
   *
   * @param channelService the distributed broadcast channel service
   * @param storeService   the distributed store service
   * @param lockService    the distributed lock service
   */
  public Grid(ChannelService channelService, StoreService storeService, LockService lockService) {
    this.channelService = channelService;
    this.storeService = storeService;
    this.lockService = lockService;
  }

  /**
   * Returns a distributed {@link com.stratio.meta2.core.grid.Store} with the specified name. It
   * returns any existent instance or, otherwise, a new one.
   *
   * @param name the {@link com.stratio.meta2.core.grid.Store}'s name
   * @return a distributed {@link com.stratio.meta2.core.grid.Store} with the specified name.
   */
  public Store store(String name) {
    return storeService.build(name);
  }

  /**
   * Returns a distributed {@link java.util.concurrent.locks.Lock} with the specified name. It
   * returns any existent instance or, otherwise, a new one.
   *
   * @param name the {@link java.util.concurrent.locks.Lock}'s name
   * @return a distributed {@link java.util.concurrent.locks.Lock} with the specified name
   */
  public Lock lock(String name) {
    return lockService.build(name);
  }

  /**
   * Returns a distributed {@link org.jgroups.Channel} with the specified name. It returns any
   * existent instance or, otherwise, a new one.
   *
   * @param name the {@link org.jgroups.Channel}'s name
   * @return a distributed {@link org.jgroups.Channel} with the specified name
   */
  public Channel channel(String name) {
    return channelService.build(name);
  }

  /**
   * Closes this and all its created distributed objects.
   */
  @Override
  public void close() {
    storeService.close();
    lockService.close();
    channelService.close();
  }

}
