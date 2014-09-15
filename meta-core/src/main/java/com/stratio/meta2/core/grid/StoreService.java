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

import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.remoting.transport.jgroups.JGroupsTransport;
import org.infinispan.transaction.LockingMode;
import org.infinispan.transaction.TransactionMode;
import org.infinispan.transaction.lookup.GenericTransactionManagerLookup;
import org.infinispan.util.concurrent.IsolationLevel;
import org.jgroups.JChannel;

import java.io.Closeable;
import java.util.concurrent.TimeUnit;

/**
 * A {@link Store} factory/manager. The created {@link Store}s are based in an Infinispan cache
 * without eviction in REPL_SYNC mode and persisted in local files.
 *
 * It must be closed ({@link #close()}) when its created {@link Store}s are not needed anymore.
 */
public class StoreService implements Closeable {

  private final JChannel channel;
  private final Configuration config;
  private final EmbeddedCacheManager manager;

  /**
   * Builds a new {@link com.stratio.meta2.core.grid.StoreService}.
   *
   * @param channel     the use JGroups channel
   * @param clusterName the cluster name
   * @param path        the persistence path
   */
  StoreService(JChannel channel, String clusterName, String path) {
    this.channel = channel;
    JGroupsTransport transport = new JGroupsTransport(channel);
    GlobalConfiguration gc = new GlobalConfigurationBuilder().transport()
        .transport(transport)
        .clusterName(clusterName)
        .build();
    config = new ConfigurationBuilder().transaction()
        .transactionManagerLookup(new GenericTransactionManagerLookup())
        .transactionMode(TransactionMode.TRANSACTIONAL)
        .autoCommit(false)
        .useSynchronization(true)
        .syncCommitPhase(true)
        .syncRollbackPhase(true)
        .cacheStopTimeout(1, TimeUnit.MINUTES)
        .lockingMode(LockingMode.OPTIMISTIC)
        .locking()
        .lockAcquisitionTimeout(1, TimeUnit.MINUTES)
        .useLockStriping(false)
        .concurrencyLevel(500)
        .writeSkewCheck(false)
        .isolationLevel(IsolationLevel.READ_COMMITTED)
        .eviction()
        .strategy(EvictionStrategy.NONE)
        .maxEntries(-1)
        .clustering()
        .cacheMode(CacheMode.REPL_SYNC)
        .persistence()
        .addSingleFileStore()
        .shared(false)
        .location(path)
        .maxEntries(-1)
        .build();
    manager = new DefaultCacheManager(gc, config);
  }

  /**
   * Returns a {@link Store} associated to the specified name. It returns the existent instance (if
   * any).
   *
   * @param name the name of the {@link Store} to be returned.
   * @return the {@link Store} associated to the specified name.
   */
  public Store build(String name) {

    manager.defineConfiguration(name, config);

    Cache<String, String> cache = manager.getCache(name);
    return new Store(cache.getAdvancedCache());
  }

  /**
   * Closes this and all the created {@link Store}s.
   */
  @Override
  public void close() {
    manager.stop();
    channel.disconnect();
  }
}
